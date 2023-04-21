package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.UserBadge;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class UserBadgeMapper extends HkRowMapper<UserBadge> {

    @Override
    public Class<UserBadge> getMapperClass() {
        return UserBadge.class;
    }

    public UserBadge mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserBadge o = new UserBadge();
        o.setOid(rs.getLong("oid"));
        o.setUserId(rs.getLong("userid"));
        o.setBadgeId(rs.getLong("badgeid"));
        o.setCreateTime(rs.getTimestamp("createtime"));
        o.setLimitflg(rs.getByte("limitflg"));
        o.setName(rs.getString("name"));
        o.setIntro(rs.getString("intro"));
        o.setPath(rs.getString("path"));
        o.setCompanyId(rs.getLong("companyid"));
        return o;
    }
}
