package com.kwoksys.biz.system.dao;

import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.connection.database.QueryHelper;
import com.kwoksys.framework.exception.DatabaseException;
import com.kwoksys.framework.exception.ObjectNotFoundException;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * BookmarkDao.
 */
public class BookmarkDao extends BaseDao {

    /**
     * Return all Bookmarks for a particular object.
     */
    public List<Bookmark> getBookmarks(QueryBits query, Integer objectTypeId, Integer objectId) throws DatabaseException {
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.selectBookmarkListQuery(query));
        queryHelper.addInputInt(objectTypeId);
        queryHelper.addInputInt(objectId);
        try {
            List<Bookmark> list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                Bookmark bookmark = new Bookmark();
                bookmark.setId(rs.getInt("bookmark_id"));
                bookmark.setName(StringUtils.replaceNull(rs.getString("bookmark_name")));
                bookmark.setDescription(StringUtils.replaceNull(rs.getString("bookmark_description")));
                bookmark.setPath(StringUtils.replaceNull(rs.getString("bookmark_path")));
                list.add(bookmark);
            }
            return list;
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
    }

    /**
     * Return a Bookmark.
     */
    public Bookmark getBookmark(Integer objectTypeId, Integer objectId, Integer bookmarkId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.selectBookmarkDetailQuery());
        queryHelper.addInputInt(objectTypeId);
        queryHelper.addInputInt(objectId);
        queryHelper.addInputInt(bookmarkId);
        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                Bookmark bookmark = new Bookmark(objectTypeId, objectId);
                bookmark.setId(rs.getInt("bookmark_id"));
                bookmark.setName(StringUtils.replaceNull(rs.getString("bookmark_name")));
                bookmark.setDescription(StringUtils.replaceNull(rs.getString("bookmark_description")));
                bookmark.setPath(StringUtils.replaceNull(rs.getString("bookmark_path")));
                return bookmark;
            }
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        throw new ObjectNotFoundException();
    }

    /**
     * Add a Bookmark.
     *
     * @param bookmark
     * @return ..
     */
    public ActionMessages add(Bookmark bookmark) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.insertBookmarkQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(bookmark.getName());
        queryHelper.addInputStringConvertNull(bookmark.getPath());
        queryHelper.addInputStringConvertNull(bookmark.getDescription());
        queryHelper.addInputInt(bookmark.getObjectTypeId());
        queryHelper.addInputInt(bookmark.getObjectId());
        queryHelper.addInputInt(bookmark.getCreatorId());
        try {
            CallableStatement cstmt = queryHelper.executeProcedure(conn);
            bookmark.setId(cstmt.getInt(1));
        } catch (Exception e) {
            queryHelper.handleError(errors, e);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        return errors;
    }

    /**
     * Update a Bookmark.
     *
     * @param bookmark
     * @return ..
     */
    public ActionMessages update(Bookmark bookmark) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.updateBookmarkQuery());
        queryHelper.addInputInt(bookmark.getId());
        queryHelper.addInputStringConvertNull(bookmark.getName());
        queryHelper.addInputStringConvertNull(bookmark.getPath());
        queryHelper.addInputStringConvertNull(bookmark.getDescription());
        queryHelper.addInputInt(bookmark.getObjectTypeId());
        queryHelper.addInputInt(bookmark.getObjectId());
        queryHelper.addInputInt(bookmark.getModifierId());
        return executeProcedure(queryHelper);
    }

    /**
     * Delete a Bookmark.
     *
     * @param bookmark
     * @return ..
     */
    public ActionMessages delete(Bookmark bookmark) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.deleteBookmarkQuery());
        queryHelper.addInputInt(bookmark.getObjectTypeId());
        queryHelper.addInputInt(bookmark.getObjectId());
        queryHelper.addInputInt(bookmark.getId());
        return executeProcedure(queryHelper);
    }
}
