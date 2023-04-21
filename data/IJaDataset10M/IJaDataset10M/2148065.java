package org.ungoverned.radical;

import java.lang.reflect.*;

public class InitializerInvocation extends MethodInvocation {

    private boolean m_designTime = true;

    public InitializerInvocation(String className, String methodName, Object param, boolean designTime) {
        super(className, methodName, param);
        m_designTime = designTime;
    }

    public InitializerInvocation(Class clazz, Method method, Object param, boolean designTime) {
        super(clazz, method, param);
        m_designTime = designTime;
    }

    public InitializerInvocation(InitializerInvocation invoke) {
        this(invoke.getTargetClass(), invoke.getMethod(), invoke.getParameter(), invoke.isDesignTime());
    }

    public InitializerInvocation(MethodInvocation invoke) {
        this(invoke.getTargetClass(), invoke.getMethod(), invoke.getParameter(), false);
    }

    public boolean isDesignTime() {
        return m_designTime;
    }

    public void setDesignTime(boolean b) {
        m_designTime = b;
    }
}
