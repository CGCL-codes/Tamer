package com.yosimite;

import java.awt.Color;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Axis extends Shape3D {

    public Axis() {
        this.setGeometry(createGeometry());
        this.setAppearance(createAppearance());
    }

    /**
	 * Create Appearance for the object
	 * @return
	 */
    protected Appearance createAppearance() {
        Appearance appearance = new Appearance();
        LineAttributes la = new LineAttributes();
        la.setLineWidth(1.5f);
        la.setLinePattern(LineAttributes.PATTERN_DOT);
        la.setLineAntialiasingEnable(true);
        Color3f objColor = new Color3f(0.9f, 0.9f, 1.0f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        ColoringAttributes ca = new ColoringAttributes(objColor, ColoringAttributes.SHADE_FLAT);
        appearance.setColoringAttributes(ca);
        return appearance;
    }

    private Geometry createGeometry() {
        IndexedLineArray axisLines = new IndexedLineArray(18, GeometryArray.COORDINATES | LineArray.COLOR_3, 30);
        final Color3f red = new Color3f(Color.red);
        final Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
        final Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
        Color3f[] colors = new Color3f[] { red, green, blue };
        axisLines.setCoordinate(0, new Point3f(-1.0f, 0.0f, 0.0f));
        axisLines.setCoordinate(1, new Point3f(1.0f, 0.0f, 0.0f));
        axisLines.setCoordinate(2, new Point3f(0.9f, 0.1f, 0.1f));
        axisLines.setCoordinate(3, new Point3f(0.9f, -0.1f, 0.1f));
        axisLines.setCoordinate(4, new Point3f(0.9f, 0.1f, -0.1f));
        axisLines.setCoordinate(5, new Point3f(0.9f, -0.1f, -0.1f));
        axisLines.setCoordinate(6, new Point3f(0.0f, -1.0f, 0.0f));
        axisLines.setCoordinate(7, new Point3f(0.0f, 1.0f, 0.0f));
        axisLines.setCoordinate(8, new Point3f(0.1f, 0.9f, 0.1f));
        axisLines.setCoordinate(9, new Point3f(-0.1f, 0.9f, 0.1f));
        axisLines.setCoordinate(10, new Point3f(0.1f, 0.9f, -0.1f));
        axisLines.setCoordinate(11, new Point3f(-0.1f, 0.9f, -0.1f));
        axisLines.setCoordinate(12, new Point3f(0.0f, 0.0f, -1.0f));
        axisLines.setCoordinate(13, new Point3f(0.0f, 0.0f, 1.0f));
        axisLines.setCoordinate(14, new Point3f(0.1f, 0.1f, 0.9f));
        axisLines.setCoordinate(15, new Point3f(-0.1f, 0.1f, 0.9f));
        axisLines.setCoordinate(16, new Point3f(0.1f, -0.1f, 0.9f));
        axisLines.setCoordinate(17, new Point3f(-0.1f, -0.1f, 0.9f));
        axisLines.setCoordinateIndex(0, 0);
        axisLines.setCoordinateIndex(1, 1);
        axisLines.setCoordinateIndex(2, 2);
        axisLines.setCoordinateIndex(3, 1);
        axisLines.setCoordinateIndex(4, 3);
        axisLines.setCoordinateIndex(5, 1);
        axisLines.setCoordinateIndex(6, 4);
        axisLines.setCoordinateIndex(7, 1);
        axisLines.setCoordinateIndex(8, 5);
        axisLines.setCoordinateIndex(9, 1);
        axisLines.setCoordinateIndex(10, 6);
        axisLines.setCoordinateIndex(11, 7);
        axisLines.setCoordinateIndex(12, 8);
        axisLines.setCoordinateIndex(13, 7);
        axisLines.setCoordinateIndex(14, 9);
        axisLines.setCoordinateIndex(15, 7);
        axisLines.setCoordinateIndex(16, 10);
        axisLines.setCoordinateIndex(17, 7);
        axisLines.setCoordinateIndex(18, 11);
        axisLines.setCoordinateIndex(19, 7);
        axisLines.setCoordinateIndex(20, 12);
        axisLines.setCoordinateIndex(21, 13);
        axisLines.setCoordinateIndex(22, 14);
        axisLines.setCoordinateIndex(23, 13);
        axisLines.setCoordinateIndex(24, 15);
        axisLines.setCoordinateIndex(25, 13);
        axisLines.setCoordinateIndex(26, 16);
        axisLines.setCoordinateIndex(27, 13);
        axisLines.setCoordinateIndex(28, 17);
        axisLines.setCoordinateIndex(29, 13);
        for (int i = 0; i <= 29; i++) {
            axisLines.setColor(i, colors[i % 3]);
        }
        return axisLines;
    }
}
