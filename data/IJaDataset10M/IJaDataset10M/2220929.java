package org.monkeypuzzler.css;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.monkeypuzzler.utils.Interceptor;
import org.monkeypuzzler.utils.SimpleHandler;
import com.thoughtworks.selenium.Selenium;

public class TestRecorder {

    @Test
    public void testStore() throws Exception {
        final String locator = "id:Test";
        final String id = "preClick";
        final String evalElement = new SimpleTestElement().toString();
        SimpleInterceptor eval = new SimpleInterceptor("getEval") {

            public Object intercept(Object proxy, Method method, Object[] args) {
                called = true;
                Assert.assertEquals("this.returnJsonFromLocator(" + locator + ");", args[0]);
                return evalElement;
            }
        };
        SimpleInterceptor store = new SimpleInterceptor("store") {

            public Object intercept(Object proxy, Method method, Object[] args) {
                called = true;
                Assert.assertEquals(locator, args[0]);
                Assert.assertEquals(id, args[1]);
                Assert.assertNotNull(args[2]);
                return null;
            }
        };
        Recorder recorder = new Recorder(getSeleniumProxy(eval), getPersistenceStrategyProxy(store));
        recorder.store(locator, id);
        Assert.assertTrue("eval was not called", eval.called);
        Assert.assertTrue("store was not called", store.called);
    }

    @Test
    public void testAssertElementNotPersisted() throws Exception {
        final String locator = "id:Test";
        final String id = "preClick";
        final String evalElement = new SimpleTestElement().toString();
        SimpleInterceptor eval = new SimpleInterceptor("getEval") {

            public Object intercept(Object proxy, Method method, Object[] args) {
                called = true;
                Assert.assertEquals("this.returnJsonFromLocator(" + locator + ");", args[0]);
                return evalElement;
            }
        };
        SimpleInterceptor store = new SimpleInterceptor("store") {

            public Object intercept(Object proxy, Method method, Object[] args) {
                called = true;
                Assert.assertEquals(locator, args[0]);
                Assert.assertEquals(id, args[1]);
                Assert.assertNotNull(args[2]);
                return null;
            }
        };
        SimpleInterceptor exists = new SimpleInterceptor("exists") {

            public Object intercept(Object proxy, Method method, Object[] args) {
                called = true;
                Assert.assertEquals(locator, args[0]);
                Assert.assertEquals(id, args[1]);
                return false;
            }
        };
        Recorder recorder = new Recorder(getSeleniumProxy(eval), getPersistenceStrategyProxy(store, exists));
        recorder.assertElement(locator, id);
        Assert.assertTrue("exists was not called", exists.called);
        Assert.assertTrue("eval was not called", eval.called);
        Assert.assertTrue("store was not called", store.called);
    }

    @Test
    public void testAssertElementPersisted() throws Exception {
        final String locator = "id:Test";
        final String id = "preClick";
        final String evalElement = new SimpleTestElement().toString();
        final Element persisted = new Element(locator);
        persisted.update(evalElement);
        SimpleInterceptor eval = new SimpleInterceptor("getEval") {

            public Object intercept(Object proxy, Method method, Object[] args) {
                called = true;
                Assert.assertEquals("this.returnJsonFromLocator(" + locator + ");", args[0]);
                return evalElement;
            }
        };
        SimpleInterceptor retrieve = new SimpleInterceptor("retrieve") {

            public Object intercept(Object proxy, Method method, Object[] args) {
                called = true;
                Assert.assertEquals(locator, args[0]);
                Assert.assertEquals(id, args[1]);
                return persisted;
            }
        };
        SimpleInterceptor exists = new SimpleInterceptor("exists") {

            public Object intercept(Object proxy, Method method, Object[] args) {
                called = true;
                Assert.assertEquals(locator, args[0]);
                Assert.assertEquals(id, args[1]);
                return true;
            }
        };
        Recorder recorder = new Recorder(getSeleniumProxy(eval), getPersistenceStrategyProxy(retrieve, exists));
        recorder.assertElement(locator, id);
        Assert.assertTrue("exists was not called", exists.called);
        Assert.assertTrue("eval was not called", eval.called);
        Assert.assertTrue("retrieve was not called", retrieve.called);
    }

    static PersistenceStrategy getPersistenceStrategyProxy(Interceptor... in) {
        InvocationHandler handler = new SimpleHandler(in);
        return (PersistenceStrategy) Proxy.newProxyInstance(TestRecorder.class.getClassLoader(), new Class[] { PersistenceStrategy.class }, handler);
    }

    static Selenium getSeleniumProxy(Interceptor... in) {
        InvocationHandler handler = new SimpleHandler(in);
        return (Selenium) Proxy.newProxyInstance(TestElement.class.getClassLoader(), new Class[] { Selenium.class }, handler);
    }

    abstract static class SimpleInterceptor implements Interceptor {

        final String methodName;

        public boolean called = false;

        public SimpleInterceptor(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return this.methodName;
        }

        public abstract Object intercept(Object proxy, Method method, Object[] args);
    }
}
