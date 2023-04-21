package com.adamo.service;

import java.util.Collection;
import java.util.Map;
import org.alfresco.service.namespace.QName;
import com.adamo.model.Model;
import com.adamo.model.Property;
import com.adamo.model.Type;

/**
 * This interface represents the Repository Data Dictionary.  The
 * dictionary provides access to content meta-data such as Type
 * and Properties descriptions.
 *
 * Content meta-data is organised into models where each model is
 * given a qualified name. Each model is composed of types and properties.
 * 
 * @author Andrea Leo
 */
public interface DataModelService {

    /**
     * @param model the model name to retrieve
     * @return the specified model (or null, if it doesn't exist)
     */
    public Model getModel(QName model);

    /**
     * @param model the model to retrieve types for
     * @return the names of all types defined within the specified model
     */
    Collection<QName> getTypes(QName model);

    /**
     * @param name the name of the type to retrieve
     * @return the type definition (or null, if it doesn't exist)
     */
    Type getType(QName name);

    /**
     * Gets the definition of the property as defined by its owning Class.
     * 
     * @param propertyName the property name
     * @return the property definition (or null, if it doesn't exist)
     */
    Property getProperty(QName propertyName);

    /**
     * Gets the definitions of the properties defined by the specified Class.
     * 
     * @param className the class name
     * @return the property definitions
     */
    Map<QName, Property> getPropertyDefs(QName className);

    /**
     * Create a new model in the Repository
     * 
     * @param model the model to create
     */
    void createModel(Model model);

    /**
     * Create a new qbrmodel type 
     * 
     * @param name the name of the new type (prefix:name)
     * @param description the description of the new type
     * @param parentName the parentName of the new type (prefix:name)
     * @param title the title of the new type
     */
    void createType(String name, String description, String parentName, String title);

    /**
     * Update a qbrmodel type 
     * 
     * @param name the name of the type (prefix:name)
     * @param description the description of the type
     * @param parentName the parentName of the type (prefix:name)
     * @param title the title of the type
     */
    void updateType(String name, String description, String parentName, String title);

    /**
     * Remove a qbrmodel type and all related properties from the Repository
     * 
     * @param name the name of the type to remove (prefix:name)   
     */
    void removeType(String name);

    /**
     * Create a new property 
     * 
     * @param name the name of the new property (prefix:name)
     * @param type the type to add the new property (prefix:name)
     * @param title the title of the new property
     * @param dataType the dataType of the new property (prefix:name)
     * 
     */
    void createProperty(String name, String type, String title, String dataType);

    /**
     * Update a property 
     * 
     * @param name the name of the property (prefix:name)
     * @param type the type to update the property (prefix:name)
     * @param title the title of the property
     * @param dataType the dataType of the property (prefix:name)
     * 
     */
    void updateProperty(String name, String type, String title, String dataType);

    /**
     * Remove a property 
     * 
     * @param name the name of the property to remove (prefix:name)
     * @param type the type of the property
     * 
     */
    void removeProperty(String name, String type);
}
