package org.debellor.classifiers;

import org.debellor.classifiers.utils.ArrayOperations;
import org.debellor.classifiers.utils.DiscreteDistributionApproximation;
import org.debellor.classifiers.utils.MinMaxObservation;
import org.debellor.core.Cell;
import org.debellor.core.Sample;
import org.debellor.core.Sample.SampleType;
import org.debellor.core.data.DataVector;
import org.debellor.core.data.SymbolicFeature;
import org.debellor.core.data.DataVector.DataVectorType;
import org.debellor.core.data.NumericFeature.NumericFeatureType;
import org.debellor.core.data.SymbolicFeature.SymbolicFeatureType;
import org.debellor.core.parameters.ParametersInfo;

/**
 * @author Sebastian Stawicki
 * 
 */
public class DecisionStump extends Cell {

    private static final int DEFAULT_NO_OF_SPLITS = 20;

    private SymbolicFeatureType decisionType;

    private DataVectorType dataType;

    private Stream input;

    private long[][][] counts;

    private DiscreteDistributionApproximation decisionEstimator;

    private int choosenAttributeIndex = -1;

    private SymbolicFeature[] model;

    private double splitValue = Double.NaN;

    private SymbolicFeature defaultDecision;

    private MinMaxObservation[] minMax;

    public DecisionStump() {
        super(true);
        ParametersInfo availParams = new ParametersInfo("splits", "20", "number of splits for the attributes");
        setAvailableParams(availParams);
    }

    @Override
    protected void onClose() throws Exception {
        input.close();
    }

    @Override
    protected void onErase() throws Exception {
        decisionType = null;
        dataType = null;
    }

