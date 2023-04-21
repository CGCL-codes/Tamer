package net.sf.jfling;

import static org.junit.Assert.assertEquals;
import net.sf.jfling.basetypes.IntWrapper;
import net.sf.jfling.basetypes.StringWrapper;
import org.junit.Test;

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
public class FixedLengthComposedWrapperTest {

    ComposedWrapper wrapper;

    @Test
    public void testGetLength() {
        this.wrapper = new ComposedWrapper();
        assertEquals(6, this.wrapper.getLength());
    }

    @Test
    public void testSetFixedLengthString() {
        this.wrapper = new ComposedWrapper();
        this.wrapper.setFixedLengthString("01" + "BLAH");
        assertEquals(1, this.wrapper.getInt());
        assertEquals("BLAH", this.wrapper.getString());
        assertEquals("01" + "BLAH", this.wrapper.getFixedLengthString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFixedLengthStringIllegalLengthTooShort() {
        this.wrapper = new ComposedWrapper();
        this.wrapper.setFixedLengthString("01" + "BL");
    }
}

class ComposedWrapper extends FixedLengthComposedWrapper<ComposedWrapper> {

    {
        this.wrappers[0] = new IntWrapper(2);
        this.wrappers[1] = new StringWrapper(4);
    }

    public ComposedWrapper() {
        super(2);
    }

    public int getInt() {
        return ((IntWrapper) this.wrappers[0]).getValue();
    }

    public String getString() {
        return ((StringWrapper) this.wrappers[1]).getValue();
    }

    public ComposedWrapper getValue() {
        return null;
    }

    public void setValue(final ComposedWrapper value) {
    }
}
