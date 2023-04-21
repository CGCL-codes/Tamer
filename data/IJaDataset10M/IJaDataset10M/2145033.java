package net.sourceforge.plantuml.eps;

public class PostScriptCommandQuadTo implements PostScriptCommand {

    private final double x1;

    private final double y1;

    private final double x2;

    private final double y2;

    public PostScriptCommandQuadTo(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public String toPostString() {
        return EpsGraphics.format(x1) + " " + EpsGraphics.format(y1) + " " + EpsGraphics.format(x2) + " " + EpsGraphics.format(y2) + " rquadto";
    }
}
