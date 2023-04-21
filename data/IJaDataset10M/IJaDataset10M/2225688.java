package jsesh.mdcDisplayer.drawingElements;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.HashMap;
import java.util.Map;
import jsesh.hieroglyphs.DefaultHieroglyphicFontManager;
import jsesh.hieroglyphs.HieroglyphicFontManager;
import jsesh.hieroglyphs.LigatureZone;
import jsesh.hieroglyphs.LigatureZoneBuilder;
import jsesh.hieroglyphs.ShapeChar;
import jsesh.mdcDisplayer.mdcView.MDCView;
import jsesh.swingUtils.ShapeHelper;

/**
 * A Hieroglyphic drawer which takes its input from (SVG) fonts. Our default
 * implementation of hieroglyphs drawing and measuring.
 */
public class SVGFontHieroglyphicDrawer implements HieroglyphsDrawer {

    /**
	 * Code used as "default" hieroglyph to compute sizes.
	 */
    private static final String DEFAULT_CODE = "A1";

    private static HashMap<String, String> normalizedCodesMap = new HashMap<String, String>();

    static {
        normalizedCodesMap.put("[[", "BEGINERASE");
        normalizedCodesMap.put("]]", "ENDERASE");
        normalizedCodesMap.put("[{", "BEGINEDITORSUPERFLUOUS");
        normalizedCodesMap.put("}]", "ENDEDITORSUPERFLUOUS");
        normalizedCodesMap.put("[&", "BEGINEDITORADDITION");
        normalizedCodesMap.put("&]", "ENDEDITORADDITION");
        normalizedCodesMap.put("[\"", "BEGINPREVIOUSLYREADABLE");
        normalizedCodesMap.put("\"]", "ENDPREVIOUSLYREADABLE");
        normalizedCodesMap.put("[(", "BEGINMINORADDITION");
        normalizedCodesMap.put(")]", "ENDMINORADDITION");
        normalizedCodesMap.put("[?", "BEGINDUBIOUS");
        normalizedCodesMap.put("?]", "ENDDUBIOUS");
    }

    /**
	 * The manager which associates codes with actual glyphs.
	 */
    private HieroglyphicFontManager fontManager;

    /**
	 * A map code for signs not managed by FontManager.
	 */
    private Map<String, Float> nonHieroglyphic;

    private float heightOfA1 = 0;

    private boolean smallBodyUsed = false;

    public SVGFontHieroglyphicDrawer() {
        this.fontManager = DefaultHieroglyphicFontManager.getInstance();
        nonHieroglyphic = new HashMap<String, Float>();
        Rectangle2D specA1 = fontManager.get(DEFAULT_CODE).getBbox();
        float w = (float) specA1.getWidth();
        float h = (float) specA1.getHeight();
        heightOfA1 = h;
        nonHieroglyphic.put("/", new Rectangle2D.Float(0, 0, w / 2f, h / 2f));
        nonHieroglyphic.put("//", new Rectangle2D.Float(0, 0, w, h));
        nonHieroglyphic.put("h/", new Rectangle2D.Float(0, 0, w, h / 2f));
        nonHieroglyphic.put("v/", new Rectangle2D.Float(0, 0, w / 2f, h));
    }

    /**
	 * @param g
	 * @param code
	 * @param angle
	 * @param view
	 * @see jsesh.mdcDisplayer.drawingElements.HieroglyphsDrawer#draw(Graphics2D,
	 *      String, int, MDCView)
	 */
    public void draw(Graphics2D g, String code, int angle, ViewBox view) {
        Graphics2D tmpG = (Graphics2D) g.create();
        code = normalizeCode(code);
        ShapeChar glyph = null;
        if (isSmallBodyUsed()) {
            glyph = fontManager.getSmallBody(code);
        }
        if (glyph == null) glyph = fontManager.get(code);
        tmpG.scale(view.getXScale(), view.getYScale());
        if (glyph != null) {
            glyph.draw(tmpG, 0, 0, 1.0, 1.0, (float) (angle * Math.PI / 180));
        } else {
            Rectangle2D r = tmpG.getFont().getStringBounds(code, new FontRenderContext(new AffineTransform(), true, true));
            double w = view.getWidth();
            tmpG.scale(w / r.getWidth(), w / r.getWidth());
            if (code.length() > 0) tmpG.drawString(code, (float) -r.getMinX(), (float) -r.getMinY());
        }
        tmpG.dispose();
    }

    /**
	 * @see jsesh.mdcDisplayer.drawingElements.HieroglyphsDrawer#getBBox(String)
	 */
    public Rectangle2D getBBox(String code, int angle, boolean fixed) {
        Rectangle2D result = null;
        code = normalizeCode(code);
        if (angle == 0) {
            ShapeChar glyph = fontManager.get(code);
            if (glyph != null) {
                result = glyph.getBbox();
            } else if (nonHieroglyphic.containsKey(code)) {
                result = getNonHieroglyphic(code).getBounds2D();
            }
        } else if (angle != 0) {
            Shape shape = getShape(code);
            AffineTransform rot = AffineTransform.getRotateInstance(angle * Math.PI / 180f);
            shape = rot.createTransformedShape(shape);
            result = shape.getBounds2D();
        }
        return result;
    }

    private Shape getNonHieroglyphic(String code) {
        return nonHieroglyphic.get(code);
    }

    public Shape getShape(String code) {
        code = normalizeCode(code);
        Shape result = null;
        ShapeChar glyph = fontManager.get(code);
        if (glyph != null) result = glyph.getShape(); else result = getBBox(code, 0, true);
        return result;
    }

    public Area getSignArea(String code, double x, double y, double xscale, double yscale, int angle, boolean reversed) {
        code = normalizeCode(code);
        Area result = null;
        ShapeChar glyph = fontManager.get(code);
        if (glyph != null) result = glyph.getSignArea(x, y, xscale, yscale, angle * Math.PI / 180.0); else if (nonHieroglyphic.containsKey(code)) {
            Shape s = ShapeHelper.transformShape(x, y, xscale, yscale, angle * Math.PI / 180.0, getNonHieroglyphic(code));
            result = new Area(s);
        } else result = new Area();
        return result;
    }

    public boolean isKnown(String code) {
        code = normalizeCode(code);
        return (fontManager.get(code) != null || nonHieroglyphic.containsKey(code));
    }

    public LigatureZone getLigatureZone(int i, String code) {
        code = normalizeCode(code);
        ShapeChar glyph = fontManager.get(code);
        LigatureZone result = null;
        if (glyph != null) {
            if (!glyph.hasZones()) {
                LigatureZoneBuilder l = new LigatureZoneBuilder(glyph);
                for (int k = 0; k < 3; k++) glyph.setZone(k, l.getLigatureArea(k));
            }
            result = glyph.getZone(i);
        }
        return result;
    }

    public double getHeightOfA1() {
        return heightOfA1;
    }

    public double getGroupUnitLength() {
        return getHeightOfA1() / 1000f;
    }

    public void setSmallBodyUsed(boolean smallBody) {
        this.smallBodyUsed = smallBody;
    }

    public boolean isSmallBodyUsed() {
        return smallBodyUsed;
    }

    /**
	 * Returns a normalized version of code. In particular, deals with codes
	 * like '[[' and ']]'.
	 * 
	 * @param code
	 * @return
	 */
    private String normalizeCode(String code) {
        if (normalizedCodesMap.containsKey(code)) return normalizedCodesMap.get(code); else return code;
    }
}