    @Override
    protected void onLearn() throws Exception {
        Stream input = openInputStream();
        try {
            decisionType = input.sampleType.decision.asSymbolicFeatureType();
        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + " can handle only symbolic decisions");
        }
        try {
            dataType = input.sampleType.data.asDataVectorType();
        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + " can handle only vector data type");
        }
        input.close();
        gatherStatistics();
        counts = new long[dataType.size()][][];
        int splits = parameters.exists("splits") ? parameters.getAsInt("splits") : DEFAULT_NO_OF_SPLITS;
        for (int i = 0; i < dataType.size(); i++) {
            if (dataType.get(i) instanceof SymbolicFeatureType) {
                SymbolicFeatureType attribute = dataType.get(i).asSymbolicFeatureType();
                counts[i] = new long[attribute.size()][decisionType.size()];
            } else if (dataType.get(i) instanceof NumericFeatureType) {
                counts[i] = new long[splits + 1][decisionType.size()];
            } else {
                throw new Exception(NaiveBayes.class.getName() + " can handle only numeric or symbolic types");
            }
        }
        decisionEstimator = new DiscreteDistributionApproximation(decisionType);
        input = openInputStream();
        Sample sample;
        while ((sample = input.next()) != null) {
            updateCounts(sample);
            decisionEstimator.addValue(sample.decision);
        }
        input.close();
        choosenAttributeIndex = chooseBestAttributeIndex(counts);
        if (dataType.get(choosenAttributeIndex) instanceof SymbolicFeatureType) {
            buildModelForSymbolicFeature(choosenAttributeIndex);
        } else {
            buildModelForNumericFeature(choosenAttributeIndex);
        }
        defaultDecision = new SymbolicFeature(decisionType.get(decisionEstimator.mostProbableElement()));
    }

    private void gatherStatistics() throws Exception {
        minMax = new MinMaxObservation[dataType.size()];
        for (int i = 0; i < dataType.size(); i++) {
            if (dataType.get(i) instanceof NumericFeatureType) {
                minMax[i] = new MinMaxObservation();
            }
        }
        Stream input = openInputStream();
        Sample sample;
        while ((sample = input.next()) != null) {
            updateEstimator(sample);
        }
        input.close();
    }

    private void updateEstimator(Sample sample) throws Exception {
        DataVector data = sample.data.asDataVector();
        for (int i = 0; i < dataType.size(); i++) {
            if (dataType.get(i) instanceof NumericFeatureType) {
                minMax[i].addValue(data.get(i));
            }
        }
    }

    private void updateCounts(Sample sample) throws Exception {
        if (sample.decision == null) return;
        DataVector data = sample.data.asDataVector();
        for (int i = 0; i < dataType.size(); i++) {
            if (data.get(i) == null) continue;
            if (dataType.get(i) instanceof SymbolicFeatureType) {
                String value = data.get(i).asSymbolicFeature().value;
                int code = dataType.get(i).asSymbolicFeatureType().codeOf(value);
                counts[i][code][decisionType.codeOf(sample.decision.asSymbolicFeature().value)] += 1;
            } else {
                final int noOfIntervals = counts[i].length;
                final double min = minMax[i].getMin();
                final double max = minMax[i].getMax();
                double value = data.get(i).asNumericFeature().value;
                int part = (int) Math.floor((value - min) / ((max - min) / (noOfIntervals)));
                part = Math.max(0, part);
                part = Math.min(noOfIntervals - 1, part);
                counts[i][part][decisionType.codeOf(sample.decision.asSymbolicFeature().value)] += 1;
            }
        }
    }

    private int chooseBestAttributeIndex(long[][][] counts) {
        long[] estimation = new long[counts.length];
        for (int i = 0; i < counts.length; i++) {
            long noOfConflicts;
            if (dataType.get(i) instanceof SymbolicFeatureType) {
                noOfConflicts = noOfConflictsSymbolicFeature(counts[i]);
            } else {
                noOfConflicts = minNoOfConflictsNumericFeature(counts[i]);
            }
            estimation[i] = noOfConflicts;
        }
        return ArrayOperations.argmin(estimation);
    }

    private long noOfConflictsSymbolicFeature(long[][] counts) {
        long estimation = 0;
        for (int j = 0; j < counts.length; j++) {
            long sum = 0;
            for (int k = 0; k < counts[j].length; k++) {
                sum += counts[j][k];
                estimation -= counts[j][k] * counts[j][k];
            }
            estimation += sum * sum;
        }
        return estimation;
    }

    private long minNoOfConflictsNumericFeature(long[][] counts) {
        return ArrayOperations.min(noOfConflictsDistributionNumericFeature(counts));
    }

    private long[] noOfConflictsDistributionNumericFeature(long[][] counts) {
        int noOfSplits = counts.length - 1;
        long[] conflictsOnSplits = new long[noOfSplits];
        long[] onLeftDecisionDistribution = new long[decisionType.size()];
        long noOfLeftElements = 0;
        long[] onRightDecisionDistribution = new long[decisionType.size()];
        for (long[] distribution : counts) {
            ArrayOperations.add(onRightDecisionDistribution, distribution);
        }
        long noOfRightElements = ArrayOperations.sum(onRightDecisionDistribution);
        for (int i = 0; i < noOfSplits; i++) {
            long noOfMovedElements = ArrayOperations.sum(counts[i]);
            noOfLeftElements += noOfMovedElements;
            ArrayOperations.add(onLeftDecisionDistribution, counts[i]);
            noOfRightElements -= noOfMovedElements;
            ArrayOperations.sub(onRightDecisionDistribution, counts[i]);
            conflictsOnSplits[i] = conflicts(noOfLeftElements, onLeftDecisionDistribution) + conflicts(noOfRightElements, onRightDecisionDistribution);
        }
        return conflictsOnSplits;
    }

    private long conflicts(long noOfElements, long[] distribution) {
        long conflicts = noOfElements * noOfElements;
        for (long l : distribution) {
            conflicts -= l * l;
        }
        return conflicts;
    }

    private void buildModelForSymbolicFeature(int index) {
        model = new SymbolicFeature[counts[index].length];
        for (int i = 0; i < model.length; i++) {
            model[i] = new SymbolicFeature(decisionType.get(ArrayOperations.argmax(counts[index][i])));
        }
    }

    private void buildModelForNumericFeature(int index) {
        model = new SymbolicFeature[2];
        long[] conflictsOnSplits = noOfConflictsDistributionNumericFeature(counts[index]);
        int bestSplit = ArrayOperations.argmin(conflictsOnSplits);
        long[] onLeftDecisionDistribution = new long[decisionType.size()];
        for (int i = 0; i < bestSplit; i++) ArrayOperations.add(onLeftDecisionDistribution, counts[index][i]);
        long[] onRightDecisionDistribution = new long[decisionType.size()];
        for (int i = bestSplit; i < counts[index].length; i++) ArrayOperations.add(onRightDecisionDistribution, counts[index][i]);
        final int noOfIntervals = counts[index].length;
        final double min = minMax[index].getMin();
        final double max = minMax[index].getMax();
        splitValue = min + (bestSplit + 1) * (max - min) / (noOfIntervals);
        model[0] = new SymbolicFeature(decisionType.get(ArrayOperations.argmax(onLeftDecisionDistribution)));
        model[1] = new SymbolicFeature(decisionType.get(ArrayOperations.argmax(onRightDecisionDistribution)));
    }

    @Override
    protected Sample onNext() throws Exception {
        Sample sample = input.next();
        if (sample.data.asDataVector().get(choosenAttributeIndex) == null) return sample.setDecision(defaultDecision);
        int decisionIndex;
        if (dataType.get(choosenAttributeIndex) instanceof SymbolicFeatureType) {
            String value = sample.data.asDataVector().get(choosenAttributeIndex).asSymbolicFeature().value;
            decisionIndex = dataType.get(choosenAttributeIndex).asSymbolicFeatureType().codeOf(value);
        } else {
            double value = sample.data.asDataVector().get(choosenAttributeIndex).asNumericFeature().value;
            decisionIndex = 0;
            if (value >= splitValue) decisionIndex = 1;
        }
        return sample.setDecision(model[decisionIndex]);
    }

    @Override
    protected SampleType onOpen() throws Exception {
        input = openInputStream();
        return input.sampleType.setDecision(decisionType);
    }
}
