package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.CmpProductTag;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class CmpProductTagMapper extends HkRowMapper<CmpProductTag> {

    @Override
    public Class<CmpProductTag> getMapperClass() {
        return CmpProductTag.class;
    }

    public CmpProductTag mapRow(ResultSet rs, int rowNum) throws SQLException {
        CmpProductTag o = new CmpProductTag();
        o.setTagId(rs.getLong("tagid"));
        o.setName(rs.getString("name"));
        return o;
    }
}
