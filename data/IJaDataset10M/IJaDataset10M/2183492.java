package weka.filters.supervised.instance.rangega;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import weka.core.Instances;
import weka.filters.supervised.instance.SamplingRangeGA;

public class ExperimentTest {

    SamplingRangeGA filter;

    Instances data;

    String testFiles[];

    String directory;

    private int numExperiments;

    protected static String PROP_FILE = "experiment.properties";

    private Properties properties;

    private int minorityClassIndex;

    int dataIndex = 0;

    public static void main(String args[]) throws Exception {
        ExperimentTest test = new ExperimentTest();
        test.testExperiment();
    }

    public ExperimentTest() {
        loadProperties();
    }

    public void setup() {
        if (filter != null) {
            filter = null;
        }
        data = null;
        System.gc();
        try {
            java.io.Reader r = new java.io.BufferedReader(new java.io.FileReader(directory + testFiles[dataIndex]));
            data = new Instances(r);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
        filter = new SamplingRangeGA();
        data.setClassIndex(data.numAttributes() - 1);
        filter.setShowResultWindow(Boolean.parseBoolean(properties.getProperty("showResultWindow", "false")));
        filter.setMaximumGenerations(Integer.parseInt(properties.getProperty("maxGenerations", "400")));
        filter.setMaximumPopulation(Integer.parseInt(properties.getProperty("maxPopulation", "100")));
        filter.setInitialPopulation(Integer.parseInt(properties.getProperty("initialPopulation", "300")));
        filter.setElitismPercent(Integer.parseInt(properties.getProperty("elitismPercent", "5")));
        filter.setPercentToSample(Integer.parseInt(properties.getProperty("percentToSample", "100")));
        filter.setNumRanges(Integer.parseInt(properties.getProperty("numRanges", "4")));
        filter.setMutationPercent(Integer.parseInt(properties.getProperty("mutationPercent", "25")));
        filter.setCrossoverPercent(Integer.parseInt(properties.getProperty("crossoverPercent", "80")));
        minorityClassIndex = Integer.parseInt(properties.getProperty("minorityClassIndex", "0"));
        filter.setMinorityClassIndex(minorityClassIndex);
    }

    public void loadProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(PROP_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        directory = properties.getProperty("directory", "./");
        numExperiments = Integer.parseInt(properties.getProperty("numExperiments", "10"));
        String files = properties.getProperty("files", "");
        StringTokenizer token = new StringTokenizer(files);
        testFiles = new String[token.countTokens()];
        int count = 0;
        while (token.hasMoreElements()) {
            String fileName = token.nextToken();
            testFiles[count] = fileName;
            count++;
        }
    }

    public void testExperiment() throws Exception {
        for (int i = 0; i < testFiles.length; ++i) {
            execute();
            dataIndex++;
        }
    }

    public void execute() throws Exception {
        double sumAUC = 0;
        double sumPrecision = 0;
        double sumRecall = 0;
        double sumFMeasure = 0;
        double sumGmean = 0;
        long sumElapsedTime = 0;
        for (int i = 0; i < numExperiments; ++i) {
            setup();
            filter.process(data);
            if (i == 0) {
                System.out.println(filter.getEngine().getSummaryString());
            }
            RangeFitness fitness = (RangeFitness) filter.getEngine().getMelhorIndividuo().getFuncaoDesempenho();
            double AUC = fitness.getDesempenho();
            double precision = fitness.getEvaluation().precision(minorityClassIndex);
            double recall = fitness.getEvaluation().recall(minorityClassIndex);
            double fmeasure = fitness.getEvaluation().fMeasure(minorityClassIndex);
            double gmean = fitness.calculateGM();
            sumPrecision += precision;
            sumRecall += recall;
            sumFMeasure += fmeasure;
            sumGmean += gmean;
            sumElapsedTime += filter.getEngine().getTempoDesempenhoMaximoAtual();
            sumAUC += AUC;
            System.out.println("\r\nexperiment:" + i + "  " + filter.getEngine().getBestIndividualString());
        }
        System.out.println("\r\n" + testFiles[dataIndex] + " AVG AUC:" + (sumAUC / numExperiments) + " AVG Precision:" + (sumPrecision / numExperiments) + " AVG Recall:" + (sumRecall / numExperiments) + " AVG F-Measure:" + (sumFMeasure / numExperiments) + " AVG G-Mean:" + (sumGmean / numExperiments) + " AVG Elapsed time:" + (sumElapsedTime / numExperiments));
    }
}
