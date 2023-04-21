package com.vishuddha.domain;

import java.io.Serializable;

/**
 * <code>Property<code> is a object containing information 
 * about properties of a method signature. <br>
 * <code>Name<code> will be the full name of property method 
 * by which this property can be accessed. 
 * (e.g. getName, setName, isNoException etc.)<br>
 * <code>ValidateSecurity<code> this boolean value used to 
 * decide that should we check for security or not at the 
 * time of method invocation. Default value is true<br>
 * <code>NoException<code> this boolean shows that throw an exception or not, if property not available 
 * providing this true will return null value if property is not available; default value is true<br>
 * <code>BestProperty<code> this boolean value decide weather best property is 
 * to be invoked or the regular. Providing this true will slow down the performance as 
 * it searches a best from all methods of given class. Default value is false<br>
 * <code>params<code> parameters for given method.<br>
 * <code>next<code> net property to be invoked on out put of this property.<br>
 * 
 * @author Lokesh Jain
 * @Since @Jan 26, 2010
 * @Version 1.0.0.0
 */
public class Property implements Serializable {

    private static final long serialVersionUID = -3128165351079269860L;

    private String name;

    private boolean validateSecurity;

    private boolean noException;

    private boolean bestProperty;

    private Params params;

    private Property next;

    private boolean beanProperty;

    public Property(String name) {
        this(name, null);
    }

    public Property(String name, Params params) {
        this(name, params, null);
    }

    public Property(String name, Params params, Property next) {
        this(name, false, params, next);
    }

    public Property(String name, boolean bestProperty, Params params, Property next) {
        this(name, true, true, bestProperty, params, next);
    }

    /**
	 * Constructor to create property object 
	 * @param name <code>Name<code> will be the full name of property method 
	 * by which this property can be accessed. 
	 * (e.g. getName, setName, isNoException etc.)<br>
	 * @param validateSecurity <code>ValidateSecurity<code> this boolean value used to 
	 * decide that should we check for security or not at the 
	 * time of method invocation.<br>
	 * @param noException <code>NoException<code> this boolean shows that throw an exception or not, if property not available 
	 * providing this true will return null value if property is not available<br>
	 * @param bestProperty <code>BestProperty<code> this boolean value decide weather best property is 
	 * to be invoked or the regular. Providing this true will slow down the performance as 
	 * it searches a best from all methods of given class<br>
	 * @param params <code>params<code> parameters for given method <br>
	 * @param next <code>next<code> net property to be invoked on out put of this property<br>
	 */
    public Property(String name, boolean validateSecurity, boolean noException, boolean bestProperty, Params params, Property next) {
        this.name = name;
        this.validateSecurity = validateSecurity;
        this.noException = noException;
        this.bestProperty = bestProperty;
        this.params = params;
        this.next = next;
        this.beanProperty = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValidateSecurity() {
        return validateSecurity;
    }

    public void setValidateSecurity(boolean validateSecurity) {
        this.validateSecurity = validateSecurity;
    }

    public boolean isNoException() {
        return noException;
    }

    public void setNoException(boolean noException) {
        this.noException = noException;
    }

    public boolean isBestProperty() {
        return bestProperty;
    }

    public void setBestProperty(boolean accessBestProperty) {
        this.bestProperty = accessBestProperty;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public ParamMetadata[] allParams() {
        return params == null ? null : params.allParams();
    }

    public Property getNext() {
        return next;
    }

    public void setNext(Property next) {
        this.next = next;
    }

    public boolean isBeanProperty() {
        return beanProperty;
    }

    public void setBeanProperty(boolean beanProperty) {
        this.beanProperty = beanProperty;
    }
}
