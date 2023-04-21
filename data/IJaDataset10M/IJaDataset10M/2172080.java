package ch.usi.inf.pf2.circuit;

import java.awt.Graphics;

public final class Wire extends Component {

    private static final long serialVersionUID = -6801809041025354496L;

    private InputPin inputPin;

    private OutputPin outputPin;

    private int x1;

    private int y1;

    private int x2;

    private int y2;

    private String color;

    public Wire(final int x1, final int y1, final int x2, final int y2) {
        this.inputPin = null;
        this.outputPin = null;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        color = "black";
    }

    public void setValue(final Value value) {
        System.out.println("wire: " + this);
        inputPin.setValue(value);
        if (value == Value.TRUE) {
            color = "green";
        } else if (value == Value.FALSE) {
            color = "blue";
        } else {
            color = "black";
        }
    }

    public void setInputPin(final InputPin inputPin) {
        this.inputPin = inputPin;
    }

    public void setOutputPin(final OutputPin outputPin) {
        this.outputPin = outputPin;
    }

    public void removePins() {
        if (inputPin != null) {
            inputPin.removeInputPin();
        }
        if (outputPin != null) {
            outputPin.removeWire(this);
        }
        inputPin = null;
        outputPin = null;
    }

    @Override
    public Wire[] getWires() {
        return new Wire[] { this };
    }

    public void setX1(final int x) {
        this.x1 = x;
    }

    public void setY1(final int y) {
        this.y1 = y;
    }

    public void setX2(final int x) {
        this.x2 = x;
    }

    public void setY2(final int y) {
        this.y2 = y;
    }

    public int getOriginX() {
        return x1 + (x2 - x1) / 2;
    }

    public int getOriginY() {
        return y1 + (y2 - y1) / 2;
    }

    public int getAnchorX() {
        return x1;
    }

    public int getAnchorY() {
        return y1;
    }

    public void draw(final Graphics g) {
        g.drawLine(x1, y1, x2, y2);
    }

    public boolean contains(final int x, final int y) {
        return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) && y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
    }

    @Override
    public void move(final int deltaX, final int deltaY) {
    }

    public void moveXY1(final int deltaX, final int deltaY) {
        this.x1 += deltaX;
        this.y1 += deltaY;
    }

    public void moveXY2(final int deltaX, final int deltaY) {
        this.x2 += deltaX;
        this.y2 += deltaY;
    }

    public String getColor() {
        return color;
    }

    public String toString() {
        return "Wire";
    }
}
