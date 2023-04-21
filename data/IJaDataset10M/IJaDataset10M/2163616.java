package org.andrewberman.ui.menu;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Rectangle2D.Float;
import org.andrewberman.ui.Color;
import org.andrewberman.ui.FocusManager;
import org.andrewberman.ui.Point;
import org.andrewberman.ui.UIUtils;
import org.andrewberman.ui.ifaces.Positionable;
import org.andrewberman.ui.ifaces.Sizable;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * The <code>ToolbarItem</code> class is a MenuItem that belongs to a Toolbar
 * object. It represents the "base" MenuItem of a Toolbar, i.e. the actual item
 * that says "File", "Edit", and so on.
 * 
 * @author Greg
 */
public class ToolbarItem extends MenuItem {

    static AffineTransform at = new AffineTransform();

    static RoundRectangle2D.Float buffRoundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    protected static final int LAYOUT_BELOW = 0;

    protected static final int LAYOUT_LEFT = 2;

    protected static final int LAYOUT_RIGHT = 1;

    static RoundRectangle2D.Float roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);

    static final float shortcutTextSize = .75f;

    static Area tri;

    static float triWidth;

    boolean drawChildrenTriangle;

    protected int layoutMode;

    Rectangle2D.Float subItemRect = new Rectangle2D.Float();

    float tWidth, shortcutWidth;

    public ToolbarItem() {
        super();
    }

    protected void calcPreferredSize() {
        super.calcPreferredSize();
        PFont font = getFont();
        float fs = getFontSize();
        float px = getPadX();
        float py = getPadY();
        tWidth = UIUtils.getTextWidth(menu.canvas.g, font, fs, getName(), true);
        float tHeight = UIUtils.getTextHeight(menu.canvas.g, font, fs, "XYZ", true);
        float triangleWidth = 0;
        if (drawChildrenTriangle && items.size() > 0 && layoutMode != LAYOUT_BELOW) {
            at = AffineTransform.getScaleInstance(tHeight / 2f, tHeight / 2f);
            Area tri = (Area) getStyle().get("subTriangle");
            Area a = tri.createTransformedArea(at);
            ToolbarItem.tri = a;
            ToolbarItem.triWidth = (float) a.getBounds2D().getWidth();
            triangleWidth = triWidth + px;
        }
        shortcutWidth = 0;
        if (shortcut != null) {
            shortcutWidth = px + UIUtils.getTextWidth(menu.buff, font, fs * shortcutTextSize, shortcut.label, true);
        }
        setWidth(tWidth + triangleWidth + shortcutWidth + 2 * px);
        setHeight(tHeight + 2 * py);
    }

    protected boolean containsPoint(Point p) {
        float ro = getStyle().getF("f.roundOff");
        buffRoundRect.setRoundRect(x, y, width, height, ro, ro);
        return buffRoundRect.contains(p);
    }

    /**
	 * Normally, the MenuItem's create() method just defers back to the nearest
	 * Menu it can use to create an item, but here we want to change some
	 * options, so let's override it.
	 */
    public MenuItem create(String label) {
        ToolbarItem ti = new ToolbarItem();
        ti.setLayoutMode(ToolbarItem.LAYOUT_RIGHT);
        ti.drawChildrenTriangle = true;
        ti.setName(label);
        return ti;
    }

    protected void drawBefore() {
        if (isOpen() && items.size() > 0) MenuUtils.drawBackgroundRoundRect(this, subItemRect.x, subItemRect.y, subItemRect.width, subItemRect.height);
    }

    protected void drawMyself() {
        float ro = getStyle().getF("f.roundOff");
        Color strokeC = getStrokeColor();
        Stroke stroke = getStroke();
        float px = getStyle().getF("f.padX");
        float py = getStyle().getF("f.padY");
        roundRect.setRoundRect(x, y, width, height, ro, ro);
        Graphics2D g2 = menu.buff.g2;
        if (isOpen() && parent == menu) {
            g2.setPaint(getStyle().getGradient(MenuItem.DOWN, y, y + height));
        } else if (!hasChildren() && getState() == MenuItem.DOWN) {
            g2.setPaint(getStyle().getGradient(MenuItem.DOWN, y, y + height));
        } else g2.setPaint(getStyle().getGradient(getState(), y, y + height));
        if (shouldPerformFill()) {
            g2.fill(roundRect);
            g2.setPaint(strokeC);
            g2.setStroke(stroke);
            g2.draw(roundRect);
        }
        float curX = x + px;
        MenuUtils.drawLeftText(this, getName(), curX);
        curX += tWidth;
        if (shortcut != null) {
            PFont font = getStyle().getFont("font");
            float fs = getStyle().getF("f.fontSize");
            float rightX = getX() + getWidth();
            curX = rightX - shortcutWidth;
            float shortSize = fs * shortcutTextSize;
            float descent = UIUtils.getTextDescent(menu.buff, font, shortSize, true);
            g2.setFont(font.getFont().deriveFont(shortSize));
            g2.setPaint(strokeC.brighter(100));
            float ht = UIUtils.getTextHeight(menu.canvas.g, font, shortSize, shortcut.label, true);
            float yOffset = (height - ht) / 2f + descent;
            yOffset += ht / 2;
            g2.drawString(shortcut.label, curX, y + yOffset);
        }
        curX += shortcutWidth;
        if (drawChildrenTriangle && items.size() > 0) {
            if (layoutMode == LAYOUT_BELOW && getState() != MenuItem.UP && !isOpen()) {
                curX = x + width / 2;
                at.setToIdentity();
                at.translate(curX, y + height + py / 2);
                at.rotate(PApplet.HALF_PI);
                Area a2 = tri.createTransformedArea(at);
                g2.setPaint(strokeC);
                g2.fill(a2);
            } else if (layoutMode != LAYOUT_BELOW) {
                curX = x + width - triWidth - px;
                at.setToIdentity();
                at.translate(curX, y + height / 2);
                Area a2 = tri.createTransformedArea(at);
                g2.setPaint(strokeC);
                g2.fill(a2);
            }
        }
    }

    protected void getRect(Rectangle2D.Float rect, Rectangle2D.Float buff) {
        buff.setFrame(x, y, width, height);
        Rectangle2D.union(rect, buff, rect);
        super.getRect(rect, buff);
    }

    protected void itemMouseEvent(MouseEvent e, Point pt) {
        super.itemMouseEvent(e, pt);
    }

    public synchronized void layout() {
        if (menu == null) return;
        float px = getPadX();
        float py = getPadY();
        float curX = 0, curY = 0;
        switch(layoutMode) {
            case (LAYOUT_BELOW):
                curX = x - py;
                curY = y + height;
                break;
            case (LAYOUT_RIGHT):
            default:
                curX = x + width;
                curY = y - px;
                break;
        }
        subItemRect.x = curX;
        subItemRect.y = curY;
        curX += px;
        curY += py;
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = (MenuItem) items.get(i);
            item.calcPreferredSize();
        }
        float maxWidth = getMaxWidth();
        float maxHeight = getMaxHeight();
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = (MenuItem) items.get(i);
            if (item.isHidden()) continue;
            item.setPosition(curX, curY);
            item.setSize(maxWidth, maxHeight);
            curY += item.getHeight();
        }
        curY += py;
        subItemRect.width = maxWidth + px * 2;
        subItemRect.height = curY - subItemRect.y;
        super.layout();
    }

    protected void setLayoutMode(int layoutMode) {
        this.layoutMode = layoutMode;
    }
}
