package org.dishevelled.matrix;

import junit.framework.TestCase;

/**
 * Unit test for BitMatrix3D.
 *
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public class BitMatrix3DTest extends TestCase {

    public void testConstructor() {
        BitMatrix3D bm0 = new BitMatrix3D(0, 0, 0);
        BitMatrix3D bm1 = new BitMatrix3D(1, 0, 0);
        BitMatrix3D bm2 = new BitMatrix3D(0, 1, 0);
        BitMatrix3D bm3 = new BitMatrix3D(0, 0, 1);
        BitMatrix3D bm4 = new BitMatrix3D(1, 1, 1);
        BitMatrix3D bm5 = new BitMatrix3D(100, 100, 100);
        try {
            BitMatrix3D bm = new BitMatrix3D(-1, 0, 0);
            fail("ctr(-1,,) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            BitMatrix3D bm = new BitMatrix3D(0, -1, 0);
            fail("ctr(,-1,) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            BitMatrix3D bm = new BitMatrix3D(0, 0, -1);
            fail("ctr(,,-1) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSize() {
    }

    public void testCardinality() {
    }

    public void testGetSetClearFlip() {
    }

    public void testIntersects() {
    }

    public void testIntersectsParams() {
    }

    public void testAnd() {
    }

    public void testAndParams() {
    }

    public void testAndNot() {
    }

    public void testAndNotParams() {
    }

    public void testOr() {
    }

    public void testOrParams() {
    }

    public void testXor() {
    }

    public void testXorParams() {
    }
}
