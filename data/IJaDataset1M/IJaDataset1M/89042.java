package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.IpCity;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class IpCityMapper extends HkRowMapper<IpCity> {

    @Override
    public Class<IpCity> getMapperClass() {
        return IpCity.class;
    }

    public IpCity mapRow(ResultSet rs, int rowNum) throws SQLException {
        IpCity city = new IpCity();
        city.setCityId(rs.getInt("cityid"));
        city.setName(rs.getString("name"));
        return city;
    }
}
