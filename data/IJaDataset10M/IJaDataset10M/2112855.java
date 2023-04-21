package jasci.util;

public class Color {

    public static final byte BLACK = 0;

    public static final byte RED = 1;

    public static final byte GREEN = 2;

    public static final byte YELLOW = 3;

    public static final byte BLUE = 4;

    public static final byte MAGENTA = 5;

    public static final byte CYAN = 6;

    public static final byte WHITE = 7;

    public static final byte REVERSE = 50;

    public static final byte TRANSPARENT = 51;

    public static final Color BandW = new Color(WHITE, BLACK);

    byte fcolor, bcolor;

    boolean bright;

    boolean freverse, breverse;

    public Color(byte fcolor, byte bcolor) {
        this.fcolor = fcolor;
        this.bcolor = bcolor;
        this.bright = false;
        this.freverse = false;
        this.breverse = false;
    }

    public Color(Color c) {
        this.fcolor = c.fcolor;
        this.bcolor = c.bcolor;
        this.bright = c.bright;
        this.freverse = false;
        this.breverse = false;
    }

    public int getForegroundColor() {
        if (freverse) return Math.min(WHITE, (int) bcolor); else return Math.min(WHITE, (int) fcolor);
    }

    public int getBackgroundColor() {
        if (breverse) return Math.min(WHITE, (int) fcolor); else return Math.min(WHITE, (int) bcolor);
    }

    public boolean isBright() {
        return bright;
    }

    public boolean isTransparent() {
        return (bcolor == TRANSPARENT);
    }

    public void set(byte fcolor, byte bcolor) {
        if (fcolor == REVERSE) freverse = true; else if (fcolor != TRANSPARENT) this.fcolor = fcolor;
        if (bcolor == REVERSE) breverse = true; else if (bcolor != TRANSPARENT) this.bcolor = bcolor;
    }

    public void set(Color color) {
        set(color.fcolor, color.bcolor);
    }

    public Color reverse() {
        Color c = new Color(this);
        c.freverse = true;
        c.breverse = true;
        return c;
    }

    public String toString() {
        switch(fcolor) {
            case BLACK:
                return "C BLACK";
            case RED:
                return "C RED";
            case GREEN:
                return "C GREEN";
            case YELLOW:
                return "C YELLOW";
            case BLUE:
                return "C BLUE";
            case MAGENTA:
                return "C MAGENTA";
            case CYAN:
                return "C CYAN";
            case WHITE:
                return "C WHITE";
            default:
                return "COLOR";
        }
    }
}
