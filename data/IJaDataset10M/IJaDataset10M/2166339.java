package org.vramework.commons.beanutils.tests;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.vramework.commons.beanutils.VSpector;
import org.vramework.commons.beanutils.exceptions.ReflectionErrors;
import org.vramework.commons.beanutils.exceptions.ReflectionException;
import org.vramework.commons.beanutils.exceptions.VSpectionException;
import org.vramework.commons.beanutils.tests.testclasses.XSpectorTestClass1;
import org.vramework.commons.beanutils.tests.testclasses.XSpectorTestClass1Child1;
import org.vramework.commons.beanutils.tests.testclasses.XSpectorTestClass2;
import org.vramework.commons.beanutils.tests.testclasses.XSpectorTestClass3;
import org.vramework.commons.junit.VTestCase;

/**
 * @author tmahring
 */
public class VSpectorTest extends VTestCase {

    /**
   * Constructor for IntrospectorTest.
   * 
   * @param arg0
   */
    public VSpectorTest(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VSpector.clearCaches();
    }

    /**
   * This testmethod generates valid an invalid nested paths like "property1.
   * property2... propertyN". We use three test classes ("TestClass1-3"), each
   * of which contains scalar properties and pointer- properties. "TestClass3"
   * ist the last element in the nested path contains the property "hashedMap".
   * This hashed maps contains "TestClass1" objects. <br>
   * </br> The nested paths are converted to objects by the {@link VSpector}
   * class. The nested property value (= object, pointed to by propertyN )
   * itself can be an object (Integer, String, Object...) or a collection. In
   * case of a collection the method "
   * {@link VSpector#collection2Table(Collection, String[], boolean, String)} "
   * creates table whose header are definedbay the parameter "details".
   * 
   * @see VSpector#getNestedProperty(Object, String, boolean, boolean, String)
   * @see VSpector#collection2Table(Collection, String[], boolean, String)
   * @see XSpectorTestClass1
   * @see XSpectorTestClass2
   * @see XSpectorTestClass3
   * @throws Exception
   * 
   */
    public void testIntrospectorTest() throws Exception {
        String testPath = "";
        try {
            testPath = "class2.class3.hashedMap";
            System.out.println("Testing correct nested path, no errors should occur: " + testPath);
            testNested(testPath);
        } catch (Exception ex) {
            System.out.println("A not expected error occured: " + ex.getClass().getName());
            throw ex;
        }
        try {
            System.out.println();
            testPath = "basePointerToClass2.class3.hashedMap";
            System.out.println("Testing correct path with super-property, no errors should occur: " + testPath);
            testNested(testPath);
        } catch (Exception ex) {
            System.out.println("A not expected error occured: " + ex.getClass().getName());
            throw ex;
        }
        try {
            System.out.println();
            testPath = "invalidPath.class3.hashedMap";
            System.out.println("Testing incorrect nested path, 1st nesting is invalid: " + testPath);
            testNested(testPath);
        } catch (VSpectionException ex) {
            System.out.println("The 'correct' error occured: " + ex.getClass().getName() + ", Message = " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("A not expected error occured: " + ex.getClass().getName());
            throw ex;
        }
        try {
            System.out.println();
            testPath = "class2.invalidPath.hashedMap";
            System.out.println("Testing incorrect nested path, 2nd nesting is invalid: " + testPath);
            testNested(testPath);
        } catch (VSpectionException ex) {
            System.out.println("The 'correct' error occured: " + ex.getClass().getName() + ", Message = " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("A not expected error occured: " + ex.getClass().getName());
            throw ex;
        }
        try {
            System.out.println();
            testPath = "class2.class3.invalidNested";
            System.out.println("Testing incorrect nested path, 3nd nested is invalid: " + testPath);
            testNested(testPath);
        } catch (VSpectionException ex) {
            System.out.println("The 'correct' error occured: " + ex.getClass().getName() + ", Message = " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("A not expected error occured: " + ex.getClass().getName());
            throw ex;
        }
        try {
            System.out.println();
            testPath = "class2.class3NullObject.hashedMap";
            System.out.println("Testing null object: " + testPath);
            testNested(testPath);
        } catch (VSpectionException ex) {
            System.out.println("The 'correct' error occured: " + ex.getClass().getName() + ", Message = " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("A not expected error occured: " + ex.getClass().getName());
            throw ex;
        }
        try {
            System.out.println();
            testPath = "class2..hashedMap";
            System.out.println("Testing invalid path with two dots: " + testPath);
            testNested(testPath);
        } catch (VSpectionException ex) {
            System.out.println("The 'correct' error occured: " + ex.getClass().getName() + ", Message = " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("A not expected error occured: " + ex.getClass().getName());
            throw ex;
        }
        try {
            String details[] = { "class2.test2", "class2.class3.test3" };
            System.out.println();
            testPath = "class2.class3.hashedMap";
            System.out.println("Testing collection2Table() with correct path: " + testPath + ", details = " + details.toString());
            testCollection2Table(testPath, details);
        } catch (VSpectionException ex) {
            System.out.println("The 'correct' error occured: " + ex.getClass().getName() + ", Message = " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("A not expected error occured: " + ex.getClass().getName());
            throw ex;
        }
    }

    /**
   * Tests the passed nested path. <br>
   * 1) Creates an object of type {@link XSpectorTestClass1}, <br>
   * 2) Creates an {@link VSpector} <br>
   * 3) Asks the introspector to return the target of the nested path (
   * {@link VSpector#getNestedPropertyDescriptor(Class, String)}).
   * 
   * @param nestedPath
   *          The path to be traversed an translated into an object.
   * @throws NoSuchFieldException
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws VSpectionException
   */
    public void testNested(String nestedPath) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, VSpectionException {
        XSpectorTestClass1 class1 = new XSpectorTestClass1();
        Object target = null;
        Method getter = null;
        @SuppressWarnings("unused") String test;
        getter = VSpector.getGetter(XSpectorTestClass1.class, "test1");
        test = (String) getter.invoke(class1, (Object[]) null);
        target = VSpector.getNestedProperty(class1, nestedPath, true, true, null);
        if (target == null) {
            System.out.println("Target is null.");
        } else {
            System.out.println("Result of nested path: " + target.toString());
            if (target instanceof Collection<?>) {
                System.out.println("Result is a collection");
            }
        }
    }

    /**
   * Tests the passed nested path. <br>
   * 1) Creates an object of type {@link XSpectorTestClass1}, <br>
   * 2) Creates a {@link VSpector} <br>
   * 3) Asks the introspector to return the target of the nested formatted as
   * table (
   * {@link VSpector#collection2Table(Collection, String[], boolean, String)}).
   * 
   * @param nestedPath
   *          The path to be traversed an translated into an object. The target
   *          must be collection
   * @param details
   *          "The headers" of the table. Each "detail" must be a valid property
   *          of each object in the collection.
   * @throws NoSuchFieldException
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws VSpectionException
   */
    public void testCollection2Table(String nestedPath, String[] details) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, VSpectionException {
        XSpectorTestClass1 class1Instance = new XSpectorTestClass1();
        Object[][] table = null;
        Collection<?> collection = null;
        int countRows = 0;
        int countCols = 0;
        String row = null;
        collection = (Collection<?>) VSpector.getNestedProperty(class1Instance, nestedPath, true, true, null);
        table = VSpector.collection2Table(collection, details, true, null);
        countRows = table.length;
        countCols = table[0].length;
        System.out.println("\r\n" + "Result of nested path: ");
        for (int i = 0; i < countRows; i++) {
            row = "Object " + i + ":\t";
            for (int j = 0; j < countCols; j++) {
                row += table[i][j] + "\t";
            }
            System.out.println(row);
        }
    }

    public void testGetDefaultGetter() throws VSpectionException {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        Method m = VSpector.getGetter(tc1.getClass(), "test1");
        assertNotNull(m);
    }

    public void testCallDefaultGetter() throws VSpectionException {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        Object value = VSpector.callGetter(tc1, "test1", true);
        System.out.println("Retrieved by callGetter(): " + value);
    }

    public void testGetDefaultSetter() throws VSpectionException {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        Method m = VSpector.getSetter(tc1.getClass(), "test1");
        assertNotNull(m);
    }

    public void testCallDefaultSetter() throws VSpectionException {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        VSpector.callSetter(tc1, "test1", "TestValue", false);
    }

    public void testGetDefaultAdder() throws VSpectionException {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        Method m = VSpector.getAdder(tc1.getClass(), "testCollection", String.class);
        assertNotNull(m);
    }

    public void testGetDefaultAdderChild() throws VSpectionException {
        XSpectorTestClass1Child1 tc1Child = new XSpectorTestClass1Child1();
        Method m = VSpector.getAdder(tc1Child.getClass(), "testCollection", String.class);
        assertNotNull(m);
    }

    public void testCallDefaultAdder() throws VSpectionException {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        VSpector.callAdder(tc1, "testCollection", "TestValue", String.class, false);
        assertTrue(tc1.getTestCollection().size() == 1);
        VSpector.callAdder(tc1, "testCollection", "TestValue2", String.class, false);
        assertTrue(tc1.getTestCollection().size() == 2);
    }

    public void testGetDefaultGetterForUnderscoredMember() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") Method getter = VSpector.getGetter(tc1.getClass(), "_underScoredMember");
    }

    public void testGetPropertyDescriptorForUnderscoredMember() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") PropertyDescriptor pd = VSpector.getPropertyDescriptor(tc1.getClass(), "_underScoredMember");
    }

    public void testGetPropertyDescriptorForUnderscoredMemberOfBaseClass() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") PropertyDescriptor pd = VSpector.getPropertyDescriptor(tc1.getClass(), "_underScoredMemberInBaseClass");
    }

    /**
   * Test {@link VSpector#getDeclaredField(Class, String, boolean)} with last
   * param == false. => Should cause a wanted exception.
   */
    public void testGetDeclaredFieldForUnderscoredMemberOfBaseClassWithoutBaseClassCheck() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        ReflectionException wantedExcaption = null;
        try {
            @SuppressWarnings("unused") Field field = VSpector.getDeclaredField(tc1.getClass(), "_underScoredMemberInBaseClass", true);
        } catch (ReflectionException e) {
            wantedExcaption = e;
        }
        assertNotNull(wantedExcaption);
    }

    /**
   * Test {@link VSpector#getDeclaredMethod(Class, String, Class...)} with last
   * param == false. => Should cause a wanted exception.
   */
    public void testGetDeclaredMEthodForMethodOfBaseClassWithoutBaseClassCheck() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        ReflectionException wantedExcaption = null;
        try {
            @SuppressWarnings("unused") Method method = VSpector.getDeclaredMethod(tc1.getClass(), "methodDeclaredInXSpectorTestBaseClass1", false);
        } catch (ReflectionException e) {
            wantedExcaption = e;
        }
        assertNotNull(wantedExcaption);
    }

    /**
   * Test {@link VSpector#getDeclaredMethod(Class, String, boolean, Class...)}.
   */
    public void testGetDeclaredMethod() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") Method method = VSpector.getDeclaredMethod(tc1.getClass(), "methodDeclaredInXSpectorTestClass1");
        method = VSpector.getDeclaredMethod(tc1.getClass(), "methodDeclaredInXSpectorTestClass1");
    }

