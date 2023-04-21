package com.certesystems.swingforms.gridset.combobox;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import com.certesystems.swingforms.grid.Grid;

public class GridComboBoxModel implements ComboBoxModel {

    private Grid grid;

    public GridComboBoxModel(Grid grid) {
        this.grid = grid;
    }

    public Grid getSelectedItem() {
        return grid;
    }

    public void setSelectedItem(Object arg0) {
        grid = (Grid) arg0;
    }

    public void addListDataListener(ListDataListener arg0) {
    }

    public Grid getElementAt(int pos) {
        return pos == 0 ? grid : null;
    }

    public int getSize() {
        return 1;
    }

    public void removeListDataListener(ListDataListener arg0) {
    }
}
