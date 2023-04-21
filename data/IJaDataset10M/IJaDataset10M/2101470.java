package net.sourceforge.logikbaukasten.button;

import net.sourceforge.logikbaukasten.schaltung.BausteinXnor;
import net.sourceforge.logikbaukasten.schaltung.Schaltelement;

public class BausteinButtonXNOR extends BausteinButton {

    private static final long serialVersionUID = -5747844952733747627L;

    public BausteinButtonXNOR() {
        super("xnor0");
        setToolTipText("XNOR");
    }

    @Override
    public Schaltelement getNewBaustein() {
        return new BausteinXnor();
    }
}
