package com.agentfactory.afapl2.core.actuator;

import com.agentfactory.afapl2.core.module.AuxiliaryMemory;
import com.agentfactory.logic.agent.Actuator;
import com.agentfactory.logic.lang.FOS;

/**
 *
 * @author  Mauro Dragone
 */
public class RememberAuxiliaryMemory extends Actuator {

    @Override
    public boolean act(FOS action) {
        AuxiliaryMemory auxMemory = (AuxiliaryMemory) getModuleByName("auxiliaryMemory");
        if (auxMemory == null) {
            adoptBelief("BELIEF(noSuchModule(auxiliaryMemory))");
            return false;
        }
        String memoryName = action.argAt(0).toString();
        auxMemory.setEnabled(memoryName, true);
        return true;
    }
}
