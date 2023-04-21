package org.baljinder.presenter.datamodel;

import java.lang.annotation.Annotation;
import junit.framework.TestCase;
import org.baljinder.presenter.datamodel.DataModelMetadataProvider;
import org.baljinder.presenter.datamodel.config.Field;
import org.baljinder.presenter.testing.support.model.ToAnnotate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Baljinder Randhawa
 *
 */
public class AnnotationTest extends TestCase {

    public void testDataModelMetaDataProvider() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("meta-data-test.xml");
        DataModelMetadataProvider metadataProvider = (DataModelMetadataProvider) ac.getBean("metadataProvider");
        System.out.println(metadataProvider.getMetaData());
    }

    public void xtest() throws SecurityException, NoSuchFieldException {
        ToAnnotate toAnnotate = new ToAnnotate();
        Annotation[] annotation = toAnnotate.getClass().getAnnotations();
        java.lang.reflect.Field field = toAnnotate.getClass().getDeclaredField("test");
        Annotation fieldAnnotation = field.getAnnotation(Field.class);
        System.out.println(fieldAnnotation);
        for (Annotation a : annotation) {
            System.out.println(a);
        }
    }
}
