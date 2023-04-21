package net.sourceforge.kas.cTree.cAlter;

import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.adapter.C_Changer;
import net.sourceforge.kas.cTree.adapter.C_Event;
import net.sourceforge.kas.cTree.adapter.C_No;
import net.sourceforge.kas.cTree.cDefence.CD_Event;
import net.sourceforge.kas.cTree.cDefence.DefHandler;

public class CA_Entklammern extends CA_Base {

    private C_Changer defencer;

    @Override
    public C_Changer getChanger(final C_Event e) {
        if (e instanceof CD_Event) {
            this.setEvent(e);
            this.defencer = DefHandler.getInst().getChanger(e);
        }
        this.defencer = new C_No(e);
        return this.defencer;
    }

    @Override
    public CElement doIt(final CD_Event message) {
        return this.defencer.doIt(null);
    }

    @Override
    public String getText() {
        return "Klammern entfernen";
    }

    @Override
    public boolean canDo() {
        return this.defencer.canDo();
    }
}
