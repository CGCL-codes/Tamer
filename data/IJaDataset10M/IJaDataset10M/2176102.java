package org.mantikhor.llapi;

import java.util.List;

/**
 * 
 * NOT EXPECTED TO BE IMPLEMENTED -- FOR EXTENSION ONLY
 * 
 * @author Bill Blondeau
 *
 */
public interface Burst extends Container {

    /**
     * Creates and returns a List<Property> containing references to the
     * instance's <code>Property</code> items. This method's return type 
     * fulfills the Parameter Object Defense Guarantee.
     * 
     * @return The instance's properties, represented as a <code>List<Property></code>,
     *      with order preserved.
     */
    public abstract List<BaseProperty> getProperties();

    /**
     * Convenience method
     * @return <code>true</code> if a call to <code>countProperties()</code>
     *      would return zero; else <code>false</code>.
     */
    public abstract boolean isEmpty();

    /**
     * Returns the count of the instance's <code>Property</code> 
     * items
     * 
     * @return
     */
    public abstract int countProperties();

    /**
     * Creates and returns a List<Property> containing references to the
     * instance's <code>Property</code> items whose property category
     * is the parameter.
     * 
     * More formally, the return contains all of the instance's property
     * items <code>pi</code> for which
     * <code>pi.getPropertyDefinition().getCategory() == category</code>.
     * 
     * This method's return type 
     * fulfills the Parameter Object Defense Guarantee.
     * 
     * @return The instance's properties whose type URI is contained
     *      within the parameter, represented as a <code>List<Property></code>,
     *      with order preserved.
     */
    public abstract List<BaseProperty> getProperties(PropertyCategory category);

    public abstract boolean containsProperties(List<BaseProperty> properties);

    public abstract boolean containsProperties(Burst other);

    /**
     * Convenience method.
     * @return <code>false</code> if a call to 
     *      <code>countProperties(category)</code> would return zero; 
     *      else <code>true</code>.
     */
    public abstract boolean hasProperties(PropertyCategory category);

    /**
     * Convenience method. Returns the same as 
     * <code>this.getProperties(category).size()</code>.
     * 
     * @return
     */
    public abstract int countProperties(PropertyCategory category);

    /**
     * Creates and returns a List<Property> containing references to the
     * instance's <code>Property</code> items whose property type is contained
     * in the domain represented by the parameter.
     * 
     * More formally, the return contains all of the instance's property
     * items <code>pi</code> for which
     * <code>domain.containsURI(pi.getPropertyType() == true</code>.
     * 
     * This method's return type 
     * fulfills the Parameter Object Defense Guarantee.
     * 
     * @return The instance's properties whose type URI is contained
     *      within the parameter, represented as a <code>List<Property></code>,
     *      with order preserved.
     */
    public abstract List<BaseProperty> getProperties(DomainURI domain);

    /**
     * Convenience method.
     * @return <code>false</code> if a call to 
     *      <code>countProperties(domain)</code> would return zero; 
     *      else <code>true</code>.
     */
    public abstract boolean hasProperties(DomainURI domain);

    /**
     * Convenience method. Returns the same as 
     * <code>this.getProperties(domain).size()</code>.
     * 
     * @return
     */
    public abstract int countProperties(DomainURI domain);

    public abstract boolean containsProperty(BaseProperty property);

    /**
     * Creates and returns a List<Property> containing references to the
     * instance's <code>Property</code> items whose property definition
     * is identical to the parameter.
     * 
     * More formally, the return contains all of the instance's property
     * items <code>pi</code> for which
     * <code>pi.getPropertyDefinition().equals(propertyDef) == true</code>.
     * 
     * This method's return type 
     * fulfills the Parameter Object Defense Guarantee.
     * 
     * @return The instance's properties whose type URI is contained
     *      within the parameter, represented as a <code>List<Property></code>,
     *      with order preserved.
     */
    public abstract List<BaseProperty> getProperties(PropertyDefinition propertyDef);

    /**
     * Convenience method. Equivalent to <code>! this.getProperties(propertyDef).isEmpty()</code>
     * 
     * @param propertyDef
     * @return
     */
    public abstract boolean hasProperties(PropertyDefinition propertyDef);

    /**
     * Convenience method. Equivalent to <code>this.getProperties(propertyDef).size()</code>
     * 
     * @param propertyDef
     * @return
     */
    public abstract int countProperties(PropertyDefinition propertyDef);

    /**
     * 
     * @param pattern
     * @return
     */
    public abstract boolean containsPattern(Burst pattern, boolean recurse);

    public abstract boolean isContainedByPattern(Burst pattern, boolean recurse);

    public abstract boolean overlapsPattern(Burst pattern, boolean recurse);

    public abstract boolean matchesPatternExactly(Burst pattern, boolean recurse);

    public abstract boolean sharesNothingWithPattern(Burst pattern);
}
