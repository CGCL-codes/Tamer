package org.jwos.plugin.file.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.jwos.plugin.file.domain.MappableBean;

public class MapUtil {

    public static Map beanToMap(Object bean) {
        Map map = null;
        if (bean != null) {
            map = new HashMap();
            PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(bean);
            if (pds != null && pds.length > 0) {
                for (int i = 0; i < pds.length; i++) {
                    String name = pds[i].getName();
                    Object value = getValueFromBean(bean, name);
                    if (!name.equals("class") && value != null) {
                        map.put(name, value);
                    }
                }
            }
        }
        return map;
    }

    public static Object mapToBean(Object bean, Map map) {
        if (bean != null && map != null) {
            for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
                String name = (String) i.next();
                Object value = map.get(name);
                setValueToBean(bean, name, value);
            }
        }
        return bean;
    }

    public static List beanListToMapList(List beanList) {
        List mapList = null;
        if (beanList != null) {
            mapList = new ArrayList();
            for (int i = 0; i < beanList.size(); i++) {
                Object bean = beanList.get(i);
                if (bean instanceof MappableBean) {
                    MappableBean mb = (MappableBean) bean;
                    mapList.add(mb.toMap());
                } else {
                    mapList.add(beanToMap(bean));
                }
            }
        }
        return mapList;
    }

    public static List mapListToBeanList(Class clazz, List mapList) {
        List beanList = null;
        if (mapList != null) {
            beanList = new ArrayList();
            for (int i = 0; i < mapList.size(); i++) {
                Object bean = getClassInstance(clazz);
                Map map = (Map) mapList.get(i);
                beanList.add(mapToBean(bean, map));
            }
        }
        return beanList;
    }

    private static Object getValueFromBean(Object bean, String name) {
        Object value = null;
        try {
            value = PropertyUtils.getProperty(bean, name);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
        return value;
    }

    private static void setValueToBean(Object bean, String name, Object value) {
        try {
            PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, name);
            if (pd != null) {
                PropertyUtils.setProperty(bean, name, value);
            }
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
    }

    private static Object getClassInstance(Class clazz) {
        Object instance = null;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return instance;
    }
}
