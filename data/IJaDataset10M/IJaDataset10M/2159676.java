package org.jmove.kernel.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmove.kernel.BaseContext;
import org.jmove.kernel.Context;

/** Defines 
 *  
 */
public class BehaviorTest extends TestCase {

    protected Context testCore;

    public static interface Behavior {
    }

    public static class BehaviorImpl implements Behavior {
    }

    public static interface SubjectA {
    }

    public static interface SubjectB {
    }

    public static interface SubjectC {
    }

    public static class Test1 {
    }

    public static class Test2 implements SubjectA {
    }

    public static class Test2B extends Test2 implements SubjectB, SubjectC {
    }

    public static Test suite() {
        return new TestSuite(BehaviorTest.class);
    }

    public BehaviorTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        testCore = new BaseContext();
        testCore.initContext();
    }

    /** To bind a behavior the behavior instance has not be null. */
    public void testBindWithNoBehavior() {
        try {
            testCore.broker().bindBehavior(Test1.class, Behavior.class, null);
            fail();
        } catch (IllegalArgumentException iae) {
        }
        ;
    }

    /** To bind a behavior the behavior type has not be null. */
    public void testBindWithNoBehaviorType() {
        try {
            testCore.broker().bindBehavior(Test1.class, null, new BehaviorImpl());
            fail();
        } catch (IllegalArgumentException iae) {
        }
        ;
    }

    /** To bind a behavior the subject type has not be null. */
    public void testBindWithNoSubjectType() {
        try {
            testCore.broker().bindBehavior(null, Behavior.class, new BehaviorImpl());
            fail();
        } catch (IllegalArgumentException iae) {
        }
        ;
    }

    /** If a behavior was bound to a subject type the behaviorForType() method
 * has to return the same behavior.
 */
    public void testBindBehavior() {
        Behavior behavior = new BehaviorImpl();
        testCore.broker().bindBehavior(Test1.class, Behavior.class, behavior);
        assertSame(behavior, testCore.broker().behaviorForType(Behavior.class, Test1.class));
        assertSame(behavior, testCore.broker().behaviorFor(Behavior.class, new Test1()));
    }

    /** If no behavior was bound to the subject type but to one of its interfaces 
 * the broker should return this inherited behavior. 
 */
    public void testInterfaceBoundInheritance() {
        Behavior behavior = new BehaviorImpl();
        testCore.broker().bindBehavior(SubjectC.class, Behavior.class, behavior);
        assertSame(behavior, testCore.broker().behaviorFor(Behavior.class, new Test2B()));
    }

    /** If no behavior was bound to the subject type but to its superclass 
 * the broker should return this inherited behavior. 
 */
    public void testSuperclassBoundInheritance() {
        Behavior behavior = new BehaviorImpl();
        testCore.broker().bindBehavior(Test2.class, Behavior.class, behavior);
        assertSame(behavior, testCore.broker().behaviorFor(Behavior.class, new Test2()));
        assertSame(behavior, testCore.broker().behaviorFor(Behavior.class, new Test2B()));
    }

    /** If no behavior was bound to the subject type but to an interface of its superclass 
 * the broker should return this inherited behavior. 
 */
    public void testSuperclassInterfacesBoundInheritance() {
        Behavior behavior = new BehaviorImpl();
        testCore.broker().bindBehavior(SubjectA.class, Behavior.class, behavior);
        assertSame(behavior, testCore.broker().behaviorFor(Behavior.class, new Test2()));
        assertSame(behavior, testCore.broker().behaviorFor(Behavior.class, new Test2B()));
    }

    /** If no behavior was bound to the subject type hierarchy null must be returned.
 */
    public void testNoBehavior() {
        assertNull(testCore.broker().behaviorFor(Behavior.class, new Test2B()));
    }
}
