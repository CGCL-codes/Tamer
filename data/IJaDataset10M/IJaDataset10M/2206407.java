package com.ibm.realtime.rtmb.tests;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import com.ibm.realtime.rtmb.params.RealTimeValues;
import com.ibm.realtime.rtmb.params.TestInfo;
import com.ibm.realtime.rtmb.params.TestParams;
import com.ibm.realtime.rtmb.results.TestResults;
import com.ibm.realtime.rtmb.results.TestScores;
import com.ibm.realtime.rtmb.tests.impl.BenchMarkThreadFactory;
import com.ibm.realtime.rtmb.tests.util.BenchMarkThread;
import com.ibm.realtime.rtmb.tests.util.CompilationWorker;
import com.ibm.realtime.rtmb.tests.util.Cpus;
import com.ibm.realtime.rtmb.tests.util.PriorityType;
import com.ibm.realtime.rtmb.tests.util.SpecTest;
import com.ibm.realtime.rtmb.tests.util.TestId;
import com.ibm.realtime.rtmb.util.VerbosePrint;

public class CompilationTest extends SpecTest {

    private static int counter = 0;

    private static Class<?> thisClass = null;

    private int totalThreads = Cpus.onlineCpus / 2;

    private boolean giveZeroScore = false;

    private Vector<Double> vectorOfMajorDrops = null;

    private double avgDropPerReCompile = 0.001;

    @Override
    public TestScores[] generateCombinedScore(TestInfo tInfo, TestResults[] tResult) {
        final double neg = -1;
        final double zero = 0;
        double finalScore = 1;
        TestScores[] tScores = null;
        if (!giveZeroScore) {
            tScores = super.generateCombinedScore(tInfo, tResult);
        } else {
            tScores = new TestScores[1];
            tScores[0] = new TestScores();
            tScores[0].setTestResults(tResult);
            tScores[0].setThroughputScoreForTest(zero);
            tScores[0].setMaxThroughput(zero);
            tScores[0].setMinThroughput(zero);
            tScores[0].setMeanThroughput(zero);
            tScores[0].setDeterminismScore(neg);
            tScores[0].setWeightedThroughput(neg);
            return tScores;
        }
        for (int k = 0; k < tScores.length; k++) {
            tScores[k].setDeterminismScore(neg);
            tScores[k].setWeightedThroughput(neg);
            finalScore *= getGMmean(tScores[k]);
            finalScore = tInfo.getReferenceScore() / finalScore;
            tScores[k].setThroughputScoreForTest(finalScore);
        }
        return tScores;
    }

    private double getGMmean(TestScores tScore) {
        TestResults[] tResult = tScore.getTestResults();
        double product = 1;
        for (int i = 0; i < tResult.length; i++) {
            int iterationMaxThroughput = 0;
            int iterationMinThroughput = 0;
            double values[] = tResult[i].getValues();
            double meanThroughput = 0;
            double initialThroughput = 1.0 * SpecTest.scale / values[0];
            double finalThroughput = 1.0 * SpecTest.scale / values[values.length - 1];
            for (int j = 0; j < values.length; j++) {
                if (values[j] == tResult[i].getMax()) {
                    iterationMinThroughput = j;
                } else if (values[j] == tResult[i].getMin()) {
                    iterationMaxThroughput = j;
                }
                meanThroughput += 1.0 * SpecTest.scale / values[j];
            }
            meanThroughput /= values.length;
            product *= initialThroughput * finalThroughput * meanThroughput * ((1.0 * SpecTest.scale) / this.avgDropPerReCompile) * ((1.0 * SpecTest.scale) * (tResult[i].getMax() - tResult[i].getMin())) / (iterationMaxThroughput - iterationMinThroughput);
            product = Math.pow(product, (1.0 / 5));
        }
        return product;
    }

    @Override
    public TestResults[] benchMarkTest(TestInfo tInfo, RealTimeValues rtValues, TestParams tParams) {
        int new_iters = 0;
        boolean isUniProcessor;
        establishThisClass(this.getClass());
        this.vectorOfMajorDrops = new Vector<Double>();
        Compiler.enable();
        new_iters = updateIterations(tInfo, rtValues, tParams);
        tInfo.setTestIterations(new_iters);
        TestResults testResults[] = new TestResults[totalThreads];
        CompilationWorker worker[] = new CompilationWorker[totalThreads];
        BenchMarkThread highPriorityThreads[] = new BenchMarkThread[totalThreads];
        isUniProcessor = (Cpus.onlineCpus == 1) ? true : false;
        for (int i = 0; i < totalThreads; i++) {
            testResults[i] = new TestResults(TestId.COMPILATION, new_iters, false);
            testResults[i].setTestResultName("Thread " + i);
            worker[i] = new CompilationWorker(testResults[i], tInfo, new_iters, isUniProcessor, tParams);
        }
        VerbosePrint.testStart(System.out, tParams, name(), tInfo.getTestIterations());
        BenchMarkThreadFactory threadFactory = BenchMarkThreadFactory.getInstance();
        for (int i = 0; i < totalThreads; i++) {
            highPriorityThreads[i] = threadFactory.getThread(PriorityType.HighPriority, worker[i]);
            incrementCounter();
            highPriorityThreads[i].execute();
        }
        waitForThreadsExit();
        if (!verifyResults(testResults, tParams, rtValues)) {
            giveZeroScore = true;
        }
        Compiler.disable();
        return testResults;
    }

