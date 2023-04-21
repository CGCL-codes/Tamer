package com.bluebrim.layout.impl.server.geom;

import org.w3c.dom.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of bounding box run around specification.
 * 
 * @author: Dennis Malmstr�m
 */
public class CoBoundingBoxRunAroundSpec extends CoRunAroundSpec implements CoBoundingBoxRunAroundSpecIF {

    private double m_left = 0;

    private double m_right = 0;

    private double m_top = 0;

    private double m_bottom = 0;

    private boolean m_useStroke = true;

    protected class MutableProxy extends CoRunAroundSpec.MutableProxy implements CoRemoteBoundingBoxRunAroundSpecIF {

        public double getLeft() {
            return CoBoundingBoxRunAroundSpec.this.getLeft();
        }

        public double getRight() {
            return CoBoundingBoxRunAroundSpec.this.getRight();
        }

        public double getTop() {
            return CoBoundingBoxRunAroundSpec.this.getTop();
        }

        public double getBottom() {
            return CoBoundingBoxRunAroundSpec.this.getBottom();
        }

        public void setLeft(double m) {
            if (m == CoBoundingBoxRunAroundSpec.this.getLeft()) return;
            CoBoundingBoxRunAroundSpec.this.setLeft(m);
            notifyOwner();
        }

        public void setRight(double m) {
            if (m == CoBoundingBoxRunAroundSpec.this.getRight()) return;
            CoBoundingBoxRunAroundSpec.this.setRight(m);
            notifyOwner();
        }

        public void setTop(double m) {
            if (m == CoBoundingBoxRunAroundSpec.this.getTop()) return;
            CoBoundingBoxRunAroundSpec.this.setTop(m);
            notifyOwner();
        }

        public void setBottom(double m) {
            if (m == CoBoundingBoxRunAroundSpec.this.getBottom()) return;
            CoBoundingBoxRunAroundSpec.this.setBottom(m);
            notifyOwner();
        }
    }

    public static final String XML_BOTTOM_MARGIN = "bottom-margin";

    public static final String XML_LEFT_MARGIN = "left-margin";

    public static final String XML_RIGHT_MARGIN = "right-margin";

    public static final String XML_TAG = "bounding-box-run-around-spec";

    public static final String XML_TOP_MARGIN = "top-margin";

    public static final String XML_USE_STROKE = "use-stroke";

    public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
        return new CoBoundingBoxRunAroundSpec(node, context);
    }

    public CoBoundingBoxRunAroundSpec() {
        super();
    }

    protected CoBoundingBoxRunAroundSpec(Node node, com.bluebrim.xml.shared.CoXmlContext context) {
        super();
        NamedNodeMap map = node.getAttributes();
        m_useStroke = CoModelBuilder.getBoolAttrVal(map, XML_USE_STROKE, m_useStroke);
        m_top = CoModelBuilder.getDoubleAttrVal(map, XML_TOP_MARGIN, m_top);
        m_bottom = CoModelBuilder.getDoubleAttrVal(map, XML_BOTTOM_MARGIN, m_bottom);
        m_left = CoModelBuilder.getDoubleAttrVal(map, XML_LEFT_MARGIN, m_left);
        m_right = CoModelBuilder.getDoubleAttrVal(map, XML_RIGHT_MARGIN, m_right);
    }

    protected CoRunAroundSpec.MutableProxy createMutableProxy() {
        return new MutableProxy();
    }

    public boolean doUseStroke() {
        return m_useStroke;
    }

    public boolean equals(Object s) {
        if (s == this) return true;
        if (!(s instanceof CoBoundingBoxRunAroundSpec)) return false;
        CoBoundingBoxRunAroundSpec S = (CoBoundingBoxRunAroundSpec) s;
        return (m_useStroke == S.m_useStroke) && (m_left == S.m_left) && (m_right == S.m_right) && (m_top == S.m_top) && (m_bottom == S.m_bottom);
    }

    public double getBottom() {
        return m_bottom;
    }

    public double getBottomMargin() {
        return m_bottom;
    }

    public String getFactoryKey() {
        return BOUNDING_BOX_RUN_AROUND_SPEC;
    }

    public double getLeft() {
        return m_left;
    }

    public double getLeftMargin() {
        return m_left;
    }

    public double getRight() {
        return m_right;
    }

    public double getRightMargin() {
        return m_right;
    }

    public CoImmutableShapeIF getRunAroundShape(CoShapePageItem pi) {
        java.awt.geom.Rectangle2D r = null;
        if (m_useStroke) {
            r = pi.getExteriorShape().getBounds2D();
        } else {
            r = pi.getCoShape().getBounds2D();
        }
        return new CoRectangleShape(r.getX() - m_left, r.getY() - m_top, r.getWidth() + m_left + m_right, r.getHeight() + m_top + m_bottom);
    }

    public double getTop() {
        return m_top;
    }

    public double getTopMargin() {
        return m_top;
    }

    public void setBottom(double b) {
        m_bottom = b;
    }

    public void setLeft(double l) {
        m_left = l;
    }

    public void setRight(double r) {
        m_right = r;
    }

    public void setTop(double t) {
        m_top = t;
    }

    public void setUseStroke(boolean b) {
        m_useStroke = b;
    }

    public String xmlGetTag() {
        return XML_TAG;
    }

    public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
        super.xmlVisit(visitor);
        visitor.exportAttribute(XML_USE_STROKE, (m_useStroke ? Boolean.TRUE : Boolean.FALSE).toString());
        visitor.exportAttribute(XML_TOP_MARGIN, Double.toString(m_top));
        visitor.exportAttribute(XML_BOTTOM_MARGIN, Double.toString(m_bottom));
        visitor.exportAttribute(XML_LEFT_MARGIN, Double.toString(m_left));
        visitor.exportAttribute(XML_RIGHT_MARGIN, Double.toString(m_right));
    }
}
