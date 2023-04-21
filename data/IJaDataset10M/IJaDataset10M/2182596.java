package jeliot.theater;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import jeliot.tracker.Tracker;
import jeliot.tracker.TrackerClock;

/**
 * <code>VariableActor</code> represent graphically the language construct
 * <code>Variable</code> for primitive data types and Strings.
 * 
 * @author Pekka Uronen
 * @author Niko Myller
 * 
 * @see jeliot.lang.Variable
 * @see jeliot.theater.ReferenceVariableActor
 * @see jeliot.theater.VariableInArrayActor
 * @see jeliot.theater.MethodStage
 * @see jeliot.theater.ObjectStage
 */
public class VariableActor extends Actor implements ActorContainer {

    /**
     * Location of the variable's name.
     */
    protected int namex;

    /**
     * Location of the variable's name.
     */
    protected int namey;

    /**
     * x-coordinate of the location of the value slot.
     */
    protected int valuex;

    /**
     * y-coordinate of the location of the value slot.
     */
    protected int valuey;

    /**
     * the width of the value slot.
     */
    protected int valuew;

    /**
     * the height of the value slot.
     */
    protected int valueh;

    /**
     * Value border width.
     */
    protected int vborderw = 2;

    /**
     * Variable's name.
     */
    protected String name = "";

    /**
     * Variable's type.
     */
    protected String type = "";

    /**
     * Value actor assigned to this variable actor.
     */
    protected ValueActor value;

    /**
     * Background color of the values of this type.
     */
    protected Color valueColor;

    /**
     *
     */
    protected ValueActor reserved;

    public void paintActor(Graphics g) {
        int w = width;
        int h = height;
        int bw = borderWidth;
        g.setColor((light == HIGHLIGHT) ? darkColor : bgcolor);
        g.fillRect(bw, bw, w - 2 * bw, h - 2 * bw);
        g.setFont(font);
        g.setColor((light == HIGHLIGHT) ? lightColor : fgcolor);
        g.drawString(getLabel(), namex, namey);
        g.drawRect(valuex - 1, valuey - 1, valuew + 1, valueh + 1);
        g.setColor(darkColor);
        g.drawRect(valuex - 2, valuey - 2, valuew + 3, valueh + 3);
        g.setColor(valueColor);
        g.fillRect(valuex, valuey, valuew, valueh);
        if (value != null) {
            int actx = value.getX();
            int acty = value.getY();
            g.translate(actx, acty);
            value.paintValue(g);
            g.translate(-actx, -acty);
        }
        ActorContainer parent = getParent();
        g.setColor((parent instanceof Actor) ? ((Actor) parent).darkColor : fgcolor);
        g.drawLine(0, 0, w - 1, 0);
        g.drawLine(0, 0, 0, h - 1);
        g.setColor((parent instanceof Actor) ? ((Actor) parent).lightColor : fgcolor);
        g.drawLine(1, h - 1, w - 1, h - 1);
        g.drawLine(w - 1, 1, w - 1, h - 1);
        g.setColor(fgcolor);
        g.drawRect(1, 1, w - 3, h - 3);
        g.setColor(darkColor);
        g.drawLine(2, h - 3, w - 3, h - 3);
        g.drawLine(w - 3, 2, w - 3, h - 3);
        g.setColor(lightColor);
        g.drawLine(2, 2, w - 3, 2);
        g.drawLine(2, 2, 2, h - 3);
    }

    public void setFont(Font font) {
        super.setFont(font);
        if (getLabel() != null) {
            calcLabelPosition();
        }
    }

    /**
     * @param actor
     * @return
     */
    public Point reserve(ValueActor actor) {
        this.reserved = actor;
        Point rp = getRootLocation();
        int w = actor.width;
        int h = actor.height;
        rp.translate(valuex + (valuew - w) / 2, valuey + (valueh - h) / 2);
        return rp;
    }

    public void removeActor(Actor actor) {
    }

    /**
     * 
     */
    public void bind() {
        this.value = this.reserved;
        value.setParent(this);
        value.setLocation(valuex + (valuew - value.width) / 2, valuey + (valueh - value.height) / 2);
    }

    /**
     * @param actor
     */
    public void setValue(ValueActor actor) {
        this.reserved = actor;
        bind();
    }

    /**
     * @return
     */
    public ValueActor getValue() {
        ValueActor act = (ValueActor) this.value.clone();
        return act;
    }

    /**
     * @param valuec
     */
    public void setValueColor(Color valuec) {
        this.valueColor = valuec;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        calcLabelPosition();
    }

    public String getName() {
        return this.name;
    }

    /**
     * @param w
     * @param h
     */
    public void setValueDimension(int w, int h) {
        this.valuew = w;
        this.valueh = h;
        calcLabelPosition();
    }

    public void setSize(int w, int h) {
        super.setSize(w, h);
        calcLabelPosition();
    }

    /**
     * 
     */
    protected void calcLabelPosition() {
        int w = getWidth();
        int h = getHeight();
        FontMetrics fm = getFontMetrics();
        int sh = fm.getHeight();
        valuex = w - insets.right - 4 - valuew;
        valuey = (h - valueh) / 2;
        namex = insets.left;
        namey = (h + sh) / 2;
    }

    public void calculateSize() {
        FontMetrics fm = getFontMetrics();
        int sw = fm.stringWidth(getLabel());
        int sh = fm.getHeight();
        setSize(4 * borderWidth + insets.right + insets.left + valuew + sw, insets.top + insets.bottom + 4 * borderWidth + Math.max(valueh, sh));
        calcLabelPosition();
    }

    public Animation disappear() {
        if (value != null) {
            value.disappear();
        }
        Point p = getRootLocation();
        Tracker.trackTheater(TrackerClock.currentTimeMillis(), Tracker.DISAPPEAR, getActorId(), Tracker.RECTANGLE, new int[] { p.x }, new int[] { p.y }, getWidth(), getHeight(), 0, -1, getDescription());
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        calcLabelPosition();
    }

    public String getLabel() {
        String name = getName();
        String displayedName = (name.lastIndexOf('.') != (-1)) ? name.substring(name.lastIndexOf('.') + 1, name.length()) : name;
        return getType() + " " + displayedName;
    }

    public String toString() {
        return "variable " + getName() + " of type " + getType();
    }
}
