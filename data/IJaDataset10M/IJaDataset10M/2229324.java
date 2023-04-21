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
import net.sf.mp.demo.conference.dao.face.conference.PresentationPlaceExtDao;
import net.sf.mp.demo.conference.domain.conference.PresentationPlace;
import net.sf.mp.demo.conference.dao.impl.jpa.conference.PresentationPlaceJPAImpl;
import net.sf.mp.demo.conference.domain.conference.Presentation;
import net.sf.mp.demo.conference.dao.impl.jpa.conference.PresentationExtendedJPAImpl;

/**
 *
 * <p>Title: PresentationPlaceExtendedJPAImpl</p>
 *
 * <p>Description: Interface of a Data access object dealing with PresentationPlaceExtendedJPAImpl
 * persistence. It offers a set of methods which allow for saving,
 * deleting and searching PresentationPlaceExtendedJPAImpl objects</p>
 *
 */
public class PresentationPlaceExtendedJPAImpl extends PresentationPlaceJPAImpl implements PresentationPlaceExtDao {

    private Logger log = Logger.getLogger(this.getClass());

    private SimpleCache simpleCache = new SimpleCache();

    private PresentationExtendedJPAImpl presentationextendedjpaimpl;

    private EntityManager emForRecursiveDao;

    public PresentationPlaceExtendedJPAImpl() {
    }

