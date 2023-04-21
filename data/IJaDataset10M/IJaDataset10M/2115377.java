package net.infonode.gui.shaped.panel;

import java.awt.*;
import javax.swing.border.*;
import net.infonode.gui.*;
import net.infonode.gui.componentpainter.*;
import net.infonode.gui.panel.*;
import net.infonode.gui.shaped.border.*;
import net.infonode.util.*;

/**
 * <p>
 * A panel that has support for a {@link ComponentPainter} and a {@link ShapedBorder}.
 * The background of the panel is painted as normal and then the {@link ComponentPainter}
 * paints the area inside the {@link ShapedBorder} or the complete component area if the
 * its border isn't a {@link ShapedBorder}.
 * </p>
 *
 * <p>
 * If a {@link ShapedBorder} is applied to this panel, mouse events etc. are only triggered
 * for this panel if the point is inside the {@link Shape} of the {@link ShapedBorder}. Child
 * components of this panel can optionally be clipped using the {@link Shape}.
 * </p>
 *
 * <p>
 * A {@link ShapedBorder} wrapped inside {@link CompoundBorder}'s will be used by the ShapedPanel,
 * but a {@link ShapedBorder} wrapped inside other border types can't be found and is hence not
 * used by the panel.
 * </p>
 *
 * @author johan
 */
public class ShapedPanel extends BaseContainer implements BackgroundPainter {

    private Direction direction = Direction.RIGHT;

    private boolean horizontalFlip;

    private boolean verticalFlip;

    private boolean clipChildren;

    private ComponentPainter painter;

    private ShapedBorder shapedBorder;

    private Insets shapedInsets;

    public ShapedPanel() {
        super();
    }

    public ShapedPanel(LayoutManager l) {
        super(l);
    }

    public ShapedPanel(ComponentPainter painter) {
        this();
        this.painter = painter;
    }

    public ShapedPanel(ComponentPainter painter, Border border) {
        this(painter);
        setBorder(border);
    }

    public ShapedPanel(Component component) {
        this();
        add(component, BorderLayout.CENTER);
    }

    public Shape getShape() {
        ShapedBorder b = getShapedBorder();
        return b == null ? null : b.getShape(this, shapedInsets.left, shapedInsets.top, getWidth() - shapedInsets.left - shapedInsets.right, getHeight() - shapedInsets.top - shapedInsets.bottom);
    }

    public ComponentPainter getComponentPainter() {
        return painter;
    }

    public void setComponentPainter(ComponentPainter painter) {
        if (this.painter != painter) {
            this.painter = painter;
            repaint();
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isHorizontalFlip() {
        return horizontalFlip;
    }

    public void setHorizontalFlip(boolean horizontalFlip) {
        if (this.horizontalFlip != horizontalFlip) {
            this.horizontalFlip = horizontalFlip;
            revalidate();
        }
    }

    public boolean isVerticalFlip() {
        return verticalFlip;
    }

    public void setVerticalFlip(boolean verticalFlip) {
        if (this.verticalFlip != verticalFlip) {
            this.verticalFlip = verticalFlip;
            revalidate();
        }
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            revalidate();
            repaint();
        }
    }

    public void setClipChildren(boolean clipChildren) {
        this.clipChildren = clipChildren;
    }

    public ShapedBorder getShapedBorder() {
        return shapedBorder;
    }

    public void setBorder(Border border) {
        super.setBorder(border);
        shapedBorder = null;
        findShapedBorder(getBorder(), new Insets(0, 0, 0, 0));
    }

    protected void paintChildren(Graphics g) {
        if (clipChildren) {
            Shape shape = getShape();
            if (shape != null) {
                Graphics2D g2 = (Graphics2D) g;
                Shape clip = g2.getClip();
                g2.clip(shape);
                super.paintChildren(g);
                g2.setClip(clip);
                return;
            }
        }
        super.paintChildren(g);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (painter != null) {
            Shape shape = getShape();
            if (shape != null) {
                Shape clip = g.getClip();
                g.clipRect(shapedInsets.left, shapedInsets.top, getWidth() - shapedInsets.left - shapedInsets.right, getHeight() - shapedInsets.top - shapedInsets.bottom);
                ((Graphics2D) g).clip(shape);
                painter.paint(this, g, 0, 0, getWidth(), getHeight(), direction, horizontalFlip, verticalFlip);
                g.setClip(clip);
            } else painter.paint(this, g, 0, 0, getWidth(), getHeight(), direction, horizontalFlip, verticalFlip);
        }
    }

    public boolean contains(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) return false;
        Shape shape = getShape();
        return shape == null ? super.contains(x, y) : shape.contains(x, y);
    }

    public boolean inside(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) return false;
        Shape shape = getShape();
        return shape == null ? super.inside(x, y) : shape.contains(x, y);
    }

    private boolean findShapedBorder(Border border, Insets i) {
        if (border == null) return false; else if (border instanceof ShapedBorder) {
            shapedBorder = (ShapedBorder) border;
            shapedInsets = i;
            return true;
        } else if (border instanceof CompoundBorder) {
            CompoundBorder c = (CompoundBorder) border;
            if (findShapedBorder(c.getOutsideBorder(), i)) return true;
            return findShapedBorder(c.getInsideBorder(), InsetsUtil.add(c.getOutsideBorder().getBorderInsets(this), i));
        } else return false;
    }
}
