package org.ximtec.igesture.algorithm.feature;

import java.util.List;
import org.sigtec.ink.Note;
import org.sigtec.ink.Trace;

/**
 * Feature representing the proportion of the stroke lengths (first/last point)
 * to each other.
 * 
 * @version 1.0 Dec 2006
 * @author Ueli Kurmann, igesture@uelikurmann.ch
 * @author Beat Signer, signer@inf.ethz.ch
 */
public class F24 implements Feature {

    private static final int MINIMAL_NUMBER_OF_POINTS = 2;

    public double compute(Note note) throws FeatureException {
        if (note.getPoints().size() < MINIMAL_NUMBER_OF_POINTS) {
            throw new FeatureException(FeatureException.NOT_ENOUGH_POINTS);
        }
        double proportion = 0;
        final List<Trace> traces = note.getTraces();
        if (traces.size() > 1) {
            for (final Trace trace : traces) {
                final double divisor = trace.getStartPoint().distance(trace.getEndPoint());
                if (proportion == 0) {
                    proportion = divisor;
                } else if (divisor != 0) {
                    proportion /= divisor;
                }
            }
        } else {
            proportion = 1;
        }
        return proportion;
    }

    public int getMinimalNumberOfPoints() {
        return MINIMAL_NUMBER_OF_POINTS;
    }
}
