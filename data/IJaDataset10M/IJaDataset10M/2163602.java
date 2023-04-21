package foa.bricks.complex;

import java.util.*;
import foa.elements.Element;
import foa.elements.fo.*;
import foa.elements.processor.*;
import foa.bricks.Brick;
import foa.apps.XSLTWriter;

public class ListItem extends Brick {

    public static class Maker extends Brick.Maker {

        public Brick make(String brickName, String brickClass, String match, String type, String group) {
            return new ListItem(brickName, brickClass, match, type, group);
        }
    }

    public ListItem(String brickName, String brickClass, String match, String group, String type) {
        super(brickName, brickClass, match, group, type);
        this.setAttribute("uses", "");
        this.setAttribute("value", "");
        this.setAttribute("select", "");
    }

    public ListItem() {
        super();
    }

    public void fillSpecificAtts(String tag, Hashtable attributes) {
        if (tag.equals("fo:list-item")) {
            this.setAttribute("uses", (String) attributes.get("xsl:use-attribute-sets"));
        } else if (tag.equals("xsl:apply-templates") && attributes.containsKey("select")) {
            this.setAttribute("select", (String) attributes.get("select"));
        } else if (tag.equals("xsl:value-of") && attributes.containsKey("select")) {
            this.setAttribute("value", (String) attributes.get("select"));
        }
    }

    public static Brick.Maker maker() {
        return new ListItem.Maker();
    }

    public void writeBrick(XSLTWriter writer) {
        writer.doStart("<xsl:template match=\"" + (String) this.getAttribute("match") + "\" foa:name=\"" + (String) this.getParent().getAttribute("name") + "\" foa:class=\"list\" foa:group=\"list\" foa:type=\"list-item\">");
        writer.doStart("<fo:list-item foa:name=\"" + (String) this.getParent().getAttribute("name") + "\" foa:group=\"list\" xsl:use-attribute-sets=\"" + ((String) this.getAttribute("uses")).substring(((String) this.getAttribute("uses")).indexOf(": ") + 1, ((String) this.getAttribute("uses")).length()) + "\">");
        if (!((String) this.getAttributesTable().get("select")).equals("")) {
            writer.doBoth("<xsl:apply-templates select=\"" + (String) this.getAttribute("select") + "\"/>");
        } else if (!((String) this.getAttributesTable().get("value")).equals("")) {
            writer.doBoth("<xsl:value-of select=\"" + (String) this.getAttribute("value") + "\"/>");
            writer.doBoth("<xsl:apply-templates/>");
        } else {
            writer.doBoth("<xsl:apply-templates/>");
        }
        writer.doEnd("</fo:list-item>");
        writer.doEnd("</xsl:template>");
    }
}
