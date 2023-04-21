package org.doframework.sample.xml_handler;

import org.doframework.*;
import org.doframework.annotation.*;
import org.doframework.sample.component.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.*;

@TargetClass(Customer.class)
public class CustomerXmlHandler implements DependentObjectHandler, ScratchPkProvider, DeletionHelper {

    CustomerComponent customerComponent = ComponentFactory.getCustomerComponent();

    public Customer createCustomer(ObjectFileInfo ofi) {
        InputStream is = ofi.getFileContentsAsInputStream();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            XPathFactory pathFactory = XPathFactory.newInstance();
            XPath xPath = pathFactory.newXPath();
            Element item = document.getDocumentElement();
            String name = xPath.evaluate("/customer/name", item);
            String phoneNumber = xPath.evaluate("/customer/phone_number", item);
            Customer customer = customerComponent.createNew();
            customer.setName(name);
            customer.setPhoneNumber(phoneNumber);
            return customer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object create(ObjectFileInfo ofi) {
        Customer customer = createCustomer(ofi);
        customerComponent.persist(customer);
        return customer;
    }

    public Object get(ObjectFileInfo ofi) {
        return customerComponent.getByName(ofi.getPk());
    }

    public boolean delete(Object object) {
        return customerComponent.delete((Customer) object);
    }

    public boolean okToDelete(Object object) {
        return !customerComponent.hasInvoices((Customer) object);
    }

    /**
     * This method is used for deletion of scratch objects. Thus, it is not necessary to implement
     * it if you are not concerned with deleting scratch objects.
     *
     * @param object Which is the type that the given handler is mapped to.
     *
     * @return The dependencies of the given object
     */
    public Object[] getReferencedObjects(Object object) {
        return null;
    }

    /**
     * Information on what parent dependencies this object has are used to for operation
     * DOF.deleteAll(). The dependencies are gathered and objects are deleted in the correct order
     * to avoid conflicts.
     *
     * @return
     */
    public Class[] getReferencedClasses() {
        return null;
    }

    /**
     * Implement this provide a scratch primary key.
     *
     * @return
     */
    public String getScratchPk() {
        return "ScratchCustomer " + System.currentTimeMillis() + " " + Math.random();
    }

    public Object extractPrimaryKey(Object object) {
        return ((Customer) object).getName();
    }
}
