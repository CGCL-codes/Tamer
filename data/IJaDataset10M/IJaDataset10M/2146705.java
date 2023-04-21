package ca.sqlpower.wabit.report.chart;

import javax.annotation.Nonnull;
import net.jcip.annotations.Immutable;

/**
 * Event object that carries notifications about a change in chart data which
 * was not necessarily caused by a change in the chart's underlying result set.
 * This is especially useful for the chart to request a repaint while following
 * streaming queries.
 */
@Immutable
public class ChartDataChangedEvent {

    private final Chart source;

    /**
     * @param source
     */
    public ChartDataChangedEvent(@Nonnull Chart source) {
        if (source == null) {
            throw new NullPointerException("Null source not allowed");
        }
        this.source = source;
    }

    @Nonnull
    public Chart getSource() {
        return source;
    }
}
