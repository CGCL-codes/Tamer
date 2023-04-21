package org.conann.tests;

import com.google.common.collect.Lists;
import static junit.framework.Assert.*;
import org.conann.configuration.Configuration;
import org.conann.metadata.records.Record;
import org.conann.util.UtilityClass;
import java.lang.annotation.Annotation;
import java.util.Collection;

@UtilityClass
public abstract class WebBeansTestUtil {

    public static void assertHasAnnotation(Record<?> record, Class<? extends Annotation> annotationType) {
        assertNotNull(record);
        assertNotNull(annotationType);
        assertTrue(containsAnnotationType(record.getAllAnnotations(), annotationType));
        for (Annotation annotation : record.getAllAnnotations()) {
            if (annotationType.equals(annotation.annotationType())) {
                return;
            }
        }
        fail("Record of " + record.getElement() + " is not annotated with @" + annotationType.getSimpleName());
    }

    public static void assertContainsAnnotationType(Collection<Annotation> annotations, Class<? extends Annotation> annotationType) {
        assertNotNull(annotations);
        assertNotNull(annotationType);
        assertFalse(annotations.isEmpty());
        for (Annotation annotation : annotations) {
            if (annotationType.equals(annotation.annotationType())) {
                return;
            }
        }
        fail();
    }

    public static boolean hasAnnotation(Record<?> record, Class<? extends Annotation> annotationType) {
        assertNotNull(record);
        assertNotNull(annotationType);
        assertTrue(containsAnnotationType(record.getAllAnnotations(), annotationType));
        for (Annotation annotation : record.getAllAnnotations()) {
            if (annotationType.equals(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAnnotationType(Collection<Annotation> annotations, Class<? extends Annotation> annotationType) {
        assertNotNull(annotations);
        assertNotNull(annotationType);
        for (Annotation annotation : annotations) {
            if (annotationType.equals(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    public static Configuration createConfiguration(Class<? extends Annotation>... deploymentTypes) {
        assertNotNull(deploymentTypes);
        return new Configuration(Lists.immutableList(deploymentTypes));
    }
}
