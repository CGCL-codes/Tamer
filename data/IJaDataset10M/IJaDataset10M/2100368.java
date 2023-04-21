package org.wikiup.core.imp.context;

import org.wikiup.core.imp.getter.BeanPropertyGetter;
import org.wikiup.core.imp.setter.BeanPropertySetter;
import org.wikiup.core.imp.bridge.ContextBridge;

public class BeanPropertyContext extends ContextBridge<Object, Object> {

    public BeanPropertyContext(Object bean) {
        super(new BeanPropertyGetter(bean), new BeanPropertySetter(bean));
    }
}
