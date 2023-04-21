package com.em.validation.client.model.tests;

import javax.validation.ConstraintValidatorFactory;
import com.em.validation.client.ConstraintValidatorFactoryImpl;
import com.em.validation.client.reflector.IReflectorFactory;
import com.em.validation.client.reflector.ReflectorFactory;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtValidationBaseTestCase extends GWTTestCase {

    @Override
    public String getModuleName() {
        return null;
    }

    public IReflectorFactory getReflectorFactory() {
        return ReflectorFactory.INSTANCE;
    }

    public ConstraintValidatorFactory getConstraintValidationFactory() {
        return new ConstraintValidatorFactoryImpl();
    }
}
