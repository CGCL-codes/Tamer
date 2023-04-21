package de.iritgo.aktera.base.database;

import de.iritgo.aktera.model.Model;
import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.model.ModelRequest;
import de.iritgo.aktera.model.ModelResponse;
import de.iritgo.aktera.model.StandardLogEnabledModel;
import de.iritgo.aktera.persist.CreateHandler;
import de.iritgo.aktera.persist.Persistent;
import de.iritgo.aktera.persist.PersistentFactory;
import de.iritgo.simplelife.string.StringTools;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.configuration.Configuration;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @avalon.component
 * @avalon.service type="de.iritgo.aktera.model.Model"
 * @x-avalon.info name="aktera.database.create"
 * @model.model name="aktera.database.create" id="aktera.database.create" logger="aktera"
 * @model.attribute name="forward" value="aktera.database.create-result"
 */
public class CreateDatabase extends StandardLogEnabledModel {

    /**
	 * Execute the model.
	 *
	 * @param req The model request.
	 * @throws ModelException In case of a business failure.
	 */
    public ModelResponse execute(ModelRequest req) throws ModelException {
        PersistentFactory persistentFactory = (PersistentFactory) req.getService(PersistentFactory.ROLE, req.getDomain());
        ModelResponse res = req.createResponse();
        DataSourceComponent dataSourceComponent = (DataSourceComponent) req.getService(DataSourceComponent.ROLE, "keel-dbpool");
        List<Configuration> moduleConfigs = de.iritgo.aktera.base.module.ModuleInfo.moduleConfigsSortedByDependency(req);
        try {
            Connection connection = null;
            try {
                connection = dataSourceComponent.getConnection();
                for (Configuration moduleConfig : moduleConfigs) {
                    String moduleId = moduleConfig.getAttribute("id", "unkown");
                    String createHandlerClassName = moduleConfig.getChild("create").getAttribute("class", null);
                    if (createHandlerClassName != null) {
                        try {
                            System.out.println("CreateDatabase: Creating tables of module '" + moduleId + "' with handler '" + createHandlerClassName + "'");
                            Class klass = Class.forName(createHandlerClassName);
                            if (klass != null) {
                                CreateHandler createHandler = (CreateHandler) klass.newInstance();
                                createHandler.setConnection(connection);
                                createHandler.createTables(req, persistentFactory, connection, log);
                            } else {
                                log.error("CreateDatabase: Unable to find create handler for module '" + moduleId + "'");
                            }
                        } catch (ClassNotFoundException x) {
                            log.error("CreateDatabase: Unable call create handler for module '" + moduleId + "': " + x);
                        } catch (Exception x) {
                            res.addOutput("databaseError", x.getMessage());
                            res.addOutput("databaseErrorStackTrace", StringTools.stackTraceToString(x).replaceAll("\n", "<br>"));
                            return res;
                        }
                    }
                }
            } catch (SQLException x) {
                log.error("Unable to create database connection", x);
            } finally {
                try {
                    connection.close();
                } catch (SQLException x) {
                }
            }
            connection = null;
            try {
                connection = dataSourceComponent.getConnection();
                for (Configuration moduleConfig : moduleConfigs) {
                    String moduleId = moduleConfig.getAttribute("id", "unkown");
                    String createHandlerClassName = moduleConfig.getChild("create").getAttribute("class", null);
                    if (createHandlerClassName != null) {
                        try {
                            System.out.println("CreateDatabase: Creating data of module '" + moduleId + "' with handler '" + createHandlerClassName + "'");
                            Class klass = Class.forName(createHandlerClassName);
                            if (klass != null) {
                                CreateHandler createHandler = (CreateHandler) klass.newInstance();
                                createHandler.setConnection(connection);
                                createHandler.createData(persistentFactory, connection, log, req);
                            } else {
                                log.error("CreateDatabase: Unable to find create handler for module '" + moduleId + "'");
                            }
                        } catch (ClassNotFoundException x) {
                            log.error("CreateDatabase: Unable call create handler for module '" + moduleId + "': " + x);
                        } catch (Exception x) {
                            res.addOutput("databaseError", x.getMessage());
                            res.addOutput("databaseErrorStackTrace", StringTools.stackTraceToString(x).replaceAll("\n", "<br>"));
                            return res;
                        }
                    }
                }
            } catch (SQLException x) {
                log.error("Unable to create database connection", x);
            } finally {
                try {
                    connection.close();
                } catch (SQLException x) {
                }
            }
            try {
                for (Configuration moduleConfig : moduleConfigs) {
                    Persistent version = persistentFactory.create("aktera.Version");
                    version.setField("type", "M");
                    version.setField("name", moduleConfig.getAttribute("id", "unkown"));
                    version.setField("version", moduleConfig.getChild("version").getValue("0.0.0"));
                    version.add();
                }
            } catch (Exception x) {
                System.out.println(x.toString());
            }
            try {
                Model appInfo = (Model) req.getService(Model.ROLE, "aktera.app-info");
                Configuration[] appConfigs = appInfo.getConfiguration().getChildren("app");
                for (int i = 0; i < appConfigs.length; ++i) {
                    Configuration appConfig = appConfigs[i];
                    Persistent version = persistentFactory.create("aktera.Version");
                    version.setField("type", "A");
                    version.setField("name", appConfig.getAttribute("id", "unkown"));
                    version.setField("version", appConfig.getChild("version").getValue("0.0.0"));
                    version.add();
                }
            } catch (Exception x) {
                System.out.println(x.toString());
            }
            try {
                Persistent config = persistentFactory.create("aktera.SystemConfig");
                config.setField("category", "system");
                config.setField("name", "databaseCreated");
                config.setField("type", "B");
                config.setField("value", "true");
                if (!config.find()) {
                    config.add();
                }
            } catch (Exception x) {
                System.out.println(x.toString());
            }
        } catch (ModelException x) {
            res.addOutput("databaseError", x.getMessage());
            res.addOutput("databaseErrorStackTrace", x.getStackTraceAsString().replaceAll("\n", "<br>"));
            return res;
        }
        return res;
    }
}
