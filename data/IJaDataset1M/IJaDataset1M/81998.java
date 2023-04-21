package bwbunit.blackbox;

import java.lang.reflect.Field;
import bwbunit.whitebox.PrivateProxy;
import bwbunit.whitebox.PrivateProxyFactory;

/**
 * ParamHolder is an internal class used to manage the many different number of paramater combinations.
 * 
 * @author syshsp
 * @version $Id: ParamHolder.java,v 1.2 2004/04/18 01:46:05 howardsp Exp $
 * 
 */
class ParamHolder {

    /** this objects member varaibles to pound */
    private ParamHolder[] nestedParameters;

    /** the type of this parameter */
    private Class parameterType;

    /** the values to use  */
    private Object[] parameterValues;

    /** the current value. */
    private Object value;

    /** reference to the Field object for this parameter. */
    private Field field;

    /** current index into values array. */
    private int currentValue = 0;

    /** 
     * non-public ctor to prevent creation outside of this package. 
     */
    ParamHolder(Class clazz, Object orgValue) throws Exception {
        parameterType = clazz;
        value = orgValue;
        parameterValues = PounderFactory.values(clazz);
        if (!clazz.isPrimitive() && clazz.toString().indexOf("java.lang") == -1) {
            Field[] fields = clazz.getDeclaredFields();
            nestedParameters = PounderFactory.createParameterHolder(fields, this);
        }
    }

    /**
     * update the variable to the next value or update the variables internal objects to there 
     * next value.
     */
    boolean nextValue() {
        if (nestedParameters != null && currentValue < parameterValues.length && parameterValues[currentValue] != null) {
            for (int i = 0; i < nestedParameters.length; i++) {
                ParamHolder holder = nestedParameters[i];
                if (holder != null && holder.nextValue()) {
                    return true;
                }
            }
            currentValue++;
        } else if (currentValue < parameterValues.length) {
            currentValue++;
        }
        if (currentValue < parameterValues.length) {
            return true;
        }
        currentValue = 0;
        return false;
    }

    /**
     * prepare and return the current value for processing.
     */
    Object value() throws Exception {
        Object result = parameterValues[currentValue];
        if (result != null && nestedParameters != null) {
            for (int i = 0; i < nestedParameters.length; i++) {
                ParamHolder holder = nestedParameters[i];
                if (holder != null) {
                    holder.value(result);
                }
            }
        }
        return result;
    }

    /**
     * prepare and return the current value for processing. 
     */
    Object value(Object parent) throws Exception {
        Object result = parameterValues[currentValue];
        PrivateProxy proxy = PrivateProxyFactory.createPrivateProxy(parent);
        String name = field.getName();
        proxy.set(name, result);
        return result;
    }

    /**
     * called by the factor to set default values.
     */
    void setOrginalValue() {
        if (parameterValues.length == 0) {
            parameterValues = new Object[] { value };
        } else {
            Object[] resultsWithOrgValue = new Object[parameterValues.length + 1];
            System.arraycopy(parameterValues, 0, resultsWithOrgValue, 1, parameterValues.length);
            resultsWithOrgValue[0] = value;
            parameterValues = resultsWithOrgValue;
        }
    }

    /**
     * return a reference to the current value.
     */
    Object getRef() {
        return value;
    }

    /**
     * set the field that matches this parameter.
     */
    void setField(Field f) {
        field = f;
    }
}
