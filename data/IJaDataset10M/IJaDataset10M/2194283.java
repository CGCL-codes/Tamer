package plot;

import org.rosuda.JRI.Rengine;
import r.RDriver;

/**
 * Class provides plotting fuctionality. This class is not supposed to be extended.
 * @author bhushan_kshirsagar
 *
 */
public final class PlottingUtility {

    private final Rengine rengine = null;

    private int graphWidth = 600;

    private int graphHeight = 300;

    /**
	 * creates object for specified workspace where all the images are stored.
	 * @param workspace 
	 */
    public PlottingUtility(String workspace) {
    }

    /**
	 * creates bar plot
	 * @param fileName image file name (e.g. abc.jpeg)
	 * @param plotName : main heading of the plot
	 * @param xLabel : x axis label
	 * @param yLabel : y axis label
	 * @param barNames : name of each bar
	 * @param data : frequency values.
	 */
    public void plotBarPlot(String fileName, String plotName, String xLabel, String yLabel, String[] barNames, int[] data) {
        System.out.println("BarPlot is called");
    }

    /**
	 * creates Histograme
	 * @param fileName : image file name (e.g. abc.jpeg)
	 * @param plotName : main heading of the plot
	 * @param xLabel : x axis label
	 * @param yLabel : y axis label
	 * @param numberOfBreaks : break points in the data(10,20,30,50 or number of breaks (6))
	 * @param data : data points for which histogram to be plotted.
	 */
    public void plotHistogram(String fileName, String plotName, String xLabel, String yLabel, double[] numberOfBreaks, double[] data) {
        System.out.println("Histogram is called");
    }

    /**
	 * Creates Barcode like plot. This plot indicates position specific property occurance.
	 * plot gives good results upto 3 catagories.
	 * @param fileName : image file name (e.g. abc.jpeg)
	 * @param plotName : main heading of the plot
	 * @param xLabel : x axis label
	 * @param yLabel : y axis label
	 * @param data catagorial data specified in term of interger. (1,0,1)
	 */
    public void plotCombPlot(String fileName, String plotName, String xLabel, String yLabel, int[] data) {
    }

    /**
	 * Creates scatter plot. small points on plot represent indivisual data points while large point indicates
	 * average value.
	 * @param fileName : image file name (e.g. abc.jpeg)
	 * @param plotName : main heading of the plot
	 * @param xLabel : x axis label
	 * @param yLabel : y axis label
	 * @param data : Average value data
	 * @param indivisualDataPointsPerFrame : indivisual data value
	 */
    public void plotScatterPlot(String fileName, String plotName, String xLabel, String yLabel, double[] data, double[][] indivisualDataPointsPerFrame) {
        System.out.println("Scatter plot is called");
    }

    /**
	 * returns file width.
	 * @return int
	 */
    public int getGraphWidth() {
        return graphWidth;
    }

    /**
	 * Sets file width
	 * @param graphWidth int
	 */
    public void setGraphWidth(int graphWidth) {
        this.graphWidth = graphWidth;
    }

    /**
	 * returns file Height.
	 * @return int
	 */
    public int getGraphHeight() {
        return graphHeight;
    }

    /**
	 * Sets file height
	 * @param graphHeight int
	 */
    public void setGraphHeight(int graphHeight) {
        this.graphHeight = graphHeight;
    }
}
