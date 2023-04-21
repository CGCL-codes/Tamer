package org.spantus.chart.marker;

/**
 * 
 * @author Mindaugas Greibus
 * @since 0.0.1
 * Created on Feb 22, 2009
 */
public abstract class MarkerComponentUtil {

    public static int timeToScreen(MarkerGraphCtx ctx, Long val) {
        Double offset = ctx.getXOffset().doubleValue();
        Double startX = (val) / 1000.0;
        startX -= offset;
        return (int) ((startX / ctx.getXScalar().doubleValue()));
    }

    public static Long screenToTime(MarkerGraphCtx ctx, int val) {
        Double offset = ctx.getXOffset().doubleValue();
        Double start = (val) * 1000.0 * ctx.getXScalar().doubleValue();
        start += offset;
        return start.longValue();
    }
}
