package org.gdbi.merge;

import org.gdbi.api.*;

/**
 * Second level of comparison using cross-reference info.
 */
public class Level2 extends ACTri.Set {

    protected static final GdbiUtilDebug udebug = MergeGUI.udebug;

    private static final int WEIGHT_SAME_REC = 10;

    private static final int WEIGHT_SAME_SPOUSE = 5;

    protected final PreviousValue level1 = new PreviousValue();

    private final ACTri.Agree same = new ACTri.Agree(WEIGHT_SAME_REC, "same-record");

    protected Level2(int weight, String name) {
        super(weight, name);
        add(level1);
        add(same);
    }

    protected boolean evaluate1(CrossRef cref, CRTable table) {
        if (table != null) table.clearVisited();
        final boolean oldVisited = cref.getVisited();
        cref.setVisited(true);
        level1.evaluate(cref);
        Boolean isSame = (cref == null) ? null : table.persist.readIsSame(cref.getMiniRec1(), cref.getMiniRec2());
        if (isSame == null) same.setUnknown(); else same.setPositive(isSame.booleanValue());
        return oldVisited;
    }

    private static class PreviousValue extends ACTri {

        private CrossRef cref = null;

        public PreviousValue() {
            this(1, "previous");
        }

        public PreviousValue(int weight, String name) {
            super(weight, name);
        }

        public void evaluate(CrossRef crefIn) {
            cref = crefIn;
        }

        public ACTValue getValues() {
            return (cref == null) ? null : cref.getACTValue();
        }
    }

    public static class CompFam extends Level2 {

        private ACTri.Agree sameHusb = new ACTri.Agree(WEIGHT_SAME_SPOUSE, "same-husb");

        private ACTri.Agree sameWife = new ACTri.Agree(WEIGHT_SAME_SPOUSE, "same-wife");

        public CompFam() {
            this(1, "level2-fam");
        }

        public CompFam(int weight, String name) {
            super(weight, name);
            add(sameHusb);
            add(sameWife);
        }

        protected void evaluateRecursive(CrossRef.CRFam crFam, CRTable table) {
            if (super.evaluate1(crFam, table)) return;
            final CrossRef.CRIndi crHusb = crFam.getHusb();
            final CrossRef.CRIndi crWife = crFam.getWife();
            sameHusb.setPositive(table.persist.readIsSame(crHusb));
            sameWife.setPositive(table.persist.readIsSame(crWife));
            udebug.debug_println("children list:");
            final CrossRef.CRIndi[] children = crFam.getChildren();
            final ACTValue[] values = new ACTValue[children.length];
            final CompIndi cindi = new CompIndi();
            for (int ic = 0; ic < children.length; ic++) {
                if (children[ic] != null) {
                    udebug.debug_println("" + ic + ": " + children[ic]);
                    cindi.evaluate1(children[ic], null);
                    values[ic] = cindi.getValuesTop();
                }
            }
            udebug.debug_println("end of fam: " + crFam);
        }
    }

    public static class CompIndi extends Level2 {

        private ACTri.Agree sameSpouse = new ACTri.Agree(WEIGHT_SAME_SPOUSE, "same-spouse");

        public CompIndi() {
            this(1, "level2-indi");
        }

        public CompIndi(int weight, String name) {
            super(weight, name);
            add(sameSpouse);
        }

        protected ACTValue evaluateValues(CrossRef.CRIndi cref, CRTable table) {
            if (cref == null) return null;
            evaluate1(cref, table);
            return getValuesTop();
        }

        protected void evaluateRecursive(CrossRef.CRIndi cref, CRTable table) {
            if (super.evaluate1(cref, table)) return;
            udebug.debug_println("FAMS list:");
            final ACTValue[] svalues = evaluateArrays(cref.getFamS());
            udebug.debug_println("FAMC list:");
            final ACTValue[] cvalues = evaluateArrays(cref.getFamC());
            udebug.debug_println("end of indi: " + cref);
        }

        private ACTValue[] evaluateArrays(CrossRef.CRFam[] fam) {
            final CompFam cfam = new CompFam();
            final ACTValue[] values = new ACTValue[fam.length];
            for (int ic = 0; ic < fam.length; ic++) {
                udebug.debug_println("" + ic + ": " + fam[ic]);
                cfam.evaluate1(fam[ic], null);
                values[ic] = cfam.getValuesTop();
            }
            return values;
        }
    }
}
