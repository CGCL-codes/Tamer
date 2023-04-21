package org.cmsuite2.business.form;

import java.util.ArrayList;
import java.util.List;
import org.cmsuite2.model.customer.Customer;

public class MailingListForm {

    private List<Customer> customersAv = new ArrayList<Customer>();

    public List<Customer> getCustomersAv() {
        return customersAv;
    }

    public void setCustomersAv(List<Customer> customersAv) {
        this.customersAv = customersAv;
    }
}
