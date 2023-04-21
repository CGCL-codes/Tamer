package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.Api_user;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class Api_userMapper extends HkRowMapper<Api_user> {

    @Override
    public Class<Api_user> getMapperClass() {
        return Api_user.class;
    }

    public Api_user mapRow(ResultSet rs, int rowNum) throws SQLException {
        Api_user o = new Api_user();
        o.setOid(rs.getLong("oid"));
        o.setUserid(rs.getLong("userid"));
        o.setApi_type(rs.getInt("api_type"));
        return o;
    }
}
