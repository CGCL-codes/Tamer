package br.com.mackenzie.fuzzy.bellmanzadeh.mf;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import br.com.mackenzie.fuzzy.bellmanzadeh.RunForm;

public class SigmoidalMembershipFunction extends MembershipFunction {

    public SigmoidalMembershipFunction() {
    }

    public SigmoidalMembershipFunction(String pathToImage) {
        super("sigmf", pathToImage);
    }

    public double compute() {
        double a = this.getParameters()[0];
        double c = this.getParameters()[1];
        double e = Math.E;
        return (1 / (1 + (Math.pow(e, -(a * (getX() - c))))));
    }

    public void showChart() {
        XYSeries series = new XYSeries("sigmf(x, " + this.getParameters().toString() + ")");
        for (double x = 0; x < 10; x += 0.1) {
            this.setX(x);
            series.add(x, this.compute());
        }
        XYSeriesCollection data = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart("SIGMF CHART", "X", "Y", data, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel panel = new ChartPanel(chart);
        RunForm.runPanel(panel);
    }

    public static void main(String[] args) {
        SigmoidalMembershipFunction mf = new SigmoidalMembershipFunction();
        mf.setParameters(new double[] { 2, 5 });
        mf.showChart();
    }
}
