package net.sourceforge.kas.cTree.cExtract;

import java.util.ArrayList;
import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.CFences;
import net.sourceforge.kas.cTree.CPlusRow;
import net.sourceforge.kas.cTree.CRow;
import net.sourceforge.kas.cTree.CTimesRow;
import net.sourceforge.kas.cTree.adapter.C_Event;

public class CE_2StrichPunkt1 extends CExtractBase {

    @Override
    public boolean canDo() {
        final C_Event e = this.getEvent();
        if (e == null || !(e instanceof C_Event)) {
            return false;
        }
        this.setEvent(e);
        final ArrayList<CElement> selection = this.getEvent().getSelection();
        for (final CElement cEl : selection) {
            if (cEl.getFirstChild() == null || cEl.getFirstChild().getNextSibling() == null || cEl.getFirstChild().getNextSibling().hasExtDiv()) {
                return false;
            }
        }
        final String vorlage = this.getEvent().getFirst().getFirstChild().getText();
        for (final CElement cEl : selection) {
            if (!vorlage.equals(cEl.getFirstChild().getText())) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected CElement createExtraction() {
        final ArrayList<CElement> selection = this.getEvent().getSelection();
        for (final CElement cEl : selection) {
            cEl.removeCLastProperty();
        }
        selection.get(0).removeCActiveProperty();
        final CElement defElement = this.getEvent().getFirstFirst().cloneCElement(false);
        CTimesRow newChild = null;
        final ArrayList<CElement> foldedList = CTimesRow.fold(CTimesRow.castList(CRow.cloneList(selection)));
        final CPlusRow reducedSum = CPlusRow.createRow(foldedList);
        reducedSum.correctInternalPraefixesAndRolle();
        final CElement fencedSum = CFences.createFenced(reducedSum);
        fencedSum.setPraefix("*");
        final ArrayList<CElement> factors = CTimesRow.createList(defElement, fencedSum);
        newChild = CTimesRow.createRow(factors);
        newChild.correctInternalCRolles();
        return newChild;
    }
}
