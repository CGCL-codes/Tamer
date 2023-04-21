package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.HkObjKindOrder;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class HkObjKindOrderMapper extends HkRowMapper<HkObjKindOrder> {

    @Override
    public Class<HkObjKindOrder> getMapperClass() {
        return HkObjKindOrder.class;
    }

    public HkObjKindOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
        HkObjKindOrder o = new HkObjKindOrder();
        o.setOid(rs.getLong("oid"));
        o.setKindId(rs.getInt("kindid"));
        o.setHkObjId(rs.getLong("hkobjid"));
        o.setStopflg(rs.getByte("stopflg"));
        o.setCityId(rs.getInt("cityid"));
        o.setMoney(rs.getInt("money"));
        o.setUtime(rs.getTimestamp("utime"));
        o.setPday(rs.getInt("pday"));
        o.setUserId(rs.getLong("userid"));
        return o;
    }
}
