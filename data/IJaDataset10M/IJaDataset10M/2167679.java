package org.gdbi.merge;

import java.util.Vector;
import org.gdbi.api.*;

/**
 * Methods to recursively combine the 2 GEDCOMs.
 */
public class RecCombine implements GdbiConstants {

    public enum ForCommon {

        COMMON_ID_ONLY, COMMON_ADDITIONAL, COMMON_PREFERRED
    }

    public enum ForImport {

        IMPORT_IGNORE, IMPORT_MIN, IMPORT_RELATIVES, IMPORT_ALL
    }

    public static class Options {

        public final MergingProgress progress;

        public final CRTable table;

        public final ForCommon forCommon;

        public final ForImport forImport;

        Options(MergingProgress progressIn, CRTable tableIn, ForCommon forCommonIn, ForImport forImportIn) {
            progress = progressIn;
            table = tableIn;
            forCommon = forCommonIn;
            forImport = forImportIn;
        }
    }

    private static final GdbiUtilDebug udebug = MergeGUI.udebug;

    private final Options options;

    private final CRTable table;

    private final boolean createRecs;

    public static void combineGedcoms(Options options) throws GdbiIOException {
        final RecCombine combine = new RecCombine(options);
        combine.doit();
    }

    private RecCombine(Options optionsIn) {
        options = optionsIn;
        table = options.table;
        createRecs = (options.forImport == ForImport.IMPORT_ALL);
    }

    private void doit() throws GdbiIOException {
        table.clearMerged();
        final GdbiMiniIndi[] mindis = table.getGedcom2().getMiniIndiArray();
        options.progress.setMaximum(mindis.length - 1);
        for (int mindex = 0; mindex < mindis.length; mindex++) {
            options.progress.progressBar.setValue(mindex);
            if (options.progress.stopRequested) {
                options.progress.appendStatus("stop requested");
                return;
            }
            final CrossRef.CRIndi crIndi = table.getSameIndi(mindis[mindex], createRecs);
            if (crIndi != null) combineIndis(crIndi);
        }
        if (udebug.getDebug()) dumpGedcom(table.getGedcom1());
    }

    private void combineIndis(CrossRef.CRIndi crIndi) throws GdbiIOException {
        if (crIndi.getMerged()) return;
        udebug.dprintln("combineIndis: " + crIndi);
        final GdbiIndi indi1 = crIndi.getIndi1();
        final GdbiIndi indi2 = crIndi.getIndi2();
        if ((indi1 == null) || (indi2 == null)) return;
        options.progress.appendStatus("merging: " + crIndi);
        final boolean onlyID = (options.forCommon == ForCommon.COMMON_ID_ONLY);
        final GdbiContext[] facts1 = getFacts(indi1, onlyID);
        final GdbiContext[] facts2 = getFacts(indi2, onlyID);
        for (int i2 = 0; i2 < facts2.length; i2++) {
            boolean found = false;
            for (int i1 = 0; ((i1 < facts1.length) && (!found)); i1++) {
                if (hasSameFacts(facts1[i1], facts2[i2])) found = true;
            }
            if (!found) {
                facts2[i2].debug_dump("adding fact");
                final GdbiContext f1 = indi1.addLine(facts2[i2]);
                f1.copyRecursively(facts2[i2]);
            }
        }
        crIndi.setMerged(true);
        checkFamilies(indi1, indi2);
        if (indi1.getModified()) {
            indi1.debug_dump("after combining...");
        }
        indi1.getDatabase().saveModified();
    }

    /**
     * Find all families for indi2 and add indi1 to the equivalent fams.
     */
    private void checkFamilies(GdbiIndi indi1, GdbiIndi indi2) throws GdbiIOException {
        final GdbiMiniFam[] fc = indi2.getMiniFamCs();
        udebug.dprintln("checking " + fc.length + " FAMCs for " + indi2);
        for (int ic = 0; ic < fc.length; ic++) {
            final CrossRef.CRFam crFam = table.getSameFam(fc[ic], createRecs);
            if (crFam != null) {
                final GdbiFam fam1 = crFam.getFam1();
                boolean found = false;
                final GdbiIndi[] children = fam1.getChildren();
                for (int ci = 0; ci < children.length; ci++) {
                    if ((children[ci] != null) && children[ci].equals(indi1)) {
                        found = true;
                        udebug.dprintln("found: " + children[ci] + "," + indi1);
                    }
                }
                if (!found) {
                    indi1.addFamC(fam1);
                }
                copyFam(crFam);
            }
        }
        final GdbiMiniFam[] fs = indi2.getMiniFamSs();
        udebug.dprintln("checking " + fs.length + " FAMSs for " + indi2);
        for (int is = 0; is < fs.length; is++) {
            final CrossRef.CRFam crFam = table.getSameFam(fs[is], createRecs);
            if (crFam != null) {
                final GdbiFam fam1 = crFam.getFam1();
                if (!(indi1.equals(fam1.getHusband()) || indi1.equals(fam1.getWife()))) {
                    indi1.addFamS(fam1);
                    fam1.debug_dump("added HUSB/WIFE:");
                }
                copyFam(crFam);
            }
        }
        indi1.debug_dump("added INDI from gedcom2:");
    }

