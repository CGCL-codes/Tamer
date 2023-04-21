package org.ujac.print.tag.graphics;

import org.ujac.print.AttributeDefinition;
import org.ujac.print.AttributeDefinitionMap;
import org.ujac.print.ChildDefinition;
import org.ujac.print.ChildDefinitionMap;
import org.ujac.print.DocumentHandlerException;
import org.ujac.print.TagAttributeException;
import com.lowagie.text.pdf.PdfContentByte;

/**
 * Name: DrawPolygonTag<br>
 * Description: Implementation of the &lt;draw-polyon&gt; tag.
 * 
 * @author lauerc
 */
public class DrawPolygonTag extends BaseGraphicsTag implements PointHolder {

    /** The item's name. */
    public static final String TAG_NAME = "draw-polygon";

    /** Definition of the 'fill' attribute. */
    private static final AttributeDefinition POLYGON_FILL = FILL.cloneAttrDef("Tells whether or not to fill the polygon, default is false.");

    /** The number of points of the polygon to draw. */
    private int numPoints = 0;

    /**
   * Constructs a DrawPolygonTag instance with no specific attributes.
   */
    public DrawPolygonTag() {
        super(TAG_NAME);
    }

    /**
   * Gets a brief description for the item.
   * @return The item's description.
   */
    public String getDescription() {
        return "Draws a polygon.";
    }

    /**
   * Gets the list of supported attributes.
   * @return The attribute definitions.
   */
    protected AttributeDefinitionMap buildSupportedAttributes() {
        return super.buildSupportedAttributes().addDefinition(POLYGON_FILL);
    }

    /**
   * Gets the list of supported childs.
   * @return The child definitions.
   */
    public ChildDefinitionMap buildSupportedChilds() {
        return super.buildSupportedChilds().addDefinition(new ChildDefinition(PointTag.class, 0, ChildDefinition.UNLIMITED));
    }

    /**
   * Opens the item.
   * @exception DocumentHandlerException Thrown in case something went wrong while opening the document item.
   */
    public void openItem() throws DocumentHandlerException {
        super.openItem();
        this.numPoints = 0;
        if (!isValid()) {
            return;
        }
    }

    /**
   * Closes the item.
   * @exception DocumentHandlerException Thrown in case something went wrong while processing the document item.
   */
    public void closeItem() throws DocumentHandlerException {
        if (!isValid()) {
            return;
        }
        boolean fill = getBooleanAttribute(FILL, false, true, null);
        PdfContentByte cb = this.graphicsTag.getContentByte();
        if (fill) {
            cb.closePathFillStroke();
        } else {
            cb.closePathStroke();
        }
    }

    /**
   * Adds the given point to the point holder's container.
   * @param x The horizontal position of the point.
   * @param y The vertical position of the point.
   * @exception TagAttributeException In case the adding of the option has been failed.
   */
    public void addPoint(float x, float y) throws TagAttributeException {
        PdfContentByte cb = this.graphicsTag.getContentByte();
        if (numPoints == 0) {
            cb.moveTo(x, y);
        } else {
            cb.lineTo(x, y);
        }
        ++numPoints;
    }
}
