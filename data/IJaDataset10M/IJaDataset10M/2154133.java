package org.qedeq.kernel.bo.module;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import org.qedeq.kernel.bo.load.DefaultModuleAddress;
import org.qedeq.kernel.bo.load.DefaultQedeqBo;

/**
 * Test class {@link org.qedeq.kernel.bo.module.QedeqBo}.
 *
 * @version $Revision: 1.8 $
 * @author    Michael Meyling
 */
public class QedeqBoTest extends AbstractBoModuleTest {

    /** This class is tested. */
    private Class clazz = DefaultQedeqBo.class;

    protected Class getTestedClass() {
        return clazz;
    }

    public void pestRest() {
        fail("missing, see TODOs in this class");
    }

    /**
     * Check the setters and getters and <code>add</code>, <code>size</code> and 
     * <code>get(int)</code>.
     * If the attribute names don't match the method names we have a problem.
     *
     * @throws  Exception   Something went wrong. Perhaps the preconditions in {@link #testAll()}
     *                      were violated.
     */
    protected void checkGetterAndSetter() throws Exception {
        final Class clazz = getTestedClass();
        Field[] attrs = clazz.getDeclaredFields();
        if (attrs.length == 0) {
            fail("no attributes found in class " + clazz.getName());
        }
        for (int i = 0; i < attrs.length; i++) {
            String attrName = attrs[i].getName();
            if (attrName.startsWith("__")) {
                continue;
            }
            boolean tested = false;
            tested = tested || testGetSetAdd(clazz, attrs[i]);
            tested = tested || testAddSizeGet(clazz, attrs[i]);
            if ("moduleLabels".equals(attrName)) {
                continue;
            }
            if (!tested) {
                fail("could not test attribute " + attrName + " in class " + clazz.getName());
            }
        }
    }

    /**
     * Tests getter and setter methods for an attribute of a class. Also tests an <code>add</code> 
     * method for list attributes.
     *
     * @param   clazz       Attribute is in this class.
     * @param   attr        Test methods for this attribute.
     * @return  Did this test match? If <code>true</code> all those methods were tested.
     * @throws  Exception   Test failure.
     */
    protected boolean testGetSetAdd(final Class clazz, final Field attr) throws Exception {
        final Method[] methods = clazz.getDeclaredMethods();
        final String attrName = attr.getName();
        Method getter = null;
        Method setter = null;
        Method adder = null;
        for (int j = 0; j < methods.length; j++) {
            if (methods[j].getName().equals("get" + getUName(attrName))) {
                getter = methods[j];
            }
            if (methods[j].getName().equals("set" + getUName(attrName))) {
                setter = methods[j];
            }
            if ((methods[j].getName() + "List").equals("add" + getUName(attrName))) {
                adder = methods[j];
            }
        }
        if (getter == null || setter == null) {
            return false;
        }
        final Object vo = getObject(clazz);
        final Object result1 = getter.invoke(vo, new Object[0]);
        System.out.println(getter.getName());
        if ("getState".equals(getter.getName())) {
            return true;
        }
        assertNull(result1);
        final Object value = getFilledObject(getter.getReturnType(), clazz, attrName);
        setter.invoke(vo, new Object[] { value });
        final Object result2 = getter.invoke(vo, new Object[0]);
        assertEquals(value, result2);
        if (adder != null) {
            final Method size1 = result2.getClass().getMethod("size", new Class[0]);
            final int number1 = ((Integer) size1.invoke(result2, new Object[0])).intValue();
            final Object add = getFilledObject(adder.getParameterTypes()[0], clazz, attrName);
            adder.invoke(vo, new Object[] { add });
            final Object result3 = getter.invoke(vo, new Object[0]);
            final Method size2 = result3.getClass().getMethod("size", new Class[0]);
            final int number2 = ((Integer) size2.invoke(result3, new Object[0])).intValue();
            assertEquals(number1 + 1, number2);
            addChecked(adder);
            removeMethodToCheck(adder.getName());
        }
        setter.invoke(vo, new Object[] { null });
        final Object result4 = getter.invoke(vo, new Object[0]);
        assertNull(result4);
        addChecked(setter);
        removeMethodToCheck(setter.getName());
        addChecked(getter);
        removeMethodToCheck(getter.getName());
        return true;
    }