    /**
     * Recursively create fam1 from fam2.
     */
    private void copyFam(CrossRef.CRFam crFam) throws GdbiIOException {
        if (crFam.getMerged()) return;
        crFam.setMerged(true);
        final GdbiFam fam1 = crFam.getFam1();
        final GdbiFam fam2 = crFam.getFam2();
        if ((fam1 == null) || (fam2 == null)) return;
        final GdbiMiniIndi mhusb2 = fam2.getMiniHusb();
        final CrossRef.CRIndi crHusb = table.getSameIndi(mhusb2, createRecs);
        if (crHusb != null) {
            final GdbiIndi husb1 = crHusb.getIndi1();
            if (!husb1.equals(fam1.getHusband())) {
                husb1.addFamS(fam1);
            }
            combineIndis(crHusb);
        }
        final GdbiMiniIndi mwife2 = fam2.getMiniWife();
        final CrossRef.CRIndi crWife = table.getSameIndi(mwife2, createRecs);
        if (crWife != null) {
            final GdbiIndi wife1 = crWife.getIndi1();
            if (!wife1.equals(fam1.getWife())) {
                wife1.addFamS(fam1);
            }
            combineIndis(crWife);
        }
        final GdbiMiniIndi[] mchildren2 = fam2.getMiniChils();
        for (int i2 = 0; i2 < mchildren2.length; i2++) {
            final CrossRef.CRIndi crChild = table.getSameIndi(mchildren2[i2], createRecs);
            if (crChild != null) {
                final GdbiMiniIndi mindi1 = crChild.getMiniIndi1();
                boolean found = false;
                final GdbiMiniIndi[] mchildren1 = fam1.getMiniChils();
                for (int i1 = 0; i1 < mchildren1.length; i1++) {
                    if ((mchildren1[i1] != null) && mchildren1[i1].equals(mindi1)) found = true;
                }
                if (!found) {
                    mindi1.getIndi().addFamC(fam1);
                }
                combineIndis(crChild);
            }
        }
        if (fam1.getModified()) {
            fam1.debug_dump("after combining...");
            fam1.toDatabase();
        }
    }

    private static void dumpGedcom(final GdbiDatabase gedcom) {
        System.out.println("\n" + "dumpGedcom: " + gedcom + "\n");
        final int max_recs = 10;
        final GdbiRecType[] types = { GdbiRecType.INDI, GdbiRecType.FAM };
        for (int tnum = 0; tnum < types.length; tnum++) {
            final GdbiRecType type = types[tnum];
            final GdbiXref[] xrefs = gedcom.getXrefArray(type);
            System.out.println("list of " + type + "s, length=" + xrefs.length);
            for (int xnum = 0; xnum < xrefs.length; xnum++) {
                final GdbiRecord rec = gedcom.getRecord(xrefs[xnum]);
                if ((xnum < max_recs) && (rec != null)) rec.dump(type + " #" + xnum + ": " + xrefs[xnum]);
                if (xnum == max_recs) System.out.println("-- TRUNCATED --");
            }
            System.out.println("");
        }
    }

    private static GdbiContext[] getFacts(GdbiContext owner, boolean onlyID) {
        udebug.dprintln("getFacts: owner=" + owner);
        final Vector<GdbiContext> vector = new Vector<GdbiContext>();
        final GdbiContext[] props = owner.getContexts();
        for (int index = 0; index < props.length; index++) {
            final GdbiContext context = props[index];
            final String tag = context.getTag();
            if (!((context instanceof GdbiLink) || tag.equals(TAG_SOUR) || tag.equals(TAG__GDBI))) {
                if ((!onlyID) || tag.equals(TAG_REFN) || tag.equals(TAG_RFN) || tag.equals(TAG__UID)) {
                    udebug.dprintln("getFacts: use " + tag);
                    vector.add(context);
                }
            }
        }
        return vector.toArray(GdbiContext.TOARRAY);
    }

    /**
     * Returns true if all facts in context2 are contained in context1.
     */
    private static boolean hasSameFacts(GdbiContext context1, GdbiContext context2) {
        if ((!context1.getTag().equals(context2.getTag())) || (!context1.getValue().equals(context2.getValue()))) return false;
        final GdbiContext[] facts1 = getFacts(context1, false);
        final GdbiContext[] facts2 = getFacts(context2, false);
        for (int i2 = 0; i2 < facts2.length; i2++) {
            for (int i1 = 0; i1 < facts1.length; i1++) {
                if (!hasSameFacts(facts1[i1], facts2[i2])) return false;
            }
        }
        return true;
    }
}
