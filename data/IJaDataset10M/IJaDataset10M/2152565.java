package com.arjuna.blue.bluefrontend.faces;

import java.util.ArrayList;
import java.util.List;

public class Macro extends BlueObject {

    private String macroValue = "";

    private boolean isModifiable = false;

    public Macro() {
    }

    public Macro(Macro macro) {
        super(macro);
        this.macroValue = macro.macroValue;
        this.isModifiable = macro.isModifiable;
    }

    public String getMacroValue() {
        return this.macroValue;
    }

    public void setMacroValue(String macroValue) {
        this.macroValue = macroValue;
    }

    public boolean getIsModifiable() {
        return this.isModifiable;
    }

    public void setIsModifiable(boolean isModifiable) {
        this.isModifiable = isModifiable;
    }

    public List<String> getObjectDetails() {
        List<String> details = new ArrayList<String>();
        details.add(String.valueOf(getId()));
        details.add(getMacroValue());
        return details;
    }
}
