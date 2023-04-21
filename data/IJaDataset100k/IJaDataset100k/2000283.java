package org.openfast;

import org.openfast.test.OpenFastTestCase;

public class ByteVectorValueTest extends OpenFastTestCase {

    public void testEquals() {
        ByteVectorValue expected = new ByteVectorValue(new byte[] { (byte) 0xff });
        ByteVectorValue actual = new ByteVectorValue(new byte[] { (byte) 0xff });
        assertEquals(expected, actual);
    }

    public void testSerialize() {
        byte[] bytes = ByteUtil.convertHexStringToByteArray("DDBBCCAA");
        ByteVectorValue val = new ByteVectorValue(bytes);
        assertEquals("ddbbccaa", val.serialize());
    }
}
