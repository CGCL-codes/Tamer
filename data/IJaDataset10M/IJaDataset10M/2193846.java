package net.sourceforge.plantuml;

public class ScaleSimple implements Scale {

    private final double scale;

    public ScaleSimple(double scale) {
        this.scale = scale;
    }

    public double getScale(double width, double height) {
        return scale;
    }
}