    /**
     * generic to move after in superclass
     */
    public PresentationPlaceExtendedJPAImpl(EntityManager emForRecursiveDao) {
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
     * Inserts a PresentationPlace entity with cascade of its children
     * @param PresentationPlace presentationPlace
     */
    public void insertPresentationPlaceWithCascade(PresentationPlace presentationPlace) {
        PresentationPlaceExtendedJPAImpl presentationplaceextendedjpaimpl = new PresentationPlaceExtendedJPAImpl(getEntityManager());
        presentationplaceextendedjpaimpl.insertPresentationPlaceWithCascade(presentationplaceextendedjpaimpl.getEntityManagerForRecursiveDao(), presentationPlace);
    }

    public void insertPresentationPlaceWithCascade(EntityManager emForRecursiveDao, PresentationPlace presentationPlace) {
        insertPresentationPlace(emForRecursiveDao, presentationPlace);
        if (!presentationPlace.getPresentationPresentationPlaceViaPresentationPlaceId().isEmpty()) {
            PresentationExtendedJPAImpl presentationextendedjpaimpl = new PresentationExtendedJPAImpl(emForRecursiveDao);
            for (Presentation _presentationPresentationPlaceViaPresentationPlaceId : presentationPlace.getPresentationPresentationPlaceViaPresentationPlaceId()) {
                presentationextendedjpaimpl.insertPresentationWithCascade(emForRecursiveDao, _presentationPresentationPlaceViaPresentationPlaceId);
            }
        }
    }

    /**
     * Inserts a list of PresentationPlace entity with cascade of its children
     * @param List<PresentationPlace> presentationPlaces
     */
    public void insertPresentationPlacesWithCascade(List<PresentationPlace> presentationPlaces) {
        for (PresentationPlace presentationPlace : presentationPlaces) {
            insertPresentationPlaceWithCascade(presentationPlace);
        }
    }

    /**
     * lookup PresentationPlace entity PresentationPlace, criteria and max result number
     */
    public List<PresentationPlace> lookupPresentationPlace(PresentationPlace presentationPlace, Criteria criteria, Integer numberOfResult, EntityManager em) {
        boolean isWhereSet = false;
        StringBuffer query = new StringBuffer();
        query.append("SELECT presentationPlace FROM PresentationPlace presentationPlace ");
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

    public List<PresentationPlace> lookupPresentationPlace(PresentationPlace presentationPlace, Criteria criteria, Integer numberOfResult) {
        return lookupPresentationPlace(presentationPlace, criteria, numberOfResult, getEntityManager());
    }

    public Integer updateNotNullOnlyPresentationPlace(PresentationPlace presentationPlace, Criteria criteria) {
        String queryWhat = getUpdateNotNullOnlyPresentationPlaceQueryChunkPrototype(presentationPlace);
        StringBuffer query = new StringBuffer(queryWhat);
        boolean isWhereSet = false;
        for (Criterion criterion : criteria.getClauseCriterions()) {
            query.append(getQueryWHERE_AND(isWhereSet));
            isWhereSet = true;
            query.append(criterion.getExpression());
        }
        Query jpaQuery = getEntityManager().createQuery(query.toString());
        isWhereSet = false;
        if (presentationPlace.getId() != null) {
            jpaQuery.setParameter("id", presentationPlace.getId());
        }
        if (presentationPlace.getLocation() != null) {
            jpaQuery.setParameter("location", presentationPlace.getLocation());
        }
        if (presentationPlace.getNumberOfSeats() != null) {
            jpaQuery.setParameter("numberOfSeats", presentationPlace.getNumberOfSeats());
        }
        return jpaQuery.executeUpdate();
    }

    public PresentationPlace affectPresentationPlace(PresentationPlace presentationPlace) {
        return referPresentationPlace(presentationPlace, false);
    }

    /**
	 * Assign the first presentationPlace retrieved corresponding to the presentationPlace criteria.
	 * Blank criteria are mapped to null.
	 * If no criteria is found, null is returned.
	 * If there is no presentationPlace corresponding in the database. Then presentationPlace is inserted and returned with its primary key(s). 
	 */
    public PresentationPlace assignPresentationPlace(PresentationPlace presentationPlace) {
        return referPresentationPlace(presentationPlace, true);
    }

    public PresentationPlace referPresentationPlace(PresentationPlace presentationPlace, boolean isAssign) {
        presentationPlace = assignBlankToNull(presentationPlace);
        if (isAllNull(presentationPlace)) return null; else {
            List<PresentationPlace> list = searchPrototypePresentationPlace(presentationPlace);
            if (list.isEmpty()) {
                if (isAssign) insertPresentationPlace(presentationPlace); else return null;
            } else if (list.size() == 1) presentationPlace.copy(list.get(0)); else presentationPlace.copy(list.get(0));
        }
        return presentationPlace;
    }

    public PresentationPlace assignPresentationPlaceUseCache(PresentationPlace presentationPlace) {
        return referPresentationPlaceUseCache(presentationPlace, true);
    }

    public PresentationPlace affectPresentationPlaceUseCache(PresentationPlace presentationPlace) {
        return referPresentationPlaceUseCache(presentationPlace, false);
    }

    public PresentationPlace referPresentationPlaceUseCache(PresentationPlace presentationPlace, boolean isAssign) {
        String key = getCacheKey(null, presentationPlace, null, "assignPresentationPlace");
        PresentationPlace presentationPlaceCache = (PresentationPlace) simpleCache.get(key);
        if (presentationPlaceCache == null) {
            presentationPlaceCache = referPresentationPlace(presentationPlace, isAssign);
            if (key != null) simpleCache.put(key, presentationPlaceCache);
        }
        presentationPlace.copy(presentationPlaceCache);
        return presentationPlaceCache;
    }

    private String getCacheKey(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, String queryKey) {
        StringBuffer sb = new StringBuffer();
        sb.append(queryKey);
        if (presentationPlaceWhat != null) sb.append(presentationPlaceWhat.toStringWithParents());
        if (positivePresentationPlace != null) sb.append(positivePresentationPlace.toStringWithParents());
        if (negativePresentationPlace != null) sb.append(negativePresentationPlace.toStringWithParents());
        return sb.toString();
    }

    public PresentationPlace partialLoadWithParentFirstPresentationPlace(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace) {
        List<PresentationPlace> list = partialLoadWithParentPresentationPlace(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace);
        return (!list.isEmpty()) ? (PresentationPlace) list.get(0) : null;
    }

    public PresentationPlace partialLoadWithParentFirstPresentationPlaceUseCache(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, Boolean useCache) {
        List<PresentationPlace> list = partialLoadWithParentPresentationPlaceUseCache(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, useCache);
        return (!list.isEmpty()) ? (PresentationPlace) list.get(0) : null;
    }

    public PresentationPlace partialLoadWithParentFirstPresentationPlaceUseCacheOnResult(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, Boolean useCache) {
        List<PresentationPlace> list = partialLoadWithParentPresentationPlaceUseCacheOnResult(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, useCache);
        return (!list.isEmpty()) ? (PresentationPlace) list.get(0) : null;
    }

    protected List<PresentationPlace> partialLoadWithParentPresentationPlace(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, Integer nbOfResult, Boolean useCache) {
        return partialLoadWithParentPresentationPlace(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, new QuerySelectInit(), nbOfResult, useCache);
    }

    protected List partialLoadWithParentPresentationPlaceQueryResult(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, Integer nbOfResult, Boolean useCache) {
        return partialLoadWithParentPresentationPlaceQueryResult(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, new QuerySelectInit(), nbOfResult, useCache);
    }

    public List<PresentationPlace> getDistinctPresentationPlace(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace) {
        return partialLoadWithParentPresentationPlace(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, new QuerySelectDistinctInit(), null, false);
    }

    public List<PresentationPlace> partialLoadWithParentPresentationPlace(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace) {
        return partialLoadWithParentPresentationPlace(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, new QuerySelectInit(), null, false);
    }

    public List<PresentationPlace> partialLoadWithParentPresentationPlaceUseCacheOnResult(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, Boolean useCache) {
        String key = getCacheKey(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, "partialLoadWithParentPresentationPlace");
        List<PresentationPlace> list = (List<PresentationPlace>) simpleCache.get(key);
        if (list == null || list.isEmpty()) {
            list = partialLoadWithParentPresentationPlace(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace);
            if (!list.isEmpty()) simpleCache.put(key, list);
        }
        return list;
    }

    public List<PresentationPlace> partialLoadWithParentPresentationPlaceUseCache(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, Boolean useCache) {
        String key = getCacheKey(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, "partialLoadWithParentPresentationPlace");
        List<PresentationPlace> list = (List<PresentationPlace>) simpleCache.get(key);
        if (list == null) {
            list = partialLoadWithParentPresentationPlace(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace);
            simpleCache.put(key, list);
        }
        return list;
    }

    private List<PresentationPlace> handleLoadWithParentPresentationPlace(Map beanPath, List list, PresentationPlace presentationPlaceWhat) {
        return handleLoadWithParentPresentationPlace(beanPath, list, presentationPlaceWhat, true);
    }

    private List<PresentationPlace> handleLoadWithParentPresentationPlace(Map beanPath, List list, PresentationPlace presentationPlaceWhat, boolean isHql) {
        if (beanPath.size() == 1) {
            return handlePartialLoadWithParentPresentationPlaceWithOneElementInRow(list, beanPath, presentationPlaceWhat, isHql);
        }
        return handlePartialLoadWithParentPresentationPlace(list, beanPath, presentationPlaceWhat, isHql);
    }

    protected void populatePresentationPlace(PresentationPlace presentationPlace, Object value, String beanPath) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        BeanUtils.populateBeanObject(presentationPlace, beanPath, value);
    }

    protected void populatePresentationPlaceFromSQL(PresentationPlace presentationPlace, Object value, String beanPath) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        BeanUtils.populateBeanObject(presentationPlace, beanPath, value);
    }

