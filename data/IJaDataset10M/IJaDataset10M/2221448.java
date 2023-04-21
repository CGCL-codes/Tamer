package com.rccloud.inproxy.converter;

/**
 * Field converter interface, it used to convert the value of field between main
 * object and proxy object
 */
public interface ProxyConverter {

    /**
	 * Convert from the value of the field of main object to the value of the
	 * field of proxy object
	 * 
	 * @param expectedProxyClass
	 *            Expected proxy class
	 * @param object
	 *            The value of a field of main object
	 * @return Return value of a field of proxy object
	 */
    public Object convertToProxy(Class<?> expectedProxyClass, Object mainObject);

    /**
	 * Convert from the value of the field of proxy object to the value of the
	 * field of main object
	 * 
	 * @param object
	 *            the value of a field of proxy object
	 * @return the value of a field of main object
	 */
    public Object convertFromProxy(Object proxyObject);
}
