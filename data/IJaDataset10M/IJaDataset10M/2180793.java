package com.eptica.ias.models.requests.requesttype.impl;

import java.util.*;
import org.hibernate.criterion.*;
import org.hibernate.*;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import com.eptica.ias.models.requests.requesttype.*;
import com.eptica.ias.util.*;

/**
 * Hibernate implementation of the RequestTypeModel interface.<br>
 * Note: the exceptions are converted to generic spring unchecked exceptions.<br>
 * To understand more this please see Chapter 9 of <a href="http://www.amazon.com/exec/obidos/tg/detail/-/0764543857/">Expert One-On-One J2EE Design and Development</a>. 
 */
public class RequestTypeDAOHibernate implements RequestTypeDAO {

    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(RequestTypeDAOHibernate.class);

    private static final String[] stringColumns = new String[] { "nameLocale" };

    /**
     * Get a RequestTypeModel using the primary key value
     *
     * @param requestTypeId the primary key value
     * @return the corresponding RequestTypeModel, null otherwise
     */
    private RequestTypeModel get(final Integer requestTypeId) {
        RequestTypeModel modelFound = null;
        try {
            modelFound = (RequestTypeModel) getCurrentSession().get(RequestTypeModel.class, requestTypeId);
        } catch (HibernateException he) {
            throw SessionFactoryUtils.convertHibernateAccessException(he);
        }
        if (modelFound == null && logger.isDebugEnabled()) {
            logger.debug("get returned null with pk=" + requestTypeId);
        }
        return modelFound;
    }

    /**
     * get a RequestTypeModel from an object
     *
     * @param requestType The RequestTypeModel sample to search for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return the corresponding RequestTypeModel, null otherwise
     */
    public RequestTypeModel get(final RequestTypeModel requestType) {
        AssertUtil.notNull(requestType, "The requestType cannot be null");
        AssertUtil.isTrue(requestType.hasPrimaryKey(), "The requestType must have its primary key set. " + requestType.toString());
        return get(requestType.getRequestTypeId());
    }

    /**
     * Search a RequestTypeModel using a sample. 
     *
     * @param requestType The RequestTypeModel sample to search for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return the resulting collection of RequestTypeModel
     */
    @SuppressWarnings("unchecked")
    public Collection<RequestTypeModel> find(final RequestTypeModel requestType, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(requestType, "The requestType cannot be null");
        prepareSearchTemplate(searchTemplate);
        if (searchTemplate != null && searchTemplate.hasNamedQuery()) {
            return namedQueryUtil.findByNamedQuery(searchTemplate, requestType);
        }
        Criteria criteria = getCriteria(requestType, searchTemplate);
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
        return (Collection<RequestTypeModel>) c;
    }

    /**
     * return the number of requestType that matches the given model using the defined searchMode
     *
     * @param requestType the model to look for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return the number of matched requestType 
     */
    public int findCount(final RequestTypeModel requestType, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(requestType, "The requestType cannot be null");
        prepareSearchTemplate(searchTemplate);
        if (searchTemplate != null && searchTemplate.hasNamedQuery()) {
            return namedQueryUtil.numberByNamedQuery(searchTemplate, requestType).intValue();
        }
        Criteria criteria = getCriteria(requestType, searchTemplate);
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
     * @param requestType the model to look for
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     * @return an hibernate criteria
     */
    private Criteria getCriteria(final RequestTypeModel requestType, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(requestType, "The requestType cannot be null");
        Criteria criteria = getCurrentSession().createCriteria(RequestTypeModel.class);
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
        if (requestType != null) {
            criteria.add(getExample(requestType, searchTemplate));
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
    private Example getExample(final RequestTypeModel requestType, final SearchTemplate searchTemplate) {
        AssertUtil.notNull(requestType, "The requestType cannot be null");
        Example example = Example.create(requestType);
        example = example.excludeZeroes();
        if (!requestType.hasRequestTypeId()) {
            example.excludeProperty("requestTypeId");
        }
        if (!requestType.hasNameLocale()) {
            example.excludeProperty("nameLocale");
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
     * Save a collection of RequestTypeModel.
     *
     * @param requestTypes The requestTypes collection to save
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     */
    public void save(final Collection<RequestTypeModel> requestTypes) {
        AssertUtil.notNull(requestTypes, "The requestTypes collection must not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("About to save a collection of requestTypes (" + requestTypes.size() + " items)");
        }
        for (RequestTypeModel requestType : requestTypes) {
            AssertUtil.notNull(requestType, "The table.modelsVar collection must not contain null item");
            try {
                getCurrentSession().saveOrUpdate(requestType);
            } catch (HibernateException he) {
                throw SessionFactoryUtils.convertHibernateAccessException(he);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Save collection of requestTypes done");
        }
    }

    /**
     * Delete a collection of RequestTypeModel.
     *
     * @param requestTypes The requestTypes collection to delete
     * @param searchTemplate the specific parameters such as named queries, extra infos, limitations, order, ...
     */
    public void delete(final Collection<RequestTypeModel> requestTypes) {
        AssertUtil.notNull(requestTypes, "The requestTypes collection must not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("About to delete a collection of requestTypes (" + requestTypes.size() + " items)");
        }
        for (RequestTypeModel requestType : requestTypes) {
            AssertUtil.notNull(requestType, "The table.modelsVar collection must not contain null item");
            AssertUtil.isTrue(requestType.hasPrimaryKey(), "The table.modelsVar collection must not contain item with no primary key set");
            try {
                getCurrentSession().delete(requestType);
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

    private String cacheRegion = "com.eptica.ias.models.requests.requesttype.RequestTypeModel";

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
