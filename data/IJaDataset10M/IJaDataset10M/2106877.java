package com.icteam.fiji.flexserialize;

import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import flex.messaging.io.AbstractProxy;
import flex.messaging.io.BeanProxy;

/**
 * Represents a proxy used to catch <i>enum</i> serialized by flex.
 * 
 * @author r.simoni
 */
class EnumProxy extends BeanProxy {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(EnumProxy.class);

    private static final String UNSET = "unset";

    private static final String ACTION_SCRIPT_VALUE_NAME = "val";

    private static List<String> propertyNames = Collections.singletonList(ACTION_SCRIPT_VALUE_NAME);

    @SuppressWarnings("unchecked")
    public Object createInstance(String className) {
        if (log.isDebugEnabled()) log.debug("Creating enum instance for class named '" + className + "'...");
        Class cl = AbstractProxy.getClassFromClassName(className);
        if (!cl.isEnum()) {
            String msg = getClass().getName() + " registered for a class which is not an enum (" + cl.getName() + ")";
            log.fatal(msg);
            throw new IllegalStateException(msg);
        }
        return new EnumStub(cl);
    }

    public Object getValue(Object instance, String propertyName) {
        if (!propertyName.equals(ACTION_SCRIPT_VALUE_NAME)) {
            String msg = "No property named '" + propertyName + "' on enum type";
            log.fatal(msg);
            throw new IllegalStateException(msg);
        }
        return instance.toString();
    }

    public void setValue(Object instance, String propertyName, Object value) {
        EnumStub es = (EnumStub) instance;
        if (!propertyName.equals(ACTION_SCRIPT_VALUE_NAME)) {
            String msg = "No EnumStub property '" + propertyName + "' found";
            log.fatal(msg);
            throw new IllegalStateException(msg);
        }
        if (log.isDebugEnabled()) log.debug("Setting " + EnumStub.class.getSimpleName() + " value to '" + value + "'");
        es.value = (String) value;
    }

    public Object instanceComplete(Object instance) {
        EnumStub es = (EnumStub) instance;
        Object ret = (!UNSET.equals(es.value)) ? Enum.valueOf(es.cl, es.value) : null;
        if (log.isDebugEnabled()) log.debug("Returning '" + ret + "' as instance complete due to an " + EnumStub.class.getSimpleName() + " with class '" + es.cl.getName() + "' and value '" + es.value + "'");
        return ret;
    }

    @SuppressWarnings("unchecked")
    public List getPropertyNames(Object instance) {
        if (!(instance instanceof Enum)) {
            String msg = "getPropertyNames called with non Enum object";
            log.fatal(msg);
            throw new IllegalStateException(msg);
        }
        return propertyNames;
    }

    private static class EnumStub {

        @SuppressWarnings("unchecked")
        private Class cl;

        private String value;

        @SuppressWarnings("unchecked")
        EnumStub(Class c) {
            cl = c;
        }
    }
}
