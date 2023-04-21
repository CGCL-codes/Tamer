package org.plazmaforge.framework.client.swing.gui;

import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * @author Oleh Hapon
 *
 */
public class XDefaultComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private List data;

    private Object selectedObject;

    public XDefaultComboBoxModel() {
        super();
    }

    public XDefaultComboBoxModel(List data) {
        this.data = data;
    }

    public int getSize() {
        return data == null ? 0 : data.size();
    }

    public Object getElementAt(int i) {
        return data.get(i);
    }

    public Object getSelectedItem() {
        return selectedObject;
    }

    public void setSelectedItem(Object item) {
        selectedObject = item;
    }

    protected List getData() {
        return data;
    }

    public void initData(List data) {
        this.data = data;
        this.fireContentsChanged(this, 0, data.size());
    }

    public void clearData() {
        this.data = null;
    }
}
