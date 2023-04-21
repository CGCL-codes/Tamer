package com.liferay.portlet.social.service.impl;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.social.NoSuchActivityException;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.service.base.SocialActivityLocalServiceBaseImpl;
import java.util.Date;
import java.util.List;

/**
 * <a href="SocialActivityLocalServiceImpl.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SocialActivityLocalServiceImpl extends SocialActivityLocalServiceBaseImpl {

    public SocialActivity addActivity(long userId, long groupId, String className, long classPK, int type, String extraData, long receiverUserId) throws PortalException, SystemException {
        return addActivity(userId, groupId, new Date(), className, classPK, type, extraData, receiverUserId);
    }

    public SocialActivity addActivity(long userId, long groupId, Date createDate, String className, long classPK, int type, String extraData, long receiverUserId) throws PortalException, SystemException {
        User user = userPersistence.findByPrimaryKey(userId);
        long classNameId = PortalUtil.getClassNameId(className);
        long activityId = counterLocalService.increment(SocialActivity.class.getName());
        SocialActivity activity = socialActivityPersistence.create(activityId);
        activity.setGroupId(groupId);
        activity.setCompanyId(user.getCompanyId());
        activity.setUserId(user.getUserId());
        activity.setCreateDate(createDate);
        activity.setMirrorActivityId(0);
        activity.setClassNameId(classNameId);
        activity.setClassPK(classPK);
        activity.setType(type);
        activity.setExtraData(extraData);
        activity.setReceiverUserId(receiverUserId);
        socialActivityPersistence.update(activity, false);
        if ((receiverUserId > 0) && (userId != receiverUserId)) {
            long mirrorActivityId = counterLocalService.increment(SocialActivity.class.getName());
            SocialActivity mirrorActivity = socialActivityPersistence.create(mirrorActivityId);
            mirrorActivity.setGroupId(groupId);
            mirrorActivity.setCompanyId(user.getCompanyId());
            mirrorActivity.setUserId(receiverUserId);
            mirrorActivity.setCreateDate(createDate);
            mirrorActivity.setMirrorActivityId(activityId);
            mirrorActivity.setClassNameId(classNameId);
            mirrorActivity.setClassPK(classPK);
            mirrorActivity.setType(type);
            mirrorActivity.setExtraData(extraData);
            mirrorActivity.setReceiverUserId(user.getUserId());
            socialActivityPersistence.update(mirrorActivity, false);
        }
        return activity;
    }

    public SocialActivity addUniqueActivity(long userId, long groupId, String className, long classPK, int type, String extraData, long receiverUserId) throws PortalException, SystemException {
        return addUniqueActivity(userId, groupId, new Date(), className, classPK, type, extraData, receiverUserId);
    }

    public SocialActivity addUniqueActivity(long userId, long groupId, Date createDate, String className, long classPK, int type, String extraData, long receiverUserId) throws PortalException, SystemException {
        long classNameId = PortalUtil.getClassNameId(className);
        SocialActivity socialActivity = socialActivityPersistence.fetchByG_U_CD_C_C_T_R(groupId, userId, createDate, classNameId, classPK, type, receiverUserId);
        if (socialActivity != null) {
            return socialActivity;
        }
        return addActivity(userId, groupId, createDate, className, classPK, type, extraData, receiverUserId);
    }

    public void deleteActivities(String className, long classPK) throws SystemException {
        long classNameId = PortalUtil.getClassNameId(className);
        deleteActivities(classNameId, classPK);
    }

    public void deleteActivities(long classNameId, long classPK) throws SystemException {
        socialActivityPersistence.removeByC_C(classNameId, classPK);
    }

    public void deleteActivity(long activityId) throws PortalException, SystemException {
        SocialActivity activity = socialActivityPersistence.findByPrimaryKey(activityId);
        try {
            socialActivityPersistence.removeByMirrorActivityId(activityId);
        } catch (NoSuchActivityException nsae) {
        }
        socialActivityPersistence.remove(activity);
    }

    public void deleteUserActivities(long userId) throws SystemException {
        List<SocialActivity> activities = socialActivityPersistence.findByUserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
        for (SocialActivity activity : activities) {
            socialActivityPersistence.remove(activity);
        }
        activities = socialActivityPersistence.findByReceiverUserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
        for (SocialActivity activity : activities) {
            socialActivityPersistence.remove(activity);
        }
    }

    public List<SocialActivity> getActivities(String className, int start, int end) throws SystemException {
        long classNameId = PortalUtil.getClassNameId(className);
        return getActivities(classNameId, start, end);
    }

    public List<SocialActivity> getActivities(long classNameId, int start, int end) throws SystemException {
        return socialActivityPersistence.findByClassNameId(classNameId, start, end);
    }

    public List<SocialActivity> getActivities(long mirrorActivityId, String className, long classPK, int start, int end) throws SystemException {
        long classNameId = PortalUtil.getClassNameId(className);
        return getActivities(mirrorActivityId, classNameId, classPK, start, end);
    }

    public List<SocialActivity> getActivities(long mirrorActivityId, long classNameId, long classPK, int start, int end) throws SystemException {
        return socialActivityPersistence.findByM_C_C(mirrorActivityId, classNameId, classPK, start, end);
    }

    public int getActivitiesCount(String className) throws SystemException {
        long classNameId = PortalUtil.getClassNameId(className);
        return getActivitiesCount(classNameId);
    }

    public int getActivitiesCount(long classNameId) throws SystemException {
        return socialActivityPersistence.countByClassNameId(classNameId);
    }

    public int getActivitiesCount(long mirrorActivityId, String className, long classPK) throws SystemException {
        long classNameId = PortalUtil.getClassNameId(className);
        return getActivitiesCount(mirrorActivityId, classNameId, classPK);
    }

    public int getActivitiesCount(long mirrorActivityId, long classNameId, long classPK) throws SystemException {
        return socialActivityPersistence.countByM_C_C(mirrorActivityId, classNameId, classPK);
    }

    public SocialActivity getActivity(long activityId) throws PortalException, SystemException {
        return socialActivityPersistence.findByPrimaryKey(activityId);
    }

    public List<SocialActivity> getGroupActivities(long groupId, int start, int end) throws SystemException {
        return socialActivityFinder.findByGroupId(groupId, start, end);
    }

    public int getGroupActivitiesCount(long groupId) throws SystemException {
        return socialActivityFinder.countByGroupId(groupId);
    }

    public SocialActivity getMirrorActivity(long mirrorActivityId) throws PortalException, SystemException {
        return socialActivityPersistence.findByMirrorActivityId(mirrorActivityId);
    }

    public List<SocialActivity> getOrganizationActivities(long organizationId, int start, int end) throws SystemException {
        return socialActivityFinder.findByOrganizationId(organizationId, start, end);
    }

    public int getOrganizationActivitiesCount(long organizationId) throws SystemException {
        return socialActivityFinder.countByOrganizationId(organizationId);
    }

    public List<SocialActivity> getRelationActivities(long userId, int start, int end) throws SystemException {
        return socialActivityFinder.findByRelation(userId, start, end);
    }

    public List<SocialActivity> getRelationActivities(long userId, int type, int start, int end) throws SystemException {
        return socialActivityFinder.findByRelationType(userId, type, start, end);
    }

    public int getRelationActivitiesCount(long userId) throws SystemException {
        return socialActivityFinder.countByRelation(userId);
    }

    public int getRelationActivitiesCount(long userId, int type) throws SystemException {
        return socialActivityFinder.countByRelationType(userId, type);
    }

    public List<SocialActivity> getUserActivities(long userId, int start, int end) throws SystemException {
        return socialActivityPersistence.findByUserId(userId, start, end);
    }

    public int getUserActivitiesCount(long userId) throws SystemException {
        return socialActivityPersistence.countByUserId(userId);
    }

    public List<SocialActivity> getUserGroupsActivities(long userId, int start, int end) throws SystemException {
        return socialActivityFinder.findByUserGroups(userId, start, end);
    }

    public int getUserGroupsActivitiesCount(long userId) throws SystemException {
        return socialActivityFinder.countByUserGroups(userId);
    }

    public List<SocialActivity> getUserGroupsAndOrganizationsActivities(long userId, int start, int end) throws SystemException {
        return socialActivityFinder.findByUserGroupsAndOrganizations(userId, start, end);
    }

    public int getUserGroupsAndOrganizationsActivitiesCount(long userId) throws SystemException {
        return socialActivityFinder.countByUserGroupsAndOrganizations(userId);
    }

    public List<SocialActivity> getUserOrganizationsActivities(long userId, int start, int end) throws SystemException {
        return socialActivityFinder.findByUserOrganizations(userId, start, end);
    }

    public int getUserOrganizationsActivitiesCount(long userId) throws SystemException {
        return socialActivityFinder.countByUserOrganizations(userId);
    }
}
