package com.google.common.collect;

import junit.framework.TestCase;
import java.util.Set;

/**
 * Tests for {@code ForwardingObject}.
 *
 * @author Mike Bostock
 */
public class ForwardingObjectTest extends TestCase {

    public void testEqualsReflexive() {
        final Object delegate = new Object();
        ForwardingObject forward = new ForwardingObject() {

            @Override
            protected Object delegate() {
                return delegate;
            }
        };
        assertTrue(forward.equals(forward));
    }

    public void testEqualsSymmetric() {
        final Set<String> delegate = Sets.newHashSet("foo");
        ForwardingObject forward = new ForwardingObject() {

            @Override
            protected Object delegate() {
                return delegate;
            }
        };
        assertEquals(forward.equals(delegate), delegate.equals(forward));
    }
}
