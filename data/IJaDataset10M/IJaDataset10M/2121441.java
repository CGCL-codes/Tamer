package uk.co.caprica.vlcj.test.oop.buttons;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;

public class SubtitlesMediaButton extends BaseMediaButton {

    private static final long serialVersionUID = 1L;

    public static final String ACTION_COMMAND = "subtitles";

    @Override
    protected String getActionCommand() {
        return ACTION_COMMAND;
    }

    @Override
    protected Shape getShape() {
        Path2D shape = new Path2D.Double();
        shape.moveTo(0, 14);
        shape.lineTo(6, 20);
        shape.lineTo(6, 14);
        shape.lineTo(20, 14);
        shape.lineTo(20, 0);
        shape.lineTo(0, 0);
        shape.closePath();
        return shape;
    }

    @Override
    protected Paint getShapeActivePaint() {
        return new Color(64, 64, 64);
    }
}
