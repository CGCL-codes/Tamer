package cw.customermanagementmodul.group.extention;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.jgoodies.binding.value.ValueModel;
import cw.boardingschoolmanagement.gui.component.CWPanel;
import cw.customermanagementmodul.customer.extention.point.CustomerSelectorFilterExtentionPoint;
import cw.customermanagementmodul.customer.persistence.Customer;
import cw.customermanagementmodul.group.gui.GroupCustomerSelectorFilterExtentionPresentationModel;
import cw.customermanagementmodul.group.gui.GroupCustomerSelectorFilterExtentionView;
import cw.customermanagementmodul.group.persistence.Group;
import cw.customermanagementmodul.group.persistence.PMGroup;

/**
 *
 * @author ManuelG
 */
public class GroupCustomerSelectorFilterExtention implements CustomerSelectorFilterExtentionPoint {

    private GroupCustomerSelectorFilterExtentionPresentationModel model;

    private GroupCustomerSelectorFilterExtentionView view;

    private ValueModel change;

    private EntityManager entityManager;

    private ListSelectionListener changeListener;

    public void init(final ValueModel change, EntityManager entityManager) {
        this.change = change;
        this.entityManager = entityManager;
        model = new GroupCustomerSelectorFilterExtentionPresentationModel(entityManager);
        view = new GroupCustomerSelectorFilterExtentionView(model);
    }

    public void initEventHandling() {
        model.getGroupSelectionModel().addListSelectionListener(changeListener = new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    change.setValue(true);
                }
            }
        });
    }

    public List<Customer> filter(List<Customer> costumers) {
        List<Group> groups = new ArrayList<Group>();
        if (model.getGroupSelectionModel().isSelectionEmpty() || model.getGroupSelectionModel().isSelectedIndex(0)) {
            return costumers;
        } else {
            int iMin = model.getGroupSelectionModel().getMinSelectionIndex();
            int iMax = model.getGroupSelectionModel().getMaxSelectionIndex();
            for (int i = iMin; i <= iMax; i++) {
                if (model.getGroupSelectionModel().isSelectedIndex(i)) {
                    groups.add((Group) model.getGroupSelection().getElementAt(i));
                }
            }
        }
        Iterator<Group> itGroups;
        Iterator<Customer> itCustomers = costumers.iterator();
        List<Customer> newCostumers = new ArrayList<Customer>();
        Customer customer;
        Group group;
        System.out.println("anz: " + costumers.size());
        while (itCustomers.hasNext()) {
            customer = itCustomers.next();
            itGroups = groups.iterator();
            boolean contains = false;
            while (itGroups.hasNext()) {
                group = itGroups.next();
                if (PMGroup.getInstance().getAllForCustomer(customer.getId(), entityManager).contains(group)) {
                    contains = true;
                    break;
                }
            }
            if (contains) {
                System.out.println("add: " + customer.getForename());
                newCostumers.add(customer);
            }
        }
        return newCostumers;
    }

    public CWPanel getView() {
        return view;
    }

    public void dispose() {
        view.dispose();
        changeListener = null;
    }

    public String getFilterName() {
        return "Gruppe(n)";
    }
}
