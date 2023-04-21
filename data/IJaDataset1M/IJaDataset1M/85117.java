package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.CmpGroup;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class CmpGroupMapper extends HkRowMapper<CmpGroup> {

    @Override
    public Class<CmpGroup> getMapperClass() {
        return CmpGroup.class;
    }

    public CmpGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
        CmpGroup o = new CmpGroup();
        o.setCmpgroupId(rs.getLong("cmpgroupid"));
        o.setCompanyId(rs.getLong("companyid"));
        o.setName(rs.getString("name"));
        o.setValidateflg(rs.getByte("validateflg"));
        return o;
    }
}
