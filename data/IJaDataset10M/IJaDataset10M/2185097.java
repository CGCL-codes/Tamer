package procsim;

import java.awt.*;

public class GOR extends OR implements GraphicalElement {

    private int width = 27;

    private int height = 25;

    private int x, y;

    private int angle = 0;

    public GOR() {
        super();
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
        int reY1 = 0, reY2 = 0, reX1 = 0, reX2 = 0;
        int co1 = 5, co2 = 22, tw1 = width - 1 - co1, th1 = height - 1 - co1;
        g.setColor(Color.BLACK);
        if (angle == 0) {
            if (list.size() > 2) {
                for (int i = 2; i < list.size(); i++) {
                    reY1 += 5;
                    reY2 += 5;
                }
                for (int i = 15; i < list.size(); i++) {
                    reY1 += 1;
                    reY2 += 1;
                }
                g.drawLine(x + 1, y - reY1, x + 1, y + 1);
                g.drawLine(x + 1, y + height - 1, x + 1, y + getHeight() + reY2);
            }
            g.drawLine(x + 1, y + 1, x + co1, y + 1);
            g.drawLine(x + 1, y + height - 1, x + co1, y + height - 1);
            g.drawArc(x - co1 + 1, y + 1, co1 * 2 - 1, height - 2, 270, 180);
            g.drawArc(x + co1 - tw1, y + 1, tw1 * 2, height - 2, 270, 180);
            if (inverted) g.drawOval(x + getWidth(), y + getHeight() / 2 - 2, 4, 4);
        } else if (angle == 90) {
            if (list.size() > 2) {
                for (int i = 2; i < list.size(); i++) {
                    reX1 += 5;
                    reX2 += 5;
                }
                for (int i = 15; i < list.size(); i++) {
                    reX1 += 1;
                    reX2 += 1;
                }
                g.drawLine(x - reX1 - 2, y + 1, x + 1, y + 1);
                g.drawLine(x + width - 1, y + 1, x + getWidth() + reX2, y + 1);
            }
            g.drawLine(x + 1, y + 1, x + 1, y + co1);
            g.drawLine(x + width - 1, y + 1, x + width - 1, y + co1);
            g.drawArc(x + 1, y - co1 + 1, width - 2, co1 * 2, 180, 180);
            g.drawArc(x + 1, y + co1 - th1, width - 2, th1 * 2, 180, 180);
            if (inverted) g.drawOval(x + getWidth() / 2 - 2, y + getHeight(), 4, 4);
        } else if (angle == 180) {
            if (list.size() > 2) {
                for (int i = 2; i < list.size(); i++) {
                    reY1 += 5;
                    reY2 += 5;
                }
                for (int i = 15; i < list.size(); i++) {
                    reY1 += 1;
                    reY2 += 1;
                }
                g.drawLine(x + getWidth() - 1, y - reY1 - 2, x + getWidth() - 1, y + 1);
                g.drawLine(x + getWidth() - 1, y + height - 1, x + getWidth() - 1, y + getHeight() + reY2);
            }
            g.drawLine(x + width - 1, y + 1, x + width - co1, y + 1);
            g.drawLine(x + width - 1, y + height - 1, x + co2, y + height - 1);
            g.drawArc(x + co2, y + 1, co1 * 2, height - 2, 90, 180);
            g.drawArc(x + 1, y + 1, tw1 * 2, height - 2, 90, 180);
            if (inverted) g.drawOval(x - 4, y + getHeight() / 2 - 2, 4, 4);
        } else if (angle == 270) {
            if (list.size() > 2) {
                for (int i = 2; i < list.size(); i++) {
                    reX1 += 5;
                    reX2 += 5;
                }
                for (int i = 15; i < list.size(); i++) {
                    reX1 += 1;
                    reX2 += 1;
                }
                g.drawLine(x - reX1 - 1, y + getHeight() - 1, x + 1, y + getHeight() - 1);
                g.drawLine(x + width - 1, y + getHeight() - 1, x + getWidth() + reX2, y + getHeight() - 1);
            }
            g.drawLine(x + 1, y + height - 1, x + 1, y + height - co1);
            g.drawLine(x + width - 1, y + height - 1, x + width - 1, y + co2);
            g.drawArc(x + 1, y + co2, width - 2, co1 * 2, 0, 180);
            g.drawArc(x + 1, y + 1, width - 2, th1 * 2, 0, 180);
            if (inverted) g.drawOval(x + getWidth() / 2 - 2, y - 4, 4, 4);
        }
        Util.restoreGraphics(g);
    }

