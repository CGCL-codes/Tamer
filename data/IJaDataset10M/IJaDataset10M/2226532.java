package edu.yale.csgp.vitapad.visual.paint;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.StringTokenizer;
import edu.yale.csgp.vitapad.graph.PathwayDecoration;
import edu.yale.csgp.vitapad.graph.PathwayElement;
import edu.yale.csgp.vitapad.util.StringBreaker;

/**
 * @author Matt Holford
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
public class DecorationPainterImp implements DecorationPainter {

    private double tbMargin = 2;

    private double lrMargin = 3;

    public DecorationPainterImp() {
    }

    public void paint(PathwayElement element, Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintFill((PathwayDecoration) element, g2);
        paintOutline((PathwayDecoration) element, g2);
        paintLabel((PathwayDecoration) element, g2);
        if (((PathwayDecoration) element).hasToolTip()) paintTooltip((PathwayDecoration) element, g2);
    }

    public void paintOutline(PathwayDecoration element, Graphics2D g2) {
        g2.setColor(element.getOutlineColor());
        g2.draw(element.getGeneralPath());
    }

    public void paintFill(PathwayDecoration element, Graphics2D g2) {
        if (element.getTexturePaint() != null) g2.setPaint(element.getTexturePaint()); else g2.setColor(element.getFillColor());
        g2.fill(element.getGeneralPath());
    }

    public void paintLabel(PathwayDecoration element, Graphics2D g2) {
        int line = 1;
        int lineHeight = element.getFontMetrics().getHeight();
        Rectangle bounds = element.getBounds();
        String broken = StringBreaker.breakString(element.getLabel());
        StringTokenizer toke = new StringTokenizer(broken, "\n");
        g2.setFont(element.getFont());
        g2.setColor(element.getFontColor());
        while (toke.hasMoreTokens()) {
            g2.drawString(toke.nextToken(), (float) (bounds.x + lrMargin), (float) (bounds.y + tbMargin + lineHeight * line - 2));
            line++;
        }
    }

    /**
     * @param element
     * @param g2
     */
    private void paintTooltip(PathwayDecoration vge, Graphics2D g2) {
        int height = 0;
        int width;
        int maxWidth = 0;
        FontMetrics fontMetrics = vge.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();
        String broken = StringBreaker.breakString((String) ((PathwayDecoration) vge).getDataClass().get("Name"));
        StringTokenizer toke = new StringTokenizer(broken, "\n");
        while (toke.hasMoreTokens()) {
            height += lineHeight;
            width = fontMetrics.stringWidth(toke.nextToken() + 2);
            maxWidth = width > maxWidth ? width : maxWidth;
        }
        Rectangle bounds = vge.getBounds();
        Rectangle2D.Double r = new Rectangle2D.Double(bounds.getWidth() + bounds.getX() + 5, bounds.getY() - 10, (double) maxWidth, (double) height + 4);
        g2.setColor(new Color(32, 32, 100));
        g2.draw(r);
        g2.setColor(new Color(200, 255, 255, 112));
        g2.fill(r);
        g2.setColor(Color.BLACK);
        Rectangle rBounds = r.getBounds();
        int line = 1;
        toke = new StringTokenizer(broken, "\n");
        while (toke.hasMoreTokens()) {
            g2.drawString(toke.nextToken(), rBounds.x + 3, rBounds.y + (lineHeight * line));
            line++;
        }
    }

    public void rescale(PathwayElement element) {
        int height = 0;
        int maxWidth = 0;
        int width = 0;
        FontMetrics fontMetrics = ((PathwayDecoration) element).getFontMetrics();
        GeneralPath drawPath = ((PathwayDecoration) element).getGeneralPath();
        AffineTransform transform = new AffineTransform();
        Rectangle origLocation = drawPath.getBounds();
        int lineHeight = fontMetrics.getHeight();
        String broken = StringBreaker.breakString(((PathwayDecoration) element).getLabel());
        StringTokenizer toke = new StringTokenizer(broken, "\n");
        while (toke.hasMoreTokens()) {
            height += lineHeight;
            width = fontMetrics.stringWidth(toke.nextToken()) + 2;
            maxWidth = width > maxWidth ? width : maxWidth;
        }
        double scalex = (maxWidth + lrMargin * 2) / origLocation.getWidth();
        double scaley = (height + tbMargin * 2) / origLocation.getHeight();
        transform.scale(scalex, scaley);
        drawPath.transform(transform);
        Rectangle newLocation = drawPath.getBounds();
        double transx = origLocation.getMinX() - newLocation.getMinX();
        double transy = origLocation.getMinY() - newLocation.getMinY();
        int xpos = 0;
        int ypos = 0;
        transx = transx - (origLocation.getX() - (xpos - (newLocation.getWidth() / 2)));
        transy = transy - (origLocation.getY() - (ypos - (newLocation.getHeight() / 2)));
        transform.setToTranslation(transx, transy);
        drawPath.transform(transform);
    }
}
