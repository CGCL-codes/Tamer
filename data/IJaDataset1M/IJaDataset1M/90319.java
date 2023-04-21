package org.powermock.tests.utils.impl;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedList;
import java.util.List;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.internal.IndicateReloadClass;
import org.powermock.tests.utils.TestClassesExtractor;

/**
 * Default implementation of the {@link TestClassesExtractor} interface.
 * 
 * @author Johan Haleby
 */
public class PrepareForTestExtractorImpl implements TestClassesExtractor {

    /**
	 * {@inheritDoc}
	 */
    public String[] getTestClasses(AnnotatedElement element) {
        List<String> all = new LinkedList<String>();
        PrepareForTest prepareAnnotation = element.getAnnotation(PrepareForTest.class);
        if (prepareAnnotation != null) {
            final Class<?>[] classesToMock = prepareAnnotation.value();
            for (Class<?> classToMock : classesToMock) {
                if (!classToMock.equals(IndicateReloadClass.class)) {
                    all.add(classToMock.getName());
                }
            }
            String[] fullyQualifiedNames = prepareAnnotation.fullyQualifiedNames();
            for (String string : fullyQualifiedNames) {
                if (!"".equals(string)) {
                    all.add(string);
                }
            }
        }
        return all.toArray(new String[0]);
    }
}
