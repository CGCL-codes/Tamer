package gnu.testlet.java2.lang.reflect.AccessibleObject;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ReflectPermission;
import java.security.Permission;
import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;
import gnu.testlet.TestSecurityManager;

public class security implements Testlet {

    public void test(TestHarness harness) {
        try {
            harness.checkPoint("constructor");
            Constructor constructor = ClassLoader.class.getDeclaredConstructor(new Class[0]);
            int mods = constructor.getModifiers();
            harness.check(Modifier.isPrivate(mods) || Modifier.isProtected(mods));
            try {
                constructor.newInstance(new Object[0]);
                harness.check(false);
            } catch (IllegalAccessException ex) {
                harness.check(true);
            }
            harness.checkPoint("field");
            Field field = String.class.getDeclaredField("serialVersionUID");
            mods = field.getModifiers();
            harness.check(Modifier.isPrivate(mods) || Modifier.isProtected(mods));
            try {
                field.get("");
                harness.check(false);
            } catch (IllegalAccessException ex) {
                harness.check(true);
            }
            harness.checkPoint("method");
            Method method = ClassLoader.class.getDeclaredMethod("getPackages", new Class[0]);
            mods = method.getModifiers();
            harness.check(Modifier.isPrivate(mods) || Modifier.isProtected(mods));
            try {
                method.invoke(getClass().getClassLoader(), new Object[0]);
                harness.check(false);
            } catch (IllegalAccessException ex) {
                harness.check(true);
            }
            AccessibleObject[] objects = new AccessibleObject[] { constructor, field, method };
            AccessibleObject class_constructor = Class.class.getDeclaredConstructors()[0];
            Permission[] suppressAccessChecks = new Permission[] { new ReflectPermission("suppressAccessChecks") };
            TestSecurityManager sm = new TestSecurityManager(harness);
            try {
                sm.install();
                harness.checkPoint("setAccessible (per-object)");
                for (int i = 0; i < objects.length; i++) {
                    try {
                        sm.prepareChecks(suppressAccessChecks);
                        objects[i].setAccessible(true);
                        sm.checkAllChecked();
                    } catch (SecurityException ex) {
                        harness.debug(ex);
                        harness.check(false, "unexpected check");
                    }
                }
                harness.checkPoint("setAccessible (class constructor)");
                try {
                    sm.prepareChecks(suppressAccessChecks);
                    class_constructor.setAccessible(true);
                    harness.check(false);
                } catch (SecurityException ex) {
                    sm.checkAllChecked();
                }
                harness.checkPoint("setAccessible (array)");
                try {
                    sm.prepareChecks(suppressAccessChecks);
                    AccessibleObject.setAccessible(objects, true);
                    sm.checkAllChecked();
                } catch (SecurityException ex) {
                    harness.debug(ex);
                    harness.check(false, "unexpected check");
                }
            } finally {
                sm.uninstall();
            }
        } catch (Exception ex) {
            harness.debug(ex);
            harness.check(false, "Unexpected exception");
        }
    }
}
