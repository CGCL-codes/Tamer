package net.sf.jfling;

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
public interface FixedLengthWrapper<T> {

    /**
	 * Sets the fixed length representation of this fixed length string wrapper.
	 * 
	 * @return the number of characters read from the string by this wrapper.
	 * 
	 * @throws IllegalArgumentException: when the input string is too short, or whenever the string format does not conform with the type
	 *             represented by this wrapper. Mind that it will throw no exception when the input string is too long.
	 * 
	 */
    int consumeFixedLengthString(String string);

    /**
	 * Returns the fixed length representation of this fixed length string wrapper.
	 */
    String getFixedLengthString();

    /**
	 * Returns the length the fixed length string representation of this wrapper.
	 */
    int getLength();

    /**
	 * 
	 * @return a tree representation of this fixed length wrapper object
	 */
    String getTreeView();

    /**
	 * Returns the object represented by this wrapper.
	 */
    T getValue();

    /**
	 * Sets the fixed length representation of this fixed length string wrapper.
	 * 
	 * @throws IllegalArgumentException: when the input string is too short or too long, or whenever the string format does not conform with
	 *             the type represented by this wrapper.
	 * 
	 */
    void setFixedLengthString(String string);

    /**
	 * Sets the value of the object represented by this wrapper.
	 * 
	 * @throws IllegalArgumentException if the object does not fit in the string representation of this wrapper.
	 */
    void setValue(T value);
}
