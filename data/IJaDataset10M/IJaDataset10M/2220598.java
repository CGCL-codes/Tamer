package net.sf.joafip.store.service.binary;

import java.util.Arrays;
import net.sf.joafip.AbstractJoafipTestCase;
import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;
import net.sf.joafip.store.service.binary.converter.ForTest64BitsConverter;
import net.sf.joafip.store.service.binary.converter.IConverterForTest;

@NotStorableClass
@DoNotTransform
public class Test64BitsConverter extends AbstractJoafipTestCase {

    @NotStorableClass
    private class LongConverter extends Abstract64BitsConverter {

        @Override
        public void fromBinary(final byte[] binary, final int offset) {
            assert assertBound(binary, offset);
            objectFromBinary = fromBinary64Bits(binary, offset);
            offsetFromBinary = offset + byteSize();
            valueDefinedFromBinary = true;
        }

        @Override
        public int toBinary(final byte[] binary, final int offset, final boolean valueDefined, final Object object) throws BinaryConverterException {
            assert assertBound(binary, offset);
            return toBinary64Bits(binary, offset, (Long) object, valueDefined, false, (byte) 0);
        }
    }

    private IConverterForTest converterForTest;

    private LongConverter testedConverter;

    private byte[] binary;

    public Test64BitsConverter() throws TestException {
        super();
    }

    public Test64BitsConverter(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        converterForTest = new ForTest64BitsConverter();
        testedConverter = new LongConverter();
        binary = new byte[testedConverter.byteSize()];
    }

    @Override
    protected void tearDown() throws Exception {
        converterForTest = null;
        testedConverter = null;
        binary = null;
        super.tearDown();
    }

    public void test() throws BinaryConverterException {
        final Long value = Long.valueOf("115465874564584225");
        testedConverter.toBinary(binary, 0, true, value);
        assertTrue("bad to binary conversion", Arrays.equals(Arrays.copyOfRange(binary, 1, 9), converterForTest.toBinary(value)));
        assertEquals("bad flags", (byte) 0xC0, binary[0]);
        testedConverter.fromBinary(binary, 0);
        assertTrue("value must be defined", testedConverter.valueDefinedFromBinary);
        final Long valueRead = (Long) testedConverter.objectFromBinary;
        assertNotNull("value read can not be null", valueRead);
        assertEquals("bad value", value, valueRead);
        assertEquals("bad from binary conversion", converterForTest.fromBinary(Arrays.copyOfRange(binary, 1, 9), 0), valueRead);
    }
}
