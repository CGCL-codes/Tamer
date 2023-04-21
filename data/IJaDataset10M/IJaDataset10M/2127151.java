package de.fraunhofer.isst.axbench.axlang.elements.applicationmodel;

import java.text.MessageFormat;
import java.util.Collection;
import de.fraunhofer.isst.axbench.axlang.api.AbstractAXLangElement;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLComparison;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLException;
import de.fraunhofer.isst.axbench.axlang.utilities.ElementWithMessage;
import de.fraunhofer.isst.axbench.axlang.utilities.ReferenceKind;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;
import de.fraunhofer.isst.axbench.axlang.utilities.ValidType;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLErrors.ValidityError;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Cardinality;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Direction;

/**
 * @brief @c Connection is the abstract super class for horizontal connections and up and down delegations
 *
 * @par Children
 * - <strong>0..*</strong> data element link
 * - <strong>1</strong> source port instance (LocalPortInstance)
 * - <strong>1</strong> target port instance (LocalPortInstance)
 *
 * @author mgrosse
 * @author ekleinod
 * @version 0.9.0
 * @since 0.1
 */
public abstract class AbstractConnection extends AbstractAXLangElement {

    /**
	 * @brief constructor
	 */
    public AbstractConnection() {
        super();
        setValidElementTypes(ReferenceKind.CHILD, new ValidType(Role.DATAELEMENTLINK, new Cardinality(0, Cardinality.INFINITY)));
    }

    /**
	 * @brief Returns the data element links.
	 * @return the collection of data element links
	 */
    @SuppressWarnings("unchecked")
    public Collection<DataElementLink> getDataElementLinks() {
        return (Collection<DataElementLink>) getChildren(Role.DATAELEMENTLINK);
    }

    /**
	 * @brief Adds a data element link.
	 * @param newDataElementLink the new data element link
	 * @throws AXLException if element could not be added
	 */
    public void addDataElementLink(DataElementLink newDataElementLink) throws AXLException {
        addChild(newDataElementLink, Role.DATAELEMENTLINK);
    }

    /**
	 * @brief Returns the referenced source port.
	 * @return referenced source port
	 */
    public abstract Port getSourcePort();

    /**
	 * @brief Returns the referenced source subcomponent.
	 * @return referenced source subcomponent
	 *  @retval null in a down delegation
	 */
    public abstract SubComponent getSourceSubComponent();

    /**
	 * @brief Returns the referenced target port.
	 * @return referenced target port
	 */
    public abstract Port getTargetPort();

    /**
	 * @brief Returns the referenced target subcomponent.
	 * @return referenced target subcomponent
	 *  @retval null in an up delegation
	 */
    public abstract SubComponent getTargetSubComponent();

    /**
	 * @brief Returns if the connection is a delegation.
	 *
	 * @return delegation?
	 *  @retval true delegation
	 *  @retval false connection
	 */
    public boolean isDelegation() {
        return (this instanceof Delegation);
    }

    /**
	 * @brief Returns if the connection is a connection, not a delegation.
	 *
	 * @return connection?
	 *  @retval true connection
	 *  @retval false delegation
	 */
    public boolean isConnection() {
        return (this instanceof Connection);
    }

    /**
	 * @brief maps a data element at the input port of the connection
	 * to the data element at the output port of the connection
	 * that is associated to the input via a data element link of the connection
	 *
	 * @param input a data element from the input port of the connection
	 * @return the data element from the output port of the connection that the input is linked to
	 * @retval null if no data element at the output port is linked to the input data element
	 */
    public DataElement getOutputDataElement(DataElement input) {
        DataElement output = null;
        for (DataElementLink link : getDataElementLinks()) {
            if (link.getSourceDataElement().equals(input)) {
                output = link.getTargetDataElement();
            }
        }
        return output;
    }

