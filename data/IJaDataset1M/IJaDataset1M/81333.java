package com.bluebrim.stroke.shared;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.paint.shared.*;

/**
 * Class for rendering a shape using a given instance of
 * CoImmutableStrokePropertiesIF.
 * 
 * @author: Dennis Malmstr�m
 */
public class CoStrokeRenderer {

    private CoImmutableStrokePropertiesIF m_strokeProperties;

    private CoImmutableShapeIF m_shape;

    private interface StrokeLayerRenderer {

        public abstract void paint(CoPaintable g);
    }

    private transient List m_strokeLayerRenderers;

    private static Line2D m_line = new Line2D.Double();

    private static class NonSymmetricStrokeLayerRenderer implements StrokeLayerRenderer {

        public Shape m_shape;

        public Stroke m_stroke;

        public Paint m_paint;

        public void paint(CoPaintable g) {
            if (m_paint != null) {
                g.setStroke(m_stroke);
                g.setPaint(m_paint);
                g.draw(m_shape);
            }
        }
    }

    ;

    private static class SymmetricStrokeLayerRenderer implements StrokeLayerRenderer {

        public Shape m_horizontalShape;

        public Stroke m_horizontalStroke;

        public Shape m_verticalShape;

        public Stroke m_verticalStroke;

        public Shape m_cornerShape;

        public Stroke m_cornerStroke;

        public Paint m_paint;

        public void paint(CoPaintable g) {
            if (m_paint != null) {
                g.setPaint(m_paint);
                g.setStroke(m_horizontalStroke);
                g.draw(m_horizontalShape);
                g.setStroke(m_verticalStroke);
                g.draw(m_verticalShape);
                if (m_cornerShape != null) {
                    g.setStroke(m_cornerStroke);
                    g.draw(m_cornerShape);
                }
            }
        }
    }

    ;

    private static class SymmetricStrokeLayerRendererGP implements StrokeLayerRenderer {

        public List m_shape = new ArrayList();

        public List m_stroke = new ArrayList();

        public List m_cornerShape = new ArrayList();

        public List m_cornerStroke = new ArrayList();

        public Paint m_paint;

        public void paint(CoPaintable g) {
            if (m_paint != null) {
                g.setPaint(m_paint);
                Iterator i1 = m_shape.iterator();
                Iterator i2 = m_stroke.iterator();
                if (m_shape.size() != m_stroke.size()) return;
                while (i1.hasNext() && i2.hasNext()) {
                    g.setStroke((Stroke) i2.next());
                    g.draw((Shape) i1.next());
                }
                i1 = m_cornerShape.iterator();
                i2 = m_cornerStroke.iterator();
                if (m_shape.size() != m_stroke.size()) return;
                while (i1.hasNext() && i2.hasNext()) {
                    g.setStroke((Stroke) i2.next());
                    g.draw((Shape) i1.next());
                }
            }
        }
    }

    ;

