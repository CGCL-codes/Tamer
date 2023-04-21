package csiebug.web.html.chart.jqPlotBean;

/**
 * @author George_Tsai
 * @version 2010/6/1
 */
public class JQPlotCandlestickPoint extends JQPlotPoint {

    private String yOpen;

    private String yHigh;

    private String yLow;

    private String yClose;

    public void setYOpen(String yOpen) {
        this.yOpen = yOpen;
    }

    public String getYOpen() {
        return yOpen;
    }

    public void setYHigh(String yHigh) {
        this.yHigh = yHigh;
    }

    public String getYHigh() {
        return yHigh;
    }

    public void setYLow(String yLow) {
        this.yLow = yLow;
    }

    public String getYLow() {
        return yLow;
    }

    public void setYClose(String yClose) {
        this.yClose = yClose;
    }

    public String getYClose() {
        return yClose;
    }

    public String getY() {
        return yOpen + ", " + yHigh + ", " + yLow + ", " + yClose;
    }
}
