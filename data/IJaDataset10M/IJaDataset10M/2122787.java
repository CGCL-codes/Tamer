package com.j2biz.compote.taglibs;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.RequestUtils;
import com.j2biz.compote.util.SystemUtils;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class ImageFromBundleTag extends TagSupport {

    private String bundle;

    private String srcKey;

    private String altKey;

    private String border;

    private String align;

    private String width;

    private String height;

    private String style;

    private String vspace;

    /**
     * @return Returns the align.
     */
    public String getAlign() {
        return align;
    }

    /**
     * @param align
     *            The align to set.
     */
    public void setAlign(String align) {
        this.align = align;
    }

    /**
     * @return Returns the altKey.
     */
    public String getAltKey() {
        return altKey;
    }

    /**
     * @param altKey
     *            The altKey to set.
     */
    public void setAltKey(String altKey) {
        this.altKey = altKey;
    }

    /**
     * @return Returns the border.
     */
    public String getBorder() {
        return border;
    }

    /**
     * @param border
     *            The border to set.
     */
    public void setBorder(String border) {
        this.border = border;
    }

    /**
     * @return Returns the bundle.
     */
    public String getBundle() {
        return bundle;
    }

    /**
     * @param bundle
     *            The bundle to set.
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
     * @return Returns the height.
     */
    public String getHeight() {
        return height;
    }

    /**
     * @param height
     *            The height to set.
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * @return Returns the srcKey.
     */
    public String getSrcKey() {
        return srcKey;
    }

    /**
     * @param srcKey
     *            The srcKey to set.
     */
    public void setSrcKey(String srcKey) {
        this.srcKey = srcKey;
    }

    /**
     * @return Returns the style.
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style
     *            The style to set.
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return Returns the vspace.
     */
    public String getVspace() {
        return vspace;
    }

    /**
     * @param vspace
     *            The vspace to set.
     */
    public void setVspace(String vspace) {
        this.vspace = vspace;
    }

    /**
     * @return Returns the width.
     */
    public String getWidth() {
        return width;
    }

    /**
     * @param width
     *            The width to set.
     */
    public void setWidth(String width) {
        this.width = width;
    }

    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String iconPath = "";
        String iconDesc = "";
        StringBuffer out = new StringBuffer();
        out.append("<img");
        if (StringUtils.isEmpty(bundle)) iconPath = SystemUtils.getMessage(pageContext, srcKey); else iconPath = SystemUtils.getMessage(pageContext, bundle, srcKey);
        iconPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath() + iconPath;
        out.append(" src=\"" + iconPath + "\"");
        if (StringUtils.isNotEmpty(altKey)) {
            if (StringUtils.isEmpty(bundle)) iconDesc = SystemUtils.getMessage(pageContext, altKey); else iconDesc = SystemUtils.getMessage(pageContext, bundle, altKey);
            out.append(" alt=\"" + iconDesc + "\" title=\"" + iconDesc + "\"");
        }
        if (StringUtils.isNotEmpty(border)) out.append(" border=\"" + border + "\"");
        if (StringUtils.isNotEmpty(width)) out.append(" width=\"" + width + "\"");
        if (StringUtils.isNotEmpty(height)) out.append(" height=\"" + height + "\"");
        if (StringUtils.isNotEmpty(style)) out.append(" style=\"" + style + "\"");
        if (StringUtils.isNotEmpty(vspace)) out.append(" vspace=\"" + vspace + "\"");
        if (StringUtils.isNotEmpty(align)) out.append(" align=\"" + align + "\"");
        out.append("/>");
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(out.toString());
        } catch (IOException e) {
            RequestUtils.saveException(pageContext, e);
            throw new JspException(e);
        }
        return SKIP_BODY;
    }
}
