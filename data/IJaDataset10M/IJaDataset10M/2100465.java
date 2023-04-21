package org.SCAraide.gcd.artifacts.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;
import javax.swing.JComponent;
import org.SCAraide.gcd.artifacts.Property;
import org.SCAraide.gcd.artifacts.Reference;
import org.SCAraide.gcd.artifacts.Service;

/**
 *
 * @author jaydee
 */
public abstract class BaseComp extends JComponent implements SelectableArtefact {

    protected static final int ARC_WIDTH = 46;

    protected static final int MARGIN = 46;

    protected static final int H_GAP = 10;

    protected static final int V_GAP = 10;

    protected static final Font FONT = new Font("Dialog", Font.BOLD, 14);

    protected static final int ADDED_COLOR = 50;

    protected static final Color TEXT_COLOR = Color.BLACK;

    protected static final Color TEXT_COLOR_SELECTED = new Color(Math.min(255, TEXT_COLOR.getRed() + ADDED_COLOR), Math.min(255, TEXT_COLOR.getGreen() + ADDED_COLOR), Math.min(255, TEXT_COLOR.getBlue() + ADDED_COLOR));

    protected static final Color BORDER_COLOR = Color.BLACK;

    protected static final Color BORDER_COLOR_SELECTED = new Color(Math.min(255, BORDER_COLOR.getRed() + ADDED_COLOR), Math.min(255, BORDER_COLOR.getGreen() + ADDED_COLOR), Math.min(255, BORDER_COLOR.getBlue() + ADDED_COLOR));

    protected final Color bgColor;

    protected final Color bgColorSelected;

    protected String name;

    protected int nameWidth;

    protected int nameHeight;

    protected int nameDescent;

    protected boolean selected;

    protected int zOrder;

    protected MovableArtefact movable;

    protected SelectionRectangular selection;

    protected Vector<Property> properties;

    protected Vector<Service> services;

    protected Vector<Reference> references;

    public BaseComp(Color bg, Color bgSelected) {
        bgColor = bg;
        bgColorSelected = bgSelected;
        init();
    }

