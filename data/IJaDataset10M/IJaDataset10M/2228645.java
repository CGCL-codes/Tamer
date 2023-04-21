package org.openymsg.support;

import java.util.ArrayList;
import java.util.List;

/**
 * A message element represents a low level segment of a decoded message. The sections form a hierarchy, with zero or
 * more sections nested inside a given section.
 * 
 * Thanks to John Morris, who provided examples of some useful upgrades and optimisations to the Swing Document code.
 * 
 * @author G. der Kinderen, Nimbuzz B.V. guus@nimbuzz.com
 * @author S.E. Morris
 */
public class MessageElement {

    public static final int NULL = -2;

    public static final int ROOT = -1;

    public static final int TEXT = 0;

    public static final int BOLD = 1;

    public static final int ITALIC = 2;

    public static final int COLOUR_INDEX = 3;

    public static final int UNDERLINE = 4;

    public static final int FONT = 5;

    public static final int FADE = 6;

    public static final int ALT = 7;

    public static final int COLOUR_ABS = 8;

    public static final int COLOUR_NAME = 9;

    protected int type;

    protected List<MessageElement> children;

    protected String text;

    protected int colour;

    static final String[] COLOUR_INDEXES = { "black", "blue", "cyan", "pink", "green", "gray", "purple", "orange", "red", "brown", "yellow" };

    /**
     * CONSTRUCTORS
     */
    protected MessageElement(int t) {
        type = t;
        children = new ArrayList<MessageElement>();
    }

    protected MessageElement(int t, String body) {
        this(t);
        switch(t) {
            case TEXT:
                text = body;
                break;
            default:
        }
    }

    protected MessageElement(int t, int num) {
        this(t);
        switch(t) {
            case COLOUR_NAME:
                colour = num;
                break;
        }
    }

    /**
     * Utility methods
     */
    static int whichColourName(String n) {
        for (int i = 0; i < COLOUR_INDEXES.length; i++) {
            if (n.equals(COLOUR_INDEXES[i])) return i;
        }
        return -1;
    }

    boolean colourEquals(int i) {
        return (colour == i);
    }

    /**
     * Add a child to this section
     */
    void addChild(MessageElement s) {
        children.add(s);
    }

    /**
     * Translate to plain text
     */
    public String toText() {
        final StringBuffer sb = new StringBuffer();
        toText(sb);
        return sb.toString();
    }

    private void toText(StringBuffer sb) {
        if (type == TEXT) sb.append(text);
        for (MessageElement sc : children) {
            sc.toText(sb);
        }
    }
}
