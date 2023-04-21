package de.mogwai.kias.example.customer;

import org.apache.log4j.Logger;
import de.mogwai.kias.CommandList;
import de.mogwai.kias.example.NavigatableController;
import de.mogwai.kias.example.bo.ContactType;
import de.mogwai.kias.example.bo.Customer;
import de.mogwai.kias.example.bo.CustomerContact;
import de.mogwai.kias.forms.FormContext;
import de.mogwai.kias.forms.definition.Forward;

public class CustomerController extends NavigatableController<Customer, CustomerFormBean> {

    private Logger logger = Logger.getLogger(CustomerController.class.getName());

    public CommandList getActionCommands(FormContext aContext) {
        CommandList theResult = new CommandList(aContext);
        theResult.addResource("BASEDATA", "openBaseData");
        theResult.addResource("CONTACTHISTORY", "openContactHistory");
        return theResult;
    }

    public void onForward(FormContext aContext, Customer aCustomer) {
        if (aCustomer.getId() != null) persistenceService.attachToSession(aCustomer);
        getBean(aContext).setData(aCustomer);
        enrichData(aContext);
    }

    public Forward openBaseData(FormContext aContext) {
        return aContext.getForm().getForward("basedata").withParams(new Object[] { getBean(aContext).getData() });
    }

    public Forward openContactHistory(FormContext aContext) {
        return aContext.getForm().getForward("contacthistory").withParams(new Object[] { getBean(aContext).getData() });
    }

    public Forward addContactClicked(FormContext aContext) {
        CustomerFormBean theForm = getBean(aContext);
        CustomerContact theKontakt = new CustomerContact();
        theKontakt.setType(theForm.getType());
        theKontakt.setValue(theForm.getValue());
        theForm.getData().getContacts().add(theKontakt);
        theForm.setType(null);
        theForm.setValue("");
        return aContext.getForm().getForward(SELF_FORWARD);
    }

    public Forward deleteContactClicked(FormContext aContext, CustomerContact aContact) {
        CustomerFormBean theForm = getBean(aContext);
        theForm.getData().getContacts().remove(aContact);
        return aContext.getForm().getForward(SELF_FORWARD);
    }

    public Forward searchClicked(FormContext aContext) {
        String[] searchProperties = new String[] { "name1", "name2", "company", "street", "country", "plz", "city", "comments" };
        String[] orderProperties = new String[] { "name1", "name2" };
        return aContext.getForm().getForward("search").withParams(new Object[] { getBean(aContext).getData(), searchProperties, orderProperties });
    }

    @Override
    protected Customer createNew() {
        return new Customer();
    }

    @Override
    protected Class getObjectClass() {
        return Customer.class;
    }

    @Override
    public void onForward(FormContext aContext) {
        CustomerFormBean theBean = getBean(aContext);
        theBean.getContactTypes().clear();
        theBean.getContactTypes().addAll(persistenceService.findAll(ContactType.class));
    }

    @Override
    public void onFirstInit(FormContext aContext) {
        super.onFirstInit(aContext);
        CustomerFormBean theBean = getBean(aContext);
        theBean.getContactTypes().clear();
        theBean.getContactTypes().addAll(persistenceService.findAll(ContactType.class));
    }
}
