package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.KeyTagSearchInfo;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class KeyTagSearchInfoMapper extends HkRowMapper<KeyTagSearchInfo> {

    @Override
    public Class<KeyTagSearchInfo> getMapperClass() {
        return KeyTagSearchInfo.class;
    }

    public KeyTagSearchInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        KeyTagSearchInfo o = new KeyTagSearchInfo();
        o.setOid(rs.getLong("oid"));
        o.setTagId(rs.getLong("oid"));
        o.setYear(rs.getInt("year"));
        o.setMonth(rs.getInt("month"));
        o.setSearchCount(rs.getInt("searchcount"));
        return o;
    }
}
