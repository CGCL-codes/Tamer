package com.softaspects.jsf.tag.table;

import com.softaspects.jsf.tag.base.WGFComponentBaseTag;
import javax.el.ValueExpression;

/**
 * Header Component tag
 */
public class BaseHeaderTag extends WGFComponentBaseTag {

    public String getComponentType() {
        return "com.softaspects.jsf.component.table.Header";
    }

    public String getRendererType() {
        return "com.softaspects.jsf.renderer.table.HeaderRenderer";
    }

    /**
     * Setter for property 'align'
     *
     * @param align 'align' property value
     */
    public void setAlign(ValueExpression align) {
        setAttribute("align", align);
    }

    /**
     * Setter for property 'alignMode'
     *
     * @param alignMode 'alignMode' property value
     */
    public void setAlignMode(ValueExpression alignMode) {
        setAttribute("alignMode", alignMode);
    }

    /**
     * Setter for property 'defaultTableHeaderRenderer'
     *
     * @param defaultTableHeaderRenderer 'defaultTableHeaderRenderer' property value
     */
    public void setDefaultTableHeaderRenderer(ValueExpression defaultTableHeaderRenderer) {
        setAttribute("defaultTableHeaderRenderer", defaultTableHeaderRenderer);
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
     * Setter for property 'highLightAllowed'
     *
     * @param highLightAllowed 'highLightAllowed' property value
     */
    public void setHighLightAllowed(ValueExpression highLightAllowed) {
        setAttribute("highLightAllowed", highLightAllowed);
    }

    /**
     * Setter for property 'hintText'
     *
     * @param hintText 'hintText' property value
     */
    public void setHintText(ValueExpression hintText) {
        setAttribute("hintText", hintText);
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
     * Setter for property 'model'
     *
     * @param model 'model' property value
     */
    public void setModel(ValueExpression model) {
        setAttribute("model", model);
    }

    /**
     * Setter for property 'noWrapMode'
     *
     * @param noWrapMode 'noWrapMode' property value
     */
    public void setNoWrapMode(ValueExpression noWrapMode) {
        setAttribute("noWrapMode", noWrapMode);
    }

    /**
     * Setter for property 'showHeader'
     *
     * @param showHeader 'showHeader' property value
     */
    public void setShowHeader(ValueExpression showHeader) {
        setAttribute("showHeader", showHeader);
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
     * Setter for property 'tableColumnModel'
     *
     * @param tableColumnModel 'tableColumnModel' property value
     */
    public void setTableColumnModel(ValueExpression tableColumnModel) {
        setAttribute("tableColumnModel", tableColumnModel);
    }

    /**
     * Setter for property 'valign'
     *
     * @param valign 'valign' property value
     */
    public void setValign(ValueExpression valign) {
        setAttribute("valign", valign);
    }

    /**
     * Setter for property 'valignMode'
     *
     * @param valignMode 'valignMode' property value
     */
    public void setValignMode(ValueExpression valignMode) {
        setAttribute("valignMode", valignMode);
    }

    /**
     * Setter for property 'visible'
     *
     * @param visible 'visible' property value
     */
    public void setVisible(ValueExpression visible) {
        setAttribute("visible", visible);
    }
}
