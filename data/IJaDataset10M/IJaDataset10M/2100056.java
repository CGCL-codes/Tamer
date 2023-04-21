package com.mytaobao.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import com.mytaobao.exception.CommonException;

/**
 * 
 * 序列化工具类
 * 
 * @author z3y2
 *
 */
public final class SerializeUtil {

    private static Log logger = LogFactory.getLog(SerializeUtil.class);

    /** 空数组常量,避免每次都创建,从而提高性能 */
    private static final Object[] EMPTY_ARRAY = new Object[0];

    private SerializeUtil() {
    }

    /** 
	 * 将对象列表序列化或是数组成字符串
	 * @param targetObject
	 * @return 
	 */
    public static String toString(List<?> list) {
        Assert.notNull(list, "serialze list must not be null");
        StringBuilder sb = new StringBuilder("[");
        for (Object o : list) {
            sb.append(toString(o)).append(",");
        }
        int index = sb.lastIndexOf(",");
        if (index != -1) {
            sb.deleteCharAt(index);
        }
        sb.append("]");
        return sb.toString();
    }

    /** 
	 * 将普通对象序列化成字符串
	 * @param targetObject
	 * @return 
	 */
    public static String toString(Object targetObject) {
        Assert.notNull(targetObject, "serialze targetObject must not be null");
        StringBuilder sb = new StringBuilder("{");
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(targetObject.getClass());
        } catch (IntrospectionException ex) {
            throw new CommonException(ex);
        }
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (pd.getPropertyType() == Class.class) {
                continue;
            }
            Method getter = pd.getReadMethod();
            try {
                sb.append(pd.getName()).append("=").append(getter.invoke(targetObject, EMPTY_ARRAY)).append(",");
            } catch (IllegalArgumentException e) {
                logger.warn(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                logger.warn(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        int index = sb.lastIndexOf(",");
        if (index != -1) {
            sb.deleteCharAt(index);
        }
        sb.append("}");
        return sb.toString();
    }
}
