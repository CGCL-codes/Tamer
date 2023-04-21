package com.anthonyeden.lib.util;

import javax.swing.ListModel;
import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/** A wrapper for a list model.

    @author Anthony Eden
*/
public class ListMap extends AbstractListModel implements ListDataListener {

    /** The wrapped ListMode. */
    protected ListModel model;

    /** Get the model.
    
        @return The ListModel
    */
    public ListModel getModel() {
        return model;
    }

    /** Set the model.
    
        @param model The list model
    */
    public void setModel(ListModel model) {
        this.model = model;
        model.addListDataListener(this);
    }

    /** Get the element at the given index.
    
        @param index The index
        @return The element
    */
    public Object getElementAt(int index) {
        return model.getElementAt(index);
    }

    /** Get the list model size.
    
        @return The size
    */
    public int getSize() {
        return model.getSize();
    }

    /** This method can be called to signal that an interval specified
        in the given event object has been added.
        
        @param evt The ListDataEvent object
    */
    public void intervalAdded(ListDataEvent evt) {
        fireIntervalAdded(evt.getSource(), evt.getIndex0(), evt.getIndex1());
    }

    /** This method can be called to signal that an interval specified
        in the given event object has been removed.
        
        @param evt The ListDataEvent object
    */
    public void intervalRemoved(ListDataEvent evt) {
        fireIntervalRemoved(evt.getSource(), evt.getIndex0(), evt.getIndex1());
    }

    /** This method can be called to signal that an interval specified
        in the given event object has been changed.
        
        @param evt The ListDataEvent object
    */
    public void contentsChanged(ListDataEvent evt) {
        fireIntervalRemoved(evt.getSource(), evt.getIndex0(), evt.getIndex1());
    }
}
