package org.middleheaven.core.reflection;

public class ClassloadingException extends ReflectionException {

    public ClassloadingException(String msg) {
        super(msg);
    }

    public ClassloadingException(Exception e) {
        super(e);
    }
}
