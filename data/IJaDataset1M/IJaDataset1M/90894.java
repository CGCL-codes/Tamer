package com.liferay.portlet.bookmarks.service.persistence;

/**
 * <a href="BookmarksFolderUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class BookmarksFolderUtil {

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder create(long folderId) {
        return getPersistence().create(folderId);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder remove(long folderId) throws com.liferay.portal.SystemException, com.liferay.portlet.bookmarks.NoSuchFolderException {
        return getPersistence().remove(folderId);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder remove(com.liferay.portlet.bookmarks.model.BookmarksFolder bookmarksFolder) throws com.liferay.portal.SystemException {
        return getPersistence().remove(bookmarksFolder);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder update(com.liferay.portlet.bookmarks.model.BookmarksFolder bookmarksFolder) throws com.liferay.portal.SystemException {
        return getPersistence().update(bookmarksFolder);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder update(com.liferay.portlet.bookmarks.model.BookmarksFolder bookmarksFolder, boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(bookmarksFolder, merge);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder updateImpl(com.liferay.portlet.bookmarks.model.BookmarksFolder bookmarksFolder, boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(bookmarksFolder, merge);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder findByPrimaryKey(long folderId) throws com.liferay.portal.SystemException, com.liferay.portlet.bookmarks.NoSuchFolderException {
        return getPersistence().findByPrimaryKey(folderId);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder fetchByPrimaryKey(long folderId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(folderId);
    }

    public static java.util.List findByGroupId(long groupId) throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId);
    }

    public static java.util.List findByGroupId(long groupId, int begin, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, begin, end);
    }

    public static java.util.List findByGroupId(long groupId, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, begin, end, obc);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder findByGroupId_First(long groupId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.bookmarks.NoSuchFolderException {
        return getPersistence().findByGroupId_First(groupId, obc);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder findByGroupId_Last(long groupId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.bookmarks.NoSuchFolderException {
        return getPersistence().findByGroupId_Last(groupId, obc);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder[] findByGroupId_PrevAndNext(long folderId, long groupId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.bookmarks.NoSuchFolderException {
        return getPersistence().findByGroupId_PrevAndNext(folderId, groupId, obc);
    }

    public static java.util.List findByG_P(long groupId, long parentFolderId) throws com.liferay.portal.SystemException {
        return getPersistence().findByG_P(groupId, parentFolderId);
    }

    public static java.util.List findByG_P(long groupId, long parentFolderId, int begin, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByG_P(groupId, parentFolderId, begin, end);
    }

    public static java.util.List findByG_P(long groupId, long parentFolderId, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByG_P(groupId, parentFolderId, begin, end, obc);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder findByG_P_First(long groupId, long parentFolderId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.bookmarks.NoSuchFolderException {
        return getPersistence().findByG_P_First(groupId, parentFolderId, obc);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder findByG_P_Last(long groupId, long parentFolderId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.bookmarks.NoSuchFolderException {
        return getPersistence().findByG_P_Last(groupId, parentFolderId, obc);
    }

    public static com.liferay.portlet.bookmarks.model.BookmarksFolder[] findByG_P_PrevAndNext(long folderId, long groupId, long parentFolderId, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portlet.bookmarks.NoSuchFolderException {
        return getPersistence().findByG_P_PrevAndNext(folderId, groupId, parentFolderId, obc);
    }

    public static java.util.List findWithDynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException {
        return getPersistence().findWithDynamicQuery(queryInitializer);
    }

    public static java.util.List findWithDynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findWithDynamicQuery(queryInitializer, begin, end);
    }

    public static java.util.List findAll() throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List findAll(int begin, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(begin, end);
    }

    public static java.util.List findAll(int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(begin, end, obc);
    }

    public static void removeByGroupId(long groupId) throws com.liferay.portal.SystemException {
        getPersistence().removeByGroupId(groupId);
    }

    public static void removeByG_P(long groupId, long parentFolderId) throws com.liferay.portal.SystemException {
        getPersistence().removeByG_P(groupId, parentFolderId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByGroupId(long groupId) throws com.liferay.portal.SystemException {
        return getPersistence().countByGroupId(groupId);
    }

    public static int countByG_P(long groupId, long parentFolderId) throws com.liferay.portal.SystemException {
        return getPersistence().countByG_P(groupId, parentFolderId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static BookmarksFolderPersistence getPersistence() {
        return _getUtil()._persistence;
    }

    public void setPersistence(BookmarksFolderPersistence persistence) {
        _persistence = persistence;
    }

    private static BookmarksFolderUtil _getUtil() {
        if (_util == null) {
            _util = (BookmarksFolderUtil) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_UTIL);
        }
        return _util;
    }

    private static final String _UTIL = BookmarksFolderUtil.class.getName();

    private static BookmarksFolderUtil _util;

    private BookmarksFolderPersistence _persistence;
}
