package smueller.datastructure;

import smueller.SymbolicRepresentationAlignment;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;

/**
 * @author Soeren Mueller, smueller@cebitec.uni-bielefeld.de
 */
public class BreakPoints {

    private final double[] common;

    public BreakPoints(final Array c, final Array d) {
        final double[] a = calcbreakpoints(c, SymbolicRepresentationAlignment.getSorti().getSortedjavarray1(), SymbolicRepresentationAlignment.getAlphabetgr());
        final double[] b = calcbreakpoints(d, SymbolicRepresentationAlignment.getSorti().getSortedjavarray2(), SymbolicRepresentationAlignment.getAlphabetgr());
        this.common = new double[a.length];
        for (int i = 0; i < this.common.length; i++) {
            this.common[i] = (a[i] + b[i]) / 2;
        }
    }

    public double[] calcbreakpoints(final Array a1, final double[] sortiert, final int alphabetgr) {
        final Array a = a1.copy();
        double total = 0;
        double save = 0;
        final IndexIterator ii1 = a.getIndexIterator();
        while (ii1.hasNext()) {
            total = total + ii1.getDoubleNext();
        }
        total = (total / (alphabetgr));
        double save2 = total;
        final double[] bps = new double[alphabetgr + 1];
        int pos = 0;
        for (int j = 0; j < sortiert.length; j++) {
            if (save < save2) {
                save = save + sortiert[j];
            } else {
                save = save + sortiert[j];
                save2 = save2 + total;
                bps[pos] = sortiert[j - 1];
                pos++;
            }
        }
        if ((bps[alphabetgr - 2] == 0.0) && (bps[alphabetgr - 3] != 0.0)) {
            bps[alphabetgr - 2] = sortiert[sortiert.length - 1] - 0.01;
        }
        bps[alphabetgr] = Math.round(sortiert[0] * 100) / 100.00;
        bps[alphabetgr - 1] = Math.round(sortiert[sortiert.length - 1] * 100) / 100.00;
        return bps;
    }

    public double[] getCommon() {
        return this.common;
    }
}
