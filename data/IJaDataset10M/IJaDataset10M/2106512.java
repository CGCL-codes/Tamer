package net.sf.xspecs.test.oomatron;

import java.lang.reflect.Method;
import net.sf.xspecs.oomatron.Invokable;
import net.sf.xspecs.oomatron.MethodReflector;
import net.sf.xspecs.oomatron.ProxiedObjectIdentity;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Invocation;

public class ProxiedObjectIdentityTest extends MockObjectTestCase implements TestData {

    private static final Method EQUALS_METHOD = MethodReflector.getMethod(Object.class, "equals", new Class[] { Object.class });

    private static final Method HASHCODE_METHOD = MethodReflector.getMethod(Object.class, "hashCode", new Class[0]);

    private static final Method TOSTRING_METHOD = MethodReflector.getMethod(Object.class, "toString", new Class[0]);

    private Object identity;

    private Mock mockDefault;

    private ProxiedObjectIdentity proxiedObjectIdentity;

    public void setUp() {
        identity = newDummy("identity");
        mockDefault = mock(Invokable.class, "mockDefault");
        proxiedObjectIdentity = new ProxiedObjectIdentity(identity, (Invokable) mockDefault.proxy());
    }

    public void testImplementsEqualsByTestingIfArgumentIsSameObjectAsTheInvokedObject() throws Throwable {
        Object invokedObject = newDummy("invokedObject");
        Object otherObject = newDummy("otherObject");
        assertEquals("should be equal to invoked object", Boolean.TRUE, proxiedObjectIdentity.invoke(new Invocation(invokedObject, EQUALS_METHOD, new Object[] { invokedObject })));
        assertEquals("should not be equal to other object", Boolean.FALSE, proxiedObjectIdentity.invoke(new Invocation(invokedObject, EQUALS_METHOD, new Object[] { otherObject })));
    }

    public void testDelegatesHashCodeToIdentityObject() throws Throwable {
        Invocation hashCodeInvocation = new Invocation(INVOKED_OBJECT, HASHCODE_METHOD, new Object[0]);
        assertEquals("hashcode", identity.hashCode(), ((Integer) proxiedObjectIdentity.invoke(hashCodeInvocation)).intValue());
    }

    public void testDelegatesToStringToIdentityObject() throws Throwable {
        Invocation toStringInvocation = new Invocation(INVOKED_OBJECT, TOSTRING_METHOD, new Object[0]);
        assertEquals("toString", identity.toString(), proxiedObjectIdentity.invoke(toStringInvocation));
    }

    public void testPassesMethodsNotDeclaredInObjectClassToDefaultAction() throws Throwable {
        mockDefault.expects(once()).method("invoke").with(same(INVOCATION));
        proxiedObjectIdentity.invoke(INVOCATION);
    }
}
