package foa.bricks.complex;

import java.util.*;
import foa.elements.Element;
import foa.elements.fo.*;
import foa.elements.processor.*;
import foa.bricks.Brick;
import foa.apps.XSLTWriter;

public class UnorderedListItem extends Brick {

    public static class Maker extends Brick.Maker {

        public Brick make(String brickName, String brickClass, String match, String type, String group) {
            return new UnorderedListItem(brickName, brickClass, match, type, group);
        }
    }

    private String itemStyle;

    private boolean isLabel = true;

    public UnorderedListItem(String brickName, String brickClass, String match, String group, String type) {
        super(brickName, brickClass, match, group, type);
        this.setAttribute("uses", "");
        this.setAttribute("select", "");
        this.setAttribute("value", "");
        this.setAttribute("label", "");
    }

    public UnorderedListItem() {
        super();
    }

    public void fillSpecificAtts(String tag, Hashtable attributes) {
        if (tag.equals("fo:block")) {
            if (!isLabel) {
                this.setAttribute("uses", (String) attributes.get("xsl:use-attribute-sets"));
            } else {
                isLabel = false;
            }
        } else if (tag.equals("xsl:value-of")) {
            this.setAttribute("label", (String) attributes.get("select"));
        } else if (tag.equals("xsl:text")) {
        } else if (tag.equals("xsl:apply-templates") && attributes.containsKey("select")) {
            this.setAttribute("select", (String) attributes.get("select"));
        } else if (tag.equals("xsl:value-of") && attributes.containsKey("select") && !isLabel) {
            this.setAttribute("value", (String) attributes.get("select"));
        }
    }

    public static Brick.Maker maker() {
        return new UnorderedListItem.Maker();
    }

    public void writeBrick(XSLTWriter writer) {
        writer.doStart("<xsl:template match=\"" + (String) this.getAttribute("match") + "\" foa:name=\"" + (String) this.getParent().getAttribute("name") + "\" foa:class=\"list\" foa:group=\"list\" foa:type=\"unord-list-item\">");
        writer.doStart("<fo:list-item foa:name=\"" + (String) this.getParent().getAttribute("name") + "\" foa:group=\"" + (String) this.getParent().getAttribute("group") + "\">");
        writer.doStart("<fo:list-item-label foa:name=\"" + (String) this.getParent().getAttribute("name") + "\" foa:group=\"" + (String) this.getParent().getAttribute("group") + "\" end-indent=\"label-end()\">");
        writer.doStart("<fo:block foa:name=\"" + (String) this.getParent().getAttribute("name") + "\" foa:group=\"" + (String) this.getParent().getAttribute("group") + "\" font-family=\"ZapfDingbats\" font-size=\"0.6em\" font-weight=\"normal\" font-style=\"normal\" color=\"black\">");
        writer.doBoth("<xsl:value-of select=\"" + (String) this.getAttribute("label") + "\"/>");
        writer.doEnd("</fo:block>");
        writer.doEnd("</fo:list-item-label>");
        writer.doStart("<fo:list-item-body foa:name=\"" + (String) this.getParent().getAttribute("name") + "\" foa:group=\"" + (String) this.getParent().getAttribute("group") + "\" start-indent=\"body-start()\">");
        writer.doStart("<fo:block foa:name=\"" + (String) this.getParent().getAttribute("name") + "\" foa:group=\"" + (String) this.getParent().getAttribute("group") + "\" xsl:use-attribute-sets=\"" + ((String) this.getAttribute("uses")).substring(((String) this.getAttribute("uses")).indexOf(": ") + 1, ((String) this.getAttribute("uses")).length()) + "\">");
        if (!((String) this.getAttributesTable().get("select")).equals("")) {
            writer.doBoth("<xsl:apply-templates select=\"" + (String) this.getAttribute("select") + "\"/>");
        } else if (!((String) this.getAttributesTable().get("value")).equals("")) {
            writer.doBoth("<xsl:value-of select=\"" + (String) this.getAttribute("value") + "\"/>");
            writer.doBoth("<xsl:apply-templates/>");
        } else {
            writer.doBoth("<xsl:apply-templates/>");
        }
        writer.doEnd("</fo:block>");
        writer.doEnd("</fo:list-item-body>");
        writer.doEnd("</fo:list-item>");
        writer.doEnd("</xsl:template>");
    }
}
