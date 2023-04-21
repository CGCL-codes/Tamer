package com.nyandu.weboffice.file.database.mysql;

import com.nyandu.weboffice.common.database.BaseDAO;
import com.nyandu.weboffice.common.database.DAOException;
import com.nyandu.weboffice.common.database.DBDataSource;
import com.nyandu.weboffice.common.database.mysql.MySQLDataSource;
import com.nyandu.weboffice.common.util.Util;
import com.nyandu.weboffice.common.util.Consts;
import com.nyandu.weboffice.file.business.File;
import com.nyandu.weboffice.file.database.IFileDAO;
import com.nyandu.weboffice.file.util.Security;
import com.nyandu.weboffice.site.business.User;
import com.nyandu.weboffice.site.business.AuthorizablePermissionsValues;
import com.nyandu.weboffice.site.business.Group;
import com.nyandu.weboffice.site.database.mysql.MySQLUserDAO;
import com.nyandu.weboffice.site.database.mysql.MySQLIGroupDAO;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * 
 *  The contents of this file are subject to the Nandu Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.nyandu.com
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Initial Developer of the Original Code is User.
 * Portions created by User are Copyleft (C) www.nyandu.com. 
 * All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * User: alvaro
 * Date: 18/11/2004
 * Time: 02:16:45 PM
 */
public class MySQLIFileDAO extends BaseDAO implements IFileDAO {

    private static final String SELECT_FILE = "SELECT id, name, parent_folder_id, data, type, size, lnk_icon, file_timestamp, user_owner_id FROM files WHERE id = {0} FOR UPDATE";

    private static final String SELECT_FILE_NO_DATA = "SELECT id, name, parent_folder_id, type, size, lnk_icon, file_timestamp, user_owner_id FROM files WHERE id = {0} FOR UPDATE";

    private static final String SELECT_FILE_NO_DATA_BY_NAME = "SELECT id, name, parent_folder_id, type, size, lnk_icon, user_owner_id FROM files WHERE name = '{0}' AND parent_folder_id = {1} FOR UPDATE";

    private static final String SELECT_FILE_USER_PERMISSIONS = "SELECT permission FROM users_files_permissions WHERE file_id = {0} AND user_id = {1} FOR UPDATE";

    private static final String SELECT_FILE_GOUPS_PERMISSIONS = "SELECT gfp.permission FROM groups_files_permissions gfp, users_groups ug WHERE gfp.file_id = {0} AND ug.user_id = {1} AND gfp.group_id = ug.group_id FOR UPDATE";

    private static final String PS_INSERT_FILE = "INSERT INTO files (name, parent_folder_id, data, type, lnk_icon, file_timestamp, user_owner_id, size) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String PS_UPDATE_FILE = "UPDATE files SET data = ? , type = ?, lnk_icon = ?, file_timestamp = ?, user_owner_id = ? WHERE name=? AND parent_folder_id=?";

    private static final String UPDATE_FILE_USER_PERMISSION = "UPDATE users_files_permissions SET permission = '{0}' WHERE user_id = {1} AND file_id = {2}";

    private static final String INSERT_FILE_USER_PERMISSION = "INSERT INTO users_files_permissions (user_id, file_id, permission) VALUES ({0}, {1}, {2})";

    private static final String INSERT_FILE_GROUP_PERMISSION = "INSERT INTO groups_files_permissions (group_id, file_id, permission) VALUES ({0}, {1}, {2})";

    private static final String DELETE_FILE = "DELETE FROM files WHERE id = {0}";

    private static final String RENAME_FILE = "UPDATE files SET name = '{0}' WHERE id = {1}";

    private static final String COPY_FILE = "INSERT INTO files (name, parent_folder_id, data, type, lnk_icon, file_timestamp, user_owner_id) SELECT name, {0}, data, type, lnk_icon, NULL, {2} FROM files WHERE id = {1}";

    private static final String COPY_USER_FILE_PERMISSION = "INSERT INTO users_files_permissions (user_id, file_id, permission) SELECT user_id, {0}, permission FROM users_files_permissions WHERE file_id = {1}";

