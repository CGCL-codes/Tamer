package com.yict.csms.system.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.yict.common.dao.impl.CommonDao;
import com.yict.csms.system.entity.UserDataper;
import com.yict.csms.system.entity.UserUserGroup;

/**
 * 
 * 
 * @author ryan.wang
 * 
 */
@Repository
public class UserDataperDaoImpl extends CommonDao<UserDataper, Long> {

    public UserDataper findGroup(Long userId, Long cwtId) {
        String queryString = " from UserDataper as u where u.user.userId =:userId and u.dataper.cwtId = :cwtId";
        UserDataper en = new UserDataper();
        try {
            Query query = this.getSession().createQuery(queryString);
            query.setParameter("userId", userId);
            query.setParameter("cwtId", cwtId);
            List<UserDataper> list = query.list();
            if (list != null && list.size() > 0) {
                en = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return en;
    }
}
