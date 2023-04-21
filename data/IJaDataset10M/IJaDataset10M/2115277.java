package com.bluebrim.base.shared.geom;

import java.awt.*;
import java.awt.geom.*;
import org.w3c.dom.*;
import com.bluebrim.base.shared.*;

/** PENDING: WHO WROT THIS, IS IT STILL VALID ??????????????
 * Rectangle with concave corners
 * Due to non optimized code in draw() methods, 
 * using concave corners results in waiting, but the code is
 * correct except some extra moveTo commands in getShape()
 */
public class CoConcaveCorner extends CoCornerRectangle implements CoConcaveCornerIF, CoGeometryConstants {

    public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
        return new CoConcaveCorner(node, context);
    }

    protected transient GeneralPath m_shape;

    public static final String XML_TAG = "concaveCorner";

    public CoConcaveCorner() {
        super();
    }

    public CoConcaveCorner(double r) {
        super(r);
    }

    public CoConcaveCorner(double x, double y, double width, double height, double r) {
        super(x, y, width, height, r);
    }

    protected CoConcaveCorner(Node node, com.bluebrim.xml.shared.CoXmlContext context) {
        super(node, context);
    }

    protected CoShape copyForTranslation() {
        try {
            CoConcaveCorner c = (CoConcaveCorner) clone();
            c.m_shape = null;
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public CoShapeIF deepClone() {
        CoConcaveCorner c = (CoConcaveCorner) super.deepClone();
        c.m_shape = null;
        return c;
    }

    protected CoShapeIF doCreateExpandedInstance(double delta) {
        return new CoConcaveCorner(getX() - delta, getY() - delta, getWidth() + 2 * delta, getHeight() + 2 * delta, getCornerRadius() + delta - delta * Math.tan(Math.PI / 4));
    }

    public String getFactoryKey() {
        return CoConcaveCornerIF.CONCAVE_CORNER;
    }

    public Shape getShape() {
        if (m_shape == null) {
            m_shape = new java.awt.geom.GeneralPath();
            float width = (float) m_width;
            float height = (float) m_height;
            float x1 = (float) m_x;
            float y1 = (float) m_y;
            if (width < 0) {
                x1 += width;
                width = -width;
            }
            if (height < 0) {
                y1 += height;
                height = -height;
            }
            float yCut = Math.min((float) m_cornerRadius, height / 2);
            float xCut = Math.min((float) m_cornerRadius, width / 2);
            float x2 = x1 + width;
            float y2 = y1 + height;
            float p = (float) 6 / 7;
            m_shape.moveTo(x1 + xCut, y1);
            m_shape.lineTo(x2 - xCut, y1);
            m_shape.quadTo(x2 - p * xCut, y1 + p * yCut, x2, y1 + yCut);
            m_shape.lineTo(x2, y2 - yCut);
            m_shape.quadTo(x2 - p * xCut, y2 - p * yCut, x2 - xCut, y2);
            m_shape.lineTo(x1 + xCut, y2);
            m_shape.quadTo(x1 + p * xCut, y2 - p * yCut, x1, y2 - yCut);
            m_shape.lineTo(x1, y1 + yCut);
            m_shape.quadTo(x1 + p * xCut, y1 + p * yCut, x1 + xCut, y1);
            m_shape.closePath();
        }
        return m_shape;
    }

    public String getXmlTag() {
        return XML_TAG;
    }

    protected void invalidate() {
        super.invalidate();
        m_shape = null;
    }

    protected boolean isOnCorner(double x, double y, double outsideStrokeWidth) {
        double dx = 0;
        double dy = 0;
        if (x - m_x < m_width / 2) {
            if (y - m_y < m_height / 2) {
                dx = m_x - x;
                dy = m_y - y;
            } else {
                dx = m_x - x;
                dy = m_y + m_height - y;
            }
        } else {
            if (y - m_y < m_height / 2) {
                dx = m_x + m_width - x;
                dy = m_y - y;
            } else {
                dx = m_x + m_width - x;
                dy = m_y + m_height - y;
            }
        }
        double D = m_cornerRadius - outsideStrokeWidth;
        return dx * dx + dy * dy > D * D;
    }

    public String toString() {
        return "CoConcaveCorner: " + m_x + " " + m_y + "   " + m_width + " " + m_height + " " + m_cornerRadius;
    }
}
