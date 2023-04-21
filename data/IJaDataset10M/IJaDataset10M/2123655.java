package tm.displayEngine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import tm.interfaces.Datum;

public abstract class DatumDisplay extends Object {

    static final int LAST_STEP = 20;

    public static final int ONLY = 0;

    public static final int OLDEST = 1;

    public static final int IN_BETWEEN = 2;

    public static final int YOUNGEST = 3;

    protected static Font addressFont = new Font("Dialog", Font.PLAIN, 12);

    protected static Font nameFont = new Font("Dialog", Font.BOLD, 12);

    protected static Font valueFont = new Font("Dialog", Font.PLAIN, 12);

    protected static int STRING_OFFSET = 5;

    public static int baseHeight = 14;

    public static final int NOT_IN_BOX = 0;

    public static final int IN_BOX = 1;

    public static final int EXPANDER_HIT = 2;

    public static int getNameX(int nestLevel) {
        return 0;
    }

    public static int getValueX(int nestLevel) {
        return 0;
    }

    public static int getAddressX(int nestLevel) {
        return 0;
    }

    public static int getDescenderX(int nestLevel) {
        return 0;
    }

    public static int getExpanderX(int nestLevel) {
        return 0;
    }

    public static int getCompoundXLeft(int nestLevel) {
        return 0;
    }

    public static int getCompoundXRight(int nestLevel) {
        return 0;
    }

    protected Datum myDatum;

    protected Expander myExpander;

    protected DataDisplayView myDataView = null;

    protected int nestLevel;

    protected StringBox nameBox;

    protected StringBox addressBox;

    protected StringBox valueBox;

    protected int birthOrder;

    protected Rectangle extent;

    protected boolean grayOut = false;

    protected boolean selected = false;

    public DatumDisplay(Datum datum, DataDisplayView displayView, boolean expand) {
        myDataView = displayView;
        myDatum = datum;
        Datum parent = datum.getParent();
        if (parent == null) {
            nestLevel = 0;
            birthOrder = ONLY;
        } else {
            DatumDisplay pdd = getAssociated(parent, myDataView);
            nestLevel = (pdd == null) ? 0 : pdd.nestLevel + 1;
            if (parent.getNumChildren() == 1) birthOrder = ONLY; else if (datum.getBirthOrder() == 0) birthOrder = OLDEST; else if (datum.getBirthOrder() == parent.getNumChildren() - 1) birthOrder = YOUNGEST; else birthOrder = IN_BETWEEN;
        }
        if (myDatum.getNumChildren() > 0) {
            myExpander = new Expander();
            myExpander.setExpanded(expand);
        } else myExpander = null;
        extent = new Rectangle(0, 0, 0, 0);
        myDatum.setProperty(myDataView.getDisplayString(), this);
        nameBox = new StringBox(datum.getName(), false, StringBox.RIGHT, StringBox.MIDDLE);
        valueBox = new StringBox(datum.getValueString(), true, StringBox.LEFT, StringBox.MIDDLE);
    }

    public static DatumDisplay getAssociated(Datum datum, DataDisplayView view) {
        return (DatumDisplay) (datum.getProperty(view.getDisplayString()));
    }

    public D3Iterator getIterator() {
        return new D3Iterator(this, myDataView);
    }

    static void updateFonts(Font baseFont, int height) {
        addressFont = new Font(baseFont.getName(), Font.PLAIN, baseFont.getSize());
        nameFont = new Font(baseFont.getName(), Font.BOLD, baseFont.getSize());
        valueFont = new Font(baseFont.getName(), Font.PLAIN, baseFont.getSize());
        baseHeight = height + 1;
    }

    public Point getLocation() {
        return new Point(extent.x, extent.y);
    }

    public Dimension getSize() {
        return new Dimension(extent.width, extent.height);
    }

    public boolean contains(Point p) {
        return extent.contains(p);
    }

    public Datum getDatum() {
        return myDatum;
    }

    public Expander getExpander() {
        return myExpander;
    }

    public void move(int x, int y) {
        extent.x = x;
        extent.y = y;
        if (myExpander != null && myExpander.getExpanded()) {
            y = 0;
            for (int i = 0; i < myDatum.getNumChildren(); i++) {
                DatumDisplay kid = getAssociated(myDatum.getChildAt(i), myDataView);
                kid.move(extent.x, extent.y + y);
                y += kid.extent.height;
            }
        }
    }

