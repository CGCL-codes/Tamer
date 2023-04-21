package code;

import java.awt.Color;

public class Block implements Cloneable {

    public static final Color[] colors = { new Color(0, 0, 0, 220), new Color(0, 0, 0, 205), new Color(0, 0, 0, 190), new Color(0, 0, 0, 165), new Color(0, 0, 0, 140), new Color(0, 0, 0, 125), new Color(0, 0, 0, 110) };

    public static final int EMPTY = 0, FILLED = 1, ACTIVE = 2;

    public static final Color emptycolor = new Color(120, 120, 190, 90);

    private volatile int state = EMPTY;

    private volatile Color color = emptycolor;

    public Block() {
    }

    public Block(int s) {
        state = s;
    }

    public String toString() {
        return color.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Block)) return false;
        Block b = (Block) o;
        return b.state == state;
    }

    public Block clone() {
        Block ret = new Block(state);
        ret.setColor(color);
        return ret;
    }

    public byte toByte() {
        switch(state) {
            case EMPTY:
            case FILLED:
            case ACTIVE:
                return (byte) state;
            default:
                return -1;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
