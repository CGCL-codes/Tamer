package net.woodstock.rockapi.struts2.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.ClosingUIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "popupWindow", tldTagClass = "net.woodstock.rockapi.struts2.views.jsp.ui.PopupWindowTag", description = "Render a window")
public class PopupWindow extends ClosingUIBean {

    public static final String TEMPLATE = "popupWindow";

    public static final String TEMPLATE_CLOSE = "popupWindow-close";

    private String title;

    private String height;

    private String width;

    private String cssBodyStyle;

    private String cssTitleStyle;

    private String center;

    private String left;

    private String top;

    private String titleColor;

    private String titleBackgroundColor;

    private String closeImage;

    public PopupWindow(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return PopupWindow.TEMPLATE;
    }

    @Override
    protected String getDefaultTemplate() {
        return PopupWindow.TEMPLATE_CLOSE;
    }

    @Override
    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (this.title != null) {
            this.addParameter("title", this.findString(this.title));
        }
        if (this.height != null) {
            this.addParameter("height", this.findString(this.height));
        }
        if (this.width != null) {
            this.addParameter("width", this.findString(this.width));
        }
        if (this.cssBodyStyle != null) {
            this.addParameter("cssBodyStyle", this.findString(this.cssBodyStyle));
        }
        if (this.cssTitleStyle != null) {
            this.addParameter("cssTitleStyle", this.findString(this.cssTitleStyle));
        }
        if (this.center != null) {
            this.addParameter("center", this.findString(this.center));
        }
        if (this.left != null) {
            this.addParameter("left", this.findString(this.left));
        }
        if (this.top != null) {
            this.addParameter("top", this.findString(this.top));
        }
        if (this.titleColor != null) {
            this.addParameter("titleColor", this.findString(this.titleColor));
        }
        if (this.titleBackgroundColor != null) {
            this.addParameter("titleBackgroundColor", this.findString(this.titleBackgroundColor));
        }
        if (this.closeImage != null) {
            this.addParameter("closeImage", this.findString(this.closeImage));
        }
    }

    @StrutsTagAttribute(description = "Window height", required = true, type = "Integer")
    public void setHeight(String height) {
        this.height = height;
    }

    @StrutsTagAttribute(description = "Window width", required = true, type = "Integer")
    public void setWidth(String width) {
        this.width = width;
    }

    @StrutsTagAttribute(description = "CSS Style for body")
    public void setCssBodyStyle(String cssBodyStyle) {
        this.cssBodyStyle = cssBodyStyle;
    }

    @StrutsTagAttribute(description = "CSS Style for title")
    public void setCssTitleStyle(String cssTitleStyle) {
        this.cssTitleStyle = cssTitleStyle;
    }

    @StrutsTagAttribute(description = "If window is centered", type = "Boolean", defaultValue = "false")
    public void setCenter(String center) {
        this.center = center;
    }

    @StrutsTagAttribute(description = "Window left posistion", type = "Integer", defaultValue = "50")
    public void setLeft(String left) {
        this.left = left;
    }

    @StrutsTagAttribute(description = "Window top posistion", type = "Integer", defaultValue = "50")
    public void setTop(String top) {
        this.top = top;
    }

    @StrutsTagAttribute(description = "Color for window title")
    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    @StrutsTagAttribute(description = "Background color for window title")
    public void setTitleBackgroundColor(String titleBackgroundColor) {
        this.titleBackgroundColor = titleBackgroundColor;
    }

    @StrutsTagAttribute(description = "Image for close button")
    public void setCloseImage(String closeImage) {
        this.closeImage = closeImage;
    }
}
