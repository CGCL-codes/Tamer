package net.sourceforge.jds.mass.exact;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import net.sourceforge.jds.hypothesis.IDiscreteHypothesis;
import net.sourceforge.jds.mass.IDiscreteMassFunction;

/**
 * @author Thomas Reineking
 *
 */
public abstract class AbstractDiscreteMassFunction<S extends Comparable<S>, H extends IDiscreteHypothesis<S, H>, M extends AbstractDiscreteMassFunction<S, H, M>> extends AbstractMassFunction<H, M> implements IDiscreteMassFunction<S, H, M> {

    protected abstract H createHypothesis(Collection<S> singletons);

    public AbstractDiscreteMassFunction() {
        super();
    }

    public AbstractDiscreteMassFunction(H hypothesis) {
        super(hypothesis);
    }

    @Override
    public M getPignisticTransformation() {
        M pignistic = clone();
        pignistic.clear();
        for (Map.Entry<H, Double> entry : toMap().entrySet()) {
            for (S s : entry.getKey()) {
                pignistic.add(createHypothesis(Collections.singleton(s)), entry.getValue() / entry.getKey().size());
            }
        }
        return pignistic;
    }

    /**
	 * Performs a first-order Markov update where this mass function expresses the belief about the current state and the model describes the state transition belief.
	 * 
	 * @param model the model describing the state transition belief
	 * @return The updated distribution.
	 */
    public M dynamicUpdate(ITransitionModel<S, H, M> model) {
        M posterior = createMassFunction();
        for (H h : this) {
            M predicted = null;
            for (S s : h) {
                if (predicted == null) predicted = model.predict(s); else predicted = predicted.combineDisjunctive(model.predict(s));
            }
            double mh = getMass(h);
            for (H hp : predicted) posterior.add(hp, mh * predicted.getMass(hp));
        }
        return posterior;
    }

    @Override
    public H getMostPlausibleSingletons() {
        double maxPl = -1;
        H maxS = createHypothesis(new LinkedList<S>());
        for (S s : getFrameOfDiscernment()) {
            LinkedList<S> col = new LinkedList<S>();
            col.add(s);
            double pl = getPlausibility(createHypothesis(col));
            if (pl == maxPl) maxS.add(col); else if (pl > maxPl) {
                maxS = createHypothesis(col);
                maxPl = pl;
            }
        }
        return maxS;
    }
}
