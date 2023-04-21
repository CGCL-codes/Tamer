package com.javampire.util.dao.db;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * TODO: document this.
 *
 * @author <a href="mailto:cnagy@ecircle.de">Csaba Nagy</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2007/04/10 10:21:59 $
 */
public interface DBNodeFactory {

    DBNodeFactory POSTGRES = new PostgresDBNodeFactory();

    public <T> DBNode<T> createNode(Class<T> recordClass) throws IOException;

    public DataSource getDataSource() throws IOException;

    public String nextSyntax(String sequenceName);
}
