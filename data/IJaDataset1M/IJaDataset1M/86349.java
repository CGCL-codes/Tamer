package org.apache.jetspeed.util.parser;

import java.beans.PropertyDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.StringKey;
import java.util.Date;
import org.apache.turbine.util.ParameterParser;
import org.apache.turbine.util.pool.Recyclable;
import org.apache.jetspeed.util.ValidationException;
import org.apache.jetspeed.util.ValidationHelper;

/**
 * A Turbine parameter parser with Validation built in. 
 * Validates any bean with methods that begin with validate[AttributeName].
 * Works with Torque-generated beans.
 * To use this class, override the TurbineResources.properties:
 * 
 * services.RunDataService.default.parameter.parser=org.apache.turbine.util.parser.DefaultParameterParser
 *
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: ValidationParameterParser.java,v 1.6 2004/02/23 03:18:08 jford Exp $
 */
public class ValidationParameterParser extends DefaultJetspeedParameterParser implements ParameterParser, Recyclable {

    public ValidationParameterParser() {
        super();
    }

    public ValidationParameterParser(String characterEncoding) {
        super(characterEncoding);
    }

    /**
     * Uses bean introspection to set writable properties of bean from
     * the parameters, where a (case-insensitive) name match between
     * the bean property and the parameter is looked for.
     *
     * @param bean An Object.
     * @exception Exception, a generic exception.
     */
    public void setProperties(Object bean) throws Exception {
        Class beanClass = bean.getClass();
        PropertyDescriptor[] props = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
        StringBuffer invalidFieldMessages = new StringBuffer("");
        boolean valid = true;
        for (int i = 0; i < props.length; i++) {
            String propname = props[i].getName();
            Method setter = props[i].getWriteMethod();
            if (setter != null && (containsKey(propname) || containsDateSelectorKeys(propname) || containsTimeSelectorKeys(propname))) {
                if (!validateProperty(bean, props[i])) {
                    invalidFieldMessages.append("'");
                    invalidFieldMessages.append(propname);
                    invalidFieldMessages.append("' is not a valid field, ");
                    valid = false;
                }
                setMyProperty(bean, props[i]);
            }
        }
        String msg = generalValidation(bean);
        if (msg != null) {
            invalidFieldMessages.append(msg);
            invalidFieldMessages.append(", ");
            valid = false;
        }
        if (!valid) {
            int lastComma = new String(invalidFieldMessages).lastIndexOf(", ");
            String result = invalidFieldMessages.substring(0, lastComma);
            throw new ValidationException(result);
        }
    }

    /**
     * Set the property 'prop' in the bean to the value of the
     * corresponding parameters.  Supports all types supported by
     * getXXX methods plus a few more that come for free because
     * primitives have to be wrapped before being passed to invoke
     * anyway.
     *
     * @param bean An Object.
     * @param prop A PropertyDescriptor.
     * @exception Exception, a generic exception.
     */
    protected void setMyProperty(Object bean, PropertyDescriptor prop) throws Exception {
        if (prop instanceof IndexedPropertyDescriptor) {
            throw new Exception(prop.getName() + " is an indexed property (not supported)");
        }
        Method setter = prop.getWriteMethod();
        if (setter == null) {
            throw new Exception(prop.getName() + " is a read only property");
        }
        Object[] args = getArguments(prop);
        try {
            setter.invoke(bean, args);
        } catch (Throwable t) {
            System.out.println("Validation: EXCEPTION (prop): " + prop.getName());
        }
    }