    /**
	 * @brief Define sorting order.
	 *
	 * Sorting order (smaller elements first):
	 * -# no source or target port defined
	 * -# kind (delegation before connections)
	 * -# Unterkomponente A
	 * -# Port A
	 * -# Unterkomponente B
	 * -# Port B
	 *
	 * @param axlOther other element
	 * @return result of comparison
	 *  @retval <0 this element is less than the other
	 *  @retval 0 this element equals the other
	 *  @retval >0 this element is greater than the other
	 */
    @Override
    public int compareTo(IAXLangElement axlOther) {
        if (!(axlOther instanceof AbstractConnection)) {
            return 1;
        }
        AbstractConnection connOther = (AbstractConnection) axlOther;
        if ((this.getSourcePort() == null) || (this.getTargetPort() == null)) {
            return -1;
        }
        if ((connOther.getSourcePort() == null) || (connOther.getTargetPort() == null)) {
            return 1;
        }
        if (this.isDelegation() && !connOther.isDelegation()) {
            return -1;
        }
        if (!this.isDelegation() && connOther.isDelegation()) {
            return 1;
        }
        Integer iComparison = AXLComparison.compareElements(this.getSourceSubComponent(), connOther.getSourceSubComponent());
        if ((iComparison != null) && (iComparison.intValue() != AXLComparison.EQUAL)) {
            return iComparison.intValue();
        }
        iComparison = AXLComparison.compareElements(this.getSourcePort(), connOther.getSourcePort());
        if ((iComparison != null) && (iComparison.intValue() != AXLComparison.EQUAL)) {
            return iComparison.intValue();
        }
        iComparison = AXLComparison.compareElements(this.getTargetSubComponent(), connOther.getTargetSubComponent());
        if ((iComparison != null) && (iComparison.intValue() != AXLComparison.EQUAL)) {
            return iComparison.intValue();
        }
        iComparison = AXLComparison.compareElements(this.getTargetPort(), connOther.getTargetPort());
        if ((iComparison != null) && (iComparison.intValue() != AXLComparison.EQUAL)) {
            return iComparison.intValue();
        }
        return AXLComparison.EQUAL;
    }

    /**
	 * @brief Returns if this element has to be removed?
	 *
	 * - both, source and target subcomponent null
	 *
	 * @param theKind the reference kind (references or paths)
	 * @return does this element have to be removed?
	 *  @retval true remove
	 *  @retval false do not remove
	 */
    @Override
    protected boolean hasThisToBeRemoved(ReferenceKind theKind) {
        return super.hasThisToBeRemoved(theKind);
    }

    /**
	 * @brief Computes the non-valid elements beginning from and including this element to it's children, children's children etc.
	 *
	 * An element is valid if it is valid itself and all it's children are valid as well.
	 *
	 * Special checks:
	 * - both, source and target component are empty in a delegation
	 * - source and target are switched
	 *   - correct connections
	 *     - x.out -> x.in
	 *   - correct delegations
	 *     - this.in -> x.in
	 *     - x.out -> this.out
	 *
	 * @return list of non-valid elements with their errors
	 *  @retval empty list of element is valid
	 */
    @Override
    public Collection<ElementWithMessage> getNonValidElements() {
        Collection<ElementWithMessage> cllReturn = super.getNonValidElements();
        Port axlSourcePort = getSourcePort();
        SubComponent axlSourceSubComponent = getSourceSubComponent();
        Port axlTargetPort = getTargetPort();
        SubComponent axlTargetSubComponent = getTargetSubComponent();
        if ((axlSourceSubComponent == null) && (axlTargetSubComponent == null) && isDelegation()) {
            cllReturn.add(new ElementWithMessage(this, MessageFormat.format(ValidityError.CONNECTION_DOUBLE_THIS.getMessage(), getIdentifier(), getClassName())));
        }
        if ((axlSourcePort != null) && (axlTargetPort != null)) {
            boolean isCorrect = false;
            if (isConnection()) {
                isCorrect |= ((axlSourcePort.getDirection() == Direction.OUT) && (axlTargetPort.getDirection() == Direction.IN));
            } else {
                isCorrect |= ((axlSourceSubComponent == null) && (axlSourcePort.getDirection() == Direction.IN) && (axlTargetPort.getDirection() == Direction.IN));
                isCorrect |= ((axlTargetSubComponent == null) && (axlSourcePort.getDirection() == Direction.OUT) && (axlTargetPort.getDirection() == Direction.OUT));
            }
            if (!isCorrect) {
                cllReturn.add(new ElementWithMessage(this, MessageFormat.format(ValidityError.CONNECTION_DIRECTION.getMessage(), getIdentifier(), getClassName())));
            }
        }
        return cllReturn;
    }
}
