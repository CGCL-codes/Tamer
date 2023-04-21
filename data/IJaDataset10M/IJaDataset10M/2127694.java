package org.codehaus.groovy.grails.plugins.batch;

import grails.util.Environment;
import groovy.lang.Closure;
import java.util.Map;
import org.codehaus.groovy.grails.commons.AbstractGrailsClass;

class DefaultGrailsMainClass extends AbstractGrailsClass {

    public static final String MAIN = "Main";

    public DefaultGrailsMainClass(Class<?> clazz) {
        super(clazz, MAIN);
    }

    @SuppressWarnings("serial")
    private static Closure BLANK_CLOSURE = new Closure(DefaultGrailsMainClass.class) {

        @Override
        public Object call(Object[] args) {
            return null;
        }
    };

    public Closure getInitClosure() {
        return getNamedClosure("init");
    }

    public Closure getRunClosure() {
        return getNamedClosure("run");
    }

    public Closure getDestroyClosure() {
        return getNamedClosure("destroy");
    }

    public void callInit() {
        Closure init = getInitClosure();
        if (init != null) {
            Environment.executeForCurrentEnvironment(init);
        }
    }

    public void callRun(Map<String, Object> context) {
        Closure run = getRunClosure();
        if (run != null) {
            run = run.curry(new Object[] { context });
            Environment.executeForCurrentEnvironment(run);
        }
    }

    public void callDestroy() {
        Closure destroy = getDestroyClosure();
        if (destroy != null) {
            Environment.executeForCurrentEnvironment(destroy);
        }
    }

    private Closure getNamedClosure(String name) {
        Object obj = getPropertyValueObject(name);
        if (obj instanceof Closure) {
            return (Closure) obj;
        }
        return BLANK_CLOSURE;
    }
}