    private void assureCache() {
        if (m_strokeLayerRenderers != null) return;
        float strokeWidth = getStrokeWidth();
        if (strokeWidth == 0) {
            m_strokeLayerRenderers = null;
            m_heaviestLayerPaint = null;
        } else {
            if (m_shape == null) {
                m_strokeLayerRenderers = null;
                m_heaviestLayerPaint = null;
                return;
            }
            m_strokeLayerRenderers = new ArrayList();
            CoStrokeIF stroke = m_strokeProperties.getStroke();
            if (stroke == null) {
                return;
            }
            String symmetry = m_strokeProperties.getSymmetry();
            if (symmetry.equals(CoStrokePropertiesIF.NON_SYMMETRIC)) {
                assureNonSymmetricCache(strokeWidth, stroke);
            } else if (symmetry.equals(CoStrokePropertiesIF.SYMMETRIC_BY_STRETCHING_CORNERS) && (m_shape instanceof CoRectangleShapeIF)) {
                assureSymmetricByStretchingCornersCache(strokeWidth, stroke);
            } else if (symmetry.equals(CoStrokePropertiesIF.SYMMETRIC_BY_STRETCHING_DASH) && (m_shape instanceof CoRectangleShapeIF)) {
                assureSymmetricByStretchingDashCache(strokeWidth, stroke);
            } else if (symmetry.equals(CoStrokePropertiesIF.SYMEETRIC_BY_STRETCHING_DASH_GP)) {
                assureSymmetricByStretchingDashGPCache(strokeWidth, stroke);
            } else {
                assureNonSymmetricCache(strokeWidth, stroke);
            }
            int I = stroke.getCount();
            if (I > 0) {
                m_thickestLayerProporionalWidth = 0;
                CoStrokeLayerIF ss = null;
                for (int i = 0; i < I; i++) {
                    CoStrokeLayerIF SS = stroke.get(i);
                    double pw = SS.getWidthProportion();
                    if (pw > m_thickestLayerProporionalWidth) {
                        m_thickestLayerProporionalWidth = pw;
                        ss = SS;
                    }
                }
                CoColorIF color = ss.getColor().getDashColor(m_strokeProperties);
                if (color == null) {
                    m_heaviestLayerPaint = null;
                } else {
                    m_heaviestLayerPaint = color.getShadedPreviewColor(ss.getColor().getDashShade(m_strokeProperties));
                }
            }
        }
    }

    private void assureNonSymmetricCache(float strokeWidth, CoStrokeIF stroke) {
        float d = -m_strokeProperties.getInsideWidth();
        int I = stroke.getCount();
        for (int i = 0; i < I; i++) {
            CoStrokeLayerIF ss = stroke.get(i);
            NonSymmetricStrokeLayerRenderer sce = new NonSymmetricStrokeLayerRenderer();
            CoColorIF color = ss.getColor().getDashColor(m_strokeProperties);
            if (color == null) {
                sce.m_paint = null;
            } else {
                sce.m_paint = color.getShadedPreviewColor(ss.getColor().getDashShade(m_strokeProperties));
            }
            float w = strokeWidth * ss.getWidthProportion();
            d += w / 2;
            CoImmutableShapeIF s = m_shape.createExpandedInstance(d);
            sce.m_stroke = ss.getDash().createStroke(w, 0);
            sce.m_shape = s.getShape();
            m_strokeLayerRenderers.add(sce);
            d += w / 2;
        }
    }

    private void assureSymmetricByStretchingCornersCache(float strokeWidth, CoStrokeIF stroke) {
        float d = -m_strokeProperties.getInsideWidth();
        int I = stroke.getCount();
        for (int i = 0; i < I; i++) {
            CoStrokeLayerIF ss = stroke.get(i);
            SymmetricStrokeLayerRenderer sce = new SymmetricStrokeLayerRenderer();
            CoColorIF color = ss.getColor().getDashColor(m_strokeProperties);
            if (color == null) {
                sce.m_paint = null;
            } else {
                sce.m_paint = color.getShadedPreviewColor(ss.getColor().getDashShade(m_strokeProperties));
            }
            float w = strokeWidth * ss.getWidthProportion();
            float D = w / 2;
            d += D;
            CoImmutableShapeIF s = m_shape.createExpandedInstance(d);
            float x = (float) s.getX();
            float y = (float) s.getY();
            float width = (float) s.getWidth();
            float height = (float) s.getHeight();
            float cl = ss.getDash().calculateCycleLength(w);
            {
                int n = 1 + (int) ((width) / cl);
                float phase = ((cl * n + cl / 2 - width) / 2) + cl / 2;
                sce.m_horizontalStroke = ss.getDash().createStroke(w, phase);
                GeneralPath gp = new GeneralPath();
                gp.moveTo(x, y);
                gp.lineTo(x + width, y);
                gp.moveTo(x, y + height);
                gp.lineTo(x + width, y + height);
                sce.m_horizontalShape = gp;
            }
            {
                int n = 1 + (int) ((height) / cl);
                float phase = ((cl * n + cl / 2 - height) / 2) + cl / 2;
                sce.m_verticalStroke = ss.getDash().createStroke(w, phase);
                GeneralPath gp = new GeneralPath();
                gp.moveTo(x, y);
                gp.lineTo(x, y + height);
                gp.moveTo(x + width, y);
                gp.lineTo(x + width, y + height);
                sce.m_verticalShape = gp;
            }
            {
                sce.m_cornerStroke = ss.getDash().createUndashedStroke(w);
                GeneralPath gp = new GeneralPath();
                gp.moveTo(x + w, y);
                gp.lineTo(x, y);
                gp.lineTo(x, y + w);
                gp.moveTo(x + width - w, y);
                gp.lineTo(x + width, y);
                gp.lineTo(x + width, y + w);
                gp.moveTo(x + w, y + height);
                gp.lineTo(x, y + height);
                gp.lineTo(x, y + height - w);
                gp.moveTo(x + width - w, y + height);
                gp.lineTo(x + width, y + height);
                gp.lineTo(x + width, y + height - w);
                sce.m_cornerShape = gp;
            }
            m_strokeLayerRenderers.add(sce);
            d += D;
        }
    }

