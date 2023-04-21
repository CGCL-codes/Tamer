package net.sourceforge.jdefprog.agent.instrumentation;

import java.util.Arrays;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class JavassistUtils {

    /**
	 * Get the canonical name of the class. It should be used
	 * instead of the method getName.
	 */
    public static String getCanonicalName(CtClass cc) {
        String name = cc.getName();
        return name.replaceAll("[$]", ".");
    }

    public static CtMethod getMethod(CtClass cc, String name) {
        CtMethod res = null;
        for (CtMethod m : cc.getMethods()) {
            if (m.getName().equals(name)) {
                if (res != null) throw new IllegalArgumentException("More than one method called " + name + " in " + getCanonicalName(cc));
                res = m;
            }
        }
        if (res == null) {
            throw new IllegalArgumentException("No method called " + name + " in " + getCanonicalName(cc));
        }
        return res;
    }

    public static CtMethod getMethod(CtClass clazz, RelativeBehaviorId bid) {
        return getMethod(clazz, bid.getName(), bid.getParams());
    }

    public static CtBehavior getBehavior(CtClass clazz, RelativeBehaviorId bid) {
        return getBehavior(clazz, bid.getName(), bid.getParams());
    }

    public static CtMethod getMethod(CtClass clazz, String methodName, CtClass[] methodParams) {
        for (CtMethod m : clazz.getMethods()) {
            try {
                if (m.getName().equals(methodName) && Arrays.equals(m.getParameterTypes(), methodParams)) {
                    return m;
                }
            } catch (NotFoundException e) {
                throw new RuntimeException("Not found a class related to parameters of a method, params classes: " + Arrays.toString(methodParams));
            }
        }
        throw new IllegalArgumentException("Method " + methodName + " with params " + Arrays.toString(methodParams) + " not found in class " + JavassistUtils.getCanonicalName(clazz));
    }

    public static CtBehavior getBehavior(CtClass clazz, String methodName, CtClass[] methodParams) {
        for (CtMethod m : clazz.getMethods()) {
            try {
                if (m.getName().equals(methodName) && Arrays.equals(m.getParameterTypes(), methodParams)) {
                    return m;
                }
            } catch (NotFoundException e) {
                throw new RuntimeException("Not found a class related to parameters of a method, params classes: " + Arrays.toString(methodParams));
            }
        }
        for (CtBehavior c : clazz.getConstructors()) {
            try {
                if (c.getName().equals(methodName) && Arrays.equals(c.getParameterTypes(), methodParams)) {
                    return c;
                }
            } catch (NotFoundException e) {
                throw new RuntimeException("Not found a class related to parameters of a method, params classes: " + Arrays.toString(methodParams));
            }
        }
        throw new IllegalArgumentException("Method " + methodName + " with params " + Arrays.toString(methodParams) + " not found in class " + JavassistUtils.getCanonicalName(clazz));
    }
}
