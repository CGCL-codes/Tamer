package com.liferay.portlet.messageboards.service;

/**
 * <a href="MBThreadLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * <code>com.liferay.portlet.messageboards.service.impl.MBThreadLocalServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil
 *
 */
public interface MBThreadLocalService {

    public com.liferay.portlet.messageboards.model.MBThread addMBThread(com.liferay.portlet.messageboards.model.MBThread mbThread) throws com.liferay.portal.SystemException;

    public void deleteMBThread(long threadId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deleteMBThread(com.liferay.portlet.messageboards.model.MBThread mbThread) throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.messageboards.model.MBThread getMBThread(long threadId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public java.util.List<com.liferay.portlet.messageboards.model.MBThread> getMBThreads(int start, int end) throws com.liferay.portal.SystemException;

    public int getMBThreadsCount() throws com.liferay.portal.SystemException;

    public com.liferay.portlet.messageboards.model.MBThread updateMBThread(com.liferay.portlet.messageboards.model.MBThread mbThread) throws com.liferay.portal.SystemException;

    public void deleteThread(long threadId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public void deleteThread(com.liferay.portlet.messageboards.model.MBThread thread) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public void deleteThreads(long categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public int getCategoriesThreadsCount(java.util.List<Long> categoryIds) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.messageboards.model.MBThread> getGroupThreads(long groupId, int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.messageboards.model.MBThread> getGroupThreads(long groupId, long userId, int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.messageboards.model.MBThread> getGroupThreads(long groupId, long userId, boolean subscribed, int start, int end) throws com.liferay.portal.SystemException;

    public int getGroupThreadsCount(long groupId) throws com.liferay.portal.SystemException;

    public int getGroupThreadsCount(long groupId, long userId) throws com.liferay.portal.SystemException;

    public int getGroupThreadsCount(long groupId, long userId, boolean subscribed) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.messageboards.model.MBThread getThread(long threadId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.messageboards.model.MBThread> getThreads(long categoryId, int start, int end) throws com.liferay.portal.SystemException;

    public int getThreadsCount(long categoryId) throws com.liferay.portal.SystemException;

    public boolean hasReadThread(long userId, long threadId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public com.liferay.portlet.messageboards.model.MBThread moveThread(long categoryId, long threadId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public com.liferay.portlet.messageboards.model.MBThread splitThread(long messageId, javax.portlet.PortletPreferences prefs, com.liferay.portal.theme.ThemeDisplay themeDisplay) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public com.liferay.portlet.messageboards.model.MBThread updateThread(long threadId, int viewCount) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;
}
