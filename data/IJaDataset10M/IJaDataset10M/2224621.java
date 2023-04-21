package maltcms.math.functions;

import lombok.Data;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.Array;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Data
@ServiceProvider(service = IScalarArraySimilarity.class)
public class ProductSimilarity implements IScalarArraySimilarity {

    private IScalarSimilarity[] scalarSimilarities = new IScalarSimilarity[0];

    private IArraySimilarity[] arraySimilarities = new IArraySimilarity[0];

    @Override
    public double apply(double[] s1, double[] s2, Array a1, Array a2) {
        double val = 1.0d;
        for (int i = 0; i < scalarSimilarities.length; i++) {
            double v = scalarSimilarities[i].apply(s1[i], s2[i]);
            if (Double.isInfinite(v) || Double.isNaN(v)) {
                return Double.NEGATIVE_INFINITY;
            }
            val *= v;
        }
        for (int i = 0; i < arraySimilarities.length; i++) {
            val *= arraySimilarities[i].apply(a1, a2);
        }
        return val;
    }
}
