package de.axbench.axlang.datamodel.localinstances;

import de.axbench.axlang.datamodel.abstractelements.IAXLangElement;
import de.axbench.axlang.datamodel.architecturemodels.Signal;
import de.axbench.axlang.datamodel.roledeclaration.BoundedRole;
import de.axbench.axlang.datamodel.roledeclaration.Cardinality;
import de.axbench.axlang.datamodel.roledeclaration.ReferenceKind;
import de.axbench.axlang.datamodel.roledeclaration.Role;

public class LocalSignalInstance extends LocalDataElementInstance {

    public LocalSignalInstance() {
        super();
        declareBoundedRole(ReferenceKind.REFERENCE, new BoundedRole(Role.SIGNAL, new Cardinality(1, 1)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IAXLangElement> T getInstantiatedElement() {
        return (T) getSignal();
    }

    @Override
    public <T extends IAXLangElement> boolean setInstantiatedElement(T instantiatedElement) {
        if (instantiatedElement instanceof Signal) {
            return setSignal((Signal) instantiatedElement);
        }
        return false;
    }

    /**
	 * @brief returns the instantiated signal
	 * @return the instantiated signal
	 */
    public Signal getSignal() {
        return (Signal) getReference(Role.SIGNAL);
    }

    /**
	 * @brief sets the instantiated signal, if possible
	 * @param port the instantiated signal
	 * @return true if the element could be set, else false
	 */
    public boolean setSignal(Signal signal) {
        return setReference(signal, Role.SIGNAL);
    }
}
