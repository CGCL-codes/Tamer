package org.olap4cloud.impl.aggr;

import org.apache.log4j.Logger;
import org.olap4cloud.client.CubeDescriptor;
import org.olap4cloud.client.OLAPEngineException;
import org.olap4cloud.impl.CubeScanAggregate;

public class MinCubeScanAggregate extends CubeScanAggregate {

    static Logger logger = Logger.getLogger(MinCubeScanAggregate.class);

    double value;

    public MinCubeScanAggregate(String s, CubeDescriptor cubeDescriptor) throws OLAPEngineException {
        super(s, cubeDescriptor);
    }

    private void collect(double v) {
        if (v < value) value = v;
    }

    @Override
    public double getResult() {
        return value;
    }

    @Override
    public void reset() {
        value = Double.POSITIVE_INFINITY;
    }

    @Override
    public void combine(double v) {
        collect(v);
    }

    @Override
    public void reduce(double v) {
        collect(v);
    }
}
