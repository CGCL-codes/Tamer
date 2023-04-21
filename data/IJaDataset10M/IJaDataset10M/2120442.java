package net.sourceforge.kas.cTree.cDefence;

import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.CFences;
import net.sourceforge.kas.cTree.adapter.C_Changer;

public abstract class CD_Base extends C_Changer {

    @Override
    public CElement doIt(final CD_Event message) {
        final CElement p = this.getParent();
        final CFences f = this.getFences();
        final CElement content = this.getInside();
        final boolean replace = this.replaceP(p, f);
        CElement insertion = this.createInsertion(f, content);
        insertion = this.replaceFoPDef(p, insertion, f, replace);
        return insertion;
    }

    protected CElement createInsertion(final CElement fences, final CElement content) {
        return fences;
    }

    protected boolean replaceP(final CElement parent, final CElement fences) {
        return false;
    }

    @Override
    public boolean canDo() {
        return true;
    }

    public CFences getFences() {
        return (CFences) ((CD_Event) this.getEvent()).getFences();
    }

    public CElement getInside() {
        return ((CD_Event) this.getEvent()).getInside();
    }
}
