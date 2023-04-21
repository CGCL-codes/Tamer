package netkit.classifiers.aggregators;

import netkit.graph.*;
import netkit.util.HistogramDiscrete;
import netkit.util.VectorMath;
import netkit.classifiers.Estimate;

/**
 * The Mode aggregator counts the number of times each possible value of a given
 * attribute is observed in the neighborhood of a node in the graph and returns
 * the value that is observed the most.  If there is a tie then the value is
 * returned through a stochastic process.
 *
 * @author Sofus A. Macskassy (sofmac@gmail.com)
 */
public final class Mode extends AggregatorImp {

    public Mode(String edgeType, Attribute attribute) {
        super("mode", edgeType, attribute, attribute.getType());
        if (!((attribute instanceof AttributeCategorical) || (attribute instanceof AttributeDiscrete))) throw new IllegalArgumentException("[" + getName() + "] Unknown attribute: " + attribute);
    }

    public double getValue(Node n, Estimate prior) {
        double value = 0;
        SharedNodeInfo info = getNodeInfo(n);
        switch(attribute.getType()) {
            case CATEGORICAL:
                double[] counts = info.countNeighbors(n, prior);
                value = VectorMath.getMaxIdx(counts);
                break;
            case DISCRETE:
                HistogramDiscrete hist = info.getHistogram(n);
                value = ((hist == null) ? Double.NaN : hist.getMode());
                break;
        }
        return value;
    }
}