    public void resize(int nWidth, int vGap, int vWidth, int aWidth) {
        vGap += Expander.EXPAND_OFFSET;
        vWidth -= Expander.EXPAND_OFFSET;
        extent.width = nWidth + vGap + vWidth + aWidth;
        extent.height = baseHeight;
        nameBox.move(0, 0);
        nameBox.resize(nWidth, baseHeight);
        nameBox.nudge(STRING_OFFSET, 0);
        valueBox.move(nWidth + vGap, 0);
        valueBox.resize(vWidth, baseHeight);
        valueBox.nudge(STRING_OFFSET, 0);
        if (addressBox != null) {
            addressBox.move(nWidth + vGap + vWidth, 0);
            addressBox.resize(aWidth, baseHeight);
            addressBox.nudge(STRING_OFFSET, 0);
        }
        if (myExpander != null) {
            myExpander.move(nWidth + vGap - Expander.EXPAND_OFFSET + Expander.EXPAND_X, Expander.EXPAND_Y);
            if (myExpander.getExpanded()) {
                int y = 0;
                for (int i = 0; i < myDatum.getNumChildren(); i++) {
                    DatumDisplay kid = getAssociated(myDatum.getChildAt(i), myDataView);
                    kid.resize(nWidth, vGap, vWidth, aWidth);
                    kid.move(extent.x, extent.y + y);
                    y += kid.extent.height;
                }
                extent.height = y;
            }
        }
    }

    public void mouseClicked(Point p) {
        if (extent.contains(p)) {
            boolean clickSelect = false;
            if (myExpander == null) clickSelect = true; else {
                if (myExpander.contains(p, getLocation())) {
                    myExpander.setExpanded(!myExpander.getExpanded());
                    myDataView.refresh();
                } else if (myExpander.getExpanded()) {
                    for (int i = 0; i < myDatum.getNumChildren(); i++) {
                        DatumDisplay kid = getAssociated(myDatum.getChildAt(i), myDataView);
                        kid.mouseClicked(p);
                    }
                } else clickSelect = true;
            }
            if (clickSelect) {
                selected = !selected;
                myDataView.refresh();
            }
        }
    }

    public boolean expanderHit(Point p) {
        if (myExpander == null) return false;
        if (myExpander.contains(p, getLocation())) {
            myExpander.setExpanded(!myExpander.getExpanded());
            return true;
        }
        if (myExpander.getExpanded()) {
            for (int i = 0; i < myDatum.getNumChildren(); i++) {
                DatumDisplay kid = getAssociated(myDatum.getChildAt(i), myDataView);
                if (kid.expanderHit(p)) return true;
            }
        }
        return false;
    }

    void setGrayOut(boolean on) {
        grayOut = on;
    }

    boolean isSelected() {
        if (selected) return true;
        if (myDatum.getParent() == null) return false;
        return getAssociated(myDatum.getParent(), myDataView).isSelected();
    }

    boolean isGrayedOut() {
        return grayOut;
    }

    public void draw(Graphics2D screen) {
        if (screen == null) return;
        if (myExpander == null) drawScalar(screen); else drawCompound(screen);
    }

    protected void drawCompound(Graphics2D screen) {
        Rectangle localR = new Rectangle(extent);
        myExpander.draw(screen, localR);
        localR.x += nameBox.getExtent().width;
        localR.width = valueBox.getExtent().x + valueBox.getExtent().width - nameBox.getExtent().width;
        screen.draw(localR);
        localR.x += Expander.EXPAND_OFFSET * nestLevel;
        if (myExpander.getExpanded()) {
            int kids = myDatum.getNumChildren();
            int xDesc = localR.x + Expander.EXPAND_OFFSET / 2;
            int yEnd;
            int xStart, xEnd;
            for (int i = 0; i < kids; i++) {
                DatumDisplay dd = (DatumDisplay) myDatum.getChildAt(i).getProperty(myDataView.getDisplayString());
                int depth = dd.extent.height;
                int yLeader = localR.y + baseHeight / 2;
                if (i > 0) {
                    xStart = xDesc;
                    yEnd = localR.y + (i == (kids - 1) ? baseHeight / 2 : depth);
                    screen.drawLine(xDesc, localR.y, xDesc, yEnd);
                } else {
                    if (kids > 1) screen.drawLine(xDesc, localR.y + Expander.EXPAND_X + Expander.EXPAND_H, xDesc, localR.y + depth);
                    xStart = localR.x + Expander.EXPAND_X + Expander.EXPAND_W;
                }
                xEnd = localR.x + Expander.EXPAND_OFFSET * (dd.getExpander() == null ? 2 : 1);
                screen.drawLine(xStart, yLeader, xEnd, yLeader);
                dd.draw(screen);
                localR.y += depth;
            }
        } else drawScalar(screen);
    }

    protected abstract void drawScalar(Graphics2D screen);

    public String toString() {
        return "DatumDisplay for " + myDatum.toString();
    }
}
