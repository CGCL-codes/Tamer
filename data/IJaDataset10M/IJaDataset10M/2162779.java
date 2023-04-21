package net.sourceforge.javo2.assembler.population.impl;

import generated.NestedProperty;
import generated.ValueObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.javo2.api.IValueObject;
import net.sourceforge.javo2.assembler.ProxyFactory;
import net.sourceforge.javo2.assembler.impl.PropertyNotFoundException;
import net.sourceforge.javo2.assembler.internalapi.IValueObjectProxy;
import net.sourceforge.javo2.assembler.population.PropertyPopulatorsContainer;
import net.sourceforge.javo2.assembler.population.api.IPropertyPopulator;
import net.sourceforge.javo2.definition.DefinitionProcessor;
import net.sourceforge.javo2.utils.Reflections;
import org.apache.commons.lang.StringUtils;

/**
 * @author Nicolás Di Benedetto
 * {@link mailto:nikodb@users.sourceforge.net nikodb@users.sourceforge.net}.<br>
 * Created on 26/09/2007.<br>
 * This class populates nested properties.<br>
 * Nested properties can be either public or private. <br>
 * Public properties are those that uses the "ref" attribute to indicate that they reference an already (somewhere else) defined VO.<br>
 * Private properties declare a "root-been" attribute to indicate the source (business) object in order to create an "in-line" definition. 
 */
public class NestedPropertyPopulator extends AbstractPropertyPopulator {

    /**
	 * Property populators for nested operation delegation.
	 */
    private PropertyPopulatorsContainer propertyPopulatorsContainer = null;

    /**
	 * A holder property for the package definition.
	 */
    private Map<NestedProperty, String> parentPackageMapping = null;

    /**
	 * Default Constructor.
	 */
    public NestedPropertyPopulator() {
    }

    /**
	 * @see net.sourceforge.javo2.assembler.population.api.IPropertyPopulator#populateProxy(net.sourceforge.javo2.assembler.internalapi.IValueObjectProxy, java.lang.Object, generated.ValueObject)
	 */
    public void populateProxy(IValueObjectProxy proxy, final Object entity, final ValueObject valueObject) {
        for (NestedProperty nestedProperty : valueObject.getNestedProperty()) {
            if (nestedProperty.isReadOnly()) {
                continue;
            }
            this.getParentPackageMapping().put(nestedProperty, DefinitionProcessor.getPackage(valueObject));
            this.populateProxy(proxy, super.getValueForProperty(nestedProperty.getName(), entity), nestedProperty);
        }
    }

    /**
	 * @see net.sourceforge.javo2.assembler.population.api.IPropertyPopulator#populateProxy(net.sourceforge.javo2.assembler.internalapi.IValueObjectProxy, java.lang.Object, generated.NestedProperty)
	 */
    public void populateProxy(IValueObjectProxy proxy, final Object entity, final NestedProperty nestedProperty) {
        if (null != nestedProperty.getRef()) {
            this.populateReferencedDefinition(proxy, entity, nestedProperty);
        } else {
            this.populateInnerDefinition(proxy, entity, nestedProperty);
        }
    }

    /**
	 * Populates a referenced nested property and its sub-nesteds.
	 * @param proxy the proxy to populate.
	 * @param entity the entity with the values to copy.
	 * @param nestedProperty the property definition.
	 */
    private void populateReferencedDefinition(IValueObjectProxy proxy, final Object entity, final NestedProperty nestedProperty) {
        ValueObject referencedVO = DefinitionProcessor.getValueObjectById(nestedProperty.getRef());
        IValueObjectProxy nestedProxy = ProxyFactory.getProxyInstance(IValueObjectProxy.class);
        for (IPropertyPopulator propertyPopulator : this.getPropertyPopulatorsContainer().getPopulators()) {
            if (propertyPopulator.equals(this)) {
                continue;
            }
            propertyPopulator.populateProxy(nestedProxy, entity, referencedVO);
        }
        Class<?> propertyType = super.getPropertyType(DefinitionProcessor.getTargetFQCN(referencedVO));
        @SuppressWarnings("unchecked") Class<IValueObject> definitionInterface = (Class<IValueObject>) propertyType;
        IValueObject adaptedProxy = ProxyFactory.adaptTo(nestedProxy, definitionInterface);
        proxy.addProperty(nestedProperty.getName(), propertyType, adaptedProxy);
    }

    /**
	 * Populates an inner definition and its sub-nesteds.
	 * @param proxy the PARENT proxy to populate.
	 * @param entity the entity with the values to copy.
	 * @param nestedProperty the property definition.
	 */
    private void populateInnerDefinition(IValueObjectProxy proxy, Object entity, NestedProperty nestedProperty) {
        String targetFQCN = null;
        String currentPackage = null;
        if (nestedProperty.getInterfaceName().contains(".")) {
            targetFQCN = nestedProperty.getInterfaceName();
            currentPackage = StringUtils.substring(targetFQCN, 0, StringUtils.lastIndexOf(targetFQCN, "."));
        } else {
            String parentPackage = this.getParentPackageMapping().get(nestedProperty);
            currentPackage = parentPackage + "." + nestedProperty.getName();
            targetFQCN = currentPackage + "." + nestedProperty.getInterfaceName();
            nestedProperty.setInterfaceName(targetFQCN);
        }
        IValueObjectProxy nestedProxy = ProxyFactory.getProxyInstance(IValueObjectProxy.class);
        for (IPropertyPopulator propertyPopulator : this.getPropertyPopulatorsContainer().getPopulators()) {
            if (propertyPopulator.equals(this)) {
                continue;
            }
            propertyPopulator.populateProxy(nestedProxy, entity, nestedProperty);
        }
        Class<?> propertyType = super.getPropertyType(nestedProperty.getInterfaceName());
        @SuppressWarnings("unchecked") Class<IValueObject> definitionInterface = (Class<IValueObject>) propertyType;
        IValueObject adaptedProxy = ProxyFactory.adaptTo(nestedProxy, definitionInterface);
        proxy.addProperty(nestedProperty.getName(), propertyType, adaptedProxy);
        this.forwardProxyHierarchy(nestedProxy, entity, nestedProperty, currentPackage);
    }

