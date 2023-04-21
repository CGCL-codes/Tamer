package desmoj.core.dist;

import desmoj.core.simulator.Model;

public abstract class DiscreteDist<N extends Number> extends NumericalDist<N> {

    /**
     * Constructs a distribution returning discretely distributed samples
     * of a custom type. Note that the method <code>N sample()</code> returning the samples 
     * (inherited from <code>NumericalDist<N></code>) has to be
     * implemented in subclasses.
     * 
     * @param owner
     *            Model : The distribution's owner
     * @param name
     *            java.lang.String : The distribution's name
     * @param showInReport
     *            boolean : Flag to show distribution in report
     * @param showInTrace
     *            boolean : Flag to show distribution in trace
     */
    public DiscreteDist(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }
}
