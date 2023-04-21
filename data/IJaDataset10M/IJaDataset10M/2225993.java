package net.sf.doolin.util.copy;

/**
 * This interface is responsible for storing and restoring property values.
 * 
 * @author Damien Coraboeuf
 * @version $Id$
 * 
 */
public interface PropertyTranslator {

    /**
	 * Translates a property value for storage
	 * 
	 * @param propertyValue
	 *            Value to translate
	 * @return Translated value
	 * @see CopyObject#copyFrom(Object)
	 */
    Object store(Object propertyValue);

    /**
	 * Translates a property value for restoration
	 * 
	 * @param propertyType
	 *            Property type
	 * @param existingTargetPropertyValue
	 *            Existing property value in the target object
	 * @param translatedPropertyValue
	 *            Translated value
	 * @return Value to restore
	 */
    Object restore(Class<?> propertyType, Object existingTargetPropertyValue, Object translatedPropertyValue);
}
