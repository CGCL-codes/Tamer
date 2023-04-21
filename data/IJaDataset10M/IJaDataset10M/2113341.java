package com.pyrphoros.erddb.db;

import com.pyrphoros.erddb.Designer;
import com.pyrphoros.erddb.model.data.DataModel;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 */
public abstract class PyrDatabaseAdapter {

    protected static Logger logger = Logger.getLogger(PyrDatabaseAdapter.class);

    public enum EXPORTTYPE {

        ALL, CREATETABLE, DROPTABLE, CREATEVIEW, DROPVIEW, CREATEFUNCTION, DROPFUNCTION, CREATESEQUENCE, DROPSEQUENCE
    }

    public abstract List<String> getIndexTypes();

    public abstract List<String> getDataTypes();

    public abstract DataModel loadModelFromDB(Connection conn, String schema) throws Exception;

    public abstract void createSQLFromModel(File file, DataModel m, EXPORTTYPE type) throws Exception;

    public abstract int createDBFromModel(Connection conn, DataModel m, EXPORTTYPE type) throws Exception;

    public static PyrDatabaseAdapter getAdapterInstance(DriverShim ds) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        File adapterFile = new File(Designer.getExternalResource("doc.drivers.adapter.dir") + ds.getAdapterJar());
        PyrDatabaseAdapter adapter = null;
        if (adapterFile.exists()) {
            try {
                URL u = adapterFile.toURI().toURL();
                String classname = ds.getAdapterClass();
                URLClassLoader ucl = new URLClassLoader(new URL[] { u });
                adapter = (PyrDatabaseAdapter) Class.forName(classname, true, ucl).newInstance();
            } catch (MalformedURLException ex) {
                logger.error(ex);
            }
        }
        return adapter;
    }
}
