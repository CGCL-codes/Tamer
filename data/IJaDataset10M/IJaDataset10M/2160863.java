package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.CmpTag;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class CmpTagMapper extends HkRowMapper<CmpTag> {

    @Override
    public Class<CmpTag> getMapperClass() {
        return CmpTag.class;
    }

    public CmpTag mapRow(ResultSet rs, int rowNum) throws SQLException {
        CmpTag o = new CmpTag();
        o.setTagId(rs.getLong("tagid"));
        o.setName(rs.getString("name"));
        return o;
    }
}