    private static final String COPY_GROUP_FILE_PERMISSION = "INSERT INTO groups_files_permissions (group_id, file_id, permission) SELECT group_id, {0}, permission FROM groups_files_permissions WHERE file_id = {1}";

    private static final String MOVE_FILE = "UPDATE files SET parent_folder_id = {0}, user_owner_id = {2} WHERE id = {1}";

    private static final String UPDATE_FILE = "UPDATE files SET data = '{0}', size = {1} WHERE id = {2}";

    private static final String UPDATE_FILE_TS = "UPDATE files SET file_timestamp = '{0}' WHERE id = {1}";

    private static final String SELECT_PARENT_FOLDER = "SELECT parent_folder_id FROM folders WHERE id={0}";

    private static final String SELECT_FILE_ALL_USER_PERMISSIONS = "SELECT permission, user_id FROM users_files_permissions WHERE file_id = {0} FOR UPDATE";

    private static final String SELECT_FILE_ALL_GROUP_PERMISSIONS = "SELECT permission, group_id FROM groups_files_permissions WHERE file_id = {0} FOR UPDATE";

    private static final String DELETE_FILE_USER_PERMISSIONS = "DELETE FROM users_files_permissions WHERE user_id = {0} and file_id = {1}";

    private static final String DELETE_FILE_GROUP_PERMISSIONS = "DELETE FROM groups_files_permissions WHERE group_id = {0} and file_id = {1}";

    private static final String DELETE_FILE_ALL_USER_PERMISSIONS = "DELETE FROM users_files_permissions WHERE file_id = {0}";

    private static final String DELETE_FILE_ALL_GROUP_PERMISSIONS = "DELETE FROM groups_files_permissions WHERE file_id = {0}";

    private MySQLDataSource dataSource;

    public MySQLIFileDAO(DBDataSource dataSource) {
        super(dataSource);
        this.dataSource = (MySQLDataSource) dataSource;
    }

    public File selectFile(int fileId) throws DAOException {
        String sql = Util.replace(SELECT_FILE, new Integer(fileId));
        File file = null;
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                String fileName = rs.getString("name");
                int parentFolderId = rs.getInt("parent_folder_id");
                InputStream stream = rs.getBinaryStream("data");
                int type = rs.getInt("type");
                long size = rs.getLong("size");
                String lnkIcon = rs.getString("lnk_icon");
                long timestamp = rs.getLong("file_timestamp");
                int owner = rs.getInt("user_owner_id");
                file = new File(fileId, fileName, parentFolderId, stream, type, size, lnkIcon, timestamp, owner);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return file;
    }

    public File selectFileNoData(int fileId) throws DAOException {
        String sql = Util.replace(SELECT_FILE_NO_DATA, new Integer(fileId));
        File file = null;
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                String fileName = rs.getString("name");
                int parentFolderId = rs.getInt("parent_folder_id");
                int type = rs.getInt("type");
                long size = rs.getLong("size");
                String lnkIcon = rs.getString("lnk_icon");
                long timestamp = rs.getLong("file_timestamp");
                int owner = rs.getInt("user_owner_id");
                file = new File(fileId, fileName, parentFolderId, null, type, size, lnkIcon, timestamp, owner);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return file;
    }

    public File selectFileNoData(String fileName, int parentFolderId) throws DAOException {
        String sql = Util.replace(SELECT_FILE_NO_DATA_BY_NAME, fileName, new Integer(parentFolderId));
        File file = null;
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("id");
                int type = rs.getInt("type");
                long size = rs.getLong("size");
                String lnkIcon = rs.getString("lnk_icon");
                int owner = rs.getInt("user_owner_id");
                file = new File(id, fileName, parentFolderId, null, type, size, lnkIcon, -1, owner);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return file;
    }

