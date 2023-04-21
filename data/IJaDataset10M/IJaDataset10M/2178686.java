package com.certesystems.swingforms.components;

import java.util.Iterator;
import java.util.List;
import javax.swing.JComboBox;
import com.certesystems.swingforms.fields.VirtualForeignKey;

public class GridSetComboBox extends JComboBox {

    private static final long serialVersionUID = 6903502321289365787L;

    private List<VirtualForeignKey> gridSetList;

    public GridSetComboBox(List<VirtualForeignKey> oElements) {
        super(new GridSetComboBoxModel(oElements));
        gridSetList = oElements;
        GridSetComboBoxRenderer renderer = new GridSetComboBoxRenderer();
        setRenderer(renderer);
    }

    public GridSetComboBoxModel getModel() {
        return (GridSetComboBoxModel) super.getModel();
    }

    public Object getSelectedKey() {
        VirtualForeignKey vfk = getModel().getSelectedItem();
        if (vfk == null) {
            return null;
        } else {
            return vfk.getKey();
        }
    }

    public void setSelectedKey(Object oKey) {
        Iterator<VirtualForeignKey> it = gridSetList.iterator();
        while (it.hasNext()) {
            VirtualForeignKey value = it.next();
            if (value.getKey().equals(oKey)) {
                getModel().setSelectedItem(value);
                return;
            }
        }
        getModel().setSelectedItem(null);
    }
}
