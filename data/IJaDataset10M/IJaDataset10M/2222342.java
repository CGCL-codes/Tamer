package foucault.filter;

import org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction;
import foucault.model.Parameters;
import foucault.model.Picture;
import foucault.utils.SplineInterpolator1D;
import foucault.utils.Utils;
import foucault.utils.ZeroFinder;

public class SequenceZoneInterpolation {

    public void transform(final Parameters p) {
        try {
            System.out.println("SequenceZoneInterpolation");
            int size = (int) p.sequence.circle.r;
            PolynomialSplineFunction[] zoneInterpolationFunctions = new PolynomialSplineFunction[size];
            Double[] zoneInterpolations = new Double[size];
            for (int i = 0; i < zoneInterpolationFunctions.length; i++) {
                SplineInterpolator1D interpolator = new SplineInterpolator1D();
                double x = p.project.radius * i / size;
                for (Picture picture : p.sequence.pictures) {
                    double y = picture.offset;
                    double z = picture.zoneFunction.value(x) * (picture.brightnessData.maxOriginal - picture.brightnessData.minOriginal);
                    interpolator.add(y, z);
                }
                PolynomialSplineFunction f = interpolator.interpolateExact();
                zoneInterpolationFunctions[i] = f;
                zoneInterpolations[i] = new ZeroFinder().getUniqueZeroOrNull(f, f.getKnots()[0], f.getKnots()[f.getN()], 1.0);
            }
            p.sequence.zoneInterpolations = zoneInterpolations;
        } catch (Exception e) {
            throw Utils.wrap(e);
        }
    }
}
