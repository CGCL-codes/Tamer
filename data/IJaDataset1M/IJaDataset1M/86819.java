package procsim;

import java.awt.*;

public class GMTriState extends MTriState implements GraphicalElement {

    private int width = 30;

    private int height = 25;

    private int x, y;

    private int angle = 0;

    public GMTriState(MSignal in, Signal c) {
        super(in, c);
    }

    @Override
    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawIt(Graphics2D g, int x, int y) {
        Util.saveGraphics(g);
        g.setColor(Color.BLACK);
        if (angle == 0) {
            g.drawPolygon(new int[] { x + 1, x + width - 1, x + 1 }, new int[] { y + 1, y + height / 2, y + height - 1 }, 3);
            if (inverted) g.drawOval(x + width, y + height / 2 - 2, 4, 4);
        } else if (angle == 90) {
            g.drawPolygon(new int[] { x + 1, x + width - 1, x + width / 2 }, new int[] { y + 1, y + 1, y + height - 1 }, 3);
            if (inverted) g.drawOval(x + width / 2 - 2, y + height, 4, 4);
        } else if (angle == 180) {
            g.drawPolygon(new int[] { x + 1, x + width - 1, x + width - 1 }, new int[] { y + height / 2, y + 1, y + height - 1 }, 3);
            if (inverted) g.drawOval(x - 4, y + height / 2 - 2, 4, 4);
        } else if (angle == 270) {
            g.drawPolygon(new int[] { x + 1, x + width / 2, x + width - 1 }, new int[] { y + height - 1, y + 1, y + height - 1 }, 3);
            if (inverted) g.drawOval(x + width / 2 - 2, y - 4, 4, 4);
        }
        Util.restoreGraphics(g);
    }

    @Override
    public void drawIt(Graphics2D g) {
        drawIt(g, x, y);
    }

    public GMTriState rotate(int angle) {
        this.angle = angle;
        if (angle == 90 || angle == 270) setSize(25, 30);
        return this;
    }

    public Coords getResCoords(int length) {
        int spacing = 0;
        Coords coords = new Coords();
        if (isInverted()) spacing += 4;
        if (angle == 0) coords.setAll(x + getWidth() + spacing, y + getHeight() / 2, x + getWidth() + spacing + length, y + getHeight() / 2); else if (angle == 90) coords.setAll(x + getWidth() / 2, y + getHeight() + spacing, x + getWidth() / 2, y + getHeight() + spacing + length); else if (angle == 180) coords.setAll(x - spacing - length, y + getHeight() / 2, x - spacing, y + getHeight() / 2); else if (angle == 270) coords.setAll(x + getWidth() / 2, y - spacing - length, x + getWidth() / 2, y - spacing);
        return coords;
    }

    public Coords getInCoords(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y + getHeight() / 2, x, y + getHeight() / 2); else if (angle == 90) coords.setAll(x + getWidth() / 2, y - length, x + getWidth() / 2, y); else if (angle == 180) coords.setAll(x + getWidth() + length, y + getHeight() / 2, x + getWidth(), y + getHeight() / 2); else if (angle == 270) coords.setAll(x + getWidth() / 2, y + height + length, x + getWidth() / 2, y + getHeight());
        return coords;
    }

    public Coords getCtrlCoords(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x + getWidth() / 2, y - length, x + getWidth() / 2, y + 6); else if (angle == 90) coords.setAll(x - length, y + getHeight() / 2, x + 6, y + getHeight() / 2); else if (angle == 180) coords.setAll(x + getWidth() / 2, y - length, x + getWidth() / 2, y + 6); else if (angle == 270) coords.setAll(x - length, y + getHeight() / 2, x + 6, y + getHeight() / 2);
        return coords;
    }
}
