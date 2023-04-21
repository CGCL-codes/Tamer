package ui.view.swing.component.search;

import javax.swing.JTextField;
import message.MessageId;
import query.criteria.SupplierSearchCriteria;
import ui.view.swing.SwingUI;

public class SuppliersSearchPanel extends StandardSearchPanel implements SupplierSearchCriteria {

    private JTextField nameField;

    public SuppliersSearchPanel() {
        initComponents();
    }

    private void initComponents() {
        nameField = new JTextField();
        this.filtersPanel().add(SwingUI.instance().decorated(nameField, MessageId.supplierName));
    }

    public String getSupplierName() {
        return nameField.getText();
    }
}
