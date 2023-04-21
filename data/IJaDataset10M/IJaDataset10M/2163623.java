package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.Impression;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class ImpressionMapper extends HkRowMapper<Impression> {

    @Override
    public Class<Impression> getMapperClass() {
        return Impression.class;
    }

    public Impression mapRow(ResultSet rs, int rowNum) throws SQLException {
        Impression o = new Impression();
        o.setOid(rs.getLong("oid"));
        o.setSenderId(rs.getLong("senderid"));
        o.setContent(rs.getString("content"));
        o.setProuserId(rs.getLong("prouserid"));
        o.setCreateTime(rs.getTimestamp("createtime"));
        return o;
    }
}
