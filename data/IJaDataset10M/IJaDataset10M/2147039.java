package icons;

import java.awt.*;
import constants.GlobalDebug;
import figures.WBShape;
import figures.Smily;

public class SmilyDrawingIcon extends DrawingIcon {

    public SmilyDrawingIcon() {
        super();
        if (GlobalDebug.isOn) System.out.println("SmilyDrawingIcon.RectangleDrawingIcon()");
    }

    public WBShape createShape(int x1, int y1, int x2, int y2) {
        if (GlobalDebug.isOn) System.out.println("SmilyDrawingIcon.createShape()");
        return new Smily(x1, y1, x2, y2);
    }

    public String getCommand() {
        if (GlobalDebug.isOn) System.out.println("SmilyDrawingIcon.getCommand()");
        return "Smily";
    }
}
