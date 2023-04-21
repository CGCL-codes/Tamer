package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
* 09.04.12 15:15
*
* @author xBlackCat
*/
class MySqlStreamingResult<T> extends StreamingResult<T> {

    public MySqlStreamingResult(String debugAnchor, IToObjectConverter<T> converter, Connection connection, String sql, Object... parameters) throws SQLException {
        super(debugAnchor, converter, connection, sql, parameters);
    }

    protected PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement st = super.prepareStatement(sql);
        st.setFetchSize(Integer.MIN_VALUE);
        return st;
    }
}
