package test.egu.plugin.mocker;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPMethod;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import egu.plugin.mocker.Interface.IMockableClass;
import egu.plugin.mocker.implementation.MockableClass;

@RunWith(JMock.class)
public class MockableClassTest {

    private Mockery context;

    private IMockableClass toTest;

    private ICPPClassType classParent;

    ICPPMethod method1;

    ICPPMethod method2;

    ICPPMethod method3;

    ICPPMethod method4;

    ICPPClassType otherClass;

    @Before
    public void setUp() throws Exception {
        context = new JUnit4Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        classParent = context.mock(ICPPClassType.class, "parent");
        toTest = new MockableClass(classParent);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testAddMethod() throws DOMException {
        createMethod();
        toTest.addMethod(method1);
        toTest.addMethod(method2);
        toTest.addMethod(method3);
        toTest.addMethod(method4);
        List<ICPPMethod> addedMethods = toTest.getMethodsToMock();
        assertEquals(3, addedMethods.size());
        assertEquals(method1, addedMethods.get(0));
        assertEquals(method2, addedMethods.get(1));
        assertEquals(method4, addedMethods.get(2));
    }

    /**
	 * @throws DOMException 
	 * 
	 */
    private void createMethod() throws DOMException {
        method1 = context.mock(ICPPMethod.class, "method1");
        method2 = context.mock(ICPPMethod.class, "method2");
        method3 = context.mock(ICPPMethod.class, "method3");
        method4 = context.mock(ICPPMethod.class, "method4");
        otherClass = context.mock(ICPPClassType.class, "otherClass");
        context.checking(new Expectations() {

            {
                allowing(method1).getClassOwner();
                will(returnValue(classParent));
                allowing(method2).getClassOwner();
                will(returnValue(classParent));
                allowing(method3).getClassOwner();
                will(returnValue(otherClass));
                allowing(method4).getClassOwner();
                will(returnValue(classParent));
            }
        });
    }

    @Test
    public final void testIsClassType() {
        final ICPPClassType otherClass = context.mock(ICPPClassType.class, "otherClass");
        assertTrue(toTest.isClassType(classParent));
        assertFalse(toTest.isClassType(otherClass));
    }

    @Test
    public final void testAddNotVirtualMethod() throws DOMException {
        createMethod();
        context.checking(new Expectations() {

            {
                allowing(method1).isVirtual();
                will(returnValue(false));
                allowing(method2).isVirtual();
                will(returnValue(true));
                allowing(method4).isVirtual();
                will(returnValue(false));
            }
        });
        toTest.addMethod(method1);
        toTest.addMethod(method2);
        toTest.addMethod(method4);
        ArrayList<ICPPMethod> notVirtualMethod = new ArrayList<ICPPMethod>();
        notVirtualMethod.add(method3);
        toTest.addNotVirtualMethod(notVirtualMethod);
        assertEquals(3, notVirtualMethod.size());
        assertEquals(method3, notVirtualMethod.get(0));
        assertEquals(method1, notVirtualMethod.get(1));
        assertEquals(method4, notVirtualMethod.get(2));
    }

    @Test
    public final void testGetName() {
        final String nameClass = "aClassName";
        context.checking(new Expectations() {

            {
                allowing(classParent).getName();
                will(returnValue(nameClass));
            }
        });
        assertEquals(nameClass, toTest.getName());
    }

    @Test
    public final void testGetClassType() {
        assertEquals(classParent, toTest.getClassType());
    }

    @Test
    public final void getWhereDefined() throws DOMException {
        final String defined = "dir1/dir2/class1.h";
        final String[] qualifiedNamed = { defined };
        context.checking(new Expectations() {

            {
                oneOf(classParent).getQualifiedName();
                will(returnValue(qualifiedNamed));
            }
        });
        assertThat("the file where shuold be defined isn't right", toTest.getWhereDefined(), equalTo(defined));
    }
}
