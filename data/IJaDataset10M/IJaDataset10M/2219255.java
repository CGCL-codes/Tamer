package org.gdal.ogr.igor.gui;

import java.awt.Component;
import java.awt.Dimension;

public class OgrGuiUtility {

    private OgrGuiUtility() {
    }

    public static void centreWindow(Component componentToMove, Component componentToCentreOn) {
        Dimension componentToCentreOnSize = componentToCentreOn.getSize();
        componentToMove.setLocation(componentToCentreOn.getX() + ((componentToCentreOnSize.width - componentToMove.getWidth()) / 2), componentToCentreOn.getY() + ((componentToCentreOnSize.height - componentToMove.getHeight()) / 2));
    }
}
