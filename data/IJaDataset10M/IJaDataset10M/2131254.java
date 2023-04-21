package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.CmpCheckInUser;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class CmpCheckInUserMapper extends HkRowMapper<CmpCheckInUser> {

    @Override
    public Class<CmpCheckInUser> getMapperClass() {
        return CmpCheckInUser.class;
    }

    public CmpCheckInUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        CmpCheckInUser o = new CmpCheckInUser();
        o.setOid(rs.getLong("oid"));
        o.setCompanyId(rs.getLong("companyid"));
        o.setUserId(rs.getLong("userid"));
        o.setUptime(rs.getTimestamp("uptime"));
        o.setSex(rs.getByte("sex"));
        o.setEffectCount(rs.getInt("effectcount"));
        return o;
    }
}
