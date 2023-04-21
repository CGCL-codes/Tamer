package com.eptica.ias.models.requests.customer.impl;

import java.util.*;
import org.hibernate.criterion.*;
import org.hibernate.*;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import com.eptica.ias.models.requests.customer.*;
import com.eptica.ias.util.*;

/**
 * Hibernate implementation of the CustomerModel interface.<br>
 * Note: the exceptions are converted to generic spring unchecked exceptions.<br>
 * To understand more this please see Chapter 9 of <a href="http://www.amazon.com/exec/obidos/tg/detail/-/0764543857/">Expert One-On-One J2EE Design and Development</a>. 
 */
public class CustomerDAOHibernate implements CustomerDAO {

    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(CustomerDAOHibernate.class);

    private static final String[] stringColumns = new String[] { "esCustomerId", "firstName", "lastName", "fullName", "email", "fax", "address", "city", "state", "zipCode", "country", "companyName" };

    /**
     * Get a CustomerModel using the primary key value
     *
     * @param customerId the primary key value
     * @return the corresponding CustomerModel, null otherwise
     */
    private CustomerModel get(final String customerId) {
        CustomerModel modelFound = null;
        try {
            modelFound = (CustomerModel) getCurrentSession().get(CustomerModel.class, customerId);
        } catch (HibernateException he) {
            throw SessionFactoryUtils.convertHibernateAccessException(he);
        }
        if (modelFound == null && logger.isDebugEnabled()) {
            logger.debug("get returned null with pk=" + customerId);
        }
        return modelFound;
    }

    /**
     * get a CustomerModel from an object
     *
     * @param customer The CustomerModel sample to search for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return the corresponding CustomerModel, null otherwise
     */
    public CustomerModel get(final CustomerModel customer) {
        AssertUtil.notNull(customer, "The customer cannot be null");
        AssertUtil.isTrue(customer.hasPrimaryKey(), "The customer must have its primary key set. " + customer.toString());
        return get(customer.getCustomerId());
    }

    /**
     * Search a CustomerModel using a sample. 
     *
     * @param customer The CustomerModel sample to search for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return the resulting collection of CustomerModel
     */
    @SuppressWarnings("unchecked")
    public Collection<CustomerModel> find(final CustomerModel customer, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(customer, "The customer cannot be null");
        prepareSearchTemplate(searchTemplate);
        if (searchTemplate != null && searchTemplate.hasNamedQuery()) {
            return namedQueryUtil.findByNamedQuery(searchTemplate, customer);
        }
        Criteria criteria = getCriteria(customer, searchTemplate);
        if (searchTemplate != null) {
            criteria.setFirstResult(searchTemplate.getFirstResult());
            criteria.setMaxResults(Math.min(maxResultsAuthorized, searchTemplate.getMaxResults()));
            if (searchTemplate.hasOrderBy()) {
                if (searchTemplate.getOrderDesc()) {
                    criteria.addOrder(Order.desc(searchTemplate.getOrderBy()));
                } else {
                    criteria.addOrder(Order.asc(searchTemplate.getOrderBy()));
                }
            }
        }
        Collection c = null;
        try {
            c = criteria.list();
        } catch (HibernateException he) {
            throw SessionFactoryUtils.convertHibernateAccessException(he);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Returned " + c.size() + " elements");
        }
        return (Collection<CustomerModel>) c;
    }

    /**
     * return the number of customer that matches the given model using the defined searchMode
     *
     * @param customer the model to look for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return the number of matched customer 
     */
    public int findCount(final CustomerModel customer, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(customer, "The customer cannot be null");
        prepareSearchTemplate(searchTemplate);
        if (searchTemplate != null && searchTemplate.hasNamedQuery()) {
            return namedQueryUtil.numberByNamedQuery(searchTemplate, customer).intValue();
        }
        Criteria criteria = getCriteria(customer, searchTemplate);
        criteria.setProjection(Projections.rowCount());
        try {
            int count = ((Number) criteria.uniqueResult()).intValue();
            if (logger.isDebugEnabled()) {
                logger.debug("Returned " + count + " elements");
            }
            return count;
        } catch (HibernateException he) {
            throw SessionFactoryUtils.convertHibernateAccessException(he);
        }
    }

    /**
     * convert local searchmode in hibernate search modes
     */
    private MatchMode getMatchMode(final SearchTemplate searchTemplate) {
        if (searchTemplate.getSearchMode() == SearchMode.ANYWHERE) {
            return MatchMode.ANYWHERE;
        } else if (searchTemplate.getSearchMode() == SearchMode.STARTING_LIKE) {
            return MatchMode.START;
        } else if (searchTemplate.getSearchMode() == SearchMode.ENDING_LIKE) {
            return MatchMode.END;
        } else {
            return MatchMode.EXACT;
        }
    }

    /**
     * create a criteria with with caching enabled
     *
     * @param customer the model to look for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return an hibernate criteria
     */
    private Criteria getCriteria(final CustomerModel customer, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(customer, "The customer cannot be null");
        Criteria criteria = getCurrentSession().createCriteria(CustomerModel.class);
        criteria.setCacheable(getCacheable());
        if (StringUtil.hasLength(getCacheRegion())) {
            criteria.setCacheRegion(getCacheRegion());
        }
        Criterion criterion = null;
        if (stringColumns.length > 0 && searchTemplate != null && searchTemplate.hasSearchPattern()) {
            MatchMode matchMode = getMatchMode(searchTemplate);
            for (String stringColumn : stringColumns) {
                Criterion tempCriterion = null;
                if (matchMode == MatchMode.EXACT) {
                    tempCriterion = Restrictions.like(stringColumn, searchTemplate.getSearchPattern(), matchMode);
                } else {
                    tempCriterion = Restrictions.eq(stringColumn, searchTemplate.getSearchPattern());
                }
                if (criterion == null) {
                    criterion = tempCriterion;
                } else {
                    criterion = Restrictions.or(criterion, tempCriterion);
                }
            }
        }
        if (criterion != null) {
            criteria.add(criterion);
        }
        if (customer != null) {
            criteria.add(getExample(customer, searchTemplate));
        }
        return criteria;
    }

