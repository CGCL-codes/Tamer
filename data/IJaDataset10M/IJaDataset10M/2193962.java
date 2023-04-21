package jaxlib.sql;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jaxlib.col.ObjectArray;
import jaxlib.sql.meta.DbColumnInfo;
import jaxlib.sql.meta.DbForeignKeyInfo;
import jaxlib.sql.meta.DbIndexInfo;
import jaxlib.sql.meta.DbInfoSchema;
import jaxlib.sql.meta.DbTableInfo;
import jaxlib.sql.meta.DbPrimaryKeyInfo;
import jaxlib.sql.meta.DbProductInfo;
import jaxlib.sql.meta.DbTypeInfo;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: JdbcMeta.java 2903 2011-04-14 19:08:58Z joerg_wassmer $
 */
public interface JdbcMeta {

    public Boolean constraintExists(String catalog, String schema, String constraintName) throws SQLException;

    public Jdbc getJdbc();

    public String getIdentifierLeftQuote() throws SQLException;

    public String getIdentifierRightQuote() throws SQLException;

    public String getIdentifierSingleQuote() throws SQLException;

    public ObjectArray<DbColumnInfo> getColumnInfo(@Nullable String catalog, @Nullable String schema, @Nullable String table, String column) throws SQLException;

    public ObjectArray<DbForeignKeyInfo> getCrossReferences(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException;

    public DbProductInfo getDbProductInfo() throws SQLException;

    public ObjectArray<DbForeignKeyInfo> getForeignKeys(@Nullable String catalog, @Nullable String schema, String table) throws SQLException;

    ;

    public ObjectArray<DbIndexInfo> getIndexInfo(@Nullable String catalog, @Nullable String schema, String table, boolean unique, boolean approximate) throws SQLException;

    public DbInfoSchema getInfoSchema() throws SQLException;

    public ObjectArray<DbPrimaryKeyInfo> getPrimaryKeyInfo(@Nullable String catalog, @Nullable String schema, String table) throws SQLException;

    public ObjectArray<DbForeignKeyInfo> getReferencees(@Nullable String catalog, @Nullable String schema, String table) throws SQLException;

    @Nonnull
    public String getStatementSeparator() throws SQLException;

    /**
   * Find all tables that are matching the specified criteria.
   *
   * @param catalog
   *  a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a
   *  catalog; <code>null</code> means that the catalog name should not be used to narrow the search.
   * @param schemaPattern
   *  a schema name pattern; must match the schema name as it is stored in the database; "" retrieves those without a
   *  schema; <code>null</code> means that the schema name should not be used to narrow the search.
   * @param tableNamePattern
   *  a table name pattern; must match the table name as it is stored in the database.
   *
   * @see java.sql.DatabaseMetaData#getTables(String,String,String,String[])
   *
   * @since JaXLib 1.0
   */
    @Nonnull
    public ObjectArray<DbTableInfo> getTableInfo(@Nullable String catalog, @Nullable String schemaPattern, String tableNamePattern) throws SQLException;

    /**
   * Find all tables that are matching the specified criteria.
   *
   * @param catalog
   *  a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a
   *  catalog; <code>null</code> means that the catalog name should not be used to narrow the search.
   * @param schemaPattern
   *  a schema name pattern; must match the schema name as it is stored in the database; "" retrieves those without a
   *  schema; <code>null</code> means that the schema name should not be used to narrow the search.
   * @param tableNamePattern
   *  a table name pattern; must match the table name as it is stored in the database.
   * @param types
   *  a list of table types, which must be from the list of table types returned from
   *  {@link java.sql.DatabaseMetaData#getTableTypes}, to include; <code>null</code> returns all types.
   *
   * @see java.sql.DatabaseMetaData#getTables(String,String,String,String[])
   *
   * @since JaXLib 1.0
   */
    @Nonnull
    public ObjectArray<DbTableInfo> getTableInfo(@Nullable String catalog, @Nullable String schemaPattern, String tableNamePattern, @Nullable String... types) throws SQLException;

    public ObjectArray<DbTypeInfo> getTypeInfo() throws SQLException;

    public String getKeywords() throws SQLException;

    public boolean indexExists(@Nullable String catalog, @Nullable String schema, @Nullable String table, String indexName) throws SQLException;

    public boolean schemaExists(String catalog, String schema) throws SQLException;

    public Boolean uniqueConstraintExists(String catalog, String schema, String constraintName) throws SQLException;

    public String qualifyName(String catalog, String schema, String name) throws SQLException;

    public String quoteIdentifier(String identifier) throws SQLException;

    public Boolean triggerExists(String catalog, String schema, String triggerName) throws SQLException;
}
