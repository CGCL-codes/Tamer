package equilibrium.commons.report.chart;

public abstract class EasyReportsChartProperties {

    /**
     * Szerokość formatu wyjściowego.
     */
    protected int outputWidht = 760;

    /**
     * Wysokość formatu wyjściowego
     */
    protected int outputHeight = 450;

    public static final String BLANK_SPACE = " ";

    protected EasyReportsChartProperties() {
    }

    public int getOutputHeight() {
        return outputHeight;
    }

    public void setOutputHeight(int height) {
        this.outputHeight = height;
    }

    public int getOutputWidht() {
        return outputWidht;
    }

    public void setOutputWidht(int widht) {
        this.outputWidht = widht;
    }
}
