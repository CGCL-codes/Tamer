package org.parallelj.providers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.eef.runtime.api.component.IPropertiesEditionComponent;
import org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider;
import org.parallelj.components.HandlerPropertiesEditionComponent;
import org.parallelj.model.Handler;
import org.parallelj.model.ParallelJPackage;

/**
 * @author
 * 
 */
public class HandlerPropertiesEditionProvider implements IPropertiesEditionProvider {

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject)
	 * 
	 */
    public boolean provides(EObject eObject) {
        return (eObject instanceof Handler) && (ParallelJPackage.eINSTANCE.getHandler() == eObject.eClass());
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject, java.lang.String)
	 * 
	 */
    public boolean provides(EObject eObject, String part) {
        return (eObject instanceof Handler) && (HandlerPropertiesEditionComponent.BASE_PART.equals(part));
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject, java.lang.Class)
	 * 
	 */
    public boolean provides(EObject eObject, java.lang.Class refinement) {
        return (eObject instanceof Handler) && (refinement == HandlerPropertiesEditionComponent.class);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject, java.lang.String, java.lang.Class)
	 * 
	 */
    public boolean provides(EObject eObject, String part, java.lang.Class refinement) {
        return (eObject instanceof Handler) && ((HandlerPropertiesEditionComponent.BASE_PART.equals(part) && refinement == HandlerPropertiesEditionComponent.class));
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#getPropertiesEditionComponent(org.eclipse.emf.ecore.EObject,
	 *  java.lang.String)
	 * 
	 */
    public IPropertiesEditionComponent getPropertiesEditionComponent(EObject eObject, String editing_mode) {
        if (eObject instanceof Handler) {
            return new HandlerPropertiesEditionComponent(eObject, editing_mode);
        }
        return null;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#getPropertiesEditionComponent(org.eclipse.emf.ecore.EObject,
	 *  java.lang.String, java.lang.String)
	 * 
	 */
    public IPropertiesEditionComponent getPropertiesEditionComponent(EObject eObject, String editing_mode, String part) {
        if (eObject instanceof Handler) {
            if (HandlerPropertiesEditionComponent.BASE_PART.equals(part)) return new HandlerPropertiesEditionComponent(eObject, editing_mode);
        }
        return null;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#getPropertiesEditionComponent(org.eclipse.emf.ecore.EObject,
	 *  java.lang.String, java.lang.String, java.lang.Class)
	 * 
	 */
    public IPropertiesEditionComponent getPropertiesEditionComponent(EObject eObject, String editing_mode, String part, java.lang.Class refinement) {
        if (eObject instanceof Handler) {
            if (HandlerPropertiesEditionComponent.BASE_PART.equals(part) && refinement == HandlerPropertiesEditionComponent.class) return new HandlerPropertiesEditionComponent(eObject, editing_mode);
        }
        return null;
    }
}