    protected Object[] getArguments(PropertyDescriptor prop) throws Exception {
        Class propclass = prop.getPropertyType();
        Object[] args = { null };
        if (propclass == String.class) {
            args[0] = getString(prop.getName());
        } else if (propclass == Integer.class || propclass == Integer.TYPE) {
            args[0] = getInteger(prop.getName());
        } else if (propclass == Short.class || propclass == Short.TYPE) {
            args[0] = new Short((short) (getInteger(prop.getName()).intValue()));
        } else if (propclass == Long.class || propclass == Long.TYPE) {
            args[0] = new Long(getLong(prop.getName()));
        } else if (propclass == Boolean.class || propclass == Boolean.TYPE) {
            args[0] = getBool(prop.getName());
        } else if (propclass == Double.class || propclass == Double.TYPE) {
            args[0] = new Double(getDouble(prop.getName()));
        } else if (propclass == BigDecimal.class) {
            args[0] = getBigDecimal(prop.getName());
        } else if (propclass == String[].class) {
            args[0] = getStrings(prop.getName());
        } else if (propclass == Object.class) {
            args[0] = getObject(prop.getName());
        } else if (propclass == int[].class) {
            args[0] = getInts(prop.getName());
        } else if (propclass == Integer[].class) {
            args[0] = getIntegers(prop.getName());
        } else if (propclass == Date.class) {
            args[0] = getDate(prop.getName());
        } else if (propclass == NumberKey.class) {
            args[0] = getNumberKey(prop.getName());
        } else if (propclass == StringKey.class) {
            args[0] = getStringKey(prop.getName());
        } else {
        }
        return args;
    }

    /**
     * Validate a bean's property based on definition in the business object
     *
     * @param bean The bean to be validated.
     * @param prop The bean's property descriptor
     * @return true if validation was successful, false if validation failed
     **/
    protected boolean validateProperty(Object bean, PropertyDescriptor prop) throws Exception {
        String propertyName = prop.getName();
        String methodName = "validate" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        Class[] signatureParams = { prop.getPropertyType() };
        Object[] methodParams = getArguments(prop);
        try {
            Method method = bean.getClass().getMethod(methodName, signatureParams);
            boolean isValidBool = ((Boolean) method.invoke(bean, methodParams)).booleanValue();
            return isValidBool;
        } catch (NoSuchMethodException nsm_e) {
            try {
                return validateObject(prop);
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }

    protected boolean validateObject(PropertyDescriptor prop) throws Exception {
        Class propclass = prop.getPropertyType();
        Object[] args = { null };
        if (propclass == String.class) {
            return ValidationHelper.isAlphaNumeric(getString(prop.getName()), false);
        } else if (propclass == Integer.class || propclass == Integer.TYPE) {
            return ValidationHelper.isInteger(getString(prop.getName()), false);
        } else if (propclass == Short.class || propclass == Short.TYPE) {
            return ValidationHelper.isInteger(getString(prop.getName()), false);
        } else if (propclass == Long.class || propclass == Long.TYPE) {
            return ValidationHelper.isDecimal(getString(prop.getName()), false);
        } else if (propclass == Boolean.class || propclass == Boolean.TYPE) {
            return true;
        } else if (propclass == Double.class || propclass == Double.TYPE) {
            return ValidationHelper.isDecimal(getString(prop.getName()), false);
        } else if (propclass == BigDecimal.class) {
            return ValidationHelper.isDecimal(getString(prop.getName()), false);
        } else if (propclass == String[].class) {
            return ValidationHelper.isAlphaNumeric(getString(prop.getName()), false);
        } else if (propclass == Object.class) {
            System.err.println("Auto validate: Object-- NOT IMPLEMENTED");
            return true;
        } else if (propclass == int[].class) {
            return ValidationHelper.isInteger(getString(prop.getName()), false);
        } else if (propclass == Integer[].class) {
            return ValidationHelper.isInteger(getString(prop.getName()), false);
        } else if (propclass == Date.class) {
            return true;
        } else if (propclass == NumberKey.class) {
            return ValidationHelper.isInteger(getString(prop.getName()), false);
        } else if (propclass == StringKey.class) {
            return ValidationHelper.isInteger(getString(prop.getName()), false);
        } else {
        }
        return false;
    }

    /**
     * Validate a bean's property based on definition in the business object
     *
     * @param bean The bean to be validated.
     * @param prop The bean's property descriptor
     * @return null if validation was successful, an error message if validation failed
     **/
    protected String generalValidation(Object bean) throws Exception {
        String methodName = "validate";
        try {
            Method method = bean.getClass().getMethod(methodName, null);
            String msg = (String) method.invoke(bean, null);
            return msg;
        } catch (NoSuchMethodException nsm_e) {
        } catch (Exception e) {
            System.err.println("EXCEPTION INVOKING METHOD " + methodName + " : " + e);
        }
        return null;
    }
}
