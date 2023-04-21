package com.googlecode.client;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Markable charts are comprised of line charts, XY line charts, and scatter plots.
 * 
 * @author Julien Chastang
 * @author Silvin Lubecki
 */
abstract class AbstractMarkableChart extends AbstractAxisChart {

    private static final String MARKER_PARAM = "chm=";

    private final List rangeMarkers = new ArrayList();

    AbstractMarkableChart() {
        super();
    }

    /**
	 * Add a vertical range marker.
	 * 
	 * @param startPoint
	 *            the position on the x-axis at which the range starts where
	 *            0.00 is the bottom and 1.00 is the top.
	 * @param endPoint
	 *            the position on the x-axis at which the range ends where 0.00
	 *            is the bottom and 1.00 is the top.
	 * @param color
	 *            color of range marker.
	 */
    public void addVerticalRangeMarker(final float startPoint, final float endPoint, final Color color) {
        rangeMarkers.add(new VerticalRangeMarker(color, startPoint, endPoint));
    }

    /**
	 * Add a horizontal range marker
	 * 
	 * @param startPoint
	 *            the position on the y-axis at which the range starts where
	 *            0.00 is the bottom and 1.00 is the top.
	 * @param endPoint
	 *            the position on the y-axis at which the range ends where 0.00
	 *            is the bottom and 1.00 is the top.
	 * @param color
	 *            color of range marker.
	 */
    public void addHorizontalRangeMarker(final float startPoint, final float endPoint, final Color color) {
        rangeMarkers.add(new HorizontalRangeMarker(color, startPoint, endPoint));
    }

    /**
	 * Internal method to set the range markers.
	 * 
	 */
    protected void setRangeMarkers() {
        String rangeMarkerParam = "";
        int i = 0;
        for (Iterator it = rangeMarkers.iterator(); it.hasNext(); ) {
            RangeMarker marker = (RangeMarker) it.next();
            rangeMarkerParam += (i++ > 0) ? "|" + marker.toString() : marker.toString();
        }
        if (rangeMarkers.size() > 0) {
            addParameter(MARKER_PARAM, rangeMarkerParam);
        }
    }

    /**
	 * Internal method to set the shape markers.
	 * 
	 * @param shapeMarkerList 
	 */
    protected void setShapeMarkers(final List shapeMarkerList) {
        String shapeMarkerParam = "";
        int cnt = 0;
        for (Iterator it = shapeMarkerList.iterator(); it.hasNext(); ) {
            List shapeMarkers = (List) it.next();
            if (shapeMarkers == null) {
                cnt++;
                continue;
            }
            shapeMarkerParam += stringifyShapeMarkers(shapeMarkers, cnt);
            cnt++;
        }
        if (shapeMarkerParam.length() > 0) {
            addParameter(MARKER_PARAM, shapeMarkerParam.endsWith("|") ? shapeMarkerParam.substring(0, shapeMarkerParam.length() - 1) : shapeMarkerParam);
        }
    }

    /**
	 * @param shapeMarker
	 * @return
	 */
    private String stringifyShapeMarkers(final List shapeMarkers, final int index) {
        String shapeMarkerString = "";
        for (Iterator it = shapeMarkers.iterator(); it.hasNext(); ) {
            ShapeMarker shapeMarker = (ShapeMarker) it.next();
            for (int i = shapeMarker.getStartIndex(); i <= shapeMarker.getEndIndex(); i++) {
                final String s = shapeMarker.getShapeSymbol() + "," + shapeMarker.getColor() + "," + index + "," + i + "," + shapeMarker.getSize();
                shapeMarkerString += s + "|";
            }
        }
        return shapeMarkerString;
    }

    private static class RangeMarker {

        private final Color color;

        private final float startPoint;

        private final float endPoint;

        private RangeMarker(final Color color, final float startPoint, final float endPoint) {
            this.color = color;
            this.startPoint = startPoint;
            this.endPoint = endPoint;
        }

        /**
		 * @return the color
		 */
        protected Color getColor() {
            return color;
        }

        /**
		 * @return the startPoint
		 */
        protected float getStartPoint() {
            return startPoint;
        }

        /**
		 * @return the endPoint
		 */
        protected float getEndPoint() {
            return endPoint;
        }
    }

    private static class VerticalRangeMarker extends RangeMarker {

        private VerticalRangeMarker(final Color color, final float startPoint, final float endPoint) {
            super(color, startPoint, endPoint);
        }

        public String toString() {
            return "R," + getColor() + ",0," + getStartPoint() + "," + getEndPoint();
        }
    }

    private static class HorizontalRangeMarker extends RangeMarker {

        private HorizontalRangeMarker(final Color color, final float startPoint, final float endPoint) {
            super(color, startPoint, endPoint);
        }

        public String toString() {
            return "r," + getColor() + ",0," + getStartPoint() + "," + getEndPoint();
        }
    }
}
