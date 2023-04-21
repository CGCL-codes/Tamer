package org.mc.app;

import java.io.File;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

public class App {

    private static App instance;

    private volatile DataSource dataSource;

    private int rootNodeId;

    private ServletContext context;

    private String realPath;

    public final Object treeChangeLock = new Object();

    public File getUploadDir() {
        return uploadDir;
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public ServletContext getContext() {
        return context;
    }

    private App() {
    }

    private File uploadDir;

    public static void init(DataSource dataSource, ServletContext context, int rootNodeId) {
        App app = getInstance();
        app.dataSource = dataSource;
        app.context = context;
        app.rootNodeId = rootNodeId;
        app.realPath = context.getRealPath(File.separator);
        app.uploadDir = new File(app.realPath, "tmp_upload");
        app.uploadDir.mkdir();
    }

    public String getRealPath() {
        return realPath;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public int getRootNodeId() {
        return rootNodeId;
    }
}
