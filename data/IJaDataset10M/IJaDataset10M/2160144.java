package bm.core.math;

import bm.core.io.SerializationException;
import bm.core.io.SerializerOutputStream;
import bm.core.io.SerializerInputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Test suite for FixedPoint
 *
 * @author <a href="mailto:narciso@elondra.com">Narciso Cerezo</a>
 * @version $Revision:4 $
 */
public class FixedPointTest extends TestCase {

    public FixedPointTest() {
        super();
    }

    public FixedPointTest(final String s) {
        super(s);
    }

    public void testParse() {
        final FixedPoint fp = FixedPoint.parse("1.000,43", ',');
        assertEquals(2, fp.getPrecision());
        assertEquals(100043, fp.getRawValue());
        final FixedPoint fp2 = FixedPoint.parse("-43321,3", ',');
        assertEquals(1, fp2.getPrecision());
        assertEquals(-433213, fp2.getRawValue());
    }

    public void testCreate() {
        FixedPoint fp = new FixedPoint(100);
        assertEquals(0, fp.getPrecision());
        assertEquals(100, fp.getRawValue());
        fp = fp.setPrecision(4);
        assertEquals(1000000, fp.getRawValue());
    }

    public void testAddSub() {
        FixedPoint fp1 = FixedPoint.parse("100,4351", ',');
        FixedPoint fp2 = FixedPoint.parse("99,56", ',');
        fp1 = fp1.add(fp2);
        assertEquals(4, fp1.getPrecision());
        assertEquals(1999951, fp1.getRawValue());
        fp1 = fp1.sub(fp2);
        assertEquals(4, fp1.getPrecision());
        assertEquals(1004351, fp1.getRawValue());
        fp2 = fp2.add(fp1);
        assertEquals(4, fp2.getPrecision());
        assertEquals(1999951, fp2.getRawValue());
        fp2 = fp2.sub(fp1);
        assertEquals(4, fp2.getPrecision());
        assertEquals(995600, fp2.getRawValue());
        fp2 = fp2.add(100);
        assertEquals(4, fp2.getPrecision());
        assertEquals(1995600, fp2.getRawValue());
        fp2 = fp2.sub(50);
        assertEquals(4, fp2.getPrecision());
        assertEquals(1495600, fp2.getRawValue());
    }

    public void testMultDiv() {
        FixedPoint fp1 = FixedPoint.parse("100", ',');
        FixedPoint zero = new FixedPoint(0);
        zero = zero.mult(fp1);
        assertEquals(0, zero.getPrecision());
        assertEquals(0, zero.getRawValue());
        FixedPoint fp2 = FixedPoint.parse("4,345", ',');
        fp2 = fp2.mult(fp1);
        assertEquals(3, fp2.getPrecision());
        assertEquals(434500, fp2.getRawValue());
        fp2 = fp2.div(fp1);
        assertEquals(3, fp2.getPrecision());
        assertEquals(4345, fp2.getRawValue());
        FixedPoint fp3 = FixedPoint.parse("60.0", '.');
        FixedPoint fp4 = FixedPoint.parse("2");
        fp4 = fp4.mult(fp3);
        assertEquals("120,0", fp4.toString());
        FixedPoint fp5 = FixedPoint.parse("12", '.');
        fp5 = fp5.mult(FixedPoint.parse("326", '.'));
        fp5 = fp5.mult(FixedPoint.parse("47.7", '.'));
        assertEquals("186602.40", fp5.toString(2, '.', null));
    }

