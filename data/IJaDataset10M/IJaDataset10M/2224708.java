package com.j256.ormlite.simpledb;

import java.sql.SQLException;
import java.sql.Savepoint;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.GeneratedKeyHolder;

/**
 * Database connection for SimpleDB.
 * 
 * @author graywatson
 */
public class SimpleDbDatabaseConnection implements DatabaseConnection {

    private final AmazonSimpleDB sdb;

    public SimpleDbDatabaseConnection(AWSCredentials credentials) {
        this.sdb = new AmazonSimpleDBClient(credentials);
    }

    public boolean isAutoCommitSupported() {
        return false;
    }

    public boolean getAutoCommit() {
        return true;
    }

    public void setAutoCommit(boolean autoCommit) {
        throw new IllegalStateException("Auto-commit is not supported");
    }

    public Savepoint setSavePoint(String name) {
        return null;
    }

    public void commit(Savepoint savepoint) {
    }

    public void rollback(Savepoint savepoint) {
    }

    public CompiledStatement compileStatement(String statement, StatementType type, FieldType[] argfieldTypes) throws SQLException {
        return new SimpleDbCompiledStatement(sdb, statement, type, argfieldTypes);
    }

    public int insert(String statement, Object[] args, FieldType[] argfieldTypes, GeneratedKeyHolder keyHolder) throws SQLException {
        return 0;
    }

    public int update(String statement, Object[] args, FieldType[] argfieldTypes) throws SQLException {
        return 0;
    }

    public int delete(String statement, Object[] args, FieldType[] argfieldTypes) throws SQLException {
        return 0;
    }

    public <T> Object queryForOne(String statement, Object[] args, FieldType[] argfieldTypes, GenericRowMapper<T> rowMapper, ObjectCache objectCache) throws SQLException {
        return null;
    }

    public long queryForLong(String statement) {
        return 0;
    }

    public long queryForLong(String statement, Object[] args, FieldType[] argfieldTypes) {
        return 0;
    }

    public void close() {
    }

    public boolean isClosed() {
        return false;
    }

    public boolean isTableExists(String tableName) throws SQLException {
        return false;
    }
}
