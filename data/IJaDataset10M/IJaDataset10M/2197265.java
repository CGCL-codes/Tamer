package com.softaspects.jsf.tag.tree;

import com.softaspects.jsf.tag.base.WGFComponentBaseTag;
import javax.el.ValueExpression;

/**
 * Tree Component tag
 */
public class BaseTreeTag extends WGFComponentBaseTag {

    public String getComponentType() {
        return "com.softaspects.jsf.component.tree.Tree";
    }

    public String getRendererType() {
        return "com.softaspects.jsf.renderer.tree.TreeRenderer";
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
     * Setter for property 'advancedSelection'
     *
     * @param advancedSelection 'advancedSelection' property value
     */
    public void setAdvancedSelection(ValueExpression advancedSelection) {
        setAttribute("advancedSelection", advancedSelection);
    }

    /**
     * Setter for property 'ajaxClientHandler'
     *
     * @param ajaxClientHandler 'ajaxClientHandler' property value
     */
    public void setAjaxClientHandler(ValueExpression ajaxClientHandler) {
        setAttribute("ajaxClientHandler", ajaxClientHandler);
    }

    /**
     * Setter for property 'ajaxEnabled'
     *
     * @param ajaxEnabled 'ajaxEnabled' property value
     */
    public void setAjaxEnabled(ValueExpression ajaxEnabled) {
        setAttribute("ajaxEnabled", ajaxEnabled);
    }

    /**
     * Setter for property 'ajaxListener'
     *
     * @param ajaxListener 'ajaxListener' property value
     */
    public void setAjaxListener(ValueExpression ajaxListener) {
        setAttribute("ajaxListener", ajaxListener);
    }

    /**
     * Setter for property 'ajaxProgressBarComponent'
     *
     * @param ajaxProgressBarComponent 'ajaxProgressBarComponent' property value
     */
    public void setAjaxProgressBarComponent(ValueExpression ajaxProgressBarComponent) {
        setAttribute("ajaxProgressBarComponent", ajaxProgressBarComponent);
    }

    /**
     * Setter for property 'ajaxProgressBarInterfaceManager'
     *
     * @param ajaxProgressBarInterfaceManager 'ajaxProgressBarInterfaceManager' property value
     */
    public void setAjaxProgressBarInterfaceManager(ValueExpression ajaxProgressBarInterfaceManager) {
        setAttribute("ajaxProgressBarInterfaceManager", ajaxProgressBarInterfaceManager);
    }

    /**
     * Setter for property 'ajaxProgressId'
     *
     * @param ajaxProgressId 'ajaxProgressId' property value
     */
    public void setAjaxProgressId(ValueExpression ajaxProgressId) {
        setAttribute("ajaxProgressId", ajaxProgressId);
    }

    /**
     * Setter for property 'ajaxProgressShow'
     *
     * @param ajaxProgressShow 'ajaxProgressShow' property value
     */
    public void setAjaxProgressShow(ValueExpression ajaxProgressShow) {
        setAttribute("ajaxProgressShow", ajaxProgressShow);
    }

    /**
     * Setter for property 'ajaxProgressTitle'
     *
     * @param ajaxProgressTitle 'ajaxProgressTitle' property value
     */
    public void setAjaxProgressTitle(ValueExpression ajaxProgressTitle) {
        setAttribute("ajaxProgressTitle", ajaxProgressTitle);
    }

    /**
     * Setter for property 'ajaxTargetElement'
     *
     * @param ajaxTargetElement 'ajaxTargetElement' property value
     */
    public void setAjaxTargetElement(ValueExpression ajaxTargetElement) {
        setAttribute("ajaxTargetElement", ajaxTargetElement);
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
     * Setter for property 'allNodesExpanded'
     *
     * @param allNodesExpanded 'allNodesExpanded' property value
     */
    public void setAllNodesExpanded(ValueExpression allNodesExpanded) {
        setAttribute("allNodesExpanded", allNodesExpanded);
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
     * Setter for property 'bgColorHilite'
     *
     * @param bgColorHilite 'bgColorHilite' property value
     */
    public void setBgColorHilite(ValueExpression bgColorHilite) {
        setAttribute("bgColorHilite", bgColorHilite);
    }

    /**
     * Setter for property 'blankImage'
     *
     * @param blankImage 'blankImage' property value
     */
    public void setBlankImage(ValueExpression blankImage) {
        setAttribute("blankImage", blankImage);
    }

    /**
     * Setter for property 'clientId'
     *
     * @param clientId 'clientId' property value
     */
    public void setClientId(ValueExpression clientId) {
        setAttribute("clientId", clientId);
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
     * Setter for property 'containerBg'
     *
     * @param containerBg 'containerBg' property value
     */
    public void setContainerBg(ValueExpression containerBg) {
        setAttribute("containerBg", containerBg);
    }

    /**
     * Setter for property 'dataModel'
     *
     * @param dataModel 'dataModel' property value
     */
    public void setDataModel(ValueExpression dataModel) {
        setAttribute("dataModel", dataModel);
    }

    /**
     * Setter for property 'dataModelName'
     *
     * @param dataModelName 'dataModelName' property value
     */
    public void setDataModelName(ValueExpression dataModelName) {
        setAttribute("dataModelName", dataModelName);
    }

    /**
     * Setter for property 'dblClickAction'
     *
     * @param dblClickAction 'dblClickAction' property value
     */
    public void setDblClickAction(ValueExpression dblClickAction) {
        setAttribute("dblClickAction", dblClickAction);
    }

    /**
     * Setter for property 'defaultTreeItemStyle'
     *
     * @param defaultTreeItemStyle 'defaultTreeItemStyle' property value
     */
    public void setDefaultTreeItemStyle(ValueExpression defaultTreeItemStyle) {
        setAttribute("defaultTreeItemStyle", defaultTreeItemStyle);
    }

    /**
     * Setter for property 'defaultTreeItemStyleClass'
     *
     * @param defaultTreeItemStyleClass 'defaultTreeItemStyleClass' property value
     */
    public void setDefaultTreeItemStyleClass(ValueExpression defaultTreeItemStyleClass) {
        setAttribute("defaultTreeItemStyleClass", defaultTreeItemStyleClass);
    }

    /**
     * Setter for property 'defaultTreeStyle'
     *
     * @param defaultTreeStyle 'defaultTreeStyle' property value
     */
    public void setDefaultTreeStyle(ValueExpression defaultTreeStyle) {
        setAttribute("defaultTreeStyle", defaultTreeStyle);
    }

    /**
     * Setter for property 'defaultTreeStyleClass'
     *
     * @param defaultTreeStyleClass 'defaultTreeStyleClass' property value
     */
    public void setDefaultTreeStyleClass(ValueExpression defaultTreeStyleClass) {
        setAttribute("defaultTreeStyleClass", defaultTreeStyleClass);
    }

    /**
     * Setter for property 'dnDStrategy'
     *
     * @param dnDStrategy 'dnDStrategy' property value
     */
    public void setDnDStrategy(ValueExpression dnDStrategy) {
        setAttribute("dnDStrategy", dnDStrategy);
    }

    /**
     * Setter for property 'dragEnabled'
     *
     * @param dragEnabled 'dragEnabled' property value
     */
    public void setDragEnabled(ValueExpression dragEnabled) {
        setAttribute("dragEnabled", dragEnabled);
    }

    /**
     * Setter for property 'dropEnabled'
     *
     * @param dropEnabled 'dropEnabled' property value
     */
    public void setDropEnabled(ValueExpression dropEnabled) {
        setAttribute("dropEnabled", dropEnabled);
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
     * Setter for property 'folderClosedLastNodeImage'
     *
     * @param folderClosedLastNodeImage 'folderClosedLastNodeImage' property value
     */
    public void setFolderClosedLastNodeImage(ValueExpression folderClosedLastNodeImage) {
        setAttribute("folderClosedLastNodeImage", folderClosedLastNodeImage);
    }

    /**
     * Setter for property 'folderClosedNodeImage'
     *
     * @param folderClosedNodeImage 'folderClosedNodeImage' property value
     */
    public void setFolderClosedNodeImage(ValueExpression folderClosedNodeImage) {
        setAttribute("folderClosedNodeImage", folderClosedNodeImage);
    }

    /**
     * Setter for property 'folderClosedTopNodeImage'
     *
     * @param folderClosedTopNodeImage 'folderClosedTopNodeImage' property value
     */
    public void setFolderClosedTopNodeImage(ValueExpression folderClosedTopNodeImage) {
        setAttribute("folderClosedTopNodeImage", folderClosedTopNodeImage);
    }

    /**
     * Setter for property 'folderOpenedLastNodeImage'
     *
     * @param folderOpenedLastNodeImage 'folderOpenedLastNodeImage' property value
     */
    public void setFolderOpenedLastNodeImage(ValueExpression folderOpenedLastNodeImage) {
        setAttribute("folderOpenedLastNodeImage", folderOpenedLastNodeImage);
    }

    /**
     * Setter for property 'folderOpenedNodeImage'
     *
     * @param folderOpenedNodeImage 'folderOpenedNodeImage' property value
     */
    public void setFolderOpenedNodeImage(ValueExpression folderOpenedNodeImage) {
        setAttribute("folderOpenedNodeImage", folderOpenedNodeImage);
    }

    /**
     * Setter for property 'folderOpenedTopNodeImage'
     *
     * @param folderOpenedTopNodeImage 'folderOpenedTopNodeImage' property value
     */
    public void setFolderOpenedTopNodeImage(ValueExpression folderOpenedTopNodeImage) {
        setAttribute("folderOpenedTopNodeImage", folderOpenedTopNodeImage);
    }

    /**
     * Setter for property 'fontColor'
     *
     * @param fontColor 'fontColor' property value
     */
    public void setFontColor(ValueExpression fontColor) {
        setAttribute("fontColor", fontColor);
    }

    /**
     * Setter for property 'fontColorHilite'
     *
     * @param fontColorHilite 'fontColorHilite' property value
     */
    public void setFontColorHilite(ValueExpression fontColorHilite) {
        setAttribute("fontColorHilite", fontColorHilite);
    }

    /**
     * Setter for property 'fontFamily'
     *
     * @param fontFamily 'fontFamily' property value
     */
    public void setFontFamily(ValueExpression fontFamily) {
        setAttribute("fontFamily", fontFamily);
    }

    /**
     * Setter for property 'fontSize'
     *
     * @param fontSize 'fontSize' property value
     */
    public void setFontSize(ValueExpression fontSize) {
        setAttribute("fontSize", fontSize);
    }

    /**
     * Setter for property 'fontStyle'
     *
     * @param fontStyle 'fontStyle' property value
     */
    public void setFontStyle(ValueExpression fontStyle) {
        setAttribute("fontStyle", fontStyle);
    }

    /**
     * Setter for property 'fontWeight'
     *
     * @param fontWeight 'fontWeight' property value
     */
    public void setFontWeight(ValueExpression fontWeight) {
        setAttribute("fontWeight", fontWeight);
    }

    /**
     * Setter for property 'footer'
     *
     * @param footer 'footer' property value
     */
    public void setFooter(ValueExpression footer) {
        setAttribute("footer", footer);
    }

    /**
     * Setter for property 'frameless'
     *
     * @param frameless 'frameless' property value
     */
    public void setFrameless(ValueExpression frameless) {
        setAttribute("frameless", frameless);
    }

    /**
     * Setter for property 'header'
     *
     * @param header 'header' property value
     */
    public void setHeader(ValueExpression header) {
        setAttribute("header", header);
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
     * Setter for property 'highlightTreeItemStyle'
     *
     * @param highlightTreeItemStyle 'highlightTreeItemStyle' property value
     */
    public void setHighlightTreeItemStyle(ValueExpression highlightTreeItemStyle) {
        setAttribute("highlightTreeItemStyle", highlightTreeItemStyle);
    }

    /**
     * Setter for property 'highlightTreeItemStyleClass'
     *
     * @param highlightTreeItemStyleClass 'highlightTreeItemStyleClass' property value
     */
    public void setHighlightTreeItemStyleClass(ValueExpression highlightTreeItemStyleClass) {
        setAttribute("highlightTreeItemStyleClass", highlightTreeItemStyleClass);
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
     * Setter for property 'imageHeight'
     *
     * @param imageHeight 'imageHeight' property value
     */
    public void setImageHeight(ValueExpression imageHeight) {
        setAttribute("imageHeight", imageHeight);
    }

    /**
     * Setter for property 'imageWidth'
     *
     * @param imageWidth 'imageWidth' property value
     */
    public void setImageWidth(ValueExpression imageWidth) {
        setAttribute("imageWidth", imageWidth);
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
     * Setter for property 'itemLastNodeImage'
     *
     * @param itemLastNodeImage 'itemLastNodeImage' property value
     */
    public void setItemLastNodeImage(ValueExpression itemLastNodeImage) {
        setAttribute("itemLastNodeImage", itemLastNodeImage);
    }

    /**
     * Setter for property 'itemNodeImage'
     *
     * @param itemNodeImage 'itemNodeImage' property value
     */
    public void setItemNodeImage(ValueExpression itemNodeImage) {
        setAttribute("itemNodeImage", itemNodeImage);
    }

    /**
     * Setter for property 'itemSelectOnRightClick'
     *
     * @param itemSelectOnRightClick 'itemSelectOnRightClick' property value
     */
    public void setItemSelectOnRightClick(ValueExpression itemSelectOnRightClick) {
        setAttribute("itemSelectOnRightClick", itemSelectOnRightClick);
    }

    /**
     * Setter for property 'itemTopNodeImage'
     *
     * @param itemTopNodeImage 'itemTopNodeImage' property value
     */
    public void setItemTopNodeImage(ValueExpression itemTopNodeImage) {
        setAttribute("itemTopNodeImage", itemTopNodeImage);
    }

    /**
     * Setter for property 'keyCollapse'
     *
     * @param keyCollapse 'keyCollapse' property value
     */
    public void setKeyCollapse(ValueExpression keyCollapse) {
        setAttribute("keyCollapse", keyCollapse);
    }

    /**
     * Setter for property 'keyCollapseAll'
     *
     * @param keyCollapseAll 'keyCollapseAll' property value
     */
    public void setKeyCollapseAll(ValueExpression keyCollapseAll) {
        setAttribute("keyCollapseAll", keyCollapseAll);
    }

    /**
     * Setter for property 'keyExpand'
     *
     * @param keyExpand 'keyExpand' property value
     */
    public void setKeyExpand(ValueExpression keyExpand) {
        setAttribute("keyExpand", keyExpand);
    }

    /**
     * Setter for property 'keyExpandAll'
     *
     * @param keyExpandAll 'keyExpandAll' property value
     */
    public void setKeyExpandAll(ValueExpression keyExpandAll) {
        setAttribute("keyExpandAll", keyExpandAll);
    }

    /**
     * Setter for property 'lazyLoadHideTimeout'
     *
     * @param lazyLoadHideTimeout 'lazyLoadHideTimeout' property value
     */
    public void setLazyLoadHideTimeout(ValueExpression lazyLoadHideTimeout) {
        setAttribute("lazyLoadHideTimeout", lazyLoadHideTimeout);
    }

    /**
     * Setter for property 'lazyLoadingListener'
     *
     * @param lazyLoadingListener 'lazyLoadingListener' property value
     */
    public void setLazyLoadingListener(ValueExpression lazyLoadingListener) {
        setAttribute("lazyLoadingListener", lazyLoadingListener);
    }

    /**
     * Setter for property 'lazyLoadLevel'
     *
     * @param lazyLoadLevel 'lazyLoadLevel' property value
     */
    public void setLazyLoadLevel(ValueExpression lazyLoadLevel) {
        setAttribute("lazyLoadLevel", lazyLoadLevel);
    }

    /**
     * Setter for property 'lazyLoadTimeout'
     *
     * @param lazyLoadTimeout 'lazyLoadTimeout' property value
     */
    public void setLazyLoadTimeout(ValueExpression lazyLoadTimeout) {
        setAttribute("lazyLoadTimeout", lazyLoadTimeout);
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
     * Setter for property 'metaModel'
     *
     * @param metaModel 'metaModel' property value
     */
    public void setMetaModel(ValueExpression metaModel) {
        setAttribute("metaModel", metaModel);
    }

    /**
     * Setter for property 'mixedStatusMode'
     *
     * @param mixedStatusMode 'mixedStatusMode' property value
     */
    public void setMixedStatusMode(ValueExpression mixedStatusMode) {
        setAttribute("mixedStatusMode", mixedStatusMode);
    }

    /**
     * Setter for property 'multipleOpenedBranches'
     *
     * @param multipleOpenedBranches 'multipleOpenedBranches' property value
     */
    public void setMultipleOpenedBranches(ValueExpression multipleOpenedBranches) {
        setAttribute("multipleOpenedBranches", multipleOpenedBranches);
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
     * Setter for property 'nodeImageHeight'
     *
     * @param nodeImageHeight 'nodeImageHeight' property value
     */
    public void setNodeImageHeight(ValueExpression nodeImageHeight) {
        setAttribute("nodeImageHeight", nodeImageHeight);
    }

    /**
     * Setter for property 'nodeImageWidth'
     *
     * @param nodeImageWidth 'nodeImageWidth' property value
     */
    public void setNodeImageWidth(ValueExpression nodeImageWidth) {
        setAttribute("nodeImageWidth", nodeImageWidth);
    }

    /**
     * Setter for property 'nodeStatusText'
     *
     * @param nodeStatusText 'nodeStatusText' property value
     */
    public void setNodeStatusText(ValueExpression nodeStatusText) {
        setAttribute("nodeStatusText", nodeStatusText);
    }

    /**
     * Setter for property 'nodesVisibility'
     *
     * @param nodesVisibility 'nodesVisibility' property value
     */
    public void setNodesVisibility(ValueExpression nodesVisibility) {
        setAttribute("nodesVisibility", nodesVisibility);
    }

    /**
     * Setter for property 'noTreeIcons'
     *
     * @param noTreeIcons 'noTreeIcons' property value
     */
    public void setNoTreeIcons(ValueExpression noTreeIcons) {
        setAttribute("noTreeIcons", noTreeIcons);
    }

    /**
     * Setter for property 'onDropListener'
     *
     * @param onDropListener 'onDropListener' property value
     */
    public void setOnDropListener(ValueExpression onDropListener) {
        setAttribute("onDropListener", onDropListener);
    }

    /**
     * Setter for property 'onMouseOverExpandCollapse'
     *
     * @param onMouseOverExpandCollapse 'onMouseOverExpandCollapse' property value
     */
    public void setOnMouseOverExpandCollapse(ValueExpression onMouseOverExpandCollapse) {
        setAttribute("onMouseOverExpandCollapse", onMouseOverExpandCollapse);
    }

    /**
     * Setter for property 'onMouseOverExpandCollapseTimeout'
     *
     * @param onMouseOverExpandCollapseTimeout 'onMouseOverExpandCollapseTimeout' property value
     */
    public void setOnMouseOverExpandCollapseTimeout(ValueExpression onMouseOverExpandCollapseTimeout) {
        setAttribute("onMouseOverExpandCollapseTimeout", onMouseOverExpandCollapseTimeout);
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
     * Setter for property 'rootNodeClosedImage'
     *
     * @param rootNodeClosedImage 'rootNodeClosedImage' property value
     */
    public void setRootNodeClosedImage(ValueExpression rootNodeClosedImage) {
        setAttribute("rootNodeClosedImage", rootNodeClosedImage);
    }

    /**
     * Setter for property 'rootNodeImagePresent'
     *
     * @param rootNodeImagePresent 'rootNodeImagePresent' property value
     */
    public void setRootNodeImagePresent(ValueExpression rootNodeImagePresent) {
        setAttribute("rootNodeImagePresent", rootNodeImagePresent);
    }

    /**
     * Setter for property 'rootNodeOpened'
     *
     * @param rootNodeOpened 'rootNodeOpened' property value
     */
    public void setRootNodeOpened(ValueExpression rootNodeOpened) {
        setAttribute("rootNodeOpened", rootNodeOpened);
    }

    /**
     * Setter for property 'rootNodeOpenedImage'
     *
     * @param rootNodeOpenedImage 'rootNodeOpenedImage' property value
     */
    public void setRootNodeOpenedImage(ValueExpression rootNodeOpenedImage) {
        setAttribute("rootNodeOpenedImage", rootNodeOpenedImage);
    }

    /**
     * Setter for property 'rootNodeVisible'
     *
     * @param rootNodeVisible 'rootNodeVisible' property value
     */
    public void setRootNodeVisible(ValueExpression rootNodeVisible) {
        setAttribute("rootNodeVisible", rootNodeVisible);
    }

    /**
     * Setter for property 'selectedTreeItemStyle'
     *
     * @param selectedTreeItemStyle 'selectedTreeItemStyle' property value
     */
    public void setSelectedTreeItemStyle(ValueExpression selectedTreeItemStyle) {
        setAttribute("selectedTreeItemStyle", selectedTreeItemStyle);
    }

    /**
     * Setter for property 'selectedTreeItemStyleClass'
     *
     * @param selectedTreeItemStyleClass 'selectedTreeItemStyleClass' property value
     */
    public void setSelectedTreeItemStyleClass(ValueExpression selectedTreeItemStyleClass) {
        setAttribute("selectedTreeItemStyleClass", selectedTreeItemStyleClass);
    }

    /**
     * Setter for property 'selectionModel'
     *
     * @param selectionModel 'selectionModel' property value
     */
    public void setSelectionModel(ValueExpression selectionModel) {
        setAttribute("selectionModel", selectionModel);
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
     * Setter for property 'serverSideOnAfterItemSelectTrigger'
     *
     * @param serverSideOnAfterItemSelectTrigger 'serverSideOnAfterItemSelectTrigger' property value
     */
    public void setServerSideOnAfterItemSelectTrigger(ValueExpression serverSideOnAfterItemSelectTrigger) {
        setAttribute("serverSideOnAfterItemSelectTrigger", serverSideOnAfterItemSelectTrigger);
    }

    /**
     * Setter for property 'serverSideOnBeforeItemSelectTrigger'
     *
     * @param serverSideOnBeforeItemSelectTrigger 'serverSideOnBeforeItemSelectTrigger' property value
     */
    public void setServerSideOnBeforeItemSelectTrigger(ValueExpression serverSideOnBeforeItemSelectTrigger) {
        setAttribute("serverSideOnBeforeItemSelectTrigger", serverSideOnBeforeItemSelectTrigger);
    }

    /**
     * Setter for property 'statusMode'
     *
     * @param statusMode 'statusMode' property value
     */
    public void setStatusMode(ValueExpression statusMode) {
        setAttribute("statusMode", statusMode);
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
     * Setter for property 'textWrap'
     *
     * @param textWrap 'textWrap' property value
     */
    public void setTextWrap(ValueExpression textWrap) {
        setAttribute("textWrap", textWrap);
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
     * Setter for property 'treeBranchClosedListener'
     *
     * @param treeBranchClosedListener 'treeBranchClosedListener' property value
     */
    public void setTreeBranchClosedListener(ValueExpression treeBranchClosedListener) {
        setAttribute("treeBranchClosedListener", treeBranchClosedListener);
    }

    /**
     * Setter for property 'treeBranchOpenedListener'
     *
     * @param treeBranchOpenedListener 'treeBranchOpenedListener' property value
     */
    public void setTreeBranchOpenedListener(ValueExpression treeBranchOpenedListener) {
        setAttribute("treeBranchOpenedListener", treeBranchOpenedListener);
    }

    /**
     * Setter for property 'treeBranchSelectedListener'
     *
     * @param treeBranchSelectedListener 'treeBranchSelectedListener' property value
     */
    public void setTreeBranchSelectedListener(ValueExpression treeBranchSelectedListener) {
        setAttribute("treeBranchSelectedListener", treeBranchSelectedListener);
    }

    /**
     * Setter for property 'treeHeight'
     *
     * @param treeHeight 'treeHeight' property value
     */
    public void setTreeHeight(ValueExpression treeHeight) {
        setAttribute("treeHeight", treeHeight);
    }

    /**
     * Setter for property 'treeInterfaceManager'
     *
     * @param treeInterfaceManager 'treeInterfaceManager' property value
     */
    public void setTreeInterfaceManager(ValueExpression treeInterfaceManager) {
        setAttribute("treeInterfaceManager", treeInterfaceManager);
    }

    /**
     * Setter for property 'treeLazyLoadingListener'
     *
     * @param treeLazyLoadingListener 'treeLazyLoadingListener' property value
     */
    public void setTreeLazyLoadingListener(ValueExpression treeLazyLoadingListener) {
        setAttribute("treeLazyLoadingListener", treeLazyLoadingListener);
    }

    /**
     * Setter for property 'treeLeafSelectedListener'
     *
     * @param treeLeafSelectedListener 'treeLeafSelectedListener' property value
     */
    public void setTreeLeafSelectedListener(ValueExpression treeLeafSelectedListener) {
        setAttribute("treeLeafSelectedListener", treeLeafSelectedListener);
    }

    /**
     * Setter for property 'useImageScaling'
     *
     * @param useImageScaling 'useImageScaling' property value
     */
    public void setUseImageScaling(ValueExpression useImageScaling) {
        setAttribute("useImageScaling", useImageScaling);
    }

    /**
     * Setter for property 'useLazyLoad'
     *
     * @param useLazyLoad 'useLazyLoad' property value
     */
    public void setUseLazyLoad(ValueExpression useLazyLoad) {
        setAttribute("useLazyLoad", useLazyLoad);
    }

    /**
     * Setter for property 'useMetaModel'
     *
     * @param useMetaModel 'useMetaModel' property value
     */
    public void setUseMetaModel(ValueExpression useMetaModel) {
        setAttribute("useMetaModel", useMetaModel);
    }

    /**
     * Setter for property 'userAddToFolder'
     *
     * @param userAddToFolder 'userAddToFolder' property value
     */
    public void setUserAddToFolder(ValueExpression userAddToFolder) {
        setAttribute("userAddToFolder", userAddToFolder);
    }

    /**
     * Setter for property 'userFunctionAfterLazyLoading'
     *
     * @param userFunctionAfterLazyLoading 'userFunctionAfterLazyLoading' property value
     */
    public void setUserFunctionAfterLazyLoading(ValueExpression userFunctionAfterLazyLoading) {
        setAttribute("userFunctionAfterLazyLoading", userFunctionAfterLazyLoading);
    }

    /**
     * Setter for property 'userFunctionOnDblClick'
     *
     * @param userFunctionOnDblClick 'userFunctionOnDblClick' property value
     */
    public void setUserFunctionOnDblClick(ValueExpression userFunctionOnDblClick) {
        setAttribute("userFunctionOnDblClick", userFunctionOnDblClick);
    }

    /**
     * Setter for property 'userFunctionOnDrop'
     *
     * @param userFunctionOnDrop 'userFunctionOnDrop' property value
     */
    public void setUserFunctionOnDrop(ValueExpression userFunctionOnDrop) {
        setAttribute("userFunctionOnDrop", userFunctionOnDrop);
    }

    /**
     * Setter for property 'userFunctionOnRightClick'
     *
     * @param userFunctionOnRightClick 'userFunctionOnRightClick' property value
     */
    public void setUserFunctionOnRightClick(ValueExpression userFunctionOnRightClick) {
        setAttribute("userFunctionOnRightClick", userFunctionOnRightClick);
    }

    /**
     * Setter for property 'userOnClearSelection'
     *
     * @param userOnClearSelection 'userOnClearSelection' property value
     */
    public void setUserOnClearSelection(ValueExpression userOnClearSelection) {
        setAttribute("userOnClearSelection", userOnClearSelection);
    }

    /**
     * Setter for property 'userOnContextMenu'
     *
     * @param userOnContextMenu 'userOnContextMenu' property value
     */
    public void setUserOnContextMenu(ValueExpression userOnContextMenu) {
        setAttribute("userOnContextMenu", userOnContextMenu);
    }

    /**
     * Setter for property 'userOnItemSelect'
     *
     * @param userOnItemSelect 'userOnItemSelect' property value
     */
    public void setUserOnItemSelect(ValueExpression userOnItemSelect) {
        setAttribute("userOnItemSelect", userOnItemSelect);
    }

    /**
     * Setter for property 'useServerSideLazyLoad'
     *
     * @param useServerSideLazyLoad 'useServerSideLazyLoad' property value
     */
    public void setUseServerSideLazyLoad(ValueExpression useServerSideLazyLoad) {
        setAttribute("useServerSideLazyLoad", useServerSideLazyLoad);
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
     * Setter for property 'valueExpression'
     *
     * @param valueExpression 'valueExpression' property value
     */
    public void setValueExpression(ValueExpression valueExpression) {
        setAttribute("valueExpression", valueExpression);
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
     * Setter for property 'verticalLineImage'
     *
     * @param verticalLineImage 'verticalLineImage' property value
     */
    public void setVerticalLineImage(ValueExpression verticalLineImage) {
        setAttribute("verticalLineImage", verticalLineImage);
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
     * Setter for property 'whenLazyLoadingText'
     *
     * @param whenLazyLoadingText 'whenLazyLoadingText' property value
     */
    public void setWhenLazyLoadingText(ValueExpression whenLazyLoadingText) {
        setAttribute("whenLazyLoadingText", whenLazyLoadingText);
    }

    /**
     * Setter for property 'whenLoadingText'
     *
     * @param whenLoadingText 'whenLoadingText' property value
     */
    public void setWhenLoadingText(ValueExpression whenLoadingText) {
        setAttribute("whenLoadingText", whenLoadingText);
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
