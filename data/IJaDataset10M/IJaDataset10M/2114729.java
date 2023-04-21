package quj;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.print.*;

/**
 * Contains Boxes, and draws lines between them.
 **/
class BoxPanel extends JComponent implements MoveListener, Printable {

    private transient UmlLayout myLayout;

    private transient Dimension offscreensize;

    private transient FontMetrics fm;

    private transient Graphics offgraphics;

    private transient Image offscreen;

    private Box selectedBox;

    private UMLEditor editor;

    /**
     * A panel for putting Box objects. This uses a custom layout that
     * knows how to draw lines between Boxes as appropriate.
     **/
    BoxPanel(UMLEditor editor_) {
        editor = editor_;
        myLayout = new UmlLayout();
        setLayout(myLayout);
        setItems(editor.getItems());
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        System.out.println("printing is screwed up");
        Dimension bp = getSize();
        Dimension paper = new Dimension((int) pageFormat.getPaper().getImageableWidth(), (int) pageFormat.getPaper().getImageableHeight());
        int columns = (int) Math.ceil(bp.width / paper.width);
        int rows = (int) Math.ceil(bp.height / paper.height);
        int numPages = (columns * rows) - 1;
        if (pageIndex > numPages) return Printable.NO_SUCH_PAGE;
        int colMultiplier = (pageIndex / columns);
        int rowMultiplier = (pageIndex % columns);
        Graphics2D g2 = (Graphics2D) graphics;
        int xStart = colMultiplier * paper.width;
        int yStart = rowMultiplier * paper.height;
        g2.drawImage(offscreen, xStart, yStart, paper.width, paper.height, null);
        return Printable.PAGE_EXISTS;
    }

    public void paintComponent(Graphics g) {
        initSelf();
        offgraphics.setColor(Color.white);
        offgraphics.setClip(0, 0, getSize().width, getSize().height);
        offgraphics.fillRect(0, 0, getSize().width, getSize().height);
        offgraphics.setColor(Color.black);
        Hashtable points = myLayout.getPoints();
        Enumeration keys = points.keys();
        Point parent, child;
        while (keys.hasMoreElements()) {
            child = (Point) keys.nextElement();
            parent = (Point) points.get(child);
            offgraphics.drawLine(child.x, child.y, parent.x, parent.y);
        }
        g.drawImage(offscreen, 0, 0, null);
    }

    public Dimension getSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        Component[] all = getComponents();
        Box b;
        int maxWidth = 0;
        int maxHeight = 0;
        int w, h;
        for (int i = 0; i < all.length; i++) {
            if (all[i] instanceof Box) {
                b = (Box) all[i];
                w = b.getLocation().x + b.getSize().width;
                h = b.getLocation().y + b.getSize().height;
                if (w > maxWidth) maxWidth = w;
                if (h > maxHeight) maxHeight = h;
            }
        }
        if ((maxWidth < 100) || (maxHeight < 100)) return new Dimension(100, 100);
        return new Dimension(maxWidth + 4, maxHeight + 4);
    }

    public void moveEvent(MoveEvent ev) {
        Box who = ev.getMoved();
        Point current = who.getLocation();
        int newX = current.x + ev.getX();
        int newY = current.y + ev.getY();
        who.setLocation(new Point(newX, newY));
        setSize(getPreferredSize());
        getParent().validate();
        doLayout();
        repaint();
        getParent().doLayout();
    }

    private void initSelf() {
        if ((offscreen == null) || (offscreensize == null) || (offscreensize.width != getSize().width) || (offscreensize.height != getSize().height)) {
            Dimension d = getSize();
            offscreensize = d;
            offscreen = createImage(d.width, d.height);
            offgraphics = offscreen.getGraphics();
            offgraphics.setFont(getFont());
            offgraphics.setColor(getBackground());
            offgraphics.fillRect(0, 0, d.width, d.height);
        }
    }

    void setItems(Vector items) {
        removeAll();
        Item item;
        for (int i = 0; i < items.size(); i++) {
            addItemPrivate((Item) items.elementAt(i));
        }
        doLayout();
        repaint();
    }

    void savePositions() {
        Component[] all = getComponents();
        Box box;
        Item item;
        for (int i = 0; i < all.length; i++) {
            if (all[i] instanceof Box) {
                box = (Box) all[i];
                item = box.getItem();
                item.setX(box.getLocation().x);
                item.setY(box.getLocation().y);
            }
        }
    }

    void setCurrent(Item current) {
        Component[] all = getComponents();
        Box box;
        for (int i = 0; i < all.length; i++) {
            if (all[i] instanceof Box) {
                box = (Box) all[i];
                if (box.getItem() == current) {
                    if (getSelectedBox() != null) getSelectedBox().setSelected(false);
                    box.setSelected(true);
                    setSelectedBox(box);
                    return;
                }
            }
        }
    }

    Box getSelectedBox() {
        return selectedBox;
    }

    void setSelectedBox(Box selected) {
        selectedBox = selected;
    }

    void addItem(Item item) {
        addItemPrivate(item);
        doLayout();
        repaint();
    }

    private void addItemPrivate(Item item) {
        Box b = new Box();
        b.setLocation(new Point(item.getX(), item.getY()));
        b.setItem(item);
        b.addMoveListener(this);
        b.addSelectionListener(editor);
        b.needsRedraw();
        add(b);
    }

    void removeItem(Item item) {
        Component[] all = getComponents();
        Box box;
        for (int i = 0; i < all.length; i++) {
            if (all[i] instanceof Box) {
                box = (Box) all[i];
                if (box.getItem() == item) {
                    remove(box);
                    return;
                }
            }
        }
    }
}
