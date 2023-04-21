package com.loc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.loc.pojo.Users;

@Component
public class UsersDao {

    @Autowired
    private BaseDao baseDao;

    @SuppressWarnings("unchecked")
    public Users findByName(String account) throws Exception {
        String sql = "select * from loc_user where user_account='" + account + "'";
        Users user = baseDao.queryForObject(sql, Users.mapRow(Users.class));
        return user;
    }
}
