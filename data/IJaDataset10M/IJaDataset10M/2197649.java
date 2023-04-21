package com.ssd.mda.core.metadata;

import com.ssd.mda.core.metadata.model.descriptor.MetadataTypeDescriptor;
import com.ssd.mda.core.metadata.model.descriptor.MetadataOperationDescriptor;
import com.ssd.mda.core.metadata.model.descriptor.MetadataAttributeDescriptor;

/**
 * Abstract interface for all Metadata implementations.
 * It looks like these interfaces are going to have to cascade their return types...
 * instead of taking an Object, the caller needs to use the result of the higher order call.
 *
 * @author Flavius Burca
 * @date Jan 13, 2008
 */
public interface MetadataProvider {

    /**
	 * Returns a bean which will be used to add properties 
	 * to a type metadata element.
	 * 
	 * @param descriptor type descriptor
	 * @return Bean object with properties which will be added to a type metadata.
	 */
    Object getTypeMetadata(MetadataTypeDescriptor descriptor);

    /**
	 * Returns a bean which will be used to add properties 
	 * to an attribute metadata element.
	 * 
	 * @param descriptor attribute descriptor
	 * @return Bean object with properties which will be added to an attribute metadata.
	 */
    Object getAttributeMetadata(MetadataAttributeDescriptor descriptor);

    /**
	 * Returns a bean which will be used to add properties 
	 * to an operation metadata element.
	 * 
	 * @param descriptor operation descriptor
	 * @return Bean object with properties which will be added to an operation metadata.
	 */
    Object getOperationMetadata(MetadataOperationDescriptor descriptor);

    /**
     * Returns the base interface for the bean. 
     * This is used to compose metadata properties.
     * 
     * @return Bean interface for type metadata
     */
    Class<?> getTypeClass();

    /**
     * Returns the base interface for the bean. 
     * This is used to compose metadata properties.
     * 
     * @return Bean interface for attribute metadata
     */
    Class<?> getAttributeClass();

    /**
     * Returns the base interface for the bean. 
     * This is used to compose metadata properties.
     * 
     * @return Bean interface for operation metadata
     */
    Class<?> getOperationClass();

    /**
     * Whether or not this provider can provide any metadata information
     * for the specified class.
     *  
     * @param clazz the class to check
     * @return true if the class is supported, false otherwise
     */
    boolean supports(Class<?> clazz);

    /**
     * Whether or not this provider can provide operation metadata information.
     *  
     * @return true if operation metadata is supported, false otherwise
     */
    boolean supportsOperations();

    /**
     * Whether or not this provider can provide attribute metadata information.
     *  
     * @return true if attribute metadata is supported, false otherwise
     */
    boolean supportsAttributes();
}
