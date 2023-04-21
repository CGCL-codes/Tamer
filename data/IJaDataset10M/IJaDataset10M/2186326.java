package net.nexttext;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import net.nexttext.property.PVectorListProperty;
import net.nexttext.property.PVectorProperty;
import net.nexttext.property.Property;
import net.nexttext.property.PropertyChangeListener;
import net.nexttext.property.ColorProperty;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class TextObjectGlyph extends TextObject {

    protected static FontRenderContext frc = new FontRenderContext(null, false, false);

    /** The character for this object.  This is a special property that is 
	 * handled through its own get/set methods */
    protected String glyph;

    protected PFont pfont;

    protected Font font;

    protected float size;

    /** A vector containing int[] arrays with indices to the Control Points property
	 list.  These contours define the shape of the glyph */
    public Vector contours;

    /** 
   	 * This rectangle stores the glyph's "logical bounds", including proper
   	 * character spacing considering the glyph is in an undeformed state.
   	 */
    private Rectangle2D logicalBounds;

    private boolean deformed = false;

    /**
     * A flag to indicate if this glyph has been deformed.
     *
     * <p>False means that the glyph and font information is sufficient to draw
     * the glyph.  True means that the "Control Points" property is needed to
     * define the shape of the glyph.  </p>
     */
    public boolean isDeformed() {
        return deformed;
    }

    public void setDeformed(boolean df) {
        deformed = df;
    }

    /**
     * The outline of the glyph represented as a GeneralPath object.
     */
    protected GeneralPath outline = null;

    /**
     * This object can be used by Renderers to cache information about
     * TextObjectGlyphs.  If the glyph is deformed this cache object will be
     * reset to null.
     */
    public Object rendererCache = null;

    /**
	 * Default constructor.  Position is set to (0,0,0) by default, and color
	 * is inherited from the parent.
	 *
	 * @param glyph		A one character-long string 
	 * @param pfont     A processing.core.PFont object 
	 */
    public TextObjectGlyph(String glyph, PFont pfont, float size) {
        this(glyph, pfont, size, new PVector(0, 0, 0));
    }

    /**
	 * Constructor with a specific position.
	 *
	 * @param glyph		A one character-long string 
	 * @param pfont     A processing.core.PFont object 
	 * @param position  A PVector representing the glyph's relative position
	 */
    public TextObjectGlyph(String glyph, PFont pfont, float size, PVector position) {
        this(glyph, pfont, size, new HashMap<String, Property>(0), position);
    }

    /**
	 * Constructor with extra properties and a specific position.
	 *
	 * @param glyph		A one character-long string 
	 * @param pfont     A processing.core.PFont object 
	 * @param pos       A PVector representing the glyph's relative position
	 * @param props     Initial properties for the glyph.
	 */
    public TextObjectGlyph(String glyph, PFont pfont, float size, Map<String, Property> props, PVector pos) {
        super(props, pos);
        this.glyph = glyph;
        this.pfont = pfont;
        this.size = size;
        font = Book.loadFontFromPFont(pfont);
        properties.init("Control Points", new PVectorListProperty());
        glyphChanged();
        getControlPoints().addChangeListener(new PropertyChangeListener() {

            public void propertyChanged(Property propertyThatChanged) {
                glyphDeformed();
            }
        });
    }

    /**
	 *  Copy Constructor.
	 * 
	 *  @param glyph	A glyph to copy
	 */
    public TextObjectGlyph(TextObjectGlyph glyph) {
        this(glyph.toString(), glyph.getFont(), glyph.getSize(), glyph.properties.properties, glyph.getPosition().get());
    }

    /**
     * Get the greatest number of layers between this TextObject and the leaves
     * of the tree.
     */
    public int getHeight() {
        return 0;
    }

    /** Get the glyph of this object as a string of length 1. */
    public String getGlyph() {
        return glyph;
    }

    /** Get the font size of this object */
    public float getSize() {
        return size;
    }

    /**
	 * Rebuild the internal representation of the glyph based on the specified
	 * character.  This operation is rather costly, so it should be used 
	 * accordingly.
	 *
	 * NOTE: While a glyph is represented by a single character, the parameter
	 * here is of type string for two reasons:
	 *
	 * 1. String is the type used by the Font class in order to generate 
	 * shapes.
	 * 2. We have been un-officially allowing more than one character per glyph
	 * for development purposes.  So far it does not seem to cause any problem,
	 * however the proper way to represent words is to use the TextObjectBuilder.
	 */
    public void setGlyph(String glyph) {
        this.glyph = glyph;
        glyphChanged();
    }

    /**
	 * Rebuild the internal representation of the glyph according to the 
	 * properties of the newly specified Font object.  This operation is rather
	 * costly, so it should be used accordingly.
	 */
    public void setFont(PFont pfont) {
        this.pfont = pfont;
        font = Book.loadFontFromPFont(pfont);
        glyphChanged();
    }

    /**
	 * Returns this TextObjectGlyph's font attribute.
	 */
    public PFont getFont() {
        return this.pfont;
    }

    /**
	 * Returns this glyph's logical bounds information used for spacing.
	 */
    public Rectangle2D getLogicalBounds() {
        return logicalBounds;
    }

    /**
     * Convenience accessor for the control points.
     */
    public PVectorListProperty getControlPoints() {
        return (PVectorListProperty) getProperty("Control Points");
    }

    /**
     * Get the outline of the glyph.
     */
    public GeneralPath getOutline() {
        if (contours == null) {
            PGraphics.showException("Outline not found. Use native fonts to access glyph outlines or use DForm behaviours.");
        }
        if (outline == null) {
            PVectorListProperty vertices = getControlPoints();
            GeneralPath gp = new GeneralPath();
            Iterator it = contours.iterator();
            while (it.hasNext()) {
                int contour[] = (int[]) it.next();
                PVector firstPoint = vertices.get(contour[0]);
                gp.moveTo((float) firstPoint.x, (float) firstPoint.y);
                for (int i = 1; i < contour.length - 1; i += 2) {
                    PVector controlPoint = vertices.get(contour[i]);
                    PVector anchorPoint = vertices.get(contour[i + 1]);
                    gp.quadTo((float) controlPoint.x, (float) controlPoint.y, (float) anchorPoint.x, (float) anchorPoint.y);
                }
                gp.closePath();
            }
            outline = gp;
        }
        return outline;
    }

    /**
     * Get the absolute outline of the glyph.
     */
    public GeneralPath getOutlineAbsolute() {
        GeneralPath outline = new GeneralPath(getOutline());
        PVector posAbs = getPositionAbsolute();
        outline.transform(AffineTransform.getTranslateInstance(posAbs.x, posAbs.y));
        return outline;
    }

    /**
     * Set the flag colour of a glyph.
     * @param newColProp the color property that was changed
     */
    protected void colourFlagChanged(ColorProperty newColProp) {
        if (newColProp.getName() == "StrokeColor") stroked = newColProp.get().getAlpha() > 0; else if (newColProp.getName() == "Color") filled = newColProp.get().getAlpha() > 0;
    }

    /**
     * Reset any internally cached information that becomes invalid because the
     * glyph has changed.
     */
    protected void glyphChanged() {
        buildControlPoints();
        outline = null;
        rendererCache = null;
        invalidateLocalBoundingPolygon();
    }

    /**
     * Reset any internally cached information that becomes invalid because the
     * glyph has deformed.
     */
    protected void glyphDeformed() {
        deformed = true;
        outline = null;
        rendererCache = null;
        invalidateLocalBoundingPolygon();
    }

    /**
	 * This method uses the Java AWT Font methods to create a vector outline of 
	 * the glyph.
	 */
    protected void buildControlPoints() {
        PVectorListProperty vertices = getControlPoints();
        vertices.clear();
        if (font == null) {
            logicalBounds = new Rectangle2D.Float(0, -(size * pfont.ascent()), size * pfont.width(getGlyph().charAt(0)), size * (pfont.ascent() + pfont.descent()));
            return;
        }
        this.contours = new Vector();
        int vertexIndex = 0;
        Vector<Integer> tmpContour = new Vector<Integer>();
        float points[] = new float[6];
        int segmentType = 0;
        int previousType = 0;
        PVector lastAnchor = new PVector();
        PVector lastControlPoint = new PVector();
        GlyphVector gv = font.createGlyphVector(frc, this.glyph);
        Shape outline = gv.getOutline();
        logicalBounds = gv.getLogicalBounds();
        PathIterator pit = outline.getPathIterator(null);
        while (!pit.isDone()) {
            segmentType = pit.currentSegment(points);
            switch(segmentType) {
                case PathIterator.SEG_MOVETO:
                    tmpContour = new Vector<Integer>();
                    PVector startingPoint = new PVector((float) points[0], (float) points[1]);
                    vertices.add(new PVectorProperty(startingPoint));
                    tmpContour.add(vertexIndex);
                    vertexIndex++;
                    lastAnchor = startingPoint;
                    lastControlPoint = startingPoint;
                    previousType = segmentType;
                    break;
                case PathIterator.SEG_LINETO:
                    PVector endPoint = new PVector((float) points[0], (float) points[1]);
                    PVector midPoint = new PVector((lastAnchor.x + endPoint.x) / 2, (lastAnchor.y + endPoint.y) / 2);
                    vertices.add(new PVectorProperty(midPoint));
                    tmpContour.add(vertexIndex);
                    vertexIndex++;
                    vertices.add(new PVectorProperty(endPoint));
                    tmpContour.add(vertexIndex);
                    vertexIndex++;
                    lastAnchor = endPoint;
                    lastControlPoint = midPoint;
                    previousType = segmentType;
                    break;
                case PathIterator.SEG_QUADTO:
                    PVector controlPoint = new PVector((float) points[0], (float) points[1]);
                    PVector anchorPoint = new PVector((float) points[2], (float) points[3]);
                    vertices.add(new PVectorProperty(controlPoint));
                    tmpContour.add(vertexIndex);
                    vertexIndex++;
                    vertices.add(new PVectorProperty(anchorPoint));
                    tmpContour.add(vertexIndex);
                    vertexIndex++;
                    lastAnchor = anchorPoint;
                    lastControlPoint = controlPoint;
                    previousType = segmentType;
                    break;
                case PathIterator.SEG_CLOSE:
                    int contour[] = new int[tmpContour.size()];
                    Iterator<Integer> it = tmpContour.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        contour[i] = it.next();
                        i++;
                    }
                    contours.add(contour);
                    break;
                case PathIterator.SEG_CUBICTO:
                    getBook().log("TextObjectGlyph: cubic segment unsupported");
                    break;
            }
            pit.next();
        }
    }

    /**
     * See TextObject's getLocalBoundingPolygon() description for details.  
     * 
     * <p>Do not modify the returned Polygon, because it may be cached. </p>
     *
     * <p>XXXBUG this method always returns a rectangle until we write an
     * algorithm to calculate the convex hull of the set of control points.</p>
     * 
     * @see net.nexttext.TextObject#getLocalBoundingPolygon()   
     */
    public synchronized Polygon getLocalBoundingPolygon() {
        if (localBoundingPolygonValidToFrame >= getFrameCount()) {
            return localBoundingPolygon;
        }
        localBoundingPolygonValidToFrame = Long.MAX_VALUE;
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        if (font == null) {
            minX = 0;
            minY = -(size * pfont.ascent());
            maxX = size * pfont.width(getGlyph().charAt(0));
            maxY = size * pfont.descent();
        } else {
            if (getGlyph().equals(" ")) {
                Rectangle2D sb = Book.loadFontFromPFont(pfont).getStringBounds(" ", frc);
                minX = (float) sb.getMinX();
                minY = (float) sb.getMinY();
                maxX = (float) sb.getMaxX();
                maxY = (float) sb.getMaxY();
            } else {
                PVectorListProperty vertices = getControlPoints();
                for (Iterator<PVectorProperty> i = vertices.iterator(); i.hasNext(); ) {
                    PVectorProperty vertex = i.next();
                    minX = Math.min(vertex.getX(), minX);
                    minY = Math.min(vertex.getY(), minY);
                    maxX = Math.max(vertex.getX(), maxX);
                    maxY = Math.max(vertex.getY(), maxY);
                }
            }
        }
        int[] x = new int[] { (int) minX, (int) maxX, (int) maxX, (int) minX };
        int[] y = new int[] { (int) minY, (int) minY, (int) maxY, (int) maxY };
        localBoundingPolygon = new Polygon(x, y, 4);
        return localBoundingPolygon;
    }

    public String toString() {
        return getGlyph();
    }
}
