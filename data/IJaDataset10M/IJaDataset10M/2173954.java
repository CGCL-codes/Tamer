package org.riverock.portlet.manager.site.bean;

import java.io.Serializable;
import java.util.Date;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:28:41
 */
public class CssBean implements Serializable, Css {

    private static final long serialVersionUID = 2057004503L;

    private Long cssId = null;

    private Long siteId = null;

    private String css = "";

    private String cssComment = "";

    private Date date = null;

    private boolean isCurrent = false;

    public CssBean() {
    }

    public CssBean(Css css) {
        this.cssId = css.getCssId();
        this.siteId = css.getSiteId();
        this.css = css.getCss();
        this.cssComment = css.getCssComment();
        this.date = css.getDate();
        this.isCurrent = css.isCurrent();
    }

    public String getCssComment() {
        return cssComment;
    }

    public void setCssComment(String cssComment) {
        this.cssComment = FacesTools.convertParameter(cssComment);
    }

    public Long getCssId() {
        return cssId;
    }

    public void setCssId(Long cssId) {
        this.cssId = cssId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = FacesTools.convertParameter(css);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
