package de.fraunhofer.isst.axbench.axlang.elements.transactionmodel;

import de.fraunhofer.isst.axbench.axlang.api.AbstractAXLangElement;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.FunctionInstance;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.HWBusInstance;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.SubComponentInstance;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLException;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLangAttribute;
import de.fraunhofer.isst.axbench.axlang.utilities.ReferenceKind;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;
import de.fraunhofer.isst.axbench.axlang.utilities.ValidType;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Attributes;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Cardinality;

/** 
 * @brief @c An ActivityAttribute contains the values of an activity that are relevant for the transaction model
 * 
 * @par References
 * - <strong>0..1</strong> global function instance (FunctionInstance)
 * - <strong>0..1</strong> global hw_component instance (HWComponentInstance)
 * - <strong>0..1</strong> global hw_bus instance (HWBusInstance)
 * exactly one of the instances must be present
 * 
 * @par Attributes
 * - <strong>0..1</strong> priority
 * - <strong>0..1</strong> wcet
 * - <strong>0..1</strong> wcet_unit
 * - <strong>0..1</strong> jitter
 * - <strong>0..1</strong> jitter_unit
 * - <strong>0..1</strong> deadline
 * - <strong>0..1</strong> deadline_unit
 * 
 * @author mgrosse
 * @version 0.10.0
 * @since 0.7.2
 */
public class ActivityAttribute extends AbstractAXLangElement {

    /**
	 * @brief Constructor.
	 */
    public ActivityAttribute() {
        super();
        setValidElementTypes(ReferenceKind.REFERENCE, new ValidType(Role.GLOBALFUNCTIONINSTANCE, new Cardinality(0, 1)), new ValidType(Role.GLOBALSUBCOMPONENTINSTANCE, new Cardinality(0, 1)), new ValidType(Role.GLOBALHWBUSINSTANCE, new Cardinality(0, 1)));
        addAttribute(new AXLangAttribute(Attributes.PRIORITY.getID()));
        addAttribute(new AXLangAttribute(Attributes.WCET.getID()));
        addAttribute(new AXLangAttribute(Attributes.WCETUNIT.getID()));
        addAttribute(new AXLangAttribute(Attributes.JITTER.getID()));
        addAttribute(new AXLangAttribute(Attributes.JITTERUNIT.getID()));
        addAttribute(new AXLangAttribute(Attributes.DEADLINE.getID()));
        addAttribute(new AXLangAttribute(Attributes.DEADLINEUNIT.getID()));
    }

    /**
	 * @brief returns the global function instance
	 * @return the global function instance if it exists, else null
	 */
    public FunctionInstance getGlobalFunctionInstance() {
        return (FunctionInstance) getReference(Role.GLOBALFUNCTIONINSTANCE);
    }

    /**
	 * @brief sets a new global function instance
	 * @param newGlobalServiceInstance the new global function instance
	 * @throws AXLException if the new global function instance cannot be set
	 */
    public void setGlobalFunctionInstance(FunctionInstance newGlobalFunctionInstance) throws AXLException {
        addReference(newGlobalFunctionInstance, Role.GLOBALFUNCTIONINSTANCE);
    }

    /**
	 * @brief returns the global hwsubcomponent instance
	 * @return the global hwsubcomponent instance if it exists, else null
	 */
    public SubComponentInstance getGlobalHWSubComponentInstance() {
        return (SubComponentInstance) getReference(Role.GLOBALSUBCOMPONENTINSTANCE);
    }

    /**
	 * @brief sets a new global hwsubcomponent instance
	 * @param newGlobalHWSubComponentInstance the new global hwsubcomponent instance
	 * @throws AXLException if the new global hwsubcomponent instance cannot be set
	 */
    public void setGlobalHWSubComponentInstance(SubComponentInstance newGlobalHWSubComponentInstance) throws AXLException {
        addReference(newGlobalHWSubComponentInstance, Role.GLOBALSUBCOMPONENTINSTANCE);
    }

    /**
	 * @brief returns the global hwbus instance
	 * @return the global hwbus instance if it exists, else null
	 */
    public HWBusInstance getGlobalHWBusInstance() {
        return (HWBusInstance) getReference(Role.GLOBALHWBUSINSTANCE);
    }

    /**
	 * @brief sets a new global hwbus instance
	 * @param newGlobalHWComponentInstance the new global hwbus instance
	 * @throws AXLException if the new global hwbus instance cannot be set
	 */
    public void setGlobalHWBusInstance(HWBusInstance newGlobalHWBusInstance) throws AXLException {
        addReference(newGlobalHWBusInstance, Role.GLOBALHWBUSINSTANCE);
    }
}