    private boolean verifyResults(TestResults[] testResults, TestParams tParams, RealTimeValues rtValues) {
        boolean moveOn = false;
        int posSpike = 0;
        for (int i = 0; i < totalThreads; i++) {
            double values[] = testResults[i].getValues();
            Double d = values[0];
            if (d.compareTo(values[values.length - 1]) < 0) {
                VerbosePrint.errPrintln("CompilationTest: Throughput reduced " + "between first and last iteration of the test run");
                VerbosePrint.errPrintln("First iteration: " + values[0]);
                VerbosePrint.errPrintln("Last iteration: " + values[values.length - 1]);
                return false;
            }
            LinkedList<Variation> listOfSpikes = new LinkedList<Variation>();
            for (int j = 0; j < values.length - 1; j++) {
                if (values[j] > values[j + 1]) {
                    listOfSpikes.add(new Variation(j, values[j], values[j + 1], false));
                } else if (values[j] < values[j + 1]) {
                    posSpike++;
                    listOfSpikes.add(new Variation(j, values[j], values[j + 1], true));
                }
            }
            Iterator<Variation> iter = listOfSpikes.iterator();
            while (iter.hasNext()) {
                Variation v = iter.next();
                if (!v.isSpike()) {
                    for (int k = v.getIteration(); k < listOfSpikes.size(); k++) {
                        if (v.getFirstValOfPair() < listOfSpikes.get(k).getFirstValOfPair()) {
                            moveOn = true;
                            break;
                        }
                    }
                    if (!moveOn) {
                        for (int k = v.getIteration(); k > 1; k--) {
                            if (v.getSecondValOfPair() > listOfSpikes.get(k).getSecondValOfPair()) {
                                moveOn = true;
                                break;
                            }
                        }
                    }
                    if (moveOn) {
                        continue;
                    } else {
                        this.vectorOfMajorDrops.add(Double.valueOf(v.getDelta()));
                    }
                }
            }
            Iterator<Double> iter2 = vectorOfMajorDrops.iterator();
            double sum = 0;
            while (iter2.hasNext()) {
                double val = iter2.next();
                sum += val;
            }
            if (vectorOfMajorDrops.size() != 0) {
                this.avgDropPerReCompile = (sum) / vectorOfMajorDrops.size();
            }
            TestResults posSpikeDeltas = new TestResults(TestId.COMPILATION, posSpike, true);
            Iterator iter3 = listOfSpikes.iterator();
            while (iter3.hasNext()) {
                Variation v = (Variation) iter3.next();
                if (v.isSpike()) {
                    posSpikeDeltas.setValuesAtNextIndex(v.getDelta());
                }
            }
            if (!verifyNoUnnacceptableSpike(posSpikeDeltas, rtValues)) {
                VerbosePrint.errPrintln("Compilation: *** ERROR ***" + " A huge increase/spike seen in execution time between two " + "iterations.");
                VerbosePrint.errPrintln();
                return false;
            }
        }
        return true;
    }

    private boolean verifyNoUnnacceptableSpike(TestResults posSpikes, RealTimeValues rtValues) {
        double median = posSpikes.getMedian();
        double[] deltaValues = posSpikes.getValues();
        int entries = deltaValues.length;
        int minAcceptableIterations = ((int) ((rtValues.getAcceptablePercent() / 100) * (entries)));
        double acceptableDelta = ((rtValues.getRangePercent() / 100) * median);
        double positiveLimit = median + acceptableDelta;
        int nonConformingIterations = 0;
        for (int i = 0; i < entries; i++) {
            if (deltaValues[i] > positiveLimit) {
                nonConformingIterations++;
            }
        }
        if ((entries - nonConformingIterations) < minAcceptableIterations) {
            return false;
        } else {
            return true;
        }
    }

    public static synchronized void decrementCounter() {
        assert (counter >= 0);
        if (counter > 0) {
            counter--;
            if (counter == 0) {
                thisClass.notify();
            }
        }
    }

    public static synchronized void establishThisClass(Class<?> clazz) {
        thisClass = clazz;
    }

    private static synchronized void incrementCounter() {
        assert (counter >= 0);
        counter++;
    }

    private static synchronized void waitForThreadsExit() {
        while (counter > 0) {
            try {
                thisClass.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String name() {
        return "CompilationTest";
    }
}

class Variation {

    private double firstValOfPair;

    private double secondValOfPair;

    private double delta;

    private boolean spike;

    private int iteration;

    public Variation(int iter, double one, double two, boolean spike) {
        super();
        this.firstValOfPair = one;
        this.secondValOfPair = two;
        this.spike = spike;
        if (spike) {
            this.delta = secondValOfPair - firstValOfPair;
        } else {
            this.delta = firstValOfPair - secondValOfPair;
        }
        this.iteration = iter;
    }

    public double getFirstValOfPair() {
        return firstValOfPair;
    }

    public void setFirstValOfPair(double firstValOfPair) {
        this.firstValOfPair = firstValOfPair;
    }

    public double getSecondValOfPair() {
        return secondValOfPair;
    }

    public void setSecondValOfPair(double secondValOfPair) {
        this.secondValOfPair = secondValOfPair;
    }

    public boolean isSpike() {
        return spike;
    }

    public void setSpike(boolean spike) {
        this.spike = spike;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public int getIteration() {
        return iteration;
    }
}
