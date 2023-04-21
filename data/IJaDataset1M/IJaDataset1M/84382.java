package com.eptica.ias.models.requests.draftrequest.impl;

import java.util.*;
import org.hibernate.criterion.*;
import org.hibernate.*;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import com.eptica.ias.models.requests.draftrequest.*;
import com.eptica.ias.util.*;

/**
 * Hibernate implementation of the DraftRequestModel interface.<br>
 * Note: the exceptions are converted to generic spring unchecked exceptions.<br>
 * To understand more this please see Chapter 9 of <a href="http://www.amazon.com/exec/obidos/tg/detail/-/0764543857/">Expert One-On-One J2EE Design and Development</a>. 
 */
public class DraftRequestDAOHibernate implements DraftRequestDAO {

    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(DraftRequestDAOHibernate.class);

    private static final String[] stringColumns = new String[] { "customerFirstName", "customerLastName", "messageSubject" };

    /**
     * Get a DraftRequestModel using the primary key value
     *
     * @param draftRequestId the primary key value
     * @return the corresponding DraftRequestModel, null otherwise
     */
    private DraftRequestModel get(final String draftRequestId) {
        DraftRequestModel modelFound = null;
        try {
            modelFound = (DraftRequestModel) getCurrentSession().get(DraftRequestModel.class, draftRequestId);
        } catch (HibernateException he) {
            throw SessionFactoryUtils.convertHibernateAccessException(he);
        }
        if (modelFound == null && logger.isDebugEnabled()) {
            logger.debug("get returned null with pk=" + draftRequestId);
        }
        return modelFound;
    }

    /**
     * get a DraftRequestModel from an object
     *
     * @param draftRequest The DraftRequestModel sample to search for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return the corresponding DraftRequestModel, null otherwise
     */
    public DraftRequestModel get(final DraftRequestModel draftRequest) {
        AssertUtil.notNull(draftRequest, "The draftRequest cannot be null");
        AssertUtil.isTrue(draftRequest.hasPrimaryKey(), "The draftRequest must have its primary key set. " + draftRequest.toString());
        return get(draftRequest.getDraftRequestId());
    }

    /**
     * Search a DraftRequestModel using a sample. 
     *
     * @param draftRequest The DraftRequestModel sample to search for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return the resulting collection of DraftRequestModel
     */
    @SuppressWarnings("unchecked")
    public Collection<DraftRequestModel> find(final DraftRequestModel draftRequest, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(draftRequest, "The draftRequest cannot be null");
        prepareSearchTemplate(searchTemplate);
        if (searchTemplate != null && searchTemplate.hasNamedQuery()) {
            return namedQueryUtil.findByNamedQuery(searchTemplate, draftRequest);
        }
        Criteria criteria = getCriteria(draftRequest, searchTemplate);
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
        return (Collection<DraftRequestModel>) c;
    }

    /**
     * return the number of draftRequest that matches the given model using the defined searchMode
     *
     * @param draftRequest the model to look for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return the number of matched draftRequest 
     */
    public int findCount(final DraftRequestModel draftRequest, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(draftRequest, "The draftRequest cannot be null");
        prepareSearchTemplate(searchTemplate);
        if (searchTemplate != null && searchTemplate.hasNamedQuery()) {
            return namedQueryUtil.numberByNamedQuery(searchTemplate, draftRequest).intValue();
        }
        Criteria criteria = getCriteria(draftRequest, searchTemplate);
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
     * @param draftRequest the model to look for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return an hibernate criteria
     */
    private Criteria getCriteria(final DraftRequestModel draftRequest, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(draftRequest, "The draftRequest cannot be null");
        Criteria criteria = getCurrentSession().createCriteria(DraftRequestModel.class);
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
        if (draftRequest != null) {
            criteria.add(getExample(draftRequest, searchTemplate));
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
    private Example getExample(final DraftRequestModel draftRequest, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(draftRequest, "The draftRequest cannot be null");
        Example example = Example.create(draftRequest);
        example = example.excludeZeroes();
        if (!draftRequest.hasDraftRequestId()) {
            example.excludeProperty("draftRequestId");
        }
        if (!draftRequest.hasRequestId()) {
            example.excludeProperty("requestId");
        }
        if (!draftRequest.hasAccountId()) {
            example.excludeProperty("accountId");
        }
        if (!draftRequest.hasCustomerId()) {
            example.excludeProperty("customerId");
        }
        if (!draftRequest.hasMessageId()) {
            example.excludeProperty("messageId");
        }
        if (!draftRequest.hasLastModificationDate()) {
            example.excludeProperty("lastModificationDate");
        }
        if (null == draftRequest.getVersion()) {
            example.excludeProperty("version");
        }
        if (!draftRequest.hasCustomerFirstName()) {
            example.excludeProperty("customerFirstName");
        }
        if (!draftRequest.hasCustomerLastName()) {
            example.excludeProperty("customerLastName");
        }
        if (!draftRequest.hasMessageSubject()) {
            example.excludeProperty("messageSubject");
        }
        if (null == draftRequest.getHasAttachment()) {
            example.excludeProperty("hasAttachment");
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
     * Save a collection of DraftRequestModel.
     *
     * @param draftRequests The draftRequests collection to save
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     */
    public void save(final Collection<DraftRequestModel> draftRequests) {
        AssertUtil.notNull(draftRequests, "The draftRequests collection must not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("About to save a collection of draftRequests (" + draftRequests.size() + " items)");
        }
        for (DraftRequestModel draftRequest : draftRequests) {
            AssertUtil.notNull(draftRequest, "The table.modelsVar collection must not contain null item");
            try {
                getCurrentSession().saveOrUpdate(draftRequest);
            } catch (HibernateException he) {
                throw SessionFactoryUtils.convertHibernateAccessException(he);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Save collection of draftRequests done");
        }
    }

    /**
     * Delete a collection of DraftRequestModel.
     *
     * @param draftRequests The draftRequests collection to delete
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     */
    public void delete(final Collection<DraftRequestModel> draftRequests) {
        AssertUtil.notNull(draftRequests, "The draftRequests collection must not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("About to delete a collection of draftRequests (" + draftRequests.size() + " items)");
        }
        for (DraftRequestModel draftRequest : draftRequests) {
            AssertUtil.notNull(draftRequest, "The table.modelsVar collection must not contain null item");
            AssertUtil.isTrue(draftRequest.hasPrimaryKey(), "The table.modelsVar collection must not contain item with no primary key set");
            try {
                getCurrentSession().delete(draftRequest);
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

    private String cacheRegion = "com.eptica.ias.models.requests.draftrequest.DraftRequestModel";

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
