package com.technoetic.xplanner.easymock;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.apache.commons.lang.StringUtils;
import org.easymock.ArgumentsMatcher;

public class BeanArgumentMatcher implements ArgumentsMatcher {

    private List excludedProperties;

    private List includedProperties;

    private List actualObjects = new ArrayList();

    public BeanArgumentMatcher() {
        excludedProperties = Collections.EMPTY_LIST;
        includedProperties = Collections.EMPTY_LIST;
    }

    public BeanArgumentMatcher(String[] includedProperties, String[] excludedProperties) {
        this.includedProperties = includedProperties == null ? Collections.EMPTY_LIST : Arrays.asList(includedProperties);
        this.excludedProperties = excludedProperties == null ? Collections.EMPTY_LIST : Arrays.asList(excludedProperties);
    }

    public boolean matches(Object[] expected, Object[] actual) {
        actualObjects.add(actual);
        try {
            for (int i = 0; i < expected.length; i++) {
                Object expectedObject = expected[i];
                Object actualObject = actual[i];
                assertEquals(expectedObject, actualObject);
            }
        } catch (AssertionFailedError e) {
            throw e;
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return true;
    }

    public void assertEquals(Object expectedObject, Object actualObject) throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(expectedObject.getClass());
        PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
        for (int j = 0; j < descriptors.length; j++) {
            PropertyDescriptor descriptor = descriptors[j];
            if ((includedProperties.size() == 0 || includedProperties.contains(descriptor.getName())) && !excludedProperties.contains(descriptor.getName())) {
                if (descriptor.getName().equals("class")) {
                    continue;
                }
                Object expectedValue = null;
                Object actualValue = null;
                try {
                    expectedValue = readValue(descriptor, expectedObject);
                    actualValue = readValue(descriptor, actualObject);
                } catch (Exception e) {
                    Assert.fail("property access failed: " + descriptor.getName());
                }
                Assert.assertEquals("property not equal: " + descriptor.getName(), expectedValue, actualValue);
            }
        }
    }

    private Object readValue(PropertyDescriptor descriptor, Object expectedObject) throws IllegalAccessException, InvocationTargetException {
        return descriptor.getReadMethod() != null ? descriptor.getReadMethod().invoke(expectedObject, null) : null;
    }

    public List getActualObjects() {
        return actualObjects;
    }

    public String toString(Object[] arguments) {
        return StringUtils.join(arguments, ",");
    }
}
