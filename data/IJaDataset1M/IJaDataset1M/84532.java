package net.sourceforge.purrpackage.model;

import net.sourceforge.cobertura.coveragedata.LineData;

/**
 * Parent is a {@link MethodDataPair}; has no children. Ordered by line number.
 */
public class LineDataPair extends CoverageDataPair<LineData, MethodDataPair, LineDataPair> {

    public LineDataPair(LineData data, LineData samePackageData, MethodDataPair parent) {
        super(data, samePackageData, parent);
    }

    @Override
    public Comparable<?> getOrderingObject() {
        return getData().getLineNumber();
    }

    static final String CLINIT = "<clinit>()V";

    @Override
    protected int[] fudgeClinit() {
        int[] result = { 0, 0, 0 };
        if (CLINIT.equals(parent.getName())) {
            result[0] = lineCounts.covered - lineCounts.samePackageCovered;
            result[1] = branchCounts.covered - branchCounts.samePackageCovered;
            result[2] = elementCounts.covered - elementCounts.samePackageCovered;
            lineCounts.samePackageCovered = lineCounts.covered;
            branchCounts.samePackageCovered = branchCounts.covered;
            elementCounts.samePackageCovered = elementCounts.covered;
        }
        return result;
    }
}
