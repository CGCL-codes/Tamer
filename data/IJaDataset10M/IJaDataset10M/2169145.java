package org.inigma.migrations;

import java.util.Map;

public interface MigrationListener {

    void onComment(String comment);

    /**
     * @return true to actually execute this piece of sql. false to not execute it.
     */
    boolean onExecuteSql(String sql, boolean multiline);

    void onInitialization(Schema schemaInfo);

    void onMigrateFile(Schema schemaInfo, MigrationResource file);

    void onMigration(Schema schemaInfo, int currentVersion, int targetVersion);

    void onMigrationComplete(Schema schemaInfo);

    void onMissingDriver(Schema schemaInfo);

    void onMissingPassword(Schema schemaInfo);

    void onMissingProperty(String key, Map<String, String> properties);

    void onMissingUrl(Schema schemaInfo);

    void onMissingUsername(Schema schemaInfo);

    /**
     * @return true to continue migration. false to terminate the migration process.
     */
    boolean onException(String sql, Exception e);

    void setProgress(double percentage);
}
