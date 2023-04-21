package org.cantaloop.cgimlet.lang.java;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.cantaloop.cgimlet.lang.Expr;
import org.cantaloop.cgimlet.lang.MethodTemplate;
import org.cantaloop.cgimlet.lang.MethodType;
import org.cantaloop.cgimlet.lang.Type;
import org.cantaloop.cgimlet.lang.Variable;

public class JMethodType implements MethodType {

    private String m_methodName;

    private Type m_returnType;

    private Type[] m_paramTypes;

    JMethodType(Method m) {
        m_methodName = m.getName();
        m_returnType = new JType(m.getReturnType());
        Class[] paramTypes = m.getParameterTypes();
        m_paramTypes = new JType[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            m_paramTypes[i] = new JType(paramTypes[i]);
        }
    }

    JMethodType(MethodTemplate mt) {
        m_returnType = mt.getReturnType();
        m_paramTypes = mt.getParameterTypes();
        m_methodName = mt.getName();
    }

    public String getName() {
        return m_methodName;
    }

    public Type getReturnType() {
        return m_returnType;
    }

    public Type[] getParameterTypes() {
        return m_paramTypes;
    }

    public Expr invoke(Expr target) {
        return invoke(target, new Expr[] {});
    }

    public Expr invoke(Expr target, Expr[] params) {
        Type[] ptypes = JHelper.getTypes(params);
        for (int i = 0; i < ptypes.length; i++) {
            if (!m_paramTypes[i].isAssignableFrom(ptypes[i])) {
                throw new IllegalArgumentException("Method parameters not compatible. Given types: " + Arrays.asList(ptypes) + ", but expected: " + Arrays.asList(m_paramTypes) + ". " + m_paramTypes[i] + " is not assignable from " + ptypes[i]);
            }
        }
        return new JMethodInvocationExpr(target, m_methodName, params);
    }
}
