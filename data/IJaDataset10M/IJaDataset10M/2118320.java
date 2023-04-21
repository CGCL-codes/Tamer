package org.rails4j.mvc.ctrl;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rails4j.mvc.api.Request;
import org.rails4j.mvc.beans.Error;
import org.rails4j.mvc.inject.DI;

/**
 * @author ama
 * 
 */
public class ObjectBinder {

    /** */
    private static final Log LOG = LogFactory.getLog(ObjectBinder.class);

    /**
     * 
     * @param clazz
     * @param params
     * @return a new instance of "clazz", or null if any error occurs
     */
    public <T> Object bind(Class<T> clazz, Map<String, Object> params) {
        final T object = DI.getNew(clazz);
        if (object == null) {
            return null;
        }
        try {
            ConvertUtilsBean cub = BeanUtilsBean.getInstance().getConvertUtils();
            cub.register(true, false, 0);
            populate(object, params);
        } catch (IllegalAccessException e) {
            LOG.warn("", e);
        } catch (InvocationTargetException e) {
            LOG.warn("", e);
        }
        return object;
    }

    /**
     * 
     * @param bean
     * @param properties
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void populate(Object bean, Map<String, Object> properties) throws IllegalAccessException, InvocationTargetException {
        if ((bean == null) || (properties == null)) {
            return;
        }
        Iterator<Entry<String, Object>> entries = properties.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, Object> entry = entries.next();
            String name = (String) entry.getKey();
            if (name == null) {
                continue;
            }
            try {
                BeanUtilsBean.getInstance().setProperty(bean, name, entry.getValue());
            } catch (Exception e) {
                LOG.info(e.getMessage());
                Request.errors().add(new Error(name, "convert"));
            }
        }
    }
}
