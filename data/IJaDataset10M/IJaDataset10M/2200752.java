package com.sitechasia.webx.core.utils.populator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;

/**
 * 实现ResultSet到Bean和Map的注值
 *
 * @author Zal
 * @version 1.2 , 2008/5/7
 * @since JDK1.5
 */
public class ResultSetBeanPopulator extends BasePopulator {

    protected boolean doPopulate(Object source, Object target, Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params) {
        if (target instanceof Collection || target instanceof ResultSet || target instanceof ServletRequest || !(source instanceof ResultSet)) {
            return false;
        }
        ResultSet resultSet = (ResultSet) source;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            int columnNumber = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnNumber; i++) {
                resultMap.put(resultSet.getMetaData().getColumnName(i).toUpperCase(), resultSet.getObject(i));
            }
        } catch (SQLException e) {
            logger.debug("Can not get MetaData from ResultSet", e);
            return false;
        }
        if (target instanceof Map) {
            ((Map) target).putAll(resultMap);
            return true;
        }
        PropertyDescriptor[] targetDescriptors = null;
        try {
            targetDescriptors = Introspector.getBeanInfo(target.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException ie) {
            logger.debug("Failed on getting bean's properties", ie);
            return false;
        }
        for (PropertyDescriptor targetDescriptor : targetDescriptors) {
            if (targetDescriptor.getName().equals("class") || !doProcess(targetDescriptor.getName(), ignoreProperties)) {
                continue;
            }
            String sourcePropertyName = null;
            if (propertiesMapping != null && propertiesMapping.containsKey(targetDescriptor.getName())) {
                sourcePropertyName = propertiesMapping.get(targetDescriptor.getName()).toUpperCase();
            }
            if ((sourcePropertyName == null && resultMap.containsKey(targetDescriptor.getName().toUpperCase())) || (sourcePropertyName != null && !resultMap.containsKey(sourcePropertyName) && resultMap.containsKey(targetDescriptor.getName().toUpperCase()))) {
                sourcePropertyName = targetDescriptor.getName().toUpperCase();
            }
            if (sourcePropertyName == null || sourcePropertyName.length() == 0) {
                continue;
            }
            try {
                Method writeMethod = targetDescriptor.getWriteMethod();
                if (writeMethod == null) {
                    continue;
                }
                Object convertedValue = getConverter().convertValue(resultMap.get(sourcePropertyName), targetDescriptor.getPropertyType(), params);
                writeMethod.invoke(target, new Object[] { convertedValue });
            } catch (Exception e) {
                logger.debug("Exception", e);
            }
        }
        return true;
    }
}
