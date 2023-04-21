package jenes.gp.operator;

import jenes.gp.GPChromosome;
import jenes.gp.GPNode;
import jenes.population.Individual;
import jenes.stage.operator.Mutator;

/**
 *
 * @author Marco
 */
public class GPConstrainedSimpleMutator<T extends GPChromosome> extends Mutator<T> {

    private Constraint[] constraints;

    public GPConstrainedSimpleMutator(Constraint[] constraints, final double probability) {
        super(probability);
        this.constraints = constraints;
    }

    @Override
    protected void mutate(Individual<T> t) {
        GPChromosome c0 = t.getChromosome();
        GPNode n0 = c0.getRandomNode();
        Object oldVariant = n0.getVariant();
        n0.setRandomVariant();
        boolean restore = false;
        for (Constraint c : constraints) {
            if (!c.verify(t)) {
                restore = true;
            }
        }
        if (restore) {
            n0.setVariant(oldVariant);
        }
    }
}
