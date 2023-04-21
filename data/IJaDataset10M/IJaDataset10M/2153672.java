package com.liferay.portal.service.persistence;

/**
 * <a href="ActivityTrackerPersistence.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface ActivityTrackerPersistence {

    public com.liferay.portal.model.ActivityTracker create(long activityTrackerId);

    public com.liferay.portal.model.ActivityTracker remove(long activityTrackerId) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker remove(com.liferay.portal.model.ActivityTracker activityTracker) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.ActivityTracker update(com.liferay.portal.model.ActivityTracker activityTracker) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.ActivityTracker update(com.liferay.portal.model.ActivityTracker activityTracker, boolean merge) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.ActivityTracker updateImpl(com.liferay.portal.model.ActivityTracker activityTracker, boolean merge) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.ActivityTracker findByPrimaryKey(long activityTrackerId) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker fetchByPrimaryKey(long activityTrackerId) throws com.liferay.portal.SystemException;

    public java.util.List findByGroupId(long groupId) throws com.liferay.portal.SystemException;

    public java.util.List findByGroupId(long groupId, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findByGroupId(long groupId, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.ActivityTracker findByGroupId_First(long groupId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker findByGroupId_Last(long groupId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker[] findByGroupId_PrevAndNext(long activityTrackerId, long groupId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public java.util.List findByCompanyId(long companyId) throws com.liferay.portal.SystemException;

    public java.util.List findByCompanyId(long companyId, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findByCompanyId(long companyId, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.ActivityTracker findByCompanyId_First(long companyId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker findByCompanyId_Last(long companyId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker[] findByCompanyId_PrevAndNext(long activityTrackerId, long companyId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public java.util.List findByUserId(long userId) throws com.liferay.portal.SystemException;

    public java.util.List findByUserId(long userId, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findByUserId(long userId, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.ActivityTracker findByUserId_First(long userId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker findByUserId_Last(long userId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker[] findByUserId_PrevAndNext(long activityTrackerId, long userId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public java.util.List findByReceiverUserId(long receiverUserId) throws com.liferay.portal.SystemException;

    public java.util.List findByReceiverUserId(long receiverUserId, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findByReceiverUserId(long receiverUserId, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.ActivityTracker findByReceiverUserId_First(long receiverUserId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker findByReceiverUserId_Last(long receiverUserId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker[] findByReceiverUserId_PrevAndNext(long activityTrackerId, long receiverUserId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public java.util.List findByC_C(long classNameId, long classPK) throws com.liferay.portal.SystemException;

    public java.util.List findByC_C(long classNameId, long classPK, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findByC_C(long classNameId, long classPK, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.ActivityTracker findByC_C_First(long classNameId, long classPK, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker findByC_C_Last(long classNameId, long classPK, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public com.liferay.portal.model.ActivityTracker[] findByC_C_PrevAndNext(long activityTrackerId, long classNameId, long classPK, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchActivityTrackerException;

    public java.util.List findWithDynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException;

    public java.util.List findWithDynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findAll() throws com.liferay.portal.SystemException;

    public java.util.List findAll(int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List findAll(int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public void removeByGroupId(long groupId) throws com.liferay.portal.SystemException;

    public void removeByCompanyId(long companyId) throws com.liferay.portal.SystemException;

    public void removeByUserId(long userId) throws com.liferay.portal.SystemException;

    public void removeByReceiverUserId(long receiverUserId) throws com.liferay.portal.SystemException;

    public void removeByC_C(long classNameId, long classPK) throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByGroupId(long groupId) throws com.liferay.portal.SystemException;

    public int countByCompanyId(long companyId) throws com.liferay.portal.SystemException;

    public int countByUserId(long userId) throws com.liferay.portal.SystemException;

    public int countByReceiverUserId(long receiverUserId) throws com.liferay.portal.SystemException;

    public int countByC_C(long classNameId, long classPK) throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;
}