    private PresentationPlace clonePresentationPlace(PresentationPlace presentationPlace) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (presentationPlace == null) return new PresentationPlace();
        return presentationPlace.clone();
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

    public List<PresentationPlace> countDistinct(PresentationPlace whatMask, PresentationPlace whereEqCriteria) {
        return partialLoadWithParentPresentationPlace(whatMask, whereEqCriteria, null, new QuerySelectCountInit("presentationPlace"), null, false);
    }

    public Long count(PresentationPlace whereEqCriteria) {
        List<PresentationPlace> list = partialLoadWithParentPresentationPlace(null, whereEqCriteria, null, new QueryCountInit("presentationPlace"), null, false);
        if (!list.isEmpty()) return list.get(0).getCount__();
        return 0L;
    }

    public PresentationPlace getFirstPresentationPlaceWhereConditionsAre(PresentationPlace presentationPlace) {
        List<PresentationPlace> list = partialLoadWithParentPresentationPlace(getDefaultPresentationPlaceWhat(), presentationPlace, null, 1, false);
        if (list.isEmpty()) {
            return null;
        } else if (list.size() == 1) return list.get(0); else return list.get(0);
    }

    private List getFirstResultWhereConditionsAre(PresentationPlace presentationPlace) {
        return partialLoadWithParentPresentationPlaceQueryResult(getDefaultPresentationPlaceWhat(), presentationPlace, null, 1, false);
    }

    protected PresentationPlace getDefaultPresentationPlaceWhat() {
        PresentationPlace presentationPlace = new PresentationPlace();
        presentationPlace.setId(Long.valueOf("-1"));
        return presentationPlace;
    }

