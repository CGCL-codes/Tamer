package com.softaspects.jsf.tag.popupDialog;

import com.softaspects.jsf.tag.base.WGFComponentBaseTag;
import javax.el.ValueExpression;

/**
 * PopupDialog Component tag
 */
public class BasePopupDialogTag extends WGFComponentBaseTag {

    public String getComponentType() {
        return "com.softaspects.jsf.component.popupDialog.PopupDialog";
    }

    public String getRendererType() {
        return "com.softaspects.jsf.renderer.popupDialog.PopupDialogRenderer";
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
     * Setter for property 'bgColor'
     *
     * @param bgColor 'bgColor' property value
     */
    public void setBgColor(ValueExpression bgColor) {
        setAttribute("bgColor", bgColor);
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
     * Setter for property 'dialogHeight'
     *
     * @param dialogHeight 'dialogHeight' property value
     */
    public void setDialogHeight(ValueExpression dialogHeight) {
        setAttribute("dialogHeight", dialogHeight);
    }

    /**
     * Setter for property 'dialogWidth'
     *
     * @param dialogWidth 'dialogWidth' property value
     */
    public void setDialogWidth(ValueExpression dialogWidth) {
        setAttribute("dialogWidth", dialogWidth);
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
     * Setter for property 'left'
     *
     * @param left 'left' property value
     */
    public void setLeft(ValueExpression left) {
        setAttribute("left", left);
    }

    /**
     * Setter for property 'messageText'
     *
     * @param messageText 'messageText' property value
     */
    public void setMessageText(ValueExpression messageText) {
        setAttribute("messageText", messageText);
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
     * Setter for property 'onClose'
     *
     * @param onClose 'onClose' property value
     */
    public void setOnClose(ValueExpression onClose) {
        setAttribute("onClose", onClose);
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
     * Setter for property 'textStyle'
     *
     * @param textStyle 'textStyle' property value
     */
    public void setTextStyle(ValueExpression textStyle) {
        setAttribute("textStyle", textStyle);
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
     * Setter for property 'urlImage'
     *
     * @param urlImage 'urlImage' property value
     */
    public void setUrlImage(ValueExpression urlImage) {
        setAttribute("urlImage", urlImage);
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