    /**
     * Test the <code>toString</code> method. This method is also tested with other check methods.
     *
     * @throws  Exception   Something went wrong. Perhaps the preconditions in {@link #testAll()} 
     *                      were violated.
     */
    protected void checkToString() throws Exception {
        {
            final Object vo1 = getObject(getTestedClass());
            assertNotNull(vo1.toString());
            final Object vo2 = getObject(getTestedClass());
            assertEquals(vo1.toString(), vo2.toString());
        }
        removeMethodToCheck("toString");
    }

    /**
     * Test the <code>hashCode</code> method. This method is also tested with other check methods.
     *
     * @throws  Exception   Something went wrong. Perhaps the preconditions in {@link #testAll()} 
     *                      were violated.
     */
    protected void checkHashCode() throws Exception {
    }

    /**
     * Get (if possible) empty instance of an class.
     *
     * @param   clazz   For this class an instance is wanted.
     * @param   parent  This class has <code>clazz</code> as an attribute. Maybe <code>null</code>.
     * @param   attribute   Attribute name of parent that shall be filled.
     * @return  Just the result of the default constructor (if existing).
     * @throws  Exception   Something went wrong. Perhaps the preconditions in {@link #testAll()}
     *                      were violated.
     */
    protected Object getObject(final Class clazz, final Class parent, final String attribute) throws Exception {
        final Object vo = getEmptyObject(clazz, parent, attribute);
        if (vo == null) {
            if (clazz.equals(LoadingState.class)) {
                return LoadingState.STATE_LOADED;
            }
            if (clazz.equals(ModuleAddress.class)) {
                return new DefaultModuleAddress(new URL("http://www.qedeq.org/0_03_03/doc/math/qedeq_set_theory_v1.xml"));
            }
            fail("no default constructor for " + clazz.getName() + " found");
        }
        return vo;
    }

