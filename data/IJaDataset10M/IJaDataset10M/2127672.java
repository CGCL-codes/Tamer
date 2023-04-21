package au.edu.qut.yawl.persistence;

import java.io.File;

/**
 * @author Matthew Sandoz
 * @author Nathan Rose
 */
public class DatasourceRoot extends DatasourceFolder {

    public DatasourceRoot(String name) {
        super(name, null);
    }

    public DatasourceRoot(File file) {
        super(file.toURI().toString(), null);
        setFile(file);
    }

    @Override
    public String getPath() {
        return getName();
    }

    @Override
    public DatasourceRoot getRoot() {
        return this;
    }

    public boolean isSchemaVirtual() {
        return getName().startsWith("virtual:");
    }

    public boolean isSchemaFile() {
        return getName().startsWith("file:");
    }

    public boolean isSchemaHibernate() {
        return getName().startsWith("hibernate:");
    }
}
