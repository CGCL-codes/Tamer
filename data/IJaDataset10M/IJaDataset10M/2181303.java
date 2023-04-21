package net.sf.mp.demo.conference.dao.impl.jpa.conference;

import java.lang.reflect.InvocationTargetException;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import net.sf.minuteProject.architecture.query.QueryWhatInit;
import net.sf.minuteProject.architecture.query.impl.QueryCountInit;
import net.sf.minuteProject.architecture.query.impl.QuerySelectCountInit;
import net.sf.minuteProject.architecture.query.impl.QuerySelectInit;
import net.sf.minuteProject.architecture.query.impl.QuerySelectDistinctInit;
import net.sf.minuteProject.architecture.cache.SimpleCache;
import net.sf.minuteProject.architecture.filter.data.ClauseCriterion;
import net.sf.minuteProject.architecture.filter.data.Criteria;
import net.sf.minuteProject.architecture.filter.data.Criterion;
import net.sf.minuteProject.architecture.filter.data.InCriterion;
import net.sf.minuteProject.architecture.filter.data.OrderCriteria;
import net.sf.minuteProject.architecture.utils.BeanUtils;
import net.sf.mp.demo.conference.dao.face.conference.ConferenceExtDao;
import net.sf.mp.demo.conference.domain.conference.Conference;
import net.sf.mp.demo.conference.dao.impl.jpa.conference.ConferenceJPAImpl;
import net.sf.mp.demo.conference.domain.conference.ConferenceFeedback;
import net.sf.mp.demo.conference.dao.impl.jpa.conference.ConferenceFeedbackExtendedJPAImpl;
import net.sf.mp.demo.conference.domain.conference.ConferenceMember;
import net.sf.mp.demo.conference.dao.impl.jpa.conference.ConferenceMemberExtendedJPAImpl;
import net.sf.mp.demo.conference.domain.conference.Sponsor;
import net.sf.mp.demo.conference.dao.impl.jpa.conference.SponsorExtendedJPAImpl;
import net.sf.mp.demo.conference.dao.impl.jpa.conference.AddressExtendedJPAImpl;

/**
 *
 * <p>Title: ConferenceExtendedJPAImpl</p>
 *
 * <p>Description: Interface of a Data access object dealing with ConferenceExtendedJPAImpl
 * persistence. It offers a set of methods which allow for saving,
 * deleting and searching ConferenceExtendedJPAImpl objects</p>
 *
 */
public class ConferenceExtendedJPAImpl extends ConferenceJPAImpl implements ConferenceExtDao {

    private Logger log = Logger.getLogger(this.getClass());

    private SimpleCache simpleCache = new SimpleCache();

    private ConferenceFeedbackExtendedJPAImpl conferencefeedbackextendedjpaimpl;

    private ConferenceMemberExtendedJPAImpl conferencememberextendedjpaimpl;

    private SponsorExtendedJPAImpl sponsorextendedjpaimpl;

    private EntityManager emForRecursiveDao;

    public ConferenceExtendedJPAImpl() {
    }

    /**
     * generic to move after in superclass
     */
    public ConferenceExtendedJPAImpl(EntityManager emForRecursiveDao) {
        this.emForRecursiveDao = emForRecursiveDao;
    }

    /**
     * generic to move after in superclass
     */
    public List<Object[]> getSQLQueryResult(String query) {
        Query queryJ = getEntityManager().createNativeQuery(query);
        return queryJ.getResultList();
    }

    /**
     * Inserts a Conference entity with cascade of its children
     * @param Conference conference
     */
    public void insertConferenceWithCascade(Conference conference) {
        ConferenceExtendedJPAImpl conferenceextendedjpaimpl = new ConferenceExtendedJPAImpl(getEntityManager());
        conferenceextendedjpaimpl.insertConferenceWithCascade(conferenceextendedjpaimpl.getEntityManagerForRecursiveDao(), conference);
    }

    public void insertConferenceWithCascade(EntityManager emForRecursiveDao, Conference conference) {
        insertConference(emForRecursiveDao, conference);
        if (!conference.getConferenceFeedbackConferenceViaConferenceId().isEmpty()) {
            ConferenceFeedbackExtendedJPAImpl conferencefeedbackextendedjpaimpl = new ConferenceFeedbackExtendedJPAImpl(emForRecursiveDao);
            for (ConferenceFeedback _conferenceFeedbackConferenceViaConferenceId : conference.getConferenceFeedbackConferenceViaConferenceId()) {
                conferencefeedbackextendedjpaimpl.insertConferenceFeedbackWithCascade(emForRecursiveDao, _conferenceFeedbackConferenceViaConferenceId);
            }
        }
        if (!conference.getConferenceMemberConferenceViaConferenceId().isEmpty()) {
            ConferenceMemberExtendedJPAImpl conferencememberextendedjpaimpl = new ConferenceMemberExtendedJPAImpl(emForRecursiveDao);
            for (ConferenceMember _conferenceMemberConferenceViaConferenceId : conference.getConferenceMemberConferenceViaConferenceId()) {
                conferencememberextendedjpaimpl.insertConferenceMemberWithCascade(emForRecursiveDao, _conferenceMemberConferenceViaConferenceId);
            }
        }
        if (!conference.getSponsorConferenceViaConferenceId().isEmpty()) {
            SponsorExtendedJPAImpl sponsorextendedjpaimpl = new SponsorExtendedJPAImpl(emForRecursiveDao);
            for (Sponsor _sponsorConferenceViaConferenceId : conference.getSponsorConferenceViaConferenceId()) {
                sponsorextendedjpaimpl.insertSponsorWithCascade(emForRecursiveDao, _sponsorConferenceViaConferenceId);
            }
        }
    }

