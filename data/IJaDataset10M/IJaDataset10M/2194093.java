package com.google.code.jtracert.traceBuilder;

import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.traceBuilder.impl.MethodCallTraceBuilderImpl;
import java.lang.reflect.Method;
import java.lang.instrument.Instrumentation;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class MethodCallTraceBuilderFactory {

    private static MethodCallTraceBuilder methodCallTraceBuilder;

    private static Method methodCallTraceBuilderFactoryMethod;

    private static final String FACTORY_METHOD_NAME = "getMethodCallTraceBuilder";

    /**
     * @return
     */
    public static MethodCallTraceBuilder getMethodCallTraceBuilder() {
        if (null == methodCallTraceBuilder) {
            methodCallTraceBuilder = new MethodCallTraceBuilderImpl();
        }
        return methodCallTraceBuilder;
    }

    /**
     * @param analyzeProperties
     */
    public static void configureMethodCallTraceBuilder(AnalyzeProperties analyzeProperties, Instrumentation instrumentation) {
        getMethodCallTraceBuilder().setAnalyzeProperties(analyzeProperties);
        getMethodCallTraceBuilder().setInstrumentation(instrumentation);
    }

    /**
     * @param methodCallTraceBuilder
     */
    public static void setMethodCallTraceBuilder(MethodCallTraceBuilder methodCallTraceBuilder) {
        MethodCallTraceBuilderFactory.methodCallTraceBuilder = methodCallTraceBuilder;
    }

    /**
     * @return
     */
    public static Method getMethodCallTraceBuilderFactoryMethod() {
        if (null == methodCallTraceBuilderFactoryMethod) {
            try {
                methodCallTraceBuilderFactoryMethod = MethodCallTraceBuilderFactory.class.getMethod(FACTORY_METHOD_NAME, (Class) null);
            } catch (NoSuchMethodException e) {
                methodCallTraceBuilderFactoryMethod = null;
            }
        }
        return methodCallTraceBuilderFactoryMethod;
    }

    /**
     * This method is called from java.lang.Object constructor and hence cannot create new objects
     * creating arrays is permitted however
     *
     * @todo imlement double buffer and process created objects in a separate thread
     *
     * when jTracert is working, this method shouldn't be called for jTracert internal classes
     * @todo implement a thread local boolean flag using java.lang.Thread class instrumentation and
     * @todo handling an arrays of boolean flags in this method
     *
     * The class, this method belongs to, must be loaded by bootstrap constructor
     * @todo instrument class like java.lang.System and add this method to System class using instrumentation
     *
     * @todo statement above is wrong - we cannot add new methods to java.lang.System class
     * @todo instead we should use ClassLoader.getSystemClassLoader() class and reflection
     *
     * @param o
     */
    public static synchronized void constructor(Object o) {
    }

    public static ThreadLocal<Boolean> instrumenting = new ThreadLocal<Boolean>() {

        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    public static void test() {
        System.out.println("test");
    }
}
