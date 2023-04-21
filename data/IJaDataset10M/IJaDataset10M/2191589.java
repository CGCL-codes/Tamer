package org.openejb.ui.jedi.openejb11.jca;

import java.util.HashMap;
import java.util.Map;

/**
 * The metadata for a Resource Adapter deployment.  Metadata is loaded by the
 * XMLReader, modified by the Actions, created and destroyed by the Categories,
 * and saved by the XMLWriter.
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.2 $
 */
public class DeploymentMetaData {

    private String name;

    private Map properties;

    public DeploymentMetaData() {
        properties = new HashMap();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public Map getProperties() {
        return properties;
    }

    public void clearProperties() {
        properties.clear();
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }
}
