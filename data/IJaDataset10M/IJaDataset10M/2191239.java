package com.jetter.gomp.util;

import com.jetter.gomp.interfaces.IGOMFramework;

/**
 * TODO .....
 * 
 * @author Fellow
 *
 */
public class FrameworkPropertyParse extends PropertyParse {

    public static String TYPE_ALIAS_PARAMETER = "parameter";

    public static String TYPE_ALIAS_DATASOURCE = "datasource";

    public static String TYPE_ALIAS_OBJECT_CREATOR = "object-creator";

    public static String TYPE_ALIAS_EXECUTOR = "exeutor";

    /**
   * TODO .....
   */
    public static Object setProperty(Object object, String propertyName, String propertyValue, String propertyType, IGOMFramework framework) throws GOMException {
        Object obj = null;
        if (TYPE_ALIAS_PARAMETER.equalsIgnoreCase(propertyType)) {
            Object value = framework.getParamter(propertyValue);
            setProperty(object, propertyName, value);
        } else if (TYPE_ALIAS_DATASOURCE.equalsIgnoreCase(propertyType)) {
            Object value = framework.getDatasource(propertyValue);
            setProperty(object, propertyName, value);
        } else if (TYPE_ALIAS_OBJECT_CREATOR.equalsIgnoreCase(propertyType)) {
            Object value = framework.getObjectCreator(propertyValue);
            setProperty(object, propertyName, value);
        } else if (TYPE_ALIAS_EXECUTOR.equalsIgnoreCase(propertyType)) {
            Object value = framework.getExecutor(propertyValue);
            setProperty(object, propertyName, value);
        } else {
            setProperty(object, propertyName, propertyValue, propertyType);
        }
        return obj;
    }
}
