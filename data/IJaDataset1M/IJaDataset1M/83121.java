package org.makagiga.commons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.border.AbstractBorder;
import org.makagiga.commons.painters.Painter;

/**
 * @since 2.0
 */
public class ImageBorder extends AbstractBorder implements Painter {

    public enum Position {

        DEFAULT, CENTERED, CENTERED_USER_BOUNDS, COMPONENT_SIZE, USER_BOUNDS
    }

    ;

    private boolean visible = true;

    private Color outlineColor;

    private float alpha = 1.0f;

    private Image image;

    private Position position = Position.DEFAULT;

    private Rectangle userBounds = new Rectangle();

    public ImageBorder() {
        this((Image) null, Position.DEFAULT);
    }

    public ImageBorder(final Icon icon, final Position position) {
        this((icon instanceof ImageIcon) ? ImageIcon.class.cast(icon).getImage() : UI.toBufferedImage(icon), position);
    }

    public ImageBorder(final Image image, final Position position) {
        this.image = image;
        this.position = position;
    }

    public ImageBorder(final String iconName, final Position position) {
        this(MIcon.stock(iconName), position);
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(final float value) {
        if ((alpha < 0.0f) || (alpha > 1.0f)) throw new IllegalArgumentException("\"alpha\" value must be in range 0.0..1.0");
        alpha = value;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(final Image value) {
        image = value;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(final Color value) {
        outlineColor = value;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position value) {
        position = value;
    }

    public Rectangle getUserBounds() {
        return userBounds.getBounds();
    }

    public void setUserBounds(final Rectangle value) {
        userBounds.setBounds(value);
    }

    /**
	 * Returns @c true.
	 */
    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean value) {
        visible = value;
    }

    @Override
    public void paintBorder(final Component c, final Graphics graphics, final int x, final int y, final int w, final int h) {
        if (!visible) return;
        if (image == null) return;
        Graphics2D g = (Graphics2D) graphics;
        Composite oldComposite;
        if (alpha < 1.0f) {
            oldComposite = g.getComposite();
            g.setComposite(AlphaComposite.SrcOver.derive(alpha));
        } else {
            oldComposite = null;
        }
        Rectangle r = getPaintBounds(c, x, y);
        if (!r.isEmpty()) {
            Object oldHint = g.getRenderingHint(RenderingHints.KEY_RENDERING);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawImage(image, r.x, r.y, r.width, r.height, null);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, oldHint);
        }
        if (outlineColor != null) {
            g.setColor(outlineColor);
            g.drawRect(r.x, r.y, r.width, r.height);
        }
        if (oldComposite != null) g.setComposite(oldComposite);
    }

    public void setUserLocation(final Point p) {
        userBounds.setLocation(p);
    }

    public void setUserLocation(final int x, final int y) {
        userBounds.setLocation(x, y);
    }

    public void setUserSize(final Dimension d) {
        userBounds.setSize(d);
    }

    public void setUserSize(final int size) {
        userBounds.setSize(size, size);
    }

    public void setUserSize(final int w, final int h) {
        userBounds.setSize(w, h);
    }

    public Insets getPainterInsets(final Component c) {
        return getBorderInsets(c);
    }

    public void paint(final Component c, final Graphics2D g) {
        paintBorder(c, g, 0, 0, c.getWidth(), c.getHeight());
    }

    public void paint(final Component c, final Graphics2D g, final int x, final int y, final int width, final int height) {
        paintBorder(c, g, x, y, width, height);
    }

    Rectangle getPaintBounds(final Component c, final int x, final int y) {
        Rectangle r = new Rectangle();
        switch(position) {
            case DEFAULT:
                r.setBounds(x, y, image.getWidth(null), image.getHeight(null));
                break;
            case CENTERED:
                r.setBounds(c.getWidth() / 2 - image.getWidth(null) / 2, c.getHeight() / 2 - image.getHeight(null) / 2, image.getWidth(null), image.getHeight(null));
                break;
            case CENTERED_USER_BOUNDS:
                if (userBounds.isEmpty()) {
                    r.setBounds(userBounds);
                } else {
                    r.setBounds(c.getWidth() / 2 - userBounds.width / 2, c.getHeight() / 2 - userBounds.height / 2, userBounds.width, userBounds.height);
                }
                break;
            case COMPONENT_SIZE:
                r.setBounds(0, 0, c.getWidth(), c.getHeight());
                break;
            case USER_BOUNDS:
                r.setBounds(userBounds);
                break;
            default:
                throw new WTFError(position);
        }
        return r;
    }
}
