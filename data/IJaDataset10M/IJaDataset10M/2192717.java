package org.formaria.data;

import java.io.Reader;
import org.formaria.data.DataSource;
import org.formaria.aria.Project;
import org.formaria.aria.build.BuildProperties;
import org.formaria.debug.DebugLogger;
import org.formaria.data.OptionalDataSource;
import org.formaria.data.sql.DataConnection;
import org.formaria.data.sql.DatabaseTableModel;
import org.formaria.aria.helper.ReflectionHelper;

/**
 * A wrapper for the database access code
 * <p> Copyright (c) Formaria Ltd., 2001-2007, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * $Revision: 1.3 $
 */
public class DatabaseConnector {

    private Project currentProject;

    private Class defaultSourceClass;

    private DataSource modelDataSource;

    private DataConnection connection;

    public DatabaseConnector(Project project) {
        currentProject = project;
        createDataSource();
    }

    public DataConnection getConnection(String connName) {
        if (connection == null) connection = new DataConnection(currentProject, connName);
        return connection;
    }

    protected void createDataSource() {
        try {
            String klass = currentProject.getStartupParam("DataSourceClass");
            if (klass != null) {
                Object[] values = { currentProject };
                modelDataSource = (DataSource) ReflectionHelper.constructViaReflection(klass, values);
            }
            if (modelDataSource == null) return;
            try {
                String fileName = currentProject.getStartupParam("DelayedModelData");
                if (fileName != null) {
                    try {
                        modelDataSource.read(currentProject.getBufferedReader(fileName, null));
                        return;
                    } catch (Exception ex3) {
                        if (BuildProperties.DEBUG) DebugLogger.logError("Could not access file:" + fileName);
                    }
                }
            } catch (Exception ex) {
                if (BuildProperties.DEBUG) DebugLogger.logError("Exception in setContent");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