    /**
     * Get a pre-set Example, to be used in a criteria.
     *
     * @param table.modelVar the model to be used for search
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return an hibernate Example
     */
    private Example getExample(final CustomerModel customer, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(customer, "The customer cannot be null");
        Example example = Example.create(customer);
        example = example.excludeZeroes();
        if (!customer.hasCustomerId()) {
            example.excludeProperty("customerId");
        }
        if (!customer.hasEsCustomerId()) {
            example.excludeProperty("esCustomerId");
        }
        if (!customer.hasFirstName()) {
            example.excludeProperty("firstName");
        }
        if (!customer.hasLastName()) {
            example.excludeProperty("lastName");
        }
        if (!customer.hasFullName()) {
            example.excludeProperty("fullName");
        }
        if (!customer.hasEmail()) {
            example.excludeProperty("email");
        }
        if (!customer.hasFax()) {
            example.excludeProperty("fax");
        }
        if (!customer.hasAddress()) {
            example.excludeProperty("address");
        }
        if (!customer.hasCity()) {
            example.excludeProperty("city");
        }
        if (!customer.hasState()) {
            example.excludeProperty("state");
        }
        if (!customer.hasZipCode()) {
            example.excludeProperty("zipCode");
        }
        if (!customer.hasCountry()) {
            example.excludeProperty("country");
        }
        if (!customer.hasCompanyName()) {
            example.excludeProperty("companyName");
        }
        if (!customer.hasSalutationId()) {
            example.excludeProperty("salutationId");
        }
        if (null == customer.getVersion()) {
            example.excludeProperty("version");
        }
        if (searchTemplate != null) {
            if (searchTemplate.isCaseInsensitive()) {
                example.ignoreCase();
            }
            if (searchTemplate.getSearchMode() == SearchMode.ANYWHERE) {
                example = example.enableLike(MatchMode.ANYWHERE);
            } else if (searchTemplate.getSearchMode() == SearchMode.STARTING_LIKE) {
                example = example.enableLike(MatchMode.START);
            } else if (searchTemplate.getSearchMode() == SearchMode.ENDING_LIKE) {
                example = example.enableLike(MatchMode.END);
            }
        }
        return example;
    }

    /**
     * Save a collection of CustomerModel.
     *
     * @param customers The customers collection to save
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     */
    public void save(final Collection<CustomerModel> customers) {
        AssertUtil.notNull(customers, "The customers collection must not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("About to save a collection of customers (" + customers.size() + " items)");
        }
        for (CustomerModel customer : customers) {
            AssertUtil.notNull(customer, "The table.modelsVar collection must not contain null item");
            try {
                getCurrentSession().saveOrUpdate(customer);
            } catch (HibernateException he) {
                throw SessionFactoryUtils.convertHibernateAccessException(he);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Save collection of customers done");
        }
    }

    /**
     * Delete a collection of CustomerModel.
     *
     * @param customers The customers collection to delete
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     */
    public void delete(final Collection<CustomerModel> customers) {
        AssertUtil.notNull(customers, "The customers collection must not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("About to delete a collection of customers (" + customers.size() + " items)");
        }
        for (CustomerModel customer : customers) {
            AssertUtil.notNull(customer, "The table.modelsVar collection must not contain null item");
            AssertUtil.isTrue(customer.hasPrimaryKey(), "The table.modelsVar collection must not contain item with no primary key set");
            try {
                getCurrentSession().delete(customer);
            } catch (HibernateException he) {
                throw SessionFactoryUtils.convertHibernateAccessException(he);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Delete collection done");
        }
    }

    /**
     * init the searchTemplate 
     */
    private void prepareSearchTemplate(final SearchTemplate searchTemplate) {
        if (searchTemplate != null) {
            if (!searchTemplate.isMaxResultsAuthorizedProvided()) {
                searchTemplate.setMaxResultsAuthorized(getMaxResultsAuthorized());
            }
            if (!searchTemplate.isCacheInformationProvided()) {
                searchTemplate.setCacheable(getCacheable());
                searchTemplate.setCacheRegion(getCacheRegion());
            }
        }
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * called by spring upon spring bean creation if requested in xml
     */
    public void init() {
        AssertUtil.notNull(sessionFactory);
        AssertUtil.notNull(namedQueryUtil);
    }

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private NamedQueryUtil namedQueryUtil = null;

    public void setNamedQueryUtil(NamedQueryUtil namedQueryUtil) {
        this.namedQueryUtil = namedQueryUtil;
    }

    private boolean cacheable = true;

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public boolean getCacheable() {
        return this.cacheable;
    }

    private String cacheRegion = "com.eptica.ias.models.requests.customer.CustomerModel";

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }

    public String getCacheRegion() {
        return this.cacheRegion;
    }

    private int maxResultsAuthorized = 500;

    public void setMaxResultsAuthorized(int maxResultsAuthorized) {
        this.maxResultsAuthorized = maxResultsAuthorized;
    }

    public int getMaxResultsAuthorized() {
        return this.maxResultsAuthorized;
    }
}
