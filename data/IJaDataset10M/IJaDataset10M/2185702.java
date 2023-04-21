package com.completex.objective.persistency.examples.ex004a.app;

import com.completex.objective.components.log.Log;
import com.completex.objective.components.log.adapter.StdErrorLogAdapter;
import com.completex.objective.components.persistency.core.adapter.DefaultMappingPersistencyAdapter;
import com.completex.objective.components.persistency.core.adapter.DefaultPersistencyAdapter;
import com.completex.objective.components.persistency.transact.Transaction;
import com.completex.objective.components.persistency.transact.TransactionManager;
import com.completex.objective.persistency.examples.ex004a.GenDescriptors;
import com.completex.objective.persistency.examples.ex004a.GenObjects;
import com.completex.objective.persistency.examples.ex004a.domain.BeanToPoMapper;
import com.completex.objective.persistency.examples.ex004a.domain.Contact;
import com.completex.objective.persistency.examples.ex004a.domain.CpxCustomer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gennady Krizhevsky
 */
public class BusinessApp {

    public static final Log logger = StdErrorLogAdapter.newLogInstance();

    private TransactionManager transactionManager;

    public static final Long QUANTITY_ONE = new Long(1);

    String[][] orgNames = new String[][] { { "Washington Police", "washington.police.com" }, { "Beck Taxi", "beck.taxi.com" }, { "Airport Taxi", "airport.taxi.com" } };

    int nameCounter;

    CpxCustomer[] customers;

    Contact[] contacts;

    private CustomerDAO customerDAO;

    public BusinessApp(String configPath) throws IOException {
        init(configPath);
    }

    void init(String configPath) throws IOException {
        DefaultPersistencyAdapter persistency = new DefaultPersistencyAdapter(configPath);
        DefaultMappingPersistencyAdapter mappingPersistency = new DefaultMappingPersistencyAdapter(persistency, new BeanToPoMapper(), Log.NULL_LOGGER);
        transactionManager = persistency.getTransactionManager();
        customerDAO = new CustomerDAO(mappingPersistency);
    }

    CpxCustomer createCustomer(String orgName, String url) {
        CpxCustomer customer = new CpxCustomer();
        customer.setOrgName(orgName);
        customer.setUrl(url);
        return customer;
    }

    Contact createContact() {
        nameCounter++;
        Contact contact = new Contact();
        contact.setFirstName("FirstName" + nameCounter);
        contact.setLastName("LastName" + nameCounter);
        contact.setPhone("1-800-111-1111");
        contact.setShipAddress("475 LENFANT PLZ SW RM 10022 WASHINGTON DC 20260-00" + nameCounter);
        return contact;
    }

    public static Log getLogger() {
        return BusinessApp.logger;
    }

    void createAllCustomers() throws CustomerException {
        info("Enter BusinessApp::createAllCustomers");
        ArrayList customers = new ArrayList();
        for (int i = 0; i < orgNames.length; i++) {
            String orgName = orgNames[i][0];
            String url = orgNames[i][1];
            CpxCustomer customer = createCustomer(orgName, url);
            Contact contact = createContact();
            customer.setContact(contact);
            customers.add(customer);
            customerDAO.insertCustomer(customer);
        }
        this.customers = (CpxCustomer[]) customers.toArray(new CpxCustomer[customers.size()]);
    }

    void updateAllCustomers() throws CustomerException {
        info("Enter BusinessApp::updateAllCustomers");
        List customers = customerDAO.loadAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            Object value = customers.get(i);
            CpxCustomer cpxCustomer = (CpxCustomer) value;
            cpxCustomer.getContact().setFirstName(cpxCustomer.getContact().getFirstName() + " Jr");
            customerDAO.updateCustomer(cpxCustomer);
        }
    }

    void deleteAllCustomers() throws CustomerException {
        info("Enter BusinessApp::deleteAllCustomers");
        List customers = customerDAO.loadAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            CpxCustomer cpxCustomer = (CpxCustomer) customers.get(i);
            customerDAO.deleteCustomer(cpxCustomer);
        }
    }

    private void info(String message) {
        BusinessApp.getLogger().info("======================================");
        BusinessApp.getLogger().info(message);
        BusinessApp.getLogger().info("--------------------------------------");
    }

    Transaction begin() throws SQLException {
        return transactionManager.begin();
    }

    void commit(Transaction transaction) throws SQLException {
        transactionManager.commit(transaction);
    }

    public static void main(String[] args) throws SQLException, CustomerException, IOException {
        GenDescriptors.createTables();
        BusinessApp app = new BusinessApp(GenObjects.configPath);
        Transaction transaction = app.begin();
        app.createAllCustomers();
        app.updateAllCustomers();
        app.deleteAllCustomers();
        app.commit(transaction);
    }
}
