package com.liferay.portlet.social.service.persistence;

/**
 * <a href="SocialRequestUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SocialRequestUtil {

    public static com.liferay.portlet.social.model.SocialRequest create(long requestId) {
        return getPersistence().create(requestId);
    }

    public static com.liferay.portlet.social.model.SocialRequest remove(long requestId) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().remove(requestId);
    }

    public static com.liferay.portlet.social.model.SocialRequest remove(com.liferay.portlet.social.model.SocialRequest socialRequest) throws com.liferay.portal.SystemException {
        return getPersistence().remove(socialRequest);
    }

    /**
	 * @deprecated Use <code>update(SocialRequest socialRequest, boolean merge)</code>.
	 */
    public static com.liferay.portlet.social.model.SocialRequest update(com.liferay.portlet.social.model.SocialRequest socialRequest) throws com.liferay.portal.SystemException {
        return getPersistence().update(socialRequest);
    }

    /**
	 * Add, update, or merge, the entity. This method also calls the model
	 * listeners to trigger the proper events associated with adding, deleting,
	 * or updating an entity.
	 *
	 * @param        socialRequest the entity to add, update, or merge
	 * @param        merge boolean value for whether to merge the entity. The
	 *                default value is false. Setting merge to true is more
	 *                expensive and should only be true when socialRequest is
	 *                transient. See LEP-5473 for a detailed discussion of this
	 *                method.
	 * @return        true if the portlet can be displayed via Ajax
	 */
    public static com.liferay.portlet.social.model.SocialRequest update(com.liferay.portlet.social.model.SocialRequest socialRequest, boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(socialRequest, merge);
    }

    public static com.liferay.portlet.social.model.SocialRequest updateImpl(com.liferay.portlet.social.model.SocialRequest socialRequest, boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(socialRequest, merge);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByPrimaryKey(long requestId) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByPrimaryKey(requestId);
    }

    public static com.liferay.portlet.social.model.SocialRequest fetchByPrimaryKey(long requestId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(requestId);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByUuid(java.lang.String uuid) throws com.liferay.portal.SystemException {
        return getPersistence().findByUuid(uuid);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByUuid(java.lang.String uuid, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByUuid(uuid, start, end);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByUuid(java.lang.String uuid, int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByUuid(uuid, start, end, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByUuid_First(java.lang.String uuid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByUuid_First(uuid, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByUuid_Last(java.lang.String uuid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByUuid_Last(uuid, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest[] findByUuid_PrevAndNext(long requestId, java.lang.String uuid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByUuid_PrevAndNext(requestId, uuid, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByUUID_G(java.lang.String uuid, long groupId) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByUUID_G(uuid, groupId);
    }

    public static com.liferay.portlet.social.model.SocialRequest fetchByUUID_G(java.lang.String uuid, long groupId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByUUID_G(uuid, groupId);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByCompanyId(long companyId) throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByCompanyId(long companyId, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByCompanyId(long companyId, int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByCompanyId_First(long companyId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByCompanyId_First(companyId, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByCompanyId_Last(long companyId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByCompanyId_Last(companyId, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest[] findByCompanyId_PrevAndNext(long requestId, long companyId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByCompanyId_PrevAndNext(requestId, companyId, obc);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByUserId(long userId) throws com.liferay.portal.SystemException {
        return getPersistence().findByUserId(userId);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByUserId(long userId, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByUserId(userId, start, end);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByUserId(long userId, int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByUserId(userId, start, end, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByUserId_First(long userId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByUserId_First(userId, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByUserId_Last(long userId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByUserId_Last(userId, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest[] findByUserId_PrevAndNext(long requestId, long userId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByUserId_PrevAndNext(requestId, userId, obc);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByReceiverUserId(long receiverUserId) throws com.liferay.portal.SystemException {
        return getPersistence().findByReceiverUserId(receiverUserId);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByReceiverUserId(long receiverUserId, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByReceiverUserId(receiverUserId, start, end);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByReceiverUserId(long receiverUserId, int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByReceiverUserId(receiverUserId, start, end, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByReceiverUserId_First(long receiverUserId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByReceiverUserId_First(receiverUserId, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByReceiverUserId_Last(long receiverUserId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByReceiverUserId_Last(receiverUserId, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest[] findByReceiverUserId_PrevAndNext(long requestId, long receiverUserId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByReceiverUserId_PrevAndNext(requestId, receiverUserId, obc);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByU_S(long userId, int status) throws com.liferay.portal.SystemException {
        return getPersistence().findByU_S(userId, status);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByU_S(long userId, int status, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByU_S(userId, status, start, end);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByU_S(long userId, int status, int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByU_S(userId, status, start, end, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByU_S_First(long userId, int status, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByU_S_First(userId, status, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByU_S_Last(long userId, int status, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByU_S_Last(userId, status, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest[] findByU_S_PrevAndNext(long requestId, long userId, int status, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByU_S_PrevAndNext(requestId, userId, status, obc);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByR_S(long receiverUserId, int status) throws com.liferay.portal.SystemException {
        return getPersistence().findByR_S(receiverUserId, status);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByR_S(long receiverUserId, int status, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByR_S(receiverUserId, status, start, end);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByR_S(long receiverUserId, int status, int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByR_S(receiverUserId, status, start, end, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByR_S_First(long receiverUserId, int status, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByR_S_First(receiverUserId, status, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByR_S_Last(long receiverUserId, int status, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByR_S_Last(receiverUserId, status, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest[] findByR_S_PrevAndNext(long requestId, long receiverUserId, int status, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByR_S_PrevAndNext(requestId, receiverUserId, status, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByU_C_C_T_R(long userId, long classNameId, long classPK, int type, long receiverUserId) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByU_C_C_T_R(userId, classNameId, classPK, type, receiverUserId);
    }

    public static com.liferay.portlet.social.model.SocialRequest fetchByU_C_C_T_R(long userId, long classNameId, long classPK, int type, long receiverUserId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByU_C_C_T_R(userId, classNameId, classPK, type, receiverUserId);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByU_C_C_T_S(long userId, long classNameId, long classPK, int type, int status) throws com.liferay.portal.SystemException {
        return getPersistence().findByU_C_C_T_S(userId, classNameId, classPK, type, status);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByU_C_C_T_S(long userId, long classNameId, long classPK, int type, int status, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByU_C_C_T_S(userId, classNameId, classPK, type, status, start, end);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findByU_C_C_T_S(long userId, long classNameId, long classPK, int type, int status, int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByU_C_C_T_S(userId, classNameId, classPK, type, status, start, end, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByU_C_C_T_S_First(long userId, long classNameId, long classPK, int type, int status, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByU_C_C_T_S_First(userId, classNameId, classPK, type, status, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByU_C_C_T_S_Last(long userId, long classNameId, long classPK, int type, int status, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByU_C_C_T_S_Last(userId, classNameId, classPK, type, status, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest[] findByU_C_C_T_S_PrevAndNext(long requestId, long userId, long classNameId, long classPK, int type, int status, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByU_C_C_T_S_PrevAndNext(requestId, userId, classNameId, classPK, type, status, obc);
    }

    public static com.liferay.portlet.social.model.SocialRequest findByU_C_C_T_R_S(long userId, long classNameId, long classPK, int type, long receiverUserId, int status) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        return getPersistence().findByU_C_C_T_R_S(userId, classNameId, classPK, type, receiverUserId, status);
    }

    public static com.liferay.portlet.social.model.SocialRequest fetchByU_C_C_T_R_S(long userId, long classNameId, long classPK, int type, long receiverUserId, int status) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByU_C_C_T_R_S(userId, classNameId, classPK, type, receiverUserId, status);
    }

    public static java.util.List<Object> findWithDynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException {
        return getPersistence().findWithDynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> findWithDynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findAll() throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findAll(int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.liferay.portlet.social.model.SocialRequest> findAll(int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByUuid(java.lang.String uuid) throws com.liferay.portal.SystemException {
        getPersistence().removeByUuid(uuid);
    }

    public static void removeByUUID_G(java.lang.String uuid, long groupId) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        getPersistence().removeByUUID_G(uuid, groupId);
    }

    public static void removeByCompanyId(long companyId) throws com.liferay.portal.SystemException {
        getPersistence().removeByCompanyId(companyId);
    }

    public static void removeByUserId(long userId) throws com.liferay.portal.SystemException {
        getPersistence().removeByUserId(userId);
    }

    public static void removeByReceiverUserId(long receiverUserId) throws com.liferay.portal.SystemException {
        getPersistence().removeByReceiverUserId(receiverUserId);
    }

    public static void removeByU_S(long userId, int status) throws com.liferay.portal.SystemException {
        getPersistence().removeByU_S(userId, status);
    }

    public static void removeByR_S(long receiverUserId, int status) throws com.liferay.portal.SystemException {
        getPersistence().removeByR_S(receiverUserId, status);
    }

    public static void removeByU_C_C_T_R(long userId, long classNameId, long classPK, int type, long receiverUserId) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        getPersistence().removeByU_C_C_T_R(userId, classNameId, classPK, type, receiverUserId);
    }

    public static void removeByU_C_C_T_S(long userId, long classNameId, long classPK, int type, int status) throws com.liferay.portal.SystemException {
        getPersistence().removeByU_C_C_T_S(userId, classNameId, classPK, type, status);
    }

    public static void removeByU_C_C_T_R_S(long userId, long classNameId, long classPK, int type, long receiverUserId, int status) throws com.liferay.portal.SystemException, com.liferay.portlet.social.NoSuchRequestException {
        getPersistence().removeByU_C_C_T_R_S(userId, classNameId, classPK, type, receiverUserId, status);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByUuid(java.lang.String uuid) throws com.liferay.portal.SystemException {
        return getPersistence().countByUuid(uuid);
    }

    public static int countByUUID_G(java.lang.String uuid, long groupId) throws com.liferay.portal.SystemException {
        return getPersistence().countByUUID_G(uuid, groupId);
    }

    public static int countByCompanyId(long companyId) throws com.liferay.portal.SystemException {
        return getPersistence().countByCompanyId(companyId);
    }

    public static int countByUserId(long userId) throws com.liferay.portal.SystemException {
        return getPersistence().countByUserId(userId);
    }

    public static int countByReceiverUserId(long receiverUserId) throws com.liferay.portal.SystemException {
        return getPersistence().countByReceiverUserId(receiverUserId);
    }

    public static int countByU_S(long userId, int status) throws com.liferay.portal.SystemException {
        return getPersistence().countByU_S(userId, status);
    }

    public static int countByR_S(long receiverUserId, int status) throws com.liferay.portal.SystemException {
        return getPersistence().countByR_S(receiverUserId, status);
    }

    public static int countByU_C_C_T_R(long userId, long classNameId, long classPK, int type, long receiverUserId) throws com.liferay.portal.SystemException {
        return getPersistence().countByU_C_C_T_R(userId, classNameId, classPK, type, receiverUserId);
    }

    public static int countByU_C_C_T_S(long userId, long classNameId, long classPK, int type, int status) throws com.liferay.portal.SystemException {
        return getPersistence().countByU_C_C_T_S(userId, classNameId, classPK, type, status);
    }

    public static int countByU_C_C_T_R_S(long userId, long classNameId, long classPK, int type, long receiverUserId, int status) throws com.liferay.portal.SystemException {
        return getPersistence().countByU_C_C_T_R_S(userId, classNameId, classPK, type, receiverUserId, status);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static void registerListener(com.liferay.portal.model.ModelListener listener) {
        getPersistence().registerListener(listener);
    }

    public static void unregisterListener(com.liferay.portal.model.ModelListener listener) {
        getPersistence().unregisterListener(listener);
    }

    public static SocialRequestPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(SocialRequestPersistence persistence) {
        _persistence = persistence;
    }

    private static SocialRequestPersistence _persistence;
}
