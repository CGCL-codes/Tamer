package com.jeantessier.classreader.impl;

import org.jmock.*;
import com.jeantessier.classreader.*;

public class TestCharConstantElementValue extends TestAnnotationsBase {

    private static final int CONST_VALUE_INDEX = 2;

    private static final int CONST_VALUE = 'a';

    private CharConstantElementValue sut;

    protected void setUp() throws Exception {
        super.setUp();
        expectReadU2(CONST_VALUE_INDEX);
        expectLookupInteger(CONST_VALUE_INDEX, CONST_VALUE, "lookup during construction");
        sut = new CharConstantElementValue(mockConstantPool, mockIn);
    }

    public void testGetConstValue() {
        expectLookupInteger(CONST_VALUE_INDEX, CONST_VALUE);
        assertEquals(CONST_VALUE, sut.getConstValue());
    }

    public void testGetTag() {
        assertEquals(ElementValueType.CHAR.getTag(), sut.getTag());
    }

    public void testAccept() {
        final Visitor mockVisitor = mock(Visitor.class);
        checking(new Expectations() {

            {
                one(mockVisitor).visitCharConstantElementValue(sut);
            }
        });
        sut.accept(mockVisitor);
    }
}
