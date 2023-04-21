package org.vikamine.kernel.data.discretization;

import java.util.ArrayList;
import java.util.List;
import org.vikamine.kernel.data.DataView;
import org.vikamine.kernel.data.NumericAttribute;

/**
 * {@link EqualWidthDiscretizer} implements equal width discretization, i.e.,
 * k segments with an equal width on the value range (min, max) are created.
 * @author lemmerich
 * @date 04/2009
 */
public class EqualWidthDiscretizer extends AbstractDiscretizationMethod {

    private static final String NAME = "Equal Width Discretizer";

    public EqualWidthDiscretizer() {
        super();
    }

    public EqualWidthDiscretizer(DataView population, NumericAttribute na, int segmentsCount) {
        this.population = population;
        this.attribute = na;
        this.segmentsCount = segmentsCount;
    }

    @Override
    public List<Double> getCutpoints() {
        if ((population == null) || (attribute == null) || (population.dataset().getIndex(attribute) < 0) || (segmentsCount < 2) || (population.size() < 2)) {
            return new ArrayList<Double>();
        }
        startTime();
        List<Double> cutpoints = new ArrayList<Double>(segmentsCount + 1);
        double[] attributesMinAndMaxValue = DiscretizationUtils.getMinMaxValue(population, attribute);
        double segmentWidth = (attributesMinAndMaxValue[1] - attributesMinAndMaxValue[0]) / segmentsCount;
        for (int i = 0; i <= segmentsCount; i++) {
            cutpoints.add(attributesMinAndMaxValue[0] + segmentWidth * i);
        }
        stopTime();
        return cutpoints;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Double> getSortedSample() {
        return null;
    }
}
