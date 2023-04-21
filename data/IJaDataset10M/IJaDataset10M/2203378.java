package com.eroi.migrate.version;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.eroi.migrate.AbstractMigration;
import com.eroi.migrate.ConfigStore;
import com.eroi.migrate.misc.Closer;
import com.eroi.migrate.misc.SchemaMigrationException;

/**
 * This migration enriches the version table by the column 'project'
 * It also provides a version for the version table itself.
 *
 */
public class Migration_2 extends AbstractMigration {

    private String versionTableNew;

    @Override
    protected void init() {
        this.versionTableNew = config.getFullQualifiedVersionTable();
    }

    public void up() {
        if (_oldVersionTableExist()) {
            int existingVersion = _getExistingVersion();
            String query = String.format("INSERT INTO %s (%s, %s) VALUES ('%s', %d)", wrapName(this.versionTableNew), wrapName(ConfigStore.PROJECT_FIELD_NAME), wrapName(ConfigStore.VERSION_FIELD_NAME), config.getEstablishedProjectID(), existingVersion);
            executeStatement(query);
        }
    }

    public void down() {
        dropTable(this.versionTableNew);
    }

    private int _getExistingVersion() {
        int result;
        String query = String.format("SELECT * FROM %s", config.getVersionTable());
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = this.config.getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet != null && resultSet.next()) {
                result = resultSet.getInt(ConfigStore.VERSION_FIELD_NAME);
            } else {
                result = -1;
            }
        } catch (SQLException e) {
            throw new SchemaMigrationException("Unable to retrieve version from old version table", e);
        } finally {
            Closer.close(resultSet);
            Closer.close(statement);
        }
        return result;
    }

    private boolean _oldVersionTableExist() {
        String tableName = this.config.getVersionTable();
        return tableExists(tableName) || tableExists(tableName.toLowerCase()) || tableExists(tableName.toUpperCase());
    }
}
