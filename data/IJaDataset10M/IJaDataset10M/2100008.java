package org.streets.database;

import javax.sql.DataSource;

/**
 * For thread shutdown and avoid DataSource interface  auto proxy
 * @author dzb
 *
 */
public interface SQLConnectionSource {

    DataSource getDataSource();
}
