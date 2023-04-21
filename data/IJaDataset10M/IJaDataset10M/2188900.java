package chart;

import java.util.Observable;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import database.DataModel;

public class TimeSeriesView implements View {

    private JFreeChart chart = null;

    private ChartPanel panel = null;

    public JFreeChart getChart() {
        return chart;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void update(Observable o, Object arg) {
        if (o instanceof DataModel) {
            DataModel model = (DataModel) o;
            TimeSeriesCollection collection = new TimeSeriesCollection();
            collection.addSeries(model.getCloseSeries());
            collection.addSeries(MovingAverage.createMovingAverage(model.getCloseSeries(), "50 days", 50, 49));
            collection.addSeries(MovingAverage.createMovingAverage(model.getCloseSeries(), "200 days", 200, 199));
            chart = ChartFactory.createTimeSeriesChart(arg.toString(), "time", "price", collection, true, true, true);
            if (panel != null) panel.setChart(chart); else panel = new ChartPanel(chart);
        }
    }
}
