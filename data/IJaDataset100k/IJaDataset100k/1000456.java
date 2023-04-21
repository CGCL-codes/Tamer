package queryable.store.sql;

import java.util.List;
import javax.sql.DataSource;
import queryable.store.IFieldMetadata;

/**
 * @author rkehoe
 *
 */
public interface IStorageProvider {

    ISQLGenerator getGenerator() throws Exception;

    DataSource getDataSource() throws Exception;

    void createTable(List<IFieldMetadata> fields, String tableName) throws Exception;

    void createView(String viewName, String selectStmt) throws Exception;

    void createIndex(String tableName, String indexName, String... columnNames) throws Exception;
}
