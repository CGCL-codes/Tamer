package com.jxva.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.jxva.dao.DataAccessException;

/**
 *
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2009-04-02 08:52:54 by Jxva
 */
public interface ResultSetCallback<T> {

    T doInResultSet(ResultSet rs) throws SQLException, DataAccessException;
}
