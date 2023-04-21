package com.petersoft.advancedswing.carousel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.Serializable;
import java.util.Vector;

public class CarouselLayout implements LayoutManager2, Serializable {

    private int hgap;

    private int vgap;

    Vector vector = new Vector();

    class Card implements Serializable {

        static final long serialVersionUID = 6640330810709497518L;

        public String name;

        public Component comp;

        public Card(String cardName, Component cardComponent) {
            name = cardName;
            comp = cardComponent;
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        synchronized (comp.getTreeLock()) {
            if (!vector.isEmpty()) {
                comp.setVisible(false);
            }
            for (int i = 0; i < vector.size(); i++) {
                if (((Card) vector.get(i)).name.equals(name)) {
                    ((Card) vector.get(i)).comp = comp;
                    return;
                }
            }
            vector.add(new Card(name, comp));
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int w = 0;
            int h = 0;
            for (int i = 0; i < ncomponents; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getPreferredSize();
                if (d.width > w) {
                    w = d.width;
                }
                if (d.height > h) {
                    h = d.height;
                }
            }
            return new Dimension(insets.left + insets.right + w + hgap * 2, insets.top + insets.bottom + h + vgap * 2);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int w = 0;
            int h = 0;
            for (int i = 0; i < ncomponents; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getMinimumSize();
                if (d.width > w) {
                    w = d.width;
                }
                if (d.height > h) {
                    h = d.height;
                }
            }
            return new Dimension(insets.left + insets.right + w + hgap * 2, insets.top + insets.bottom + h + vgap * 2);
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            Component comp = null;
            boolean currentFound = false;
            for (int i = 0; i < ncomponents; i++) {
                comp = parent.getComponent(i);
                if (i == 0) {
                    comp.setBounds(hgap + insets.left, vgap + insets.top, parent.getSize().width - (hgap * 2 + insets.left + insets.right), parent.getSize().height - (vgap * 2 + insets.top + insets.bottom));
                } else {
                    comp.setBounds(hgap + insets.left, vgap + insets.top, parent.getPreferredSize().width - (hgap * 2 + insets.left + insets.right), parent.getPreferredSize().height - (vgap * 2 + insets.top + insets.bottom));
                }
                if (comp.isVisible()) {
                    currentFound = true;
                }
            }
            if (!currentFound && ncomponents > 0) {
                parent.getComponent(0).setVisible(true);
            }
        }
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        synchronized (comp.getTreeLock()) {
            if (constraints instanceof String) {
                addLayoutComponent((String) constraints, comp);
            } else {
                throw new IllegalArgumentException("cannot add to layout: constraint must be a string");
            }
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    public void first(Container parent) {
        synchronized (parent.getTreeLock()) {
            checkLayout(parent);
            int ncomponents = parent.getComponentCount();
            for (int i = 0; i < ncomponents; i++) {
                Component comp = parent.getComponent(i);
                if (comp.isVisible()) {
                    comp.setVisible(false);
                    break;
                }
            }
            if (ncomponents > 0) {
                parent.getComponent(0).setVisible(true);
                parent.validate();
            }
        }
    }

    void checkLayout(Container parent) {
        if (parent.getLayout() != this) {
            throw new IllegalArgumentException("wrong parent for CardLayout");
        }
    }

    public void show(Container parent, String name) {
        synchronized (parent.getTreeLock()) {
            checkLayout(parent);
            Component next = null;
            int ncomponents = vector.size();
            for (int i = 0; i < ncomponents; i++) {
                Card card = (Card) vector.get(i);
                if (card.name.equals(name)) {
                    next = card.comp;
                    break;
                }
            }
            if ((next != null) && !next.isVisible()) {
                ncomponents = parent.getComponentCount();
                for (int i = 0; i < ncomponents; i++) {
                    Component comp = parent.getComponent(i);
                    if (comp.isVisible()) {
                        comp.setVisible(false);
                        break;
                    }
                }
                next.setVisible(true);
                parent.validate();
            }
        }
    }
}
