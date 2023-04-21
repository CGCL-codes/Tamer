package cunei.evaluation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import cunei.config.BooleanParameter;
import cunei.config.Configuration;
import cunei.config.FloatParameter;
import cunei.config.IntegerParameter;
import cunei.document.Phrase;
import cunei.document.Sentence;
import cunei.evaluation.AbstractEvaluation.AbstractSentenceEvaluation;
import cunei.model.Gradient;
import cunei.type.SequenceType;
import cunei.type.Type;
import cunei.type.TypeSequence;

public class F1 implements Metric<AbstractSentenceEvaluation<HypothesisEvaluation>> {

    private class F1Expectation implements MetricExpectation, MetricExpectationUpdate<AbstractSentenceEvaluation<HypothesisEvaluation>> {

        private double f1CorrectMean;

        private double f1CorrectVariance;

        private double f1TotalMean;

        private double f1TotalVariance;

        private Gradient f1CorrectMeanGradient;

        private Gradient f1CorrectVarianceGradient;

        private Gradient f1TotalMeanGradient;

        private Gradient f1TotalVarianceGradient;

        public F1Expectation() {
            f1CorrectMean = 0;
            f1CorrectVariance = 0;
            f1TotalMean = 0;
            f1TotalVariance = 0;
            f1CorrectMeanGradient = new Gradient();
            f1CorrectVarianceGradient = new Gradient();
            f1TotalMeanGradient = new Gradient();
            f1TotalVarianceGradient = new Gradient();
        }

        public Gradient getGradient() {
            Gradient result = new Gradient();
            result.sum(f1CorrectMeanGradient, -1 / f1CorrectMean);
            result.sum(f1CorrectMeanGradient, -f1CorrectVariance / Math.pow(f1CorrectMean, 3));
            result.sum(f1CorrectVarianceGradient, 0.5 / Math.pow(f1CorrectMean, 2));
            result.sum(f1TotalMeanGradient, 1 / f1TotalMean);
            result.sum(f1TotalMeanGradient, f1TotalVariance / Math.pow(f1TotalMean, 3));
            result.sum(f1TotalVarianceGradient, -0.5 / Math.pow(f1TotalMean, 2));
            return result;
        }

        public double getScore() {
            return Math.log(f1TotalMean) - f1TotalVariance / (2 * Math.pow(f1TotalMean, 2)) - Math.log(f1CorrectMean) + f1CorrectVariance / (2 * Math.pow(f1CorrectMean, 2));
        }

        public void update(final int count, Collection<HypothesisEvaluation> normHypotheses, AbstractSentenceEvaluation<HypothesisEvaluation> sentence, final int id) {
            double f1CorrectMean = 0;
            double f1TotalMean = 0;
            Gradient f1CorrectMeanGradient = new Gradient();
            Gradient f1TotalMeanGradient = new Gradient();
            for (HypothesisEvaluation hypothesis : normHypotheses) {
                final double prob = Math.exp(hypothesis.getLogScore());
                if (prob == 0) continue;
                final Gradient gradient = hypothesis.getGradient();
                final F1Statistic statistic = (F1Statistic) hypothesis.getStatistic(id);
                double f1Correct = statistic.getF1Correct();
                double f1Total = statistic.getF1Total();
                f1CorrectMean += prob * f1Correct;
                f1CorrectMeanGradient.sum(gradient, f1Correct);
                f1TotalMean += prob * f1Total;
                f1TotalMeanGradient.sum(gradient, f1Total);
            }
            synchronized (this) {
                this.f1CorrectMean += f1CorrectMean;
                this.f1TotalMean += f1TotalMean;
            }
            synchronized (this.f1TotalMeanGradient) {
                this.f1CorrectMeanGradient.sum(f1CorrectMeanGradient, 1);
                this.f1TotalMeanGradient.sum(f1TotalMeanGradient, 1);
            }
            for (HypothesisEvaluation hypothesis : normHypotheses) {
                final double prob = Math.exp(hypothesis.getLogScore());
                if (prob == 0) continue;
                final Gradient gradient = hypothesis.getGradient();
                final F1Statistic statistic = (F1Statistic) hypothesis.getStatistic(id);
                double f1Correct = statistic.getF1Correct();
                double f1Total = statistic.getF1Total();
                synchronized (this) {
                    f1CorrectVariance += prob * Math.pow(f1Correct - f1CorrectMean, 2);
                    f1TotalVariance += prob * Math.pow(f1Total - f1TotalMean, 2);
                }
                synchronized (f1TotalVarianceGradient) {
                    double precisionCorrectDistance = f1Correct - f1CorrectMean;
                    f1CorrectVarianceGradient.sum(gradient, Math.pow(precisionCorrectDistance, 2));
                    f1CorrectVarianceGradient.sum(f1CorrectMeanGradient, -2.0 * prob * precisionCorrectDistance);
                    double precisionTotalDistance = f1Total - f1TotalMean;
                    f1TotalVarianceGradient.sum(gradient, Math.pow(precisionTotalDistance, 2));
                    f1TotalVarianceGradient.sum(f1TotalMeanGradient, -2.0 * prob * precisionTotalDistance);
                }
            }
        }

