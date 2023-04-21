package org.edisoncor.gui.textField;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import org.edisoncor.gui.toolTip.ToolTipRound;

/**
 *
 * @author Edison
 */
public class TextFieldRound extends JTextField {

    private int direccionDeSombra = 60;

    private int distanciaDeSombra = 1;

    private boolean vertical = true;

    private Float opacidad = 1f;

    private float borde = 2f;

    public TextFieldRound(Icon icon) {
    }

    public TextFieldRound() {
        setOpaque(false);
        setFont(new Font("Arial", Font.BOLD, 12));
    }

    private static Image loadImage(String fileName) {
        try {
            return ImageIO.read(TextFieldRound.class.getResource(fileName));
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public JToolTip createToolTip() {
        ToolTipRound tip = new ToolTipRound();
        tip.setComponent(this);
        return tip;
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Stroke st = g2.getStroke();
        g2.setStroke(new BasicStroke(borde));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight() / 3, getHeight() / 3);
        g2.setStroke(st);
    }

    @Override
    protected void paintComponent(Graphics g) {
        float x1 = 0, x2 = 0, y1 = 0, y2 = getHeight();
        if (!vertical) {
            x1 = 0;
            y1 = 0;
            x2 = getWidth();
            y2 = 0;
        }
        Graphics2D g2 = (Graphics2D) g;
        Paint oldPaint = g2.getPaint();
        RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, getHeight() / 3, getHeight() / 3);
        g2.clip(rect);
        g2.setPaint(new GradientPaint(x1, y1, getBackground(), x2, y2, getBackground().darker()));
        g2.fill(rect);
        g2.setPaint(oldPaint);
        super.paintComponent(g);
    }

    public Float getOpacidad() {
        return opacidad;
    }

    public void setOpacidad(Float opacidad) {
        if (opacidad >= 0 & opacidad <= 1) this.opacidad = opacidad;
    }

    public int getDireccionDeSombra() {
        return direccionDeSombra;
    }

    public void setDireccionDeSombra(int direccionDeSombra) {
        this.direccionDeSombra = direccionDeSombra;
        repaint();
    }

    public int getDistanciaDeSombra() {
        return distanciaDeSombra;
    }

    public void setDistanciaDeSombra(int distanciaDeSombra) {
        this.distanciaDeSombra = distanciaDeSombra;
        repaint();
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
        repaint();
    }

    public float getBorde() {
        return borde;
    }

    public void setBorde(float borde) {
        this.borde = borde;
    }
}
