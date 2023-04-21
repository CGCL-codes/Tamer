package com.league.schedule.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ClassLoaderProxy;
import com.liferay.portal.kernel.util.MethodCache;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * The utility for the schedule local service. This utility wraps {@link com.league.schedule.service.impl.ScheduleLocalServiceImpl} and is the primary access point for service operations in application layer code running on the local server.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Guolin Wang
 * @see ScheduleLocalService
 * @see com.league.schedule.service.base.ScheduleLocalServiceBaseImpl
 * @see com.league.schedule.service.impl.ScheduleLocalServiceImpl
 * @generated
 */
public class ScheduleLocalServiceUtil {

    /**
	* Adds the schedule to the database. Also notifies the appropriate model listeners.
	*
	* @param schedule the schedule
	* @return the schedule that was added
	* @throws SystemException if a system exception occurred
	*/
    public static com.league.schedule.model.Schedule addSchedule(com.league.schedule.model.Schedule schedule) throws com.liferay.portal.kernel.exception.SystemException {
        return getService().addSchedule(schedule);
    }

    /**
	* Creates a new schedule with the primary key. Does not add the schedule to the database.
	*
	* @param schedule_id the primary key for the new schedule
	* @return the new schedule
	*/
    public static com.league.schedule.model.Schedule createSchedule(long schedule_id) {
        return getService().createSchedule(schedule_id);
    }

    /**
	* Deletes the schedule with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param schedule_id the primary key of the schedule
	* @throws PortalException if a schedule with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
    public static void deleteSchedule(long schedule_id) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException {
        getService().deleteSchedule(schedule_id);
    }

    /**
	* Deletes the schedule from the database. Also notifies the appropriate model listeners.
	*
	* @param schedule the schedule
	* @throws SystemException if a system exception occurred
	*/
    public static void deleteSchedule(com.league.schedule.model.Schedule schedule) throws com.liferay.portal.kernel.exception.SystemException {
        getService().deleteSchedule(schedule);
    }

    /**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	* @throws SystemException if a system exception occurred
	*/
    @SuppressWarnings("rawtypes")
    public static java.util.List dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.kernel.exception.SystemException {
        return getService().dynamicQuery(dynamicQuery);
    }

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
    public static java.util.List dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.kernel.exception.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end);
    }

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
    public static java.util.List dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end, com.liferay.portal.kernel.util.OrderByComparator orderByComparator) throws com.liferay.portal.kernel.exception.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end, orderByComparator);
    }

    /**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
    public static long dynamicQueryCount(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.kernel.exception.SystemException {
        return getService().dynamicQueryCount(dynamicQuery);
    }

    public static com.league.schedule.model.Schedule fetchSchedule(long schedule_id) throws com.liferay.portal.kernel.exception.SystemException {
        return getService().fetchSchedule(schedule_id);
    }

    /**
	* Returns the schedule with the primary key.
	*
	* @param schedule_id the primary key of the schedule
	* @return the schedule
	* @throws PortalException if a schedule with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
    public static com.league.schedule.model.Schedule getSchedule(long schedule_id) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException {
        return getService().getSchedule(schedule_id);
    }

    public static com.liferay.portal.model.PersistedModel getPersistedModel(java.io.Serializable primaryKeyObj) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException {
        return getService().getPersistedModel(primaryKeyObj);
    }

    /**
	* Returns a range of all the schedules.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of schedules
	* @param end the upper bound of the range of schedules (not inclusive)
	* @return the range of schedules
	* @throws SystemException if a system exception occurred
	*/
    public static java.util.List<com.league.schedule.model.Schedule> getSchedules(int start, int end) throws com.liferay.portal.kernel.exception.SystemException {
        return getService().getSchedules(start, end);
    }

    /**
	* Returns the number of schedules.
	*
	* @return the number of schedules
	* @throws SystemException if a system exception occurred
	*/
    public static int getSchedulesCount() throws com.liferay.portal.kernel.exception.SystemException {
        return getService().getSchedulesCount();
    }

    /**
	* Updates the schedule in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param schedule the schedule
	* @return the schedule that was updated
	* @throws SystemException if a system exception occurred
	*/
    public static com.league.schedule.model.Schedule updateSchedule(com.league.schedule.model.Schedule schedule) throws com.liferay.portal.kernel.exception.SystemException {
        return getService().updateSchedule(schedule);
    }

    /**
	* Updates the schedule in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param schedule the schedule
	* @param merge whether to merge the schedule with the current session. See {@link com.liferay.portal.service.persistence.BatchSession#update(com.liferay.portal.kernel.dao.orm.Session, com.liferay.portal.model.BaseModel, boolean)} for an explanation.
	* @return the schedule that was updated
	* @throws SystemException if a system exception occurred
	*/
    public static com.league.schedule.model.Schedule updateSchedule(com.league.schedule.model.Schedule schedule, boolean merge) throws com.liferay.portal.kernel.exception.SystemException {
        return getService().updateSchedule(schedule, merge);
    }

    /**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
    public static java.lang.String getBeanIdentifier() {
        return getService().getBeanIdentifier();
    }

    /**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
    public static void setBeanIdentifier(java.lang.String beanIdentifier) {
        getService().setBeanIdentifier(beanIdentifier);
    }

    public static java.util.List<com.league.schedule.model.Schedule> findAllSchedule() throws com.liferay.portal.kernel.exception.SystemException {
        return getService().findAllSchedule();
    }

    public static void clearService() {
        _service = null;
    }

    public static ScheduleLocalService getService() {
        if (_service == null) {
            Object object = PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(), ScheduleLocalService.class.getName());
            ClassLoader portletClassLoader = (ClassLoader) PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(), "portletClassLoader");
            ClassLoaderProxy classLoaderProxy = new ClassLoaderProxy(object, ScheduleLocalService.class.getName(), portletClassLoader);
            _service = new ScheduleLocalServiceClp(classLoaderProxy);
            ClpSerializer.setClassLoader(portletClassLoader);
            ReferenceRegistry.registerReference(ScheduleLocalServiceUtil.class, "_service");
            MethodCache.remove(ScheduleLocalService.class);
        }
        return _service;
    }

    public void setService(ScheduleLocalService service) {
        MethodCache.remove(ScheduleLocalService.class);
        _service = service;
        ReferenceRegistry.registerReference(ScheduleLocalServiceUtil.class, "_service");
        MethodCache.remove(ScheduleLocalService.class);
    }

    private static ScheduleLocalService _service;
}
