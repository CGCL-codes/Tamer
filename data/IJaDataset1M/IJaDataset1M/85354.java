package net.sourceforge.kas.cTree.cAlter;

import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.adapter.C_Changer;
import net.sourceforge.kas.cTree.cDefence.CD_Event;
import net.sourceforge.kas.cTree.cExtract.ExtractHandler;

public class CA_Extract extends CA_Base {

    private C_Changer extracter = null;

    @Override
    public CElement doIt(final CD_Event message) {
        return this.extracter.doIt(null);
    }

    @Override
    public String getText() {
        return "Extrahieren";
    }

    @Override
    public boolean canDo() {
        this.extracter = ExtractHandler.getInst().getChanger(this.getEvent());
        return this.extracter.canDo();
    }
}
