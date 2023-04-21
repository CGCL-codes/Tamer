package cw.coursemanagementmodul.gui.model;

import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import com.jgoodies.binding.list.SelectionInList;
import cw.customermanagementmodul.persistence.model.CustomerModel;

/**
 *
 * @author André Salmhofer
 */
public class CostumerComboBoxModel implements ComboBoxModel {

    private CustomerModel selectedItem;

    private List<CustomerModel> customerList;

    public CostumerComboBoxModel(List<CustomerModel> customerList) {
        this.customerList = customerList;
    }

    public void setSelectedItem(Object anItem) {
        selectedItem = (CustomerModel) anItem;
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public int getSize() {
        return customerList.size();
    }

    public Object getElementAt(int index) {
        return customerList.get(index);
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }

    public SelectionInList<CustomerModel> getSelectionInList() {
        return new SelectionInList(customerList);
    }
}
