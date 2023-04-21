package de.axbench.axlang.datamodel.globalinstances;

import de.axbench.axlang.datamodel.abstractelements.IAXLangElement;
import de.axbench.axlang.datamodel.architecturemodels.AbstractDataElement;
import de.axbench.axlang.datamodel.architecturemodels.Operation;
import de.axbench.axlang.datamodel.architecturemodels.Signal;
import de.axbench.axlang.datamodel.attributes.AXLangAttributeID;

/**
 * @brief A SignalInstance is a global instance of a Signal.
 * @author mgrosse
 * @version 0.11.0
 * @since 0.11.0
 */
public class DataElementInstance extends AbstractGlobalInstance {

    /**
	 * @brief constructor
	 * 
	 * attribute 
	 * - data type
	 * 
	 * references
	 * - signal
	 */
    public DataElementInstance() {
        super();
        declareAttribute(AXLangAttributeID.DATATYPE);
    }

    @Override
    public ComponentInstance getParent() {
        return (ComponentInstance) super.getParent();
    }

    /**
	 * @brief returns the instantiated data element
	 * @return the instantiated data element
	 */
    public AbstractDataElement getDataElement() {
        if (this instanceof SignalInstance) {
            return ((SignalInstance) this).getSignal();
        }
        if (this instanceof OperationInstance) {
            return ((OperationInstance) this).getOperation();
        }
        return null;
    }

    /**
	 * @brief sets the instantiated data element
	 * @param instantiatedDataElement the instantiated data element
	 * @return true if the element could be set, else false
	 */
    public boolean setDataElement(AbstractDataElement instantiatedDataElement) {
        if (this instanceof SignalInstance && instantiatedDataElement instanceof Signal) {
            return ((SignalInstance) this).setSignal((Signal) instantiatedDataElement);
        }
        if (this instanceof OperationInstance && instantiatedDataElement instanceof Operation) {
            return ((OperationInstance) this).setOperation((Operation) instantiatedDataElement);
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IAXLangElement> T getInstantiatedElement() {
        return (T) getDataElement();
    }

    @Override
    public boolean setInstantiatedElement(IAXLangElement instantiatedElement) {
        if (!(instantiatedElement instanceof AbstractDataElement)) {
            return false;
        }
        return setDataElement((AbstractDataElement) instantiatedElement);
    }
}
