/*
 * Copyright 2013 Mario Arias
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.funktionale.partials

import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 31/10/16
 * Time: 12:00 AM
 */
class PartialFunctionTest {
    val definetAt: (Int) -> Boolean = { it.mod(2) == 0 }
    val body: (Int) -> String = {
        if (definetAt(it)) "is even"
        else throw IllegalArgumentException()
    }

    @Test
    fun partial() {
        val isEven = PartialFunction(definetAt, body)

        assertTrue(isEven.isDefinedAt(2))
        assertEquals(isEven(2), "is even")
    }

    @Test fun toPartialFunction() {
        val isEven = body.toPartialFunction(definetAt)
        assertTrue(isEven.isDefinedAt(2))
        assertEquals(isEven(2), "is even")
    }

    @Test fun orElse() {
        val isEven = body.toPartialFunction(definetAt)
        val isOdd = { i: Int -> if (!definetAt(i)) "is odd" else throw IllegalArgumentException() }.toPartialFunction { !definetAt(it) }
        assertEquals(listOf(1, 2, 3).map(isEven orElse isOdd), listOf("is odd", "is even", "is odd"))
    }

    @Test fun invokeOrElse() {
        val isEven = body.toPartialFunction(definetAt)
        assertEquals(listOf(1, 2, 3).map { isEven.invokeOrElse(it, "is odd") }, listOf("is odd", "is even", "is odd"))
    }
}