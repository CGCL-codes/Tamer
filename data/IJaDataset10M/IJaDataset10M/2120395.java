package com.ovea.acidmelon.agent.testing;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class AcidMelonFeatureAdapter implements AcidMelonFeature {

    @Override
    public void init(Class<?> testClass) throws Throwable {
    }

    @Override
    public void beforeClass(Class<?> testClass) throws Throwable {
    }

    @Override
    public void afterClass(Class<?> testClass) throws Throwable {
    }

    @Override
    public void init(Object testInstance, Method method) throws Throwable {
    }

    @Override
    public void before(Object testInstance, Method method) throws Throwable {
    }

    @Override
    public void after(Object testInstance, Method method) throws Throwable {
    }
}