    public PresentationPlace getFirstPresentationPlace(PresentationPlace presentationPlace) {
        if (isAllNull(presentationPlace)) return null; else {
            List<PresentationPlace> list = searchPrototype(presentationPlace, 1);
            if (list.isEmpty()) {
                return null;
            } else if (list.size() == 1) return list.get(0); else return list.get(0);
        }
    }

    /**
    * checks if the PresentationPlace entity exists
    */
    public boolean existsPresentationPlace(PresentationPlace presentationPlace) {
        if (getFirstPresentationPlace(presentationPlace) != null) return true;
        return false;
    }

    public boolean existsPresentationPlaceWhereConditionsAre(PresentationPlace presentationPlace) {
        if (getFirstResultWhereConditionsAre(presentationPlace).isEmpty()) return false;
        return true;
    }

    private int countPartialField(PresentationPlace presentationPlace) {
        int cpt = 0;
        if (presentationPlace.getId() != null) {
            cpt++;
        }
        if (presentationPlace.getLocation() != null) {
            cpt++;
        }
        if (presentationPlace.getNumberOfSeats() != null) {
            cpt++;
        }
        return cpt;
    }

    public List<PresentationPlace> partialLoadWithParentPresentationPlace(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, QueryWhatInit queryWhatInit, Integer nbOfResult, Boolean useCache) {
        Map beanPath = new Hashtable();
        List list = partialLoadWithParentPresentationPlaceJPAQueryResult(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, queryWhatInit, beanPath, nbOfResult, useCache);
        if (beanPath.size() == 1) {
            return handlePartialLoadWithParentPresentationPlaceWithOneElementInRow(list, beanPath, presentationPlaceWhat, true);
        }
        return handlePartialLoadWithParentPresentationPlace(list, beanPath, presentationPlaceWhat, true);
    }

    private List partialLoadWithParentPresentationPlaceQueryResult(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, QueryWhatInit queryWhatInit, Integer nbOfResult, Boolean useCache) {
        return partialLoadWithParentPresentationPlaceJPAQueryResult(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, queryWhatInit, new Hashtable(), nbOfResult, useCache);
    }

    private List partialLoadWithParentPresentationPlaceJPAQueryResult(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, QueryWhatInit queryWhatInit, Map beanPath, Integer nbOfResult, Boolean useCache) {
        Query hquery = getPartialLoadWithParentPresentationPlaceJPAQuery(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, beanPath, queryWhatInit, nbOfResult);
        return hquery.getResultList();
    }

    /**
    * @returns an JPA Hsql query based on entity PresentationPlace and its parents and the maximum number of result
    */
    protected Query getPartialLoadWithParentPresentationPlaceJPAQuery(PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, Map beanPath, QueryWhatInit queryWhatInit, Integer nbOfResult) {
        Query query = getEntityManager().createQuery(getPartialLoadWithParentPresentationPlaceHsqlQuery(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, beanPath, queryWhatInit));
        if (nbOfResult != null) query.setMaxResults(nbOfResult);
        return query;
    }

    private List<PresentationPlace> handlePartialLoadWithParentPresentationPlace(List<Object[]> list, Map<Integer, String> beanPath, PresentationPlace presentationPlaceWhat, boolean isJql) {
        try {
            return convertPartialLoadWithParentPresentationPlace(list, beanPath, presentationPlaceWhat);
        } catch (Exception ex) {
            log.error("Error conversion list from handlePartialLoadWithParentPresentationPlace, message:" + ex.getMessage());
            return new ArrayList<PresentationPlace>();
        }
    }

    private List<PresentationPlace> handlePartialLoadWithParentPresentationPlaceWithOneElementInRow(List<Object> list, Map<Integer, String> beanPath, PresentationPlace presentationPlaceWhat, boolean isJql) {
        try {
            return convertPartialLoadWithParentPresentationPlaceWithOneElementInRow(list, beanPath, presentationPlaceWhat);
        } catch (Exception ex) {
            log.error("Error conversion list from handlePartialLoadWithParentPresentationPlaceWithOneElementInRow, message:" + ex.getMessage());
            return new ArrayList<PresentationPlace>();
        }
    }

