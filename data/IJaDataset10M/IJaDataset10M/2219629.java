package org.j2eebuilder;

import org.jdom.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import org.j2eebuilder.util.LogManager;
import org.j2eebuilder.util.UtilityBean;

/**
 * @(#)ModuleDefinition.java	1.350 01/12/03
 * Specifies an adapter definition to read the adapter specific file and convert
 * it into an XML document
 */
public class ModuleDefinition implements Definition {

    private static transient LogManager log = new LogManager(ModuleDefinition.class);

    private String name;

    private String description;

    private String type;

    private String layout;

    private Collection<ComponentDefinition> componentDefinitions = new ArrayList();

    public ModuleDefinition() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLayout() {
        return this.layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
	  * get collection of component definitions
	  */
    public Collection<ComponentDefinition> getColOfComponentDefinition() {
        return this.componentDefinitions;
    }

    /**
	* get an array of component definitions
	* @return an array of component definitions
	*/
    public ComponentDefinition[] getComponentDefinitions() {
        return (ComponentDefinition[]) this.componentDefinitions.toArray(new ComponentDefinition[0]);
    }

    /**
	* add component definition to the collection
	* @param ComponentDefinition the component definition object
	*/
    public void addComponentDefinition(ComponentDefinition recordDefinition) throws DuplicateDefinitionException, DefinitionException {
        if (recordDefinition == null) throw new DefinitionException("Null is not a valid ComponentDefinition.");
        ComponentDefinition componentDefinition = this.findComponentDefinitionByName(recordDefinition.getName());
        if (componentDefinition != null) throw new DuplicateDefinitionException("Could not add: " + recordDefinition);
        this.componentDefinitions.add(recordDefinition);
    }

    /**
	* find ComponentDefinition by type
	* @param String name of the componet
	* @return ComponentDefinition the component's definition
	*/
    public Collection<ComponentDefinition> findComponentDefinitionByType(String type) {
        Collection<ComponentDefinition> colOfComponentDefinition = new HashSet();
        for (ComponentDefinition componentDefinition : this.getColOfComponentDefinition()) {
            if (componentDefinition.getType() != null && componentDefinition.getType().equals(type)) colOfComponentDefinition.add(componentDefinition);
        }
        return colOfComponentDefinition;
    }

    /**
	* find ComponentDefinition by name
	* @param String name of the componet
	* @return ComponentDefinition the component's definition
	*/
    public ComponentDefinition findComponentDefinitionByName(String name) {
        for (ComponentDefinition componentDefinition : this.getColOfComponentDefinition()) {
            if (componentDefinition.getName() != null && componentDefinition.getName().equals(name)) return componentDefinition;
        }
        return null;
    }

    public ComponentDefinition findComponentDefinitionByNonManagedBeanClassName(String className) throws DefinitionException {
        for (ComponentDefinition componentDefinition : this.getColOfComponentDefinition()) {
            try {
                NonManagedBeanDefinition useBeanDefinition = componentDefinition.getNonManagedBeansDefinition().findNonManagedBeanDefinitionByClassName(className);
                return componentDefinition;
            } catch (DefinitionNotFoundException dnfe) {
            }
        }
        return null;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ModuleDefinition[");
        buf.append(this.getName());
        buf.append(", ");
        buf.append(this.getDescription());
        buf.append(", ");
        buf.append(this.getType());
        buf.append(", ");
        buf.append(this.getLayout());
        buf.append(", ");
        buf.append("ComponentDefinitions[");
        for (ComponentDefinition componentDefinition : this.getComponentDefinitions()) {
            buf.append("ComponentDefinition[");
            buf.append(componentDefinition);
            buf.append("]");
        }
        buf.append("]");
        return buf.toString();
    }
}
