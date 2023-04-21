package org.olap4cloud.impl;

import java.util.ArrayList;
import java.util.List;
import org.olap4cloud.client.CubeDescriptor;

public class AggregationCubeDescriptor extends CubeDescriptor {

    List<CubeScanAggregate> aggregates = new ArrayList<CubeScanAggregate>();

    public AggregationCubeDescriptor() {
        super();
    }

    public List<CubeScanAggregate> getAggregates() {
        return aggregates;
    }
}
