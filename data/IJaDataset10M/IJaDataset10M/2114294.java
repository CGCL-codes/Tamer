package icons;

import java.awt.*;
import constants.GlobalDebug;
import figures.Line;
import figures.WBShape;

public class LineDrawingIcon extends DrawingIcon {

    public LineDrawingIcon() {
        super();
        if (GlobalDebug.isOn) System.out.println("LineDrawingIcon.LineDrawingIcon()");
    }

    public WBShape createShape(int x1, int y1, int x2, int y2) {
        if (GlobalDebug.isOn) System.out.println("LineDrawingIcon.createShape()");
        return new Line(x1, y1, x2, y2);
    }

    public String getCommand() {
        if (GlobalDebug.isOn) System.out.println("LineDrawingIcon.getCommand()");
        return "Line";
    }

    public static void main(String argv[]) {
        Frame f = new Frame("Test Frame");
        f.add(new LineDrawingIcon());
        f.setVisible(true);
        f.setLocation(100, 100);
        f.pack();
        f.show();
    }
}
