package net.sourceforge.plantuml.ugraphic;

public class UStroke {

    private final double dashVisible;

    private final double dashSpace;

    private final double thickness;

    public UStroke(double dashVisible, double dashSpace, double thickness) {
        this.dashVisible = dashVisible;
        this.dashSpace = dashSpace;
        this.thickness = thickness;
    }

    public UStroke(double thickness) {
        this(0, 0, thickness);
    }

    public UStroke() {
        this(1.0);
    }

    public double getDashVisible() {
        return dashVisible;
    }

    public double getDashSpace() {
        return dashSpace;
    }

    public double getThickness() {
        return thickness;
    }

    public String getDasharraySvg() {
        if (dashVisible == 0) {
            return null;
        }
        return "" + dashVisible + "," + dashSpace;
    }
}
