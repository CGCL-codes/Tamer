package com.juliashine.db.spring;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Juliashine	2011-4-28 
 * 
 */
public interface ResultSetExtractor<T> {

    T extractData(ResultSet rs) throws SQLException;
}
