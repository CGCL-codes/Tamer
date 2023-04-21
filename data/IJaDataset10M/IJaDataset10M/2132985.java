package org.nakedobjects.nof.reflect.remote.spec;

import junit.framework.TestSuite;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.nof.reflect.peer.MemberIdentifierImpl;
import org.nakedobjects.nof.testsystem.ProxyTestCase;
import org.nakedobjects.nof.testsystem.TestProxySpecification;

public class JavaFieldTest extends ProxyTestCase {

    private static final String MEMBERS_FIELD_NAME = "members";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(new TestSuite(JavaFieldTest.class));
    }

    private JavaField field;

    protected void setUp() throws Exception {
        super.setUp();
        MemberIdentifierImpl memberIdentifierImpl = new MemberIdentifierImpl("cls", MEMBERS_FIELD_NAME);
        field = new JavaField(memberIdentifierImpl, String.class) {

            public boolean isEmpty(NakedObject inObject) {
                return true;
            }
        };
    }

    protected void tearDown() throws Exception {
        system.shutdown();
    }

    public void testType() {
        TestProxySpecification spec = system.getSpecification(String.class);
        assertEquals(spec, field.getSpecification());
    }

    public void testIsEmpty() throws Exception {
        NakedObject inObject = system.createPersistentTestObject();
        assertTrue(field.isEmpty(inObject));
    }
}
