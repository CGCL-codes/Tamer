package org.isistan.flabot.coremodel;

import java.util.List;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * Represents a responsibility-component-role keyed registry
 * @author $Author: mblech $
 * @model
 */
public interface ResponsibilityRegistry extends EObject {

    /**
	 * Register the given registrable on the given responsibility for
	 * the given component and role (component and role can be null)
	 * @param responsibility the responsibility
	 * @param component the component
	 * @param role the role
	 * @param registrable the registrable
	 * @return the registrable that was registered previously in the same place
	 */
    Registrable register(Responsibility responsibility, ComponentModel component, ComponentRole role, Registrable registrable);

    /**
	 * Get the registrable that is registered on the given responsibility,
	 * component and role
	 * @param responsibility
	 * @param component
	 * @param role
	 * @return
	 */
    Registrable get(Responsibility responsibility, ComponentModel component, ComponentRole role);

    /**
	 * Get the role based registry. For internal use only, use the
	 * register and get methods instead.
	 * @return the role-based registry
	 * @model mapType="RoleToMapMapEntry" keyType="ComponentRole" valueType="java.util.Map.Entry"
	 */
    EMap getRoleBasedRegistry();

    /**
	 * Get the component based registry. For internal use only,
	 * use the register and get methods instead.
	 * @return the component-based registry
	 * @model mapType="ComponentToMapMapEntry" keyType="ComponentModel" valueType="java.util.Map.Entry"
	 */
    EMap getComponentBasedRegistry();

    /**
	 * Get the responsibility based registry. For internal use only,
	 * use the register and get methods instead.
	 * @return the responsibility-based registry
	 * @model mapType="ResponsibilityToRegistrableMapEntry" keyType="Responsibility" valueType="Registrable"
	 */
    EMap getResponsibilityBasedRegistry();

    /**
	 * Get a list containing all the registrables that are registered in
	 * this registry.
	 * @return
	 */
    List getAllRegistrables();
}
