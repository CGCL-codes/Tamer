package org.easyrec.taglib;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * JSP tag that is used for creating SF wiki links.
 * <p/>
 * <p><b>Company:&nbsp;</b>
 * SAT, Research Studios Austria</p>
 * <p/>
 * <p><b>Copyright:&nbsp;</b>
 * (c) 2007</p>
 * <p/>
 * <p><b>last modified:</b><br/>
 * $Author: dmann $<br/>
 * $Date: 2011-04-20 10:45:07 +0200 (Mi, 20 Apr 2011) $<br/>
 * $Revision: 18166 $</p>
 *
 * @author David Mann
 */
public class WikiLink implements Tag {

    private String WIKI_URL = "http://sourceforge.net/apps/mediawiki/easyrec/index.php?title=";

    private PageContext pageContext;

    private Tag parent;

    private String name = "";

    private String pageName = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public WikiLink() {
        super();
    }

    public int doStartTag() throws JspTagException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException {
        try {
            String link = WIKI_URL + ("".equals(pageName) ? name.replaceAll(" ", "_") : pageName);
            pageContext.getOut().write("<a href='" + link + "' class='wikiLink' target='blank'>" + name + "</a>");
        } catch (java.io.IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }

    public void release() {
    }

    public void setPageContext(final PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public void setParent(final Tag parent) {
        this.parent = parent;
    }

    public Tag getParent() {
        return parent;
    }
}
