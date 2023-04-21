package net.sf.jcgm.core;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.io.DataInput;
import java.io.IOException;

/**
 * Common class for text support
 * @author Philippe Cadé
 * @version $Id: TextCommand.java 46 2011-12-14 08:26:44Z phica $
 */
public abstract class TextCommand extends Command {

    /** The string to display */
    protected String string;

    /** The position at which the string should be displayed */
    protected Point2D.Double position;

    public TextCommand(int ec, int eid, int l, DataInput in) throws IOException {
        super(ec, eid, l, in);
    }

    /**
	 * Returns an offset to apply to the defined text position
	 * @param d
	 * @return Offset to apply to the text position
	 */
    abstract Double getTextOffset(CGMDisplay d);

    protected void scaleText(CGMDisplay d, FontMetrics fontMetrics, GlyphVector glyphVector, double width, double height) {
    }

    @Override
    public void paint(CGMDisplay d) {
        if (this.string.length() == 0) {
            return;
        }
        Graphics2D g2d = d.getGraphics2D();
        AffineTransform savedTransform = g2d.getTransform();
        AffineTransform coordinateSystemTransformation = d.getCoordinateSystemTransformation(this.position, d.getCharacterOrientationBaselineVector(), d.getCharacterOrientationUpVector());
        AffineTransform textTransform = d.getTextTransform();
        coordinateSystemTransformation.concatenate(textTransform);
        g2d.transform(coordinateSystemTransformation);
        Point2D.Double textOrigin = getTextOffset(d);
        g2d.translate(textOrigin.x, textOrigin.y);
        g2d.setColor(d.getTextColor());
        String decodedString = d.useSymbolEncoding() ? SymbolDecoder.decode(this.string) : this.string;
        if (TextPath.Type.LEFT.equals(d.getTextPath())) {
            decodedString = flipString(decodedString);
        }
        Font font = g2d.getFont();
        Point2D.Double[] extent = d.getExtent();
        Font adjustedFont = font.deriveFont((float) (Math.abs(extent[0].y - extent[1].y) / 100));
        g2d.setFont(adjustedFont);
        FontRenderContext fontRenderContext = g2d.getFontRenderContext();
        GlyphVector glyphVector = adjustedFont.createGlyphVector(fontRenderContext, decodedString);
        Rectangle2D logicalBounds = glyphVector.getLogicalBounds();
        FontMetrics fontMetrics = g2d.getFontMetrics(adjustedFont);
        int screenResolution;
        if (GraphicsEnvironment.isHeadless()) {
            screenResolution = 96;
        } else {
            screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
        }
        double height = fontMetrics.getAscent() * 72 / screenResolution;
        scaleText(d, fontMetrics, glyphVector, logicalBounds.getWidth(), height);
        if (TextPath.Type.UP.equals(d.getTextPath()) || TextPath.Type.DOWN.equals(d.getTextPath())) {
            applyTextPath(d, glyphVector);
        }
        g2d.drawGlyphVector(glyphVector, 0, 0);
        g2d.setTransform(savedTransform);
    }

    /**
	 * Flip the given string for left text path
	 * @param s
	 * @return
	 */
    protected String flipString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    /**
	 * @param glyphVector
	 */
    protected void applyTextPath(CGMDisplay d, GlyphVector glyphVector) {
        double height = glyphVector.getLogicalBounds().getHeight();
        if (TextPath.Type.DOWN.equals(d.getTextPath())) {
            float[] glyphPositions = glyphVector.getGlyphPositions(0, glyphVector.getNumGlyphs(), null);
            int glyphIndex = 0;
            for (int i = 0; i < (glyphPositions.length / 2); i++) {
                Point2D.Float newPos = new Point2D.Float(glyphPositions[0], (float) (i * height));
                glyphVector.setGlyphPosition(glyphIndex++, newPos);
            }
        } else if (TextPath.Type.DOWN.equals(d.getTextPath())) {
        }
    }

    public String getString() {
        return string;
    }
}
