package com.syrtsov.alinker.factory;

import com.syrtsov.alinker.ALinker;
import com.syrtsov.alinker.Context;
import com.syrtsov.alinker.Factory;
import com.syrtsov.alinker.FactoryException;
import java.lang.annotation.Annotation;

/**
 * Created-By: Pavel Syrtsov
 * Date: Apr 10, 2008
 * Time: 4:28:55 PM
 */
public class InjectValueFactory implements Factory {

    public Object create(ALinker nInjector, Context ctx) throws FactoryException {
        final Annotation[] annotations = ctx.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof InjectValue) {
                InjectValue injectValue = (InjectValue) annotation;
                return injectValue.value();
            }
        }
        return null;
    }
}
