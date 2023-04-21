package org.yagnus.opt.binpacking;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * First Fit fits. if the data binSize is not non-negative less than bin size
 * the algorithm will return null. 2-opt.
 * 
 * @author Alexia
 * 
 */
public class FirstFit extends BaseAddWhenNoBinFitsPacker<ArrayList<IBin>> {

    public boolean init(double binSize, List<Double> objects) {
        this.bins = new ArrayList<IBin>();
        return super.init(binSize, objects);
    }

    public IBin getNextFittingBin(double size) {
        for (int i = 0; i < bins.size(); ++i) {
            if (bins.get(i).getCapacityRemaining() >= size) return bins.get(i);
        }
        return null;
    }

    public double getOptimality() {
        return 2;
    }
}
