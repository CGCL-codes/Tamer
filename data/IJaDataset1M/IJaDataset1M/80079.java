package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.UserRecentFeed;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class UserRecentFeedMapper extends HkRowMapper<UserRecentFeed> {

    @Override
    public Class<UserRecentFeed> getMapperClass() {
        return UserRecentFeed.class;
    }

    public UserRecentFeed mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserRecentFeed o = new UserRecentFeed();
        o.setUserId(rs.getLong("userid"));
        o.setFeedData(rs.getString("feeddata"));
        return o;
    }
}
