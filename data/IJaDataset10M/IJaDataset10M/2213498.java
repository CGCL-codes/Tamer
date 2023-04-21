package com.yict.csms.system.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.yict.common.dao.impl.CommonDao;
import com.yict.csms.system.entity.UserGroupPermission;
import com.yict.csms.system.entity.UserUserGroup;

/**
 * 
 * 
 * @author Patrick.Deng
 * 
 */
@Repository
public class UserGroupPermissionDaoImpl extends CommonDao<UserGroupPermission, Long> {

    public UserGroupPermission findPermission(Long userGroupId, Long permissionId) {
        String queryString = " from UserUserGroup as u where u.UserGroup.userGroupId =:userGroupId and u.Permission.permissionId = :permissionId";
        UserGroupPermission en = new UserGroupPermission();
        try {
            Query query = this.getSession().createQuery(queryString);
            query.setParameter("userGroupId", userGroupId);
            query.setParameter("permissionId", permissionId);
            List<UserGroupPermission> list = query.list();
            if (list != null && list.size() > 0) {
                en = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return en;
    }

    public boolean check(Long userGroupId) {
        String queryString = " from UserUserGroup as u where u.UserGroup.userGroupId =:userGroupId";
        try {
            Query query = this.getSession().createQuery(queryString);
            query.setParameter("userGroupId", userGroupId);
            List<UserGroupPermission> list = query.list();
            if (list != null && list.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
