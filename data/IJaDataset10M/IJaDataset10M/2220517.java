package com.dotmarketing.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.dotmarketing.beans.Permission;
import com.dotmarketing.cms.factories.PublicCompanyFactory;
import com.dotmarketing.cms.factories.PublicRoleFactory;
import com.dotmarketing.cms.factories.PublicUserFactory;
import com.dotmarketing.db.DotConnect;
import com.dotmarketing.db.DotHibernate;
import com.dotmarketing.db.HibernateUtil;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotHibernateException;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;

/**
 * 
 * @author maria & david (2005)
 * @author Carlos Rivas (2007)
*/
public class PermissionFactoryImpl extends PermissionFactory {

    private PermissionCache permissionCache;

    /**
	 * @param permissionCache
	 */
    public PermissionFactoryImpl(PermissionCache permissionCache) {
        super();
        this.permissionCache = permissionCache;
    }

    protected Permission getPermission(String x) {
        try {
            return (Permission) new DotHibernate(Permission.class).load(Long.parseLong(x));
        } catch (Exception e) {
            return (Permission) new DotHibernate(Permission.class).load(x);
        }
    }

    protected List<Permission> getPermissions(Permissionable permissionable) {
        List<Permission> l = null;
        l = permissionCache.getPermissionsFromCache(String.valueOf(permissionable.getPermissionId()));
        if (l != null) {
            return l;
        }
        long pID = permissionable.getPermissionId();
        try {
            if (pID > 0) {
                HibernateUtil persistenceService = new HibernateUtil();
                if (pID > 0) {
                    persistenceService.setQuery("from inode in class com.dotmarketing.beans.Permission where inode_id = ? order by inode_id");
                    persistenceService.setParam(pID);
                    l = (List<Permission>) persistenceService.list();
                } else {
                    l = new ArrayList<Permission>();
                }
                Permission perm = new Permission();
                perm.setInode(pID);
                perm.setPermission(Config.getIntProperty("PERMISSION_READ"));
                perm.setRoleId(Long.parseLong(PublicRoleFactory.getCMSAdminRole().getRoleId()));
                l.add(perm);
                perm = new Permission();
                perm.setInode(pID);
                perm.setPermission(Config.getIntProperty("PERMISSION_WRITE"));
                perm.setRoleId(Long.parseLong(PublicRoleFactory.getCMSAdminRole().getRoleId()));
                l.add(perm);
                perm = new Permission();
                perm.setInode(pID);
                perm.setPermission(Config.getIntProperty("PERMISSION_PUBLISH"));
                perm.setRoleId(Long.parseLong(PublicRoleFactory.getCMSAdminRole().getRoleId()));
                l.add(perm);
                permissionCache.addToPermissionCache(String.valueOf(pID), l);
            } else {
                l = new java.util.ArrayList<Permission>();
            }
        } catch (DotHibernateException e) {
            String cause = String.format("Cannot get permissions for object with id %d.", pID);
            Logger.error(getClass(), cause);
            throw new DataAccessException(cause, e);
        } catch (Exception e) {
            Logger.warn(getClass(), "getPermissions failed:" + e, e);
        }
        return l;
    }

    @SuppressWarnings("static-access")
    protected void deletePermission(Permission p) {
        if (p != null && p.getId() != 0 && permissionExists(p)) {
            HibernateUtil hu = new HibernateUtil(Permission.class);
            Permission pToDel = null;
            try {
                pToDel = (Permission) hu.get(p.getId());
                if (pToDel != null) {
                    hu.delete(pToDel);
                    Logger.debug(this.getClass(), String.format("deletePermission: %s deleted successful!", p.toString()));
                } else {
                    Logger.debug(this.getClass(), String.format("deletePermission: Trying to load a non-existent permission (%s)", p.toString()));
                }
            } catch (DotHibernateException dhe) {
                String cause = String.format("deletePermission: Unable to delete %s in database", p.toString());
                Logger.error(this.getClass(), cause, dhe);
                throw new DataAccessException(cause, dhe);
            }
        } else {
            String cause = String.format("deletePermission: %s not found", p.toString());
            Logger.debug(this.getClass(), cause);
        }
        permissionCache.remove(String.valueOf(p.getInode()));
    }

