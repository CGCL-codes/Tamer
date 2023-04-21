package cn.edu.wuse.musicxml.symbol;

import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;

/**
 * 符梁符号
 *
 */
public class BeamSymbol extends MusicSymbol {

    /**
	 * 直线beam
	 */
    public static byte BEAM_LINE = 0x01;

    public static byte BEAM_HOOK = 0x02;

    public static byte HOOK_UP = 0x03;

    public static byte HOOK_DOWN = 0x04;

    public static byte HOOK_SHORT = 0x05;

    public static byte HOOK_LONG;

    public byte type = 0x00;

    ;

    /**
	 * 对于连线的符梁的符号由两个点决定,此点用来存储结束位置的.
	 */
    private Point2D endPoint = new Point2D.Float(0, 0);

    private Stroke stroke;

    private byte hookDirection = HOOK_DOWN;

    private byte hookLength = HOOK_SHORT;

    private int[] glyphArray;

    private boolean isInit = false;

    private void init() {
        if (hookDirection == HOOK_UP) {
            if (hookLength == HOOK_SHORT) glyphArray = new int[] { glyph.getFLAG_1_UP_SHORT() }; else glyphArray = new int[] { glyph.getFLAG_1_UP() };
        } else {
            if (hookLength == HOOK_SHORT) glyphArray = new int[] { glyph.getFLAG_1_DOWN_SHORT() }; else glyphArray = new int[] { glyph.getFLAG_1_DOWN() };
        }
        isInit = true;
    }

    public void draw(Graphics g) {
        if (!isInit) init();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        Color dc = g2.getColor();
        g2.setColor(color);
        g2.setStroke(stroke);
        if (type == BEAM_LINE) g2.drawLine((int) point.getX(), (int) point.getY(), (int) endPoint.getX(), (int) endPoint.getY()); else {
            glyph.setFont(glyph.getFont().deriveFont(fontstyle | fontweight, fontsize));
            g2.setFont(glyph.getFont());
            FontRenderContext frc = g2.getFontRenderContext();
            GlyphVector vector = glyph.getFont().createGlyphVector(frc, glyphArray);
            g2.drawGlyphVector(vector, (float) point.getX(), (float) point.getY());
        }
        g2.setColor(dc);
    }

    public void setEndPoint(float x, float y) {
        endPoint.setLocation(x, y);
    }

    public void setEndPoint(Point2D p) {
        endPoint.setLocation(p.getX(), p.getY());
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public byte getHookDirection() {
        return hookDirection;
    }

    public void setHookDirection(byte hookDirection) {
        this.hookDirection = hookDirection;
    }

    public byte getHookLength() {
        return hookLength;
    }

    public void setHookLength(byte hookLength) {
        this.hookLength = hookLength;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
