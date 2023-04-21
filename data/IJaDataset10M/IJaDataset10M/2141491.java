package org.parallelj.providers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.eef.runtime.api.component.IPropertiesEditionComponent;
import org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider;
import org.parallelj.components.ProcedurePropertiesEditionComponent;
import org.parallelj.model.ParallelJPackage;
import org.parallelj.model.Procedure;

/**
 * @author
 * 
 */
public class ProcedurePropertiesEditionProvider implements IPropertiesEditionProvider {

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject)
	 * 
	 */
    public boolean provides(EObject eObject) {
        return (eObject instanceof Procedure) && (ParallelJPackage.eINSTANCE.getProcedure() == eObject.eClass());
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject, java.lang.String)
	 * 
	 */
    public boolean provides(EObject eObject, String part) {
        return (eObject instanceof Procedure) && (ProcedurePropertiesEditionComponent.BASE_PART.equals(part));
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject, java.lang.Class)
	 * 
	 */
    public boolean provides(EObject eObject, java.lang.Class refinement) {
        return (eObject instanceof Procedure) && (refinement == ProcedurePropertiesEditionComponent.class);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#provides(org.eclipse.emf.ecore.EObject, java.lang.String, java.lang.Class)
	 * 
	 */
    public boolean provides(EObject eObject, String part, java.lang.Class refinement) {
        return (eObject instanceof Procedure) && ((ProcedurePropertiesEditionComponent.BASE_PART.equals(part) && refinement == ProcedurePropertiesEditionComponent.class));
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.eef.runtime.api.providers.IPropertiesEditionProvider#getPropertiesEditionComponent(org.eclipse.emf.ecore.EObject,
	 *  java.lang.String)
	 * 
	 */
    public IPropertiesEditionComponent getPropertiesEditionComponent(EObject eObject, String editing_mode) {
        if (eObject instanceof Procedure) {
            return new ProcedurePropertiesEditionComponent(eObject, editing_mode);
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
        if (eObject instanceof Procedure) {
            if (ProcedurePropertiesEditionComponent.BASE_PART.equals(part)) return new ProcedurePropertiesEditionComponent(eObject, editing_mode);
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
        if (eObject instanceof Procedure) {
            if (ProcedurePropertiesEditionComponent.BASE_PART.equals(part) && refinement == ProcedurePropertiesEditionComponent.class) return new ProcedurePropertiesEditionComponent(eObject, editing_mode);
        }
        return null;
    }
}
