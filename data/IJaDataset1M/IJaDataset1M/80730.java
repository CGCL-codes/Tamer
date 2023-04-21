package monitor.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import monitor.edu.berkeley.guir.prefuse.VisualItem;
import monitor.edu.berkeley.guir.prefuse.render.TextImageItemRenderer;
import monitor.edu.berkeley.guir.prefuse.util.FontLib;

public class MyTextImageItemRenderer extends TextImageItemRenderer {

    protected String getText(VisualItem item) {
        return (item.getAttribute("sysName"));
    }

    protected URL getImageLocation(VisualItem item) {
        String agent = item.getAttribute("Agent");
        String managed = item.getAttribute("Managed");
        String router = item.getAttribute("Router");
        if (router != null) {
            if (router.compareToIgnoreCase("True") == 0) return (getClass().getResource("images/router.gif"));
        }
        if (agent != null) {
            if (agent.compareToIgnoreCase("True") == 0) return (getClass().getResource("images/agent.gif"));
        }
        if (managed != null) {
            if (managed.compareToIgnoreCase("False") == 0) return (getClass().getResource("images/unmanagedswitch.gif"));
            if (managed.compareToIgnoreCase("Unknown") == 0) return (getClass().getResource("images/unknown.gif"));
        }
        return (getClass().getResource("images/switch.gif"));
    }

    protected URL getImageLocationURL(VisualItem item) {
        String str = item.getAttribute("RootBridge");
        String managed = item.getAttribute("Managed");
        String router = item.getAttribute("Router");
        if (router != null) {
            if (router.compareToIgnoreCase("True") == 0) return (getClass().getResource("images/router.gif"));
        }
        if (str != null) {
            if (str.compareToIgnoreCase("True") == 0) return (getClass().getResource("images/rootbridge.gif"));
            if (str.compareToIgnoreCase("NoSTP") == 0) return (getClass().getResource("images/nospt.gif"));
        }
        if (managed != null) {
            if (managed.compareToIgnoreCase("False") == 0) return (getClass().getResource("images/unmanagedswitch.gif"));
            if (managed.compareToIgnoreCase("Unknown") == 0) return (getClass().getResource("images/unknown.gif"));
        }
        return (getClass().getResource("images/switch.gif"));
    }

    public void render(Graphics2D g, VisualItem item) {
        Shape shape = getShape(item);
        if (shape == null) return;
        Paint itemColor = item.getColor();
        Paint fillColor = item.getFillColor();
        int type = getRenderType(item);
        if (type == RENDER_TYPE_FILL || type == RENDER_TYPE_DRAW_AND_FILL) {
            g.setPaint(fillColor);
            g.fill(shape);
        }
        String s = getText(item);
        Image img = getImage(item);
        if (s == null && img == null) return;
        Rectangle2D r = shape.getBounds2D();
        double size = item.getSize();
        double x = r.getMinX() + size * m_horizBorder;
        if (img != null) {
            Composite comp = g.getComposite();
            if (fillColor instanceof Color) {
                int alpha = ((Color) fillColor).getAlpha();
                if (alpha < 255) {
                    AlphaComposite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ((float) alpha) / 255);
                    g.setComposite(alphaComp);
                }
            }
            double is = 1.0;
            if (item.getAttribute("Agent") == "True") {
                is = 1.4 * m_imageSize * size;
            } else {
                is = m_imageSize * size;
            }
            double w = is * img.getWidth(null);
            double h = is * img.getHeight(null);
            double y = r.getMinY() + (r.getHeight() - h) / 2;
            m_transform.setTransform(is, 0, 0, is, x, y);
            g.drawImage(img, m_transform, null);
            x += w + (s != null && w > 0 ? size * m_imageMargin : 0);
            g.setComposite(comp);
        }
        if (s != null) {
            g.setPaint(itemColor);
            g.setFont(m_font);
            FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);
            Rectangle rec = new Rectangle();
            if (s.length() > Topology.textLength) {
                s = s.substring(0, Topology.textLength);
            }
            rec.x = (int) r.getX();
            rec.y = (int) r.getY() + (int) r.getHeight() - fm.getHeight();
            rec.height = fm.getHeight();
            rec.width = fm.stringWidth(s);
            g.setColor(Color.YELLOW);
            g.fill(rec);
            g.setColor(Color.BLACK);
            g.drawString(s, (float) rec.getX(), (int) (rec.y + fm.getHeight() / 1.5));
        }
        if (type == RENDER_TYPE_DRAW || type == RENDER_TYPE_DRAW_AND_FILL) {
            Stroke st = g.getStroke();
            Stroke ist = getStroke(item);
            if (ist != null) g.setStroke(ist);
            g.setPaint(itemColor);
            g.draw(shape);
            g.setStroke(st);
        }
    }

    protected Shape getRawShape(VisualItem item) {
        double size = item.getSize();
        Image img = getImage(item);
        double is = 1.0;
        if (item.getAttribute("Agent") == "True") {
            is = 1.4 * m_imageSize * size;
        } else {
            is = m_imageSize * size;
        }
        double ih = (img == null ? 0 : is * img.getHeight(null));
        double iw = (img == null ? 0 : is * img.getWidth(null));
        m_font = item.getFont();
        if (size != 1) m_font = FontLib.getFont(m_font.getName(), m_font.getStyle(), size * m_font.getSize());
        String s = getText(item);
        if (s == null) {
            s = "";
        }
        if (s.length() > Topology.textLength) {
            s = s.substring(0, Topology.textLength);
        }
        FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);
        int th = fm.getHeight();
        int tw = fm.stringWidth(s) + 2;
        double w = iw + size * (2 * m_horizBorder + (iw > 0 ? m_imageMargin : 0));
        double h = ih + th + size * 2 * m_vertBorder;
        getAlignedPoint(m_tmpPoint, item, w, h, m_xAlign, m_yAlign);
        if (m_imageBox instanceof RoundRectangle2D) {
            ((RoundRectangle2D) m_imageBox).setRoundRect(m_tmpPoint.getX(), m_tmpPoint.getY(), w, h, size * m_arcWidth, size * m_arcHeight);
        } else {
            m_imageBox.setFrame(m_tmpPoint.getX(), m_tmpPoint.getY(), w, h);
        }
        return m_imageBox;
    }
}
