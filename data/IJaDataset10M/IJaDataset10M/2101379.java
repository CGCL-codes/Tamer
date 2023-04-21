package com.j2biz.blogunity.web.taglibs;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.j2biz.blogunity.exception.BlogunityRuntimeException;
import com.j2biz.blogunity.i18n.I18N;
import com.j2biz.blogunity.i18n.I18NMessageManager;
import com.j2biz.blogunity.i18n.I18NStatusFactory;
import com.j2biz.blogunity.pojo.Category;
import com.j2biz.blogunity.pojo.Entry;
import com.j2biz.blogunity.util.BlogUtils;

public class EntryTag extends TagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Entry entry;

    private String ctx;

    private HttpServletRequest request;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
        request = (HttpServletRequest) pageContext.getRequest();
        ctx = request.getContextPath();
        setEntry((Entry) request.getAttribute("entry"));
        String out = getHTML();
        try {
            pageContext.getOut().print(out.toString());
        } catch (IOException e) {
            throw new BlogunityRuntimeException(I18NStatusFactory.create(I18N.ERRORS.TAGLIB_RENDERING, e));
        }
        return SKIP_BODY;
    }

    private String getHTML() {
        BlogUtils utils = BlogUtils.getInstance();
        I18NMessageManager i18n = I18NMessageManager.getInstance();
        StringBuffer out = new StringBuffer();
        out.append("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">\n");
        out.append("<tbody>\n");
        out.append("<tr bgcolor=\"#F0F0F0\">\n");
        out.append("<td width=\"100%\">\n");
        out.append("<div class=\"smalltitle\">\n");
        out.append(utils.renderEntry(entry, request));
        out.append("</div>\n");
        out.append("</td>\n");
        out.append("<td>\n");
        if (entry.getUserpic() != null) {
            out.append("<img src=\"").append(ctx).append(entry.getUserpic().getUrl()).append("\" border=\"0\">\n");
        }
        out.append("</td>\n");
        out.append("</tr>\n");
        out.append("<tr><td colspan=\"2\">\n");
        Set categories = entry.getCategories();
        int counter = 0;
        for (Iterator itx = categories.iterator(); itx.hasNext(); ) {
            Category c = (Category) itx.next();
            if (counter != 0) out.append(",&nbsp;");
            out.append(utils.renderCategory(entry.getBlog(), c, request));
            counter++;
        }
        out.append("</td></tr>\n");
        if (entry.getExcerpt() != null) {
            out.append("<tr><td colspan=\"2\">");
            out.append("<b>").append(entry.getExcerpt()).append("</b>\n");
            out.append("</div>\n");
            out.append("</td></tr>\n");
        }
        out.append("<tr><td colspan=\"2\">").append(entry.getBody()).append("</td></tr>\n");
        out.append("<tr><td colspan=\"2\">\n");
        out.append(i18n.getMessage("POSTED_BY", request)).append("&nbsp;").append(utils.renderUser(entry.getAuthor(), request)).append("&nbsp;");
        out.append(i18n.getMessage("POSTED_IN", request)).append("&nbsp;").append(utils.renderBlog(entry.getBlog(), request)).append("&nbsp;");
        out.append(i18n.getMessage("POSTED_AT", request)).append("&nbsp;").append(utils.formatDateTime(entry.getCreateTime()));
        out.append("<tr><td>\n");
        out.append("</tbody>\n");
        out.append("</table>\n");
        return out.toString();
    }
}
