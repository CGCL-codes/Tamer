package de.fraunhofer.isst.axbench.maturitychecker;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.maturitychecker.MaturityDefinition.Property;

/**
 * @brief abstract super class for all element specific maturity checkers
 * 
 * @author mgrosse
 * @version 0.9.0
 * @since 0.9.0
 */
public abstract class AbstractSpecificMaturityChecker implements ISpecificMaturityChecker {

    protected Map<Property, Collection<IAXLangElement>> propertyMap;

    protected Map<Property, Map<IAXLangElement, Collection<IAXLangElement>>> perturbingElementMap;

    /**
	 * @brief constructor
	 * 
	 * initializes the maps
	 */
    public AbstractSpecificMaturityChecker() {
        propertyMap = new HashMap<Property, Collection<IAXLangElement>>();
        perturbingElementMap = new HashMap<Property, Map<IAXLangElement, Collection<IAXLangElement>>>();
        for (Property property : Property.values()) {
            propertyMap.put(property, new HashSet<IAXLangElement>());
            Map<IAXLangElement, Collection<IAXLangElement>> elementMap = new HashMap<IAXLangElement, Collection<IAXLangElement>>();
            perturbingElementMap.put(property, elementMap);
        }
    }

    /**
	 * @brief getter method for the propertyMap
	 */
    public Map<Property, Collection<IAXLangElement>> getPropertyMap() {
        return propertyMap;
    }

    /**
	 * @brief getter method for the perturbingElementMap
	 */
    public Map<Property, Map<IAXLangElement, Collection<IAXLangElement>>> getPerturbingElementMap() {
        return perturbingElementMap;
    }

    /**
	 * @brief adds a data element to the port part of the property part of the perturbing element map
	 * @param property the property
	 * @param port the port
	 * @param dataElement the data element
	 */
    protected void addToPerturbingElementMap(Property property, IAXLangElement axlElement, IAXLangElement perturbingElement) {
        if (!perturbingElementMap.get(property).containsKey(axlElement)) {
            perturbingElementMap.get(property).put(axlElement, new HashSet<IAXLangElement>());
        }
        perturbingElementMap.get(property).get(axlElement).add(perturbingElement);
    }

    /**
	 * @brief checks whether the checker is to be used for an element of the type
	 * @param type class of the aXLang-element that is to be checked
	 */
    public abstract boolean isCheckerFor(Object type);

    /**
	 * @brief checks the element and thereby updates the maps
	 * @param axlElement the aXLang-element that is to be checked
	 */
    public abstract void check(IAXLangElement axlElement);
}