    public File updateFile(File file, int userId) throws DAOException {
        File result = null;
        try {
            int size = file.getInputStream().available();
            PreparedStatement pstmt = dataSource.getConnection().prepareStatement(PS_UPDATE_FILE);
            pstmt.setBinaryStream(1, file.getInputStream(), size);
            pstmt.setInt(2, file.getType());
            pstmt.setString(3, file.getLnkIcon());
            pstmt.setLong(4, file.getTimeStamp());
            pstmt.setInt(5, userId);
            pstmt.setString(6, file.getName());
            pstmt.setInt(7, file.getParentId());
            pstmt.setInt(8, size);
            pstmt.execute();
            String sql = Util.replace(SELECT_FILE_NO_DATA_BY_NAME, file.getName(), new Integer(file.getParentId()));
            ResultSet rs = dataSource.executeQuery(sql);
            rs.next();
            int id = rs.getInt(1);
            rs.close();
            int actions[] = { Security.FILE_CHANGE_PERMISSIONS_PERMISSION, Security.FILE_COPY_PERMISSION, Security.FILE_DELETE_PERMISSION, Security.FILE_MODIFY_PERMISSION, Security.FILE_MOVE_PERMISSION, Security.FILE_READ_PERMISSION, Security.FILE_RENAME_PERMISSION };
            sql = Util.replace(UPDATE_FILE_USER_PERMISSION, new Integer(Security.getPermission(actions)), new Integer(userId), new Integer(id));
            dataSource.executeUpdate(sql);
            result = new File(id, file.getName(), file.getParentId(), null, file.getType(), size, file.getLnkIcon(), file.getTimeStamp(), file.getUserOwnerId());
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        } catch (IOException ioe) {
            throw new DAOException(ioe);
        }
        return result;
    }

    public File insertFile(File file, int userId) throws DAOException {
        File result = null;
        try {
            int size = file.getInputStream().available();
            PreparedStatement pstmt = dataSource.getConnection().prepareStatement(PS_INSERT_FILE);
            pstmt.setString(1, file.getName());
            pstmt.setInt(2, file.getParentId());
            pstmt.setBinaryStream(3, file.getInputStream(), size);
            pstmt.setInt(4, file.getType());
            pstmt.setString(5, file.getLnkIcon());
            pstmt.setLong(6, file.getTimeStamp());
            pstmt.setInt(7, userId);
            pstmt.setInt(8, size);
            pstmt.execute();
            String sql = Util.replace(SELECT_FILE_NO_DATA_BY_NAME, file.getName(), new Integer(file.getParentId()));
            ResultSet rs = dataSource.executeQuery(sql);
            rs.next();
            int id = rs.getInt(1);
            rs.close();
            int actions[] = { Security.FILE_CHANGE_PERMISSIONS_PERMISSION, Security.FILE_COPY_PERMISSION, Security.FILE_DELETE_PERMISSION, Security.FILE_MODIFY_PERMISSION, Security.FILE_MOVE_PERMISSION, Security.FILE_READ_PERMISSION, Security.FILE_RENAME_PERMISSION };
            sql = Util.replace(INSERT_FILE_USER_PERMISSION, new Integer(userId), new Integer(id), new Integer(Security.getPermission(actions)));
            dataSource.executeUpdate(sql);
            result = new File(id, file.getName(), file.getParentId(), null, file.getType(), size, file.getLnkIcon(), file.getTimeStamp(), file.getUserOwnerId());
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        } catch (IOException ioe) {
            throw new DAOException(ioe);
        }
        return result;
    }

