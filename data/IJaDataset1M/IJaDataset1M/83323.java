package org.jfree.chart.labels.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;

/**
 * Tests for the {@link StandardCategoryToolTipGenerator} class.
 */
public class StandardCategoryToolTipGeneratorTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(StandardCategoryToolTipGeneratorTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public StandardCategoryToolTipGeneratorTests(String name) {
        super(name);
    }

    /**
     * Tests the equals() method.
     */
    public void testEquals() {
        StandardCategoryToolTipGenerator g1 = new StandardCategoryToolTipGenerator();
        StandardCategoryToolTipGenerator g2 = new StandardCategoryToolTipGenerator();
        assertTrue(g1.equals(g2));
        assertTrue(g2.equals(g1));
        g1 = new StandardCategoryToolTipGenerator("{0}", new DecimalFormat("0.000"));
        assertFalse(g1.equals(g2));
        g2 = new StandardCategoryToolTipGenerator("{0}", new DecimalFormat("0.000"));
        assertTrue(g1.equals(g2));
        g1 = new StandardCategoryToolTipGenerator("{1}", new DecimalFormat("0.000"));
        assertFalse(g1.equals(g2));
        g2 = new StandardCategoryToolTipGenerator("{1}", new DecimalFormat("0.000"));
        assertTrue(g1.equals(g2));
        g1 = new StandardCategoryToolTipGenerator("{2}", new SimpleDateFormat("d-MMM"));
        assertFalse(g1.equals(g2));
        g2 = new StandardCategoryToolTipGenerator("{2}", new SimpleDateFormat("d-MMM"));
        assertTrue(g1.equals(g2));
    }

    /**
     * Confirm that cloning works.
     */
    public void testCloning() {
        StandardCategoryToolTipGenerator g1 = new StandardCategoryToolTipGenerator();
        StandardCategoryToolTipGenerator g2 = null;
        try {
            g2 = (StandardCategoryToolTipGenerator) g1.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Failed to clone.");
        }
        assertTrue(g1 != g2);
        assertTrue(g1.getClass() == g2.getClass());
        assertTrue(g1.equals(g2));
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {
        StandardCategoryToolTipGenerator g1 = new StandardCategoryToolTipGenerator("{2}", DateFormat.getInstance());
        StandardCategoryToolTipGenerator g2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(g1);
            out.close();
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            g2 = (StandardCategoryToolTipGenerator) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        assertEquals(g1, g2);
    }
}
