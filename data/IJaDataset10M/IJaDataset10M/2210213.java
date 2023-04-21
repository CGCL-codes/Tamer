package actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import views.LineChartView;
import chart.ChartObjectCollection;
import chart.ChartObjectFactory;
import chart.IChartObjectCollection;
import chart.LineChart;

/**
 * Jan 24, 2006
 * @author Dan Phifer
 * @version 1.0
 * 
 * A single class to handle menu actions.  The id defined in the plugin.xml class
 * is passed to the run method by the framework and the appropriate method is
 * called in the view.  
 * <P>
 * A reference to the view is passed by the framework during initialization 
 */
public class LineChartActionDelegate implements IViewActionDelegate {

    LineChartView lineChartView;

    private FileDialog fileDialog;

    public void init(IViewPart view) {
        if (view instanceof LineChartView) {
            lineChartView = (LineChartView) view;
        }
    }

    public void run(IAction action) {
        if (lineChartView == null) return;
        String id = action.getId();
        LineChart lineChart = lineChartView.getChart();
        if (id.equals("autoZoom")) {
            lineChart.zoomToFit(true, true);
        } else if (id.equals("showVertical")) {
            lineChart.setShowVerticalGridLines(action.isChecked());
        } else if (id.equals("showHorizontal")) {
            lineChart.setShowHorizontalGridLines(action.isChecked());
        } else if (id.equals("smoothZoom")) {
            lineChart.setSmoothZooming(action.isChecked());
        } else if (id.equals("showLegend")) {
            lineChart.setLegendVisible(action.isChecked());
        } else if (id.equals("showTitle")) {
            lineChart.setTitleVisible(action.isChecked());
        } else if (id.equals("showXAxis")) {
            lineChart.setXAxisVisible(action.isChecked());
        } else if (id.equals("showXAxisLabel")) {
            lineChart.setXAxisTitleVisible(action.isChecked());
        } else if (id.equals("showYAxis")) {
            lineChart.setYAxisVisible(action.isChecked());
        } else if (id.equals("showYAxisLabel")) {
            lineChart.setYAxisTitleVisible(action.isChecked());
        } else if (id.equals("autoFitXAxis")) {
            lineChart.setAutoFitXAxis(action.isChecked());
        } else if (id.equals("autoFitYAxis")) {
            lineChart.setAutoFitYAxis(action.isChecked());
        } else if (id.equals("save")) {
            FileDialog d = getFileDialog();
            String fileName = d.open();
            if (fileName != null && !fileName.equals("")) lineChart.saveScreenshot(fileName);
            return;
        } else if (id.equals("export")) {
            IChartObjectCollection collection = new ChartObjectCollection(lineChart.getSeriesObjects());
            FileDialog d = getFileDialog();
            String fileName = d.open();
            if (fileName != null && !fileName.equals("")) {
                OutputStream out;
                try {
                    out = new FileOutputStream(new File(fileName));
                    ChartObjectFactory.exportChart(collection, out, "\n", "\t");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        lineChart.repaint();
    }

    /**
	 * @return
	 */
    private FileDialog getFileDialog() {
        if (fileDialog == null) fileDialog = new FileDialog(lineChartView.getSite().getShell());
        return fileDialog;
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
