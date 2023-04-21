package de.ibk.ods.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import de.ibk.ods.core.sql.QueryHandler;
import de.ibk.ods.xml.Factory;

/**
 * @author Reinhard Kessler, Ingenieurbüro Kessler
 * @version 5.0.0
 */
public class Kernel {

    /**
	 * 
	 */
    private Logger log = LogManager.getLogger("de.ibk.ods.openaos");

    /**
	 * 
	 */
    private String kernelId;

    private ApplicationStructureValue applicationStructureValue = null;

    private Connection connection;

    private QueryHandler queryHandler = null;

    private HashMap sqlMap;

    private HashMap typeMap;

    /**
	 * @return Returns the kernelId.
	 */
    public String getKernelId() {
        return kernelId;
    }

    /**
	 * @return Returns the applicationStructureValue.
	 */
    public ApplicationStructureValue getApplicationStructureValue() {
        if (this.applicationStructureValue == null) {
            this.applicationStructureValue = new ApplicationStructureValue(this);
        }
        return this.applicationStructureValue;
    }

    /**
	 * @return Returns the connection.
	 */
    public Connection getConnection() {
        return connection;
    }

    /**
	 * @return Returns the queryHandler.
	 */
    public QueryHandler getQueryHandler() {
        if (queryHandler == null) {
            queryHandler = new QueryHandler(connection);
        }
        return queryHandler;
    }

    /**
	 * @return Returns the sqlMap.
	 */
    public HashMap getSqlMap() {
        return sqlMap;
    }

    /**
	 * @param sqlMap The sqlMap to set.
	 */
    public void setSqlMap(HashMap sqlMap) {
        this.sqlMap = sqlMap;
    }

    /**
	 * @return Returns the typeMap.
	 */
    public HashMap getTypeMap() {
        return typeMap;
    }

    /**
	 * @param typeMap The typeMap to set.
	 */
    public void setTypeMap(HashMap typeMap) {
        this.typeMap = typeMap;
    }

    /**
	 * @param connection
	 */
    public Kernel(String kernelId, Factory factory) {
        super();
        log.debug("Enter Kernel::Kernel()");
        this.kernelId = kernelId;
        try {
            this.connection = DriverManager.getConnection(factory.getUrl(), factory.getUserId(), factory.getPassword());
            this.connection.setAutoCommit(true);
            this.setSqlMap(factory.getSqlMap());
            this.setTypeMap(factory.getTypeMap());
        } catch (SQLException e) {
            log.fatal(e.getMessage());
        }
        log.debug("Exit Kernel::Kernel()");
    }

    /**
	 * 
	 *
	 */
    public void close() {
        log.debug("Enter Kernel::close()");
        if (this.applicationStructureValue != null) {
            applicationStructureValue.close();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        log.debug("Exit Kernel::close()");
    }
}
