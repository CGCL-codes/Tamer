package org.yagnus.stats.samplers.discrete.withreplacement;

import java.util.List;

/**
 * 
 * @author hc.busy
 * 
 *         This class will use a heuristic and look through a list of numbers trying to find the one
 *         sample to return each time.
 */
public class ListSampler<BASETYPE> extends WithReplacementSampler<BASETYPE> {

    protected org.yagnus.stats.sampler.discrete.ListArraySampler<BASETYPE> internal;

    @Override
    public BASETYPE draw() {
        return internal.sample_wr();
    }

    @Override
    protected void _init(List<BASETYPE> t, List<Double> weights) {
        internal = new org.yagnus.stats.sampler.discrete.ListArraySampler<BASETYPE>(t, weights);
        internal.setReplacement(true);
    }

    public ListSampler(List<BASETYPE> t, List<Double> weights) {
        super(t, weights);
    }

    public ListSampler(List<BASETYPE> t) {
        super(t);
    }

    @Override
    public void addSample(BASETYPE t, double w) {
        internal.addSample_wr(t, w);
    }

    @Override
    public void removeSample(BASETYPE t) {
        internal.removeSample(t);
    }
}
