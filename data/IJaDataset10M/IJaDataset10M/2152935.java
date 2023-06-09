package com.league.schedule.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.service.PersistedModelLocalService;

/**
 * The interface for the league local service.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Guolin Wang
 * @see LeagueLocalServiceUtil
 * @see com.league.schedule.service.base.LeagueLocalServiceBaseImpl
 * @see com.league.schedule.service.impl.LeagueLocalServiceImpl
 * @generated
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor = { PortalException.class, SystemException.class })
public interface LeagueLocalService extends PersistedModelLocalService {

    /**
	* Adds the league to the database. Also notifies the appropriate model listeners.
	*
	* @param league the league
	* @return the league that was added
	* @throws SystemException if a system exception occurred
	*/
    public com.league.schedule.model.League addLeague(com.league.schedule.model.League league) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Creates a new league with the primary key. Does not add the league to the database.
	*
	* @param league_id the primary key for the new league
	* @return the new league
	*/
    public com.league.schedule.model.League createLeague(long league_id);

    /**
	* Deletes the league with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param league_id the primary key of the league
	* @throws PortalException if a league with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
    public void deleteLeague(long league_id) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException;

    /**
	* Deletes the league from the database. Also notifies the appropriate model listeners.
	*
	* @param league the league
	* @throws SystemException if a system exception occurred
	*/
    public void deleteLeague(com.league.schedule.model.League league) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	* @throws SystemException if a system exception occurred
	*/
    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	* @throws SystemException if a system exception occurred
	*/
    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	* @throws SystemException if a system exception occurred
	*/
    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end, com.liferay.portal.kernel.util.OrderByComparator orderByComparator) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
    public long dynamicQueryCount(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.kernel.exception.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.league.schedule.model.League fetchLeague(long league_id) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the league with the primary key.
	*
	* @param league_id the primary key of the league
	* @return the league
	* @throws PortalException if a league with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.league.schedule.model.League getLeague(long league_id) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.liferay.portal.model.PersistedModel getPersistedModel(java.io.Serializable primaryKeyObj) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns a range of all the leagues.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of leagues
	* @param end the upper bound of the range of leagues (not inclusive)
	* @return the range of leagues
	* @throws SystemException if a system exception occurred
	*/
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.league.schedule.model.League> getLeagues(int start, int end) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the number of leagues.
	*
	* @return the number of leagues
	* @throws SystemException if a system exception occurred
	*/
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getLeaguesCount() throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Updates the league in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param league the league
	* @return the league that was updated
	* @throws SystemException if a system exception occurred
	*/
    public com.league.schedule.model.League updateLeague(com.league.schedule.model.League league) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Updates the league in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param league the league
	* @param merge whether to merge the league with the current session. See {@link com.liferay.portal.service.persistence.BatchSession#update(com.liferay.portal.kernel.dao.orm.Session, com.liferay.portal.model.BaseModel, boolean)} for an explanation.
	* @return the league that was updated
	* @throws SystemException if a system exception occurred
	*/
    public com.league.schedule.model.League updateLeague(com.league.schedule.model.League league, boolean merge) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
    public java.lang.String getBeanIdentifier();

    /**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
    public void setBeanIdentifier(java.lang.String beanIdentifier);
}