    private void assureSymmetricByStretchingDashCache(float strokeWidth, CoStrokeIF stroke) {
        float d = -m_strokeProperties.getInsideWidth();
        int I = stroke.getCount();
        for (int i = 0; i < I; i++) {
            CoStrokeLayerIF ss = stroke.get(i);
            SymmetricStrokeLayerRenderer sce = new SymmetricStrokeLayerRenderer();
            CoColorIF color = ss.getColor().getDashColor(m_strokeProperties);
            if (color == null) {
                sce.m_paint = null;
            } else {
                sce.m_paint = color.getShadedPreviewColor(ss.getColor().getDashShade(m_strokeProperties));
            }
            float w = strokeWidth * ss.getWidthProportion();
            float D = w / 2;
            d += D;
            CoImmutableShapeIF s = m_shape.createExpandedInstance(d);
            float x = (float) s.getX();
            float y = (float) s.getY();
            float width = (float) s.getWidth();
            float height = (float) s.getHeight();
            float cl = ss.getDash().calculateCycleLength(w);
            float dash_length = ss.getDash().calculateCycleLength(w);
            float dash[] = ss.getDash().getDash();
            if (dash == null) dash = new float[] { 1, 0 };
            float hLength = w;
            float vLength = w;
            {
                float newdarray[] = new float[dash.length];
                float length = width;
                float factor = length / (((int) (length / dash_length + 0.5) * dash_length));
                float dashPhase = 1;
                CoDashIF useDash = ss.getDash();
                if (Double.isInfinite(factor) || Double.isNaN(factor)) factor = 1; else {
                    for (int ii = 0; ii < dash.length; ii++) newdarray[ii] = (float) dash[ii] * factor;
                    dashPhase = newdarray[0] / 2;
                    useDash = ss.getDash().deepClone();
                    useDash.setDash(newdarray);
                    hLength = dashPhase * useDash.calculateCycleLength(w);
                }
                sce.m_horizontalStroke = useDash.createStroke(w, dashPhase * useDash.calculateCycleLength(w));
                GeneralPath gp = new GeneralPath();
                gp.moveTo(x, y);
                gp.lineTo(x + width, y);
                gp.moveTo(x, y + height);
                gp.lineTo(x + width, y + height);
                sce.m_horizontalShape = gp;
            }
            {
                float newdarray[] = new float[dash.length];
                float length = height;
                float factor = length / (((int) (length / dash_length + 0.5) * dash_length));
                float dashPhase = 1;
                CoDashIF useDash = ss.getDash();
                if (Double.isInfinite(factor) || Double.isNaN(factor)) factor = 1; else {
                    for (int ii = 0; ii < dash.length; ii++) newdarray[ii] = (float) dash[ii] * factor;
                    dashPhase = newdarray[0] / 2;
                    useDash = ss.getDash().deepClone();
                    useDash.setDash(newdarray);
                    vLength = dashPhase * useDash.calculateCycleLength(w);
                }
                sce.m_verticalStroke = useDash.createStroke(w, dashPhase * useDash.calculateCycleLength(w));
                GeneralPath gp = new GeneralPath();
                gp.moveTo(x, y);
                gp.lineTo(x, y + height);
                gp.moveTo(x + width, y);
                gp.lineTo(x + width, y + height);
                sce.m_verticalShape = gp;
            }
            {
                sce.m_cornerStroke = ss.getDash().createUndashedStroke(w);
                GeneralPath gp = new GeneralPath();
                gp.moveTo(x + hLength, y);
                gp.lineTo(x, y);
                gp.lineTo(x, y + vLength);
                gp.moveTo(x + width - hLength, y);
                gp.lineTo(x + width, y);
                gp.lineTo(x + width, y + vLength);
                gp.moveTo(x + hLength, y + height);
                gp.lineTo(x, y + height);
                gp.lineTo(x, y + height - vLength);
                gp.moveTo(x + width - hLength, y + height);
                gp.lineTo(x + width, y + height);
                gp.lineTo(x + width, y + height - vLength);
                sce.m_cornerShape = gp;
            }
            m_strokeLayerRenderers.add(sce);
            d += D;
        }
    }

