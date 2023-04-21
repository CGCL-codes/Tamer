package org.kabeja.svg.dimension;

import org.kabeja.dxf.DXFDimension;
import org.kabeja.dxf.DXFDimensionStyle;
import org.kabeja.dxf.DXFVariable;
import org.kabeja.svg.SVGConstants;
import org.kabeja.svg.SVGUtils;
import org.kabeja.svg.generators.AbstractSVGSAXGenerator;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth</a>
 *
 */
public abstract class AbstractDimensionOutputter extends AbstractSVGSAXGenerator {

    protected DXFDimension dim;

    public AbstractDimensionOutputter(DXFDimension dim) {
        this.dim = dim;
    }

    protected void outputText(ContentHandler handler) throws SAXException {
        if ((dim.getDimensionText().length() > 0) && (dim.getDimensionText().indexOf("<>") < 0)) {
            System.out.println("Textdim:" + dim.getDimensionText());
            AttributesImpl attr = new AttributesImpl();
            SVGUtils.addAttribute(attr, "x", "" + dim.getTextPoint().getX());
            SVGUtils.addAttribute(attr, "y", "" + dim.getTextPoint().getY());
            StringBuffer transform = new StringBuffer();
            transform.append("matrix(1 0 0 -1 0 ");
            transform.append(2 * dim.getTextPoint().getY());
            transform.append(")");
            if (dim.getTextRotation() != 0.0) {
                double rotation = 0.0;
                if (dim.getDimensionRotation() != 0.0) {
                    rotation = 360 - dim.getDimensionRotation();
                }
                rotation += dim.getTextRotation();
                transform.append(" rotate(");
                transform.append("" + (-1 * rotation));
                transform.append(" " + dim.getTextPoint().getX());
                transform.append(" " + dim.getTextPoint().getY() + " )");
            }
            SVGUtils.addAttribute(attr, "transform", transform.toString());
            DXFDimensionStyle style = dim.getDXFDimensionStyle();
            if (style == null) {
                style = dim.getDXFDocument().getDXFDimensionStyle("STANDARD");
            }
            if ((style != null) && style.hasProperty(DXFDimensionStyle.PROPERTY_DIMTXT)) {
                double height = style.getDoubleProperty(DXFDimensionStyle.PROPERTY_DIMTXT);
                System.out.println("height from style:" + height);
                SVGUtils.addAttribute(attr, "font-size", "" + height);
            } else {
                DXFVariable var = dim.getDXFDocument().getDXFHeader().getVariable("$DIMTXT");
                if ((var != null) && (var.getDoubleValue("40") != 0.0)) {
                    System.out.println("height from doc:" + var.getDoubleValue("40"));
                    SVGUtils.addAttribute(attr, "font-size", "" + var.getDoubleValue("40"));
                }
            }
            SVGUtils.addAttribute(attr, "writing-mode", "rl");
            SVGUtils.addAttribute(attr, SVGConstants.SVG_ATTRIBUTE_TEXT_ANCHOR, "middle");
            SVGUtils.addAttribute(attr, SVGConstants.SVG_ATTRIBUTE_TEXT_ALIGNMENT_BASELINE, "middle");
            SVGUtils.addAttribute(attr, "fill", "currentColor");
            SVGUtils.startElement(handler, SVGConstants.SVG_TEXT, attr);
            SVGUtils.characters(handler, dim.getDimensionText());
            SVGUtils.endElement(handler, SVGConstants.SVG_TEXT);
        }
    }
}
