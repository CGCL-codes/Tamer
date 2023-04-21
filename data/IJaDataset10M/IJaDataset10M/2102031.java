package org.weasis.core.ui.graphic;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.weasis.core.api.gui.util.GeomUtil;
import org.weasis.core.api.gui.util.MathUtil;
import org.weasis.core.api.image.measure.MeasurementsAdapter;
import org.weasis.core.api.image.util.ImageLayer;
import org.weasis.core.ui.Messages;
import org.weasis.core.ui.util.MouseEventDouble;

public class ParallelLineGraphic extends AbstractDragGraphic {

    public static final Icon ICON = new ImageIcon(ParallelLineGraphic.class.getResource("/icon/22x22/draw-parallel.png"));

    public static final Measurement DISTANCE = new Measurement(Messages.getString("measure.distance"), 1, true, true, true);

    public static final Measurement ORIENTATION = new Measurement(Messages.getString("measure.orientation"), 2, true, true, false);

    public static final Measurement AZIMUTH = new Measurement(Messages.getString("measure.azimuth"), 3, true, true, false);

    protected Point2D ptA, ptB, ptC, ptD;

    protected Point2D ptE, ptF;

    protected boolean lineABvalid, lineCDvalid;

    public ParallelLineGraphic(float lineThickness, Color paintColor, boolean labelVisible) {
        super(6, paintColor, lineThickness, labelVisible);
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public String getUIName() {
        return Messages.getString("measure.parallel");
    }

    @Override
    protected int moveAndResizeOnDrawing(int handlePointIndex, double deltaX, double deltaY, MouseEventDouble mouseEvent) {
        handlePointIndex = super.moveAndResizeOnDrawing(handlePointIndex, deltaX, deltaY, mouseEvent);
        if (handlePointIndex >= 0 && handlePointIndex < getHandlePointListSize()) {
            updateTool();
            if (lineABvalid && lineCDvalid) {
                if (handlePointIndex == 0 || handlePointIndex == 1) {
                    Point2D anchor = (handlePointIndex == 0) ? ptB : ptA;
                    double theta = GeomUtil.getSmallestAngleRad(GeomUtil.getAngleRad(ptC, ptD) - GeomUtil.getAngleRad(ptA, ptB));
                    AffineTransform rotate = AffineTransform.getRotateInstance(theta, anchor.getX(), anchor.getY());
                    rotate.transform(ptC, ptC);
                    rotate.transform(ptD, ptD);
                    setHandlePoint(2, ptC);
                    setHandlePoint(3, ptD);
                } else if (handlePointIndex == 2 || handlePointIndex == 3) {
                    Point2D pt1 = (handlePointIndex == 2) ? ptC : ptD;
                    Point2D pt2 = (handlePointIndex == 2) ? ptD : ptC;
                    int hIndex = (handlePointIndex == 2) ? 3 : 2;
                    Point2D ptI = GeomUtil.getPerpendicularPointToLine(ptA, ptB, pt1);
                    Point2D ptJ = GeomUtil.getPerpendicularPointToLine(ptA, ptB, pt2);
                    double transX = (pt1.getX() - ptI.getX()) - (pt2.getX() - ptJ.getX());
                    double transY = (pt1.getY() - ptI.getY()) - (pt2.getY() - ptJ.getY());
                    AffineTransform translate = AffineTransform.getTranslateInstance(transX, transY);
                    translate.transform(pt2, pt2);
                    setHandlePoint(hIndex, pt2);
                } else if (handlePointIndex == 4 || handlePointIndex == 5) {
                    Point2D pt0 = (handlePointIndex == 4) ? ptE : ptF;
                    Point2D pt1 = (handlePointIndex == 4) ? ptA : ptC;
                    Point2D pt2 = (handlePointIndex == 4) ? ptB : ptD;
                    int hIndex1 = (handlePointIndex == 4) ? 0 : 2;
                    int hIndex2 = (handlePointIndex == 4) ? 1 : 3;
                    if (pt0 != null) {
                        Point2D ptI = GeomUtil.getPerpendicularPointToLine(pt1, pt2, pt0);
                        AffineTransform translate = AffineTransform.getTranslateInstance(pt0.getX() - ptI.getX(), pt0.getY() - ptI.getY());
                        translate.transform(pt1, pt1);
                        translate.transform(pt2, pt2);
                        setHandlePoint(hIndex1, pt1);
                        setHandlePoint(hIndex2, pt2);
                    }
                }
                setHandlePoint(4, GeomUtil.getMidPoint(ptA, ptB));
                setHandlePoint(5, GeomUtil.getMidPoint(ptC, ptD));
            }
        }
        return handlePointIndex;
    }

    @Override
    protected void updateShapeOnDrawing(MouseEventDouble mouseEvent) {
        updateTool();
        Shape newShape = null;
        Path2D path = new Path2D.Double(Path2D.WIND_NON_ZERO, 2);
        if (lineABvalid) {
            path.append(new Line2D.Double(ptA, ptB), false);
        }
        if (lineCDvalid) {
            path.append(new Line2D.Double(ptC, ptD), false);
        }
        if (path.getCurrentPoint() != null) {
            newShape = path;
        }
        setShape(newShape, mouseEvent);
        updateLabel(mouseEvent, getDefaultView2d(mouseEvent));
    }

    @Override
    public List<MeasureItem> computeMeasurements(ImageLayer layer, boolean releaseEvent) {
        if (layer != null && layer.getSourceImage() != null && isShapeValid()) {
            MeasurementsAdapter adapter = layer.getSourceImage().getMeasurementAdapter();
            if (adapter != null) {
                ArrayList<MeasureItem> measVal = new ArrayList<MeasureItem>(3);
                if (DISTANCE.isComputed()) {
                    Double val = ptC.distance(GeomUtil.getPerpendicularPointToLine(ptA, ptB, ptC)) * adapter.getCalibRatio();
                    measVal.add(new MeasureItem(DISTANCE, val, adapter.getUnit()));
                }
                if (ORIENTATION.isComputed()) {
                    measVal.add(new MeasureItem(ORIENTATION, MathUtil.getOrientation(ptA, ptB), Messages.getString("measure.deg")));
                }
                if (AZIMUTH.isComputed()) {
                    measVal.add(new MeasureItem(AZIMUTH, MathUtil.getAzimuth(ptA, ptB), Messages.getString("measure.deg")));
                }
                return measVal;
            }
        }
        return null;
    }

    @Override
    public boolean isShapeValid() {
        updateTool();
        return (lineABvalid && lineCDvalid);
    }

    protected void updateTool() {
        ptA = getHandlePoint(0);
        ptB = getHandlePoint(1);
        ptC = getHandlePoint(2);
        ptD = getHandlePoint(3);
        ptE = getHandlePoint(4);
        ptF = getHandlePoint(5);
        lineABvalid = ptA != null && ptB != null && !ptB.equals(ptA);
        lineCDvalid = ptC != null && ptD != null && !ptC.equals(ptD);
    }

    @Override
    public List<Measurement> getMeasurementList() {
        List<Measurement> list = new ArrayList<Measurement>();
        list.add(DISTANCE);
        list.add(ORIENTATION);
        list.add(AZIMUTH);
        return list;
    }
}
