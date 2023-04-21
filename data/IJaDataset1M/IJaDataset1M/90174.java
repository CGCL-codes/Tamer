package jogamp.graph.font.typecast.ot;

import jogamp.graph.font.typecast.ot.table.Charstring;
import jogamp.graph.font.typecast.ot.table.CharstringType2;
import jogamp.graph.font.typecast.ot.table.GlyfDescript;
import jogamp.graph.font.typecast.ot.table.GlyphDescription;
import jogamp.graph.font.typecast.t2.T2Interpreter;
import com.jogamp.graph.geom.AABBox;

/**
 * An individual glyph within a font.
 * @version $Id: Glyph.java,v 1.3 2007-02-21 12:23:54 davidsch Exp $
 * @author <a href="mailto:davidsch@dev.java.net">David Schweinsberg</a>, Sven Gothel
 */
public class OTGlyph {

    protected short _leftSideBearing;

    protected int _advanceWidth;

    private Point[] _points;

    AABBox _bbox;

    /**
     * Construct a Glyph from a TrueType outline described by
     * a GlyphDescription.
     * @param cs The Charstring describing the glyph.
     * @param lsb The Left Side Bearing.
     * @param advance The advance width.
     */
    public OTGlyph(GlyphDescription gd, short lsb, int advance) {
        _leftSideBearing = lsb;
        _advanceWidth = advance;
        describe(gd);
    }

    /**
     * Construct a Glyph from a PostScript outline described by a Charstring.
     * @param cs The Charstring describing the glyph.
     * @param lsb The Left Side Bearing.
     * @param advance The advance width.
     */
    public OTGlyph(Charstring cs, short lsb, int advance) {
        _leftSideBearing = lsb;
        _advanceWidth = advance;
        if (cs instanceof CharstringType2) {
            T2Interpreter t2i = new T2Interpreter();
            _points = t2i.execute((CharstringType2) cs);
        } else {
        }
    }

    public AABBox getBBox() {
        return _bbox;
    }

    public int getAdvanceWidth() {
        return _advanceWidth;
    }

    public short getLeftSideBearing() {
        return _leftSideBearing;
    }

    public Point getPoint(int i) {
        return _points[i];
    }

    public int getPointCount() {
        return _points.length;
    }

    /**
     * Resets the glyph to the TrueType table settings
     */
    public void reset() {
    }

    /**
     * @param factor a 16.16 fixed value
     */
    public void scale(int factor) {
        for (int i = 0; i < _points.length; i++) {
            _points[i].x = ((_points[i].x << 10) * factor) >> 26;
            _points[i].y = ((_points[i].y << 10) * factor) >> 26;
        }
        _leftSideBearing = (short) ((_leftSideBearing * factor) >> 6);
        _advanceWidth = (_advanceWidth * factor) >> 6;
    }

    /**
     * Set the points of a glyph from the GlyphDescription
     */
    private void describe(GlyphDescription gd) {
        int endPtIndex = 0;
        _points = new Point[gd.getPointCount()];
        for (int i = 0; i < gd.getPointCount(); i++) {
            boolean endPt = gd.getEndPtOfContours(endPtIndex) == i;
            if (endPt) {
                endPtIndex++;
            }
            _points[i] = new Point(gd.getXCoordinate(i), gd.getYCoordinate(i), (gd.getFlags(i) & GlyfDescript.onCurve) != 0, endPt);
        }
        _bbox = new AABBox(gd.getXMinimum(), gd.getYMinimum(), 0, gd.getXMaximum(), gd.getYMaximum(), 0);
    }
}