    /**
     * Get the value of Name
     *
     * @return the value of Name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the value of Name
     *
     * @param Name new value of Name
     */
    @Override
    public void setName(String Name) {
        this.name = Name;
        FontMetrics fm = getFontMetrics(FONT);
        nameWidth = fm.stringWidth(name);
        nameHeight = fm.getHeight();
        nameDescent = fm.getDescent();
        setSize(computeSize());
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            zOrder = getParent().getComponentZOrder(this);
            getParent().setComponentZOrder(this, 0);
        } else {
            getParent().setComponentZOrder(this, zOrder);
        }
        selection.setSelected(selected);
        repaint();
    }

    private void init() {
        setDoubleBuffered(true);
        setOpaque(false);
        setFont(FONT);
        setBackground(new Color(255, 255, 255, 0));
        name = "";
        nameWidth = 0;
        nameHeight = 0;
        nameDescent = 0;
        selected = false;
        movable = new MovableArtefact();
        movable.linkToComponent(this);
        selection = new SelectionRectangular(this, true);
        selection.setLocation(0, 0);
        selection.setPreferredSize(getPreferredSize());
        selection.setSize(getSize());
        add(selection);
        properties = new Vector<Property>();
        services = new Vector<Service>();
        references = new Vector<Reference>();
    }

    public void add(Property property) {
        properties.insertElementAt(property, property.getOrder());
        property.setLocation(property.getOrder() * (Property.SIZE.width + H_GAP) + (MARGIN / 2) + H_GAP, 0);
        super.add(property);
        setSize(computeSize());
    }

    public void add(Service service) {
        services.insertElementAt(service, service.getOrder());
        service.setLocation(0, service.getOrder() * (Service.SIZE.height + V_GAP) + (MARGIN / 2) + V_GAP);
        super.add(service);
        setSize(computeSize());
    }

    public void add(Reference reference) {
        references.insertElementAt(reference, reference.getOrder());
        reference.setLocation(getWidth() - reference.getWidth(), reference.getOrder() * (Reference.SIZE.height + V_GAP) + (MARGIN / 2) + V_GAP);
        super.add(reference);
        setSize(computeSize());
    }

    @Override
    public void paintComponent(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2D = (Graphics2D) g;
            Color effectiveBgColor = null;
            Color textColor = null;
            Color borderColor = null;
            if (!isSelected()) {
                effectiveBgColor = bgColor;
                textColor = TEXT_COLOR;
                borderColor = BORDER_COLOR;
            } else {
                effectiveBgColor = bgColorSelected;
                textColor = TEXT_COLOR_SELECTED;
                borderColor = BORDER_COLOR_SELECTED;
            }
            int marginTop = properties.isEmpty() ? 0 : (Property.SIZE.height / 2);
            int marginLeft = services.isEmpty() ? 0 : (Service.SIZE.width * 3 / 5);
            int marginRight = references.isEmpty() ? 0 : (Reference.SIZE.width * 3 / 5);
            int top = Math.max(Anchor.HALF_SIZE, marginTop) + 1;
            int left = Math.max(Anchor.HALF_SIZE, marginLeft) + 1;
            int width = getWidth() - Math.max(Anchor.SIZE, marginLeft + marginRight) - 3;
            int height = getHeight() - Math.max(Anchor.SIZE, marginTop) - 3;
            g2D.setColor(effectiveBgColor);
            g2D.fillRoundRect(left, top, width, height, ARC_WIDTH, ARC_WIDTH);
            g2D.setColor(borderColor);
            g2D.drawRoundRect(left, top, width, height, ARC_WIDTH, ARC_WIDTH);
            super.paintComponent(g);
            drawText(g2D, textColor);
        }
    }

    protected abstract void drawText(Graphics2D g2D, Color textColor);

    @Override
    public void setBounds(int x, int y, int width, int height) {
        if (isSizeOK(width, height)) {
            int maxWidth = Math.max(width, getMinimumSize().width);
            int maxHeight = Math.max(height, getMinimumSize().height);
            selection.setBounds(0, 0, maxWidth, maxHeight);
            for (Reference ref : references) {
                ref.setLocation(maxWidth - ref.getWidth(), ref.getOrder() * (Reference.SIZE.height + V_GAP) + (MARGIN / 2) + V_GAP);
            }
            super.setBounds((maxWidth == width) ? x : getX(), (maxHeight == height) ? y : getY(), maxWidth, maxHeight);
        }
    }

    protected boolean isSizeOK(int width, int height) {
        if ((height < getMinimumSize().height) && (width < getMinimumSize().width)) {
            return false;
        } else {
            return true;
        }
    }

    protected Dimension computeSize() {
        int minWidth = Math.max(nameWidth + MARGIN, properties.size() * (Property.SIZE.width + H_GAP) + (properties.isEmpty() ? 0 : H_GAP) + MARGIN) + Reference.SIZE.width + Service.SIZE.width;
        int minHeight = Math.max(Math.max(references.size() * (Reference.SIZE.height + V_GAP) + (references.isEmpty() ? 0 : V_GAP) + MARGIN, services.size() * (Service.SIZE.height + V_GAP) + (services.isEmpty() ? 0 : V_GAP) + MARGIN), nameHeight + MARGIN);
        Dimension minSize = new Dimension(minWidth, minHeight);
        setMinimumSize(minSize);
        setPreferredSize(minSize);
        return minSize;
    }

    protected Dimension getMaxSize(Dimension... dimensions) {
        Dimension max = new Dimension();
        for (Dimension dim : dimensions) {
            max.height = Math.max(max.height, dim.height);
            max.width = Math.max(max.width, dim.width);
        }
        return max;
    }
}
