package org.jfree.chart.util.junit;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.font.TextAttribute;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.AttributedString;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.util.AttributedStringUtilities;
import org.jfree.chart.util.SerialUtilities;
import org.jfree.chart.util.ShapeUtilities;

/**
 * Tests for the {@link SerialUtilities} class.
 */
public class SerialUtilitiesTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(SerialUtilitiesTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public SerialUtilitiesTests(final String name) {
        super(name);
    }

    /**
     * Tests the isSerializable(Class) method for some common cases.
     */
    public void testIsSerializable() {
        assertTrue(SerialUtilities.isSerializable(Color.class));
        assertTrue(SerialUtilities.isSerializable(ColorUIResource.class));
        assertFalse(SerialUtilities.isSerializable(GradientPaint.class));
        assertFalse(SerialUtilities.isSerializable(TexturePaint.class));
    }

    /**
     * Serialize a <code>Color</code> and check that it can be deserialized
     * correctly.
     */
    public void testColorSerialization() {
        Paint p1 = Color.blue;
        Paint p2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writePaint(p1, out);
            out.close();
            ByteArrayInputStream bias = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bias);
            p2 = SerialUtilities.readPaint(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(p1, p2);
    }

    /**
     * Serialize a <code>ColorUIResource</code> and check that it can be
     * deserialized correctly.
     */
    public void testColorUIResourceSerialization() {
        Paint p1 = UIManager.getColor("Panel.background");
        Paint p2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writePaint(p1, out);
            out.close();
            ByteArrayInputStream bias = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bias);
            p2 = SerialUtilities.readPaint(in);
            in.close();
        } catch (Exception e) {
            fail(e.toString());
        }
        assertEquals(p1, p2);
    }

    /**
     * Serialize a <code>GradientPaint</code>, restore it, and check for
     * equality.
     */
    public void testGradientPaintSerialization() {
        Paint p1 = new GradientPaint(0.0f, 0.0f, Color.blue, 100.0f, 200.0f, Color.red);
        Paint p2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writePaint(p1, out);
            out.close();
            ByteArrayInputStream bias = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bias);
            p2 = SerialUtilities.readPaint(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GradientPaint gp1 = (GradientPaint) p1;
        GradientPaint gp2 = (GradientPaint) p2;
        assertEquals(gp1.getColor1(), gp2.getColor1());
        assertEquals(gp1.getPoint1(), gp2.getPoint1());
        assertEquals(gp1.getColor2(), gp2.getColor2());
        assertEquals(gp1.getPoint2(), gp2.getPoint2());
        assertEquals(gp1.isCyclic(), gp2.isCyclic());
    }

    /**
     * Serialize a <code>TexturePaint</code>, restore it, and check for
     * equality.  Since this class is not serializable, we expect null as the
     * result.
     */
    public void testTexturePaintSerialization() {
        Paint p1 = new TexturePaint(new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB), new Rectangle2D.Double(0, 0, 5, 5));
        Paint p2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writePaint(p1, out);
            out.close();
            ByteArrayInputStream bias = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bias);
            p2 = SerialUtilities.readPaint(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(p2);
    }

    /**
     * Serialize a <code>Line2D.Float</code> instance, and check that it can be
     * deserialized correctly.
     */
    public void testLine2DFloatSerialization() {
        Line2D l1 = new Line2D.Float(1.0f, 2.0f, 3.0f, 4.0f);
        Line2D l2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeShape(l1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            l2 = (Line2D) SerialUtilities.readShape(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(ShapeUtilities.equal(l1, l2));
    }

    /**
     * Serialize a <code>Line2D.Double</code> instance and check that it can be
     * deserialized correctly.
     */
    public void testLine2DDoubleSerialization() {
        Line2D l1 = new Line2D.Double(1.0, 2.0, 3.0, 4.0);
        Line2D l2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeShape(l1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            l2 = (Line2D) SerialUtilities.readShape(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(ShapeUtilities.equal(l1, l2));
    }

    /**
     * Serialize a <code>Rectangle2D.Float</code> instance, and check that it
     * can be deserialized correctly.
     */
    public void testRectangle2DFloatSerialization() {
        Rectangle2D r1 = new Rectangle2D.Float(1.0f, 2.0f, 3.0f, 4.0f);
        Rectangle2D r2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeShape(r1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            r2 = (Rectangle2D) SerialUtilities.readShape(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(ShapeUtilities.equal(r1, r2));
    }

    /**
     * Serialize a <code>Rectangle2D.Double</code> instance and check that it
     * can be deserialized correctly.
     */
    public void testRectangle2DDoubleSerialization() {
        Rectangle2D r1 = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        Rectangle2D r2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeShape(r1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            r2 = (Rectangle2D) SerialUtilities.readShape(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(ShapeUtilities.equal(r1, r2));
    }

    /**
     * Serialize an <code>Arc2D.Float</code> instance and check that it
     * can be deserialized correctly.
     */
    public void testArc2DFloatSerialization() {
        Arc2D a1 = new Arc2D.Float(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, Arc2D.PIE);
        Arc2D a2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeShape(a1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            a2 = (Arc2D) SerialUtilities.readShape(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(ShapeUtilities.equal(a1, a2));
    }

    /**
     * Serialize an <code>Arc2D.Double</code> instance and check that it
     * can be deserialized correctly.
     */
    public void testArc2DDoubleSerialization() {
        Arc2D a1 = new Arc2D.Double(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, Arc2D.PIE);
        Arc2D a2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeShape(a1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            a2 = (Arc2D) SerialUtilities.readShape(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(ShapeUtilities.equal(a1, a2));
    }

    /**
     * Some checks for the serialization of a GeneralPath instance.
     */
    public void testGeneralPathSerialization() {
        GeneralPath g1 = new GeneralPath();
        g1.moveTo(1.0f, 2.0f);
        g1.lineTo(3.0f, 4.0f);
        g1.curveTo(5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f);
        g1.quadTo(1.0f, 2.0f, 3.0f, 4.0f);
        g1.closePath();
        GeneralPath g2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeShape(g1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            g2 = (GeneralPath) SerialUtilities.readShape(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(ShapeUtilities.equal(g1, g2));
    }

    /**
     * Tests the serialization of an {@link AttributedString}.
     */
    public void testAttributedStringSerialization1() {
        AttributedString s1 = new AttributedString("");
        AttributedString s2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeAttributedString(s1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            s2 = SerialUtilities.readAttributedString(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(AttributedStringUtilities.equal(s1, s2));
    }

    /**
     * Tests the serialization of an {@link AttributedString}.
     */
    public void testAttributedStringSerialization2() {
        AttributedString s1 = new AttributedString("ABC");
        AttributedString s2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeAttributedString(s1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            s2 = SerialUtilities.readAttributedString(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(AttributedStringUtilities.equal(s1, s2));
    }

    /**
     * Tests the serialization of an {@link AttributedString}.
     */
    public void testAttributedStringSerialization3() {
        AttributedString s1 = new AttributedString("ABC");
        s1.addAttribute(TextAttribute.LANGUAGE, "English");
        AttributedString s2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            SerialUtilities.writeAttributedString(s1, out);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            s2 = SerialUtilities.readAttributedString(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(AttributedStringUtilities.equal(s1, s2));
    }
}
