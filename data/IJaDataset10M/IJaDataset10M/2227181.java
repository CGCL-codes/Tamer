package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.taobao.Tb_Friend_News;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class Tb_Friend_NewsMapper extends HkRowMapper<Tb_Friend_News> {

    @Override
    public Class<Tb_Friend_News> getMapperClass() {
        return Tb_Friend_News.class;
    }

    @Override
    public Tb_Friend_News mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tb_Friend_News o = new Tb_Friend_News();
        o.setOid(rs.getLong("oid"));
        o.setUserid(rs.getLong("userid"));
        o.setNews_userid(rs.getLong("news_userid"));
        o.setNid(rs.getLong("nid"));
        o.setCreate_time(rs.getTimestamp("create_time"));
        return o;
    }
}
