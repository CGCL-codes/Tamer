package com.jeantessier.classreader.impl;

import org.jmock.*;
import com.jeantessier.classreader.*;

public class TestParameter extends TestAttributeBase {

    private Parameter sut;

    protected void setUp() throws Exception {
        super.setUp();
        expectReadNumAnnotations(0);
        sut = new Parameter(mockConstantPool, mockIn);
    }

    public void testAccept() throws Exception {
        final Visitor mockVisitor = mock(Visitor.class);
        checking(new Expectations() {

            {
                one(mockVisitor).visitParameter(sut);
            }
        });
        sut.accept(mockVisitor);
    }
}
