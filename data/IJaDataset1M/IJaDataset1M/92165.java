package com.liferay.portlet.journal.service.persistence;

/**
 * <a href="JournalFeedPersistence.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface JournalFeedPersistence {

    public com.liferay.portlet.journal.model.JournalFeed create(long id);

    public com.liferay.portlet.journal.model.JournalFeed remove(long id) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed remove(com.liferay.portlet.journal.model.JournalFeed journalFeed) throws com.liferay.portal.SystemException;

    /**
	 * @deprecated Use <code>update(JournalFeed journalFeed, boolean merge)</code>.
	 */
    public com.liferay.portlet.journal.model.JournalFeed update(com.liferay.portlet.journal.model.JournalFeed journalFeed) throws com.liferay.portal.SystemException;

    /**
	 * Add, update, or merge, the entity. This method also calls the model
	 * listeners to trigger the proper events associated with adding, deleting,
	 * or updating an entity.
	 *
	 * @param        journalFeed the entity to add, update, or merge
	 * @param        merge boolean value for whether to merge the entity. The
	 *                default value is false. Setting merge to true is more
	 *                expensive and should only be true when journalFeed is
	 *                transient. See LEP-5473 for a detailed discussion of this
	 *                method.
	 * @return        true if the portlet can be displayed via Ajax
	 */
    public com.liferay.portlet.journal.model.JournalFeed update(com.liferay.portlet.journal.model.JournalFeed journalFeed, boolean merge) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.journal.model.JournalFeed updateImpl(com.liferay.portlet.journal.model.JournalFeed journalFeed, boolean merge) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.journal.model.JournalFeed findByPrimaryKey(long id) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed fetchByPrimaryKey(long id) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.journal.model.JournalFeed> findByUuid(java.lang.String uuid) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.journal.model.JournalFeed> findByUuid(java.lang.String uuid, int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.journal.model.JournalFeed> findByUuid(java.lang.String uuid, int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.journal.model.JournalFeed findByUuid_First(java.lang.String uuid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed findByUuid_Last(java.lang.String uuid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed[] findByUuid_PrevAndNext(long id, java.lang.String uuid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed findByUUID_G(java.lang.String uuid, long groupId) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed fetchByUUID_G(java.lang.String uuid, long groupId) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.journal.model.JournalFeed> findByGroupId(long groupId) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.journal.model.JournalFeed> findByGroupId(long groupId, int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.journal.model.JournalFeed> findByGroupId(long groupId, int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.journal.model.JournalFeed findByGroupId_First(long groupId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed findByGroupId_Last(long groupId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed[] findByGroupId_PrevAndNext(long id, long groupId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed findByG_F(long groupId, java.lang.String feedId) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public com.liferay.portlet.journal.model.JournalFeed fetchByG_F(long groupId, java.lang.String feedId) throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.journal.model.JournalFeed> findAll() throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.journal.model.JournalFeed> findAll(int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.liferay.portlet.journal.model.JournalFeed> findAll(int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public void removeByUuid(java.lang.String uuid) throws com.liferay.portal.SystemException;

    public void removeByUUID_G(java.lang.String uuid, long groupId) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public void removeByGroupId(long groupId) throws com.liferay.portal.SystemException;

    public void removeByG_F(long groupId, java.lang.String feedId) throws com.liferay.portal.SystemException, com.liferay.portlet.journal.NoSuchFeedException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByUuid(java.lang.String uuid) throws com.liferay.portal.SystemException;

    public int countByUUID_G(java.lang.String uuid, long groupId) throws com.liferay.portal.SystemException;

    public int countByGroupId(long groupId) throws com.liferay.portal.SystemException;

    public int countByG_F(long groupId, java.lang.String feedId) throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;

    public void registerListener(com.liferay.portal.model.ModelListener listener);

    public void unregisterListener(com.liferay.portal.model.ModelListener listener);
}
