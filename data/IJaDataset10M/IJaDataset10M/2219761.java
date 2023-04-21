package jaxlib.tcol.tint;

import org.jaxlib.tcol.junit.IntListTestCase;

/**
 * TODO: comment
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: IntArrayTest.java 2271 2007-03-16 08:48:23Z joerg_wassmer $
 */
public final class IntArrayTest extends IntListTestCase {

    public static void main(String[] arg) {
        runSuite(IntArrayTest.class);
    }

    public IntArrayTest(String name) {
        super(name);
    }

    protected IntList createIntList(IntCollection elements) {
        return new IntArray(elements);
    }
}