    /**
	 * This method return true if exists in db that permission object
	 * @param p permission
	 * @return boolean
	 * @version 1.0
	 * @since 1.0
	 */
    private boolean permissionExists(Permission p) {
        String condition = "roleid =" + p.getRoleId() + " and permission=" + p.getPermission() + " and inode_id=" + p.getInode();
        DotHibernate dh = new DotHibernate(Permission.class);
        dh.setQuery("from inode in class com.dotmarketing.beans.Permission where " + condition);
        Permission permission = (Permission) dh.load();
        if (permission.getId() > 0) {
            return true;
        }
        return false;
    }

    protected void savePermission(Permission p) {
        if (!permissionExists(p)) {
            try {
                HibernateUtil hu = new HibernateUtil(Permission.class);
                hu.save(p);
            } catch (DotHibernateException dhe) {
                String cause = String.format("Unable to save %s to database", p.toString());
                Logger.error(this.getClass(), cause, dhe);
                throw new DataAccessException(cause, dhe);
            }
            permissionCache.remove(String.valueOf(p.getInode()));
        } else {
            String cause = String.format("%s not found", p.toString());
            Logger.debug(this.getClass(), cause);
        }
    }

    @Deprecated
    @Override
    protected java.util.Map<String, Object> getUsersAndRolesWithPermissionOnInode(String filter, Permissionable permissionable, int permissionType, int start, int limit) {
        DotConnect dc = new DotConnect();
        String baseQuery = " (select distinct 0 as isuser, 'role' as type, role_.roleId || '' as id, role_.name as name " + "from role_, permission where " + " companyid = '" + PublicCompanyFactory.getDefaultCompanyId() + "' and " + "inode_id = ? and permission.permission  = ?  and " + "role_.roleId = permission.roleId " + "union " + "select distinct 1 as isuser, 'user' as type, user_.userId, user_.firstName || ' ' || user_.lastName as name " + "from user_, users_roles, permission where " + " companyid = '" + PublicCompanyFactory.getDefaultCompanyId() + "' and " + "inode_id = ? and permission.permission  = ?  and " + "permission.roleId = users_roles.roleId and " + "user_.userId = users_roles.userId " + "order by isuser, name) ";
        String query = "select isuser, id, name, type from " + baseQuery + " uar ";
        if (UtilMethods.isSet(filter)) query += "where name like ?";
        dc.setSQL(query);
        dc.addParam(permissionable.getPermissionId());
        dc.addParam(permissionType);
        dc.addParam(permissionable.getPermissionId());
        dc.addParam(permissionType);
        if (UtilMethods.isSet(filter)) {
            dc.addParam("%" + filter + "%");
        }
        if (start > -1) dc.setStartRow(start);
        if (limit > -1) dc.setMaxRows(limit);
        ArrayList<Map<String, String>> list = dc.getResults();
        int total = 0;
        query = "select count(*) as total from " + baseQuery + " uar ";
        if (UtilMethods.isSet(filter)) query += "where name like ?";
        dc.setSQL(query);
        dc.addParam(permissionable.getPermissionId());
        dc.addParam(permissionType);
        dc.addParam(permissionable.getPermissionId());
        dc.addParam(permissionType);
        if (UtilMethods.isSet(filter)) {
            dc.addParam("%" + filter + "%");
        }
        total = dc.getResults().size() == 0 ? 0 : dc.getInt("total");
        HashMap<String, Object> results = new HashMap<String, Object>();
        results.put("data", list);
        results.put("total", total);
        return results;
    }

