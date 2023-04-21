package net.sf.implessbean;

import java.io.Serializable;
import java.util.Map;

/**
 * {@link IKeyMapper} is responsible to mapping a property name of the bean to a corresponding key of a Map
 * containing values of the bean.
 * 
 * If you want to serialize/deserialize the bean created by {@link BeanFactory},
 * you need to make an implementation of {@link IKeyMapper} {@link Serializable}.
 * 
 * @author Kohji Nakamura
 * @version $Revision: 1.1 $
 * @since 1.0
 */
public interface IKeyMapper<KeyType, ValueType> {

    /**
	 * Returns a key object of Map corresponding to propertyName.
	 * 
	 * Return null if there is no key object appropriate for propertyName.
	 * In this case, {@link net.sf.implessbean.BeanHandler} tries to invoke Map object's accessor method
	 * rather than to call its get/put method. If it doesn't have a corresponding accessor
	 * method, {@link java.lang.IllegalArgumentException} would be thrown.
	 * 
	 * @param values A Map object containing properties of the bean.
	 * 	You can use this Map to find out a key of Map corresponding to the propertyName.
	 * 	Don't change this Map.
	 * @param propertyName A name of property.
	 * @return A key object of Map corresponding to propertyName, or null.
	 */
    KeyType propertyNameToKey(Map<KeyType, ValueType> values, String propertyName);
}
