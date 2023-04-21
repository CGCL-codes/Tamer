package de.axbench.axlang.datamodel.globalinstances;

import de.axbench.axlang.datamodel.abstractelements.IAXLangElement;
import de.axbench.axlang.datamodel.architecturemodels.Operation;
import de.axbench.axlang.datamodel.attributes.AXLangAttributeID;
import de.axbench.axlang.datamodel.roledeclaration.BoundedRole;
import de.axbench.axlang.datamodel.roledeclaration.Cardinality;
import de.axbench.axlang.datamodel.roledeclaration.ReferenceKind;
import de.axbench.axlang.datamodel.roledeclaration.Role;

/**
 * @brief A SignalInstance is a global instance of an Operation.
 * @author mgrosse
 * @version 0.11.0
 * @since 0.11.0
 */
public class OperationInstance extends DataElementInstance {

    /**
	 * @brief constructor
	 * 
	 * attributes 
	 * - data type
	 * 
	 * children
	 * - parameter instances
	 * 
	 * references
	 * - operation
	 */
    public OperationInstance() {
        super();
        declareAttribute(AXLangAttributeID.DATATYPE);
        declareBoundedRole(ReferenceKind.CHILD, new BoundedRole(Role.PARAMETERINSTANCE, new Cardinality(0, Cardinality.INFINITY)));
        declareBoundedRole(ReferenceKind.REFERENCE, new BoundedRole(Role.OPERATION, new Cardinality(1, 1)));
    }

    @Override
    public ComponentInstance getParent() {
        return (ComponentInstance) super.getParent();
    }

    /**
	 * @brief returns the instantiated operation
	 * @return the instantiated operation
	 */
    public Operation getOperation() {
        return (Operation) getReference(Role.OPERATION);
    }

    /**
	 * @brief sets the instantiated operation
	 * @param instantiatedOperation the instantiated operation
	 * @return true if the element could be set, else false
	 */
    public boolean setOperation(Operation instantiatedOperation) {
        return setReference(instantiatedOperation, Role.OPERATION);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IAXLangElement> T getInstantiatedElement() {
        return (T) getOperation();
    }

    @Override
    public boolean setInstantiatedElement(IAXLangElement instantiatedElement) {
        if (!(instantiatedElement instanceof Operation)) {
            return false;
        }
        return setOperation((Operation) instantiatedElement);
    }
}
