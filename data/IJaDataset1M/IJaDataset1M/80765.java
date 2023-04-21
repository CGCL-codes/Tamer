package org.az.hhp.predictors;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.az.hhp.collections.StatsMatrix;
import org.az.hhp.domain.Claim;
import org.az.hhp.domain.ClaimsList;
import org.az.hhp.domain.Member;
import org.az.hhp.domain.Target;
import org.az.hhp.interfaces.NonTrainedException;
import org.az.hhp.tools.AbstractPredictor;
import org.az.hhp.tools.ArraysTools;
import org.az.hhp.tools.DataImporter;
import org.az.hhp.tools.Evaluator;
import org.az.hhp.tools.Genetics;
import org.az.hhp.tools.Genetics.FitnessFunction;
import org.az.hhp.tools.StatsMatrixFactory;
import org.az.hhp.tools.TrainSetManager;
import org.az.hhp.tools.Year;

public abstract class AbstractPredictorBase implements AbstractPredictor {

    protected static final String GENOME_ROW = "genome";

    protected StatsMatrix genomeMatrix;

    private final String name;

    @Override
    public String getName() {
        return name;
    }

    public AbstractPredictorBase(final String name) {
        this.name = name;
    }

    public AbstractPredictorBase() {
        this("");
    }

    protected void init() {
        genomeMatrix = DataImporter.instance().loadStatsMatrix(makeGenomeFilename());
        if (genomeMatrix == null) {
            genomeMatrix = initDefaultGenome();
        }
        cacheGenomeValues();
    }

    protected String makeGenomeFilename() {
        return "genome_" + this.getClass().getSimpleName() + ".csv";
    }

    @Override
    public final void setParametersGenome(final List<Double> values) {
        genomeMatrix.setValues(GENOME_ROW, values);
        cacheGenomeValues();
    }

    protected void cacheGenomeValues() {
    }

    public final synchronized void saveGenome() {
        DataImporter.instance().saveStatsMatrix(genomeMatrix, makeGenomeFilename());
    }

    @Override
    public final List<Double> getParametersGenome() {
        final ArrayList<Float> row = genomeMatrix.getStatistics(GENOME_ROW);
        final ArrayList<Double> ret = ArraysTools.toDouble(row);
        return ret;
    }

    public float genome(final int i) {
        return genomeMatrix.getValue(GENOME_ROW, i);
    }

    public Float genome(final String s) {
        return genomeMatrix.getValue(GENOME_ROW, s);
    }

    protected abstract StatsMatrix initDefaultGenome();

    /**
     * 
     * @return RMSLE
     * @throws NonTrainedException
     */
    public final double evaluate(final Collection<Integer> prooveset, final Map<Integer, Integer> actualValues, final Year yearToPredict) throws NonTrainedException {
        final ArrayList<Double> actuals = new ArrayList<Double>();
        final ArrayList<Double> predictions = new ArrayList<Double>();
        for (final Integer mi : prooveset) {
            if (actualValues.get(mi) != null) {
                actuals.add(actualValues.get(mi).doubleValue());
                predictions.add(predict(mi, yearToPredict));
            }
        }
        return Evaluator.RMSLE(predictions, actuals);
    }

