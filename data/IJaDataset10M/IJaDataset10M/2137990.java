package com.eptica.ias.models.requests.customer.impl;

import java.util.*;
import com.eptica.ias.models.requests.customer.*;
import com.eptica.ias.util.*;
import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 * Implementation of the CustomerManager interface
 */
public class CustomerManagerImpl implements CustomerManager {

    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(CustomerManagerImpl.class);

    /**
     * Saves or update the passed CustomerModel instance.
     *
     * @param customer The CustomerModel to save or update
     */
    public void save(final CustomerModel customer) {
        Collection<CustomerModel> customers = new ArrayList<CustomerModel>(1);
        customers.add(customer);
        save(customers);
    }

    /**
     * Saves or update the passed collection of CustomerModel instances.
     *
     * @param customers The customers collection to save
     */
    public void save(final Collection<CustomerModel> customers) {
        if (logger.isDebugEnabled()) {
            logger.debug("Save collection (" + customers.size() + " items)");
        }
        customerDAO.save(customers);
    }

    /**
     * Return the persistent instance of CustomerModel class with the given primary key value, 
     * or null if there is no such persistent instance. 
     *
     * @param customerId the primary key value
     * @return the corresponding CustomerModel persistent instance or null
     */
    public CustomerModel get(final String customerId) {
        CustomerModel customer = new CustomerModel();
        customer.setCustomerId(customerId);
        return get(customer);
    }

    /**
     * Delete a CustomerModel using the primary key value
     *
     * @param customerId the primary key value
     */
    public void delete(final String customerId) {
        delete(get(customerId));
    }

    /**
     * Return the persistent instance of CustomerModel class with the primary key of the passed CustomerModel instance, 
     * or null if there is no such persistent instance. 
     *
     * @param customer the CustomerModel instance with its primary key set
     * @return the corresponding CustomerModel persistant instance or null
     */
    public CustomerModel get(final CustomerModel customer) {
        return customerDAO.get(customer);
    }

    /**
     * Delete a CustomerModel given the primary key value of the passed isntance.
     *
     * @param customer The customer to delete
     */
    public void delete(final CustomerModel customer) {
        if (customer == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("skipping deletion if null model!");
            }
        }
        Collection<CustomerModel> customers = new ArrayList<CustomerModel>(1);
        customers.add(customer);
        delete(customers);
    }

    /**
     * Delete a collection of CustomerModel given the primary key value of the passed isntances.
     *
     * @param customers The collection of CustomerModel to delete
     */
    public void delete(final Collection<CustomerModel> customers) {
        if (logger.isDebugEnabled()) {
            logger.debug("Delete collection (" + customers.size() + " items)");
        }
        customerDAO.delete(customers);
    }

    /**
     * Return the persistent instance of CustomerModel with the given unique property value esCustomerId, 
     * or null if there is no such persistent instance. 
     *
     * @param esCustomerId the unique value
     * @return the corresponding CustomerModel persistant instance or null
     */
    public CustomerModel getByEsCustomerId(final String esCustomerId) {
        CustomerModel customer = new CustomerModel();
        customer.setEsCustomerId(esCustomerId);
        return findUniqueOrNone(customer);
    }

    /**
     * Delete a CustomerModel using the unique column es_customer_id
     *
     * @param esCustomerId the unique value
     */
    public void deleteByEsCustomerId(final String esCustomerId) {
        delete(getByEsCustomerId(esCustomerId));
    }

    /**
     * Return the persistent instance of CustomerModel with the given unique property value email, 
     * or null if there is no such persistent instance. 
     *
     * @param email the unique value
     * @return the corresponding CustomerModel persistant instance or null
     */
    public CustomerModel getByEmail(final String email) {
        CustomerModel customer = new CustomerModel();
        customer.setEmail(email);
        return findUniqueOrNone(customer);
    }

    /**
     * Delete a CustomerModel using the unique column email
     *
     * @param email the unique value
     */
    public void deleteByEmail(final String email) {
        delete(getByEmail(email));
    }

    /**
     * Return the unique persistent instance of CustomerModel matching exactly the given sample,
     * or throw an exception if zero or more than one instance match the sample.
     * Each non-null property of the passed sample is used and must exactly match the desired instance.
     *
     * @param customer the sample to match exactly
     * @return the matching CustomerModel persistant instance
     * @throws an IllegalStateException if zero or more than one instance match the sample
     */
    public CustomerModel findUnique(final CustomerModel customer) {
        CustomerModel result = findUniqueOrNone(customer);
        if (result == null) {
            throw new InvalidDataAccessApiUsageException("Developper: You expected 1 result but we found none ! sample: " + customer);
        }
        return result;
    }

    /**
     * Return the unique persistent instance of CustomerModel matching exactly the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     * Each non-null property of the passed sample is used and must exactly match the desired instance.
     *
     * @param customer the sample to match exactly
     * @return the matching CustomerModel persistant instance or null
     * @throws an IllegalStateException if more than one instance match the sample
     */
    public CustomerModel findUniqueOrNone(final CustomerModel customer) {
        if (logger.isDebugEnabled()) {
            logger.debug("Finding first CustomerModel with sample " + customer.toString());
        }
        SearchTemplate searchTemplate = new SearchTemplateImpl();
        searchTemplate.setFirstResult(0);
        searchTemplate.setMaxResults(2);
        Collection<CustomerModel> results = find(customer, searchTemplate);
        if (results == null || results.isEmpty()) {
            return null;
        }
        if (results.size() > 1) {
            throw new InvalidDataAccessApiUsageException("Developper: You expected 1 result but we found more ! sample: " + customer);
        }
        return results.iterator().next();
    }

    /**
     * Return the persistent instances of CustomerModel matching exactly the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     *
     * @param customer the sample to match exactly
     */
    public Collection<CustomerModel> find(final CustomerModel customer) {
        return find(customer, new SearchTemplateImpl());
    }

    /**
     * Return the persistent instances of CustomerModel matching the given sample, 
     * null if there is no such persistent instance, or throw an exception if more than one instance match the sample.
     * 
     * @param customer the sample to match
     * @param searchTemplate the specific criteria such as SearchMode, orderBy, etc.
     */
    public Collection<CustomerModel> find(final CustomerModel customer, final SearchTemplate searchTemplate) {
        return customerDAO.find(customer, searchTemplate);
    }

    /**
     * Return the number of persistent instances of CustomerModel matching exactly the given sample.
     * Each non-null property of the passed sample is used and compared exactly.
     * 
     * @param customer the sample to match
     * @return the resulting count
     */
    public int findCount(final CustomerModel customer) {
        return findCount(customer, new SearchTemplateImpl());
    }

    /**
     * Return the number of persistent instances of CustomerModel matching the given sample.
     * 
     * @param customer the model to match
     * @param searchTemplate the specific criteria such as SearchMode
     * @return the resulting count
     */
    public int findCount(final CustomerModel customer, final SearchTemplate searchTemplate) {
        return customerDAO.findCount(customer, searchTemplate);
    }

    /**
     * alled by spring upon spring bean creation if requested in xml
     */
    public void init() {
        AssertUtil.notNull(customerDAO);
    }

    private CustomerDAO customerDAO = null;

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
}
