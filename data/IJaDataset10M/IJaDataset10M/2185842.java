package org.agileinsider.concordion.command;

import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import java.lang.annotation.Annotation;
import java.util.List;

public class MethodInvoker {

    public void invokeAnnotatedMethods(Object fixtureObject, Class<? extends Annotation> annotationClass, ResultRecorder resultRecorder) {
        TestClass testClass = new TestClass(fixtureObject.getClass());
        List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods(annotationClass);
        for (FrameworkMethod annotatedMethod : annotatedMethods) {
            try {
                annotatedMethod.invokeExplosively(fixtureObject);
            } catch (Throwable e) {
                resultRecorder.record(Result.EXCEPTION);
            }
        }
    }
}
