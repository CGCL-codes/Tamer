package jalview.gui;

import java.awt.*;
import java.util.*;
import jalview.gui.event.*;

class SeqFillPanel extends ControlledPanel implements FontChangeListener {

    AlignViewport av;

    int position;

    Vector ignore = new Vector();

    boolean debug = false;

    String debugStr;

    public static final int LEFT = 1;

    public static final int TOP = 2;

    public SeqFillPanel(int position, AlignViewport av, Controller c) {
        setBackground(Color.white);
        this.av = av;
        setController(c);
        this.position = position;
    }

    public Dimension minimumSize() {
        Dimension size = getParent().size();
        Dimension min = new Dimension(0, 0);
        if (debug) System.out.println(debugStr + "parent size = " + size);
        if (position == LEFT) {
            min.width = (size.width - ignoreSize()) - ((size.width - ignoreSize()) / av.getCharWidth() * av.getCharWidth());
        } else if (position == TOP) {
            min.height = (size.height - ignoreSize()) - ((size.height - ignoreSize()) / av.getCharHeight() * av.getCharHeight());
        }
        if (debug) System.out.println(debugStr + "min size = " + min);
        return min;
    }

    public int ignoreSize() {
        int ignoredSize = 0;
        for (int i = 0; i < ignore.size(); i++) {
            Component c = (Component) ignore.elementAt(i);
            if (position == LEFT) {
                ignoredSize += c.preferredSize().width;
            } else if (position == TOP) {
                ignoredSize += c.preferredSize().height;
            }
            ignoredSize--;
        }
        if (debug) System.out.println(debugStr + "Ignored size = " + ignoredSize);
        return ignoredSize;
    }

    public void setDebug(boolean state, String debugStr) {
        this.debug = state;
        this.debugStr = debugStr;
        setBackground(Color.red);
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (debug) System.out.println(debugStr + "Current size = " + getSize() + " Min size = " + minimumSize());
    }

    public void addDeadSpace(Component c) {
        ignore.addElement(c);
    }

    public boolean handleFontChangeEvent(FontChangeEvent evt) {
        invalidate();
        return true;
    }

    public Dimension preferredSize() {
        return minimumSize();
    }
}