    private void assureSymmetricByStretchingDashGPCache(float strokeWidth, CoStrokeIF stroke) {
        float d = -m_strokeProperties.getInsideWidth();
        int I = stroke.getCount();
        for (int i = 0; i < I; i++) {
            CoStrokeLayerIF ss = stroke.get(i);
            SymmetricStrokeLayerRendererGP sce = new SymmetricStrokeLayerRendererGP();
            CoColorIF color = ss.getColor().getDashColor(m_strokeProperties);
            if (color == null) {
                sce.m_paint = null;
            } else {
                sce.m_paint = color.getShadedPreviewColor(ss.getColor().getDashShade(m_strokeProperties));
            }
            float w = strokeWidth * ss.getWidthProportion();
            float D = w / 2;
            d += D;
            CoImmutableShapeIF s = m_shape.createExpandedInstance(d);
            float cl = ss.getDash().calculateCycleLength(w);
            float dash_length = ss.getDash().calculateCycleLength(w);
            float dash[] = ss.getDash().getDash();
            if (dash == null) dash = new float[] { 1, 0 };
            float fLength = Float.NaN;
            float sLength1 = Float.NaN;
            List phase = new LinkedList();
            float length = 0;
            PathIterator pi = s.getPathIterator(null);
            float seg[] = new float[6];
            int segType = 0;
            float x = 0, y = 0, dx, dy;
            Point2D p1 = new Point2D.Double();
            Point2D p2 = new Point2D.Double();
            Point2D begin = null;
            while (!pi.isDone()) {
                segType = pi.currentSegment(seg);
                switch(segType) {
                    case PathIterator.SEG_MOVETO:
                        x = seg[0];
                        y = seg[1];
                        p1.setLocation(x, y);
                        if (begin == null) begin = new Point2D.Double(x, y);
                        pi.next();
                        continue;
                    case PathIterator.SEG_LINETO:
                        dx = seg[0];
                        dy = seg[1];
                        p2 = new Point2D.Double(dx, dy);
                        length = (float) p1.distance(p2);
                        sce.m_shape.add(new Line2D.Double(p1, p2));
                        p1 = p2;
                        break;
                    case PathIterator.SEG_CLOSE:
                        p2 = begin;
                        length = (float) p1.distance(p2);
                        sce.m_shape.add(new Line2D.Double(p1, p2));
                        p1 = p2;
                        break;
                    case PathIterator.SEG_CUBICTO:
                        Point2D[] varr = new Point2D[4];
                        varr[0] = p1;
                        varr[1] = new Point2D.Double(seg[0], seg[1]);
                        varr[2] = new Point2D.Double(seg[2], seg[3]);
                        varr[3] = new Point2D.Double(seg[4], seg[5]);
                        p2 = varr[3];
                        length = (float) CoBezierLength.arclen(varr, 0.00001);
                        sce.m_shape.add(new CubicCurve2D.Double(p1.getX(), p1.getY(), seg[0], seg[1], seg[2], seg[3], seg[4], seg[5]));
                        p1 = p2;
                        break;
                    case PathIterator.SEG_QUADTO:
                        {
                            varr = new Point2D[4];
                            varr[0] = p1;
                            varr[1] = new Point2D.Double(seg[0], seg[1]);
                            varr[2] = new Point2D.Double(seg[0], seg[1]);
                            varr[3] = new Point2D.Double(seg[2], seg[3]);
                            p2 = varr[3];
                            length = (float) CoBezierLength.arclen(varr, 0.00001);
                            sce.m_shape.add(new QuadCurve2D.Double(p1.getX(), p1.getY(), seg[0], seg[1], seg[2], seg[3]));
                            p1 = p2;
                        }
                        break;
                    default:
                        throw new Error("Illegal seg type : " + segType);
                }
                pi.next();
                float newdarray[] = new float[dash.length];
                float factor = length / (((int) (length / dash_length + 0.5) * dash_length));
                float dashPhase = 1;
                CoDashIF useDash = ss.getDash();
                if (Double.isInfinite(factor) || Double.isNaN(factor)) factor = 1; else {
                    for (int ii = 0; ii < dash.length; ii++) newdarray[ii] = (float) dash[ii] * factor;
                    dashPhase = newdarray[0] / 2;
                    useDash = ss.getDash().deepClone();
                    useDash.setDash(newdarray);
                    phase.add(new Float(dashPhase * useDash.calculateCycleLength(w)));
                    if (phase.size() == 1) phase.add(phase.get(0));
                    if (phase.size() > 3) phase.remove(1);
                }
                sce.m_stroke.add(useDash.createStroke(w, dashPhase * useDash.calculateCycleLength(w)));
                if (phase.size() > 2) {
                }
            }
            m_strokeLayerRenderers.add(sce);
            d += D;
        }
    }

