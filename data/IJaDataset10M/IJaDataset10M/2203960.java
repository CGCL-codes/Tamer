package org.jabusuite.address.customer.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jabusuite.address.Address;
import org.jabusuite.address.customer.Customer;
import org.jabusuite.address.session.AddressesBean;
import org.jabusuite.core.companies.JbsCompany;
import org.jabusuite.core.users.JbsUser;
import org.jabusuite.core.users.session.JbsUsersLocal;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.core.utils.session.UserNumbersLocal;
import org.jabusuite.logging.Logger;

/**
 * Management-Bean for customers
 * @author hilwers
 */
@Stateless
public class CustomersBean extends AddressesBean implements CustomersRemote {

    @PersistenceContext(unitName = "jabusuite")
    private EntityManager manager;

    @Resource
    SessionContext context;

    @EJB
    JbsUsersLocal userManagement;

    @EJB
    UserNumbersLocal userNumberManagement;

    private Logger logger = Logger.getLogger(CustomersBean.class);

    public void createDataset(Customer customer, JbsUser user, JbsCompany company) {
        this.userNumberManagement.setNextUserNumber(Customer.class, customer.getUserNo(), company);
        if (logger.isDebugEnabled()) {
            logger.debug("Saving customer " + customer.getName1());
        }
        this.createDataset(manager, customer, user, user, user.getMainGroup(), company);
    }

    public void updateDataset(Customer customer, JbsUser changeUser) throws EJbsObject {
        Customer existingCustomer = manager.find(Customer.class, customer.getId());
        if (logger.isDebugEnabled()) {
            logger.debug("Letter-Count: " + existingCustomer.getLetters().size());
            logger.debug("Deleting letters that no longer exist.");
        }
        this.deleteOldAddressLetters(manager, customer, existingCustomer.getLetters());
        this.updateDataset(manager, customer, changeUser);
    }

    public Customer findDataset(long id) {
        return this.findDataset(id, true);
    }

    public Customer findDataset(long id, boolean withAdditionalData) {
        logger.debug("Searching for customer " + id);
        Customer customer = manager.find(Customer.class, id);
        if ((customer != null) && (withAdditionalData)) {
            int letterCount = customer.getLetters().size();
            logger.debug("Letters: " + letterCount);
        }
        return customer;
    }

    @Override
    public long getDatasetCount(String filter, JbsUser user, JbsCompany company) {
        return super.getDatasetCount(manager, Customer.class, filter, user, company);
    }

    @Override
    public List getDatasets(String filter, String orderFields, JbsUser user, JbsCompany company, int firstResult, int resultCount) {
        return super.getDatasets(manager, Customer.class, filter, orderFields, user, company, firstResult, resultCount);
    }

    public void deleteDataset(Customer customer, JbsUser changeUser) throws EJbsObject {
        customer.setDeleted(true);
        this.updateDataset(customer, changeUser);
    }

    public void addCustomers(ArrayList<Customer> customers, JbsCompany company) throws EJbsObject {
        Iterator<Customer> it = customers.iterator();
        JbsUser rootUser = userManagement.findUser("root");
        while (it.hasNext()) {
            Customer customer = it.next();
            Customer exCustomer = this.findDataset(customer.getId());
            if (exCustomer != null) {
                logger.debug("Changing customer " + customer.getId());
                customer.setOwner(exCustomer.getOwner());
                customer.setGroup(exCustomer.getGroup());
                customer.setWriteGroup(exCustomer.isWriteGroup());
                customer.setWriteUser(exCustomer.isWriteUser());
                customer.setWriteOther(exCustomer.isWriteOther());
                customer.setReadGroup(exCustomer.isReadGroup());
                customer.setReadUser(exCustomer.isReadUser());
                customer.setReadOther(exCustomer.isReadOther());
                this.updateDataset(customer, rootUser);
            } else {
                logger.debug("Adding new customer " + customer.getId());
                this.createDataset(customer, rootUser, company);
            }
        }
    }
}
