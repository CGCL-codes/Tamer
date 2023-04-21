package equilibrium.commons.report.chart;

public class SpiderChartDataset extends EasyReportsChartDataset {

    private String[] valuesLabels;

    private double[] values;

    public SpiderChartDataset(double[] values, String[] labels) {
        this.values = values;
        this.valuesLabels = labels;
    }

    public String[] getValuesLabels() {
        return valuesLabels;
    }

    public double[] getValues() {
        return values;
    }
}
