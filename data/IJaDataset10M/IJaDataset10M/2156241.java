package com.isa.jump.plugin;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.cursortool.MultiClickTool;

public class MeasureM_FTool extends MultiClickTool {

    private List savedCoordinates = new ArrayList();

    private Coordinate currCoord;

    public MeasureM_FTool() {
        allowSnapping();
    }

    public Icon getIcon() {
        return new ImageIcon(getClass().getResource("RulerM_F.gif"));
    }

    public Cursor getCursor() {
        for (int i = 0; i < savedCoordinates.size(); i++) {
            add((Coordinate) savedCoordinates.get(i));
        }
        return createCursor(new ImageIcon(getClass().getResource("RulerCursorM_F.gif")).getImage());
    }

    public void mouseLocationChanged(MouseEvent e) {
        try {
            if (isShapeOnScreen()) {
                ArrayList currentCoordinates = new ArrayList(getCoordinates());
                currentCoordinates.add(getPanel().getViewport().toModelCoordinate(e.getPoint()));
                display(currentCoordinates, getPanel());
            }
            currCoord = snap(e.getPoint());
            super.mouseLocationChanged(e);
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        savedCoordinates = new ArrayList(getCoordinates());
    }

    protected void gestureFinished() throws NoninvertibleTransformException {
        reportNothingToUndoYet();
        savedCoordinates.clear();
        display(getCoordinates(), getPanel());
    }

    private void display(List coordinates, LayerViewPanel panel) throws NoninvertibleTransformException {
        display(distance(coordinates), panel);
    }

    private void display(double distance, LayerViewPanel panel) {
        DecimalFormat df3 = new DecimalFormat("###,###,##0.0##");
        String distString = df3.format(distance / 0.3048);
        panel.getContext().setStatusMessage("Distance: " + panel.format(distance) + " meters" + " = " + distString + " feet");
    }

    private double distance(List coordinates) {
        double distance = 0;
        for (int i = 1; i < coordinates.size(); i++) {
            distance += ((Coordinate) coordinates.get(i - 1)).distance((Coordinate) coordinates.get(i));
        }
        if ((currCoord != null) && (coordinates.size() > 1)) {
            distance -= ((Coordinate) coordinates.get(coordinates.size() - 2)).distance((Coordinate) coordinates.get(coordinates.size() - 1));
            distance += ((Coordinate) coordinates.get(coordinates.size() - 2)).distance(currCoord);
        }
        return distance;
    }
}
