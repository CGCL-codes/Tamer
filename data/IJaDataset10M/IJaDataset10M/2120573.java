package ca.sqlpower.wabit.swingui.chart.effect;

/**
 * Provides some useful features for implementations of
 * {@link ChartAnimatorFactory} to inherit.
 */
public abstract class AbstractChartAnimatorFactory implements ChartAnimatorFactory {

    /**
     * Number of milliseconds between frames.
     */
    private int frameDelay = 20;

    /**
     * Number of frames the chart animation should play for.
     */
    private int frameCount = 30;

    public int getFrameDelay() {
        return frameDelay;
    }

    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }
}
