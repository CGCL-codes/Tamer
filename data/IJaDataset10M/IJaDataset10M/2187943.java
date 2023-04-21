package org.jjflyboy.slee.descriptors.validation.constraints;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.IClientSelector;
import Slee11.Slee11Package;

/**
 * Selects constraints for the constraint binding when the {@link org.jjflyboy.slee.descriptors.validation.actions.BatchValidationDelegate}
 *  was the entry point into validation.
 * 
 * @author Chris McGee
 */
public class ValidationDelegateClientSelector implements IClientSelector {

    public boolean selects(Object object) {
        if (object instanceof EObject) {
            EObject eObject = (EObject) object;
            boolean inPackage = eObject.eClass().getEPackage() == Slee11Package.eINSTANCE;
            return inPackage;
        }
        return false;
    }
}
