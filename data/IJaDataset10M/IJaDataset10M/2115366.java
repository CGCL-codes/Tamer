package com.oat.domains.cfo.algorithms;

import java.util.Random;
import com.oat.Problem;
import com.oat.algorithms.GenericRandomSearchAlgorithm;
import com.oat.domains.cfo.CFOProblemInterface;
import com.oat.domains.cfo.CFOSolution;
import com.oat.utils.RandomUtils;

/**
 * Type: RandomSearch<br/>
 * Date: 10/03/2006<br/>
 * <br/>
 * Description: 
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 22/12/2006   JBrownlee   Random moved to method variable rather than instance variable
 * 07/09/2007	JBrownlee	Updated to use a common ancestor
 * 
 * </pre>
 */
public class RandomSearch extends GenericRandomSearchAlgorithm<CFOSolution> {

    @Override
    protected CFOSolution generateRandomSolution(Random rand, Problem problem) {
        return new CFOSolution(RandomUtils.randomPointInRange(rand, ((CFOProblemInterface) problem).getMinmax()));
    }
}
