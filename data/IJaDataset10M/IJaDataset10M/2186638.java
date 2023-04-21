package genj.option;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import genj.gedcom.*;
import genj.util.swing.ImgIconConverter;

/**
 * Option - Layout Properties in Box
 */
public class OptionLayoutProperties extends Option implements MouseMotionListener, ChangeListener {

    private JLabel lEntity;

    private JSlider sGrid;

    private boolean resizing = false;

    private Point start;

    private static int grid = 0;

    /**
   * Constructor for Option
   */
    public OptionLayoutProperties(JFrame frame) {
        super(frame);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        lEntity = new JLabel() {

            /** painting */
            public void paint(Graphics g) {
                int h = getHeight(), w = getWidth();
                g.setColor(Color.white);
                g.fillRect(0, 0, w - 1, h - 1);
                if (grid > 1) {
                    g.setColor(new Color(240, 240, 240));
                    for (int x = grid * 3; x < w; x += grid * 3) g.drawLine(x, 0, x, h);
                    for (int y = grid * 3; y < h; y += grid * 3) g.drawLine(0, y, w, y);
                }
                super.paint(g);
            }
        };
        lEntity.setBorder(BorderFactory.createLineBorder(Color.black));
        lEntity.addMouseMotionListener(this);
        add(lEntity);
        sGrid = new JSlider(JSlider.VERTICAL, 0, 8, grid);
        sGrid.addChangeListener(this);
        sGrid.setToolTipText("Choose grid setting");
        add(sGrid);
    }

    /**
   * Adds a property to be laid out
   */
    public void add(TagPath path, Rectangle box) {
        JComponent c = null;
        int maxy = 0, maxx = 0;
        for (int i = 0; i < lEntity.getComponentCount(); i++) {
            c = (JComponent) lEntity.getComponent(i);
            if (c.getClientProperty("PATH").toString().equals(path.asString())) return;
            maxy = Math.max(maxy, c.getLocation().y + c.getSize().height);
            maxx = Math.max(maxx, c.getLocation().x + c.getSize().width);
        }
        JLabel l = new JLabel(path.asString(), ImgIconConverter.get(Property.getDefaultImage(path.getLast())), JLabel.CENTER);
        l.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        l.setHorizontalAlignment(JLabel.LEFT);
        if (box != null) l.setBounds(box); else {
            l.setSize(l.getPreferredSize());
            if (maxy > lEntity.getSize().height - l.getSize().height) {
                maxx = Math.min(maxx, lEntity.getSize().width - l.getSize().width);
                l.setLocation(new Point(maxx, lEntity.getSize().height - l.getSize().height));
            } else {
                l.setLocation(new Point(1, maxy));
            }
        }
        l.setFont(getFont());
        l.putClientProperty("PATH", path);
        lEntity.add(l, 0);
        l.addMouseMotionListener(this);
        l.repaint();
    }

    /**
   * Layout out the enclosed components
   */
    public void doLayout() {
        Dimension dim = sGrid.getPreferredSize();
        Insets i = getInsets();
        sGrid.setLocation(getSize().width - i.right - dim.width, i.top);
        sGrid.setSize(dim.width, getSize().height - i.top - i.bottom);
        dim = sGrid.getSize();
        lEntity.setLocation((getWidth() - dim.width) / 2 - lEntity.getSize().width / 2, getHeight() / 2 - lEntity.getSize().height / 2);
    }

    /**
   * Return the box for a path.
   */
    public Rectangle getBoxForPath(TagPath path) {
        JComponent c = null;
        for (int i = 0; i < lEntity.getComponentCount(); i++) {
            c = (JComponent) lEntity.getComponent(i);
            if (c.getClientProperty("PATH").toString().equals(path.asString())) {
                return c.getBounds();
            }
        }
        return null;
    }

    /**
   * Override Preferred Size
   */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
   * Override Preferred Size
   */
    public Dimension getPreferredSize() {
        return new Dimension(256, 128);
    }

    /**
   * Returns size of entities' boxes
   */
    public Dimension getSizeOfEntities() {
        return lEntity.getSize();
    }

