package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.taobao.Tb_User_News;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class Tb_User_NewsMapper extends HkRowMapper<Tb_User_News> {

    @Override
    public Class<Tb_User_News> getMapperClass() {
        return Tb_User_News.class;
    }

    public Tb_User_News mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tb_User_News o = new Tb_User_News();
        o.setUserid(rs.getLong("userid"));
        o.setNid(rs.getLong("nid"));
        o.setCreate_time(rs.getTimestamp("create_time"));
        return o;
    }
}
