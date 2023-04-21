package org.parallelj.providers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.eef.runtime.api.component.IPropertiesEditionComponent;
import org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider;
import org.parallelj.components.ForEachLoopPropertiesEditionComponent;
import org.parallelj.model.ForEachLoop;
import org.parallelj.model.ParallelJPackage;

/**
 * @author
 * 
 */
public class ForEachLoopPropertiesEditionProvider implements IPropertiesEditionProvider {

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject)
	 * 
	 */
    public boolean provides(EObject eObject) {
        return (eObject instanceof ForEachLoop) && (ParallelJPackage.eINSTANCE.getForEachLoop() == eObject.eClass());
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject, java.lang.String)
	 * 
	 */
    public boolean provides(EObject eObject, String part) {
        return (eObject instanceof ForEachLoop) && (ForEachLoopPropertiesEditionComponent.BASE_PART.equals(part));
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject, java.lang.Class)
	 * 
	 */
    public boolean provides(EObject eObject, java.lang.Class refinement) {
        return (eObject instanceof ForEachLoop) && (refinement == ForEachLoopPropertiesEditionComponent.class);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject, java.lang.String, java.lang.Class)
	 * 
	 */
    public boolean provides(EObject eObject, String part, java.lang.Class refinement) {
        return (eObject instanceof ForEachLoop) && ((ForEachLoopPropertiesEditionComponent.BASE_PART.equals(part) && refinement == ForEachLoopPropertiesEditionComponent.class));
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#getPropertiesEditionComponent(org.eclipse.emf.ecore.EObject,
	 *  java.lang.String)
	 * 
	 */
    public IPropertiesEditionComponent getPropertiesEditionComponent(EObject eObject, String editing_mode) {
        if (eObject instanceof ForEachLoop) {
            return new ForEachLoopPropertiesEditionComponent(eObject, editing_mode);
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
        if (eObject instanceof ForEachLoop) {
            if (ForEachLoopPropertiesEditionComponent.BASE_PART.equals(part)) return new ForEachLoopPropertiesEditionComponent(eObject, editing_mode);
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
        if (eObject instanceof ForEachLoop) {
            if (ForEachLoopPropertiesEditionComponent.BASE_PART.equals(part) && refinement == ForEachLoopPropertiesEditionComponent.class) return new ForEachLoopPropertiesEditionComponent(eObject, editing_mode);
        }
        return null;
    }
}
