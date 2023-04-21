package net.sf.doolin.app.sc.sample;

public class SampleExtraction extends AbstractSampleChart {

    public static void main(String[] args) {
        new SampleExtraction().run();
    }

    public SampleExtraction() {
        super("Extraction");
        long resources = 10000;
        int populationMax = 50000;
        long expenses = 2000;
        for (int extractionLevel = 1; extractionLevel < 10; extractionLevel++) {
            int count = 50;
            double[][] curve = new double[2][count];
            for (int i = 1; i <= 50; i++) {
                int population = i * 1000;
                long extracted = this.gameComputation.extraction(resources, population, populationMax, expenses, extractionLevel);
                curve[0][i - 1] = population;
                curve[1][i - 1] = extracted;
            }
            String curveName = String.format("Level %s", extractionLevel);
            addCurve(curveName, curve);
        }
    }
}
