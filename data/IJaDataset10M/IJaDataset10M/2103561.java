package de.iritgo.aktera.struts.tags.html;

import de.iritgo.aktera.struts.tags.BaseTagSupport;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.RequestUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * This creates a HTML img Tag.
 */
public class ImageTag extends BaseTagSupport {

    /** */
    private static final long serialVersionUID = 1L;

    /** The image name (no path and no extension). */
    private String src;

    /** The name of the bean containing the image src. */
    private String name;

    /** The name of the bean property containing the image src. */
    private String property;

    /** The scope containing the src bean. */
    private String scope;

    /** Alternate image text. */
    private String alt;

    /** Image border. */
    private String border;

    /** Image alignment. */
    private String align;

    /** Image style. */
    private String styleClass;

    /** The image width. */
    private String width;

    /** The image height. */
    private String height;

    /** The image path prefix. */
    private String path;

    /** On click code. */
    private String onclick;

    /** The tag id. */
    private String styleId;

    /**
	 * Get the image name.
	 *
	 * @return The image name.
	 */
    public String getSrc() {
        return src;
    }

    /**
	 * Set the image name.
	 *
	 * @param src The image name.
	 */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
	 * Get the image name.
	 *
	 * @return The image name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Set the image name.
	 *
	 * @param name The image name.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Get the image name.
	 *
	 * @return The image name.
	 */
    public String getProperty() {
        return property;
    }

    /**
	 * Set the image name.
	 *
	 * @param property The image name.
	 */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
	 * Get the image name.
	 *
	 * @return The image name.
	 */
    public String getScope() {
        return scope;
    }

    /**
	 * Set the image name.
	 *
	 * @param scope The image name.
	 */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
	 * Get the alternate image text.
	 *
	 * @return The alternate image text.
	 */
    public String getAlt() {
        return alt;
    }

    /**
	 * Set the alternate image text.
	 *
	 * @param alt The alternate image text.
	 */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
	 * Get the image border.
	 *
	 * @return The image border.
	 */
    public String getBorder() {
        return border;
    }

    /**
	 * Set the image border.
	 *
	 * @param border The image border.
	 */
    public void setBorder(String border) {
        this.border = border;
    }

    /**
	 * Get the image alignment.
	 *
	 * @return The image alignment.
	 */
    public String getAlign() {
        return align;
    }

    /**
	 * Set the image alignment.
	 *
	 * @param align The image alignment.
	 */
    public void setAlign(String align) {
        this.align = align;
    }

    /**
	 * Get the image style class.
	 *
	 * @return The image style class.
	 */
    public String getStyleClass() {
        return styleClass;
    }

    /**
	 * Set the image style class.
	 *
	 * @param style The image style class.
	 */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
	 * Get the image width.
	 *
	 * @return The image width.
	 */
    public String getWidth() {
        return width;
    }

    /**
	 * Set the image width.
	 *
	 * @param width The image width.
	 */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
	 * Get the image height.
	 *
	 * @return The image height.
	 */
    public String getHeight() {
        return height;
    }

    /**
	 * Set the image height.
	 *
	 * @param height The image height.
	 */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
	 * Get the image path.
	 *
	 * @return The image path.
	 */
    public String getPath() {
        return path;
    }

    /**
	 * Set the image path.
	 *
	 * @param path The image path.
	 */
    public void setPath(String path) {
        if (!path.endsWith("/")) {
            this.path = path + "/";
        } else {
            this.path = path;
        }
    }

    /**
	 * Get the on click code.
	 *
	 * @return The on click code.
	 */
    public String getOnclick() {
        return onclick;
    }

    /**
	 * Set the on click code.
	 *
	 * @param src The on click code.
	 */
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    /**
	 * Get the tag id.
	 *
	 * @return The tag id.
	 */
    public String getStyleId() {
        return styleId;
    }

    /**
	 * Set the tag id.
	 *
	 * @param styleId The tag id.
	 */
    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    /**
	 * Reset all tag attributes to their default values.
	 */
    public void release() {
        src = null;
        alt = null;
        border = "0";
        align = null;
        styleClass = null;
        width = null;
        height = null;
        name = null;
        property = null;
        scope = "page";
        path = "";
        onclick = "";
        styleId = null;
    }

    /**
	 * Execute the tag.
	 *
	 * @return EVAL_PAGE.
	 */
    public int doEndTag() throws JspException {
        try {
            if (name != null) {
                Object value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
                if (value != null) {
                    src = value.toString();
                }
            }
            String browser = ((HttpServletRequest) pageContext.getRequest()).getHeader("User-Agent");
            if (browser == null) {
                browser = "unknown";
            }
            browser = browser.toLowerCase();
            String url = ((HttpServletRequest) pageContext.getRequest()).getContextPath() + path + src;
            if (!url.endsWith(".gif") && !url.endsWith(".png") && !url.endsWith(".jpg")) {
                url = url + ((browser.indexOf("msie") != -1) && (browser.indexOf("opera") == -1) ? ".gif" : ".png");
            }
            pageContext.getOut().print("<img src=\"" + url + "\"" + ((alt != null) ? (" alt=\"" + alt + "\"") : "") + ((styleId != null) ? (" styleId=\"" + styleId + "\"") : "") + ((border != null) ? (" border=\"" + border + "\"") : "") + ((align != null) ? (" align=\"" + align + "\"") : "") + ((styleClass != null) ? (" class=\"" + styleClass + "\"") : "") + ((width != null) ? (" width=\"" + width + "\"") : "") + ((height != null) ? (" height=\"" + height + "\"") : "") + ((onclick != null) ? (" onclick=\"" + onclick + "\"") : "") + "/>");
            return EVAL_PAGE;
        } catch (Exception x) {
            throw new JspException(x);
        }
    }
}