    @Deprecated
    @Override
    protected java.util.Map<String, Object> getUsersWithPermissionOnInode(String filter, Permissionable permissionable, int permissionType, int start, int limit) {
        DotConnect dc = new DotConnect();
        String baseQuery = " (select distinct 1 as isuser, 'user' as type, user_.userId, user_.firstName || ' ' || user_.lastName as name, user_.emailAddress as emailaddress " + "from user_, users_roles, permission where " + " companyid = '" + PublicCompanyFactory.getDefaultCompanyId() + "' and " + " inode_id = ? and permission.permission  = ?  and " + "permission.roleId = users_roles.roleId and " + "user_.userId = users_roles.userId " + "order by isuser, name) ";
        String query = "select isuser, id, name, type, emailaddress from " + baseQuery + " uar ";
        if (UtilMethods.isSet(filter)) query += "where name like ?";
        dc.setSQL(query);
        dc.addParam(permissionable.getPermissionId());
        dc.addParam(permissionType);
        dc.addParam(permissionable.getPermissionId());
        dc.addParam(permissionType);
        if (UtilMethods.isSet(filter)) {
            dc.addParam("%" + filter + "%");
        }
        if (start > -1) dc.setStartRow(start);
        if (limit > -1) dc.setMaxRows(limit);
        ArrayList<Map<String, String>> list = dc.getResults();
        int total = 0;
        query = "select count(*) as total from " + baseQuery + " uar ";
        if (UtilMethods.isSet(filter)) query += "where name like ?";
        dc.setSQL(query);
        dc.addParam(permissionable.getPermissionId());
        dc.addParam(permissionType);
        dc.addParam(permissionable.getPermissionId());
        dc.addParam(permissionType);
        if (UtilMethods.isSet(filter)) {
            dc.addParam("%" + filter + "%");
        }
        total = dc.getResults().size() == 0 ? 0 : dc.getInt("total");
        HashMap<String, Object> results = new HashMap<String, Object>();
        results.put("data", list);
        results.put("total", total);
        return results;
    }

    protected List<User> getUsers(Permissionable permissionable, int permissionType, String filter, int start, int limit) {
        ArrayList<User> users = new ArrayList<User>();
        DotConnect dotConnect = new DotConnect();
        boolean isFilteredByName = UtilMethods.isSet(filter);
        String userFullName = dotConnect.concat(new String[] { "user_.firstName", "' '", "user_.lastName" });
        StringBuffer baseSql = new StringBuffer("select distinct user_.userid, ");
        baseSql.append(userFullName);
        baseSql.append(" from user_, users_roles, permission where");
        baseSql.append(" user_.companyid = ? and permission.inode_id = ? and permission.permission  = ?");
        baseSql.append(" and permission.roleId = users_roles.roleId ");
        baseSql.append(" and user_.userId = users_roles.userId ");
        if (isFilteredByName) {
            baseSql.append(" and lower(");
            baseSql.append(userFullName);
            baseSql.append(") like ?");
        }
        baseSql.append(" order by ");
        baseSql.append(userFullName);
        String sql = baseSql.toString();
        dotConnect.setSQL(sql);
        Logger.info(PublicRoleFactory.class, "::getUsers -> query: " + dotConnect.getSQL());
        dotConnect.addParam(PublicCompanyFactory.getDefaultCompanyId());
        dotConnect.addParam(permissionable.getPermissionId());
        dotConnect.addParam(permissionType);
        if (isFilteredByName) {
            dotConnect.addParam("%" + filter.toLowerCase() + "%");
        }
        if (start > -1) dotConnect.setStartRow(start);
        if (limit > -1) dotConnect.setMaxRows(limit);
        ArrayList<Map<String, Object>> results = dotConnect.getResults();
        int lenght = results.size();
        for (int i = 0; i < lenght; i++) {
            Map<String, Object> hash = (Map<String, Object>) results.get(i);
            String userId = (String) hash.get("userid");
            users.add(PublicUserFactory.findUserByUserId(userId));
        }
        return users;
    }

