package br.usp.pulga.iteration;

import java.awt.Color;
import br.usp.pulga.Iteration;

public class BorderListener implements IterationListener {

    private final PGraphics graphics;

    private final World xWorld;

    private final World yWorld;

    private final Axis y;

    private final Axis x;

    public BorderListener(PGraphics graphics, World xWorld, World yWorld, Axis x, Axis y) {
        this.graphics = graphics;
        this.xWorld = xWorld;
        this.yWorld = yWorld;
        this.x = x;
        this.y = y;
    }

    public void after(Iteration iteration) {
    }

    public void before(Iteration iteration) {
    }

    public void init(Iteration iteration) {
        graphics.setColor(Color.BLACK);
        double zeroY = Math.max(0, yWorld.locate(0, y));
        graphics.line(0, zeroY, graphics.height(), zeroY);
        graphics.write("" + x.max(), graphics.width(), zeroY + 2, Alignment.RIGHT, Alignment.BOTTOM);
        graphics.write("" + x.min(), 2, zeroY + 2, Alignment.LEFT, Alignment.TOP);
        double zeroX = Math.max(0, xWorld.locate(0, x));
        graphics.line(zeroX, 0, zeroX, graphics.width());
        graphics.write("" + y.max(), zeroX + 2, graphics.height(), Alignment.LEFT, Alignment.TOP);
        graphics.write("" + y.min(), zeroX + 2, 2, Alignment.LEFT, Alignment.BOTTOM);
    }
}
