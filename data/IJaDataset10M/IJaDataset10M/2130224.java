package com.ivis.xprocess.ui.dialogs;

/**
 * Classes that handle the request for creation of xelements from the
 * ElementManagementDialog need to implement this interface and register
 * themselves with the Dialog.
 *
 */
public interface ICreate {

    /**
     * Create a new Xelement, and return the list with this new Xelement in
     *
     * @return
     */
    public Object create();

    /**
     * @return the selected elements
     */
    public Object[] getSelectedElements();

    /**
     * Add the object. The implementation determine how and what
     * to add and to what.
     *
     * @param object
     */
    public void addElement(Object object);
}
