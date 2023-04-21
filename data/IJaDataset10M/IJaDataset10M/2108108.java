package de.offis.semanticmm4u.compositors.variables.operators.basics.temporal;

import component_interfaces.semanticmm4u.realization.compositor.provided.IDelay;
import component_interfaces.semanticmm4u.realization.compositor.provided.IVariable;
import component_interfaces.semanticmm4u.realization.compositor.provided.IVariableList;
import component_interfaces.semanticmm4u.realization.compositor.realization.IElement;
import de.offis.semanticmm4u.compositors.variables.operators.basics.AbstractBasicOperator;
import de.offis.semanticmm4u.global.Constants;
import de.offis.semanticmm4u.global.Utilities;

/**
 * @testcase test.de.offis.semanticmm4u.compositors.basics.TestDelay 
 */
public class Delay extends AbstractBasicOperator implements IDelay {

    private int delay = Constants.UNDEFINED_INTEGER;

    public Delay(int myDelay) {
        createNewDelay(myDelay);
    }

    private void createNewDelay(int myDelay) {
        super.createNewVariable(null, null);
        this.delay = myDelay;
    }

    @Override
    public void setVariables(IVariableList myVariables) {
        throw new RuntimeException("Illegal method call: setVariables() is not allowed in class Delay.");
    }

    @Override
    public boolean addVariable(IVariable myVariable) {
        throw new RuntimeException("Illegal method call: addVariable() is not allowed in class Delay.");
    }

    public void setDelay(int myDelay) {
        this.delay = myDelay;
    }

    public int getDelay() {
        return this.delay;
    }

    @Override
    public String getID() {
        return "delay_" + Utilities.getHexHashCode(this);
    }

    /**
     * Clone the object recursive.
     * 
     * @return a copy of the Object.
     * @see de.offis.semanticmm4u.compositors.AbstractElement#recursiveClone()
     */
    public IElement recursiveClone() {
        Delay object = new Delay(this.delay);
        super.recursiveClone(object);
        return object;
    }
}
