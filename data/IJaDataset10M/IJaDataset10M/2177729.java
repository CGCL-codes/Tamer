package net.sourceforge.kas.cTree.cCombine.cPunkt;

import java.util.ArrayList;
import net.sourceforge.kas.cTree.CElement;
import net.sourceforge.kas.cTree.CFences;
import net.sourceforge.kas.cTree.CPlusRow;
import net.sourceforge.kas.cTree.CTimesRow;
import net.sourceforge.kas.cTree.cCombine.CC_Base;
import net.sourceforge.kas.cTree.cDefence.CD_Event;

public class CC_PunktNumFencedSum extends CC_Base {

    @Override
    protected CElement createComb(final CElement oldSumme, final CElement cE1, final CElement cE2, final CD_Event cDEvent) {
        System.out.println("Multipliziere Num mit Klammer, die Summe enth�lt");
        final ArrayList<CElement> oldAddendList = ((CPlusRow) cE2.getFirstChild()).getMemberList();
        cE1.removeCActiveProperty();
        final ArrayList<CElement> newAddendList = CTimesRow.map(cE1, oldAddendList);
        final CPlusRow cPR = CPlusRow.createRow(newAddendList);
        cPR.correctInternalPraefixesAndRolle();
        return CFences.condCreateFenced(cPR, cDEvent);
    }

    @Override
    public boolean canDo() {
        return !this.getSec().hasExtDiv();
    }
}
