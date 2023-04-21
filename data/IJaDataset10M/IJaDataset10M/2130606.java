package ch.usi.inf.pf2.circuit;

import java.awt.Font;
import java.awt.Graphics;
import ch.usi.inf.pf2.time.Schedule;

public class XorGate extends Gate {

    private static final long serialVersionUID = 6030806630375190085L;

    private int x1;

    private int y1;

    private int x2;

    private int y2;

    private int x3;

    private int y3;

    private int xInputPin1;

    private int yInputPin1;

    private int xInputPin2;

    private int yInputPin2;

    private int xOutputPin;

    private int yOutputPin;

    public XorGate(final int x, final int y) {
        super();
        this.x1 = x;
        this.y1 = y;
        this.x2 = x1;
        this.y2 = y1 + 50;
        this.x3 = x1 + 50;
        this.y3 = (y1 + y2) / 2;
        this.xInputPin1 = (x1 - 5);
        this.yInputPin1 = y1 + (y2 - y1) / 3;
        this.xInputPin2 = (x1 - 5);
        this.yInputPin2 = y1 + 2 * (y2 - y1) / 3;
        this.xOutputPin = x3;
        this.yOutputPin = y3;
    }

    public void compute() {
        if (valuesInputPin1.containsKey(Schedule.getStep() - delay)) {
            values[0] = valuesInputPin1.get(Schedule.getStep() - delay);
        }
        if (valuesInputPin2.containsKey(Schedule.getStep() - delay)) {
            values[1] = valuesInputPin2.get(Schedule.getStep() - delay);
        }
        if (this.values[0] == Value.TRUE && this.values[1] == Value.TRUE) {
            this.valueAtExit = Value.FALSE;
        } else if (this.values[0] == Value.FALSE && this.values[1] == Value.FALSE) {
            this.valueAtExit = Value.FALSE;
        } else if (this.values[0] == Value.UNKNOWN || this.values[1] == Value.UNKNOWN) {
            this.valueAtExit = Value.UNKNOWN;
        } else {
            this.valueAtExit = Value.TRUE;
        }
        valuesInputPin1.remove(Schedule.getStep() - delay);
        valuesInputPin2.remove(Schedule.getStep() - delay);
        createEvent();
    }

    public int getOriginX() {
        return ((x1 - 5) + x3) / 2;
    }

    public int getOriginY() {
        return (y1 + y2) / 2;
    }

    public int getXInputPin1() {
        return this.xInputPin1;
    }

    public int getYInputPin1() {
        return this.yInputPin1;
    }

    public int getXInputPin2() {
        return this.xInputPin2;
    }

    public int getYInputPin2() {
        return this.yInputPin2;
    }

    public int getXOutputPin() {
        return this.xOutputPin + 5;
    }

    public int getYOutputPin() {
        return this.yOutputPin;
    }

    @Override
    public void draw(final Graphics g) {
        g.drawLine(x1, y1, x3, y3);
        g.drawLine(x2, y2, x3, y3);
        g.drawArc((x1 - 10), y1, 20, 50, 90, -180);
        g.drawArc(((x1 - 10) - 5), y1, 20, 50, 90, -180);
        g.drawLine(xInputPin1, yInputPin1, (xInputPin1 + 5), yInputPin1);
        g.drawLine(xInputPin2, yInputPin2, (xInputPin2 + 5), yInputPin2);
        g.drawLine(xOutputPin, yOutputPin, (xOutputPin + 5), yOutputPin);
        String d = delay + "";
        g.setFont(new Font("TimesRoman", Font.BOLD, 10));
        g.drawString(d, getOriginX() - g.getFontMetrics().stringWidth(d) / 2, y1);
    }

    @Override
    public boolean contains(final int x, final int y) {
        return x >= (x1 - 5) && x <= x3 && y >= y1 && y <= y2;
    }

    @Override
    public void move(final int deltaX, final int deltaY) {
        x1 += deltaX;
        x2 += deltaX;
        x3 += deltaX;
        xInputPin1 += deltaX;
        xInputPin2 += deltaX;
        xOutputPin += deltaX;
        y1 += deltaY;
        y2 += deltaY;
        y3 += deltaY;
        yInputPin1 += deltaY;
        yInputPin2 += deltaY;
        yOutputPin += deltaY;
        this.moveWire(deltaX, deltaY);
    }

    public String toString() {
        return "XOR gate";
    }
}
