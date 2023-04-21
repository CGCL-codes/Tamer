package uk.ac.roslin.ensembl.dao.factory;

import java.util.Properties;
import org.apache.ibatis.session.SqlSession;
import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.model.database.Database;
import uk.ac.roslin.ensembl.model.database.DatabaseType;
import uk.ac.roslin.ensembl.model.database.Registry;

public interface DAOFactory {

    public void setDBType(DatabaseType type);

    public DatabaseType getDBType();

    public Registry getRegistry();

    public Properties getConfiguration();

    public void setMybatisSchemaFilePath(String schema);

    public String getMybatisSchemaFilePath();

    public void setEnsemblSchemaVersion(String schemaVersion);

    public String getEnsemblSchemaVersion();

    public void setDBVersion(String dbversion);

    public String getDBVersion();

    public void setDatabaseName(String full_db_name);

    public String getDatabaseName();

    public Database getDatabase();

    public SqlSession getNewSqlSession() throws DAOException;
}
