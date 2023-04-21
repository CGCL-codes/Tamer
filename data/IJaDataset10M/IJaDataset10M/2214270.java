package net.sf.yajac.telnet;

/**
 *
 * @author Mox Tryer
 */
class TerminalCharacter {

    /** Make character normal. */
    public static final int NORMAL = 0x00;

    /** Make character bold. */
    public static final int BOLD = 0x01;

    /** Underline character. */
    public static final int UNDERLINE = 0x02;

    /** Invert character. */
    public static final int INVERT = 0x04;

    /** Lower intensity character. */
    public static final int LOW = 0x08;

    /** Invisible character. */
    public static final int INVISIBLE = 0x10;

    /** Blinking character. */
    public static final int BLINK = 0x20;

    /** how much to left shift the foreground color */
    public static final int COLOR_FG_SHIFT = 8;

    /** how much to left shift the background color */
    public static final int COLOR_BG_SHIFT = 12;

    /** color mask */
    public static final int COLOR = 0x0000ff00;

    /** foreground color mask */
    public static final int COLOR_FG = 0x00000f00;

    /** background color mask */
    public static final int COLOR_BG = 0x0000f000;

    private char ch;

    private int attributes;

    public TerminalCharacter() {
        this((char) 0, 0);
    }

    public TerminalCharacter(char ch, int attributes) {
        this.ch = ch;
        this.attributes = attributes;
    }

    public void clear() {
        clear(0);
    }

    public void clear(int attributes) {
        this.ch = 0;
        this.attributes = attributes;
    }

    public void set(char ch, int attributes) {
        this.ch = ch;
        this.attributes = attributes;
    }

    public char getCharacter() {
        return ch;
    }

    public int getAttributes() {
        return attributes;
    }
}
