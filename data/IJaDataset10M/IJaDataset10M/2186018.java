package de.fraunhofer.isst.axbench.axlang.elements.localinstances;

import java.text.MessageFormat;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Storage;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLErrors.CommonErrors;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLException;
import de.fraunhofer.isst.axbench.axlang.utilities.ReferenceKind;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;
import de.fraunhofer.isst.axbench.axlang.utilities.ValidType;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Cardinality;

/**
 * @brief local instance of a storage
 * 
 * @author mgrosse
 * @version 0.9.0
 * @since 0.9.0
 */
public class LocalStorageInstance extends AbstractLocalInstance {

    private final Role ELEMENT_ROLE = Role.STORAGE;

    /**
	 * @brief constructor; adds the reference to a storage as the type ('Storage') of a local storage instance
	 */
    public LocalStorageInstance() {
        super();
        setValidElementTypes(ReferenceKind.REFERENCE, new ValidType(ELEMENT_ROLE, new Cardinality(1, 1)));
    }

    /**
	 * @brief returns the instantiated element
	 * @return the instantiated element
	 */
    @SuppressWarnings("unchecked")
    public Storage getInstantiatedElement() {
        return (Storage) getReference(ELEMENT_ROLE);
    }

    /**
	 * @brief set the reference to the instantiated storage
	 * @param newStorage the new referenced storage
	 * @throws AXLException if the reference to the new storage cannot be set
	 */
    public void setStorage(Storage newStorage) throws AXLException {
        addReference(newStorage, Role.STORAGE);
    }

    /**
	 * @brief Sets the instantiated element reference.
	 * @param <T> the type of the instantiated element
	 * @param newInstantiatedElement the new instantiated element
	 * @throws AXLException if the reference to the new instantiated element cannot be set
	 */
    public <T extends IAXLangElement> void setInstantiatedElement(T newElement) throws AXLException {
        if (ELEMENT_ROLE.getType().isInstance(newElement)) {
            setStorage((Storage) newElement);
        } else {
            throw new AXLException(MessageFormat.format(CommonErrors.ADD_WRONG_ROLE.getMessage(), getIdentifier(), getClassName(), newElement.getIdentifier(), ELEMENT_ROLE.getType().getSimpleName(), ELEMENT_ROLE.toString(), newElement.getClassName()));
        }
    }

    /**
	 * @brief removes the instantiated element
	 * @return true if the instantiated element could be removed or has been null, else false
	 */
    public boolean removeInstantiatedElement() {
        return removeReference(getInstantiatedElement(), ELEMENT_ROLE);
    }
}
