package edu.arizona.cs.learn.timeseries.classification;

import edu.arizona.cs.learn.timeseries.model.Instance;

public class Distance {

    public Instance instance;

    public double d;

    public Distance(Instance instance, double d) {
        this.instance = instance;
        this.d = d;
    }
}
