package com.softaspects.jsf.component.tabbedPanel;

import com.softaspects.jsf.component.base.WGFComponentBase;

/**
 * TabbedPanel Component
 */
public class BaseTabbedPanel extends WGFComponentBase implements com.softaspects.jsf.component.base.ajax.AjaxSupportedComponent {

    public static final String COMPONENT_TYPE = "com.softaspects.jsf.component.tabbedPanel.TabbedPanel";

    public static final String RENDERER_TYPE = "com.softaspects.jsf.renderer.tabbedPanel.TabbedPanelRenderer";

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
        return new Class[] { TabbedPanelData.class, TabbedPanelSelection.class, Tab.class };
    }

    /**
     * Setter for property 'accessKey'
     *
     * @param accessKey 'accessKey' property value
     */
    public void setAccessKey(java.lang.String accessKey) {
        setProperty("accessKey", accessKey);
    }

    /**
     * Getter for property 'accessKey'
     *
     * @return accessKey 'accessKey' property value
     */
    public java.lang.String getAccessKey() {
        return (java.lang.String) getProperty("accessKey");
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
     * Setter for property 'align'
     *
     * @param align 'align' property value
     */
    public void setAlign(java.lang.String align) {
        setProperty("align", align);
    }

    /**
     * Getter for property 'align'
     *
     * @return align 'align' property value
     */
    public java.lang.String getAlign() {
        return (java.lang.String) getProperty("align");
    }

    /**
     * Setter for property 'centerActiveImage'
     *
     * @param centerActiveImage 'centerActiveImage' property value
     */
    public void setCenterActiveImage(java.lang.String centerActiveImage) {
        setProperty("centerActiveImage", centerActiveImage);
    }

    /**
     * Getter for property 'centerActiveImage'
     *
     * @return centerActiveImage 'centerActiveImage' property value
     */
    public java.lang.String getCenterActiveImage() {
        return (java.lang.String) getProperty("centerActiveImage");
    }

    /**
     * Setter for property 'centerInactiveImage'
     *
     * @param centerInactiveImage 'centerInactiveImage' property value
     */
    public void setCenterInactiveImage(java.lang.String centerInactiveImage) {
        setProperty("centerInactiveImage", centerInactiveImage);
    }

    /**
     * Getter for property 'centerInactiveImage'
     *
     * @return centerInactiveImage 'centerInactiveImage' property value
     */
    public java.lang.String getCenterInactiveImage() {
        return (java.lang.String) getProperty("centerInactiveImage");
    }

    /**
     * Setter for property 'constraint'
     *
     * @param constraint 'constraint' property value
     */
    public void setConstraint(com.softaspects.jsf.support.components.Constraint constraint) {
        setProperty("constraint", constraint);
    }

    /**
     * Getter for property 'constraint'
     *
     * @return constraint 'constraint' property value
     */
    public com.softaspects.jsf.support.components.Constraint getConstraint() {
        return (com.softaspects.jsf.support.components.Constraint) getProperty("constraint");
    }

    /**
     * Setter for property 'dataModelWasChangedFromRequest'
     *
     * @param dataModelWasChangedFromRequest 'dataModelWasChangedFromRequest' property value
     */
    public void setDataModelWasChangedFromRequest(java.util.Map dataModelWasChangedFromRequest) {
        setProperty("dataModelWasChangedFromRequest", dataModelWasChangedFromRequest);
    }

    /**
     * Getter for property 'dataModelWasChangedFromRequest'
     *
     * @return dataModelWasChangedFromRequest 'dataModelWasChangedFromRequest' property value
     */
    public java.util.Map getDataModelWasChangedFromRequest() {
        return (java.util.Map) getProperty("dataModelWasChangedFromRequest");
    }

    /**
     * Setter for property 'enabled'
     *
     * @param enabled 'enabled' property value
     */
    public void setEnabled(java.lang.Boolean enabled) {
        setProperty("enabled", enabled);
    }

    /**
     * Getter for property 'enabled'
     *
     * @return enabled 'enabled' property value
     */
    public java.lang.Boolean getEnabled() {
        return (java.lang.Boolean) getProperty("enabled", Boolean.TRUE);
    }

    /**
     * Getter for property 'enabled'
     *
     * @return enabled 'enabled' property value
     */
    public java.lang.Boolean isEnabled() {
        return (java.lang.Boolean) getProperty("enabled", Boolean.TRUE);
    }

    /**
     * Setter for property 'fixedItemWidth'
     *
     * @param fixedItemWidth 'fixedItemWidth' property value
     */
    public void setFixedItemWidth(java.lang.String fixedItemWidth) {
        setProperty("fixedItemWidth", fixedItemWidth);
    }

    /**
     * Getter for property 'fixedItemWidth'
     *
     * @return fixedItemWidth 'fixedItemWidth' property value
     */
    public java.lang.String getFixedItemWidth() {
        return (java.lang.String) getProperty("fixedItemWidth");
    }

    /**
     * Setter for property 'focused'
     *
     * @param focused 'focused' property value
     */
    public void setFocused(java.lang.String focused) {
        setProperty("focused", focused);
    }

    /**
     * Getter for property 'focused'
     *
     * @return focused 'focused' property value
     */
    public java.lang.String getFocused() {
        return (java.lang.String) getProperty("focused");
    }

    /**
     * Setter for property 'height'
     *
     * @param height 'height' property value
     */
    public void setHeight(java.lang.String height) {
        setProperty("height", height);
    }

    /**
     * Getter for property 'height'
     *
     * @return height 'height' property value
     */
    public java.lang.String getHeight() {
        return (java.lang.String) getProperty("height");
    }

    /**
     * Setter for property 'hintText'
     *
     * @param hintText 'hintText' property value
     */
    public void setHintText(java.lang.String hintText) {
        setProperty("hintText", hintText);
    }

    /**
     * Getter for property 'hintText'
     *
     * @return hintText 'hintText' property value
     */
    public java.lang.String getHintText() {
        return (java.lang.String) getProperty("hintText");
    }

    /**
     * Setter for property 'imagesHeight'
     *
     * @param imagesHeight 'imagesHeight' property value
     */
    public void setImagesHeight(java.lang.String imagesHeight) {
        setProperty("imagesHeight", imagesHeight);
    }

    /**
     * Getter for property 'imagesHeight'
     *
     * @return imagesHeight 'imagesHeight' property value
     */
    public java.lang.String getImagesHeight() {
        return (java.lang.String) getProperty("imagesHeight");
    }

    /**
     * Setter for property 'imagesWidth'
     *
     * @param imagesWidth 'imagesWidth' property value
     */
    public void setImagesWidth(java.lang.String imagesWidth) {
        setProperty("imagesWidth", imagesWidth);
    }

    /**
     * Getter for property 'imagesWidth'
     *
     * @return imagesWidth 'imagesWidth' property value
     */
    public java.lang.String getImagesWidth() {
        return (java.lang.String) getProperty("imagesWidth");
    }

    /**
     * Setter for property 'imageType'
     *
     * @param imageType 'imageType' property value
     */
    public void setImageType(java.lang.String imageType) {
        setProperty("imageType", imageType);
    }

    /**
     * Getter for property 'imageType'
     *
     * @return imageType 'imageType' property value
     */
    public java.lang.String getImageType() {
        return (java.lang.String) getProperty("imageType");
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
     * Setter for property 'kindOfImagesUsedTabComponent'
     *
     * @param kindOfImagesUsedTabComponent 'kindOfImagesUsedTabComponent' property value
     */
    public void setKindOfImagesUsedTabComponent(java.lang.String kindOfImagesUsedTabComponent) {
        setProperty("kindOfImagesUsedTabComponent", kindOfImagesUsedTabComponent);
    }

    /**
     * Getter for property 'kindOfImagesUsedTabComponent'
     *
     * @return kindOfImagesUsedTabComponent 'kindOfImagesUsedTabComponent' property value
     */
    public java.lang.String getKindOfImagesUsedTabComponent() {
        return (java.lang.String) getProperty("kindOfImagesUsedTabComponent", "BIG");
    }

    /**
     * Setter for property 'left'
     *
     * @param left 'left' property value
     */
    public void setLeft(java.lang.String left) {
        setProperty("left", left);
    }

    /**
     * Getter for property 'left'
     *
     * @return left 'left' property value
     */
    public java.lang.String getLeft() {
        return (java.lang.String) getProperty("left");
    }

    /**
     * Setter for property 'leftActiveImage'
     *
     * @param leftActiveImage 'leftActiveImage' property value
     */
    public void setLeftActiveImage(java.lang.String leftActiveImage) {
        setProperty("leftActiveImage", leftActiveImage);
    }

    /**
     * Getter for property 'leftActiveImage'
     *
     * @return leftActiveImage 'leftActiveImage' property value
     */
    public java.lang.String getLeftActiveImage() {
        return (java.lang.String) getProperty("leftActiveImage");
    }

    /**
     * Setter for property 'leftInactiveImage'
     *
     * @param leftInactiveImage 'leftInactiveImage' property value
     */
    public void setLeftInactiveImage(java.lang.String leftInactiveImage) {
        setProperty("leftInactiveImage", leftInactiveImage);
    }

    /**
     * Getter for property 'leftInactiveImage'
     *
     * @return leftInactiveImage 'leftInactiveImage' property value
     */
    public java.lang.String getLeftInactiveImage() {
        return (java.lang.String) getProperty("leftInactiveImage");
    }

    /**
     * Setter for property 'listDataModel'
     *
     * @param listDataModel 'listDataModel' property value
     */
    public void setListDataModel(com.softaspects.jsf.component.tabbedPanel.TabbedPanelDataModel listDataModel) {
        setProperty("listDataModel", listDataModel);
    }

    /**
     * Getter for property 'listDataModel'
     *
     * @return listDataModel 'listDataModel' property value
     */
    public com.softaspects.jsf.component.tabbedPanel.TabbedPanelDataModel getListDataModel() {
        return (com.softaspects.jsf.component.tabbedPanel.TabbedPanelDataModel) getProperty("listDataModel");
    }

    /**
     * Setter for property 'listSelectionModel'
     *
     * @param listSelectionModel 'listSelectionModel' property value
     */
    public void setListSelectionModel(com.softaspects.jsf.component.tabbedPanel.TabbedPanelSelectionModel listSelectionModel) {
        setProperty("listSelectionModel", listSelectionModel);
    }

    /**
     * Getter for property 'listSelectionModel'
     *
     * @return listSelectionModel 'listSelectionModel' property value
     */
    public com.softaspects.jsf.component.tabbedPanel.TabbedPanelSelectionModel getListSelectionModel() {
        return (com.softaspects.jsf.component.tabbedPanel.TabbedPanelSelectionModel) getProperty("listSelectionModel");
    }

    /**
     * Setter for property 'name'
     *
     * @param name 'name' property value
     */
    public void setName(java.lang.String name) {
        setProperty("name", name);
    }

    /**
     * Getter for property 'name'
     *
     * @return name 'name' property value
     */
    public java.lang.String getName() {
        return (java.lang.String) getProperty("name");
    }

    /**
     * Setter for property 'orientation'
     *
     * @param orientation 'orientation' property value
     */
    public void setOrientation(java.lang.String orientation) {
        setProperty("orientation", orientation);
    }

    /**
     * Getter for property 'orientation'
     *
     * @return orientation 'orientation' property value
     */
    public java.lang.String getOrientation() {
        return (java.lang.String) getProperty("orientation", "horizontal");
    }

    /**
     * Setter for property 'required'
     *
     * @param required 'required' property value
     */
    public void setRequired(java.lang.Boolean required) {
        setProperty("required", required);
    }

    /**
     * Getter for property 'required'
     *
     * @return required 'required' property value
     */
    public java.lang.Boolean getRequired() {
        return (java.lang.Boolean) getProperty("required");
    }

    /**
     * Getter for property 'required'
     *
     * @return required 'required' property value
     */
    public java.lang.Boolean isRequired() {
        return (java.lang.Boolean) getProperty("required");
    }

    /**
     * Setter for property 'rightActiveImage'
     *
     * @param rightActiveImage 'rightActiveImage' property value
     */
    public void setRightActiveImage(java.lang.String rightActiveImage) {
        setProperty("rightActiveImage", rightActiveImage);
    }

    /**
     * Getter for property 'rightActiveImage'
     *
     * @return rightActiveImage 'rightActiveImage' property value
     */
    public java.lang.String getRightActiveImage() {
        return (java.lang.String) getProperty("rightActiveImage");
    }

    /**
     * Setter for property 'rightInactiveImage'
     *
     * @param rightInactiveImage 'rightInactiveImage' property value
     */
    public void setRightInactiveImage(java.lang.String rightInactiveImage) {
        setProperty("rightInactiveImage", rightInactiveImage);
    }

    /**
     * Getter for property 'rightInactiveImage'
     *
     * @return rightInactiveImage 'rightInactiveImage' property value
     */
    public java.lang.String getRightInactiveImage() {
        return (java.lang.String) getProperty("rightInactiveImage");
    }

    /**
     * Setter for property 'selectionModelWasChangedFromRequest'
     *
     * @param selectionModelWasChangedFromRequest 'selectionModelWasChangedFromRequest' property value
     */
    public void setSelectionModelWasChangedFromRequest(java.util.Map selectionModelWasChangedFromRequest) {
        setProperty("selectionModelWasChangedFromRequest", selectionModelWasChangedFromRequest);
    }

    /**
     * Getter for property 'selectionModelWasChangedFromRequest'
     *
     * @return selectionModelWasChangedFromRequest 'selectionModelWasChangedFromRequest' property value
     */
    public java.util.Map getSelectionModelWasChangedFromRequest() {
        return (java.util.Map) getProperty("selectionModelWasChangedFromRequest");
    }

    /**
     * Setter for property 'serverSideAction'
     *
     * @param serverSideAction 'serverSideAction' property value
     */
    public void setServerSideAction(java.lang.Boolean serverSideAction) {
        setProperty("serverSideAction", serverSideAction);
    }

    /**
     * Getter for property 'serverSideAction'
     *
     * @return serverSideAction 'serverSideAction' property value
     */
    public java.lang.Boolean getServerSideAction() {
        return (java.lang.Boolean) getProperty("serverSideAction", Boolean.TRUE);
    }

    /**
     * Getter for property 'serverSideAction'
     *
     * @return serverSideAction 'serverSideAction' property value
     */
    public java.lang.Boolean isServerSideAction() {
        return (java.lang.Boolean) getProperty("serverSideAction", Boolean.TRUE);
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
     * Setter for property 'tabComponent'
     *
     * @param tabComponent 'tabComponent' property value
     */
    public void setTabComponent(com.softaspects.jsf.component.tabbedPanel.Tab tabComponent) {
        setProperty("tabComponent", tabComponent);
    }

    /**
     * Getter for property 'tabComponent'
     *
     * @return tabComponent 'tabComponent' property value
     */
    public com.softaspects.jsf.component.tabbedPanel.Tab getTabComponent() {
        return (com.softaspects.jsf.component.tabbedPanel.Tab) getProperty("tabComponent");
    }

    /**
     * Setter for property 'tabComponentActiveStyle'
     *
     * @param tabComponentActiveStyle 'tabComponentActiveStyle' property value
     */
    public void setTabComponentActiveStyle(java.lang.String tabComponentActiveStyle) {
        setProperty("tabComponentActiveStyle", tabComponentActiveStyle);
    }

    /**
     * Getter for property 'tabComponentActiveStyle'
     *
     * @return tabComponentActiveStyle 'tabComponentActiveStyle' property value
     */
    public java.lang.String getTabComponentActiveStyle() {
        return (java.lang.String) getProperty("tabComponentActiveStyle");
    }

    /**
     * Setter for property 'tabComponentActiveStyleClass'
     *
     * @param tabComponentActiveStyleClass 'tabComponentActiveStyleClass' property value
     */
    public void setTabComponentActiveStyleClass(java.lang.String tabComponentActiveStyleClass) {
        setProperty("tabComponentActiveStyleClass", tabComponentActiveStyleClass);
    }

    /**
     * Getter for property 'tabComponentActiveStyleClass'
     *
     * @return tabComponentActiveStyleClass 'tabComponentActiveStyleClass' property value
     */
    public java.lang.String getTabComponentActiveStyleClass() {
        return (java.lang.String) getProperty("tabComponentActiveStyleClass");
    }

    /**
     * Setter for property 'tabComponentHLightStyle'
     *
     * @param tabComponentHLightStyle 'tabComponentHLightStyle' property value
     */
    public void setTabComponentHLightStyle(java.lang.String tabComponentHLightStyle) {
        setProperty("tabComponentHLightStyle", tabComponentHLightStyle);
    }

    /**
     * Getter for property 'tabComponentHLightStyle'
     *
     * @return tabComponentHLightStyle 'tabComponentHLightStyle' property value
     */
    public java.lang.String getTabComponentHLightStyle() {
        return (java.lang.String) getProperty("tabComponentHLightStyle");
    }

    /**
     * Setter for property 'tabComponentHLightStyleClass'
     *
     * @param tabComponentHLightStyleClass 'tabComponentHLightStyleClass' property value
     */
    public void setTabComponentHLightStyleClass(java.lang.String tabComponentHLightStyleClass) {
        setProperty("tabComponentHLightStyleClass", tabComponentHLightStyleClass);
    }

    /**
     * Getter for property 'tabComponentHLightStyleClass'
     *
     * @return tabComponentHLightStyleClass 'tabComponentHLightStyleClass' property value
     */
    public java.lang.String getTabComponentHLightStyleClass() {
        return (java.lang.String) getProperty("tabComponentHLightStyleClass");
    }

    /**
     * Setter for property 'tabComponentImageHeight'
     *
     * @param tabComponentImageHeight 'tabComponentImageHeight' property value
     */
    public void setTabComponentImageHeight(java.lang.String tabComponentImageHeight) {
        setProperty("tabComponentImageHeight", tabComponentImageHeight);
    }

    /**
     * Getter for property 'tabComponentImageHeight'
     *
     * @return tabComponentImageHeight 'tabComponentImageHeight' property value
     */
    public java.lang.String getTabComponentImageHeight() {
        return (java.lang.String) getProperty("tabComponentImageHeight");
    }

    /**
     * Setter for property 'tabComponentImageWidth'
     *
     * @param tabComponentImageWidth 'tabComponentImageWidth' property value
     */
    public void setTabComponentImageWidth(java.lang.String tabComponentImageWidth) {
        setProperty("tabComponentImageWidth", tabComponentImageWidth);
    }

    /**
     * Getter for property 'tabComponentImageWidth'
     *
     * @return tabComponentImageWidth 'tabComponentImageWidth' property value
     */
    public java.lang.String getTabComponentImageWidth() {
        return (java.lang.String) getProperty("tabComponentImageWidth");
    }

    /**
     * Setter for property 'tabComponentSmallImageHeight'
     *
     * @param tabComponentSmallImageHeight 'tabComponentSmallImageHeight' property value
     */
    public void setTabComponentSmallImageHeight(java.lang.String tabComponentSmallImageHeight) {
        setProperty("tabComponentSmallImageHeight", tabComponentSmallImageHeight);
    }

    /**
     * Getter for property 'tabComponentSmallImageHeight'
     *
     * @return tabComponentSmallImageHeight 'tabComponentSmallImageHeight' property value
     */
    public java.lang.String getTabComponentSmallImageHeight() {
        return (java.lang.String) getProperty("tabComponentSmallImageHeight");
    }

    /**
     * Setter for property 'tabComponentSmallImageWidth'
     *
     * @param tabComponentSmallImageWidth 'tabComponentSmallImageWidth' property value
     */
    public void setTabComponentSmallImageWidth(java.lang.String tabComponentSmallImageWidth) {
        setProperty("tabComponentSmallImageWidth", tabComponentSmallImageWidth);
    }

    /**
     * Getter for property 'tabComponentSmallImageWidth'
     *
     * @return tabComponentSmallImageWidth 'tabComponentSmallImageWidth' property value
     */
    public java.lang.String getTabComponentSmallImageWidth() {
        return (java.lang.String) getProperty("tabComponentSmallImageWidth");
    }

    /**
     * Setter for property 'tabComponentStyle'
     *
     * @param tabComponentStyle 'tabComponentStyle' property value
     */
    public void setTabComponentStyle(java.lang.String tabComponentStyle) {
        setProperty("tabComponentStyle", tabComponentStyle);
    }

    /**
     * Getter for property 'tabComponentStyle'
     *
     * @return tabComponentStyle 'tabComponentStyle' property value
     */
    public java.lang.String getTabComponentStyle() {
        return (java.lang.String) getProperty("tabComponentStyle");
    }

    /**
     * Setter for property 'tabComponentStyleClass'
     *
     * @param tabComponentStyleClass 'tabComponentStyleClass' property value
     */
    public void setTabComponentStyleClass(java.lang.String tabComponentStyleClass) {
        setProperty("tabComponentStyleClass", tabComponentStyleClass);
    }

    /**
     * Getter for property 'tabComponentStyleClass'
     *
     * @return tabComponentStyleClass 'tabComponentStyleClass' property value
     */
    public java.lang.String getTabComponentStyleClass() {
        return (java.lang.String) getProperty("tabComponentStyleClass");
    }

    /**
     * Setter for property 'tabIndex'
     *
     * @param tabIndex 'tabIndex' property value
     */
    public void setTabIndex(java.lang.Integer tabIndex) {
        setProperty("tabIndex", tabIndex);
    }

    /**
     * Getter for property 'tabIndex'
     *
     * @return tabIndex 'tabIndex' property value
     */
    public java.lang.Integer getTabIndex() {
        return (java.lang.Integer) getProperty("tabIndex");
    }

    /**
     * Setter for property 'tabSelectedListener'
     *
     * @param tabSelectedListener 'tabSelectedListener' property value
     */
    public void setTabSelectedListener(com.softaspects.jsf.component.tabbedPanel.listener.TabSelectedListener tabSelectedListener) {
        setProperty("tabSelectedListener", tabSelectedListener);
    }

    /**
     * Getter for property 'tabSelectedListener'
     *
     * @return tabSelectedListener 'tabSelectedListener' property value
     */
    public com.softaspects.jsf.component.tabbedPanel.listener.TabSelectedListener getTabSelectedListener() {
        return (com.softaspects.jsf.component.tabbedPanel.listener.TabSelectedListener) getProperty("tabSelectedListener");
    }

    /**
     * Setter for property 'tabCloseListener'
     *
     * @param tabCloseListener 'tabCloseListener' property value
     */
    public void setTabCloseListener(com.softaspects.jsf.component.tabbedPanel.listener.TabCloseListener tabCloseListener) {
        setProperty("tabCloseListener", tabCloseListener);
    }

    /**
     * Getter for property 'tabCloseListener'
     *
     * @return tabCloseListener 'tabCloseListener' property value
     */
    public com.softaspects.jsf.component.tabbedPanel.listener.TabCloseListener getTabCloseListener() {
        return (com.softaspects.jsf.component.tabbedPanel.listener.TabCloseListener) getProperty("tabCloseListener");
    }

    /**
     * Setter for property 'textOrientation'
     *
     * @param textOrientation 'textOrientation' property value
     */
    public void setTextOrientation(java.lang.String textOrientation) {
        setProperty("textOrientation", textOrientation);
    }

    /**
     * Getter for property 'textOrientation'
     *
     * @return textOrientation 'textOrientation' property value
     */
    public java.lang.String getTextOrientation() {
        return (java.lang.String) getProperty("textOrientation", "horizontal");
    }

    /**
     * Setter for property 'textWidth'
     *
     * @param textWidth 'textWidth' property value
     */
    public void setTextWidth(java.lang.String textWidth) {
        setProperty("textWidth", textWidth);
    }

    /**
     * Getter for property 'textWidth'
     *
     * @return textWidth 'textWidth' property value
     */
    public java.lang.String getTextWidth() {
        return (java.lang.String) getProperty("textWidth");
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
     * Setter for property 'top'
     *
     * @param top 'top' property value
     */
    public void setTop(java.lang.String top) {
        setProperty("top", top);
    }

    /**
     * Getter for property 'top'
     *
     * @return top 'top' property value
     */
    public java.lang.String getTop() {
        return (java.lang.String) getProperty("top");
    }

    /**
     * Setter for property 'userOnItemSelect'
     *
     * @param userOnItemSelect 'userOnItemSelect' property value
     */
    public void setUserOnItemSelect(java.lang.String userOnItemSelect) {
        setProperty("userOnItemSelect", userOnItemSelect);
    }

    /**
     * Getter for property 'userOnItemSelect'
     *
     * @return userOnItemSelect 'userOnItemSelect' property value
     */
    public java.lang.String getUserOnItemSelect() {
        return (java.lang.String) getProperty("userOnItemSelect");
    }

    /**
     * Setter for property 'valign'
     *
     * @param valign 'valign' property value
     */
    public void setValign(java.lang.String valign) {
        setProperty("valign", valign);
    }

    /**
     * Getter for property 'valign'
     *
     * @return valign 'valign' property value
     */
    public java.lang.String getValign() {
        return (java.lang.String) getProperty("valign");
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

    /**
     * Setter for property 'visible'
     *
     * @param visible 'visible' property value
     */
    public void setVisible(java.lang.Boolean visible) {
        setProperty("visible", visible);
    }

    /**
     * Getter for property 'visible'
     *
     * @return visible 'visible' property value
     */
    public java.lang.Boolean getVisible() {
        return (java.lang.Boolean) getProperty("visible", Boolean.TRUE);
    }

    /**
     * Getter for property 'visible'
     *
     * @return visible 'visible' property value
     */
    public java.lang.Boolean isVisible() {
        return (java.lang.Boolean) getProperty("visible", Boolean.TRUE);
    }

    /**
     * Setter for property 'width'
     *
     * @param width 'width' property value
     */
    public void setWidth(java.lang.String width) {
        setProperty("width", width);
    }

    /**
     * Getter for property 'width'
     *
     * @return width 'width' property value
     */
    public java.lang.String getWidth() {
        return (java.lang.String) getProperty("width");
    }

    /**
     * Setter for property 'wrap'
     *
     * @param wrap 'wrap' property value
     */
    public void setWrap(java.lang.Boolean wrap) {
        setProperty("wrap", wrap);
    }

    /**
     * Getter for property 'wrap'
     *
     * @return wrap 'wrap' property value
     */
    public java.lang.Boolean getWrap() {
        return (java.lang.Boolean) getProperty("wrap", Boolean.FALSE);
    }

    /**
     * Getter for property 'wrap'
     *
     * @return wrap 'wrap' property value
     */
    public java.lang.Boolean isWrap() {
        return (java.lang.Boolean) getProperty("wrap", Boolean.FALSE);
    }

    /**
     * Setter for property 'zindex'
     *
     * @param zindex 'zindex' property value
     */
    public void setZindex(java.lang.Integer zindex) {
        setProperty("zindex", zindex);
    }

    /**
     * Getter for property 'zindex'
     *
     * @return zindex 'zindex' property value
     */
    public java.lang.Integer getZindex() {
        return (java.lang.Integer) getProperty("zindex");
    }
}