    public void deleteFile(int fileId) throws DAOException {
        String sql = Util.replace(DELETE_FILE, new Integer(fileId));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public File renameFile(int fileId, String newFileName) throws DAOException {
        String sql = Util.replace(RENAME_FILE, newFileName, new Integer(fileId));
        File result = null;
        try {
            dataSource.executeUpdate(sql);
            sql = Util.replace(SELECT_FILE_NO_DATA, new Integer(fileId));
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int parentId = rs.getInt("parent_folder_id");
                int type = rs.getInt("type");
                long size = rs.getLong("size");
                String lnkIcon = rs.getString("lnk_icon");
                long timeStamp = rs.getLong("file_timestamp");
                int ownerId = rs.getInt("user_owner_id");
                result = new File(id, name, parentId, null, type, size, lnkIcon, timeStamp, ownerId);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return result;
    }

    public File copyFile(int fileId, String fileName, int folderId, int userOwnerId) throws DAOException {
        String sql = Util.replace(COPY_FILE, new Integer(folderId), new Integer(fileId), new Integer(userOwnerId));
        File result = null;
        try {
            dataSource.executeUpdate(sql);
            sql = Util.replace(SELECT_FILE_NO_DATA_BY_NAME, fileName, new Integer(folderId));
            ResultSet rs = dataSource.executeQuery(sql);
            rs.next();
            int id = rs.getInt("id");
            int type = rs.getInt("type");
            long size = rs.getLong("size");
            String lnkIcon = rs.getString("lnk_icon");
            int ownerId = rs.getInt("user_owner_id");
            rs.close();
            sql = Util.replace(COPY_USER_FILE_PERMISSION, new Integer(id), new Integer(fileId));
            dataSource.executeUpdate(sql);
            sql = Util.replace(COPY_GROUP_FILE_PERMISSION, new Integer(id), new Integer(fileId));
            dataSource.executeUpdate(sql);
            result = new File(id, fileName, folderId, null, type, size, lnkIcon, -1, ownerId);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return result;
    }

    public File moveFile(int fileId, int folderId, int userOwnerId) throws DAOException {
        String sql = Util.replace(MOVE_FILE, new Integer(folderId), new Integer(fileId), new Integer(userOwnerId));
        File result = null;
        try {
            dataSource.executeUpdate(sql);
            sql = Util.replace(SELECT_FILE_NO_DATA, new Integer(fileId));
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int parentId = rs.getInt("parent_folder_id");
                int type = rs.getInt("type");
                long size = rs.getLong("size");
                String lnkIcon = rs.getString("lnk_icon");
                long timeStamp = rs.getLong("file_timestamp");
                int ownerId = rs.getInt("user_owner_id");
                result = new File(id, name, parentId, null, type, size, lnkIcon, timeStamp, ownerId);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return result;
    }

    public int getFileUserPermissions(int fileId, int userId) throws DAOException {
        int res = Consts.NO_PERMISSION_FOUND;
        String sql = Util.replace(SELECT_FILE_USER_PERMISSIONS, new Integer(fileId), new Integer(userId));
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return res;
    }

    public int getUserPermissions(int fileId, int userId) throws DAOException {
        int res = 0;
        String sql = Util.replace(SELECT_FILE_USER_PERMISSIONS, new Integer(fileId), new Integer(userId));
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                res = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return res;
    }

    public int[] getGroupsPermissions(int fileId, int userId) throws DAOException {
        int res[] = {};
        String sql = Util.replace(SELECT_FILE_GOUPS_PERMISSIONS, new Integer(fileId), new Integer(userId));
        try {
            LinkedList list = new LinkedList();
            ResultSet rs = dataSource.executeQuery(sql);
            while (rs.next()) {
                list.addLast(new Integer(rs.getInt(1)));
            }
            rs.close();
            Integer tmp[] = new Integer[list.size()];
            list.toArray(tmp);
            res = new int[tmp.length];
            for (int i = 0; i < res.length; i++) {
                res[i] = tmp[i].intValue();
            }
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return res;
    }

    public AuthorizablePermissionsValues[] getAuthorizablePermissionsValues(File file) throws DAOException {
        AuthorizablePermissionsValues[] permissions = null;
        LinkedList listUsersPermissionsIds = new LinkedList();
        LinkedList listGroupsPermissionsIds = new LinkedList();
        LinkedList listAuthPermValues = new LinkedList();
        Iterator it;
        String usersPermissions = Util.replace(SELECT_FILE_ALL_USER_PERMISSIONS, new Integer(file.getId()));
        String groupsPermissions = Util.replace(SELECT_FILE_ALL_GROUP_PERMISSIONS, new Integer(file.getId()));
        try {
            ResultSet rs = dataSource.executeQuery(usersPermissions);
            while (rs.next()) {
                listUsersPermissionsIds.addLast(new int[] { rs.getInt("user_id"), rs.getInt("permission") });
            }
            rs.close();
            it = listUsersPermissionsIds.iterator();
            MySQLUserDAO userDAO = new MySQLUserDAO(dataSource);
            User user;
            int aux[];
            while (it.hasNext()) {
                aux = (int[]) it.next();
                user = userDAO.selectUser(aux[0]);
                listAuthPermValues.add(new AuthorizablePermissionsValues(user, aux[1]));
            }
            rs = dataSource.executeQuery(groupsPermissions);
            while (rs.next()) {
                listGroupsPermissionsIds.addLast(new int[] { rs.getInt("group_id"), rs.getInt("permission") });
            }
            it = listGroupsPermissionsIds.iterator();
            MySQLIGroupDAO groupDAO = new MySQLIGroupDAO(dataSource);
            Group group;
            while (it.hasNext()) {
                aux = (int[]) it.next();
                group = groupDAO.selectGroup(aux[0]);
                listAuthPermValues.add(new AuthorizablePermissionsValues(group, aux[1]));
            }
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        permissions = new AuthorizablePermissionsValues[listAuthPermValues.size()];
        it = listAuthPermValues.iterator();
        int i = 0;
        while (it.hasNext()) permissions[i++] = (AuthorizablePermissionsValues) it.next();
        return permissions;
    }

    public void deleteUserPermissionsValues(int userId, int fileId) throws DAOException {
        String sql = Util.replace(DELETE_FILE_USER_PERMISSIONS, new Integer(userId), new Integer(fileId));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public void deleteGroupPermissionsValues(int groupId, int fileId) throws DAOException {
        String sql = Util.replace(DELETE_FILE_GROUP_PERMISSIONS, new Integer(groupId), new Integer(fileId));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public void deleteAllUserPermissionsValues(int fileId) throws DAOException {
        String sql = Util.replace(DELETE_FILE_ALL_USER_PERMISSIONS, new Integer(fileId));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public void deleteAllGroupPermissionsValues(int fileId) throws DAOException {
        String sql = Util.replace(DELETE_FILE_ALL_GROUP_PERMISSIONS, new Integer(fileId));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public void insertUserAuthorizablePermissionsValues(int fileId, int userId, int permissionsValues) throws DAOException {
        String sql = Util.replace(INSERT_FILE_USER_PERMISSION, new Integer(userId), new Integer(fileId), new Integer(permissionsValues));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public void insertGroupAuthorizablePermissionsValues(int fileId, int groupId, int permissionsValues) throws DAOException {
        String sql = Util.replace(INSERT_FILE_GROUP_PERMISSION, new Integer(groupId), new Integer(fileId), new Integer(permissionsValues));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public void updateTimeStamp(int fileId, long newTs) throws DAOException {
        String sql = Util.replace(UPDATE_FILE_TS, new Long(newTs), new Integer(fileId));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public void updateFileActionTimeStamp(int timestamp, int fileId, int actionId, int userId, String value) throws DAOException {
    }

    public void updateFile(File file, String newData) throws DAOException {
        String sql = Util.replace(UPDATE_FILE, newData, new Long(file.getSize()), new Integer(file.getId()));
        try {
            dataSource.executeUpdate(sql);
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
    }

    public Integer getParentFolder(int fileId) throws DAOException {
        Integer res = null;
        String sql = Util.replace(SELECT_PARENT_FOLDER, new Integer(fileId));
        try {
            ResultSet rs = dataSource.executeQuery(sql);
            if (rs.next()) {
                res = new Integer(rs.getString(0));
            }
        } catch (SQLException sqle) {
            throw new DAOException(sqle);
        }
        return res;
    }
}
