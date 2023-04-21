package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.taobao.Tb_Item_Cat;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class Tb_Item_CatMapper extends HkRowMapper<Tb_Item_Cat> {

    @Override
    public Class<Tb_Item_Cat> getMapperClass() {
        return Tb_Item_Cat.class;
    }

    public Tb_Item_Cat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tb_Item_Cat o = new Tb_Item_Cat();
        o.setCid(rs.getLong("cid"));
        o.setParent_cid(rs.getLong("parent_cid"));
        o.setName(rs.getString("name"));
        o.setParentflg(rs.getByte("parentflg"));
        o.setStatus(rs.getByte("status"));
        o.setSort_order(rs.getInt("sort_order"));
        o.setChild_update(rs.getByte("child_update"));
        return o;
    }
}
