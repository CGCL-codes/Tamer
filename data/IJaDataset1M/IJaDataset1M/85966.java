package jenes.gp.operator;

import jenes.algorithms.SimpleGP;
import jenes.gp.GPChromosome;
import jenes.gp.GPDomain;
import jenes.gp.GPNode;
import jenes.gp.GPPrototype;
import jenes.population.Individual;

/**
 *
 * @author Marco
 */
public class GPSimpleBidCrossover<T extends GPChromosome> extends GPBidCrossover<T> {

    public GPSimpleBidCrossover(final double probability) {
        super(probability, DEFAULT_TRIALS, SimpleGP.MAXIMUM_CROSSOVER_DEPTH);
    }

    public GPSimpleBidCrossover(final double probability, final int trials) {
        super(probability, trials, SimpleGP.MAXIMUM_CROSSOVER_DEPTH);
    }

    public GPSimpleBidCrossover(final double probability, final int trials, final int maxDepth) {
        super(probability, trials, maxDepth);
    }

    @Override
    protected boolean tryToCross(Individual<T>... offsprings) {
        GPChromosome c0 = offsprings[0].getChromosome();
        GPChromosome c1 = offsprings[1].getChromosome();
        GPNode n0 = c0.getRandomNode();
        GPNode n1 = c1.getRandomNode();
        GPNode f0 = n0.getParent();
        GPNode f1 = n1.getParent();
        if (f0 == null && f1 == null) {
            return false;
        }
        GPPrototype p0 = n0.getPrototype();
        GPPrototype p1 = n1.getPrototype();
        int s0 = n0.getParentSlot();
        int s1 = n1.getParentSlot();
        GPDomain d0 = f0 != null ? f0.getScheme()[s0] : c0.getDomain();
        GPDomain d1 = f1 != null ? f1.getScheme()[s1] : c1.getDomain();
        if (d0.equals(d1) || (d0.contains(p1) && d1.contains(p0))) {
            if (n0.getDepth() + n1.getHeight() + 1 <= this.maxDepth && n1.getDepth() + n0.getHeight() + 1 <= this.maxDepth) {
                if (f0 == null) {
                    c0.setRoot(n1);
                } else {
                    f0.setArg(s0, n1);
                }
                if (f1 == null) {
                    c1.setRoot(n0);
                } else {
                    f1.setArg(s1, n0);
                }
                return true;
            }
        }
        return false;
    }
}
