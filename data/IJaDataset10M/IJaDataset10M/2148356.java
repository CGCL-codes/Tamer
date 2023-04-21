package org.gjt.universe.scheme001;

import org.gjt.universe.*;

class Module_001 extends ModuleBase {

    static ModuleID newModule(ModuleDesignID EID) {
        Module_001 newModule = new Module_001(EID);
        ModuleList.add(newModule);
        return newModule.getID();
    }

    protected Module_001(ModuleDesignID EID) {
        super(EID);
    }

    public boolean build() {
        return false;
    }

    public VectorDisplayReturn specificDisplayDebug() {
        VectorDisplayReturn returnList = new VectorDisplayReturn();
        returnList.add(new DisplayReturn("Type: 001"));
        returnList.add(new DisplayReturn("Owner: " + getOwner(), getOwner()));
        returnList.add(new DisplayReturn("Location: " + getLocation()));
        returnList.add(new DisplayReturn("Design: " + getDesign(), getDesign()));
        return returnList;
    }
}
