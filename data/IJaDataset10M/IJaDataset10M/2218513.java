package jaxlib.tcol.tbyte;

import org.jaxlib.tcol.junit.ByteListTestCase;

/**
 * TODO: comment
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: ByteArrayTest.java 2271 2007-03-16 08:48:23Z joerg_wassmer $
 */
public final class ByteArrayTest extends ByteListTestCase {

    public static void main(String[] arg) {
        runSuite(ByteArrayTest.class);
    }

    public ByteArrayTest(String name) {
        super(name);
    }

    protected ByteList createByteList(ByteList elements) {
        return new ByteArray(elements);
    }
}
