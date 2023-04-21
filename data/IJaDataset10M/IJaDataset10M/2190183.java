package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.UserRecentLaba;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class UserRecentLabaMapper extends HkRowMapper<UserRecentLaba> {

    @Override
    public Class<UserRecentLaba> getMapperClass() {
        return UserRecentLaba.class;
    }

    public UserRecentLaba mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserRecentLaba laba = new UserRecentLaba();
        laba.setUserId(rs.getLong("userid"));
        laba.setLabaData(rs.getString("labadata"));
        return laba;
    }
}
