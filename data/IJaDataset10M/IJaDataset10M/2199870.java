package net.sourceforge.plantuml;

public class UmlDiagramInfo {

    private final double width;

    public UmlDiagramInfo() {
        this(0);
    }

    public UmlDiagramInfo(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }
}