    public void testToString() {
        assertEquals("0", FixedPoint.ZERO.toString());
        assertEquals("0.0", FixedPoint.ZERO.toString(1, '.', null));
        assertEquals("0.00", FixedPoint.ZERO.toString(2, '.', null));
        assertEquals("1.00", FixedPoint.ONE.toString(2, '.', null));
        final FixedPoint small1 = FixedPoint.parse("0.546", '.');
        assertEquals("0,546", small1.toString(',', null));
        assertEquals("0.55", small1.toString(2, '.', null));
        assertEquals("1", small1.toString(0));
        final FixedPoint small2 = FixedPoint.parse("-0.546", '.');
        assertEquals("-0,546", small2.toString(',', null));
        assertEquals("-0.55", small2.toString(2, '.', null));
        assertEquals("-1", small2.toString(0));
        final FixedPoint fp1 = FixedPoint.parse("1342991,321", ',');
        assertEquals("1342991,321", fp1.toString(',', null));
        assertEquals("1.342.991,321", fp1.toString(',', FixedPoint.DOT));
        assertEquals("1,342,991.321", fp1.toString('.', FixedPoint.COMMA));
        assertEquals("1,342,991.32", fp1.toString(2, '.', FixedPoint.COMMA));
        assertEquals("1,342,991", fp1.toString(0, '.', FixedPoint.COMMA));
        final FixedPoint fp2 = FixedPoint.parse("-43,1245", ',');
        assertEquals("-43.1245", fp2.toString('.', FixedPoint.COMMA));
        final FixedPoint fp4 = FixedPoint.parse("-430,1245", ',');
        assertEquals("-430.1245", fp4.toString('.', FixedPoint.COMMA));
        final FixedPoint fp5 = FixedPoint.parse("-1430,1245", ',');
        assertEquals("-1,430.1245", fp5.toString('.', FixedPoint.COMMA));
        final FixedPoint fp3 = FixedPoint.parse("991,321", ',');
        assertEquals("991.321", fp3.toString('.', FixedPoint.COMMA));
        assertEquals("991.32", fp3.toString(2, '.', FixedPoint.COMMA));
    }

    public void testReadWrite() {
        final FixedPoint fp = FixedPoint.parse("12392,4412", ',');
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream bais = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        SerializerOutputStream out = null;
        SerializerInputStream in = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            out = new SerializerOutputStream(dos);
            FixedPoint.serialize(out, fp, false);
            FixedPoint.serialize(out, fp, true);
            FixedPoint.serialize(out, null, true);
            bais = new ByteArrayInputStream(baos.toByteArray());
            dis = new DataInputStream(bais);
            in = new SerializerInputStream(dis);
            final FixedPoint fp1 = FixedPoint.deserialize(in, false);
            assertTrue(fp.equals(fp1));
            final FixedPoint fp2 = FixedPoint.deserialize(in, true);
            assertTrue(fp.equals(fp2));
            final FixedPoint fp3 = FixedPoint.deserialize(in, true);
            assertNull(fp3);
        } catch (SerializationException e) {
            fail(e.getMessage());
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
            try {
                dos.close();
            } catch (Exception e) {
            }
            try {
                baos.close();
            } catch (Exception e) {
            }
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                dis.close();
            } catch (Exception e) {
            }
            try {
                bais.close();
            } catch (Exception e) {
            }
        }
    }

    public void testCompare() {
        final FixedPoint fp1 = FixedPoint.parse("100,4351", ',');
        final FixedPoint fp2 = FixedPoint.parse("99,56", ',');
        assertEquals(1, fp1.compareTo(fp2));
        assertEquals(-1, fp2.compareTo(fp1));
        assertEquals(0, fp1.compareTo(fp1.clone()));
    }

    public static TestSuite suite() {
        final TestSuite suite = new TestSuite(FixedPointTest.class.getName());
        try {
            suite.addTest(new FixedPointTest("testParse"));
            suite.addTest(new FixedPointTest("testCreate"));
            suite.addTest(new FixedPointTest("testAddSub"));
            suite.addTest(new FixedPointTest("testMultDiv"));
            suite.addTest(new FixedPointTest("testToString"));
            suite.addTest(new FixedPointTest("testReadWrite"));
            suite.addTest(new FixedPointTest("testCompare"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return suite;
    }
}