    @Override
    public void drawIt(Graphics2D g) {
        drawIt(g, x, y);
    }

    public GOR rotate(int angle) {
        this.angle = angle;
        if (angle == 90 || angle == 270) setSize(25, 27);
        return this;
    }

    public Coords getResCoords(int length) {
        int spacing = 0;
        Coords coords = new Coords();
        if (isInverted()) spacing += 5;
        if (angle == 0) coords.setAll(x + getWidth() + spacing, y + getHeight() / 2, x + getWidth() + spacing + length, y + getHeight() / 2); else if (angle == 90) coords.setAll(x + getWidth() / 2, y + getHeight() + spacing, x + getWidth() / 2, y + getHeight() + spacing + length); else if (angle == 180) coords.setAll(x - spacing - length, y + getHeight() / 2, x - spacing, y + getHeight() / 2); else if (angle == 270) coords.setAll(x + getWidth() / 2, y - spacing - length, x + getWidth() / 2, y - spacing);
        return coords;
    }

    public Coords getUpper2Coords(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y + 5, x + 3, y + 5); else if (angle == 90) coords.setAll(x + 20, y - length, x + 20, y + 4); else if (angle == 180) coords.setAll(x + getWidth() + length, y + 20, x + getWidth() - 3, y + 20); else if (angle == 270) coords.setAll(x + 4, y + getHeight() + length, x + 4, y + getHeight() - 3);
        return coords;
    }

    public Coords getLower2Coords(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y + 20, x + 3, y + 20); else if (angle == 90) coords.setAll(x + 5, y - length, x + 5, y + 4); else if (angle == 180) coords.setAll(x + getWidth() + length, y + 5, x + getWidth() - 3, y + 5); else if (angle == 270) coords.setAll(x + 20, y + getHeight() + length, x + 20, y + getHeight() - 3);
        return coords;
    }

    public Coords getUpper3Coords(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y - 3, x, y - 3); else if (angle == 90) coords.setAll(x + 28, y - length, x + 28, y); else if (angle == 180) coords.setAll(x + getWidth() + length, y - 4, x + getWidth(), y - 4); else if (angle == 270) coords.setAll(x + 28, y + getHeight() + length, x + 28, y + getHeight());
        return coords;
    }

    public Coords getMiddle3Coords(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y + 12, x + 4, y + 12); else if (angle == 90) coords.setAll(x + 12, y - length, x + 12, y + 5); else if (angle == 180) coords.setAll(x + getWidth() + length, y + 11, x + getWidth() - 4, y + 11); else if (angle == 270) coords.setAll(x + 12, y + getHeight() + length, x + 12, y + getHeight() - 4);
        return coords;
    }

    public Coords getLower3Coords(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y + 28, x, y + 28); else if (angle == 90) coords.setAll(x - 3, y - length, x - 3, y); else if (angle == 180) coords.setAll(x + getWidth() + length, y + 28, x + getWidth(), y + 28); else if (angle == 270) coords.setAll(x - 3, y + getHeight() + length, x - 3, y + getHeight());
        return coords;
    }

    public Coords getUpper2CoordsInv(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y + 5, x - 2, y + 5); else if (angle == 90) coords.setAll(x + 20, y - length, x + 20, y - 2); else if (angle == 180) coords.setAll(x + getWidth() + length, y + 20, x + getWidth() + 3, y + 20); else if (angle == 270) coords.setAll(x + 4, y + getHeight() + length, x + 4, y + getHeight() + 3);
        return coords;
    }

    public Coords getLower2CoordsInv(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y + 20, x - 2, y + 20); else if (angle == 90) coords.setAll(x + 5, y - length, x + 5, y - 2); else if (angle == 180) coords.setAll(x + getWidth() + length, y + 5, x + getWidth() + 3, y + 5); else if (angle == 270) coords.setAll(x + 20, y + getHeight() + length, x + 20, y + getHeight() + 3);
        return coords;
    }

    public Coords getUpper3CoordsInv(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y - 3, x - 5, y - 3); else if (angle == 90) coords.setAll(x + 28, y - length, x + 28, y - 5); else if (angle == 180) coords.setAll(x + getWidth() + length, y - 4, x + getWidth() + 6, y - 4); else if (angle == 270) coords.setAll(x + 28, y + getHeight() + length, x + 28, y + getHeight() + 6);
        return coords;
    }

    public Coords getMiddle3CoordsInv(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y + 12, x, y + 12); else if (angle == 90) coords.setAll(x + 12, y - length, x + 12, y); else if (angle == 180) coords.setAll(x + getWidth() + length, y + 11, x + getWidth() + 1, y + 11); else if (angle == 270) coords.setAll(x + 12, y + getHeight() + length, x + 12, y + getHeight() + 1);
        return coords;
    }

    public Coords getLower3CoordsInv(int length) {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - length, y + 28, x - 5, y + 28); else if (angle == 90) coords.setAll(x - 3, y - length, x - 3, y - 5); else if (angle == 180) coords.setAll(x + getWidth() + length, y + 28, x + getWidth() + 6, y + 28); else if (angle == 270) coords.setAll(x - 3, y + getHeight() + length, x - 3, y + getHeight() + 6);
        return coords;
    }

    public Coords getUpperInv2Position() {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - 1, y + 3, 0, 0); else if (angle == 90) coords.setAll(x + 18, y - 1, 0, 0); else if (angle == 180) coords.setAll(x + getWidth() - 2, y + 18, 0, 0); else if (angle == 270) coords.setAll(x + 2, y + getHeight() - 2, 0, 0);
        return coords;
    }

    public Coords getLowerInv2Position() {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - 1, y + 18, 0, 0); else if (angle == 90) coords.setAll(x + 3, y - 1, 0, 0); else if (angle == 180) coords.setAll(x + getWidth() - 2, y + 3, 0, 0); else if (angle == 270) coords.setAll(x + 18, y + getHeight() - 2, 0, 0);
        return coords;
    }

    public Coords getUpperInv3Position() {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - 4, y - 5, 0, 0); else if (angle == 90) coords.setAll(x + 26, y - 4, 0, 0); else if (angle == 180) coords.setAll(x + getWidth() + 1, y - 6, 0, 0); else if (angle == 270) coords.setAll(x + 26, y + getHeight() + 1, 0, 0);
        return coords;
    }

    public Coords getMiddleInv3Position() {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x + 1, y + 10, 0, 0); else if (angle == 90) coords.setAll(x + 10, y + 1, 0, 0); else if (angle == 180) coords.setAll(x + getWidth() - 4, y + 9, 0, 0); else if (angle == 270) coords.setAll(x + 10, y + getHeight() - 4, 0, 0);
        return coords;
    }

    public Coords getLowerInv3Position() {
        Coords coords = new Coords();
        if (angle == 0) coords.setAll(x - 4, y + 26, 0, 0); else if (angle == 90) coords.setAll(x - 4, y - 5, 0, 0); else if (angle == 180) coords.setAll(x + getWidth() + 1, y + 26, 0, 0); else if (angle == 270) coords.setAll(x - 5, y + getHeight() + 1, 0, 0);
        return coords;
    }
}
