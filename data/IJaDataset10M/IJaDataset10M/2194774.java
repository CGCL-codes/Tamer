package com.wgo.precise.client.ui.model;

import java.util.logging.Logger;
import java.util.logging.Level;
import com.wgo.precise.client.ui.model.PropertyType;
import remato.common.domain.property.PropertyTypeEnumImpl;
import junit.framework.Assert;
import junit.framework.TestCase;

public class PropertyTypeTest extends TestCase {

    private static final transient Logger logger = Logger.getLogger(PropertyTypeTest.class.getSimpleName());

    public void testPropertyTypeEnumsImplementedInGlientModel() {
        testEnumImplementation(PropertyType.class, PropertyTypeEnumImpl.values(), "Incomplete client implementation.");
    }

    public void testPropertyTypesImplementedInDomainModel() {
        testEnumImplementation(PropertyTypeEnumImpl.class, PropertyType.getPropertyTypes(), "Incomplete model implementation.");
    }

    private <T extends Enum<T>> void testEnumImplementation(Class<T> enumClass, Enum[] enums, String errorMessage) {
        for (Enum enumImpl : enums) {
            T enumValue = null;
            try {
                enumValue = Enum.valueOf(enumClass, enumImpl.name());
            } catch (IllegalArgumentException e) {
                logger.log(Level.WARNING, errorMessage, e);
                Assert.assertNull(errorMessage, e);
            }
            Assert.assertNotNull(errorMessage, enumValue);
        }
    }

    public void testPropertyTypeGetDefaultValue() {
        for (PropertyType propertyType : PropertyType.getPropertyTypes()) {
            try {
                propertyType.createNewValue();
            } catch (Throwable e) {
                logger.log(Level.WARNING, propertyType.name() + ": " + e.getMessage(), e);
                Assert.assertNull(e.getMessage(), e);
            }
        }
    }
}
