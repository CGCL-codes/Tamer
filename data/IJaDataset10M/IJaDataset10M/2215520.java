package com.softaspects.jsf.component.dualList;

import com.softaspects.jsf.component.base.WGFComponentBase;

/**
 * DualList Component
 */
public class BaseDualList extends WGFComponentBase implements com.softaspects.jsf.component.base.ajax.AjaxSupportedComponent {

    public static final String COMPONENT_TYPE = "com.softaspects.jsf.component.dualList.DualList";

    public static final String RENDERER_TYPE = "com.softaspects.jsf.renderer.dualList.DualListRenderer";

    public String getComponentTypeName() {
        return COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    public String getFamily() {
        return COMPONENT_TYPE;
    }

    public Class[] getPossibleChildComponents() {
        return new Class[] {};
    }

    /**
     * Setter for property 'actionListener'
     *
     * @param actionListener 'actionListener' property value
     */
    public void setActionListener(javax.faces.component.UIComponent actionListener) {
        setProperty("actionListener", actionListener);
    }

    /**
     * Getter for property 'actionListener'
     *
     * @return actionListener 'actionListener' property value
     */
    public javax.faces.component.UIComponent getActionListener() {
        return (javax.faces.component.UIComponent) getProperty("actionListener");
    }

    /**
     * Setter for property 'actionListeners'
     *
     * @param actionListeners 'actionListeners' property value
     */
    public void setActionListeners(com.softaspects.jsf.component.dualList.DualList actionListeners) {
        setProperty("actionListeners", actionListeners);
    }

    /**
     * Getter for property 'actionListeners'
     *
     * @return actionListeners 'actionListeners' property value
     */
    public com.softaspects.jsf.component.dualList.DualList getActionListeners() {
        return (com.softaspects.jsf.component.dualList.DualList) getProperty("actionListeners");
    }

    /**
     * Setter for property 'ajaxClientHandler'
     *
     * @param ajaxClientHandler 'ajaxClientHandler' property value
     */
    public void setAjaxClientHandler(java.lang.String ajaxClientHandler) {
        setProperty("ajaxClientHandler", ajaxClientHandler);
    }

    /**
     * Getter for property 'ajaxClientHandler'
     *
     * @return ajaxClientHandler 'ajaxClientHandler' property value
     */
    public java.lang.String getAjaxClientHandler() {
        return (java.lang.String) getProperty("ajaxClientHandler");
    }

    /**
     * Setter for property 'ajaxEnabled'
     *
     * @param ajaxEnabled 'ajaxEnabled' property value
     */
    public void setAjaxEnabled(java.lang.Boolean ajaxEnabled) {
        setProperty("ajaxEnabled", ajaxEnabled);
    }

    /**
     * Getter for property 'ajaxEnabled'
     *
     * @return ajaxEnabled 'ajaxEnabled' property value
     */
    public java.lang.Boolean getAjaxEnabled() {
        return (java.lang.Boolean) getProperty("ajaxEnabled", Boolean.FALSE);
    }

    /**
     * Getter for property 'ajaxEnabled'
     *
     * @return ajaxEnabled 'ajaxEnabled' property value
     */
    public java.lang.Boolean isAjaxEnabled() {
        return (java.lang.Boolean) getProperty("ajaxEnabled", Boolean.FALSE);
    }

    /**
     * Setter for property 'ajaxListener'
     *
     * @param ajaxListener 'ajaxListener' property value
     */
    public void setAjaxListener(com.softaspects.jsf.component.base.listener.AjaxEventListener ajaxListener) {
        setProperty("ajaxListener", ajaxListener);
    }

    /**
     * Getter for property 'ajaxListener'
     *
     * @return ajaxListener 'ajaxListener' property value
     */
    public com.softaspects.jsf.component.base.listener.AjaxEventListener getAjaxListener() {
        return (com.softaspects.jsf.component.base.listener.AjaxEventListener) getProperty("ajaxListener");
    }

    /**
     * Setter for property 'ajaxProgressBarComponent'
     *
     * @param ajaxProgressBarComponent 'ajaxProgressBarComponent' property value
     */
    public void setAjaxProgressBarComponent(com.softaspects.jsf.component.progressBar.ProgressBar ajaxProgressBarComponent) {
        setProperty("ajaxProgressBarComponent", ajaxProgressBarComponent);
    }

    /**
     * Getter for property 'ajaxProgressBarComponent'
     *
     * @return ajaxProgressBarComponent 'ajaxProgressBarComponent' property value
     */
    public com.softaspects.jsf.component.progressBar.ProgressBar getAjaxProgressBarComponent() {
        return (com.softaspects.jsf.component.progressBar.ProgressBar) getProperty("ajaxProgressBarComponent");
    }

    /**
     * Setter for property 'ajaxProgressBarInterfaceManager'
     *
     * @param ajaxProgressBarInterfaceManager 'ajaxProgressBarInterfaceManager' property value
     */
    public void setAjaxProgressBarInterfaceManager(com.softaspects.jsf.component.progressBar.ProgressBarInterfaceManager ajaxProgressBarInterfaceManager) {
        setProperty("ajaxProgressBarInterfaceManager", ajaxProgressBarInterfaceManager);
    }

    /**
     * Getter for property 'ajaxProgressBarInterfaceManager'
     *
     * @return ajaxProgressBarInterfaceManager 'ajaxProgressBarInterfaceManager' property value
     */
    public com.softaspects.jsf.component.progressBar.ProgressBarInterfaceManager getAjaxProgressBarInterfaceManager() {
        return (com.softaspects.jsf.component.progressBar.ProgressBarInterfaceManager) getProperty("ajaxProgressBarInterfaceManager");
    }

    /**
     * Setter for property 'ajaxProgressId'
     *
     * @param ajaxProgressId 'ajaxProgressId' property value
     */
    public void setAjaxProgressId(java.lang.String ajaxProgressId) {
        setProperty("ajaxProgressId", ajaxProgressId);
    }

    /**
     * Getter for property 'ajaxProgressId'
     *
     * @return ajaxProgressId 'ajaxProgressId' property value
     */
    public java.lang.String getAjaxProgressId() {
        return (java.lang.String) getProperty("ajaxProgressId");
    }

    /**
     * Setter for property 'ajaxProgressShow'
     *
     * @param ajaxProgressShow 'ajaxProgressShow' property value
     */
    public void setAjaxProgressShow(java.lang.Boolean ajaxProgressShow) {
        setProperty("ajaxProgressShow", ajaxProgressShow);
    }

    /**
     * Getter for property 'ajaxProgressShow'
     *
     * @return ajaxProgressShow 'ajaxProgressShow' property value
     */
    public java.lang.Boolean getAjaxProgressShow() {
        return (java.lang.Boolean) getProperty("ajaxProgressShow", Boolean.FALSE);
    }

    /**
     * Getter for property 'ajaxProgressShow'
     *
     * @return ajaxProgressShow 'ajaxProgressShow' property value
     */
    public java.lang.Boolean isAjaxProgressShow() {
        return (java.lang.Boolean) getProperty("ajaxProgressShow", Boolean.FALSE);
    }

    /**
     * Setter for property 'ajaxProgressTitle'
     *
     * @param ajaxProgressTitle 'ajaxProgressTitle' property value
     */
    public void setAjaxProgressTitle(java.lang.String ajaxProgressTitle) {
        setProperty("ajaxProgressTitle", ajaxProgressTitle);
    }

    /**
     * Getter for property 'ajaxProgressTitle'
     *
     * @return ajaxProgressTitle 'ajaxProgressTitle' property value
     */
    public java.lang.String getAjaxProgressTitle() {
        return (java.lang.String) getProperty("ajaxProgressTitle");
    }

    /**
     * Setter for property 'ajaxTargetElement'
     *
     * @param ajaxTargetElement 'ajaxTargetElement' property value
     */
    public void setAjaxTargetElement(java.lang.String ajaxTargetElement) {
        setProperty("ajaxTargetElement", ajaxTargetElement);
    }

    /**
     * Getter for property 'ajaxTargetElement'
     *
     * @return ajaxTargetElement 'ajaxTargetElement' property value
     */
    public java.lang.String getAjaxTargetElement() {
        return (java.lang.String) getProperty("ajaxTargetElement");
    }

    /**
     * Setter for property 'firstListId'
     *
     * @param firstListId 'firstListId' property value
     */
    public void setFirstListId(java.lang.String firstListId) {
        setProperty("firstListId", firstListId);
    }

    /**
     * Getter for property 'firstListId'
     *
     * @return firstListId 'firstListId' property value
     */
    public java.lang.String getFirstListId() {
        return (java.lang.String) getProperty("firstListId");
    }

    /**
     * Setter for property 'immediate'
     *
     * @param immediate 'immediate' property value
     */
    public void setImmediate(boolean immediate) {
        setProperty("immediate", immediate);
    }

    /**
     * Getter for property 'immediate'
     *
     * @return immediate 'immediate' property value
     */
    public boolean getImmediate() {
        return (boolean) getProperty("immediate", false);
    }

    /**
     * Getter for property 'immediate'
     *
     * @return immediate 'immediate' property value
     */
    public boolean isImmediate() {
        return (boolean) getProperty("immediate", false);
    }

    /**
     * Setter for property 'moveAllItemsFromFirstToSecondButtonId'
     *
     * @param moveAllItemsFromFirstToSecondButtonId 'moveAllItemsFromFirstToSecondButtonId' property value
     */
    public void setMoveAllItemsFromFirstToSecondButtonId(java.lang.String moveAllItemsFromFirstToSecondButtonId) {
        setProperty("moveAllItemsFromFirstToSecondButtonId", moveAllItemsFromFirstToSecondButtonId);
    }

    /**
     * Getter for property 'moveAllItemsFromFirstToSecondButtonId'
     *
     * @return moveAllItemsFromFirstToSecondButtonId 'moveAllItemsFromFirstToSecondButtonId' property value
     */
    public java.lang.String getMoveAllItemsFromFirstToSecondButtonId() {
        return (java.lang.String) getProperty("moveAllItemsFromFirstToSecondButtonId");
    }

    /**
     * Setter for property 'moveAllItemsFromSecondToFirstButtonId'
     *
     * @param moveAllItemsFromSecondToFirstButtonId 'moveAllItemsFromSecondToFirstButtonId' property value
     */
    public void setMoveAllItemsFromSecondToFirstButtonId(java.lang.String moveAllItemsFromSecondToFirstButtonId) {
        setProperty("moveAllItemsFromSecondToFirstButtonId", moveAllItemsFromSecondToFirstButtonId);
    }

    /**
     * Getter for property 'moveAllItemsFromSecondToFirstButtonId'
     *
     * @return moveAllItemsFromSecondToFirstButtonId 'moveAllItemsFromSecondToFirstButtonId' property value
     */
    public java.lang.String getMoveAllItemsFromSecondToFirstButtonId() {
        return (java.lang.String) getProperty("moveAllItemsFromSecondToFirstButtonId");
    }

    /**
     * Setter for property 'moveItemFromFirstToSecondButtonId'
     *
     * @param moveItemFromFirstToSecondButtonId 'moveItemFromFirstToSecondButtonId' property value
     */
    public void setMoveItemFromFirstToSecondButtonId(java.lang.String moveItemFromFirstToSecondButtonId) {
        setProperty("moveItemFromFirstToSecondButtonId", moveItemFromFirstToSecondButtonId);
    }

    /**
     * Getter for property 'moveItemFromFirstToSecondButtonId'
     *
     * @return moveItemFromFirstToSecondButtonId 'moveItemFromFirstToSecondButtonId' property value
     */
    public java.lang.String getMoveItemFromFirstToSecondButtonId() {
        return (java.lang.String) getProperty("moveItemFromFirstToSecondButtonId");
    }

    /**
     * Setter for property 'moveItemFromSecondToFirstButtonId'
     *
     * @param moveItemFromSecondToFirstButtonId 'moveItemFromSecondToFirstButtonId' property value
     */
    public void setMoveItemFromSecondToFirstButtonId(java.lang.String moveItemFromSecondToFirstButtonId) {
        setProperty("moveItemFromSecondToFirstButtonId", moveItemFromSecondToFirstButtonId);
    }

    /**
     * Getter for property 'moveItemFromSecondToFirstButtonId'
     *
     * @return moveItemFromSecondToFirstButtonId 'moveItemFromSecondToFirstButtonId' property value
     */
    public java.lang.String getMoveItemFromSecondToFirstButtonId() {
        return (java.lang.String) getProperty("moveItemFromSecondToFirstButtonId");
    }

    /**
     * Setter for property 'owner'
     *
     * @param owner 'owner' property value
     */
    public void setOwner(javax.faces.component.UIComponent owner) {
        setProperty("owner", owner);
    }

    /**
     * Getter for property 'owner'
     *
     * @return owner 'owner' property value
     */
    public javax.faces.component.UIComponent getOwner() {
        return (javax.faces.component.UIComponent) getProperty("owner");
    }

    /**
     * Setter for property 'secondListId'
     *
     * @param secondListId 'secondListId' property value
     */
    public void setSecondListId(java.lang.String secondListId) {
        setProperty("secondListId", secondListId);
    }

    /**
     * Getter for property 'secondListId'
     *
     * @return secondListId 'secondListId' property value
     */
    public java.lang.String getSecondListId() {
        return (java.lang.String) getProperty("secondListId");
    }

    /**
     * Setter for property 'selectionModel'
     *
     * @param selectionModel 'selectionModel' property value
     */
    public void setSelectionModel(java.lang.String selectionModel) {
        setProperty("selectionModel", selectionModel);
    }

    /**
     * Getter for property 'selectionModel'
     *
     * @return selectionModel 'selectionModel' property value
     */
    public java.lang.String getSelectionModel() {
        return (java.lang.String) getProperty("selectionModel");
    }

    /**
     * Setter for property 'style'
     *
     * @param style 'style' property value
     */
    public void setStyle(java.lang.String style) {
        setProperty("style", style);
    }

    /**
     * Getter for property 'style'
     *
     * @return style 'style' property value
     */
    public java.lang.String getStyle() {
        return (java.lang.String) getProperty("style");
    }

    /**
     * Setter for property 'styleClass'
     *
     * @param styleClass 'styleClass' property value
     */
    public void setStyleClass(java.lang.String styleClass) {
        setProperty("styleClass", styleClass);
    }

    /**
     * Getter for property 'styleClass'
     *
     * @return styleClass 'styleClass' property value
     */
    public java.lang.String getStyleClass() {
        return (java.lang.String) getProperty("styleClass");
    }

    /**
     * Setter for property 'title'
     *
     * @param title 'title' property value
     */
    public void setTitle(java.lang.String title) {
        setProperty("title", title);
    }

    /**
     * Getter for property 'title'
     *
     * @return title 'title' property value
     */
    public java.lang.String getTitle() {
        return (java.lang.String) getProperty("title");
    }

    /**
     * Setter for property 'useAjaxRefresh'
     *
     * @param useAjaxRefresh 'useAjaxRefresh' property value
     */
    public void setUseAjaxRefresh(java.lang.Boolean useAjaxRefresh) {
        setProperty("useAjaxRefresh", useAjaxRefresh);
    }

    /**
     * Getter for property 'useAjaxRefresh'
     *
     * @return useAjaxRefresh 'useAjaxRefresh' property value
     */
    public java.lang.Boolean getUseAjaxRefresh() {
        return (java.lang.Boolean) getProperty("useAjaxRefresh");
    }

    /**
     * Getter for property 'useAjaxRefresh'
     *
     * @return useAjaxRefresh 'useAjaxRefresh' property value
     */
    public java.lang.Boolean isUseAjaxRefresh() {
        return (java.lang.Boolean) getProperty("useAjaxRefresh");
    }

    /**
     * Setter for property 'valueExpression'
     *
     * @param valueExpression 'valueExpression' property value
     */
    public void setValueExpression(java.lang.String valueExpression) {
        setProperty("valueExpression", valueExpression);
    }

    /**
     * Getter for property 'valueExpression'
     *
     * @return valueExpression 'valueExpression' property value
     */
    public java.lang.String getValueExpression() {
        return (java.lang.String) getProperty("valueExpression");
    }

    /**
     * Setter for property 'varName'
     *
     * @param varName 'varName' property value
     */
    public void setVarName(java.lang.String varName) {
        setProperty("varName", varName);
    }

    /**
     * Getter for property 'varName'
     *
     * @return varName 'varName' property value
     */
    public java.lang.String getVarName() {
        return (java.lang.String) getProperty("varName");
    }
}
