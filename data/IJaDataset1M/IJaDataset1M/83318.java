package org.edisoncor.gui.passwordField;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Edison
 */
public class PasswordFieldRectIcon extends JPasswordField {

    private Boolean left = true;

    private Icon icon;

    protected float anchoDeBorde = 2f;

    protected Color colorDeBorde = new Color(173, 173, 173);

    public PasswordFieldRectIcon() {
        setOpaque(false);
        setBorder(new EmptyBorder(0, 5, 0, 2));
        setPreferredSize(new Dimension(69, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Paint oldPaint = g2.getPaint();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle2D.Float r2d = new Rectangle2D.Float(0, 0, getWidth(), getHeight());
        g2.clip(r2d);
        g2.setPaint(new GradientPaint(0.0f, 0.0f, getBackground(), 0.0f, getHeight(), getBackground()));
        g2.fillRect(0, 0, getWidth(), getHeight());
        if (icon != null) {
            if (left) {
                g2.drawImage(((ImageIcon) icon).getImage(), 2, 2, getHeight() - 3, getHeight() - 3, null);
                setBorder(new EmptyBorder(2, (int) (getHeight()), 2, 2));
            } else {
                g2.drawImage(((ImageIcon) icon).getImage(), getWidth() - getHeight(), 2, getHeight() - 3, getHeight() - 3, null);
                setBorder(new EmptyBorder(2, 5, 2, (int) (getHeight())));
            }
        }
        g2.setPaint(new GradientPaint(0.0f, 0.0f, Color.BLACK, 0.0f, getHeight(), Color.BLACK));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2.setPaint(oldPaint);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        int x = 1, y = 1;
        int w = getWidth() - 2, h = getHeight() - 2;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(anchoDeBorde));
        g2.setColor(colorDeBorde);
        g2.drawRect(x, y, w, h);
        g2.dispose();
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        repaint();
    }

    public Boolean getLeft() {
        return left;
    }

    public void setLeft(Boolean left) {
        this.left = left;
        repaint();
    }

    public float getAnchoDeBorde() {
        return anchoDeBorde;
    }

    public void setAnchoDeBorde(float anchoDeBorde) {
        this.anchoDeBorde = anchoDeBorde;
    }

    public Color getColorDeBorde() {
        return colorDeBorde;
    }

    public void setColorDeBorde(Color colorDeBorde) {
        this.colorDeBorde = colorDeBorde;
    }
}
