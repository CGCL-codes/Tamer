package net.tourbook.ui.tourChart;

import net.tourbook.chart.Chart;
import net.tourbook.chart.ChartDataYSerie;
import net.tourbook.chart.GraphDrawingData;
import net.tourbook.chart.IChartLayer;
import net.tourbook.data.TourData;
import net.tourbook.tour.TourManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ChartSegmentValueLayer implements IChartLayer {

    private RGB lineColorRGB = new RGB(255, 0, 0);

    private TourData _tourData;

    private float[] _xDataSerie;

    /**
	 * Draws the marker(s) for the current graph config
	 * 
	 * @param gc
	 * @param drawingData
	 * @param chartComponents
	 */
    public void draw(final GC gc, final GraphDrawingData drawingData, final Chart chart) {
        final int[] segmentSerie = _tourData.segmentSerieIndex;
        if (segmentSerie == null) {
            return;
        }
        final Display display = Display.getCurrent();
        final int devYTop = drawingData.getDevYTop();
        final int devYBottom = drawingData.getDevYBottom();
        final int devGraphImageXOffset = chart.getXXDevViewPortLeftBorder();
        final float graphYBottom = drawingData.getGraphYBottom();
        final ChartDataYSerie yData = drawingData.getYData();
        final Object segmentValuesObject = yData.getCustomData(TourManager.CUSTOM_DATA_SEGMENT_VALUES);
        if ((segmentValuesObject instanceof float[]) == false) {
            return;
        }
        final float[] segmentValues = (float[]) segmentValuesObject;
        final int valueDivisor = yData.getValueDivisor();
        final float scaleX = drawingData.getScaleX();
        final float scaleY = drawingData.getScaleY();
        final Color lineColor = new Color(display, lineColorRGB);
        Point lastPoint = null;
        for (int segmentIndex = 0; segmentIndex < segmentSerie.length; segmentIndex++) {
            final int serieIndex = segmentSerie[segmentIndex];
            final int devXOffset = (int) (_xDataSerie[serieIndex] * scaleX) - devGraphImageXOffset;
            final float graphYValue = segmentValues[segmentIndex] * valueDivisor;
            final int devYGraph = (int) ((graphYValue - graphYBottom) * scaleY);
            int devYMarker = devYBottom - devYGraph;
            if (devYMarker > devYBottom) {
                devYMarker = devYBottom;
            }
            if (devYMarker < devYTop) {
                devYMarker = devYTop;
            }
            if (lastPoint == null) {
                lastPoint = new Point(devXOffset, devYMarker);
            } else {
                gc.setForeground(lineColor);
                gc.setLineStyle(SWT.LINE_DOT);
                gc.drawLine(lastPoint.x, lastPoint.y, lastPoint.x, devYMarker);
                gc.setLineStyle(SWT.LINE_SOLID);
                gc.drawLine(lastPoint.x, devYMarker, devXOffset, devYMarker);
                lastPoint.x = devXOffset;
                lastPoint.y = devYMarker;
            }
            gc.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
            gc.setLineStyle(SWT.LINE_DOT);
            gc.drawLine(devXOffset, devYMarker, devXOffset, devYTop);
        }
        lineColor.dispose();
    }

    public void setLineColor(final RGB lineColor) {
        this.lineColorRGB = lineColor;
    }

    public void setTourData(final TourData tourData) {
        _tourData = tourData;
    }

    public void setXDataSerie(final float[] dataSerie) {
        _xDataSerie = dataSerie;
    }
}
