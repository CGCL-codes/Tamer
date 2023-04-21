package com.siema.games.freeland.lib;

import org.anddev.andengine.entity.shape.Shape;
import com.siema.games.freeland.main.Stock;

public class RelativeLayoutHelper {

    public static float[] percentPositionToPixels(float[] c) {
        return RelativeLayoutHelper.percentPositionToPixels(c[0], c[1]);
    }

    public static float[] percentPositionToPixels(float x, float y) {
        return new float[] { RelativeLayoutHelper.percentPositionTopixelsX(x), RelativeLayoutHelper.percentPositionTopixelsY(y) };
    }

    public static float percentPositionTopixelsX(float x) {
        return Stock.getInstance().CAMERA_WIDTH * (x * .01f);
    }

    public static float percentPositionTopixelsY(float y) {
        return Stock.getInstance().CAMERA_HEIGHT * (y * .01f);
    }

    public static float[] centerOffsetToGlobalPosition(float[] c) {
        return RelativeLayoutHelper.centerOffsetToGlobalPosition(c[0], c[1]);
    }

    public static float[] centerOffsetToGlobalPosition(float x, float y) {
        return new float[] { Stock.getInstance().cameraCenterX + x, Stock.getInstance().cameraCenterY + y };
    }

    public static void centerShapeOnCoords(Shape s) {
        RelativeLayoutHelper.centerShapeOnCoords(s, s.getX(), s.getY());
    }

    public static void centerShapeOnCoords(Shape s, float[] c) {
        RelativeLayoutHelper.centerShapeOnCoords(s, c[0], c[1]);
    }

    public static void centerShapeOnCoords(Shape s, float x, float y) {
        s.setPosition(x - s.getWidthScaled() * .5f, y - s.getHeightScaled() * .5f);
    }

    public static void centerShapeOnScreen(Shape s) {
        RelativeLayoutHelper.centerShapeOnCoords(s, Stock.getInstance().cameraCenterX, Stock.getInstance().cameraCenterY);
    }
}
