package ca.sqlpower.wabit.swingui.chart.effect;

import javax.annotation.Nonnull;
import org.jfree.chart.JFreeChart;

/**
 * Utility class for working with the chart animation system. Provides the
 * convenience method {@link #animateIfPossible(JFreeChart)} which makes a best
 * effort to animate any JFreeChart in a single, simple line of code.
 */
public class ChartAnimation {

    /**
     * All the factories tried by {@link #animateIfPossible(JFreeChart)} in
     * order to try and animate a chart.
     * <p>
     * <b>Note to maintainers</b>: some animator factories will be highly specialized
     * for a certain chart type, whereas others may be widely applicable. Be
     * sure to arrange this array so the specialized factories come earlier in
     * the list than the general ones, since
     * {@link #animateIfPossible(JFreeChart)} always uses the first factory that
     * accepts any given chart.
     */
    private static final ChartAnimatorFactory[] ANIMATOR_FACTORIES = new ChartAnimatorFactory[] { new PieChartAnimatorFactory(), new BarChartAnimatorFactory(), new ScatterChartAnimatorFactory() };

    /**
     * Makes a "best effort" attempt to apply an animation effect to the given
     * chart.
     * 
     * @param chart
     *            The chart to attempt to animate.
     * @return The best available animator for the given chart. The animator
     *         will have been started for you. It will eventually stop on its
     *         own, but you are free to call
     *         {@link ChartAnimator#stopAnimation()} on it at any time.
     *         <p>
     *         This method never returns null, but the selected animator is not
     *         guaranteed to have any visible effect on the chart.
     */
    @Nonnull
    public static ChartAnimator animateIfPossible(JFreeChart chart) {
        for (ChartAnimatorFactory f : ANIMATOR_FACTORIES) {
            if (f.canAnimate(chart)) {
                try {
                    ChartAnimator animator = f.createAnimator(chart);
                    animator.startAnimation();
                    return animator;
                } catch (CantAnimateException e) {
                    throw new RuntimeException("Animator factory " + f + " said it could animate " + chart + " but then threw an exception!", e);
                }
            }
        }
        return new StubChartAnimator();
    }
}
