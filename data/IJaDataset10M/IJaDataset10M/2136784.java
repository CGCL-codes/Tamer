package net.sf.mp.demo.conference.dao.face.statistics;

import net.sf.mp.demo.conference.domain.statistics.StatMbByRole;
import java.util.List;
import net.sf.minuteProject.architecture.filter.data.Criteria;
import net.sf.minuteProject.architecture.bsla.dao.face.DataAccessObject;

/**
 *
 * <p>Title: StatMbByRoleExtDao</p>
 *
 * <p>Description: Interface of a Data access object dealing with StatMbByRoleExtDao
 * persistence. It offers extended DAO functionalities</p>
 *
 */
public interface StatMbByRoleExtDao extends DataAccessObject {

    /**
     * Inserts a StatMbByRole entity with cascade of its children
     * @param StatMbByRole statMbByRole
     */
    public void insertStatMbByRoleWithCascade(StatMbByRole statMbByRole);

    /**
     * Inserts a list of StatMbByRole entity with cascade of its children
     * @param List<StatMbByRole> statMbByRoles
     */
    public void insertStatMbByRolesWithCascade(List<StatMbByRole> statMbByRoles);

    /**
     * lookup StatMbByRole entity StatMbByRole, criteria and max result number
     */
    public List<StatMbByRole> lookupStatMbByRole(StatMbByRole statMbByRole, Criteria criteria, Integer numberOfResult);

    public Integer updateNotNullOnlyStatMbByRole(StatMbByRole statMbByRole, Criteria criteria);

    /**
	 * Affect the first statMbByRole retrieved corresponding to the statMbByRole criteria.
	 * Blank criteria are mapped to null.
	 * If no criteria is found, null is returned.
	 */
    public StatMbByRole affectStatMbByRole(StatMbByRole statMbByRole);

    public StatMbByRole affectStatMbByRoleUseCache(StatMbByRole statMbByRole);

    /**
	 * Assign the first statMbByRole retrieved corresponding to the statMbByRole criteria.
	 * Blank criteria are mapped to null.
	 * If no criteria is found, null is returned.
	 * If there is no statMbByRole corresponding in the database. Then statMbByRole is inserted and returned with its primary key(s). 
	 */
    public StatMbByRole assignStatMbByRole(StatMbByRole statMbByRole);

    public StatMbByRole assignStatMbByRoleUseCache(StatMbByRole statMbByRole);

    /**
    * return the first StatMbByRole entity found
    */
    public StatMbByRole getFirstStatMbByRole(StatMbByRole statMbByRole);

    /**
    * checks if the StatMbByRole entity exists
    */
    public boolean existsStatMbByRole(StatMbByRole statMbByRole);

    public boolean existsStatMbByRoleWhereConditionsAre(StatMbByRole statMbByRole);

    /**
    * partial load enables to specify the fields you want to load explicitly
    */
    public List<StatMbByRole> partialLoadStatMbByRole(StatMbByRole statMbByRole, StatMbByRole positiveStatMbByRole, StatMbByRole negativeStatMbByRole);

    /**
    * partial load with parent entities
    * variation (list, first, distinct decorator)
    * variation2 (with cache)
    */
    public List<StatMbByRole> partialLoadWithParentStatMbByRole(StatMbByRole statMbByRole, StatMbByRole positiveStatMbByRole, StatMbByRole negativeStatMbByRole);

    public List<StatMbByRole> partialLoadWithParentStatMbByRoleUseCache(StatMbByRole statMbByRole, StatMbByRole positiveStatMbByRole, StatMbByRole negativeStatMbByRole, Boolean useCache);

    public List<StatMbByRole> partialLoadWithParentStatMbByRoleUseCacheOnResult(StatMbByRole statMbByRole, StatMbByRole positiveStatMbByRole, StatMbByRole negativeStatMbByRole, Boolean useCache);

    /**
    * variation first
    */
    public StatMbByRole partialLoadWithParentFirstStatMbByRole(StatMbByRole statMbByRoleWhat, StatMbByRole positiveStatMbByRole, StatMbByRole negativeStatMbByRole);

    public StatMbByRole partialLoadWithParentFirstStatMbByRoleUseCache(StatMbByRole statMbByRoleWhat, StatMbByRole positiveStatMbByRole, StatMbByRole negativeStatMbByRole, Boolean useCache);

    public StatMbByRole partialLoadWithParentFirstStatMbByRoleUseCacheOnResult(StatMbByRole statMbByRoleWhat, StatMbByRole positiveStatMbByRole, StatMbByRole negativeStatMbByRole, Boolean useCache);

    /**
    * variation distinct
    */
    public List<StatMbByRole> getDistinctStatMbByRole(StatMbByRole statMbByRoleWhat, StatMbByRole positiveStatMbByRole, StatMbByRole negativeStatMbByRole);

    public List partialLoadWithParentForBean(Object bean, StatMbByRole statMbByRole, StatMbByRole positiveStatMbByRole, StatMbByRole negativeStatMbByRole);

    /**
    * search on prototype with cache
    */
    public List<StatMbByRole> searchPrototypeWithCacheStatMbByRole(StatMbByRole statMbByRole);

    /**
     * Searches a list of distinct StatMbByRole entity based on a StatMbByRole mask and a list of StatMbByRole containing StatMbByRole matching criteria
     * @param StatMbByRole statMbByRole
     * @param List<StatMbByRole> statMbByRoles
     * @return List<StatMbByRole>
     */
    public List<StatMbByRole> searchDistinctPrototypeStatMbByRole(StatMbByRole statMbByRoleMask, List<StatMbByRole> statMbByRoles);

    public List<StatMbByRole> countDistinct(StatMbByRole whatMask, StatMbByRole whereEqCriteria);

    public Long count(StatMbByRole whereEqCriteria);

    public List<StatMbByRole> loadGraph(StatMbByRole graphMaskWhat, List<StatMbByRole> whereMask);

    public List<StatMbByRole> loadGraphFromParentKey(StatMbByRole graphMaskWhat, List<StatMbByRole> parents);

    /**
     * generic to move after in superclass
     */
    public List<Object[]> getSQLQueryResult(String query);
}
