package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.CmpContact;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class CmpContactMapper extends HkRowMapper<CmpContact> {

    @Override
    public Class<CmpContact> getMapperClass() {
        return CmpContact.class;
    }

    public CmpContact mapRow(ResultSet rs, int rowNum) throws SQLException {
        CmpContact o = new CmpContact();
        o.setOid(rs.getLong("oid"));
        o.setCompanyId(rs.getLong("companyid"));
        o.setQqhtml(rs.getString("qqhtml"));
        o.setQq(rs.getString("qq"));
        o.setName(rs.getString("name"));
        return o;
    }
}
