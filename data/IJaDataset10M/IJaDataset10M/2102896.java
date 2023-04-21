package cn.vlabs.duckling.vwb.tags;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Introduction Here.
 * @date Feb 25, 2010
 * @author xiejj@cnic.cn
 */
public class FloatEditLinkTag extends VWBBaseTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doVWBStart() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextPath = request.getContextPath();
        String image = "<img src=\"" + contextPath + "/images/edit.png\"/>";
        String html = "<a href=\'" + buildEditLink(contextPath) + "\'>" + image + "</a>";
        try {
            pageContext.getOut().println(html);
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    public void setViewPort(int id) {
        m_viewportid = id;
    }

    public void setEditId(int id) {
        this.m_editid = id;
    }

    private String buildEditLink(String contextPath) {
        int parentid = vwbcontext.getSite().getViewPort(m_editid).getParent();
        String ref = contextPath + "/page/" + m_editid + "?a=edit&parentPage=" + parentid + "&returnPage=" + m_viewportid;
        return ref;
    }

    private int m_viewportid;

    private int m_editid;
}
