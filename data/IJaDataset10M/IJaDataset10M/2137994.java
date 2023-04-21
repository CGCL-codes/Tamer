package de.axbench.axlang.datamodel.architecturemodels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import de.axbench.axlang.datamodel.abstractelements.AbstractAXLangElement;
import de.axbench.axlang.datamodel.abstractelements.IAXLangElement;
import de.axbench.axlang.datamodel.attributes.AXLangAttributeID;
import de.axbench.axlang.datamodel.attributes.Direction;
import de.axbench.axlang.datamodel.attributes.PortType;
import de.axbench.axlang.datamodel.globalinstances.PortInstance;
import de.axbench.axlang.datamodel.roledeclaration.BoundedRole;
import de.axbench.axlang.datamodel.roledeclaration.Cardinality;
import de.axbench.axlang.datamodel.roledeclaration.ReferenceKind;
import de.axbench.axlang.datamodel.roledeclaration.Role;
import de.axbench.axlang.datamodel.validity.IInvalidity;
import de.axbench.axlang.datamodel.validity.PortInvalidity;
import de.axbench.axlang.datamodel.validity.PortInvalidity.PortInvalidityID;

/**
 * @brief A port is an access point of a component; it is declared by its direction, 
 * its signals and operations (in case of an application port) or its port type (in case of a resource port), 
 * and its name.
 * 
 * @author mgrosse
 * @version 0.11.0
 * @since 0.11.0
 */
public class Port extends AbstractAXLangElement {

    /**
	 * @brief constructor
	 * 
	 * attributes
	 * - direction
	 * - port type
	 * 
	 * references
	 * - signals
	 * - operations
	 */
    public Port() {
        super();
        declareAttribute(AXLangAttributeID.ISOPTIONAL);
        declareAttribute(AXLangAttributeID.DIRECTION);
        declareAttribute(AXLangAttributeID.PORTTYPE);
        declareBoundedRole(ReferenceKind.REFERENCE, new BoundedRole(Role.SIGNAL, new Cardinality(0, Cardinality.INFINITY)));
        declareBoundedRole(ReferenceKind.REFERENCE, new BoundedRole(Role.OPERATION, new Cardinality(0, Cardinality.INFINITY)));
    }

    /**
	 * @brief a port is an interface element
	 */
    public boolean isInterfaceElement() {
        return true;
    }

    /**
	 * @brief returns the parent component
	 */
    @Override
    public Component getParent() {
        return (Component) super.getParent();
    }

    /**
	 * @brief indicates whether this element is optional
	 * @return true if the element is optional, else false
	 */
    public boolean isOptional() {
        return (Boolean) getAttributeValue(AXLangAttributeID.ISOPTIONAL.getAttribute());
    }

    /**
	 * @brief sets the isOptional value
	 * @param isOptional true or false
	 * @return true
	 */
    public boolean setOptional(boolean isOptional) {
        return setAttributeValue(AXLangAttributeID.ISOPTIONAL.getAttribute(), isOptional);
    }

    /**
	 * @brief returns the direction
	 * @return the direction
	 */
    public Direction getDirection() {
        return (Direction) getAttributeValue(AXLangAttributeID.DIRECTION.getAttribute());
    }

    /**
	 * @brief sets the direction
	 * @param direction the direction 
	 * @return true if the operation succeeded, else false
	 */
    public boolean setDirection(Direction direction) {
        return setAttributeValue(AXLangAttributeID.DIRECTION.getAttribute(), direction);
    }

    /**
	 * @brief returns the port type
	 * @return the port type
	 */
    public PortType getPortType() {
        return (PortType) getAttributeValue(AXLangAttributeID.PORTTYPE.getAttribute());
    }

    /**
	 * @brief sets the port type
	 * @param port type the port type 
	 * @return true if the operation succeeded, else false
	 */
    public boolean setPortType(PortType portType) {
        return setAttributeValue(AXLangAttributeID.PORTTYPE.getAttribute(), portType);
    }

    /**
	 * @brief returns the referenced signals
	 * @return the referenced signals
	 */
    @SuppressWarnings("unchecked")
    public Collection<Signal> getSignals() {
        return (Collection<Signal>) getReferences(Role.SIGNAL);
    }

    /**
	 * @brief adds a signal
	 * @param signal the signal to be added
	 * @return true if the addition succeeded, else false
	 */
    public boolean addSignal(Signal signal) {
        return addReference(signal, Role.SIGNAL);
    }