    /**
     * Check equals during successive fill up by calling setters.
     *
     * @throws Exception
     */
    protected void checkEqualsFillUp() throws Exception {
        {
            final Object vo1 = getObject(getTestedClass());
            final Object vo2 = getObject(getTestedClass());
            assertTrue(vo1.equals(vo1));
            assertTrue(vo1.equals(vo2));
            assertTrue(vo2.equals(vo1));
            assertFalse(vo1.equals(null));
            final Method[] methods = getTestedClass().getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().startsWith("set") || methods[i].getName().startsWith("add")) {
                    final Method setter = methods[i];
                    if (setter.getParameterTypes().length > 1) {
                        continue;
                    }
                    final Class setClazz = setter.getParameterTypes()[0];
                    final Object value1 = getFilledObject(setClazz, getTestedClass(), setter.getName());
                    setter.invoke(vo1, new Object[] { value1 });
                    System.out.println(setter.getName());
                    if ("setState".equals(setter.getName())) {
                        continue;
                    }
                    assertFalse(vo1.equals(vo2));
                    assertFalse(vo2.equals(vo1));
                    assertFalse(vo1.equals(null));
                    setter.invoke(vo2, new Object[] { value1 });
                    assertTrue(vo1.equals(vo2));
                    assertTrue(vo2.equals(vo1));
                    assertTrue(vo2.equals(vo1));
                    final Object value2 = getFilledObject(setClazz, getTestedClass(), setter.getName());
                    setter.invoke(vo2, new Object[] { value2 });
                    if (methods[i].getName().startsWith("set")) {
                        assertTrue(vo1.equals(vo2));
                        assertTrue(vo2.equals(vo1));
                    } else {
                        assertFalse(vo1.equals(vo2));
                        assertFalse(vo2.equals(vo1));
                        setter.invoke(vo1, new Object[] { value2 });
                        assertTrue(vo1.equals(vo2));
                        assertTrue(vo2.equals(vo1));
                    }
                }
                assertTrue(vo1.hashCode() == vo2.hashCode());
                assertTrue(vo1.toString().equals(vo2.toString()));
            }
        }
    }

    /**
     * Check equals after calling setter on empty object.
     *
     * @throws Exception
     */
    protected void checkEqualsForEachSetter() throws Exception {
        {
            final Method[] methods = getTestedClass().getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                final Object vo1 = getObject(getTestedClass());
                final Object vo2 = getObject(getTestedClass());
                if (methods[i].getName().startsWith("set") || methods[i].getName().startsWith("add")) {
                    final Method setter = methods[i];
                    if (setter.getParameterTypes().length > 1) {
                        continue;
                    }
                    final Class setClazz = setter.getParameterTypes()[0];
                    setter.invoke(vo1, new Object[] { null });
                    if (methods[i].getName().startsWith("set")) {
                        assertTrue(vo1.equals(vo2));
                        assertTrue(vo2.equals(vo1));
                    } else {
                        assertFalse(vo1.equals(vo2));
                        assertFalse(vo2.equals(vo1));
                    }
                    final Object value1 = getFilledObject(setClazz, getTestedClass(), setter.getName());
                    setter.invoke(vo2, new Object[] { value1 });
                    System.out.println(setter.getName());
                    if ("setState".equals(setter.getName())) {
                        continue;
                    }
                    assertFalse(vo1.equals(vo2));
                    assertFalse(vo2.equals(vo1));
                    assertTrue(vo1.hashCode() != vo2.hashCode());
                    assertFalse(vo1.toString().equals(vo2.toString()));
                }
            }
        }
    }

    /**
     * Test the <code>equals</code> method. This method is also tested with other check methods.
     *
     * @throws  Exception   Something went wrong. Perhaps the preconditions in {@link #testAll()}
     *                      were violated.
     */
    protected void checkEquals() throws Exception {
        checkEqualsFillUp();
        checkEqualsForEachSetter();
        {
            final Object vo1 = getFilledObject(getTestedClass());
            final Object vo2 = getFilledObject(getTestedClass());
            final Object vo3 = getEmptyObject(getTestedClass(), null, null);
            assertTrue(vo1.equals(vo1));
            assertTrue(vo1.equals(vo2));
            assertTrue(vo2.equals(vo1));
            assertFalse(vo1.equals(null));
            assertFalse(vo1.equals(vo3));
        }
        {
            Field[] attrs = getTestedClass().getDeclaredFields();
            for (int i = 0; i < attrs.length; i++) {
                if (attrs[i].getName().startsWith("__")) {
                    continue;
                }
                final Object vo1 = getFilledObject(attrs[i].getType(), getTestedClass(), "", attrs[i].getName());
                final Object vo2 = getFilledObject(attrs[i].getType(), getTestedClass(), "", attrs[i].getName());
                assertTrue(vo1.equals(vo1));
                System.out.println(attrs[i].getName());
                if ("moduleLabels".equals(attrs[i].getName())) {
                    continue;
                }
                assertTrue(vo1.equals(vo2));
                assertTrue(vo2.equals(vo1));
            }
        }
        removeMethodToCheck("equals");
    }

    /**
     * Test if all methods of the value object were tested..
     */
    protected void checkAllChecked() {
        removeMethodToCheck("getState");
        removeMethodToCheck("setState");
        removeMethodToCheck("hashCode");
        removeMethodToCheck("toString");
        removeMethodToCheck("getModuleAddress");
        removeMethodToCheck("getModuleLabels");
        super.checkAllChecked();
    }
}
