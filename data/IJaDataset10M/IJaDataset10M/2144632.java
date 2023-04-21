package net.sf.amemailchecker.gui.component.accordion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Point2D;
import java.text.AttributedString;

public class BasicAccordionHeaderUI extends AccordionHeaderUI {

    private Color highlightBorderColor;

    private Border border;

    protected AccordionHeader accordionHeader;

    public static ComponentUI createUI(JComponent c) {
        return new BasicAccordionHeaderUI();
    }

    public void installUI(JComponent c) {
        accordionHeader = (AccordionHeader) c;
        installDefaults();
        installComponents();
    }

    public void uninstallUI(JComponent c) {
        uninstallDefaults();
        uninstallComponents();
        accordionHeader = null;
    }

    public void installDefaults() {
        border = BorderFactory.createEtchedBorder();
        highlightBorderColor = new Color(188, 210, 230);
    }

    public void uninstallDefaults() {
        accordionHeader.setBorder(null);
        highlightBorderColor = null;
    }

    public void installComponents() {
        accordionHeader.setBorder(border);
    }

    public void uninstallComponents() {
        accordionHeader.setBorder(null);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setComposite(AlphaComposite.SrcOver);
        Paint paint = g2.getPaint();
        boolean hasBorder = c.getBorder() != null;
        int startX = (hasBorder) ? c.getBorder().getBorderInsets(c).left : 0;
        int startY = (hasBorder) ? c.getBorder().getBorderInsets(c).top : 0;
        int endY = (hasBorder) ? c.getHeight() - (startY * 2) : c.getHeight();
        int endX = (hasBorder) ? c.getWidth() - (startX * 2) : c.getWidth();
        Point2D start = new Point2D.Float(startX, startY);
        Point2D end = new Point2D.Float(startX, endY);
        Color middle = c.getParent().getBackground();
        g2.setColor(middle);
        if (!accordionHeader.isActive()) {
            float[] dist = { 0.0f, 0.3f, 0.7f, 1.0f };
            Color[] colors = { Color.WHITE, middle, middle, Color.WHITE };
            g2.setPaint(new LinearGradientPaint(start, end, dist, colors));
        }
        g2.fillRect(startX, startY, endX, endY);
        g2.setPaint(paint);
        if (accordionHeader.isFocusable()) {
            accordionHeader.setBorder(null);
            Rectangle rectangle = new Rectangle(startX, startY, endX - 1, endY - 1);
            paintFocusBorder(g2, 1, rectangle, highlightBorderColor);
        }
        paintText(g2, c);
        super.paint(g, c);
    }

    private void paintFocusBorder(Graphics2D g2, int shadowWidth, Shape shape, Color color) {
        g2.setColor(color);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int sw = shadowWidth * 2;
        for (int i = sw; i >= 2; i -= 2) {
            g2.setStroke(new BasicStroke(i));
            g2.draw(shape);
        }
    }

    protected void paintText(Graphics2D g2, JComponent c) {
        String label = accordionHeader.getText();
        g2.setColor((accordionHeader.isActive()) ? Color.LIGHT_GRAY : Color.BLACK);
        AttributedString attributedString = new AttributedString(label);
        attributedString.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        int w = g2.getFontMetrics().stringWidth(label);
        int h = (int) g2.getFontMetrics().getStringBounds(label, g2).getHeight();
        g2.drawString(attributedString.getIterator(), (c.getWidth() / 2) - (w / 2), (c.getHeight() / 2) + (h / 4));
    }
}
