package org.python.compiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import org.python.core.imp;
import org.python.util.Generic;

public class VerifyingClassFile extends ClassFile {

    public VerifyingClassFile(String name, String superclass) {
        super(name, superclass, imp.NO_MTIME, null);
    }

    public boolean noteSuperclassMethod(Method meth) {
        return superclassMeths.add(new MethodDescr(meth));
    }

    public boolean hasMethod(String name, Class<?> returns, Class<?>[] parameters, Class<?>[] thrown) {
        return false;
    }

    public boolean hasConstructor(Class<?>[] parameterClasses, Class<?>[] thrownClasses) {
        return false;
    }

    public void noteSuperclassConstructor(Constructor<?> constructor) {
    }

    public Code addMethod(int access, Class<?> returns, String name, Class<?>... parameters) {
        return addMethod(access, returns, name, parameters, new Class<?>[0]);
    }

    public Code addMethod(int access, Class<?> returns, String name, Class<?>[] parameters, Class<?>... exceptions) {
        return addMethod(name, ProxyCodeHelpers.makeSig(returns, parameters), access, ProxyCodeHelpers.mapClasses(exceptions));
    }

    private static class MethodDescr {

        public final Class<?> returnType;

        public final String name;

        public final Class<?>[] parameters;

        public final Class<?>[] exceptions;

        public MethodDescr(Method m) {
            this(m.getName(), m.getReturnType(), m.getParameterTypes(), m.getExceptionTypes());
        }

        public MethodDescr(String name, Class<?> returnType, Class<?>[] parameters, Class<?>[] exceptions) {
            this.name = name;
            this.returnType = returnType;
            this.parameters = parameters;
            this.exceptions = exceptions;
        }

        @Override
        public int hashCode() {
            return name.hashCode() + parameters.length;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof MethodDescr)) {
                return false;
            }
            MethodDescr oDescr = (MethodDescr) obj;
            if (!name.equals(oDescr.name) || parameters.length != oDescr.parameters.length) {
                return false;
            }
            for (int i = 0; i < parameters.length; i++) {
                if (!parameters[i].equals(oDescr.parameters[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class ConstructorDescr extends MethodDescr {

        public ConstructorDescr(Constructor<?> cons) {
            this(cons.getParameterTypes(), cons.getExceptionTypes());
        }

        public ConstructorDescr(Class<?>[] parameters, Class<?>[] exceptions) {
            super("<init>", Void.TYPE, parameters, exceptions);
        }
    }

    protected final Set<MethodDescr> superclassMeths = Generic.set();
}
