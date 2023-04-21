package org.t2framework.lucy.aop.javassist.util;

import javassist.ClassPool;
import javassist.CtClass;

public class LucyClassPool extends ClassPool {

    public LucyClassPool() {
        super();
    }

    @Override
    public CtClass removeCached(String classname) {
        return super.removeCached(classname);
    }

    public int size() {
        return classes.size();
    }
}
