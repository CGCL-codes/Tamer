package net.sf.jfling.basetypes;

import org.apache.commons.lang.StringUtils;

/**
 * Copyright 2008 Tom Rigole (tom.rigole@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * 
 * 
 * @author tom.rigole@gmail.com
 * 
 */
public class StringWrapper extends FixedLengthBaseWrapper<String> {

    public static final char DEFAULT_PADDING = ' ';

    public static final char DEFAULT_NULLPADDING = ' ';

    public static final String OBSOLETE_FORMAT = "";

    private String value;

    public StringWrapper(final int length) {
        super(length, OBSOLETE_FORMAT, DEFAULT_PADDING, DEFAULT_NULLPADDING);
    }

    /**
	 * 
	 * @param length
	 * @param format Is the default value which is given to the contained string.
	 */
    public StringWrapper(final int length, final String format) {
        super(length, format, DEFAULT_PADDING, DEFAULT_NULLPADDING);
        setFixedLengthString(format);
    }

    /**
	 * 
	 * @param length
	 * @param format Is the default value which is given to the contained string.
	 * @param padding
	 * @param nullPadding
	 */
    public StringWrapper(final int length, final String format, final char padding, final char nullPadding) {
        super(length, format, padding, nullPadding);
        setFixedLengthString(format);
    }

    public String getFixedLengthString() {
        if (this.value == null) return StringUtils.leftPad("", getLength(), getNullPadding()); else return StringUtils.leftPad(this.value, getLength(), getPadding());
    }

    public String getValue() {
        return this.value;
    }

    public void setFixedLengthString(final String stringValue) {
        if (stringValue.length() != getLength()) throw new IllegalArgumentException("Illegal length for fixed length string wrapper for input string : '" + stringValue + "'."); else {
            this.value = stringValue;
        }
    }

    public void setValue(final String value) {
        if ((value != null) && (value.length() > getLength())) throw new IllegalArgumentException("Can't fit string: '" + value + "' in fixed length field of size '" + getLength() + "'.");
        this.value = value;
    }
}
