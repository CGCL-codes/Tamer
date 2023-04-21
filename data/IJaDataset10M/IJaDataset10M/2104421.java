package org.pimslims.metamodel;

import java.util.Map;

/**
 * Represents a model type.
 * 
 * @version 0.1
 * @composed 1 - * org.pimslims.metamodel.MetaAttribute
 * @composed 1 - * org.pimslims.metamodel.MetaRole
 */
public interface MetaClass extends MetaModelElement {

    /**
     * @param version the current transaction
     * @param owner the new object's data ownership
     * @param attributes map name to value for fields of new object
     * 
     * @throws AccessException if the currrent user is not allowed to do this
     * @throws ConstraintException if the values supplied are inconsistent
     */
    public <T extends ModelObject> T create(final WritableVersion version, final String owner, final Map<String, Object> attributes) throws AccessException, ConstraintException;

    /**
     * @return fully qualified name of this type
     */
    public String getMetaClassName();

    /**
     * @return one-word name of this type
     */
    public String getShortName();

    /**
     * @return true if this class cannot be instantiated
     */
    public boolean isAbstract();

    /**
     * Returns the generated class representing this model type.
     * 
     * @return the java.lang.Class for objects of this type
     */
    public Class getJavaClass();

    /**
     * @param object model object to check
     * @return true is it is an instance of this type
     */
    public boolean isInstance(ModelObject object);

    /**
     * @return representation of model type which this is a subtype of.
     */
    MetaClass getSupertype();

    /**
     * @return collection of MetaClass objects representing all model types with this as subtype.
     */
    public java.util.Set<MetaClass> getSubtypes();

    /**
     * @return ccpn orignial collection of MetaClass objects representing all model types with this as
     *         subtype.
     */
    Invariant getInvariant();

    /**
     * Determine whether a value for an attribute is acceptable
     * 
     * @param value the value to check
     * @param attributeName the name of the attribute
     * @param object the model object the value is for
     * @return true if the value is permissible
     */
    public abstract boolean checkConstraint(Object value, String attributeName, ModelObject object);

    /**
     * @param name of attribute
     * @return representation of it
     */
    abstract MetaAttribute getAttribute(String name);

    /**
     * @return map attribute names => MetaAttribute instances
     */
    java.util.Map<String, MetaAttribute> getAttributes();

    /**
     * Get information about an assocation this type participates in.
     * 
     * @param name of role
     * @return a represenation of the role the other type in the association plays
     */
    MetaRole getMetaRole(String name);

    /**
     * @return map role names => MetaRole instances
     */
    java.util.Map<String, MetaRole> getMetaRoles();

    /**
     * @param name
     * @return information for the role or attribute of that name
     */
    MetaModelElement getElement(String name);

    /**
     * @return a description of the type
     */
    String getHelpText();
}