    protected int getUserCount(Permissionable permissionable, int permissionType, String filter) {
        try {
            DotConnect dotConnect = new DotConnect();
            boolean isFilteredByName = UtilMethods.isSet(filter);
            StringBuffer baseSql = new StringBuffer("select count(distinct user_.userid) as total from user_, users_roles, permission where");
            baseSql.append(" user_.companyid = ? and permission.inode_id = ? and permission.permission  = ?");
            baseSql.append(" and permission.roleId = users_roles.roleId");
            baseSql.append(" and user_.userId = users_roles.userId");
            if (isFilteredByName) {
                String userFullName = dotConnect.concat(new String[] { "user_.firstName", "' '", "user_.lastName" });
                baseSql.append(" and lower(");
                baseSql.append(userFullName);
                baseSql.append(") like ?");
            }
            String sql = baseSql.toString();
            dotConnect.setSQL(sql);
            Logger.info(PublicUserFactory.class, "::getUserCount -> query: " + dotConnect.getSQL());
            dotConnect.addParam(PublicCompanyFactory.getDefaultCompanyId());
            dotConnect.addParam(permissionable.getPermissionId());
            dotConnect.addParam(permissionType);
            if (isFilteredByName) {
                dotConnect.addParam("%" + filter.toLowerCase() + "%");
            }
            return dotConnect.getInt("total");
        } catch (Exception ex) {
            Logger.error(PublicUserFactory.class, ex.toString(), ex);
            throw new DotRuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    protected List<Permission> getPermissionsFromCache(Permissionable permissionable) {
        List<Permission> l = null;
        l = permissionCache.getPermissionsFromCache(String.valueOf(permissionable.getPermissionId()));
        return l;
    }

    @Override
    protected <P extends Permissionable> Map<Permissionable, List<Permission>> getPermissions(List<P> permissionables) throws DotDataException, DotSecurityException {
        Map<Permissionable, List<Permission>> result = new HashMap<Permissionable, List<Permission>>();
        Map<Long, Permissionable> assetToQueryMap = new HashMap<Long, Permissionable>();
        StringBuffer queryBuffer = new StringBuffer();
        for (Permissionable permissionable : permissionables) {
            long idForQuery = 0;
            if (permissionable != null && permissionable.getPermissionId() > 0) {
                idForQuery = permissionable.getPermissionId();
                List<Permission> perms = getPermissionsFromCache(permissionable);
                if (perms == null && idForQuery > 0) {
                    if (!(queryBuffer.length() > 0)) {
                        queryBuffer.append("'" + idForQuery + "'");
                    } else {
                        queryBuffer.append(",'" + idForQuery + "'");
                    }
                    assetToQueryMap.put(idForQuery, permissionable);
                } else {
                    result.put(permissionable, perms);
                }
            }
        }
        if (queryBuffer.length() > 0) {
            HibernateUtil hu = new HibernateUtil();
            hu.setQuery("from inode in class com.dotmarketing.beans.Permission where inode_id in (" + queryBuffer.toString() + ") order by inode_id asc");
            List<Permission> queryResult = (List<Permission>) hu.list();
            for (Permission perm : queryResult) {
                Permissionable asset = assetToQueryMap.get(perm.getInode());
                List<Permission> perms = result.get(asset);
                if (perms == null) {
                    perms = new ArrayList<Permission>();
                    result.put(asset, perms);
                }
                perms.add(perm);
            }
        }
        Role cmsAdmin = PublicRoleFactory.getCMSAdminRole();
        for (Entry<Permissionable, List<Permission>> permEntry : result.entrySet()) {
            Permissionable permAsset = permEntry.getKey();
            List<Permission> entryPerms = permEntry.getValue();
            long permId = permAsset.getPermissionId();
            boolean updateCache = false;
            Permission perm = new Permission();
            perm.setInode(permId);
            perm.setPermission(Config.getIntProperty("PERMISSION_READ"));
            perm.setRoleId(Long.parseLong(cmsAdmin.getRoleId()));
            if (!entryPerms.contains(perm)) {
                entryPerms.add(perm);
                updateCache = true;
            }
            perm = new Permission();
            perm.setInode(permId);
            perm.setPermission(Config.getIntProperty("PERMISSION_WRITE"));
            perm.setRoleId(Long.parseLong(cmsAdmin.getRoleId()));
            if (!entryPerms.contains(perm)) {
                savePermission(perm);
                entryPerms.add(perm);
                updateCache = true;
            }
            perm = new Permission();
            perm.setInode(permId);
            perm.setPermission(Config.getIntProperty("PERMISSION_PUBLISH"));
            perm.setRoleId(Long.parseLong(cmsAdmin.getRoleId()));
            if (!entryPerms.contains(perm)) {
                savePermission(perm);
                entryPerms.add(perm);
                updateCache = true;
            }
            if (updateCache || permissionCache.getPermissionsFromCache(String.valueOf(permId)) == null) permissionCache.addToPermissionCache(String.valueOf(permId), entryPerms);
        }
        return result;
    }
}
