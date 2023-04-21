package com.hk.svr.pub.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.hk.bean.BlockUser;
import com.hk.frame.dao.rowmapper.HkRowMapper;

public class BlockUserMapper extends HkRowMapper<BlockUser> {

    public BlockUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        BlockUser f = new BlockUser();
        f.setUserId(rs.getLong("userid"));
        f.setBlockUserId(rs.getLong("blockuserid"));
        return f;
    }

    @Override
    public Class<BlockUser> getMapperClass() {
        return BlockUser.class;
    }
}
