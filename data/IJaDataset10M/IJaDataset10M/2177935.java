package jp.dodododo.aop.impl;

import java.lang.reflect.Method;
import java.util.regex.Pattern;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import jp.dodododo.aop.annotation.Enhance;

@Enhance(false)
public class DefaultPointcutImpl {

    public DefaultPointcutImpl() {
    }

    public boolean isApplied(Method targetMethod) {
        Class<?> declaringClass = targetMethod.getDeclaringClass();
        return declaredMethodInInterface(declaringClass, targetMethod.getName(), targetMethod.getParameterTypes());
    }

    public boolean isApplied(CtBehavior methodOrConstructor) {
        if (methodOrConstructor instanceof CtMethod) {
            return isApplied((CtMethod) methodOrConstructor);
        } else if (methodOrConstructor instanceof CtConstructor) {
            return isApplied((CtConstructor) methodOrConstructor);
        } else {
            throw new UnsupportedOperationException("UnsupportedType.[" + methodOrConstructor.getClass() + "]");
        }
    }

    public boolean isApplied(CtConstructor constructor) {
        return false;
    }

    public boolean isApplied(CtMethod targetMethod) {
        CtClass declaringClass = targetMethod.getDeclaringClass();
        try {
            return declaredMethodInInterface(declaringClass, targetMethod.getName(), targetMethod.getParameterTypes());
        } catch (NotFoundException e) {
            return false;
        }
    }

    private boolean declaredMethodInInterface(CtClass targetClass, String methodName, CtClass[] paramTypes) throws NotFoundException {
        if (targetClass == null || Object.class.equals(targetClass)) {
            return false;
        }
        CtClass[] interfaces = targetClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            CtClass interfaceClass = interfaces[i];
            CtMethod declaredMethod = interfaceClass.getDeclaredMethod(methodName, paramTypes);
            if (declaredMethod != null) {
                return true;
            }
        }
        return declaredMethodInInterface(targetClass.getSuperclass(), methodName, paramTypes);
    }

    private boolean declaredMethodInInterface(Class<?> targetClass, String methodName, Class<?>[] paramTypes) {
        if (targetClass == null || Object.class.equals(targetClass)) {
            return false;
        }
        Class<?>[] interfaces = targetClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Class<?> interfaceClass = interfaces[i];
            try {
                interfaceClass.getDeclaredMethod(methodName, paramTypes);
                return true;
            } catch (SecurityException ignore) {
            } catch (NoSuchMethodException ignore) {
            }
        }
        return declaredMethodInInterface(targetClass.getSuperclass(), methodName, paramTypes);
    }

    public Pattern[] getIgnorePatterns() {
        return new Pattern[0];
    }

    public Pattern[] getPatterns() {
        return new Pattern[0];
    }
}