        public void write(PrintStream output) {
            double score = getScore();
            output.format("@ %-48s   %-12.7g%n", "F1", Math.exp(-score));
            output.format("@ %-33s [%12.6g]   %-12.7g%n", "Log F1", getWeight(), score);
        }
    }

    private class F1Scorer implements MetricScorer {

        private HashMap<TypeSequence, Integer> refNgrams[];

        @SuppressWarnings("unchecked")
        public F1Scorer(Sentence<Phrase>[] references) {
            final int refsSize = references.length;
            refNgrams = new HashMap[refsSize];
            for (int i = 0; i < refsSize; i++) {
                Phrase phrase = references[i].get();
                if (phrase == null) continue;
                TypeSequence sequence = phrase.get(SequenceType.LEXICAL);
                if (sequence == null) continue;
                refNgrams[i] = getNgrams(sequence);
            }
        }

        private HashMap<TypeSequence, Integer> getNgrams(TypeSequence sequence) {
            HashMap<TypeSequence, Integer> result = new HashMap<TypeSequence, Integer>();
            if (!caseSensitive) sequence = sequence.getCaseInsensitiveSequence();
            final int startNgram = ngramSize < 0 ? 1 : ngramSize;
            final int endNgram = ngramSize < 0 ? -ngramSize : ngramSize;
            int size = sequence.size();
            for (int ngram = startNgram; ngram <= endNgram; ngram++) for (int i = 0; i <= size - ngram; i++) if (ngram == 1 || gapSize == 0) {
                TypeSequence subSequence = sequence.sub(i, ngram);
                Integer count = result.get(subSequence);
                result.put(subSequence, count == null ? 1 : count + 1);
            } else for (Type[] types : getSkipNgrams(sequence, i, size, new Type[ngram], 0)) {
                TypeSequence subSequence = new TypeSequence(types);
                Integer count = result.get(subSequence);
                result.put(subSequence, count == null ? 1 : count + 1);
            }
            return result;
        }

        private Collection<Type[]> getSkipNgrams(TypeSequence sequence, int pos, int size, Type[] types, int i) {
            types[i++] = sequence.get(pos++);
            Collection<Type[]> result = new ArrayList<Type[]>();
            if (i == types.length) result.add(types); else for (int gap = Math.min(gapSize, size - pos - 1); gap >= 0; gap--) result.addAll(getSkipNgrams(sequence, pos + gap, size, gap == 0 ? types : Arrays.copyOf(types, types.length), i));
            return result;
        }

        public F1Statistic getStatistic(TypeSequence target) {
            double f1Correct = 0;
            double f1Total = 0;
            HashMap<TypeSequence, Integer> targetNgrams;
            if (target == null) targetNgrams = null; else {
                targetNgrams = getNgrams(target);
                for (Integer count : targetNgrams.values()) f1Total += count;
                f1Total *= refNgrams.length;
            }
            for (HashMap<TypeSequence, Integer> refNgram : refNgrams) {
                if (refNgram == null) continue;
                if (targetNgrams != null) for (Map.Entry<TypeSequence, Integer> entry : refNgram.entrySet()) {
                    final TypeSequence ngram = entry.getKey();
                    final Integer targetCount = targetNgrams.get(ngram);
                    if (targetCount == null) continue;
                    final Integer refCount = entry.getValue();
                    f1Correct += 2 * Math.min(targetCount, refCount);
                }
                for (Integer count : refNgram.values()) f1Total += count;
            }
            return new F1Statistic(f1Correct, f1Total);
        }
    }

    protected static class F1Statistic {

        private double f1Correct;

        private double f1Total;

        public F1Statistic(double f1Correct, double f1Total) {
            this.f1Correct = f1Correct;
            this.f1Total = f1Total;
        }

        protected float getF1() {
            return (float) ((f1Correct + 1) / (f1Total + 1));
        }

        protected double getF1Correct() {
            return f1Correct;
        }

        protected double getF1Total() {
            return f1Total;
        }
    }

    private final String name;

    private final FloatParameter weight;

    private final BooleanParameter doAnnealing;

    private final int ngramSize;

    private final int gapSize;

    private final boolean caseSensitive;

    public F1(final Configuration config, final String name) {
        this.name = name;
        weight = FloatParameter.get(config, "Weight", 1f);
        doAnnealing = BooleanParameter.get(config, "Anneal", false);
        ngramSize = IntegerParameter.get(config, "Ngram", 1).getValue();
        gapSize = IntegerParameter.get(config, "Gap", 0).getValue();
        caseSensitive = BooleanParameter.get(config, "CaseSensitive", true).getValue();
    }

    public boolean doAnnealing() {
        return doAnnealing.getValue();
    }

    public MetricExpectationUpdate<AbstractSentenceEvaluation<HypothesisEvaluation>> getExpectation() {
        return new F1Expectation();
    }

    public F1Scorer getScorer(Sentence<Phrase>[] references) {
        return new F1Scorer(references);
    }

    public float getWeight() {
        return weight.getValue();
    }

    public void setWeight(float value) {
        weight.setValue(value);
    }

    public String toString() {
        return name;
    }
}
