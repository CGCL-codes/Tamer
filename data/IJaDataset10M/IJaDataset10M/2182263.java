package com.ds.theme;

import java.awt.*;

public class BlueColorTheme implements ColorTheme {

    private Paint backgoundPaint = null;

    private Paint darkerBackgoundPaint = null;

    private Paint brighterBackgroundPaint = null;

    private Paint backgroundSelectedPaint = null;

    private Paint foregroundPaint = null;

    private Paint foregroundSelectedPaint = null;

    private Paint foregroundDisabledPaint = null;

    private Color backgroundColor1 = new Color(138, 161, 177);

    private Color backgroundColor2 = new Color(199, 217, 229);

    public BlueColorTheme() {
        this(new Dimension());
    }

    public BlueColorTheme(Dimension dim) {
        update(dim);
        this.foregroundPaint = new Color(77, 77, 77);
        this.backgroundSelectedPaint = this.backgoundPaint;
        this.foregroundSelectedPaint = Color.BLACK;
        this.foregroundDisabledPaint = new Color(185, 185, 185);
    }

    public void update(Dimension dim) {
        this.backgoundPaint = new GradientPaint(dim.width / 2, 0, backgroundColor1, dim.width / 2, dim.height, backgroundColor2, false);
        this.darkerBackgoundPaint = new GradientPaint(dim.width / 2, 0, backgroundColor1.darker(), dim.width / 2, dim.height, backgroundColor2.darker(), false);
        this.brighterBackgroundPaint = new GradientPaint(dim.width / 2, 0, backgroundColor1.brighter(), dim.width / 2, dim.height, backgroundColor2.brighter(), false);
    }

    public Paint darkerBackgroundPaint() {
        return darkerBackgoundPaint;
    }

    public Paint brighterBackgroundPaint() {
        return brighterBackgroundPaint;
    }

    public Paint getBackgroundPaint() {
        return this.backgoundPaint;
    }

    public Paint getForegroundPaint() {
        return this.foregroundPaint;
    }

    public Paint getBackgroundSelectedPaint() {
        return this.backgroundSelectedPaint;
    }

    public Paint getForegroundSelectedPaint() {
        return this.foregroundSelectedPaint;
    }

    public Paint getForegroundDisabledPaint() {
        return this.foregroundDisabledPaint;
    }
}
