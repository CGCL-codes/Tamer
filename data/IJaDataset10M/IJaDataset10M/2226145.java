package home.jes.ui.jswing;

import javax.swing.*;
import java.util.HashMap;

public class JSComboBoxModel extends DefaultComboBoxModel {

    public JSComboBoxModel() {
        super();
        renderer = new JSListCellRenderer();
    }

    public ListCellRenderer getListCellRenderer() {
        return renderer;
    }

    public void setId(String id) {
        this.id = id;
        instances.put(id, this);
    }

    public void addListItem(JSListItem element) {
        super.addElement(element);
        if (element.isSelected()) {
            setSelectedItem(element);
        }
    }

    public static JSComboBoxModel findInstance(String id) {
        return (JSComboBoxModel) instances.get(id);
    }

    ListCellRenderer renderer = null;

    String id;

    static HashMap instances = new HashMap();
}
