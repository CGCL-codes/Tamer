package net.sf.jdmf.algorithms.clustering.centroid.impl;

import java.util.List;
import net.sf.jdmf.algorithms.clustering.centroid.AbstractOthersDistantChoiceStrategy;
import net.sf.jdmf.algorithms.clustering.centroid.InitialCentroidChoiceStrategy;
import net.sf.jdmf.data.output.clustering.Point;

/**
 * An initial centroid choice strategy that is fully predictive (useful when
 * testing algorithms).
 * 
 * The first centroid is chosen from available points using the specified index
 * of a point on the list.
 * 
 * @author quorthon
 * @see net.sf.jdmf.algorithms.clustering.centroid.AbstractOthersDistantChoiceStrategy
 */
public class FirstAsIndexFromListOthersDistantChoiceStrategy extends AbstractOthersDistantChoiceStrategy {

    private Integer firstPointIndex = 0;

    public FirstAsIndexFromListOthersDistantChoiceStrategy() {
        super();
    }

    public FirstAsIndexFromListOthersDistantChoiceStrategy(Integer firstPointIndex) {
        this();
        this.firstPointIndex = firstPointIndex;
    }

    public Integer getFirstPointIndex(List<Point> points) {
        Integer firstPointIndex = this.firstPointIndex;
        if (firstPointIndex >= points.size()) {
            firstPointIndex = 0;
        }
        return firstPointIndex;
    }

    public void setFirstPointIndex(Integer firstPointIndex) {
        this.firstPointIndex = firstPointIndex;
    }
}
