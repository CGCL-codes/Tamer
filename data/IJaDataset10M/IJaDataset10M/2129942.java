package com.softaspects.jsf.tag.spinner;

import com.softaspects.jsf.tag.base.WGFComponentBaseTag;
import javax.el.ValueExpression;

/**
 * Spinner Component tag
 */
public class BaseSpinnerTag extends WGFComponentBaseTag {

    public String getComponentType() {
        return "com.softaspects.jsf.component.spinner.Spinner";
    }

    public String getRendererType() {
        return "com.softaspects.jsf.renderer.spinner.SpinnerRenderer";
    }

    /**
     * Setter for property 'accessKey'
     *
     * @param accessKey 'accessKey' property value
     */
    public void setAccessKey(ValueExpression accessKey) {
        setAttribute("accessKey", accessKey);
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
     * Setter for property 'constraint'
     *
     * @param constraint 'constraint' property value
     */
    public void setConstraint(ValueExpression constraint) {
        setAttribute("constraint", constraint);
    }

    /**
     * Setter for property 'dataModelWasChangedFromRequest'
     *
     * @param dataModelWasChangedFromRequest 'dataModelWasChangedFromRequest' property value
     */
    public void setDataModelWasChangedFromRequest(ValueExpression dataModelWasChangedFromRequest) {
        setAttribute("dataModelWasChangedFromRequest", dataModelWasChangedFromRequest);
    }

    /**
     * Setter for property 'enabled'
     *
     * @param enabled 'enabled' property value
     */
    public void setEnabled(ValueExpression enabled) {
        setAttribute("enabled", enabled);
    }

    /**
     * Setter for property 'focused'
     *
     * @param focused 'focused' property value
     */
    public void setFocused(ValueExpression focused) {
        setAttribute("focused", focused);
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
     * Setter for property 'inputFieldId'
     *
     * @param inputFieldId 'inputFieldId' property value
     */
    public void setInputFieldId(ValueExpression inputFieldId) {
        setAttribute("inputFieldId", inputFieldId);
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
     * Setter for property 'max'
     *
     * @param max 'max' property value
     */
    public void setMax(ValueExpression max) {
        setAttribute("max", max);
    }

    /**
     * Setter for property 'min'
     *
     * @param min 'min' property value
     */
    public void setMin(ValueExpression min) {
        setAttribute("min", min);
    }

    /**
     * Setter for property 'name'
     *
     * @param name 'name' property value
     */
    public void setName(ValueExpression name) {
        setAttribute("name", name);
    }

    /**
     * Setter for property 'required'
     *
     * @param required 'required' property value
     */
    public void setRequired(ValueExpression required) {
        setAttribute("required", required);
    }

    /**
     * Setter for property 'selectionModelWasChangedFromRequest'
     *
     * @param selectionModelWasChangedFromRequest 'selectionModelWasChangedFromRequest' property value
     */
    public void setSelectionModelWasChangedFromRequest(ValueExpression selectionModelWasChangedFromRequest) {
        setAttribute("selectionModelWasChangedFromRequest", selectionModelWasChangedFromRequest);
    }

    /**
     * Setter for property 'serverSideAction'
     *
     * @param serverSideAction 'serverSideAction' property value
     */
    public void setServerSideAction(ValueExpression serverSideAction) {
        setAttribute("serverSideAction", serverSideAction);
    }

    /**
     * Setter for property 'step'
     *
     * @param step 'step' property value
     */
    public void setStep(ValueExpression step) {
        setAttribute("step", step);
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
     * Setter for property 'tabIndex'
     *
     * @param tabIndex 'tabIndex' property value
     */
    public void setTabIndex(ValueExpression tabIndex) {
        setAttribute("tabIndex", tabIndex);
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
     * Setter for property 'valign'
     *
     * @param valign 'valign' property value
     */
    public void setValign(ValueExpression valign) {
        setAttribute("valign", valign);
    }

    /**
     * Setter for property 'varName'
     *
     * @param varName 'varName' property value
     */
    public void setVarName(ValueExpression varName) {
        setAttribute("varName", varName);
    }

    /**
     * Setter for property 'visible'
     *
     * @param visible 'visible' property value
     */
    public void setVisible(ValueExpression visible) {
        setAttribute("visible", visible);
    }

    /**
     * Setter for property 'width'
     *
     * @param width 'width' property value
     */
    public void setWidth(ValueExpression width) {
        setAttribute("width", width);
    }

    /**
     * Setter for property 'zindex'
     *
     * @param zindex 'zindex' property value
     */
    public void setZindex(ValueExpression zindex) {
        setAttribute("zindex", zindex);
    }
}