    /**
   * Test {@link VSpector#getDeclaredMethod(Class, String, boolean, Class...)}.
   */
    public void testGetDeclaredMethodForThisClass() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") Method method = VSpector.getDeclaredMethod(tc1.getClass(), "methodDeclaredInXSpectorTestClass1", false);
        method = VSpector.getDeclaredMethod(tc1.getClass(), "methodDeclaredInXSpectorTestClass1", false);
    }

    /**
   * Test {@link VSpector#getDeclaredField(Class, String)}.
   */
    public void testGetDeclaredField() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") Field f = VSpector.getDeclaredField(tc1.getClass(), "_underScoredMember");
        f = VSpector.getDeclaredField(tc1.getClass(), "_underScoredMember");
    }

    /**
   * Test {@link VSpector#getDeclaredField(Class, String, boolean)}.
   */
    public void testGetDeclaredFieldForThisClass() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") Field f = VSpector.getDeclaredField(tc1.getClass(), "_underScoredMember", false);
        f = VSpector.getDeclaredField(tc1.getClass(), "_underScoredMember", false);
    }

    /**
   * Test {@link VSpector#getDeclaredField(Class, String, boolean, boolean)}.
   * exception.
   */
    public void testGetDeclaredFieldForUnderscoredMemberOfBaseClassWithBaseClassCheck() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") Field field = VSpector.getDeclaredField(tc1.getClass(), "_underScoredMemberInBaseClass", true, true);
        field = VSpector.getDeclaredField(tc1.getClass(), "_underScoredMemberInBaseClass", true, true);
    }

    /**
   * Test {@link VSpector#getDeclaredMethod(Class, String, boolean, Class...)}
   * with last param == true. => Should NOT cause an exception.
   */
    public void testGetDeclaredMethodForMethodOfBaseClassWithBaseClassCheck() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") Method method = VSpector.getDeclaredMethod(tc1.getClass(), "methodDeclaredInXSpectorTestBaseClass1", true);
        method = VSpector.getDeclaredMethod(tc1.getClass(), "methodDeclaredInXSpectorTestBaseClass1", true);
    }

    /**
   * Test {@link VSpector#getDeclaredMethod(Class, String, boolean, Class...)}
   * with last param == true. => Should NOT cause an exception.
   */
    public void testGetDeclaredMethodForMethodOfBaseClassInclParamsWithBaseClassCheck() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        @SuppressWarnings("unused") Method method = VSpector.getDeclaredMethod(tc1.getClass(), "methodWithParameters", true, String.class, Class[].class);
        method = VSpector.getDeclaredMethod(tc1.getClass(), "methodWithParameters", true, String.class, Class[].class);
    }

    /**
   * Test and invalid field with
   * {@link VSpector#getDeclaredField(Class, String, boolean)} with last param
   * == true. => Should cause exception.
   */
    public void testGetDeclaredFieldForInvalidFieldWithBaseClassCheck() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        ReflectionException wantedExcaption = null;
        try {
            @SuppressWarnings("unused") Field field = VSpector.getDeclaredField(tc1.getClass(), "nonExistingField", true);
        } catch (ReflectionException e) {
            wantedExcaption = e;
        }
        assertNotNull(wantedExcaption);
    }

    /**
   * Test and invalid method with
   * {@link VSpector#getDeclaredMethod(Class, String, boolean, Class...)} with
   * last param == true. => Should cause exception.
   */
    public void testGetDeclaredMethodForInvalidMethodWithBaseClassCheck() {
        XSpectorTestClass1 tc1 = new XSpectorTestClass1();
        ReflectionException wantedException = null;
        try {
            @SuppressWarnings("unused") Method method = VSpector.getDeclaredMethod(tc1.getClass(), "nonExistingMethod", true);
        } catch (ReflectionException e) {
            wantedException = e;
        }
        assertNotNull("Watned exception expected because we asked for non existing method", wantedException);
    }

    /**
   * Test extracting fields of type byte[].
   */
    public void testGetByteArrayFields() {
        List<Field> byteArrayFields = VSpector.getByteArrayFields(SuperClassTestChild.class);
        assertEquals(byteArrayFields.get(0).getName(), "byteArray1");
        assertEquals(byteArrayFields.get(1).getName(), "byteArray2");
    }

    /**
   * Tests finding a field in a class, its superclasses and subclasses.
   */
    public void testSuperAndSubclasses() {
        List<Class<?>> classes = Arrays.asList(new Class<?>[] { SuperClassTestChild.class, SuperClassTestGrandChild.class });
        Field field = VSpector.getDeclaredField(classes, "parentField", true, true, false);
        assertNotNull(field);
        Exception expectedEx = null;
        try {
            field = VSpector.getDeclaredField(classes, "invalidField", true, true, false);
        } catch (ReflectionException ex) {
            if (ex.getKey() == ReflectionErrors.NoSuchFieldInClassOrSubclasses) {
                expectedEx = ex;
            } else {
                throw ex;
            }
        }
        assertNotNull(expectedEx);
        field = VSpector.getDeclaredField(classes, "parentField", false, false, false);
        assertNull(field);
        field = VSpector.getDeclaredField(classes, "parentField", true, false, true);
        assertNotNull(field);
        field = VSpector.getDeclaredField(classes, "parentField", false, false, true);
        assertNotNull(field);
        field = VSpector.getDeclaredField(classes, "byteArray1", true, true, false);
        assertNotNull(field);
        field = VSpector.getDeclaredField(classes, "grandChildField", true, true, false);
        assertNotNull(field);
    }
}
