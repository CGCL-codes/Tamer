package com.beanview.swing.component;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.IllegalFormatConversionException;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import com.beanview.MultipleSelectComponent;
import com.beanview.model.ConvertingSelectionModel;

/**
 * A Swing component, useful for representing collections.
 * 
 * @author $Author: wiverson $
 * @version $Revision: 1.1.1.1 $, $Date: 2006/09/19 04:21:39 $
 */
public class SettableJList extends JScrollPane implements MultipleSelectComponent {

    private static final long serialVersionUID = -3921775129440642578L;

    private JList list = new JList();

    private ConvertingSelectionModel selectionModel;

    public SettableJList() {
        this.getViewport().setView(list);
    }

    public void setMultipleSelectOptions(ConvertingSelectionModel manyToManyValues) {
        this.list.setModel(manyToManyValues);
    }

    public ConvertingSelectionModel getMultipleSelectOptions() {
        return (ConvertingSelectionModel) this.list.getModel();
    }

    public Object getValue() {
        if (this.list.getSelectedValues() == null) return null;
        if (this.list.getSelectedValues().length == 0) return null;
        ConvertingSelectionModel converter = (ConvertingSelectionModel) this.list.getModel();
        return converter.returnSelection(this.list.getSelectedValues());
    }

    public void setValue(Object in) throws IllegalFormatConversionException {
        ListSelectionModel model = this.list.getSelectionModel();
        if (in == null) {
            model.clearSelection();
            return;
        }
        if (in.getClass().isArray()) {
            Object objectArray = Array.newInstance(in.getClass().getComponentType(), Array.getLength(in));
            for (int i = 0; i < Array.getLength(objectArray); i++) {
                Array.set(objectArray, i, Array.get(in, i));
            }
            ListModel available = this.list.getModel();
            model.clearSelection();
            for (int i = 0; i < available.getSize(); i++) {
                for (int ii = 0; ii < Array.getLength(objectArray); ii++) {
                    if (available.getElementAt(i).equals(Array.get(objectArray, ii))) model.addSelectionInterval(i, i);
                }
            }
        }
        if (in instanceof Collection) {
            Collection valuesToSet = (Collection) in;
            model.clearSelection();
            ListModel available = this.list.getModel();
            for (int i = 0; i < available.getSize(); i++) {
                for (Object currentSetValue : valuesToSet) {
                    if (currentSetValue.equals(available.getElementAt(i))) model.addSelectionInterval(i, i);
                }
            }
        }
    }

    public JList getList() {
        return list;
    }

    public void setList(JList list) {
        this.list = list;
    }

    public ConvertingSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public void setSelectionModel(ConvertingSelectionModel selectionModel) {
        this.selectionModel = selectionModel;
    }
}
