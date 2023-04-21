package net.sourceforge.squirrel_sql.plugins.mssql.event;

import java.lang.String;
import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;

/**
 *
 * @author  rwalberg
 */
public interface IndexIterationListener {

    public void indexSpotted(ITableInfo tableInfo, String indexName);
}
