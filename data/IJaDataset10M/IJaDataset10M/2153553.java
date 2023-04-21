package common.analytic;

import arit.DecimalArithmetics;
import common.point.APoint;
import common.segment.ASegment;

public interface DecimalAnalyticGeometry<T, P extends APoint<T>, S extends ASegment<T, P>> extends AnalyticIntersectionGeometry<T, P, S, T, P, S> {

    DecimalArithmetics<T> arithmetics();
}
