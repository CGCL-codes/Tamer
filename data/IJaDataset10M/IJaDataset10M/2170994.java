package com.softaspects.jsf.tag.tree;

import com.softaspects.jsf.tag.base.WGFComponentBaseTag;
import javax.el.ValueExpression;

/**
 * TreeItem Component tag
 */
public class BaseTreeItemTag extends WGFComponentBaseTag {

    public String getComponentType() {
        return "com.softaspects.jsf.component.tree.TreeItem";
    }

    public String getRendererType() {
        return "com.softaspects.jsf.renderer.tree.TreeItemRenderer";
    }

    /**
     * Setter for property 'assosiatedObject'
     *
     * @param assosiatedObject 'assosiatedObject' property value
     */
    public void setAssosiatedObject(ValueExpression assosiatedObject) {
        setAttribute("assosiatedObject", assosiatedObject);
    }

    /**
     * Setter for property 'containedList'
     *
     * @param containedList 'containedList' property value
     */
    public void setContainedList(ValueExpression containedList) {
        setAttribute("containedList", containedList);
    }

    /**
     * Setter for property 'data'
     *
     * @param data 'data' property value
     */
    public void setData(ValueExpression data) {
        setAttribute("data", data);
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
     * Setter for property 'description'
     *
     * @param description 'description' property value
     */
    public void setDescription(ValueExpression description) {
        setAttribute("description", description);
    }

    /**
     * Setter for property 'element'
     *
     * @param element 'element' property value
     */
    public void setElement(ValueExpression element) {
        setAttribute("element", element);
    }

    /**
     * Setter for property 'eventSource'
     *
     * @param eventSource 'eventSource' property value
     */
    public void setEventSource(ValueExpression eventSource) {
        setAttribute("eventSource", eventSource);
    }

    /**
     * Setter for property 'folderClosedImage'
     *
     * @param folderClosedImage 'folderClosedImage' property value
     */
    public void setFolderClosedImage(ValueExpression folderClosedImage) {
        setAttribute("folderClosedImage", folderClosedImage);
    }

    /**
     * Setter for property 'folderClosedRollOverImage'
     *
     * @param folderClosedRollOverImage 'folderClosedRollOverImage' property value
     */
    public void setFolderClosedRollOverImage(ValueExpression folderClosedRollOverImage) {
        setAttribute("folderClosedRollOverImage", folderClosedRollOverImage);
    }

    /**
     * Setter for property 'folderOpenedImage'
     *
     * @param folderOpenedImage 'folderOpenedImage' property value
     */
    public void setFolderOpenedImage(ValueExpression folderOpenedImage) {
        setAttribute("folderOpenedImage", folderOpenedImage);
    }

    /**
     * Setter for property 'folderOpenedRollOverImage'
     *
     * @param folderOpenedRollOverImage 'folderOpenedRollOverImage' property value
     */
    public void setFolderOpenedRollOverImage(ValueExpression folderOpenedRollOverImage) {
        setAttribute("folderOpenedRollOverImage", folderOpenedRollOverImage);
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
     * Setter for property 'immediate'
     *
     * @param immediate 'immediate' property value
     */
    public void setImmediate(ValueExpression immediate) {
        setAttribute("immediate", immediate);
    }

    /**
     * Setter for property 'itemImage'
     *
     * @param itemImage 'itemImage' property value
     */
    public void setItemImage(ValueExpression itemImage) {
        setAttribute("itemImage", itemImage);
    }

    /**
     * Setter for property 'itemRollOverImage'
     *
     * @param itemRollOverImage 'itemRollOverImage' property value
     */
    public void setItemRollOverImage(ValueExpression itemRollOverImage) {
        setAttribute("itemRollOverImage", itemRollOverImage);
    }

    /**
     * Setter for property 'itemStatusText'
     *
     * @param itemStatusText 'itemStatusText' property value
     */
    public void setItemStatusText(ValueExpression itemStatusText) {
        setAttribute("itemStatusText", itemStatusText);
    }

    /**
     * Setter for property 'lock'
     *
     * @param lock 'lock' property value
     */
    public void setLock(ValueExpression lock) {
        setAttribute("lock", lock);
    }

    /**
     * Setter for property 'navigationAction'
     *
     * @param navigationAction 'navigationAction' property value
     */
    public void setNavigationAction(ValueExpression navigationAction) {
        setAttribute("navigationAction", navigationAction);
    }

    /**
     * Setter for property 'opened'
     *
     * @param opened 'opened' property value
     */
    public void setOpened(ValueExpression opened) {
        setAttribute("opened", opened);
    }

    /**
     * Setter for property 'parentEntity'
     *
     * @param parentEntity 'parentEntity' property value
     */
    public void setParentEntity(ValueExpression parentEntity) {
        setAttribute("parentEntity", parentEntity);
    }

    /**
     * Setter for property 'scrambled'
     *
     * @param scrambled 'scrambled' property value
     */
    public void setScrambled(ValueExpression scrambled) {
        setAttribute("scrambled", scrambled);
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
     * Setter for property 'type'
     *
     * @param type 'type' property value
     */
    public void setType(ValueExpression type) {
        setAttribute("type", type);
    }
}
