package org.nakedobjects.reflector.java.reflect;

import java.lang.reflect.Method;

class ControlMethods {

    private final Method visibleMethod;

    private final Method availableMethod;

    private final Method validMethod1;

    private final Method validMethod2;

    public ControlMethods(Method visibleMethod, Method availableMethod, Method validMethod1, Method validMethod2) {
        this.visibleMethod = visibleMethod;
        this.availableMethod = availableMethod;
        this.validMethod1 = validMethod1;
        this.validMethod2 = validMethod2;
    }

    public final Method getAvailableMethod() {
        return availableMethod;
    }

    public final Method getValidMethod1() {
        return validMethod1;
    }

    public Method getValidMethod2() {
        return validMethod2;
    }

    public final Method getVisibleMethod() {
        return visibleMethod;
    }
}