    /**
     * Inserts a list of Conference entity with cascade of its children
     * @param List<Conference> conferences
     */
    public void insertConferencesWithCascade(List<Conference> conferences) {
        for (Conference conference : conferences) {
            insertConferenceWithCascade(conference);
        }
    }

    /**
     * lookup Conference entity Conference, criteria and max result number
     */
    public List<Conference> lookupConference(Conference conference, Criteria criteria, Integer numberOfResult, EntityManager em) {
        boolean isWhereSet = false;
        StringBuffer query = new StringBuffer();
        query.append("SELECT conference FROM Conference conference ");
        for (Criterion criterion : criteria.getClauseCriterions()) {
            query.append(getQueryWHERE_AND(isWhereSet));
            isWhereSet = true;
            query.append(criterion.getExpression());
        }
        OrderCriteria orderCriteria = criteria.getOrderCriteria();
        if (criteria.getOrderCriteria() != null) query.append(orderCriteria.getExpression());
        Query hquery = em.createQuery(query.toString());
        if (numberOfResult != null) hquery.setMaxResults(numberOfResult);
        return hquery.getResultList();
    }

    public List<Conference> lookupConference(Conference conference, Criteria criteria, Integer numberOfResult) {
        return lookupConference(conference, criteria, numberOfResult, getEntityManager());
    }

    public Integer updateNotNullOnlyConference(Conference conference, Criteria criteria) {
        String queryWhat = getUpdateNotNullOnlyConferenceQueryChunkPrototype(conference);
        StringBuffer query = new StringBuffer(queryWhat);
        boolean isWhereSet = false;
        for (Criterion criterion : criteria.getClauseCriterions()) {
            query.append(getQueryWHERE_AND(isWhereSet));
            isWhereSet = true;
            query.append(criterion.getExpression());
        }
        Query jpaQuery = getEntityManager().createQuery(query.toString());
        isWhereSet = false;
        if (conference.getId() != null) {
            jpaQuery.setParameter("id", conference.getId());
        }
        if (conference.getName() != null) {
            jpaQuery.setParameter("name", conference.getName());
        }
        if (conference.getStartDate() != null) {
            jpaQuery.setParameter("startDate", conference.getStartDate());
        }
        if (conference.getEndDate() != null) {
            jpaQuery.setParameter("endDate", conference.getEndDate());
        }
        if (conference.getAddressId() != null) {
            jpaQuery.setParameter("addressId", conference.getAddressId());
        }
        return jpaQuery.executeUpdate();
    }

    public Conference affectConference(Conference conference) {
        return referConference(conference, false);
    }

    /**
	 * Assign the first conference retrieved corresponding to the conference criteria.
	 * Blank criteria are mapped to null.
	 * If no criteria is found, null is returned.
	 * If there is no conference corresponding in the database. Then conference is inserted and returned with its primary key(s). 
	 */
    public Conference assignConference(Conference conference) {
        return referConference(conference, true);
    }

    public Conference referConference(Conference conference, boolean isAssign) {
        conference = assignBlankToNull(conference);
        if (isAllNull(conference)) return null; else {
            List<Conference> list = searchPrototypeConference(conference);
            if (list.isEmpty()) {
                if (isAssign) insertConference(conference); else return null;
            } else if (list.size() == 1) conference.copy(list.get(0)); else conference.copy(list.get(0));
        }
        return conference;
    }

    public Conference assignConferenceUseCache(Conference conference) {
        return referConferenceUseCache(conference, true);
    }

    public Conference affectConferenceUseCache(Conference conference) {
        return referConferenceUseCache(conference, false);
    }

    public Conference referConferenceUseCache(Conference conference, boolean isAssign) {
        String key = getCacheKey(null, conference, null, "assignConference");
        Conference conferenceCache = (Conference) simpleCache.get(key);
        if (conferenceCache == null) {
            conferenceCache = referConference(conference, isAssign);
            if (key != null) simpleCache.put(key, conferenceCache);
        }
        conference.copy(conferenceCache);
        return conferenceCache;
    }

    private String getCacheKey(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, String queryKey) {
        StringBuffer sb = new StringBuffer();
        sb.append(queryKey);
        if (conferenceWhat != null) sb.append(conferenceWhat.toStringWithParents());
        if (positiveConference != null) sb.append(positiveConference.toStringWithParents());
        if (negativeConference != null) sb.append(negativeConference.toStringWithParents());
        return sb.toString();
    }

