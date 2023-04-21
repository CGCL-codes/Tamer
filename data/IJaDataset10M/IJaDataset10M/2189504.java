package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.CmpActStepCost;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class CmpActStepCostMapper extends HkRowMapper<CmpActStepCost> {

    @Override
    public Class<CmpActStepCost> getMapperClass() {
        return CmpActStepCost.class;
    }

    public CmpActStepCost mapRow(ResultSet rs, int rowNum) throws SQLException {
        CmpActStepCost o = new CmpActStepCost();
        o.setCostId(rs.getLong("costid"));
        o.setActId(rs.getLong("actid"));
        o.setUserCount(rs.getInt("usercount"));
        o.setActCost(rs.getDouble("actcost"));
        o.setCompanyId(rs.getLong("companyid"));
        return o;
    }
}
