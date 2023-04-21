package com.oat.domains.cfo.algorithms.evolution;

import java.util.Random;
import com.oat.domains.cfo.CFOProblemInterface;
import com.oat.domains.cfo.CFOSolution;

/**
 * 
 * Description: 
 *  
 * Date: 10/03/2008<br/>
 * @author Jason Brownlee 
 *
 * <br/>
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class ESSolution extends CFOSolution {

    protected final double[] stdevs;

    protected final double[] directions;

    public ESSolution(double[] aCoord) {
        super(aCoord);
        stdevs = new double[aCoord.length];
        directions = new double[aCoord.length];
    }

    /**
     * only needed for initial random pop
     * @param p
     */
    public void prepare(CFOProblemInterface p, Random r) {
        double[][] minmax = p.getMinmax();
        for (int i = 0; i < p.getDimensions(); i++) {
            stdevs[i] = (minmax[i][1] - minmax[i][0]) * r.nextDouble();
            directions[i] = (2 * Math.PI) * r.nextDouble();
        }
    }

    public double[] getDirections() {
        return directions;
    }

    public double[] getStdevs() {
        return stdevs;
    }
}
