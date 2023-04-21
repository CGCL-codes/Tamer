package com.googlecode.beanfiles.translators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.googlecode.beanfiles.TranslationException;
import com.googlecode.beanfiles.TranslationNotice;

/**
 * Handles creation of nested bean objects by utilizing a map of nested property names to lists of
 * indexes.
 * 
 */
public class NestedConvertUtilsTranslator extends NestedPropertyTranslator {

    protected static Log log() {
        return LogFactory.getLog(NestedConvertUtilsTranslator.class);
    }

    public NestedConvertUtilsTranslator(String parentPropertyName, Map<String, List<Integer>> propertyIndexesMap) {
        super(parentPropertyName, propertyIndexesMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object read(Object i, Class<?> clazz) {
        try {
            Object target = clazz.newInstance();
            for (String propertyName : propertyIndexesMap.keySet()) {
                if (propertyIndexesMap.get(propertyName).size() == 1) {
                    int index = propertyIndexesMap.get(propertyName).get(0);
                    Object value = ((List) i).get(index);
                    value = trimToNull(value);
                    if (value != null) {
                        try {
                            BeanUtils.copyProperty(target, propertyName, value);
                        } catch (ConversionException e) {
                            TranslationNotice notice = new TranslationNotice("Invalid value", index);
                            notice.setValue(value);
                            notice.setPropertyName(parentPropertyName + "." + propertyName);
                            addTranslationNotice(notice);
                        }
                    } else {
                        addNullIndex(index, parentPropertyName + "." + propertyName);
                    }
                } else {
                    List values = new ArrayList();
                    for (int index : propertyIndexesMap.get(propertyName)) {
                        Object value = ((List) i).get(index);
                        value = trimToNull(value);
                        if (value != null) {
                            values.add(value);
                        } else {
                            addNullIndex(index, propertyName);
                        }
                    }
                    BeanUtils.copyProperty(target, propertyName, values);
                }
            }
            addNullNotices();
            return target;
        } catch (InstantiationException e) {
            throw new TranslationException(e);
        } catch (IllegalAccessException e) {
            throw new TranslationException(e);
        } catch (InvocationTargetException e) {
            throw new TranslationException(e);
        }
    }

    private Object trimToNull(Object value) {
        if (value instanceof String) {
            value = StringUtils.trimToNull((String) value);
        }
        return value;
    }

    @Override
    public Object write(Object t) {
        throw new TranslationException("not supported");
    }

    public boolean resolvesProperty(String propertyName) {
        for (String aProperty : propertyIndexesMap.keySet()) {
            if (propertyName.equals(parentPropertyName + "." + aProperty)) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> getIndexes(String propertyName) {
        for (String aProperty : propertyIndexesMap.keySet()) {
            if (propertyName.equals(parentPropertyName + "." + aProperty)) {
                return propertyIndexesMap.get(aProperty);
            }
        }
        throw new TranslationException("Does not resolve property: " + propertyName);
    }
}
