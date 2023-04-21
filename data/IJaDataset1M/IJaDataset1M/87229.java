package net.sourceforge.kas.cTree.cAlter;

import java.util.ArrayList;
import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.CFrac;
import net.sourceforge.kas.cTree.CMixedNumber;
import net.sourceforge.kas.cTree.CNum;
import net.sourceforge.kas.cTree.CRolle;
import net.sourceforge.kas.cTree.adapter.C_Event;
import net.sourceforge.kas.cTree.cDefence.CD_Event;

public class CA_Frac_InGemZahl extends CA_Base {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    private int zz;

    private int nz;

    @Override
    public CElement doIt(final CD_Event message) {
        final int gz = this.zz / this.nz;
        System.out.println("GanzZahl " + gz);
        final int newZZ = this.zz % this.nz;
        System.out.println("Neuer Zaehler " + this.nz);
        final CFrac cF = (CFrac) this.cFrac.cloneCElement(false);
        ((CNum) cF.getZaehler()).setValue(newZZ);
        final CNum cGanz = CNum.createNum(this.cFrac, "" + gz);
        final CMixedNumber cEl = CMixedNumber.createMixedNumber(cGanz, cF);
        this.cFrac.getParent().replaceChild(cEl, this.cFrac, true, true);
        return cEl;
    }

    @Override
    public String getText() {
        return "Bruch -> GemZahl";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final C_Event event = this.getEvent();
            final CElement first = event.getFirst();
            final ArrayList<CElement> sel = event.getSelection();
            if (sel.size() > 0 && first instanceof CFrac && (first.getCRolle() != CRolle.FRACTION)) {
                this.cFrac = (CFrac) first;
                this.z = this.cFrac.getZaehler();
                this.n = this.cFrac.getNenner();
                if ((this.z instanceof CNum) && (this.n instanceof CNum)) {
                    this.zz = ((CNum) this.z).getValue();
                    this.nz = ((CNum) this.n).getValue();
                    if (this.zz > this.nz) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
