package physicssite.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JComponent;
import javax.swing.border.Border;
import physicssite.util.Legend;

/**
 * ein JComponent, welcher seine Kinder in einem Gitter anordnet.
 * 
 * @author Peter Güttinger
 */
@SuppressWarnings("serial")
public class GridJComponent extends JComponent implements LayoutManager2 {

    private int[] size = { 1, 1 };

    /**
	 * Gewichtung der Spalten/Zeilen.<br>
	 * fängt bei 0 an und geht bis 1<br>
	 * und hat somit 1 Element mehr als es Zeilen/Spalten gibt.
	 */
    public double[][] weights = { { 0, 1 }, { 0, 1 } };

    /**
	 * gleich wie weights, geht aber von 0 bis zu einem unbestimmten Wert.<br>
	 * beim Setzen müssen nur die Minima der einzelnen Zeilen/Spalten angegeben werden,<br>
	 * diese werden dann automatisch in dieses Format umgewandelt.
	 */
    private final int[][] mins = { { 0, 0 }, { 0, 0 } };

    /**
	 * Abstand zwischen den Zeilen/Spalten, jeweils addiert wie bei mins
	 */
    private final int[][] spacing = { { 0, 0 }, { 0, 0 } };

    private final boolean checkMins = true;

    private static final class Constraints {

        Constraints(final int x, final int y) {
            pos[0] = x;
            pos[1] = y;
        }

        Constraints(final int x, final int y, final int xspan, final int yspan) {
            pos[0] = x;
            pos[1] = y;
            span[0] = xspan;
            span[1] = yspan;
        }

        int[] pos = { 0, 0 };

        int[] span = { 1, 1 };
    }

    private final HashMap<Component, Constraints> components = new HashMap<Component, Constraints>();

    public GridJComponent() {
        this.setLayout(this);
    }

    final void easyLayout(final String[] layout, final Legend legend) {
        size = new int[] { layout[0].length(), layout.length };
        if (mins[0].length <= size[0]) mins[0] = new int[size[0] + 1];
        if (mins[1].length <= size[1]) mins[1] = new int[size[1] + 1];
        if (spacing[0].length <= size[0]) spacing[0] = new int[size[0] + 1];
        if (spacing[1].length <= size[1]) spacing[1] = new int[size[1] + 1];
        if (weights[0].length <= size[0]) {
            weights[0] = new double[size[0] + 1];
            for (int i = 0; i < weights[0].length; i++) weights[0][i] = 1. * i / (weights[0].length - 1);
        }
        if (weights[1].length <= size[1]) {
            weights[1] = new double[size[1] + 1];
            for (int i = 0; i < weights[1].length; i++) weights[1][i] = 1. * i / (weights[1].length - 1);
        }
        final ArrayList<Character> done = new ArrayList<Character>(legend.size());
        for (int y = 0; y < size[1]; y++) {
            if (size[0] != layout[y].length()) throw new IllegalArgumentException("invalid layout");
            for (int x = 0; x < layout[y].length(); x++) {
                final Character c = layout[y].charAt(x);
                if (c == ' ' || done.contains(c)) continue;
                done.add(c);
                if (legend.get(c) == null) throw new IllegalArgumentException("" + c);
                int xspan, yspan;
                for (xspan = 1; x + xspan < size[0]; xspan++) if (layout[y].charAt(x + xspan) != c) break;
                for (yspan = 1; y + yspan < size[1]; yspan++) if (layout[y + yspan].charAt(x) != c) break;
                this.add(legend.get(c), x, y, xspan, yspan);
                x += xspan - 1;
            }
        }
        checkMins();
        checkBorder();
    }

    final void easyLayout(final String[] layout, final Legend legend, final double[] xWeights, final double[] yWeights) {
        if (layout.length > yWeights.length - 1 || layout[0].length() > xWeights.length - 1) throw new IllegalArgumentException("too few weights specified");
        this.weights[0] = xWeights;
        this.weights[1] = yWeights;
        easyLayout(layout, legend);
    }

    final void easyLayout(final String[] layout, final Legend legend, final int[] xSpacing, final int[] ySpacing) {
        if (layout.length >= ySpacing.length || layout[0].length() >= xSpacing.length) throw new IllegalArgumentException("too few spacings specified");
        setSpacing(xSpacing, ySpacing);
        easyLayout(layout, legend);
    }

