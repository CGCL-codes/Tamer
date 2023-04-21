package com.quickrss.view.swing;

/**
 * @version 1.10 11/17/05
 * @author Scott Violet
 */
public class DynamicTreeNodeData extends Object {

    private int itemCountLoaded;

    private Object dataObject;

    /**
	 * Constructs a new instance of SampleData with the passed in arguments.
	 */
    public DynamicTreeNodeData(Object dataObject) {
        this.dataObject = dataObject;
    }

    public Object getDataObject() {
        return dataObject;
    }

    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }

    public int getItemCountLoaded() {
        return itemCountLoaded;
    }

    public void setItemCountLoaded(int itemCountLoaded) {
        this.itemCountLoaded = itemCountLoaded;
    }

    public String toString() {
        return dataObject.toString();
    }
}
