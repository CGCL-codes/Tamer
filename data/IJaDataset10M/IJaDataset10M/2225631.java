package de.indisopht.guice.groovy.utest.performance;

import java.lang.reflect.Proxy;
import net.sf.cglib.proxy.Enhancer;
import org.junit.Test;

public final class ProxyTest {

    @Test
    public void testPerformance() {
        int numberToExecute = 10000;
        MyClass[] classes = new MyClass[numberToExecute];
        for (int i = 0; i < numberToExecute; i++) {
            classes[i] = new MyClassImpl();
        }
        MyClass[] cglibProxyClasses = new MyClassImpl[numberToExecute];
        for (int i = 0; i < numberToExecute; i++) {
            cglibProxyClasses[i] = (MyClass) Enhancer.create(MyClassImpl.class, new SimpleMethodInterceptor());
        }
        MyClass[] jdkProxyClasses = new MyClass[numberToExecute];
        for (int i = 0; i < numberToExecute; i++) {
            jdkProxyClasses[i] = (MyClass) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), new Class[] { MyClass.class }, new SimpleMethodHandler());
        }
        long unproxyExecution = executeClasses(numberToExecute, classes);
        displayResults("Unproxied", unproxyExecution);
        long cglibExecution = executeClasses(numberToExecute, cglibProxyClasses);
        displayResults("cglib", cglibExecution);
        long proxyExecution = executeClasses(numberToExecute, jdkProxyClasses);
        displayResults("Proxy", proxyExecution);
    }

    private void displayResults(String label, long executionTime) {
        System.out.println(label + ": " + executionTime + "(ns) " + (executionTime / 100000) + "(ms)");
    }

    private long executeClasses(int numberToExecute, MyClass[] classes) {
        long start = System.nanoTime();
        for (int i = 0; i < numberToExecute; i++) {
            classes[i].doSomething();
        }
        long end = System.nanoTime();
        long execution = end - start;
        return execution;
    }
}
