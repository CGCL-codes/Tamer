package com.pragprog.dhnako.carserv.wicket.ui;

import org.apache.wicket.markup.html.basic.Label;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import com.pragprog.dhnako.carserv.dom.customer.Customer;

public class EditCustomerPanel2 extends NakedObjectsPanel {

    private static final long serialVersionUID = 1L;

    public EditCustomerPanel2(final String id, final Customer customer) {
        super(id);
        addCustomerNameLabel(customer);
        addForm(customer);
    }

    private void addCustomerNameLabel(final Customer customer) {
        add(new Label("customerName", customer.title()));
    }

    private void addForm(final Customer customer) {
        NakedObject customerAdapter = getAdapterManager().adapterFor(customer);
        EditCustomerForm form = new EditCustomerForm("editCustomerForm", customerAdapter.getOid());
        add(form);
        form.init();
    }

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }
}
