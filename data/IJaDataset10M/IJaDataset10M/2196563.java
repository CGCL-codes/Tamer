package com.testonica.kickelhahn.core.elements.pin.indicator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import com.testonica.kickelhahn.core.elements.board.BoardComponent;
import com.testonica.kickelhahn.core.elements.net.Connectable;
import com.testonica.kickelhahn.core.elements.net.Net;
import com.testonica.kickelhahn.core.elements.net.NoSuchPinException;

public class InputOutputPinIndicator extends BoardComponent implements Connectable {

    public static final int TYPE_INPUT_PIN_INDICATOR = 21;

    public static final int TYPE_OUTPUT_PIN_INDICATOR = 22;

    public static final int TYPE_INPUT_OUTPUT_PIN_INDICATOR = 23;

    protected boolean control;

    private boolean left;

    protected Net net;

    private int type = -1;

    protected char value;

    public InputOutputPinIndicator(BoardComponent boardComponent, boolean left, int type) {
        setBoard(boardComponent.getBoard());
        this.left = left;
        this.type = type;
        value = !isInput() ? '0' : 'X';
    }

    public void setNet(Net net) {
        this.net = net;
    }

    public BoardComponent getBoardComponent() {
        return this;
    }

    public Connectable getPin(String name) throws NoSuchPinException {
        if (name.equals(getName())) return this;
        return null;
    }

    public Point getPoint() {
        return new Point(left ? getLocation().x + getSize().width : getLocation().x, getLocation().y + getSize().height / 2);
    }

    public Net getNet() {
        return net;
    }

    public int getType() {
        return type;
    }

    public char getValue() {
        return value;
    }

    public boolean isInput() {
        return type == TYPE_INPUT_PIN_INDICATOR;
    }

    public boolean isLeft() {
        return left;
    }

    public void paint(Graphics g) {
        if ((g == null) || !net.getBoard().isVisible() || net.getBoard().getIgnoreRepaint()) return;
        if ((g.getClipBounds() != null) && !g.getClipBounds().intersects(getBounds())) return;
        String stringValue = value + "";
        g.setColor(control ? Color.red : new Color(0, 0, 64));
        int tw = g.getFontMetrics().stringWidth(stringValue);
        int th = (int) g.getFontMetrics().getStringBounds(stringValue, g).getHeight();
        if ((left & isInput()) | (!left & !isInput())) {
            g.fillPolygon(new int[] { getLocation().x, getLocation().x + getSize().width - 5, getLocation().x + getSize().width, getLocation().x + getSize().width - 5, getLocation().x }, new int[] { getLocation().y, getLocation().y, getLocation().y + getSize().height / 2, getLocation().y + getSize().height, getLocation().y + getSize().height }, 5);
            g.setColor(Color.lightGray);
            g.drawString(stringValue, getLocation().x - 2 + getSize().width / 2 - tw / 2, getLocation().y + getSize().height - Math.round(((float) getSize().height - th)));
        } else {
            g.fillPolygon(new int[] { getLocation().x, getLocation().x + 5, getLocation().x + getSize().width, getLocation().x + getSize().width, getLocation().x + 5 }, new int[] { getLocation().y + getSize().height / 2, getLocation().y, getLocation().y, getLocation().y + getSize().height, getLocation().y + getSize().height }, 5);
            g.setColor(Color.lightGray);
            g.drawString(stringValue, getLocation().x + 2 + getSize().width / 2 - tw / 2, getLocation().y + getSize().height - Math.round(((float) getSize().height - th)));
        }
    }

    public void repaint() {
        getBoard().repaint(getBounds());
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    public void setValue(char value) {
        this.value = value;
        repaint();
    }
}
