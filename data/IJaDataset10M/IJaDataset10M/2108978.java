package org.nem.layout;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An <code>ActiveLayout</code> arranges components in a directional flow by lines
 * with proportional filling free spaces between components and <code>container's</code> bounds.
 * <p>
 * Each component managed by the <code>ActiveLayout</code> is associated with
 * an instance of {@link ActiveConstraints}.  The constraints object
 * specifies where a component's display area should be located on the flow line
 * and how the component should fill free space.  In addition to its constraints object, the <code>ActiveLayout</code> also
 * considers each component's preferred sizes in order to determine a component's size.
 * </p>
 * <p>
 * To use a active layout effectively, you must customize one or more
 * of the <code>ActiveConstraints</code> objects that are associated
 * with its components. You customize the <code>ActiveConstraints</code>
 * object by setting one or more of its instance variables:
 * </p>
 * <dl>
 * <dt>{@link ActiveConstraints#line}
 * <dd>Specifies the line number, where the component should be placed.
 * The value restricted with predefined size of lines {@link ActiveLayout#ActiveLayout(int, int) }.
 * <dt>{@link ActiveConstraints#hfill}, {@link ActiveConstraints#vfill}
 * <dd>Used when the component's display size
 * is larger or lesser than the component's requested size
 * to determine whether (and how) to resize the component.
 * {@link ActiveConstraints#hfill} - makes the component wide enough to fill its display area
 * horizontally. If several components in the current line should resize their width,
 * this will be done proportionally relatively to each other.
 * {@link ActiveConstraints#vfill} - makes the component tall enough to fill its display area
 * vertically. If several components in the any lines should resize their height,
 * this will be done proportionally relatively to each other.
 * </dl>
 * <p>
 * There are several constants to help you construct <code>ActiveConstraints </code>:
 * <li>{@link ActiveConstraints#HFILL}</li>
 * <li>{@link ActiveConstraints#VFILL}</li>
 * <li>{@link ActiveConstraints#HVFILL}</li>
 * <li>{@link ActiveConstraints#NFILL}</li>
 * </p>
 * <hr>
 * <blockquote>
 * <pre>
 *  import org.nem.ui.form.layout.ActiveLayout;
 *
 *  import javax.swing.*;
 *  import java.awt.*;
 *
 *  public class TestActiveLayout {
 *      public static void main(String[] args) {
 *          final JDialog dialog = new JDialog((Frame) null, "Test Active Layout", true);
 *          JPanel panel = new JPanel(new ActiveLayout(3, 3));
 *          panel.add(new JLabel("Label:"), "0h-");
 *          panel.add(new JScrollPane(new JTextArea("Text Area")), "1hv");
 *          panel.add(new JPanel(), "2h-");
 *          panel.add(new JButton("Button"), "2--");
 *          dialog.setContentPane(panel);
 *          dialog.setSize(new Dimension(320, 240));
 *          dialog.setVisible(true);
 *          System.exit(0);
 *      }
 *  }</pre>
 * </blockquote>
 * <hr>
 * <p/>
 * @author nemyatov
 * @version 1.00, 17/08/09
 */
public class ActiveLayout implements LayoutManager2 {

    private final ArrayList<ArrayList<Component>> compLines = new ArrayList<ArrayList<Component>>();

    private final HashMap<Component, ActiveConstraints> cons = new HashMap<Component, ActiveConstraints>();

    private final int lines;

    private int hgap = 2;

    private int vgap = 2;

    /**
     * Default constructor specifies <code>ActiveLayout</code> with one line of components and 2px gaps between them or bounds of container
     * @see org.nem.layout.ActiveLayout#ActiveLayout(int, int)
     * @see org.nem.layout.ActiveLayout#ActiveLayout(int, int, int)
     */
    public ActiveLayout() {
        this(1, 2);
    }

    /**
     * Constructor specifies <code>ActiveLayout</code> with one line of components
     * @param gap Horisontal and vertical gap between components or bounds of container
     * @see org.nem.layout.ActiveLayout#ActiveLayout(int, int)
     * @see org.nem.layout.ActiveLayout#ActiveLayout(int, int, int)
     */
    public ActiveLayout(int gap) {
        this(1, gap);
    }

    /**
     * Constructor specifies <code>ActiveLayout</code> with several lines of components
     * @param lines Number of lines of components
     * @param gap Horisontal and vertical gap between components or bounds of container
     * @throws IllegalArgumentException If the <code>lines</code> argument is zero or less than zero
     * @see org.nem.layout.ActiveLayout#ActiveLayout(int, int, int)
     */
    public ActiveLayout(int lines, int gap) {
        this(lines, gap, gap);
    }

    /**
     * Constructor specifies <code>ActiveLayout</code> with several lines of components
     * @param lines Number of lines of components
     * @param hgap Horisontal gap between components or bounds of container
     * @param vgap Vertical gap between components or bounds of container
     * @throws IllegalArgumentException If the <code>lines</code> argument is zero or less than zero
     */
    public ActiveLayout(int lines, int hgap, int vgap) {
        if (lines == 0) throw new IllegalArgumentException("Lines argument cannot be zero or less than zero");
        this.lines = lines;
        this.hgap = hgap;
        this.vgap = vgap;
        while (compLines.size() < lines) compLines.add(new ArrayList<Component>());
    }

    /**
     * Adds the specified component to the layout, using the specified <code>constraints</code> object. If <coed>constraints</code>
     * is <code>null</code>, layout manager set up default <code> ActiveConstraints(0) </code>
     * @param comp The component to be added
     * @param constraints An object that determines how the component is added to the layout
     * @throws IllegalArgumentException If <code>constraints</code> specifies line number out of bounds with predefined size of lines
     */
    public void addLayoutComponent(Component comp, Object constraints) {
        ActiveConstraints con = null;
        if (constraints != null) {
            if (constraints instanceof String) con = ActiveConstraints.decode((String) constraints);
            if (constraints instanceof ActiveConstraints) con = (ActiveConstraints) constraints;
        }
        if (con == null) con = new ActiveConstraints(0);
        cons.put(comp, con);
        if (con.line > compLines.size() - 1) throw new IllegalArgumentException("Argument \"line\" out of bounds: " + compLines.size());
        compLines.get(con.line).add(comp);
    }

    /**
     * Returns the maximum dimensions for this layout given the components in the specified target container.
     * @param target The container which needs to be laid out
     * @return The maximum dimensions for this layout
     * @see Container
     * @see #minimumLayoutSize(Container)
     * @see #preferredLayoutSize(Container)
     */
    public Dimension maximumLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int width = 0;
            int height = 0;
            for (ArrayList<Component> comp : compLines) {
                int w = 0;
                int h = 0;
                for (Component c : comp) {
                    Dimension size = c.getMaximumSize();
                    w += hgap + size.width;
                    h = Math.max(vgap + size.height, h);
                }
                width = Math.max(width, w);
                height += h;
            }
            return new Dimension(width + insets.left + insets.right + hgap, height + insets.top + insets.bottom + vgap);
        }
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * <p>
     * @return the value <code>0.5f</code> to indicate centered
     */
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * <p>
     * @return the value <code>0.5f</code> to indicate centered
     */
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    /** Invalidates the layout, indicating that if the layout manager has cached information it should be discarded. */
    public void invalidateLayout(Container target) {
    }

    /** Has no effect, since this layout manager does not use a per-component string. */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from this layout.
     * <p>
     * Most applications do not call this method directly.
     * @param comp The component to be removed.
     * @see java.awt.Container#remove(java.awt.Component)
     * @see java.awt.Container#removeAll()
     */
    public void removeLayoutComponent(Component comp) {
        compLines.get(cons.get(comp).line).remove(comp);
    }

    /**
     * Determines the preferred size of the <code>parent</code> container using this active layout.
     * <p>
     * Most applications do not call this method directly.
     * @param parent The container in which to do the layout
     * @return The preferred size of the <code>parent</code> container
     * @see java.awt.Container#getPreferredSize
     */
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = 0;
            int height = 0;
            for (ArrayList<Component> comp : compLines) {
                int w = 0;
                int h = 0;
                for (Component c : comp) {
                    Dimension size = c.getPreferredSize();
                    w += hgap + size.width;
                    h = Math.max(vgap + size.height, h);
                }
                width = Math.max(width, w);
                height += h;
            }
            return new Dimension(width + insets.left + insets.right + hgap, height + insets.top + insets.bottom + vgap);
        }
    }

    /**
     * Determines the minimum size of the <code>parent</code> container using this active layout.
     * <p>
     * Most applications do not call this method directly.
     * @param parent the container in which to do the layout
     * @return the minimum size of the <code>parent</code> container
     * @see java.awt.Container#doLayout
     */
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = 0;
            int height = 0;
            for (ArrayList<Component> comp : compLines) {
                int w = 0;
                int h = 0;
                for (Component c : comp) {
                    Dimension size = c.getMinimumSize();
                    w += hgap + size.width;
                    h = Math.max(vgap + size.height, h);
                }
                width = Math.max(width, w);
                height += h;
            }
            return new Dimension(width + insets.left + insets.right + hgap, height + insets.top + insets.bottom + vgap);
        }
    }

    private int[] getHeights(int ht) {
        double h = 0;
        double hf = 0;
        double hg = 2 * vgap;
        for (ArrayList<Component> comps : compLines) {
            double hm = 0;
            double hfm = 0;
            for (Component comp : comps) {
                Dimension s = comp.getPreferredSize();
                hm = Math.max(s.getHeight(), hm);
                if (cons.get(comp).vfill) hfm = hm;
            }
            h += hm;
            hf += hfm;
            hg += vgap;
        }
        double k = 1;
        if (hf > vgap) k = (ht - h - hg + hf) / hf;
        int[] heights = new int[lines];
        h = 0;
        int index = -1;
        for (int i = 0; i < heights.length; i++) {
            ArrayList<Component> comps = compLines.get(i);
            double hm = 0;
            for (Component comp : comps) {
                Dimension s = comp.getPreferredSize();
                if (cons.get(comp).vfill) {
                    hm = Math.max(hm, s.getHeight() * k);
                    index = i;
                } else hm = Math.max(hm, s.getHeight());
            }
            heights[i] = (int) Math.round(hm);
            h += heights[i];
        }
        if (index != -1) heights[index] = heights[index] + (int) Math.round(ht - h - hg);
        return heights;
    }

    private int[] getWidths(ArrayList<Component> comps, int wt) {
        double w = 0;
        double wf = 0;
        double wg = 2 * hgap;
        for (Component comp : comps) {
            Dimension s = comp.getPreferredSize();
            w += s.getWidth();
            wg += hgap;
            if (cons.get(comp).hfill) wf += s.getWidth();
        }
        double k = 1;
        if (wf > hgap) k = (wt - w - wg + wf) / wf;
        int[] widths = new int[comps.size()];
        w = 0;
        int index = -1;
        for (int i = 0; i < widths.length; i++) {
            Component c = comps.get(i);
            Dimension s = c.getPreferredSize();
            if (cons.get(c).hfill) {
                widths[i] = (int) Math.round(s.getWidth() * k);
                index = i;
            } else widths[i] = s.width;
            w += widths[i];
        }
        if (index != -1) widths[index] = widths[index] + (int) Math.round(wt - w - wg);
        return widths;
    }

    /**
     * Lays out the specified container using this active layout.
     * This method reshapes components in the specified container in
     * order to satisfy the contraints of this <code>ActiveLayout</code> object.
     * <p>
     * Most applications do not call this method directly.
     * @param parent The container in which to do the layout
     * @see java.awt.Container
     * @see java.awt.Container#doLayout
     */
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            if (parent.getComponentCount() == 0) return;
            int ht = parent.getHeight() - (insets.top + insets.bottom - vgap);
            int wt = parent.getWidth() - (insets.left + insets.right - hgap);
            int y = insets.top + vgap;
            int[] heights = getHeights(ht);
            for (int j = 0; j < lines; j++) {
                ArrayList<Component> comps = compLines.get(j);
                int x = insets.left + hgap;
                int[] widths = getWidths(comps, wt);
                int mh = 0;
                for (int i = 0; i < widths.length; i++) {
                    Component c = comps.get(i);
                    int w = widths[i];
                    int h = (cons.get(c).vfill) ? heights[j] : c.getPreferredSize().height;
                    c.setBounds(x, y, w, h);
                    mh = Math.max(mh, h);
                    x += w + hgap;
                }
                y += mh + vgap;
            }
        }
    }
}