    public final StatsMatrix getPredictionQualityMatrix(final Collection<Integer> prooveset, final Map<Integer, Integer> actualValues, final Year yearToPredict) throws NonTrainedException, FileNotFoundException {
        final Map<Integer, Member> members = DataImporter.instance().loadMembers();
        final StatsMatrix sm = StatsMatrixFactory.createMatrix("actual dih", "predicted dih", "claims increase", "abs delta", "SLE", "number of claims " + yearToPredict.prev().prev(), "number of claims " + yearToPredict.prev(), "age", "sex");
        final DataImporter di = DataImporter.instance();
        final ClaimsList Y2 = di.loadClaims(yearToPredict.prev() + "_Claims.csv");
        final ClaimsList Y1 = di.loadClaims(yearToPredict.prev().prev() + "_Claims.csv");
        for (final Integer mi : prooveset) {
            if (actualValues.get(mi) != null) {
                final Member m = members.get(mi);
                final float actual = (float) actualValues.get(mi).doubleValue();
                final float prediction = (float) predict(mi, yearToPredict);
                final String keyId = mi.toString();
                sm.increaseCount(keyId, "actual dih", actual);
                sm.increaseCount(keyId, "predicted dih", prediction);
                sm.increaseCount(keyId, "abs delta", Math.abs(prediction - actual));
                sm.increaseCount(keyId, "SLE", (float) Evaluator.SLE(prediction, actual));
                sm.increaseCount(keyId, "sex", m.getSex());
                sm.increaseCount(keyId, "age", m.getAge());
                final ArrayList<Claim> claims1 = Y2.getClaimsByMember(mi);
                if (claims1 != null) {
                    sm.increaseCount(keyId, "number of claims " + yearToPredict.prev(), claims1.size());
                }
                final ArrayList<Claim> claims2 = Y1.getClaimsByMember(mi);
                if (claims2 != null) {
                    sm.increaseCount(keyId, "number of claims " + yearToPredict.prev().prev(), claims2.size());
                }
                if (claims2 != null && claims1 != null) {
                    sm.increaseCount(keyId, "claims increase", claims2.size() - claims1.size());
                }
            }
        }
        di.saveStatsMatrix(sm, "PredictionQuality_" + this.getClass().getSimpleName() + ".csv");
        int i = 0;
        final List<Entry<String, ArrayList<Float>>> entrys = sm.getSordedEntrys("abs delta");
        for (final Entry<String, ArrayList<Float>> e : entrys) {
            if (i < 15) {
                final Integer memberId = Integer.parseInt(e.getKey());
                final ArrayList<Claim> claims = Y2.getClaimsByMember(memberId);
                di.saveToCsvFile(claims, "claimsY2-" + memberId + ".csv");
                i++;
            }
        }
        return sm;
    }

    public static final double evaluateBestConst(final Collection<Integer> prooveset, final Map<Integer, Integer> actualValues) {
        double constant = 100;
        double bestRmsle = 100;
        for (double prediction = 0; prediction < 5; prediction += 0.01) {
            final double pr = evaluateOnConst(prooveset, actualValues, prediction);
            if (pr < bestRmsle) {
                constant = prediction;
                bestRmsle = pr;
            }
        }
        return constant;
    }

    public void genenticalTrain(final Collection<Integer> prooveSet, final Collection<Integer> trainSet, final Map<Integer, Integer> DIH, final Year year) throws IOException {
        train(trainSet, prooveSet);
        final Genetics g = new Genetics(new FitnessFunction() {

            double bestR = Double.MAX_VALUE;

            @Override
            public synchronized double estimate(final List<Double> genome) {
                setParametersGenome(genome);
                double rmsleee;
                try {
                    rmsleee = evaluate(prooveSet, DIH, year);
                    if (rmsleee < bestR) {
                        bestR = rmsleee;
                        saveGenome();
                    }
                    return rmsleee;
                } catch (final NonTrainedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, getParametersGenome().size(), 32);
        g.seed(getParametersGenome());
        final PrintWriter out = new PrintWriter(new FileWriter(DataImporter.instance().file("learning_" + makeGenomeFilename())));
        g.multiThreadProcess(out, "T-" + getName());
    }

    public static double evaluateOnConst(final Collection<Integer> prooveset, final Map<Integer, Integer> actualValues, final double prediction) {
        final ArrayList<Double> actuals = new ArrayList<Double>();
        final ArrayList<Double> predictions = new ArrayList<Double>();
        for (final Integer mi : prooveset) {
            if (actualValues.get(mi) != null) {
                actuals.add(actualValues.get(mi).doubleValue());
                predictions.add(prediction);
            }
        }
        return Evaluator.RMSLE(predictions, actuals);
    }

    public void makeTargetPrediction() throws IOException, NonTrainedException {
        final DataImporter dataImporter = DataImporter.instance();
        final Map<Integer, Member> m = dataImporter.loadMembers();
        train(m.keySet(), TrainSetManager.instance().getProoveSetY3());
        final List<Target> targets = dataImporter.loadTargetTable(dataImporter.file("Target.csv"));
        for (final Target target : targets) {
            final Integer memberId = target.getMemberId();
            double predictedvalue = predict(memberId, Year.Y4);
            if (predictedvalue < 0) {
                predictedvalue = 0;
            }
            target.setPredictedValue(predictedvalue);
        }
        dataImporter.saveTargeFile("Target_Y4_" + getClass().getSimpleName() + ".csv", targets);
    }
}