    public Conference partialLoadWithParentFirstConference(Conference conferenceWhat, Conference positiveConference, Conference negativeConference) {
        List<Conference> list = partialLoadWithParentConference(conferenceWhat, positiveConference, negativeConference);
        return (!list.isEmpty()) ? (Conference) list.get(0) : null;
    }

    public Conference partialLoadWithParentFirstConferenceUseCache(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, Boolean useCache) {
        List<Conference> list = partialLoadWithParentConferenceUseCache(conferenceWhat, positiveConference, negativeConference, useCache);
        return (!list.isEmpty()) ? (Conference) list.get(0) : null;
    }

    public Conference partialLoadWithParentFirstConferenceUseCacheOnResult(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, Boolean useCache) {
        List<Conference> list = partialLoadWithParentConferenceUseCacheOnResult(conferenceWhat, positiveConference, negativeConference, useCache);
        return (!list.isEmpty()) ? (Conference) list.get(0) : null;
    }

    protected List<Conference> partialLoadWithParentConference(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, Integer nbOfResult, Boolean useCache) {
        return partialLoadWithParentConference(conferenceWhat, positiveConference, negativeConference, new QuerySelectInit(), nbOfResult, useCache);
    }

    protected List partialLoadWithParentConferenceQueryResult(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, Integer nbOfResult, Boolean useCache) {
        return partialLoadWithParentConferenceQueryResult(conferenceWhat, positiveConference, negativeConference, new QuerySelectInit(), nbOfResult, useCache);
    }

    public List<Conference> getDistinctConference(Conference conferenceWhat, Conference positiveConference, Conference negativeConference) {
        return partialLoadWithParentConference(conferenceWhat, positiveConference, negativeConference, new QuerySelectDistinctInit(), null, false);
    }

    public List<Conference> partialLoadWithParentConference(Conference conferenceWhat, Conference positiveConference, Conference negativeConference) {
        return partialLoadWithParentConference(conferenceWhat, positiveConference, negativeConference, new QuerySelectInit(), null, false);
    }

    public List<Conference> partialLoadWithParentConferenceUseCacheOnResult(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, Boolean useCache) {
        String key = getCacheKey(conferenceWhat, positiveConference, negativeConference, "partialLoadWithParentConference");
        List<Conference> list = (List<Conference>) simpleCache.get(key);
        if (list == null || list.isEmpty()) {
            list = partialLoadWithParentConference(conferenceWhat, positiveConference, negativeConference);
            if (!list.isEmpty()) simpleCache.put(key, list);
        }
        return list;
    }

    public List<Conference> partialLoadWithParentConferenceUseCache(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, Boolean useCache) {
        String key = getCacheKey(conferenceWhat, positiveConference, negativeConference, "partialLoadWithParentConference");
        List<Conference> list = (List<Conference>) simpleCache.get(key);
        if (list == null) {
            list = partialLoadWithParentConference(conferenceWhat, positiveConference, negativeConference);
            simpleCache.put(key, list);
        }
        return list;
    }

    private List<Conference> handleLoadWithParentConference(Map beanPath, List list, Conference conferenceWhat) {
        return handleLoadWithParentConference(beanPath, list, conferenceWhat, true);
    }

    private List<Conference> handleLoadWithParentConference(Map beanPath, List list, Conference conferenceWhat, boolean isHql) {
        if (beanPath.size() == 1) {
            return handlePartialLoadWithParentConferenceWithOneElementInRow(list, beanPath, conferenceWhat, isHql);
        }
        return handlePartialLoadWithParentConference(list, beanPath, conferenceWhat, isHql);
    }

