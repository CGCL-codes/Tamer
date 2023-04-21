package de.axbench.axlang.datamodel.architecturemodels;

import java.util.Collection;
import de.axbench.axlang.datamodel.abstractelements.AbstractAXLangElement;
import de.axbench.axlang.datamodel.globalinstances.ComponentInstance;
import de.axbench.axlang.datamodel.model.Model;
import de.axbench.axlang.datamodel.roledeclaration.BoundedRole;
import de.axbench.axlang.datamodel.roledeclaration.Cardinality;
import de.axbench.axlang.datamodel.roledeclaration.ReferenceKind;
import de.axbench.axlang.datamodel.roledeclaration.Role;

/**
 * @brief An architecture model is a container for components.
 * There are two concrete classes of architecture models: 
 * application models and resource models.
 * 
 * @author mgrosse
 * @version 0.11.0
 * @since 0.11.0
 */
public abstract class AbstractArchitectureModel extends AbstractAXLangElement {

    /**
	 * @brief constructor
	 * 
	 * children
	 * - components
	 */
    protected AbstractArchitectureModel() {
        super();
        declareBoundedRole(ReferenceKind.CHILD, new BoundedRole(Role.COMPONENT, new Cardinality(0, Cardinality.INFINITY)));
        declareBoundedRole(ReferenceKind.CHILD, new BoundedRole(Role.COMPONENTINSTANCE, new Cardinality(0, 1)));
    }

    @Override
    public Model getParent() {
        return (Model) super.getParent();
    }

    /**
	 * @brief returns the components
	 * @return the components
	 */
    @SuppressWarnings("unchecked")
    public Collection<Component> getComponents() {
        return (Collection<Component>) getChildren(Role.COMPONENT);
    }

    /**
	 * @brief adds a new component
	 * @param component the new component
	 * @return true if the addition succeeded, else false
	 */
    public boolean addComponent(Component component) {
        return addChild(component, Role.COMPONENT);
    }

    /**
	 * @brief returns the top component, if (at least) one exists, else null
	 * @return the top component, if (at least) one exists, else null
	 */
    public Component getTopComponent() {
        Component topComponent = null;
        for (Component component : getComponents()) {
            if (component.isTop()) {
                topComponent = component;
            }
        }
        return topComponent;
    }

    /**
	 * @brief returns the top component instance if it exists, else null
	 * @return the top component instance if it exists, else null
	 */
    public ComponentInstance getTopComponentInstance() {
        return (ComponentInstance) getChild(Role.COMPONENTINSTANCE);
    }

    /**
	 * @brief creates the component instance hierarchy 
	 * in that an instance of the top component (if one exists) is created 
	 */
    public void createInstances() {
        if (getTopComponent() == null) {
            return;
        }
        ComponentInstance topComponentInstance = getTopComponent().createComponentInstance();
        if (topComponentInstance != null) {
            addChild(topComponentInstance, Role.COMPONENTINSTANCE);
        }
    }
}
