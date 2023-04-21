package mekhangar.design.gui.models;

import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import mekhangar.design.MechDesign;
import mekhangar.design.MechDesignListener;
import mekhangar.design.gui.MechDesigner;

public class TechBaseModel extends AbstractListModel implements ComboBoxModel, MechDesignListener {

    private MechDesigner designer;

    private String[] list;

    public TechBaseModel(MechDesigner designer, String[] list) {
        this.designer = designer;
        this.list = list;
    }

    public void setSelectedItem(Object anItem) {
        for (int i = 0; i < list.length; i++) {
            if (anItem.equals(list[i])) {
                designer.techBaseChanged(i);
            }
        }
    }

    public Object getSelectedItem() {
        return list[getDesign().getTechBase()];
    }

    public int getSize() {
        return getAvailable().size();
    }

    public Object getElementAt(int index) {
        int pos = ((Integer) getAvailable().get(index)).intValue();
        return list[pos];
    }

    public void designChanged(String change) {
        if (change.equals("techBase.value")) {
            fireContentsChanged(this, 0, list.length - 1);
        }
        if (change.equals("techBase.choices")) {
            fireContentsChanged(this, 0, list.length - 1);
        }
    }

    private List getAvailable() {
        return getDesign().getAvailableTechBase();
    }

    private MechDesign getDesign() {
        return designer.getDesign();
    }
}
