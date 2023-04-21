package net.sourceforge.kas.cTree.cCombine.cStrich;

import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.CFences;
import net.sourceforge.kas.cTree.CPlusRow;
import net.sourceforge.kas.cTree.CPlusTerm;
import net.sourceforge.kas.cTree.cCombine.CC_Base;
import net.sourceforge.kas.cTree.cDefence.CD_Event;

public class CC_StrichFencedPlusMost extends CC_Base {

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1, final CElement cE2, CD_Event cDEvent) {
        System.out.println("Addiere geklammertesP Any");
        final CElement inCE1 = ((CPlusTerm) cE1.getFirstChild()).getValue().cloneCElement(false);
        final CElement newCE1 = CFences.condCreateFenced(inCE1, new CD_Event(false));
        final CElement xcE2 = cE2.cloneCElement(true);
        final CElement newCE2 = CFences.condCreateFenced(xcE2, new CD_Event(false));
        final CFences newChild = CFences.createFenced(CPlusRow.createRow(CPlusRow.createList(newCE1, newCE2)));
        ((CPlusRow) newChild.getInnen()).correctInternalPraefixesAndRolle();
        if (cE2.hasExtMinus()) {
            ((CPlusRow) newChild.getInnen()).getFirstChild().getNextSibling().togglePlusMinus(false);
        }
        if (cE1.hasExtMinus()) {
            ((CPlusRow) newChild.getInnen()).getFirstChild().getNextSibling().togglePlusMinus(false);
        }
        return newChild;
    }

    @Override
    public boolean canDo() {
        System.out.println("Repell fenced plus any");
        return true;
    }
}
