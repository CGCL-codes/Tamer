package org.openscience.jchempaint.controller;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Point2d;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;
import org.openscience.jchempaint.controller.RotateModule;
import org.openscience.jchempaint.controller.undoredo.IUndoRedoFactory;
import org.openscience.jchempaint.controller.undoredo.IUndoRedoable;
import org.openscience.jchempaint.controller.undoredo.UndoRedoHandler;
import org.openscience.jchempaint.renderer.BoundsCalculator;
import org.openscience.jchempaint.renderer.selection.IChemObjectSelection;

public class Rotate3DModule extends RotateModule {

    private double rotationAnglePhi;

    private double rotationAnglePsi;

    /**
     * Constructor 
     * @param chemModelRelay
     */
    public Rotate3DModule(IChemModelRelay chemModelRelay) {
        super(chemModelRelay);
        logger.debug("constructor");
    }

    /**
     * On mouse drag, quasi-3D rotation around the center is done
     * (It isn't real 3D rotation because of truncation of transformation
     * matrix to 2x2)
     */
    @Override
    public void mouseDrag(Point2d worldCoordFrom, Point2d worldCoordTo) {
        if (selectionMade) {
            rotationPerformed = true;
            final int SLOW_DOWN_FACTOR = 1;
            rotationAnglePhi += (worldCoordTo.x - worldCoordFrom.x) / SLOW_DOWN_FACTOR;
            rotationAnglePsi += (worldCoordTo.y - worldCoordFrom.y) / SLOW_DOWN_FACTOR;
            double cosinePhi = java.lang.Math.cos(rotationAnglePhi);
            double sinePhi = java.lang.Math.sin(rotationAnglePhi);
            double cosinePsi = java.lang.Math.cos(rotationAnglePsi);
            double sinePsi = java.lang.Math.sin(rotationAnglePsi);
            for (int i = 0; i < startCoordsRelativeToRotationCenter.length; i++) {
                double newX = startCoordsRelativeToRotationCenter[i].x * cosinePhi + startCoordsRelativeToRotationCenter[i].y * sinePhi * sinePsi;
                double newY = startCoordsRelativeToRotationCenter[i].y * cosinePsi;
                Point2d newCoords = new Point2d(newX + rotationCenter.x, newY + rotationCenter.y);
                selection.getConnectedAtomContainer().getAtom(i).setPoint2d(newCoords);
            }
        }
        chemModelRelay.updateView();
    }

    public String getDrawModeString() {
        return "Rotate in space";
    }
}
