package net.raymanoz.config;

import java.sql.Connection;
import net.raymanoz.io.File;
import net.raymanoz.util.Properties;

public interface Configuration {

    File getScriptDirectory(long dbVersion);

    File getLatestScriptDirectory();

    File getTemplateFile();

    String getExecutionCommand();

    int getNumberOfDigits();

    File getExecuteDirectory();

    Confirmation getConfirmation();

    String getMigrationMessage();

    Connection getConnection();

    Connection getConnection(String account, String password);

    boolean checkUserExists(String account);

    String getSchemaVersionTable();

    String getScriptHistoryTable();

    DatabaseType getDatabaseType();

    long getLatestDBVersion();

    Properties uMigrateProperties();

    java.io.File getSchemaRepositryRootDir();
}
