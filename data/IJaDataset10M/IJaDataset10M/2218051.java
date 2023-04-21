/*******************************************************************************
 * $Id: EnumInitializationException.java 137 2003-07-08 16:11:03Z pgmjsd $
 * $Author: pgmjsd $
 * $Date: 2003-07-08 12:11:03 -0400 (Tue, 08 Jul 2003) $
 *
 * Copyright 2002 - YAJUL Developers, Joshua Davis, Kent Vogel.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 ******************************************************************************/

package org.yajul.enum;

import org.yajul.util.DetailedException;

/**
 * A handled exception that is thrown when an enumerated type cannot be initialized.
 * User: jdavis
 * Date: Jul 7, 2003
 * Time: 2:09:00 PM
 * @author jdavis
 */
public class EnumInitializationException extends DetailedException
{
    /**
     * Creates a new exception with no nested exception and the specified detail message.
     * @param s the detail message.
     */
    public EnumInitializationException(String s)
    {
        super(s);
    }

    /**
     * Constructs a DetailedException with the specified detail message and
     * the specified throwable as the nested exception.
     * @param s the detail message.
     * @param t an object of type Throwable
     */
    public EnumInitializationException(String s, Throwable t)
    {
        super(s, t);
    }
}
