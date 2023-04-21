package iso;

import junit.framework.TestCase;
import org.jpos.iso.IFB_LLLHBINARY;
import org.jpos.iso.ISOBinaryField;

/**
 * @author joconnor
 */
public class IFB_LLLHBINARYTest extends TestCase {

    public void testPack() throws Exception {
        ISOBinaryField field = new ISOBinaryField(12, new byte[] { 0x12, 0x34 });
        IFB_LLLHBINARY packager = new IFB_LLLHBINARY(4, "Should be 1234");
        TestUtils.assertEquals(new byte[] { 0x00, 0x02, 0x12, 0x34 }, packager.pack(field));
    }

    public void testUnpack() throws Exception {
        byte[] raw = new byte[] { 0x00, 0x02, 0x12, 0x34 };
        IFB_LLLHBINARY packager = new IFB_LLLHBINARY(4, "Should be 1234");
        ISOBinaryField field = new ISOBinaryField(12);
        packager.unpack(field, raw, 0);
        TestUtils.assertEquals(new byte[] { 0x12, 0x34 }, (byte[]) field.getValue());
    }

    public void testReversability() throws Exception {
        byte[] origin = new byte[] { 0x12, 0x34, 0x56, 0x78 };
        ISOBinaryField f = new ISOBinaryField(12, origin);
        IFB_LLLHBINARY packager = new IFB_LLLHBINARY(10, "Should be 12345678");
        ISOBinaryField unpack = new ISOBinaryField(12);
        packager.unpack(unpack, packager.pack(f), 0);
        TestUtils.assertEquals(origin, (byte[]) unpack.getValue());
    }
}
