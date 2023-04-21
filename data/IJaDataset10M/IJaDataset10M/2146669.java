package com.tll;

/**
 * Provides values for property paths.
 * @author jpk
 */
public interface IPropertyValueProvider {

    /**
	 * @param propertyPath
	 * @return
	 */
    Object getPropertyValue(String propertyPath);
}
