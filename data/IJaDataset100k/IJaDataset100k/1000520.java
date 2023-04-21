package org.apache.harmony.awt.tests.java.awt.font;

import com.google.code.appengine.awt.font.TransformAttribute;
import com.google.code.appengine.awt.geom.AffineTransform;
import junit.framework.TestCase;

public class TransformAttributeTest extends TestCase {

    public TransformAttributeTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testTransformAttribute() {
        TransformAttribute ta;
        AffineTransform at = AffineTransform.getRotateInstance(1);
        ta = new TransformAttribute(at);
        assertEquals(at, ta.getTransform());
        at.translate(10, 10);
        assertFalse(at.equals(ta.getTransform()));
        try {
            ta = new TransformAttribute(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }
    }

    public final void testGetTransform() {
        AffineTransform at = AffineTransform.getRotateInstance(1);
        TransformAttribute ta = new TransformAttribute(at);
        assertEquals(at, ta.getTransform());
    }

    public final void testIsIdentity() {
        TransformAttribute ta = new TransformAttribute(new AffineTransform());
        assertTrue(ta.isIdentity());
        AffineTransform at = AffineTransform.getRotateInstance(1);
        ta = new TransformAttribute(at);
        assertFalse(ta.isIdentity());
    }
}
