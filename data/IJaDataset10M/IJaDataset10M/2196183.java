package ru.beta2.testyard.engine;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import ru.beta2.testyard.MessageControl;

/**
 * User: Inc
 * Date: 20.06.2008
 * Time: 2:55:30
 */
public class SmartMessagesCheckingTest extends AbstractTest {

    MessageControl mctl;

    protected void setUp() throws Exception {
        super.setUp();
        mctl = engine.getMessageControl();
    }

    class A {

        int i;

        int j;

        A(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    class B {

        A a;

        B b;

        B(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

    class Array {

        int[] a;

        int b;

        Array(int... a) {
            b = a[0];
            this.a = a;
        }
    }

    class ArrayOfArray {

        Array[] array;

        ArrayOfArray(Array... array) {
            this.array = array;
        }
    }

    protected boolean switchSmartCheckOff() {
        return false;
    }

    public void testDefaultReflectionChecking() {
        engine.expectChannelMessage(1, "one", new A(12, 34));
        engine.expectSessionMessage(2, new A(56, 78));
        answer(new ChannelMessageAnswer(1, "one", new A(12, 34)), new SessionMessageAnswer(2, new A(56, 78)));
        go();
    }

    public void testDefaultChecking_Arrays() {
        engine.expectSessionMessage(1, new Array(12, 34, 56, 78));
        engine.expectSessionMessage(2, new ArrayOfArray(new Array(12, 34), new Array(56, 78)));
        answer(new SessionMessageAnswer(1, new Array(12, 34, 56, 78)), new SessionMessageAnswer(2, new ArrayOfArray(new Array(12, 34), new Array(56, 78))));
        go();
    }

    public void testDefaultChecking_Arrays_Unexpected() {
        engine.expectSessionMessage(1, new Array(12, 34, 56, 78));
        answer(new SessionMessageAnswer(1, new Array(12, 34, 56, 8888)));
        checkUnexpected();
    }

    public void testDefaultChecking_ObjectArrays_Unexpected() {
        engine.expectSessionMessage(2, new ArrayOfArray(new Array(12, 34), new Array(56, 78)));
        answer(new SessionMessageAnswer(2, new ArrayOfArray(new Array(12, 34), new Array(56, 8888))));
        checkUnexpected();
    }

    public void testDefaultChecking_ObjectArrays_DifferentMembers_Unexpected() {
        engine.expectSessionMessage(2, new ArrayOfArray(new Array(12, 34), new Array(56, 78)));
        answer(new SessionMessageAnswer(2, new ArrayOfArray(new Array(12, 34))));
        checkUnexpected();
    }

    public void testDefaultChecking_NestingObjects() {
        engine.expectSessionMessage(1, new B(new A(11, 22), null));
        engine.expectSessionMessage(2, new B(new A(12, 34), new B(new A(56, 78), null)));
        answer(new SessionMessageAnswer(1, new B(new A(11, 22), null)), new SessionMessageAnswer(2, new B(new A(12, 34), new B(new A(56, 78), null))));
        go();
    }

    public void testDefaultChecking_NestingObjects_Unexpected() {
        engine.expectSessionMessage(2, new B(new A(12, 34), new B(new A(56, 78), null)));
        answer(new SessionMessageAnswer(2, new B(new A(12, 34), new B(new A(56, 8888), null))));
        checkUnexpected();
    }

    public void testDefaultChecking_NestingObjects_NullInReceived_Unexpected() {
        engine.expectSessionMessage(2, new B(new A(12, 34), new B(new A(56, 78), null)));
        answer(new SessionMessageAnswer(2, new B(new A(12, 34), new B(null, null))));
        checkUnexpected();
    }

    class X extends B {

        int x;

        X(int x, A a, B b) {
            super(a, b);
            this.x = x;
        }
    }

    class Y extends X {

        int y;

        Y(int y, int x, A a, B b) {
            super(x, a, b);
            this.y = y;
        }
    }

    public void testDefaultChecking_Inheritance() {
        engine.expectSessionMessage(2, new Y(11, 22, new A(12, 34), new B(new A(56, 78), null)));
        answer(new SessionMessageAnswer(2, new Y(11, 22, new A(12, 34), new B(new A(56, 78), null))));
        go();
    }

    public void testDefaultChecking_Inheritance_Unexpected() {
        engine.expectSessionMessage(2, new Y(11, 22, new A(12, 34), new B(new A(56, 78), null)));
        answer(new SessionMessageAnswer(2, new Y(11, 22, new A(12, 34), new B(new A(56, 8888), null))));
        checkUnexpected();
    }

    public void testDefaultChecking_NullMessages() {
        engine.expectSessionMessage(1, null);
        answer(new SessionMessageAnswer(1, null));
        go();
    }

    public void testDefaultChecking_ExpectNullMessage() {
        engine.expectSessionMessage(1, null);
        answer(new SessionMessageAnswer(1, new Object()));
        checkUnexpected();
    }

    public void testDefaultChecking_UnexpectedNullMessage() {
        engine.expectSessionMessage(1, new Object());
        answer(new SessionMessageAnswer(1, null));
        checkUnexpected();
    }

    public void testMessageEventAttributesTakingIntoAccount_SessionMessage() {
        engine.expectSessionMessage(1, new A(56, 78));
        answer(new SessionMessageAnswer(2, new A(56, 78)));
        checkUnexpected();
    }

    public void testMessageEventAttributesTakingIntoAccount_ChannelMessage() {
        engine.expectChannelMessage(1, "one", new A(12, 34));
        engine.expectChannelMessage(2, "one", new A(12, 34));
        engine.expectChannelMessage(1, "two", new A(12, 34));
        answer(new ChannelMessageAnswer(2, "two", new A(12, 34)));
        checkUnexpected();
    }

    public void testSmartCheckOff_SessionMessage() {
        engine.getMessageControl().setSmartCheck(false);
        engine.expectSessionMessage(1, new Object());
        answer(new SessionMessageAnswer(1, new Object()));
        checkUnexpected();
    }

    public void testSmartCheckOff_ChannelMessage() {
        engine.getMessageControl().setSmartCheck(false);
        engine.expectChannelMessage(1, "one", new Object());
        answer(new ChannelMessageAnswer(1, "one", new Object()));
        checkUnexpected();
    }

    public void testMessageControl_IgnoreField() {
        mctl.ignoreField(A.class, "i");
        mctl.ignoreField(B.class, "b");
        engine.expectSessionMessage(1, new A(11, 22));
        engine.expectSessionMessage(2, new B(new A(12, 34), new B(new A(56, 78), null)));
        answer(new SessionMessageAnswer(1, new A(9999, 22)), new SessionMessageAnswer(2, new B(new A(12, 34), new B(new A(56, 8888), null))));
        go();
    }

    public void testMessageControl_IgnoreSeveralFieldsFromOneClass() {
        mctl.ignoreField(A.class, "i");
        mctl.ignoreField(A.class, "j");
        engine.expectSessionMessage(1, new A(11, 22));
        answer(new SessionMessageAnswer(1, new A(9999, 8888)));
        go();
    }

    public void __testMessageToStringBuilder() {
        ArrayOfArray b = new ArrayOfArray(new Array(12, 34), new Array(56, 78));
        System.out.println(b);
        System.out.println(ReflectionToStringBuilder.toString(b));
        System.out.println(new ScenarioEngine.MessageToStringBuilder(b).toString());
    }

    public void testReflectionEqualsOnArray() {
        assertTrue("EqualsBuilder.reflectionEquals badly work with arrays", EqualsBuilder.reflectionEquals(new int[] { 1, 2, 3, 4, 5 }, new int[] { 1, 2, 3, 4, 55 }));
        assertFalse("EqualsBuilder.reflectionEquals good work with arrays in fields", EqualsBuilder.reflectionEquals(new Array(1, 2, 3, 4, 5), new Array(1, 2, 3, 4, 55)));
    }
}