    void easyLayout(final String[] layout, final Legend legend, final double[] xWeights, final double[] yWeights, final int[] xSpacing, final int[] ySpacing) {
        if (layout.length >= ySpacing.length || layout[0].length() >= xSpacing.length) throw new IllegalArgumentException("too few spacings specified");
        setSpacing(xSpacing, ySpacing);
        easyLayout(layout, legend, xWeights, yWeights);
    }

    public final void easyLayout(final String[] layout, final Legend legend, final double[] xWeights, final double[] yWeights, final int[] xSpacing, final int[] ySpacing, final int[] xMins, final int[] yMins) {
        if (layout.length > yMins.length || layout[0].length() > xMins.length) throw new IllegalArgumentException("too few min values specified");
        setMins(xMins, yMins);
        easyLayout(layout, legend, xWeights, yWeights, xSpacing, ySpacing);
    }

    public final void setDim(final int x, final int y) {
        size = new int[] { x, y };
        if (mins[0].length <= x) mins[0] = new int[x + 1];
        if (mins[1].length <= y) mins[1] = new int[y + 1];
        if (spacing[0].length <= x) spacing[0] = new int[x + 1];
        if (spacing[1].length <= y) spacing[1] = new int[y + 1];
        if (weights[0].length <= x) {
            weights[0] = new double[x + 1];
            for (int i = 0; i < weights[0].length; i++) weights[0][i] = 1. * i / (weights[0].length - 1);
        }
        if (weights[1].length <= y) {
            weights[1] = new double[y + 1];
            for (int i = 0; i < weights[1].length; i++) weights[1][i] = 1. * i / (weights[1].length - 1);
        }
    }

    public final int[] getDim() {
        return size.clone();
    }

    public final int getDimX() {
        return size[0];
    }

    public final int getDimY() {
        return size[1];
    }

    @Override
    public void layoutContainer(final Container c) {
        assert c == this;
        if (this.getWidth() <= 0 || this.getHeight() <= 0) return;
        final int[] free = { Math.max(0, this.getWidth() - mins[0][mins[0].length - 1] - spacing[0][spacing[0].length - 1]), Math.max(0, this.getHeight() - mins[1][mins[1].length - 1] - spacing[1][spacing[1].length - 1]) };
        for (final Entry<Component, Constraints> e : components.entrySet()) {
            final Constraints cs = e.getValue();
            final int[] pos = new int[2], size = new int[2];
            for (final int o : new int[] { 0, 1 }) {
                pos[o] = spacing[o][cs.pos[o]] + mins[o][cs.pos[o]] + (int) (free[o] * weights[o][cs.pos[o]]);
                size[o] = mins[o][cs.pos[o] + cs.span[o]] - mins[o][cs.pos[o]] + spacing[o][cs.pos[o] + cs.span[o] - 1] - spacing[o][cs.pos[o]] + (int) (free[o] * (weights[o][cs.pos[o] + cs.span[o]] - weights[o][cs.pos[o]]));
            }
            e.getKey().setBounds(pos[0], pos[1], size[0], size[1]);
        }
    }

    @Override
    public void addLayoutComponent(final String name, final Component c) {
    }

    @Override
    public void addLayoutComponent(final Component comp, final Object c) {
        if (c == null) return;
        if (!(c instanceof Constraints)) throw new IllegalArgumentException("constraints must be of class GridJComponent.Constraints");
        components.put(comp, (Constraints) c);
        checkMins();
    }

    @Override
    public void removeLayoutComponent(final Component c) {
        components.remove(c);
    }

    protected void add(final Component c, final int x, final int y) {
        if (x >= size[0] || y >= size[1]) throw new IllegalArgumentException("outside grid");
        components.put(c, new Constraints(x, y));
        this.add(c);
        checkMins();
    }

    public void add(final Component c, final int x, final int y, final int xspan, final int yspan) {
        if (x + xspan > size[0] || y + yspan > size[1]) throw new IllegalArgumentException("outside grid");
        components.put(c, new Constraints(x, y, xspan, yspan));
        this.add(c);
        checkMins();
    }

    protected void move(final Component c, final int x, final int y) {
        if (x >= size[0] || y >= size[1]) throw new IllegalArgumentException("outside grid");
        final Constraints cs = components.get(c);
        cs.pos[0] = x;
        cs.pos[1] = y;
        checkMins();
    }

    protected void move(final Component c, final int x, final int y, final int xspan, final int yspan) {
        if (x + xspan > size[0] || y + yspan > size[1]) throw new IllegalArgumentException("outside grid");
        final Constraints cs = components.get(c);
        cs.pos[0] = x;
        cs.pos[1] = y;
        cs.span[0] = xspan;
        cs.span[1] = yspan;
        checkMins();
    }