    /**
	 * @brief returns the referenced operations
	 * @return the referenced operations
	 */
    @SuppressWarnings("unchecked")
    public Collection<Operation> getOperations() {
        return (Collection<Operation>) getReferences(Role.OPERATION);
    }

    /**
	 * @brief adds an operation
	 * @param operation the operation to be added
	 * @return true if the addition succeeded, else false
	 */
    public boolean addOperation(Operation operation) {
        return addReference(operation, Role.OPERATION);
    }

    /**
	 * @brief returns the data elements, i.e. the signals and the operations
	 * @return the data elements, i.e. the signals and the operations
	 */
    public Collection<AbstractDataElement> getDataElements() {
        Collection<AbstractDataElement> dataElements = new ArrayList<AbstractDataElement>();
        dataElements.addAll(getSignals());
        dataElements.addAll(getOperations());
        return dataElements;
    }

    /**
	 * @brief adds a data element
	 * @param dataElement the data element to be added
	 * @return true if the addition succeeded, else false
	 */
    public boolean addDataElement(AbstractDataElement dataElement) {
        if (dataElement instanceof Signal) {
            return addSignal((Signal) dataElement);
        }
        if (dataElement instanceof Operation) {
            return addOperation((Operation) dataElement);
        }
        return false;
    }

    /**
	 * @brief creates an instance of this port
	 * @return an instance of this port
	 */
    public PortInstance createPortInstance() {
        return (PortInstance) createInstance(Role.PORTINSTANCE);
    }

    /**
	 * @brief returns the list of messages on invalidities (constraints violations)
	 * 
	 * 1. the referenced signals and operations must belong to the same component as this port
	 * 2. if the port is an application model port, its direction must be IN or OUT, and it must not have a port type
	 * 3. if the port is a resource model port, it must have a port type and must not reference signals or operations
	 * 
	 * @return the list of messages on invalidities (constraints violations)
	 */
    public List<IInvalidity> getInvalidities() {
        List<IInvalidity> invalidities = super.getInvalidities();
        for (AbstractDataElement dataElement : getDataElements()) {
            if (!dataElement.getParent().equals(getParent())) {
                invalidities.add(new PortInvalidity(PortInvalidityID.PORT_DATA_NOT_DECLARED, this, dataElement));
            }
        }
        if (isApplicationModelElement()) {
            if (getDirection() == Direction.INOUT) {
                invalidities.add(new PortInvalidity(PortInvalidityID.APP_PORT_INOUT, this, null));
            }
            if (getPortType() != null) {
                invalidities.add(new PortInvalidity(PortInvalidityID.APP_PORT_WITH_PORT_TYPE, this, null));
            }
        }
        if (isResourceModelElement()) {
            if (getPortType() == null) {
                invalidities.add(new PortInvalidity(PortInvalidityID.RES_PORT_WITHOUT_PORT_TYPE, this, null));
            }
            if (!getSignals().isEmpty()) {
                invalidities.add(new PortInvalidity(PortInvalidityID.RES_PORT_WITH_SIGNALS, this, null));
            }
            if (!getOperations().isEmpty()) {
                invalidities.add(new PortInvalidity(PortInvalidityID.RES_PORT_WITH_OPERATIONS, this, null));
            }
        }
        return invalidities;
    }

    XORInterfaceBuilder interfaceBuilder;

    public boolean updateAddedElement(SubComponent alternative, IAXLangElement alternativeElement, ReferenceKind referenceKind, Role role) {
        if (interfaceBuilder == null) {
            interfaceBuilder = new XORInterfaceBuilder();
        }
        return interfaceBuilder.updateAddedElement(this, alternative, alternativeElement, referenceKind, role);
    }

    @Override
    public void elementAdded(IAXLangElement owner, ReferenceKind referenceKind, Role role, IAXLangElement addedElement) {
        if (this.getParent() != null && this.getParent() instanceof XORComponent) {
            if (owner instanceof Port && owner.getIdentifier() != null && owner.getIdentifier().equals(this.getIdentifier()) && referenceKind == ReferenceKind.REFERENCE && (role == Role.SIGNAL || role == Role.OPERATION)) {
                SubComponent alternative = null;
                for (SubComponent subcomponent : getParent().getSubComponents()) {
                    if (subcomponent.getComponentType() != null && subcomponent.getComponentType().getPorts().contains(owner)) {
                        alternative = subcomponent;
                    }
                }
                updateAddedElement(alternative, addedElement, referenceKind, role);
            }
        }
    }
}