    /**
   * Returns all layouted TagPaths
   */
    public TagPath[] getTagPaths() {
        JComponent c = null;
        int count = lEntity.getComponentCount();
        TagPath[] result = new TagPath[count];
        for (int i = 0; i < count; i++) {
            c = (JComponent) lEntity.getComponent(i);
            result[i] = (TagPath) c.getClientProperty("PATH");
        }
        return result;
    }

    /**
   * Fired when user dragges one of the components.
   * Resizes or moves that component.
   */
    public void mouseDragged(MouseEvent e) {
        Component c = (Component) e.getSource();
        Rectangle r = c.getBounds();
        Dimension d;
        if (c == lEntity) {
            if (!resizing) return;
            int minwidth = 48, minheight = 16;
            Point p;
            for (int i = 0; i < lEntity.getComponentCount(); i++) {
                c = lEntity.getComponent(i);
                p = c.getLocation();
                d = c.getSize();
                minwidth = Math.max(p.x + d.width + 1, minwidth);
                minheight = Math.max(p.y + d.height + 1, minheight);
            }
            d = new Dimension(getSize().width - sGrid.getSize().width, getSize().height);
            Insets isets = getInsets();
            int w = Math.min(Math.max(minwidth, e.getX()), d.width - r.x - isets.left - isets.right), h = Math.min(Math.max(minheight, e.getY()), d.height - r.y - isets.top - isets.bottom);
            lEntity.setSize(new Dimension(w, h));
            lEntity.setLocation(d.width / 2 - w / 2, d.height / 2 - h / 2);
            return;
        }
        d = lEntity.getSize();
        int g = sGrid.getValue(), x = e.getX(), y = e.getY();
        if (resizing) {
            if (g > 0) {
                x = ((int) (r.x + x) / (g * 3)) * (g * 3) - r.x;
                y = ((int) (r.y + y) / (g * 3)) * (g * 3) - r.y;
            }
            int w = Math.min(Math.max(8, x), d.width - r.x - 1), h = Math.min(Math.max(8, y), d.height - r.y - 1);
            c.setSize(new Dimension(w, h));
        } else {
            int posx = Math.min(Math.max(1, r.x - start.x + x), d.width - r.width - 1), posy = Math.min(Math.max(1, r.y - start.y + y), d.height - r.height - 1);
            if (g > 0) {
                posx = ((int) posx / (g * 3)) * (g * 3) + 1;
                posy = ((int) posy / (g * 3)) * (g * 3) + 1;
            }
            c.setLocation(posx, posy);
        }
    }

    /**
   * Fired when the users moves the mouse of one of the labels.
   * Prepares dragging/resizing.
   */
    public void mouseMoved(MouseEvent e) {
        JComponent l = (JComponent) e.getSource();
        start = new Point(e.getX(), e.getY());
        if ((e.getX() > l.getSize().width - 8) && (e.getY() > l.getSize().height - 8)) {
            l.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            resizing = true;
        } else {
            l.setCursor(Cursor.getPredefinedCursor(l == lEntity ? Cursor.DEFAULT_CURSOR : Cursor.MOVE_CURSOR));
            resizing = false;
        }
    }

    /**
   * Removes a property that has been laid out
   */
    public void remove(TagPath path) {
        JComponent c = null;
        for (int i = 0; i < lEntity.getComponentCount(); i++) {
            c = (JComponent) lEntity.getComponent(i);
            if (c.getClientProperty("PATH").toString().equals(path.asString())) break;
        }
        if (c != null) {
            lEntity.remove(c);
            lEntity.repaint();
        }
    }

    /**
   * Removes all properties that have been laid out
   */
    public void removeAll() {
        lEntity.removeAll();
        lEntity.repaint();
    }

    /**
   * Sets Size of entities' boxes
   */
    public void setSizeOfEntities(Dimension size) {
        lEntity.setSize(size);
        doLayout();
    }

    /**
   * Called in case slider value is changed by user
   */
    public void stateChanged(ChangeEvent e) {
        grid = sGrid.getValue();
        lEntity.repaint();
    }
}