    /**
	 * Moves forward into the hierarchy (property, entity and package) and delegates the population.
	 * @param parentProxy the parent proxy to get its hierarchy.
	 * @param entity the parent entity.
	 * @param nestedProperty the property to iterate over its nesteds.
	 * @param parentPackage the parent package.
	 */
    private void forwardProxyHierarchy(IValueObjectProxy parentProxy, Object entity, NestedProperty nestedProperty, String parentPackage) {
        for (NestedProperty childProperty : nestedProperty.getNestedProperty()) {
            this.getParentPackageMapping().put(childProperty, parentPackage);
            Object childEntity = super.getValueForProperty(childProperty.getName(), entity);
            this.populateProxy(parentProxy, childEntity, childProperty);
        }
    }

    /**
	 * Getter method for the propertyPopulatorsContainer attribute.
	 * @return the propertyPopulatorsContainer attribute.
	 */
    public PropertyPopulatorsContainer getPropertyPopulatorsContainer() {
        return (null == this.propertyPopulatorsContainer) ? this.propertyPopulatorsContainer = new PropertyPopulatorsContainer(null) : this.propertyPopulatorsContainer;
    }

    /**
	 * Setter method for the propertyPopulatorsContainer attribute.
	 * @param propertyPopulatorsContainer the propertyPopulatorsContainer attribute to set.
	 */
    public void setPropertyPopulatorsContainer(PropertyPopulatorsContainer propertyPopulatorsContainer) {
        this.propertyPopulatorsContainer = propertyPopulatorsContainer;
    }

    /**
	 * Getter method for the parentPackageMapping attribute.
	 * This method is a null-safe getter.
	 * Default value is a SynchronizedMap so it's Thread-safe as well.
	 * @return the parentPackageMapping attribute.
	 */
    protected Map<NestedProperty, String> getParentPackageMapping() {
        return (null == this.parentPackageMapping) ? this.parentPackageMapping = Collections.synchronizedMap(new HashMap<NestedProperty, String>()) : this.parentPackageMapping;
    }

    /**
	 * @see net.sourceforge.javo2.assembler.population.api.IPropertyPopulator#populateEntity(java.lang.Object, net.sourceforge.javo2.assembler.internalapi.IValueObjectProxy, generated.ValueObject)
	 */
    public void populateEntity(Object entity, IValueObjectProxy proxy, ValueObject valueObject) {
        for (NestedProperty nestedProperty : valueObject.getNestedProperty()) {
            this.populateEntity(entity, proxy, nestedProperty);
        }
    }

    /**
	 * @see net.sourceforge.javo2.assembler.population.api.IPropertyPopulator#populateEntity(java.lang.Object, net.sourceforge.javo2.assembler.internalapi.IValueObjectProxy, generated.NestedProperty)
	 */
    public void populateEntity(Object entity, IValueObjectProxy proxy, NestedProperty nestedProperty) {
        Object entityNestedInstance = Reflections.newInstance(nestedProperty.getRootBean());
        super.setPropertyOnEntity(entity, entityNestedInstance, nestedProperty.getName());
        IValueObject propertyValueFromEntity = (IValueObject) super.getPropertyValueFromEntity(entity, proxy, nestedProperty.getName());
        IValueObjectProxy nestedProxy = ProxyFactory.getProxyInstance(IValueObjectProxy.class);
        for (IPropertyPopulator propertyPopulator : this.getPropertyPopulatorsContainer().getPopulators()) {
            if (this.equals(propertyPopulator)) {
                continue;
            }
            propertyPopulator.populateProxy(nestedProxy, propertyValueFromEntity, nestedProperty);
        }
        for (IPropertyPopulator propertyPopulator : this.getPropertyPopulatorsContainer().getPopulators()) {
            if (this.equals(propertyPopulator)) {
                continue;
            }
            propertyPopulator.populateEntity(entityNestedInstance, nestedProxy, nestedProperty);
        }
        for (NestedProperty nestedsNested : nestedProperty.getNestedProperty()) {
            this.populateNestedEntity(entityNestedInstance, nestedProxy, nestedsNested);
        }
    }

    /**
	 * @param entity
	 * @param proxy
	 * @param nestedProperty
	 */
    private void populateNestedEntity(Object entity, IValueObjectProxy proxy, NestedProperty nestedProperty) {
        Object entityNestedInstance = Reflections.newInstance(nestedProperty.getRootBean());
    }
}
