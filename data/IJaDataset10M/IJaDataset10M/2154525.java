package org.jenia.faces.datatools.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import org.jenia.faces.datatools.component.html.HtmlListPager;

public class ListPagerTag extends UIComponentELTag {

    private ValueExpression forId;

    private ValueExpression style;

    private ValueExpression styleClass;

    private ValueExpression currentStyle;

    private ValueExpression currentStyleClass;

    private ValueExpression showFirstLast;

    private ValueExpression allwaysShowNextPrev;

    private ValueExpression hideIfOnePage;

    private ValueExpression fastStep;

    public void setFor(ValueExpression forId) {
        this.forId = forId;
    }

    public void setStyle(ValueExpression style) {
        this.style = style;
    }

    public void setCurrentStyle(ValueExpression currentStyle) {
        this.currentStyle = currentStyle;
    }

    public void setCurrentStyleClass(ValueExpression currentStyleClass) {
        this.currentStyleClass = currentStyleClass;
    }

    public void setShowFirstLast(ValueExpression showFirstLast) {
        this.showFirstLast = showFirstLast;
    }

    public void setStyleClass(ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

    public String getRendererType() {
        return HtmlListPager.RENDERER_TYPE;
    }

    public String getComponentType() {
        return HtmlListPager.COMPONENT_TYPE;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        HtmlListPager pager = null;
        try {
            pager = (HtmlListPager) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: HtmlListPager.  Perhaps you're missing a tag?");
        }
        if (forId != null) {
            if (forId.isLiteralText()) {
                pager.setFor(forId.getExpressionString());
            } else {
                pager.setValueExpression("for", forId);
            }
        }
        if (style != null) {
            if (style.isLiteralText()) {
                pager.setStyle(style.getExpressionString());
            } else {
                pager.setValueExpression("style", style);
            }
        }
        if (styleClass != null) {
            if (styleClass.isLiteralText()) {
                pager.setStyleClass(styleClass.getExpressionString());
            } else {
                pager.setValueExpression("styleClass", styleClass);
            }
        }
        if (currentStyle != null) {
            if (currentStyle.isLiteralText()) {
                pager.setCurrentStyle(currentStyle.getExpressionString());
            } else {
                pager.setValueExpression("currentStyle", currentStyle);
            }
        }
        if (currentStyleClass != null) {
            if (currentStyleClass.isLiteralText()) {
                pager.setCurrentStyleClass(currentStyleClass.getExpressionString());
            } else {
                pager.setValueExpression("currentStyleClass", currentStyleClass);
            }
        }
        if (showFirstLast != null) {
            if (showFirstLast.isLiteralText()) {
                pager.setShowFirstLast(showFirstLast.getExpressionString());
            } else {
                pager.setValueExpression("showFirstLast", showFirstLast);
            }
        }
        if (allwaysShowNextPrev != null) {
            if (allwaysShowNextPrev.isLiteralText()) {
                pager.setAllwaysShowNextPrev(allwaysShowNextPrev.getExpressionString());
            } else {
                pager.setValueExpression("allwaysShowNextPrev", allwaysShowNextPrev);
            }
        }
        if (hideIfOnePage != null) {
            if (hideIfOnePage.isLiteralText()) {
                pager.setHideIfOnePage(hideIfOnePage.getExpressionString());
            } else {
                pager.setValueExpression("hideIfOnePage", hideIfOnePage);
            }
        }
        if (fastStep != null) {
            if (fastStep.isLiteralText()) {
                pager.setFastStep(fastStep.getExpressionString());
            } else {
                pager.setValueExpression("fastStep", fastStep);
            }
        }
    }

    public void setAllwaysShowNextPrev(ValueExpression allwaysShowNextPrev) {
        this.allwaysShowNextPrev = allwaysShowNextPrev;
    }

    protected int getDoStartValue() throws JspException {
        HtmlListPager pager = (HtmlListPager) getComponentInstance();
        try {
            pager.setFirstRowOfList();
        } catch (Exception e) {
        }
        return super.getDoStartValue();
    }

    public void setHideIfOnePage(ValueExpression hideIfOnePage) {
        this.hideIfOnePage = hideIfOnePage;
    }

    public void setFastStep(ValueExpression fastStep) {
        this.fastStep = fastStep;
    }
}