    private List<PresentationPlace> convertPartialLoadWithParentPresentationPlace(List<Object[]> list, Map<Integer, String> beanPath, PresentationPlace presentationPlaceWhat) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        List<PresentationPlace> resultList = new ArrayList<PresentationPlace>();
        for (Object[] row : list) {
            PresentationPlace presentationPlace = clonePresentationPlace(presentationPlaceWhat);
            Iterator<Entry<Integer, String>> iter = beanPath.entrySet().iterator();
            while (iter.hasNext()) {
                Entry entry = iter.next();
                populatePresentationPlace(presentationPlace, row[(Integer) entry.getKey()], (String) entry.getValue());
            }
            resultList.add(presentationPlace);
        }
        return resultList;
    }

    private List<PresentationPlace> convertPartialLoadWithParentPresentationPlaceWithOneElementInRow(List<Object> list, Map<Integer, String> beanPath, PresentationPlace presentationPlaceWhat) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        List<PresentationPlace> resultList = new ArrayList<PresentationPlace>();
        for (Object row : list) {
            PresentationPlace presentationPlace = clonePresentationPlace(presentationPlaceWhat);
            Iterator<Entry<Integer, String>> iter = beanPath.entrySet().iterator();
            while (iter.hasNext()) {
                Entry entry = iter.next();
                populatePresentationPlace(presentationPlace, row, (String) entry.getValue());
            }
            resultList.add(presentationPlace);
        }
        return resultList;
    }

    public List partialLoadWithParentForBean(Object bean, PresentationPlace presentationPlaceWhat, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace) {
        Map beanPath = new Hashtable();
        Query hquery = getPartialLoadWithParentPresentationPlaceJPAQuery(presentationPlaceWhat, positivePresentationPlace, negativePresentationPlace, beanPath, new QuerySelectInit(), null);
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
    public String getPartialLoadWithParentPresentationPlaceHsqlQuery(PresentationPlace presentationPlace, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace, Map beanPath, QueryWhatInit queryWhatInit) {
        Hashtable aliasWhatHt = new Hashtable();
        String what = getPartialLoadWithParentPresentationPlaceQuery(presentationPlace, null, aliasWhatHt, null, null, beanPath, "", queryWhatInit);
        Hashtable aliasWhereHt = new Hashtable();
        String where = getPartialLoadWithParentWherePresentationPlaceQuery(positivePresentationPlace, null, aliasWhatHt, aliasWhereHt, null, null);
        String whereHow = reconciliateWherePath(aliasWhatHt, aliasWhereHt);
        String how = reconciliateHowPath(aliasWhatHt, aliasWhereHt);
        String whereConcat = "";
        if (where != null && !where.equals("") && whereHow != null && !whereHow.equals("")) whereConcat = " AND ";
        return what + " FROM " + how + " WHERE " + whereHow + whereConcat + where;
    }

    /**
    * partial on a single entity load enables to specify the fields you want to load explicitly
    */
    public List<PresentationPlace> partialLoadPresentationPlace(PresentationPlace presentationPlace, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace) {
        Query hquery = getEntityManager().createQuery(getPartialLoadPresentationPlaceQuery(presentationPlace, positivePresentationPlace, negativePresentationPlace));
        int countPartialField = countPartialField(presentationPlace);
        if (countPartialField == 0) return new ArrayList<PresentationPlace>();
        List list = hquery.getResultList();
        Iterator iter = list.iterator();
        List<PresentationPlace> returnList = new ArrayList<PresentationPlace>();
        while (iter.hasNext()) {
            int index = 0;
            Object[] row;
            if (countPartialField == 1) {
                row = new Object[1];
                row[0] = iter.next();
            } else row = (Object[]) iter.next();
            PresentationPlace presentationPlaceResult = new PresentationPlace();
            if (presentationPlace.getId() != null) {
                presentationPlaceResult.setId((Long) row[index]);
                index++;
            }
            if (presentationPlace.getLocation() != null) {
                presentationPlaceResult.setLocation((String) row[index]);
                index++;
            }
            if (presentationPlace.getNumberOfSeats() != null) {
                presentationPlaceResult.setNumberOfSeats((Integer) row[index]);
                index++;
            }
            returnList.add(presentationPlaceResult);
        }
        return returnList;
    }

    public static String getPartialLoadWithParentWherePresentationPlaceQuery(PresentationPlace presentationPlace, Boolean isWhereSet, Hashtable aliasHt, Hashtable aliasWhereHt, String childAlias, String childFKAlias) {
        if (presentationPlace == null) return "";
        String alias = null;
        if (aliasWhereHt == null) {
            aliasWhereHt = new Hashtable();
        }
        if (isLookedUp(presentationPlace)) {
            alias = getNextAlias(aliasWhereHt, presentationPlace);
            aliasWhereHt.put(getAliasKey(alias), getAliasConnection(alias, childAlias, childFKAlias));
        }
        if (isWhereSet == null) isWhereSet = false;
        StringBuffer query = new StringBuffer();
        if (presentationPlace.getId() != null) {
            query.append(getQueryBLANK_AND(isWhereSet));
            isWhereSet = true;
            query.append(" " + alias + ".id = " + presentationPlace.getId() + " ");
        }
        if (presentationPlace.getLocation() != null) {
            query.append(getQueryBLANK_AND(isWhereSet));
            isWhereSet = true;
            query.append(" " + alias + ".location = '" + presentationPlace.getLocation() + "' ");
        }
        if (presentationPlace.getNumberOfSeats() != null) {
            query.append(getQueryBLANK_AND(isWhereSet));
            isWhereSet = true;
            query.append(" " + alias + ".numberOfSeats = " + presentationPlace.getNumberOfSeats() + " ");
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

    public static String getPartialLoadWithParentPresentationPlaceQuery(PresentationPlace presentationPlace, Boolean isWhereSet, Hashtable aliasHt, String childAlias, String childFKAlias, Map beanPath, String rootPath, QueryWhatInit queryWhatInit) {
        if (presentationPlace == null) return "";
        String alias = null;
        if (aliasHt == null) {
            aliasHt = new Hashtable();
        }
        if (isLookedUp(presentationPlace)) {
            alias = getNextAlias(aliasHt, presentationPlace);
            aliasHt.put(getAliasKey(alias), getAliasConnection(alias, childAlias, childFKAlias));
        }
        if (isWhereSet == null) isWhereSet = false;
        StringBuffer query = new StringBuffer();
        if (presentationPlace.getId() != null) {
            query.append(queryWhatInit.getWhatInit(isWhereSet));
            isWhereSet = true;
            beanPath.put(beanPath.size(), rootPath + "id");
            query.append(" " + alias + ".id ");
        }
        if (presentationPlace.getLocation() != null) {
            query.append(queryWhatInit.getWhatInit(isWhereSet));
            isWhereSet = true;
            beanPath.put(beanPath.size(), rootPath + "location");
            query.append(" " + alias + ".location ");
        }
        if (presentationPlace.getNumberOfSeats() != null) {
            query.append(queryWhatInit.getWhatInit(isWhereSet));
            isWhereSet = true;
            beanPath.put(beanPath.size(), rootPath + "numberOfSeats");
            query.append(" " + alias + ".numberOfSeats ");
        }
        return query.toString();
    }

    protected static String getAliasConnection(String existingAlias, String childAlias, String childFKAlias) {
        if (childAlias == null) return "";
        return childAlias + "." + childFKAlias + " = " + existingAlias + "." + "id";
    }

    protected static String getAliasKey(String alias) {
        return "PresentationPlace|" + alias;
    }

    protected static String getAliasKeyAlias(String aliasKey) {
        return StringUtils.substringAfter(aliasKey, "|");
    }

    protected static String getAliasKeyDomain(String aliasKey) {
        return StringUtils.substringBefore(aliasKey, "|");
    }

    protected static String getNextAlias(Hashtable aliasHt, PresentationPlace presentationPlace) {
        int cptSameAlias = 0;
        Enumeration<String> keys = aliasHt.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith("presentationPlace")) cptSameAlias++;
        }
        if (cptSameAlias == 0) return "presentationPlace"; else return "presentationPlace_" + cptSameAlias;
    }

    protected static boolean isLookedUp(PresentationPlace presentationPlace) {
        if (presentationPlace == null) return false;
        if (presentationPlace.getId() != null) {
            return true;
        }
        if (presentationPlace.getLocation() != null) {
            return true;
        }
        if (presentationPlace.getNumberOfSeats() != null) {
            return true;
        }
        return false;
    }

    public String getPartialLoadPresentationPlaceQuery(PresentationPlace presentationPlace, PresentationPlace positivePresentationPlace, PresentationPlace negativePresentationPlace) {
        boolean isWhereSet = false;
        StringBuffer query = new StringBuffer();
        if (presentationPlace.getId() != null) {
            query.append(getQuerySelectComma(isWhereSet));
            isWhereSet = true;
            query.append(" id ");
        }
        if (presentationPlace.getLocation() != null) {
            query.append(getQuerySelectComma(isWhereSet));
            isWhereSet = true;
            query.append(" location ");
        }
        if (presentationPlace.getNumberOfSeats() != null) {
            query.append(getQuerySelectComma(isWhereSet));
            isWhereSet = true;
            query.append(" numberOfSeats ");
        }
        query.append(getPresentationPlaceSearchEqualQuery(positivePresentationPlace, negativePresentationPlace));
        return query.toString();
    }

    public List<PresentationPlace> searchPrototypeWithCachePresentationPlace(PresentationPlace presentationPlace) {
        SimpleCache simpleCache = new SimpleCache();
        List<PresentationPlace> list = (List<PresentationPlace>) simpleCache.get(presentationPlace.toString());
        if (list == null) {
            list = searchPrototypePresentationPlace(presentationPlace);
            simpleCache.put(presentationPlace.toString(), list);
        }
        return list;
    }

    public List<PresentationPlace> loadGraph(PresentationPlace graphMaskWhat, List<PresentationPlace> whereMask) {
        return loadGraphOneLevel(graphMaskWhat, whereMask);
    }

    public List<PresentationPlace> loadGraphOneLevel(PresentationPlace graphMaskWhat, List<PresentationPlace> whereMask) {
        graphMaskWhat.setId(graphMaskWhat.longMask__);
        List<PresentationPlace> presentationPlaces = searchPrototypePresentationPlace(whereMask);
        return getLoadGraphOneLevel(graphMaskWhat, presentationPlaces);
    }

    private List<PresentationPlace> copy(List<PresentationPlace> inputs) {
        List<PresentationPlace> l = new ArrayList<PresentationPlace>();
        for (PresentationPlace input : inputs) {
            PresentationPlace copy = new PresentationPlace();
            copy.copy(input);
            l.add(copy);
        }
        return l;
    }

    private List<PresentationPlace> getLoadGraphOneLevel(PresentationPlace graphMaskWhat, List<PresentationPlace> parents) {
        return loadGraphFromParentKey(graphMaskWhat, parents);
    }

    public List<PresentationPlace> loadGraphFromParentKey(PresentationPlace graphMaskWhat, List<PresentationPlace> parents) {
        parents = copy(parents);
        if (parents == null || parents.isEmpty()) return parents;
        List<String> ids = getPk(parents);
        if (graphMaskWhat.getPresentationPresentationPlaceViaPresentationPlaceId() != null && !graphMaskWhat.getPresentationPresentationPlaceViaPresentationPlaceId().isEmpty()) {
            for (Presentation childWhat : graphMaskWhat.getPresentationPresentationPlaceViaPresentationPlaceId()) {
                childWhat.setPresentationPlaceId_(graphMaskWhat.longMask__);
                PresentationExtendedJPAImpl presentationextendedjpaimpl = new PresentationExtendedJPAImpl();
                List<Presentation> children = presentationextendedjpaimpl.lookupPresentation(childWhat, getFkCriteria(" id ", ids), null, getEntityManager());
                reassemblePresentation(children, parents);
                break;
            }
        }
        return parents;
    }

    private void reassemblePresentation(List<Presentation> children, List<PresentationPlace> parents) {
        for (Presentation child : children) {
            for (PresentationPlace parent : parents) {
                if (parent.getId() != null && parent.getId().toString().equals(child.getPresentationPlaceId() + "")) {
                    parent.addPresentationPresentationPlaceViaPresentationPlaceId(child);
                    child.setPresentationPlaceId(parent);
                    break;
                }
            }
        }
    }

    private List<String> getPk(List<PresentationPlace> input) {
        List<String> s = new ArrayList<String>();
        for (PresentationPlace t : input) {
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

    public void setPresentationExtendedJPAImpl(PresentationExtendedJPAImpl presentationextendedjpaimpl) {
        this.presentationextendedjpaimpl = presentationextendedjpaimpl;
    }

    public PresentationExtendedJPAImpl getPresentationExtendedJPAImpl() {
        return presentationextendedjpaimpl;
    }
}
