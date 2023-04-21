package org.t2framework.lucy.exception;

import org.t2framework.commons.meta.impl.ClassDescImpl;
import org.t2framework.lucy.Lifecycle;
import junit.framework.TestCase;

public class BehaviorNotFoundExceptionTest extends TestCase {

    public void test1() throws Exception {
        BehaviorNotFoundException e = new BehaviorNotFoundException(null, Lifecycle.COMPONENT_CREATED, new ClassDescImpl<BehaviorNotFoundExceptionTest>(this.getClass()));
        assertNotNull(e.getMessage());
    }
}
