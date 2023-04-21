package jopt.csp.spi.arcalgorithm.constraint.num.global;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumRangeConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumBetweenArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumBetweenArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspGenericNumConstant;

/**
 * Z is a subset of A, or exists between min and max
 */
public class NumBetweenConstraint extends NumRangeConstraint {

    public NumBetweenConstraint(NumExpr sourceMin, boolean minExclusive, NumExpr sourceMax, boolean maxExclusive, NumExpr z) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, z, null);
    }

    public NumBetweenConstraint(Number sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, NumExpr z) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, z, null);
    }

    public NumBetweenConstraint(CspGenericNumConstant sourceMin, boolean minExclusive, CspGenericNumConstant sourceMax, boolean maxExclusive, NumExpr z) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, z, null);
    }

    protected AbstractConstraint createConstraint(NumExpr sourceMin, boolean minExclusive, Number numMinConst, GenericNumConstant gcmin, NumExpr sourceMax, boolean maxExclusive, Number numMaxConst, GenericNumConstant gcmax, NumExpr z) {
        if ((sourceMin != null) && (sourceMax != null)) {
            return new NumBetweenConstraint(sourceMin, minExclusive, sourceMax, maxExclusive, z);
        } else if ((numMinConst != null) && (numMaxConst != null)) {
            return new NumBetweenConstraint(numMinConst, minExclusive, numMaxConst, maxExclusive, z);
        } else {
            return new NumBetweenConstraint(gcmin, minExclusive, gcmax, maxExclusive, z);
        }
    }

    protected AbstractConstraint createOpposite() {
        if ((sourceExprMin != null) && (sourceExprMax != null)) {
            return new NumNotBetweenConstraint(sourceExprMin, minExclusive, sourceExprMax, maxExclusive, zexpr);
        } else if (sourceConstMin != null) {
            return new NumNotBetweenConstraint(sourceConstMin, minExclusive, sourceConstMax, maxExclusive, zexpr);
        } else {
            return new NumNotBetweenConstraint(sourceGenConstMin, minExclusive, sourceGenConstMax, maxExclusive, zexpr);
        }
    }

    /**
     * Creates generic numeric arcs
     */
    protected Arc[] createGenericArcs() {
        GenericNumNode sourceMin = (sourceExprMin != null) ? (GenericNumNode) sourceExprMin.getNode() : null;
        GenericNumNode sourceMax = (sourceExprMax != null) ? (GenericNumNode) sourceExprMax.getNode() : null;
        GenericNumNode z = (zexpr != null) ? (GenericNumNode) zexpr.getNode() : null;
        if (sourceMin != null) {
            Arc a1 = new GenericNumBetweenArc(sourceMin, minExclusive, sourceMax, maxExclusive, z, getPrecision());
            return new Arc[] { a1 };
        } else if (sourceConstMin != null) {
            Arc a1 = new GenericNumBetweenArc(sourceConstMin, minExclusive, sourceConstMax, maxExclusive, z, getPrecision());
            return new Arc[] { a1 };
        } else {
            Arc a1 = new GenericNumBetweenArc(sourceGenConstMin, minExclusive, sourceGenConstMax, maxExclusive, z, getPrecision());
            return new Arc[] { a1 };
        }
    }

    /**
     * Creates standard numeric arcs
     */
    protected Arc[] createStandardArcs() {
        NumNode sourceMin = (sourceExprMin != null) ? (NumNode) sourceExprMin.getNode() : null;
        NumNode sourceMax = (sourceExprMax != null) ? (NumNode) sourceExprMax.getNode() : null;
        NumNode z = (zexpr != null) ? (NumNode) zexpr.getNode() : null;
        if ((sourceMin == null) && (sourceMax == null)) {
            Arc a1 = new TernaryNumBetweenArc(sourceConstMin, minExclusive, sourceConstMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        } else if ((sourceMin != null) && (sourceMax == null)) {
            Arc a1 = new TernaryNumBetweenArc(sourceMin, minExclusive, sourceConstMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        } else if ((sourceMin == null) && (sourceMax != null)) {
            Arc a1 = new TernaryNumBetweenArc(sourceConstMin, minExclusive, sourceMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        } else {
            Arc a1 = new TernaryNumBetweenArc(sourceMin, minExclusive, sourceMax, maxExclusive, z, nodeType);
            return new Arc[] { a1 };
        }
    }

    public boolean violated() {
        double precision = getPrecision();
        Number runtimeMin = currentMin;
        Number runtimeMax = currentMax;
        if (runtimeMin == null) {
            if (currentGenSourceExprMin != null) {
                runtimeMin = currentGenSourceExprMin.getNumMax();
            } else if (currentSourceExprMin != null) {
                runtimeMin = currentSourceExprMin.getNumMax();
            }
        }
        if (runtimeMax == null) {
            if (currentGenSourceExprMax != null) {
                runtimeMax = currentGenSourceExprMax.getNumMin();
            } else if (currentSourceExprMax != null) {
                runtimeMax = currentSourceExprMax.getNumMin();
            }
        }
        if (NumberMath.compare(runtimeMax, runtimeMin, precision, nodeType) <= 0) {
            return false;
        }
        int cmp = NumberMath.compare(runtimeMax, currentZ.getNumMin(), precision, nodeType);
        if ((minExclusive) ? cmp <= 0 : cmp < 0) return true;
        cmp = NumberMath.compare(runtimeMin, currentZ.getNumMax(), precision, nodeType);
        if ((maxExclusive) ? cmp >= 0 : cmp > 0) return true;
        Number lowerBoundUpperHalf = currentZ.getNextHigher(runtimeMin);
        cmp = NumberMath.compare(runtimeMax, lowerBoundUpperHalf, precision, nodeType);
        if ((minExclusive) ? cmp < 0 : cmp <= 0) return true;
        return false;
    }
}
