package net.sf.xsltbuddy.xslt.struts.html;

import net.sf.xsltbuddy.xslt.struts.HTMLItem;
import net.sf.xsltbuddy.xslt.struts.HTMLUtil;
import net.sf.xsltbuddy.xslt.struts.LinkTag;
import net.sf.xsltbuddy.xslt.struts.Tag;

/**
 * This class is used to mimic struts html taglibs
 * (Part of xsltbuddy/struts integration)
 */
public class Link implements HTMLItem {

    /** Render <a> tag
   *
   * @param util
   * @param tag
   * @return tag value
   */
    public String render(HTMLUtil util, Tag tag) throws Exception {
        LinkTag linkTag = (LinkTag) tag;
        StringBuffer results = new StringBuffer();
        if (linkTag.getLinkName() != null) {
            results.append("<a name=\"");
            results.append(linkTag.getLinkName());
            results.append("\">");
            return results.toString();
        }
        results.append("<a href=\"");
        results.append(util.calculateURL(linkTag));
        results.append("\"");
        if (linkTag.getTarget() != null) {
            results.append(" target=\"");
            results.append(linkTag.getTarget());
            results.append("\"");
        }
        if (linkTag.getAccesskey() != null) {
            results.append(" accesskey=\"");
            results.append(linkTag.getAccesskey());
            results.append("\"");
        }
        if (linkTag.getTabindex() != null) {
            results.append(" tabindex=\"");
            results.append(linkTag.getTabindex());
            results.append("\"");
        }
        results.append(util.prepareEvents(tag));
        results.append(util.prepareStyles(tag.getStyle(), tag.getStyleClass(), tag.getStyleId(), tag.getTitle(), tag.getAlt()));
        if (linkTag.getText() == null) {
            results.append(" />");
        } else {
            results.append(">" + linkTag.getText() + "</a>");
        }
        return results.toString();
    }
}
