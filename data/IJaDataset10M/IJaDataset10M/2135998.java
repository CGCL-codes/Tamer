package net.sf.mp.demo.conference.dao.face.conference;

import net.sf.mp.demo.conference.domain.conference.ConferenceMember;
import java.util.List;
import net.sf.minuteProject.architecture.filter.data.Criteria;
import net.sf.minuteProject.architecture.bsla.dao.face.DataAccessObject;

/**
 *
 * <p>Title: ConferenceMemberExtDao</p>
 *
 * <p>Description: Interface of a Data access object dealing with ConferenceMemberExtDao
 * persistence. It offers extended DAO functionalities</p>
 *
 */
public interface ConferenceMemberExtDao extends DataAccessObject {

    /**
     * Inserts a ConferenceMember entity with cascade of its children
     * @param ConferenceMember conferenceMember
     */
    public void insertConferenceMemberWithCascade(ConferenceMember conferenceMember);

    /**
     * Inserts a list of ConferenceMember entity with cascade of its children
     * @param List<ConferenceMember> conferenceMembers
     */
    public void insertConferenceMembersWithCascade(List<ConferenceMember> conferenceMembers);

    /**
     * lookup ConferenceMember entity ConferenceMember, criteria and max result number
     */
    public List<ConferenceMember> lookupConferenceMember(ConferenceMember conferenceMember, Criteria criteria, Integer numberOfResult);

    public Integer updateNotNullOnlyConferenceMember(ConferenceMember conferenceMember, Criteria criteria);

    /**
	 * Affect the first conferenceMember retrieved corresponding to the conferenceMember criteria.
	 * Blank criteria are mapped to null.
	 * If no criteria is found, null is returned.
	 */
    public ConferenceMember affectConferenceMember(ConferenceMember conferenceMember);

    public ConferenceMember affectConferenceMemberUseCache(ConferenceMember conferenceMember);

    /**
	 * Assign the first conferenceMember retrieved corresponding to the conferenceMember criteria.
	 * Blank criteria are mapped to null.
	 * If no criteria is found, null is returned.
	 * If there is no conferenceMember corresponding in the database. Then conferenceMember is inserted and returned with its primary key(s). 
	 */
    public ConferenceMember assignConferenceMember(ConferenceMember conferenceMember);

    public ConferenceMember assignConferenceMemberUseCache(ConferenceMember conferenceMember);

    /**
    * return the first ConferenceMember entity found
    */
    public ConferenceMember getFirstConferenceMember(ConferenceMember conferenceMember);

    /**
    * checks if the ConferenceMember entity exists
    */
    public boolean existsConferenceMember(ConferenceMember conferenceMember);

    public boolean existsConferenceMemberWhereConditionsAre(ConferenceMember conferenceMember);

    /**
    * partial load enables to specify the fields you want to load explicitly
    */
    public List<ConferenceMember> partialLoadConferenceMember(ConferenceMember conferenceMember, ConferenceMember positiveConferenceMember, ConferenceMember negativeConferenceMember);

    /**
    * partial load with parent entities
    * variation (list, first, distinct decorator)
    * variation2 (with cache)
    */
    public List<ConferenceMember> partialLoadWithParentConferenceMember(ConferenceMember conferenceMember, ConferenceMember positiveConferenceMember, ConferenceMember negativeConferenceMember);

    public List<ConferenceMember> partialLoadWithParentConferenceMemberUseCache(ConferenceMember conferenceMember, ConferenceMember positiveConferenceMember, ConferenceMember negativeConferenceMember, Boolean useCache);

    public List<ConferenceMember> partialLoadWithParentConferenceMemberUseCacheOnResult(ConferenceMember conferenceMember, ConferenceMember positiveConferenceMember, ConferenceMember negativeConferenceMember, Boolean useCache);

    /**
    * variation first
    */
    public ConferenceMember partialLoadWithParentFirstConferenceMember(ConferenceMember conferenceMemberWhat, ConferenceMember positiveConferenceMember, ConferenceMember negativeConferenceMember);

    public ConferenceMember partialLoadWithParentFirstConferenceMemberUseCache(ConferenceMember conferenceMemberWhat, ConferenceMember positiveConferenceMember, ConferenceMember negativeConferenceMember, Boolean useCache);

    public ConferenceMember partialLoadWithParentFirstConferenceMemberUseCacheOnResult(ConferenceMember conferenceMemberWhat, ConferenceMember positiveConferenceMember, ConferenceMember negativeConferenceMember, Boolean useCache);

    /**
    * variation distinct
    */
    public List<ConferenceMember> getDistinctConferenceMember(ConferenceMember conferenceMemberWhat, ConferenceMember positiveConferenceMember, ConferenceMember negativeConferenceMember);

    public List partialLoadWithParentForBean(Object bean, ConferenceMember conferenceMember, ConferenceMember positiveConferenceMember, ConferenceMember negativeConferenceMember);

    /**
    * search on prototype with cache
    */
    public List<ConferenceMember> searchPrototypeWithCacheConferenceMember(ConferenceMember conferenceMember);

    /**
     * Searches a list of distinct ConferenceMember entity based on a ConferenceMember mask and a list of ConferenceMember containing ConferenceMember matching criteria
     * @param ConferenceMember conferenceMember
     * @param List<ConferenceMember> conferenceMembers
     * @return List<ConferenceMember>
     */
    public List<ConferenceMember> searchDistinctPrototypeConferenceMember(ConferenceMember conferenceMemberMask, List<ConferenceMember> conferenceMembers);

    public List<ConferenceMember> countDistinct(ConferenceMember whatMask, ConferenceMember whereEqCriteria);

    public Long count(ConferenceMember whereEqCriteria);

    public List<ConferenceMember> loadGraph(ConferenceMember graphMaskWhat, List<ConferenceMember> whereMask);

    public List<ConferenceMember> loadGraphFromParentKey(ConferenceMember graphMaskWhat, List<ConferenceMember> parents);

    /**
     * generic to move after in superclass
     */
    public List<Object[]> getSQLQueryResult(String query);
}
