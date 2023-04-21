package org.metastopheles.annotation;

import org.metastopheles.*;
import org.metastopheles.FacetKey;
import org.testng.annotations.Test;
import java.lang.annotation.Annotation;
import static org.testng.Assert.*;

public class TestAnnotationBasedMetaDataDecorator {

    public static final FacetKey<Boolean> FOUND = new FacetKey<Boolean>() {
    };

    @Test
    public void testAnnotationsFound() {
        final BeanMetaDataFactory factory = new BeanMetaDataFactory();
        factory.getBeanMetaDataDecorators().add(new FindMeDecorator<BeanMetaData>());
        factory.getMethodMetaDataDecorators().add(new FindMeDecorator<MethodMetaData>());
        factory.getPropertyMetaDataDecorators().add(new FindMeDecorator<PropertyMetaData>());
        BeanMetaData meta = factory.getBeanMetaData(FindMeBean.class);
        assertTrue(meta.getFacet(FOUND));
        assertTrue(meta.getPropertyMetaData("name").getFacet(FOUND));
        assertTrue(meta.getMethodMetaData("someMethod").getFacet(FOUND));
    }

    private static final class FindMeDecorator<T extends MetaDataObject> extends AnnotationBasedMetaDataDecorator<T, FindMe> {

        private FindMeDecorator() {
            super(FindMe.class);
        }

        @Override
        protected void decorate(T metaData, Annotation annotation) {
            metaData.setFacet(FOUND, true);
        }
    }
}
