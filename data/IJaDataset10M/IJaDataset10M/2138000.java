package co.edu.unal.ungrid.registration.evolutionary.genetic;

import java.io.Serializable;
import org.jdom.Element;
import co.edu.unal.ungrid.registration.evolutionary.operator.GeneticMutationOperator;
import co.edu.unal.ungrid.registration.evolutionary.operator.GeneticMutationOperatorFactory;
import co.edu.unal.ungrid.registration.evolutionary.operator.OneOffspringConvexRealEncodingCrossover;
import co.edu.unal.ungrid.registration.evolutionary.operator.OneOffspringCrossoverOperator;
import co.edu.unal.ungrid.registration.evolutionary.operator.OneOffsringCrossoverOperatorFactory;
import co.edu.unal.ungrid.registration.evolutionary.operator.SingleParameterRealEncodingMutator;
import co.edu.unal.ungrid.registration.evolutionary.population.Individual;
import co.edu.unal.ungrid.transformation.Transform;
import co.edu.unal.ungrid.validation.Verifiable;
import co.edu.unal.ungrid.xml.XmlConfigurable;
import co.edu.unal.ungrid.xml.XmlUtil;

public class OneOffsringGeneticSelection implements Serializable, XmlConfigurable, Verifiable {

    public static final long serialVersionUID = 20050103000070L;

    public boolean isValid() {
        return (m_crossover != null && m_mutator != null);
    }

    public void config(Element root) {
        OneOffspringCrossoverOperator cop = OneOffsringCrossoverOperatorFactory.getInstance(XmlUtil.getParameter(root, "crossover-operator-class"));
        if (cop != null) {
            setCrossoverOperator(cop);
        } else {
            setCrossoverOperator(new OneOffspringConvexRealEncodingCrossover());
        }
        GeneticMutationOperator mop = GeneticMutationOperatorFactory.getInstance(XmlUtil.getParameter(root, "mutation-operator-class"));
        if (mop != null) {
            setMutationOperator(mop);
        } else {
            setMutationOperator(new SingleParameterRealEncodingMutator());
        }
    }

    public Individual selectByTournament(GeneticMethodParameters params, Generation population) {
        Individual ind = null;
        if (params != null && population != null) {
            int nTounamentSize = params.getInt(GeneticMethodParameters.TOURNAMENT_SIZE);
            if (nTounamentSize == params.getInt(GeneticMethodParameters.POPULATION_SIZE)) {
                ind = population.getBestValue();
            } else {
                for (int t = 0; t < nTounamentSize; t++) {
                    Individual rand = population.getRandIndividual();
                    if (rand != null) {
                        if (ind == null || rand.getFunctionValue() > ind.getFunctionValue()) {
                            ind = rand;
                        }
                    }
                }
            }
        }
        return ind;
    }

    public Individual crossover(final Individual f, final Individual m, final Transform min, final Transform max) {
        assert m_crossover != null;
        return m_crossover.crossover(f, m, min, max);
    }

    public void mutate(final Individual ind, final Transform min, final Transform max) {
        assert m_mutator != null;
        m_mutator.mutate(ind, min, max);
    }

    public void setCrossoverOperator(final OneOffspringCrossoverOperator cop) {
        assert cop != null;
        m_crossover = cop;
    }

    public void setMutationOperator(GeneticMutationOperator mop) {
        assert mop != null;
        m_mutator = mop;
    }

    private OneOffspringCrossoverOperator m_crossover;

    private GeneticMutationOperator m_mutator;
}
