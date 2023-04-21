package org.cmsuite2.business.form;

import java.util.ArrayList;
import java.util.List;
import org.cmsuite2.model.customer.Customer;
import org.cmsuite2.model.mailing.MailingList;

public class NewsLetterForm {

    private List<Customer> customersAv = new ArrayList<Customer>();

    private List<MailingList> mailingLists = new ArrayList<MailingList>();

    public List<Customer> getCustomersAv() {
        return customersAv;
    }

    public void setCustomersAv(List<Customer> customersAv) {
        this.customersAv = customersAv;
    }

    public List<MailingList> getMailingLists() {
        return mailingLists;
    }

    public void setMailingLists(List<MailingList> mailingLists) {
        this.mailingLists = mailingLists;
    }
}
