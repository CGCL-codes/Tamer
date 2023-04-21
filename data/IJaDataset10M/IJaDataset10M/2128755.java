package net.sf.dub.application.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import net.sf.dub.miniframework.data.DataContainer;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class Configuration implements DataContainer {

    private Properties properties = new Properties();

    private VersionSet versions = new VersionSet();

    public Configuration() {
    }

    public Object get(Object key) {
        return properties.get(key);
    }

    public void clearProperties() {
        properties.clear();
        properties.setProperty("update.table", "dub");
    }

    public void loadProperties(InputStream inputStream) throws IOException {
        properties.load(inputStream);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public VersionSet getVersions() {
        return versions;
    }

    public void clearVersionsInFile() {
        Iterator iterVersions = versions.getIterator();
        while (iterVersions.hasNext()) {
            VersionBean currentVersion = (VersionBean) iterVersions.next();
            if (currentVersion.isInFile()) {
                iterVersions.remove();
            }
        }
    }
}
