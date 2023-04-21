package com.choicemaker.shared.util;

import java.lang.reflect.Method;
import java.util.Set;
import junit.framework.TestCase;
import com.choicemaker.shared.util.ReflectionTestInterface.F;

public class ReflectionTest extends TestCase {

    /** Checks if this test is valid */
    public void testPrerequisites() {
        Set<String> validNames = Utils.getReflectionTestEnumMethodNames();
        Utils.checkAccessorNames(ReflectionTestBean.class, validNames);
    }

    public void testGetFieldAccessorClassOfVStringBoolean() {
        for (F f : F.values()) {
            String methodName = f.p.prefix + f.nameStem;
            try {
                Method m = CMReflectionUtils.getFieldAccessor(ReflectionTestBean.class, methodName);
                assertTrue(f.nameStem + ": ", m != null);
            } catch (Exception x) {
                fail(f.nameStem + ": " + x.toString());
            }
        }
        for (F f : F.values()) {
            try {
                Method m = CMReflectionUtils.getFieldAccessor(ReflectionTestBean.class, f.nameStem, f.p);
                assertTrue(f.nameStem + ": ", m != null);
            } catch (Exception x) {
                fail(f.nameStem + ": " + x.toString());
            }
        }
    }

    public void testIsFieldAccessor() {
        for (F f : F.values()) {
            try {
                Method m = CMReflectionUtils.getFieldAccessor(ReflectionTestBean.class, f.nameStem, f.p);
                assertTrue(f.nameStem + ": ", m != null);
                assertTrue(f.nameStem + ": ", CMReflectionUtils.isFieldAccessor(m));
            } catch (Exception x) {
                fail(f.nameStem + ": " + x.toString());
            }
        }
    }
}
