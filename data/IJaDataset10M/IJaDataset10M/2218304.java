package fr.mmteam.selenium.ext;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.openqa.selenium.WebDriver;

public class SeleniumExtRunner extends Suite {

    /**
     * Annotation for a method which provides parameters to be injected into the test class constructor by <code>Parameterized</code>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.TYPE })
    public static @interface BrowserConfig {

        Class<? extends WebDriver> driverClass();

        String title();
    }

    private class TestClassRunnerForMultipleDrivers extends BlockJUnit4ClassRunner {

        private final WebDriverExtBuilder builder;

        private String title;

        private final WebDriverExt driver;

        TestClassRunnerForMultipleDrivers(Class<?> type, String title, WebDriverExtBuilder builder) throws InitializationError {
            super(type);
            this.builder = builder;
            this.title = title;
            this.driver = builder.build();
        }

        @Override
        protected String getName() {
            return String.format("[%s]", title);
        }

        @Override
        protected String testName(final FrameworkMethod method) {
            return String.format("%s[%s]", method.getName(), title);
        }

        @Override
        protected Statement classBlock(RunNotifier notifier) {
            return childrenInvoker(notifier);
        }

        @Override
        protected Annotation[] getRunnerAnnotations() {
            return new Annotation[0];
        }

        @Override
        protected Statement methodInvoker(FrameworkMethod method, Object test) {
            return new InvokeMethodWithWebDriverExt(method, test, driver);
        }

        @Override
        protected void validateTestMethods(List<Throwable> errors) {
            List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(Test.class);
            for (FrameworkMethod eachTestMethod : methods) {
                eachTestMethod.validatePublicVoid(false, errors);
                Method fMethod = eachTestMethod.getMethod();
                if (fMethod.getParameterTypes().length != 1 && fMethod.getParameterTypes()[0].isAssignableFrom(WebDriverExt.class)) errors.add(new Exception("Method " + fMethod.getName() + " should have a single parameter of type WebDriverExtended"));
            }
        }
    }

    private class InvokeMethodWithWebDriverExt extends Statement {

        private final FrameworkMethod fTestMethod;

        private Object fTarget;

        private WebDriverExt driver;

        public InvokeMethodWithWebDriverExt(FrameworkMethod testMethod, Object target, WebDriverExt driver) {
            fTestMethod = testMethod;
            fTarget = target;
            this.driver = driver;
        }

        @Override
        public void evaluate() throws Throwable {
            fTestMethod.invokeExplosively(fTarget, driver);
        }
    }

    private final ArrayList<Runner> runners = new ArrayList<Runner>();

    /**
     * Only called reflectively. Do not use programmatically.
     */
    public SeleniumExtRunner(Class<?> klass) throws Throwable {
        super(klass, Collections.<Runner>emptyList());
        Map<String, WebDriverExtBuilder> configuredBuilders = getConfiguredDriverBuilder(getTestClass());
        for (String title : configuredBuilders.keySet()) {
            runners.add(new TestClassRunnerForMultipleDrivers(getTestClass().getJavaClass(), title, configuredBuilders.get(title)));
        }
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

    @SuppressWarnings("unchecked")
    private Map<String, WebDriverExtBuilder> getConfiguredDriverBuilder(TestClass klass) throws Throwable {
        Map<String, WebDriverExtBuilder> result = new LinkedHashMap<String, WebDriverExtBuilder>();
        for (FrameworkMethod configMethod : getBrowserConfigMethods(klass)) {
            WebDriverExtBuilder builder = new WebDriverExtBuilder().setDriverClass(configMethod.getAnnotation(BrowserConfig.class).driverClass());
            configMethod.invokeExplosively(null, builder);
            result.put(configMethod.getAnnotation(BrowserConfig.class).title(), builder);
        }
        return result;
    }

    private List<FrameworkMethod> getBrowserConfigMethods(TestClass testClass) throws Exception {
        List<FrameworkMethod> methods = testClass.getAnnotatedMethods(BrowserConfig.class);
        for (FrameworkMethod each : methods) {
            int modifiers = each.getMethod().getModifiers();
            if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers) || !each.getMethod().getReturnType().equals(Void.TYPE)) throw new Exception("Browser config method " + each.getName() + " on class " + testClass.getName() + " must be public static void");
        }
        return methods;
    }
}
