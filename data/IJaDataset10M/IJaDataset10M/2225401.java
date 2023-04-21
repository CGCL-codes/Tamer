package jaxlib.io.checksum;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: CrcAlgorithmTest.java 2601 2008-05-15 13:44:11Z joerg_wassmer $
 */
public final class CrcAlgorithmTest extends junit.framework.TestCase {

    public CrcAlgorithmTest(final String name) {
        super(name);
    }

    public void testCrc32Zip() {
        assertEquals(0xcbf43926, Crc.getInstance(CrcAlgorithm.CRC32_ZIP).check());
    }

    public void testReflect() {
        assertEquals(0x3e26, Crc32Bit.reflect(0x3e23, 3));
    }
}
