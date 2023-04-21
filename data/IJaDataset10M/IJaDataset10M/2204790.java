package de.offis.semanticmm4u.compositors.variables.operators.basics.interaction;

import component_interfaces.semanticmm4u.realization.compositor.realization.IInteractionOperator;
import de.offis.semanticmm4u.compositors.variables.operators.basics.AbstractBasicOperator;

public abstract class Interaction extends AbstractBasicOperator implements IInteractionOperator {

    /** this gets called from the EventBroker
     * FIXME: Muss hier noch raus, da nicht von allen Unterklassen ben�tigt! 
     **/
    public void sendEvent(String type, String value) {
    }
}
