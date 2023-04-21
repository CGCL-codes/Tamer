package tico.interpreter.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class TInterpreterLabel extends JLabel {

    boolean transparentBackgroundLabel;

    Color gradientColorLabel;

    private static final int HORIZONTAL_MARGIN = 10;

    public TInterpreterLabel() {
        super();
    }

    public TInterpreterLabel setAttributes(Color borderColor, float borderSize, Color backgroundColor, Color gradientColor, Rectangle r, String text, Font font, Color textColor, boolean transparentBackground) {
        transparentBackgroundLabel = transparentBackground;
        gradientColorLabel = gradientColor;
        this.setLocation(r.x, r.y);
        this.setSize(r.width, r.height);
        if (backgroundColor != null) {
            setOpaque(true);
            setBackground(backgroundColor);
        }
        setBorder(null);
        if (borderColor != null) setBorder(new LineBorder(borderColor, (int) borderSize));
        this.setForeground(textColor);
        this.setHorizontalAlignment(CENTER);
        this.setVerticalAlignment(CENTER);
        this.setFont(font);
        this.setText(text);
        int cellTextSpace = (int) (r.width - 2 * borderSize - 2 * HORIZONTAL_MARGIN);
        JButton j = new JButton();
        j.setSize(cellTextSpace, cellTextSpace);
        FontMetrics fm = j.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        if (textWidth > cellTextSpace) {
            int fontSize = font.getSize();
            int fontStyle = font.getStyle();
            String fontName = font.getFontName();
            while ((textWidth > cellTextSpace) && fontSize > 0) {
                fontSize--;
                font = new Font(fontName, fontStyle, fontSize);
                fm = j.getFontMetrics(font);
                textWidth = fm.stringWidth(text);
            }
            if (fontSize != 0) {
                setFont(font);
                setText(text);
            }
        }
        return this;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (!transparentBackgroundLabel && gradientColorLabel != null) {
            setOpaque(false);
            g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), gradientColorLabel, true));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paint(g);
    }
}
