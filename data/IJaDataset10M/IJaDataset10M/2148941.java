package com.jeantessier.classreader.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.DataInput;
import java.io.IOException;
import static org.hamcrest.Matchers.is;
import static org.jmock.Expectations.aNull;
import static org.junit.Assert.assertThat;

@RunWith(JMock.class)
public class TestField_info {

    private static final int TEST_ACCESS_FLAG = 0x0000;

    private static final int TEST_NAME_INDEX = 123;

    private static final int TEST_SIGNATURE_INDEX = 456;

    private Mockery context;

    private DataInput mockIn;

    private ConstantPool mockConstantPool;

    private Sequence dataReads;

    @Before
    public void setUp() {
        context = new Mockery();
        context.setImposteriser(ClassImposteriser.INSTANCE);
        mockIn = context.mock(DataInput.class);
        mockConstantPool = context.mock(ConstantPool.class);
        dataReads = context.sequence("dataReads");
    }

    @Test
    public void testGetConstantValue_noAttributes_shouldReturnNull() throws IOException {
        final Classfile mockClassfile = context.mock(Classfile.class);
        context.checking(new Expectations() {

            {
                allowing(mockClassfile).getConstantPool();
                will(returnValue(mockConstantPool));
            }
        });
        expectReadU2(TEST_ACCESS_FLAG);
        expectReadU2(TEST_NAME_INDEX);
        expectLookupUtf8(TEST_NAME_INDEX, "foo", "name");
        expectReadU2(TEST_SIGNATURE_INDEX);
        expectLookupUtf8(TEST_SIGNATURE_INDEX, "i", "descriptor");
        expectReadU2(0);
        Field_info sut = new Field_info(mockClassfile, mockIn);
        assertThat("getConstantValue()", sut.getConstantValue(), is(aNull(ConstantValue_attribute.class)));
    }

    @Test
    public void testGetConstantValue_oneDifferentAttribute_shouldReturnNull() throws IOException {
        final Classfile mockClassfile = context.mock(Classfile.class);
        int attributeNameIndex = 789;
        context.checking(new Expectations() {

            {
                allowing(mockClassfile).getConstantPool();
                will(returnValue(mockConstantPool));
            }
        });
        expectReadU2(TEST_ACCESS_FLAG);
        expectReadU2(TEST_NAME_INDEX);
        expectLookupUtf8(TEST_NAME_INDEX, "foo", "name");
        expectReadU2(TEST_SIGNATURE_INDEX);
        expectLookupUtf8(TEST_SIGNATURE_INDEX, "i", "descriptor");
        expectReadU2(1);
        expectReadU2(attributeNameIndex);
        expectLookupUtf8(attributeNameIndex, "Synthetic", "Synthetic attribute");
        expectReadU4(0);
        Field_info sut = new Field_info(mockClassfile, mockIn);
        assertThat("getConstantValue()", sut.getConstantValue(), is(aNull(ConstantValue_attribute.class)));
    }

    protected void expectReadU2(final int i) throws IOException {
        context.checking(new Expectations() {

            {
                one(mockIn).readUnsignedShort();
                inSequence(dataReads);
                will(returnValue(i));
            }
        });
    }

    protected void expectReadU4(final int i) throws IOException {
        context.checking(new Expectations() {

            {
                one(mockIn).readInt();
                inSequence(dataReads);
                will(returnValue(i));
            }
        });
    }

    protected void expectLookupUtf8(int index, String value, String mockName) {
        expectLookupUtf8(index, value, context.mock(UTF8_info.class, mockName));
    }

    private void expectLookupUtf8(final int index, final String value, final UTF8_info mockUtf8_info) {
        context.checking(new Expectations() {

            {
                atLeast(1).of(mockConstantPool).get(index);
                will(returnValue(mockUtf8_info));
                atLeast(1).of(mockUtf8_info).getValue();
                will(returnValue(value));
            }
        });
    }
}
