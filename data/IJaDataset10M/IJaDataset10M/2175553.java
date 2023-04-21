package com.softaspects.jsf.tag.container;

import com.softaspects.jsf.tag.base.WGFComponentBaseTag;
import javax.el.ValueExpression;

/**
 * AbsoluteLayoutManager Component tag
 */
public class BaseAbsoluteLayoutManagerTag extends WGFComponentBaseTag {

    public String getComponentType() {
        return "com.softaspects.jsf.component.container.AbsoluteLayoutManager";
    }

    public String getRendererType() {
        return "com.softaspects.jsf.renderer.container.AbsoluteLayoutManagerRenderer";
    }

    /**
     * Setter for property 'beanName'
     *
     * @param beanName 'beanName' property value
     */
    public void setBeanName(ValueExpression beanName) {
        setAttribute("beanName", beanName);
    }

    /**
     * Setter for property 'binding'
     *
     * @param binding 'binding' property value
     */
    public void setBinding(ValueExpression binding) {
        setAttribute("binding", binding);
    }

    /**
     * Setter for property 'height'
     *
     * @param height 'height' property value
     */
    public void setHeight(ValueExpression height) {
        setAttribute("height", height);
    }

    /**
     * Setter for property 'immediate'
     *
     * @param immediate 'immediate' property value
     */
    public void setImmediate(ValueExpression immediate) {
        setAttribute("immediate", immediate);
    }

    /**
     * Setter for property 'left'
     *
     * @param left 'left' property value
     */
    public void setLeft(ValueExpression left) {
        setAttribute("left", left);
    }

    /**
     * Setter for property 'overflow'
     *
     * @param overflow 'overflow' property value
     */
    public void setOverflow(ValueExpression overflow) {
        setAttribute("overflow", overflow);
    }

    /**
     * Setter for property 'overwriteExistent'
     *
     * @param overwriteExistent 'overwriteExistent' property value
     */
    public void setOverwriteExistent(ValueExpression overwriteExistent) {
        setAttribute("overwriteExistent", overwriteExistent);
    }

    /**
     * Setter for property 'scope'
     *
     * @param scope 'scope' property value
     */
    public void setScope(ValueExpression scope) {
        setAttribute("scope", scope);
    }

    /**
     * Setter for property 'style'
     *
     * @param style 'style' property value
     */
    public void setStyle(ValueExpression style) {
        setAttribute("style", style);
    }

    /**
     * Setter for property 'styleClass'
     *
     * @param styleClass 'styleClass' property value
     */
    public void setStyleClass(ValueExpression styleClass) {
        setAttribute("styleClass", styleClass);
    }

    /**
     * Setter for property 'title'
     *
     * @param title 'title' property value
     */
    public void setTitle(ValueExpression title) {
        setAttribute("title", title);
    }

    /**
     * Setter for property 'top'
     *
     * @param top 'top' property value
     */
    public void setTop(ValueExpression top) {
        setAttribute("top", top);
    }

    /**
     * Setter for property 'valueExpression'
     *
     * @param valueExpression 'valueExpression' property value
     */
    public void setValueExpression(ValueExpression valueExpression) {
        setAttribute("valueExpression", valueExpression);
    }

    /**
     * Setter for property 'variable'
     *
     * @param variable 'variable' property value
     */
    public void setVariable(ValueExpression variable) {
        setAttribute("variable", variable);
    }

    /**
     * Setter for property 'width'
     *
     * @param width 'width' property value
     */
    public void setWidth(ValueExpression width) {
        setAttribute("width", width);
    }
}
