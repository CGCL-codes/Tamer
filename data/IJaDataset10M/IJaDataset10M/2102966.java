package org.dllearner.learningproblems;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.dllearner.core.AbstractReasonerComponent;
import org.dllearner.core.ComponentInitException;
import org.dllearner.core.EvaluatedDescription;
import org.dllearner.core.options.BooleanConfigOption;
import org.dllearner.core.options.ConfigEntry;
import org.dllearner.core.options.ConfigOption;
import org.dllearner.core.options.DoubleConfigOption;
import org.dllearner.core.options.InvalidConfigOptionValueException;
import org.dllearner.core.owl.Description;
import org.dllearner.core.owl.Individual;
import org.dllearner.core.owl.Negation;
import org.dllearner.reasoning.ReasonerType;
import org.dllearner.utilities.Helper;
import org.dllearner.utilities.datastructures.SortedSetTuple;

/**
 * @author Jens Lehmann
 *
 */
public class PosNegLPStrict extends PosNegLP {

    private Set<Individual> neutralExamples;

    private boolean penaliseNeutralExamples = false;

    private static final double defaultAccuracyPenalty = 1;

    private double accuracyPenalty = defaultAccuracyPenalty;

    private static final double defaultErrorPenalty = 3;

    private double errorPenalty = defaultErrorPenalty;

    public PosNegLPStrict(AbstractReasonerComponent reasoningService) {
        super(reasoningService);
    }

    public static String getName() {
        return "three valued definition learning problem";
    }

    public static Collection<ConfigOption<?>> createConfigOptions() {
        Collection<ConfigOption<?>> options = PosNegLP.createConfigOptions();
        options.add(new BooleanConfigOption("penaliseNeutralExamples", "if set to true neutral examples are penalised"));
        options.add(new DoubleConfigOption("accuracyPenalty", "penalty for pos/neg examples which are classified as neutral", defaultAccuracyPenalty));
        options.add(new DoubleConfigOption("errorPenalty", "penalty for pos. examples classified as negative or vice versa", defaultErrorPenalty));
        return options;
    }

    @Override
    public <T> void applyConfigEntry(ConfigEntry<T> entry) throws InvalidConfigOptionValueException {
        super.applyConfigEntry(entry);
        String name = entry.getOptionName();
        if (name.equals("penaliseNeutralExamples")) penaliseNeutralExamples = (Boolean) entry.getValue(); else if (name.equals("accuracyPenalty")) accuracyPenalty = (Double) entry.getValue(); else if (name.equals("errorPenalty")) errorPenalty = (Double) entry.getValue();
    }

    @Override
    public void init() throws ComponentInitException {
        super.init();
        neutralExamples = Helper.intersection(getReasoner().getIndividuals(), positiveExamples);
        neutralExamples.retainAll(negativeExamples);
    }

    @Override
    public ScorePosNeg computeScore(Description concept) {
        if (isUseRetrievalForClassification()) {
            if (getReasoner().getReasonerType() == ReasonerType.FAST_RETRIEVAL) {
                SortedSetTuple<Individual> tuple = getReasoner().doubleRetrieval(concept);
                Set<Individual> neutClassified = Helper.intersectionTuple(getReasoner().getIndividuals(), tuple);
                return new ScoreThreeValued(concept.getLength(), accuracyPenalty, errorPenalty, penaliseNeutralExamples, getPercentPerLengthUnit(), tuple.getPosSet(), neutClassified, tuple.getNegSet(), positiveExamples, neutralExamples, negativeExamples);
            } else if (getReasoner().getReasonerType() == ReasonerType.KAON2) {
                SortedSet<Individual> posClassified = getReasoner().getIndividuals(concept);
                SortedSet<Individual> negClassified = getReasoner().getIndividuals(new Negation(concept));
                Set<Individual> neutClassified = Helper.intersection(getReasoner().getIndividuals(), posClassified);
                neutClassified.retainAll(negClassified);
                return new ScoreThreeValued(concept.getLength(), accuracyPenalty, errorPenalty, penaliseNeutralExamples, getPercentPerLengthUnit(), posClassified, neutClassified, negClassified, positiveExamples, neutralExamples, negativeExamples);
            } else throw new Error("score cannot be computed in this configuration");
        } else {
            if (getReasoner().getReasonerType() == ReasonerType.KAON2) {
                if (penaliseNeutralExamples) throw new Error("It does not make sense to use single instance checks when" + "neutral examples are penalized. Use Retrievals instead.");
                SortedSet<Individual> posClassified = new TreeSet<Individual>();
                SortedSet<Individual> negClassified = new TreeSet<Individual>();
                for (Individual example : positiveExamples) {
                    if (getReasoner().hasType(concept, example)) posClassified.add(example);
                }
                for (Individual example : negativeExamples) {
                    if (getReasoner().hasType(concept, example)) posClassified.add(example);
                }
                for (Individual example : positiveExamples) {
                    if (getReasoner().hasType(new Negation(concept), example)) negClassified.add(example);
                }
                for (Individual example : negativeExamples) {
                    if (getReasoner().hasType(new Negation(concept), example)) negClassified.add(example);
                }
                Set<Individual> neutClassified = Helper.intersection(getReasoner().getIndividuals(), posClassified);
                neutClassified.retainAll(negClassified);
                return new ScoreThreeValued(concept.getLength(), accuracyPenalty, errorPenalty, penaliseNeutralExamples, getPercentPerLengthUnit(), posClassified, neutClassified, negClassified, positiveExamples, neutralExamples, negativeExamples);
            } else throw new Error("score cannot be computed in this configuration");
        }
    }

    @Override
    public int coveredNegativeExamplesOrTooWeak(Description concept) {
        throw new UnsupportedOperationException("Method not implemented for three valued definition learning problem.");
    }

    public Set<Individual> getNeutralExamples() {
        return neutralExamples;
    }

    /**
	 * @return the accuracyPenalty
	 */
    public double getAccuracyPenalty() {
        return accuracyPenalty;
    }

    /**
	 * @return the errorPenalty
	 */
    public double getErrorPenalty() {
        return errorPenalty;
    }

    /**
	 * @return the penaliseNeutralExamples
	 */
    public boolean isPenaliseNeutralExamples() {
        return penaliseNeutralExamples;
    }

    @Override
    public double getAccuracy(Description description) {
        return 0;
    }

    @Override
    public double getAccuracyOrTooWeak(Description description, double minAccuracy) {
        return 0;
    }

    @Override
    public EvaluatedDescription evaluate(Description description) {
        return null;
    }
}