    public void checkMins() {
        if (!checkMins) return;
        for (final Entry<Component, Constraints> e : components.entrySet()) {
            if (!e.getKey().isVisible()) continue;
            e.getKey().validate();
            final Constraints cs = e.getValue();
            final Dimension d = e.getKey().getMinimumSize();
            final int[] a = { d.width - (mins[0][cs.pos[0] + cs.span[0]] - mins[0][cs.pos[0]]), d.height - (mins[1][cs.pos[1] + cs.span[1]] - mins[1][cs.pos[1]]) };
            for (final int o : new int[] { 0, 1 }) {
                if (a[o] > 0) {
                    for (int i = cs.pos[o] + 1; i < mins[o].length; i++) {
                        if (i >= cs.pos[o] + cs.span[o]) mins[o][i] += a[o]; else mins[o][i] += (int) (a[o] * ((weights[o][i] - weights[o][cs.pos[o]]) / (weights[o][cs.pos[o] + cs.span[o]] - weights[o][cs.pos[o]])));
                    }
                }
            }
        }
    }

    protected void setMins(final int[] xMins, final int[] yMins) {
        setMins(xMins, 0);
        setMins(yMins, 1);
    }

    protected void setMins(final int[][] mins) {
        setMins(mins[0], 0);
        setMins(mins[1], 1);
    }

    public void setXMins(final int[] xMins) {
        setMins(xMins, 0);
    }

    protected void setYMins(final int[] yMins) {
        setMins(yMins, 1);
    }

    protected void setMins(final int[] mins, final int o) {
        if (mins.length < size[o]) throw new IllegalArgumentException("too few mins for " + (o == 0 ? "x" : "y") + " axis");
        final int[] m = new int[mins.length + 1];
        for (int j = 0; j < mins.length; j++) {
            for (int i = j; i < mins.length; i++) {
                m[i + 1] += mins[j];
            }
        }
        this.mins[o] = m;
        checkMins();
    }

    public void setSpacing(final int[] xSpacing, final int[] ySpacing) {
        setSpacing(xSpacing, 0);
        setSpacing(ySpacing, 1);
    }

    protected void setSpacing(final int[][] spacing) {
        setSpacing(spacing[0], 0);
        setSpacing(spacing[1], 1);
    }

    public void setSpacing(final int[] spacing, final int o) {
        if (spacing.length <= size[o]) throw new IllegalArgumentException("too few spacings for " + (o == 0 ? "x" : "y") + " axis");
        final int[] s = new int[spacing.length];
        for (int j = 0; j < spacing.length; j++) {
            for (int i = j; i < spacing.length; i++) {
                s[i] += spacing[j];
            }
        }
        this.spacing[o] = s;
        checkBorder();
    }

    @Override
    public void setBorder(final Border border) {
        super.setBorder(border);
        checkBorder();
    }

    private void checkBorder() {
        if (this.getBorder() == null) return;
        final Insets insets = this.getBorder().getBorderInsets(this);
        final int l = insets.left - spacing[0][0];
        if (l > 0) {
            for (int i = 0; i < spacing[0].length - 1; i++) {
                spacing[0][i] += l;
            }
        }
        spacing[0][spacing[0].length - 1] += Math.max(0, l) + Math.max(0, insets.right - (spacing[0][spacing[0].length - 1] - spacing[0][spacing[0].length - 2]));
        final int t = insets.top - spacing[1][0];
        if (t > 0) {
            for (int i = 0; i < spacing[1].length - 1; i++) {
                spacing[1][i] += t;
            }
        }
        spacing[1][spacing[1].length - 1] += Math.max(0, t) + Math.max(0, insets.bottom - (spacing[1][spacing[1].length - 1] - spacing[1][spacing[1].length - 2]));
    }

    @Override
    public Dimension minimumLayoutSize(final Container c) {
        assert c == this;
        return getMinimumSize();
    }

    @Override
    public Dimension preferredLayoutSize(final Container c) {
        assert c == this;
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(mins[0][mins[0].length - 1] + spacing[0][spacing[0].length - 1], mins[1][mins[1].length - 1] + spacing[1][spacing[1].length - 1]);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public Dimension maximumLayoutSize(final Container c) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(final Container c) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(final Container c) {
        return 0;
    }

    @Override
    public void invalidateLayout(final Container c) {
    }

    public boolean contains(final Component c) {
        return components.containsKey(c);
    }
}