    protected void populateConference(Conference conference, Object value, String beanPath) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        BeanUtils.populateBeanObject(conference, beanPath, value);
    }

    protected void populateConferenceFromSQL(Conference conference, Object value, String beanPath) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        BeanUtils.populateBeanObject(conference, beanPath, value);
    }

    private Conference cloneConference(Conference conference) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (conference == null) return new Conference();
        return conference.clone();
    }

    private Object getBeanObjectInstance(Object bean) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return BeanUtils.getBeanObjectInstance(bean);
    }

    protected void populateObject(Object bean, Object value, String beanPath) throws IllegalAccessException, InvocationTargetException {
        BeanUtils.populateObject(bean, value, beanPath);
    }

    protected void populateObjectFromSQL(Object bean, Object value, String beanPath) throws IllegalAccessException, InvocationTargetException {
        BeanUtils.populateObject(bean, value, beanPath);
    }

    public List<Conference> countDistinct(Conference whatMask, Conference whereEqCriteria) {
        return partialLoadWithParentConference(whatMask, whereEqCriteria, null, new QuerySelectCountInit("conference"), null, false);
    }

    public Long count(Conference whereEqCriteria) {
        List<Conference> list = partialLoadWithParentConference(null, whereEqCriteria, null, new QueryCountInit("conference"), null, false);
        if (!list.isEmpty()) return list.get(0).getCount__();
        return 0L;
    }

    public Conference getFirstConferenceWhereConditionsAre(Conference conference) {
        List<Conference> list = partialLoadWithParentConference(getDefaultConferenceWhat(), conference, null, 1, false);
        if (list.isEmpty()) {
            return null;
        } else if (list.size() == 1) return list.get(0); else return list.get(0);
    }

    private List getFirstResultWhereConditionsAre(Conference conference) {
        return partialLoadWithParentConferenceQueryResult(getDefaultConferenceWhat(), conference, null, 1, false);
    }

    protected Conference getDefaultConferenceWhat() {
        Conference conference = new Conference();
        conference.setId(Long.valueOf("-1"));
        return conference;
    }

    public Conference getFirstConference(Conference conference) {
        if (isAllNull(conference)) return null; else {
            List<Conference> list = searchPrototype(conference, 1);
            if (list.isEmpty()) {
                return null;
            } else if (list.size() == 1) return list.get(0); else return list.get(0);
        }
    }

    /**
    * checks if the Conference entity exists
    */
    public boolean existsConference(Conference conference) {
        if (getFirstConference(conference) != null) return true;
        return false;
    }

    public boolean existsConferenceWhereConditionsAre(Conference conference) {
        if (getFirstResultWhereConditionsAre(conference).isEmpty()) return false;
        return true;
    }

    private int countPartialField(Conference conference) {
        int cpt = 0;
        if (conference.getId() != null) {
            cpt++;
        }
        if (conference.getName() != null) {
            cpt++;
        }
        if (conference.getStartDate() != null) {
            cpt++;
        }
        if (conference.getEndDate() != null) {
            cpt++;
        }
        if (conference.getAddressId() != null) {
            cpt++;
        }
        return cpt;
    }

    public List<Conference> partialLoadWithParentConference(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, QueryWhatInit queryWhatInit, Integer nbOfResult, Boolean useCache) {
        Map beanPath = new Hashtable();
        List list = partialLoadWithParentConferenceJPAQueryResult(conferenceWhat, positiveConference, negativeConference, queryWhatInit, beanPath, nbOfResult, useCache);
        if (beanPath.size() == 1) {
            return handlePartialLoadWithParentConferenceWithOneElementInRow(list, beanPath, conferenceWhat, true);
        }
        return handlePartialLoadWithParentConference(list, beanPath, conferenceWhat, true);
    }

    private List partialLoadWithParentConferenceQueryResult(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, QueryWhatInit queryWhatInit, Integer nbOfResult, Boolean useCache) {
        return partialLoadWithParentConferenceJPAQueryResult(conferenceWhat, positiveConference, negativeConference, queryWhatInit, new Hashtable(), nbOfResult, useCache);
    }

    private List partialLoadWithParentConferenceJPAQueryResult(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, QueryWhatInit queryWhatInit, Map beanPath, Integer nbOfResult, Boolean useCache) {
        Query hquery = getPartialLoadWithParentConferenceJPAQuery(conferenceWhat, positiveConference, negativeConference, beanPath, queryWhatInit, nbOfResult);
        return hquery.getResultList();
    }

    /**
    * @returns an JPA Hsql query based on entity Conference and its parents and the maximum number of result
    */
    protected Query getPartialLoadWithParentConferenceJPAQuery(Conference conferenceWhat, Conference positiveConference, Conference negativeConference, Map beanPath, QueryWhatInit queryWhatInit, Integer nbOfResult) {
        Query query = getEntityManager().createQuery(getPartialLoadWithParentConferenceHsqlQuery(conferenceWhat, positiveConference, negativeConference, beanPath, queryWhatInit));
        if (nbOfResult != null) query.setMaxResults(nbOfResult);
        return query;
    }

    private List<Conference> handlePartialLoadWithParentConference(List<Object[]> list, Map<Integer, String> beanPath, Conference conferenceWhat, boolean isJql) {
        try {
            return convertPartialLoadWithParentConference(list, beanPath, conferenceWhat);
        } catch (Exception ex) {
            log.error("Error conversion list from handlePartialLoadWithParentConference, message:" + ex.getMessage());
            return new ArrayList<Conference>();
        }
    }

    private List<Conference> handlePartialLoadWithParentConferenceWithOneElementInRow(List<Object> list, Map<Integer, String> beanPath, Conference conferenceWhat, boolean isJql) {
        try {
            return convertPartialLoadWithParentConferenceWithOneElementInRow(list, beanPath, conferenceWhat);
        } catch (Exception ex) {
            log.error("Error conversion list from handlePartialLoadWithParentConferenceWithOneElementInRow, message:" + ex.getMessage());
            return new ArrayList<Conference>();
        }
    }

    private List<Conference> convertPartialLoadWithParentConference(List<Object[]> list, Map<Integer, String> beanPath, Conference conferenceWhat) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        List<Conference> resultList = new ArrayList<Conference>();
        for (Object[] row : list) {
            Conference conference = cloneConference(conferenceWhat);
            Iterator<Entry<Integer, String>> iter = beanPath.entrySet().iterator();
            while (iter.hasNext()) {
                Entry entry = iter.next();
                populateConference(conference, row[(Integer) entry.getKey()], (String) entry.getValue());
            }
            resultList.add(conference);
        }
        return resultList;
    }

    private List<Conference> convertPartialLoadWithParentConferenceWithOneElementInRow(List<Object> list, Map<Integer, String> beanPath, Conference conferenceWhat) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        List<Conference> resultList = new ArrayList<Conference>();
        for (Object row : list) {
            Conference conference = cloneConference(conferenceWhat);
            Iterator<Entry<Integer, String>> iter = beanPath.entrySet().iterator();
            while (iter.hasNext()) {
                Entry entry = iter.next();
                populateConference(conference, row, (String) entry.getValue());
            }
            resultList.add(conference);
        }
        return resultList;
    }

    public List partialLoadWithParentForBean(Object bean, Conference conferenceWhat, Conference positiveConference, Conference negativeConference) {
        Map beanPath = new Hashtable();
        Query hquery = getPartialLoadWithParentConferenceJPAQuery(conferenceWhat, positiveConference, negativeConference, beanPath, new QuerySelectInit(), null);
        List<Object[]> list = hquery.getResultList();
        return handlePartialLoadWithParentForBean(list, beanPath, bean);
    }

    private List handlePartialLoadWithParentForBean(List<Object[]> list, Map<Integer, String> beanPath, Object bean) {
        try {
            return convertPartialLoadWithParentForBean(list, beanPath, bean);
        } catch (Exception ex) {
            return new ArrayList();
        }
    }

    private List convertPartialLoadWithParentForBean(List<Object[]> list, Map<Integer, String> beanPath, Object bean) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        List resultList = new ArrayList();
        for (Object[] row : list) {
            Object result = getBeanObjectInstance(bean);
            Iterator<Entry<Integer, String>> iter = beanPath.entrySet().iterator();
            while (iter.hasNext()) {
                Entry entry = iter.next();
                populateObject(result, row[(Integer) entry.getKey()], getFieldFromBeanPath((String) entry.getValue()));
            }
            resultList.add(result);
        }
        return resultList;
    }

    private String getFieldFromBeanPath(String beanPath) {
        String result = StringUtils.substringAfterLast(beanPath, ".");
        if (result.equals("")) return beanPath;
        return result;
    }

    /**
	  * 
    * partial on entity and its parents load enables to specify the fields you want to load explicitly
    */
    public String getPartialLoadWithParentConferenceHsqlQuery(Conference conference, Conference positiveConference, Conference negativeConference, Map beanPath, QueryWhatInit queryWhatInit) {
        Hashtable aliasWhatHt = new Hashtable();
        String what = getPartialLoadWithParentConferenceQuery(conference, null, aliasWhatHt, null, null, beanPath, "", queryWhatInit);
        Hashtable aliasWhereHt = new Hashtable();
        String where = getPartialLoadWithParentWhereConferenceQuery(positiveConference, null, aliasWhatHt, aliasWhereHt, null, null);
        String whereHow = reconciliateWherePath(aliasWhatHt, aliasWhereHt);
        String how = reconciliateHowPath(aliasWhatHt, aliasWhereHt);
        String whereConcat = "";
        if (where != null && !where.equals("") && whereHow != null && !whereHow.equals("")) whereConcat = " AND ";
        return what + " FROM " + how + " WHERE " + whereHow + whereConcat + where;
    }

    /**
    * partial on a single entity load enables to specify the fields you want to load explicitly
    */
    public List<Conference> partialLoadConference(Conference conference, Conference positiveConference, Conference negativeConference) {
        Query hquery = getEntityManager().createQuery(getPartialLoadConferenceQuery(conference, positiveConference, negativeConference));
        int countPartialField = countPartialField(conference);
        if (countPartialField == 0) return new ArrayList<Conference>();
        List list = hquery.getResultList();
        Iterator iter = list.iterator();
        List<Conference> returnList = new ArrayList<Conference>();
        while (iter.hasNext()) {
            int index = 0;
            Object[] row;
            if (countPartialField == 1) {
                row = new Object[1];
                row[0] = iter.next();
            } else row = (Object[]) iter.next();
            Conference conferenceResult = new Conference();
            if (conference.getId() != null) {
                conferenceResult.setId((Long) row[index]);
                index++;
            }
            if (conference.getName() != null) {
                conferenceResult.setName((String) row[index]);
                index++;
            }
            if (conference.getStartDate() != null) {
                conferenceResult.setStartDate((Date) row[index]);
                index++;
            }
            if (conference.getEndDate() != null) {
                conferenceResult.setEndDate((Date) row[index]);
                index++;
            }
            if (conference.getAddressId() != null) {
                conferenceResult.setAddressId_((Long) row[index]);
                index++;
            }
            returnList.add(conferenceResult);
        }
        return returnList;
    }

    public static String getPartialLoadWithParentWhereConferenceQuery(Conference conference, Boolean isWhereSet, Hashtable aliasHt, Hashtable aliasWhereHt, String childAlias, String childFKAlias) {
        if (conference == null) return "";
        String alias = null;
        if (aliasWhereHt == null) {
            aliasWhereHt = new Hashtable();
        }
        if (isLookedUp(conference)) {
            alias = getNextAlias(aliasWhereHt, conference);
            aliasWhereHt.put(getAliasKey(alias), getAliasConnection(alias, childAlias, childFKAlias));
        }
        if (isWhereSet == null) isWhereSet = false;
        StringBuffer query = new StringBuffer();
        if (conference.getId() != null) {
            query.append(getQueryBLANK_AND(isWhereSet));
            isWhereSet = true;
            query.append(" " + alias + ".id = " + conference.getId() + " ");
        }
        if (conference.getName() != null) {
            query.append(getQueryBLANK_AND(isWhereSet));
            isWhereSet = true;
            query.append(" " + alias + ".name = '" + conference.getName() + "' ");
        }
        if (conference.getStartDate() != null) {
            query.append(getQueryBLANK_AND(isWhereSet));
            isWhereSet = true;
            query.append(" " + alias + ".startDate = '" + conference.getStartDate() + "' ");
        }
        if (conference.getEndDate() != null) {
            query.append(getQueryBLANK_AND(isWhereSet));
            isWhereSet = true;
            query.append(" " + alias + ".endDate = '" + conference.getEndDate() + "' ");
        }
        if (conference.getAddressId() != null) {
            query.append(getQueryBLANK_AND(isWhereSet));
            isWhereSet = true;
            query.append(" " + alias + ".addressId = " + conference.getAddressId() + " ");
        }
        if (conference.getAddressId() != null) {
            String chunck = net.sf.mp.demo.conference.dao.impl.jpa.conference.AddressExtendedJPAImpl.getPartialLoadWithParentWhereAddressQuery(conference.getAddressId(), isWhereSet, aliasHt, aliasWhereHt, alias, "addressId");
            if (chunck != null && !chunck.equals("")) {
                query.append(chunck);
                isWhereSet = true;
            }
        }
        return query.toString();
    }

    public static String reconciliateWherePath(Hashtable aliasWhatHt, Hashtable aliasWhereHt) {
        StringBuffer sb = new StringBuffer();
        boolean isBlankSet = false;
        aliasWhatHt.putAll(aliasWhereHt);
        Enumeration<String> elements = aliasWhatHt.elements();
        while (elements.hasMoreElements()) {
            String element = elements.nextElement();
            if (!element.equals("")) {
                sb.append(getQueryBLANK_AND(isBlankSet));
                isBlankSet = true;
                sb.append(element);
            }
        }
        return sb.toString();
    }

    public static String reconciliateHowPath(Hashtable aliasWhatHt, Hashtable aliasWhereHt) {
        StringBuffer sb = new StringBuffer();
        boolean isBlankSet = false;
        aliasWhatHt.putAll(aliasWhereHt);
        Enumeration<String> keys = aliasWhatHt.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            sb.append(getQueryBLANK_COMMA(isBlankSet));
            isBlankSet = true;
            sb.append(getAliasKeyDomain(key) + " " + getAliasKeyAlias(key));
        }
        return sb.toString();
    }

    protected static String getRootDomainName(String domainName) {
        return StringUtils.substringBefore(domainName, "_");
    }

    public static String getPartialLoadWithParentConferenceQuery(Conference conference, Boolean isWhereSet, Hashtable aliasHt, String childAlias, String childFKAlias, Map beanPath, String rootPath, QueryWhatInit queryWhatInit) {
        if (conference == null) return "";
        String alias = null;
        if (aliasHt == null) {
            aliasHt = new Hashtable();
        }
        if (isLookedUp(conference)) {
            alias = getNextAlias(aliasHt, conference);
            aliasHt.put(getAliasKey(alias), getAliasConnection(alias, childAlias, childFKAlias));
        }
        if (isWhereSet == null) isWhereSet = false;
        StringBuffer query = new StringBuffer();
        if (conference.getId() != null) {
            query.append(queryWhatInit.getWhatInit(isWhereSet));
            isWhereSet = true;
            beanPath.put(beanPath.size(), rootPath + "id");
            query.append(" " + alias + ".id ");
        }
        if (conference.getName() != null) {
            query.append(queryWhatInit.getWhatInit(isWhereSet));
            isWhereSet = true;
            beanPath.put(beanPath.size(), rootPath + "name");
            query.append(" " + alias + ".name ");
        }
        if (conference.getStartDate() != null) {
            query.append(queryWhatInit.getWhatInit(isWhereSet));
            isWhereSet = true;
            beanPath.put(beanPath.size(), rootPath + "startDate");
            query.append(" " + alias + ".startDate ");
        }
        if (conference.getEndDate() != null) {
            query.append(queryWhatInit.getWhatInit(isWhereSet));
            isWhereSet = true;
            beanPath.put(beanPath.size(), rootPath + "endDate");
            query.append(" " + alias + ".endDate ");
        }
        if (conference.getAddressId() != null) {
            query.append(queryWhatInit.getWhatInit(isWhereSet));
            isWhereSet = true;
            beanPath.put(beanPath.size(), rootPath + "addressId");
            query.append(" " + alias + ".addressId ");
        }
        if (conference.getAddressId() != null) {
            String chunck = net.sf.mp.demo.conference.dao.impl.jpa.conference.AddressExtendedJPAImpl.getPartialLoadWithParentAddressQuery(conference.getAddressId(), isWhereSet, aliasHt, alias, "addressId", beanPath, rootPath + "addressId.", queryWhatInit);
            if (chunck != null && !chunck.equals("")) {
                query.append(chunck);
                isWhereSet = true;
            }
        }
        return query.toString();
    }

    protected static String getAliasConnection(String existingAlias, String childAlias, String childFKAlias) {
        if (childAlias == null) return "";
        return childAlias + "." + childFKAlias + " = " + existingAlias + "." + "id";
    }

    protected static String getAliasKey(String alias) {
        return "Conference|" + alias;
    }

    protected static String getAliasKeyAlias(String aliasKey) {
        return StringUtils.substringAfter(aliasKey, "|");
    }

    protected static String getAliasKeyDomain(String aliasKey) {
        return StringUtils.substringBefore(aliasKey, "|");
    }

    protected static String getNextAlias(Hashtable aliasHt, Conference conference) {
        int cptSameAlias = 0;
        Enumeration<String> keys = aliasHt.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith("conference")) cptSameAlias++;
        }
        if (cptSameAlias == 0) return "conference"; else return "conference_" + cptSameAlias;
    }

    protected static boolean isLookedUp(Conference conference) {
        if (conference == null) return false;
        if (conference.getId() != null) {
            return true;
        }
        if (conference.getName() != null) {
            return true;
        }
        if (conference.getStartDate() != null) {
            return true;
        }
        if (conference.getEndDate() != null) {
            return true;
        }
        if (conference.getAddressId() != null) {
            return true;
        }
        if (conference.getAddressId() != null) {
            return true;
        }
        return false;
    }

    public String getPartialLoadConferenceQuery(Conference conference, Conference positiveConference, Conference negativeConference) {
        boolean isWhereSet = false;
        StringBuffer query = new StringBuffer();
        if (conference.getId() != null) {
            query.append(getQuerySelectComma(isWhereSet));
            isWhereSet = true;
            query.append(" id ");
        }
        if (conference.getName() != null) {
            query.append(getQuerySelectComma(isWhereSet));
            isWhereSet = true;
            query.append(" name ");
        }
        if (conference.getStartDate() != null) {
            query.append(getQuerySelectComma(isWhereSet));
            isWhereSet = true;
            query.append(" startDate ");
        }
        if (conference.getEndDate() != null) {
            query.append(getQuerySelectComma(isWhereSet));
            isWhereSet = true;
            query.append(" endDate ");
        }
        if (conference.getAddressId() != null) {
            query.append(getQuerySelectComma(isWhereSet));
            isWhereSet = true;
            query.append(" addressId ");
        }
        query.append(getConferenceSearchEqualQuery(positiveConference, negativeConference));
        return query.toString();
    }

    public List<Conference> searchPrototypeWithCacheConference(Conference conference) {
        SimpleCache simpleCache = new SimpleCache();
        List<Conference> list = (List<Conference>) simpleCache.get(conference.toString());
        if (list == null) {
            list = searchPrototypeConference(conference);
            simpleCache.put(conference.toString(), list);
        }
        return list;
    }

    public List<Conference> loadGraph(Conference graphMaskWhat, List<Conference> whereMask) {
        return loadGraphOneLevel(graphMaskWhat, whereMask);
    }

    public List<Conference> loadGraphOneLevel(Conference graphMaskWhat, List<Conference> whereMask) {
        graphMaskWhat.setId(graphMaskWhat.longMask__);
        List<Conference> conferences = searchPrototypeConference(whereMask);
        return getLoadGraphOneLevel(graphMaskWhat, conferences);
    }

    private List<Conference> copy(List<Conference> inputs) {
        List<Conference> l = new ArrayList<Conference>();
        for (Conference input : inputs) {
            Conference copy = new Conference();
            copy.copy(input);
            l.add(copy);
        }
        return l;
    }

    private List<Conference> getLoadGraphOneLevel(Conference graphMaskWhat, List<Conference> parents) {
        return loadGraphFromParentKey(graphMaskWhat, parents);
    }

    public List<Conference> loadGraphFromParentKey(Conference graphMaskWhat, List<Conference> parents) {
        parents = copy(parents);
        if (parents == null || parents.isEmpty()) return parents;
        List<String> ids = getPk(parents);
        if (graphMaskWhat.getConferenceFeedbackConferenceViaConferenceId() != null && !graphMaskWhat.getConferenceFeedbackConferenceViaConferenceId().isEmpty()) {
            for (ConferenceFeedback childWhat : graphMaskWhat.getConferenceFeedbackConferenceViaConferenceId()) {
                childWhat.setConferenceId_(graphMaskWhat.longMask__);
                ConferenceFeedbackExtendedJPAImpl conferencefeedbackextendedjpaimpl = new ConferenceFeedbackExtendedJPAImpl();
                List<ConferenceFeedback> children = conferencefeedbackextendedjpaimpl.lookupConferenceFeedback(childWhat, getFkCriteria(" id ", ids), null, getEntityManager());
                reassembleConferenceFeedback(children, parents);
                break;
            }
        }
        if (graphMaskWhat.getConferenceMemberConferenceViaConferenceId() != null && !graphMaskWhat.getConferenceMemberConferenceViaConferenceId().isEmpty()) {
            for (ConferenceMember childWhat : graphMaskWhat.getConferenceMemberConferenceViaConferenceId()) {
                childWhat.setConferenceId_(graphMaskWhat.longMask__);
                ConferenceMemberExtendedJPAImpl conferencememberextendedjpaimpl = new ConferenceMemberExtendedJPAImpl();
                List<ConferenceMember> children = conferencememberextendedjpaimpl.lookupConferenceMember(childWhat, getFkCriteria(" id ", ids), null, getEntityManager());
                reassembleConferenceMember(children, parents);
                break;
            }
        }
        if (graphMaskWhat.getSponsorConferenceViaConferenceId() != null && !graphMaskWhat.getSponsorConferenceViaConferenceId().isEmpty()) {
            for (Sponsor childWhat : graphMaskWhat.getSponsorConferenceViaConferenceId()) {
                childWhat.setConferenceId_(graphMaskWhat.longMask__);
                SponsorExtendedJPAImpl sponsorextendedjpaimpl = new SponsorExtendedJPAImpl();
                List<Sponsor> children = sponsorextendedjpaimpl.lookupSponsor(childWhat, getFkCriteria(" id ", ids), null, getEntityManager());
                reassembleSponsor(children, parents);
                break;
            }
        }
        return parents;
    }

    private void reassembleConferenceFeedback(List<ConferenceFeedback> children, List<Conference> parents) {
        for (ConferenceFeedback child : children) {
            for (Conference parent : parents) {
                if (parent.getId() != null && parent.getId().toString().equals(child.getConferenceId() + "")) {
                    parent.addConferenceFeedbackConferenceViaConferenceId(child);
                    child.setConferenceId(parent);
                    break;
                }
            }
        }
    }

    private void reassembleConferenceMember(List<ConferenceMember> children, List<Conference> parents) {
        for (ConferenceMember child : children) {
            for (Conference parent : parents) {
                if (parent.getId() != null && parent.getId().toString().equals(child.getConferenceId() + "")) {
                    parent.addConferenceMemberConferenceViaConferenceId(child);
                    child.setConferenceId(parent);
                    break;
                }
            }
        }
    }

    private void reassembleSponsor(List<Sponsor> children, List<Conference> parents) {
        for (Sponsor child : children) {
            for (Conference parent : parents) {
                if (parent.getId() != null && parent.getId().toString().equals(child.getConferenceId() + "")) {
                    parent.addSponsorConferenceViaConferenceId(child);
                    child.setConferenceId(parent);
                    break;
                }
            }
        }
    }

    private List<String> getPk(List<Conference> input) {
        List<String> s = new ArrayList<String>();
        for (Conference t : input) {
            s.add(t.getId() + "");
        }
        return s;
    }

    private Criteria getFkCriteria(String field, List<String> ids) {
        Criteria criteria = new Criteria();
        criteria.addCriterion(getInPk(field, ids));
        return criteria;
    }

    private ClauseCriterion getInPk(String field, List<String> ids) {
        InCriterion inCriterion = new InCriterion(field, ids, true);
        return inCriterion;
    }

    public EntityManager getEntityManagerForRecursiveDao() {
        return emForRecursiveDao;
    }

    public void setEntityManagerForRecursiveDao(EntityManager emForRecursiveDao) {
        this.emForRecursiveDao = emForRecursiveDao;
    }

    public void setConferenceFeedbackExtendedJPAImpl(ConferenceFeedbackExtendedJPAImpl conferencefeedbackextendedjpaimpl) {
        this.conferencefeedbackextendedjpaimpl = conferencefeedbackextendedjpaimpl;
    }

    public ConferenceFeedbackExtendedJPAImpl getConferenceFeedbackExtendedJPAImpl() {
        return conferencefeedbackextendedjpaimpl;
    }

    public void setConferenceMemberExtendedJPAImpl(ConferenceMemberExtendedJPAImpl conferencememberextendedjpaimpl) {
        this.conferencememberextendedjpaimpl = conferencememberextendedjpaimpl;
    }

    public ConferenceMemberExtendedJPAImpl getConferenceMemberExtendedJPAImpl() {
        return conferencememberextendedjpaimpl;
    }

    public void setSponsorExtendedJPAImpl(SponsorExtendedJPAImpl sponsorextendedjpaimpl) {
        this.sponsorextendedjpaimpl = sponsorextendedjpaimpl;
    }

    public SponsorExtendedJPAImpl getSponsorExtendedJPAImpl() {
        return sponsorextendedjpaimpl;
    }
}
