package com.global360.sketchpadbpmn.graphic.symbol;

import java.awt.geom.*;
import com.global360.sketchpadbpmn.graphic.SketchGraphic;

public class Signal extends Symbol {

    public Signal() {
        name = "Signal";
        type = Symbol.SIGNAL;
    }

    protected GeneralPath makeShape() {
        GeneralPath result = new GeneralPath(SketchGraphic.windingRule);
        float centerX = this.naturalWidth / 2;
        float centerY = this.naturalHeight / 2;
        centerY -= 4;
        float size = 34;
        result.moveTo(centerX, centerY - size);
        result.lineTo(centerX + size, centerY + size);
        result.lineTo(centerX - size, centerY + size);
        result.lineTo(centerX, centerY - size);
        return result;
    }

    @Override
    public void makeShapes() {
        addPath(makeShape());
    }
}
