package ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.GraphPane;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Anna R. Zhukova
 */
public class SuperVertex extends Vertex {

    private boolean isCollapsed;

    private final Set<SimpleVertex> simpleVertices = new HashSet<SimpleVertex>();

    private static final Color COLOR = Color.BLACK;

    private static final Color SELECTED_COLOR = new Color(192, 192, 192, 50);

    private final String toolTip;

    private int width;

    private final String name;

    public SuperVertex(Point location, String name, String toolTip) {
        this.name = name;
        setLocation(location);
        this.toolTip = toolTip;
    }

    public void addSimpleVertex(SimpleVertex vertex) {
        this.simpleVertices.add(vertex);
    }

    public Set<SimpleVertex> getSimpleVertices() {
        return new HashSet<SimpleVertex>(this.simpleVertices);
    }

    public boolean isCollapsed() {
        return this.isCollapsed;
    }

    public void collapse(boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
    }

    public Point getAbsoluteLocation() {
        return getLocation();
    }

    public void paint(Graphics g, GraphPane graphPane, boolean isSelected) {
        int width = getWidth();
        int height = getHeight();
        Point location = getAbsoluteLocation();
        if (isCollapsed()) {
            width = LETTER_WIDTH * getName().length();
            height = LETTER_HEIGHT + 4;
        }
        g.setFont(FONT);
        g.setColor(SuperVertex.COLOR);
        g.drawRect(location.x, location.y, width, height);
        if (isSelected) {
            g.setColor(SuperVertex.SELECTED_COLOR);
            g.fillRect(location.x + 1, location.y + 1, width - 1, height - 1);
        }
        g.setColor(SuperVertex.COLOR);
        if (isCollapsed()) {
            g.drawString(getName(), location.x + 5, location.y + 5);
        } else {
            int lettersNumber = width / g.getFontMetrics().getWidths()['w'];
            if (getName() == null) {
                return;
            }
            String title = (getName().length() <= lettersNumber) || lettersNumber < 3 ? getName() : getName().substring(0, lettersNumber - 3) + "...";
            g.drawString(title, location.x + 5, location.y + 10);
        }
    }

    public void setHidden(boolean need) {
        super.setHidden(need);
        for (SimpleVertex vertex : getSimpleVertices()) {
            vertex.setHidden(need);
        }
    }

    public String getToolTipText() {
        return toolTip;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return SuperVertex.getVertexHeight();
    }

    public static int getVertexHeight() {
        return Vertex.LETTER_HEIGHT + LABEL_GAP + Y_GAP + SimpleVertex.getVertexHeight();
    }

    public boolean leftBorderTest(Point p) {
        int x = getAbsoluteLocation().x;
        int y = getAbsoluteLocation().y;
        int height = getHeight();
        return (isNear(p.x, x) && isBetween(p.y, y, y + height));
    }

    public boolean rightBorderTest(Point p) {
        int x = getAbsoluteLocation().x;
        int y = getAbsoluteLocation().y;
        int width = getWidth();
        int height = getHeight();
        return (isNear(p.x, x + width) && isBetween(p.y, y, y + height));
    }

    public boolean topBorderTest(Point p) {
        int x = getAbsoluteLocation().x;
        int y = getAbsoluteLocation().y;
        int width = getWidth();
        return (isNear(p.y, y) && isBetween(p.x, x, x + width));
    }

    public boolean bottomBorderTest(Point p) {
        int x = getAbsoluteLocation().x;
        int y = getAbsoluteLocation().y;
        int width = getWidth();
        int height = getHeight();
        return (isNear(p.y, y + height) && isBetween(p.x, x, x + width));
    }

    private static boolean isNear(int a, int b) {
        return Math.abs(a - b) <= 5;
    }

    private static boolean isBetween(int a, int left, int right) {
        return (a - left) >= -5 && (right - a) >= -5;
    }

    private int getMinWidth() {
        int minWidth = 3 * LETTER_WIDTH + 6;
        for (SimpleVertex vertex : this.simpleVertices) {
            if (!vertex.isHidden()) {
                minWidth = Math.max(minWidth, vertex.getWidth() + 4);
            }
        }
        return minWidth;
    }

    private int getMinHeight() {
        int minHeight = LETTER_HEIGHT;
        for (SimpleVertex vertex : this.simpleVertices) {
            if (!vertex.isHidden()) {
                minHeight = Math.max(minHeight, vertex.getHeight() + 4);
            }
        }
        return minHeight;
    }

    public void moveLeftBorder(int dx) {
        int width = getWidth();
        if (dx > 0) {
            dx = Math.min(Math.abs(width - getMinWidth()), dx);
        }
        setWidth(width - dx);
        setLocation(new Point(getAbsoluteLocation().x + dx, getAbsoluteLocation().y));
    }

    public void moveRightBorder(int dx) {
        int width = getWidth();
        if (dx < 0) {
            dx = -Math.min(Math.abs(width - getMinWidth()), -dx);
        }
        setWidth(width + dx);
    }

    public void moveTopBorder(int dy) {
    }

    public void moveBottomBorder(int dy) {
    }

    public String getName() {
        return this.name;
    }
}