    public float getStrokeWidth() {
        return m_strokeProperties == null ? 0 : m_strokeProperties.getWidth();
    }

    public void setShape(CoImmutableShapeIF shape) {
        m_shape = shape;
        m_strokeLayerRenderers = null;
    }

    public void setStrokeProperties(CoImmutableStrokePropertiesIF props) {
        m_strokeProperties = props;
        m_strokeLayerRenderers = null;
    }

    private transient Paint m_heaviestLayerPaint;

    private transient double m_thickestLayerProporionalWidth;

    public static boolean USE_ANTIALIASING = true;

    public void paint(CoPaintable g) {
        assureCache();
        if (m_strokeLayerRenderers != null) {
            double scale = g.getScale();
            double W = m_strokeProperties.getWidth() * scale;
            if (!useAntiAliasing() && W < 1 / m_thickestLayerProporionalWidth) {
                g.setPaint(m_heaviestLayerPaint);
                g.setStroke(new BasicStroke((float) (1 / scale)));
                g.draw(m_shape.getShape());
            } else {
                Object tmp = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
                if (useAntiAliasing()) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                CoStrokeIF stroke = m_strokeProperties.getStroke();
                int I = m_strokeLayerRenderers.size();
                for (int i = 0; i < I; i++) {
                    CoStrokeLayerIF ss = stroke.get(i);
                    if (useAntiAliasing() || ss.getWidthProportion() * W >= 1) {
                        StrokeLayerRenderer e = (StrokeLayerRenderer) m_strokeLayerRenderers.get(i);
                        e.paint(g);
                    }
                }
                if (useAntiAliasing() && tmp != null) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, tmp);
            }
        }
    }

    private static boolean useAntiAliasing() {
        return USE_ANTIALIASING;
    }
}
