package net.sf.balm.workflow.spring;

import java.lang.reflect.Method;

public interface AttributeFinderCallback {

    public Object find(Method method);
}
