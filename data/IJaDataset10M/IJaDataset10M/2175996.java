package com.hk.frame.web.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import com.hk.frame.util.DataUtil;

public abstract class BaseBodyTag extends BodyTagSupport implements HkTag {

    private static final long serialVersionUID = 6156831085682303775L;

    protected static final String DOMAIN_ATTR = "com.hk.frame.web.taglib.wap.BaseWapTag.domain_attr";

    @Override
    public int doEndTag() throws JspException {
        try {
            adapter(pageContext.getOut());
        } catch (Exception e) {
            throw new JspException(e);
        }
        return super.doEndTag();
    }

    public String appendContextPath(HttpServletRequest request, String uri) {
        if (null == uri) {
            return uri;
        }
        if (!uri.startsWith("/")) {
            return uri;
        }
        String contextPath = request.getContextPath();
        if (contextPath.length() <= 0) return uri;
        if (uri.startsWith(contextPath)) return uri;
        return contextPath + uri;
    }

    protected String buildURL(String domain, String href) {
        String tdomain = domain;
        if (DataUtil.isEmpty(tdomain)) {
            tdomain = (String) this.getRequest().getAttribute(DOMAIN_ATTR);
        }
        String localHref = appendContextPath(this.getRequest(), href);
        if (!DataUtil.isEmpty(tdomain)) {
            StringBuilder sb = new StringBuilder("http://");
            sb.append(tdomain).append(href);
            localHref = sb.toString();
        }
        return localHref;
    }

    /**
	 * @param writer
	 * @throws IOException
	 */
    protected abstract void adapter(JspWriter writer) throws Exception;

    @SuppressWarnings("unchecked")
    protected List<String> getIngoreStringList() {
        List<String> list = (List<String>) this.getRequest().getAttribute(TAG_TEXTAREA_CONTENT);
        if (list == null) {
            list = new ArrayList<String>();
            this.getRequest().setAttribute(TAG_TEXTAREA_CONTENT, list);
        }
        return list;
    }

    protected void clearIngoreString() {
        this.getRequest().removeAttribute(TAG_TEXTAREA_CONTENT);
    }

    protected void setIngoreString(String s) {
        List<String> list = this.getIngoreStringList();
        list.add(s);
    }

    @SuppressWarnings("unchecked")
    protected List<HkTag> getChildTagList() {
        List<HkTag> list = (List<HkTag>) this.getRequest().getAttribute(TAG_LIST);
        if (list == null) {
            list = new ArrayList<HkTag>();
            this.getRequest().setAttribute(TAG_LIST, list);
        }
        return list;
    }

    protected void clearChildTagList() {
        this.getRequest().removeAttribute(TAG_LIST);
    }

    protected void setChildInRequest(HkTag tag) {
        List<HkTag> list = this.getChildTagList();
        list.add(tag);
    }

    /**
	 * 绘制标签Body块的内容
	 * 
	 * @param writer
	 * @throws IOException
	 */
    protected void renderBodyContent(JspWriter writer) throws IOException {
        writer.append(getBodyContentAsString());
    }

    /**
	 * 以字符串的形式返回标签块的内容
	 * 
	 * @return
	 */
    protected String getBodyContentAsString() {
        if (this.bodyContent == null) return "";
        if (this.bodyContent.getString() == null) return "";
        return this.bodyContent.getString();
    }

    protected HttpServletRequest getRequest() {
        return (HttpServletRequest) pageContext.getRequest();
    }

    protected HttpServletResponse getResponse() {
        return (HttpServletResponse) pageContext.getResponse();
    }

    protected HttpSession getSession() {
        return pageContext.getSession();
    }

    protected ServletContext getApplication() {
        return pageContext.getServletContext();
    }

    protected boolean equalValue(Object v1, Object v2) {
        if (v1 == null || v2 == null) {
            return false;
        }
        if ("".equals(v1.toString()) || "".equals(v2.toString())) {
            return false;
        }
        if (v1.equals(v2)) {
            return true;
        }
        if (v1.toString().equals(v2.toString())) {
            return true;
        }
        return false;
    }
}
