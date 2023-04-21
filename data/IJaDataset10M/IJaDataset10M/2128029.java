package ops.netbeansmodules.idlsupport.projectproperties;

import com.sun.corba.se.impl.orbutil.GetPropertyAction;
import configlib.ArchiverInOut;
import configlib.Serializable;
import configlib.SerializableFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author angr
 */
public class OPSProjectProperties implements Serializable {

    public boolean generateCpp = true;

    public boolean generateJava = true;

    public boolean buildJava = true;

    public boolean buildDebugProject = true;

    public String defaultOPSTopicConfigFile = "src/ops_config.xml";

    public Vector<JarDependency> javaBuildJarDependencies = new Vector<JarDependency>();

    public boolean generateOPSConfigClass = false;

    public boolean opsConfigClassNamespace = false;

    public boolean generateOPSConfigXMLFileFromSourceComments = false;

    public boolean addChecksumToConfig = false;

    public String debugProjDomainID;

    public String vsExampleTopicName = "";

    public String vsExampleDataType = "";

    public String vsExampleDomainID = "";

    public boolean vsExampleEnabled = false;

    private Vector<Property> properties = new Vector<Property>();

    public void serialize(ArchiverInOut archiver) throws IOException {
        generateCpp = archiver.inout("generateCpp", generateCpp);
        generateJava = archiver.inout("generateJava", generateJava);
        buildJava = archiver.inout("buildJava", buildJava);
        buildDebugProject = archiver.inout("buildDebugProject", buildDebugProject);
        debugProjDomainID = archiver.inout("debugProjDomainID", debugProjDomainID);
        defaultOPSTopicConfigFile = archiver.inout("defaultOPSTopicConfigFile", defaultOPSTopicConfigFile);
        generateOPSConfigClass = archiver.inout("generateOPSConfigClass", generateOPSConfigClass);
        javaBuildJarDependencies = (Vector<JarDependency>) archiver.inoutSerializableList("javaBuildJarDependencies", javaBuildJarDependencies);
        vsExampleTopicName = archiver.inout("vsExampleTopicName", vsExampleTopicName);
        vsExampleDataType = archiver.inout("vsExampleDataType", vsExampleDataType);
        vsExampleDomainID = archiver.inout("vsExampleDomainID", vsExampleDomainID);
        vsExampleEnabled = archiver.inout("vsExampleEnabled", vsExampleEnabled);
        properties = (Vector<Property>) archiver.inoutSerializableList("properties", properties);
    }

    public void setProperty(Property p) {
        if (!existProperty(p.key)) {
            properties.add(p);
        } else {
            getProperty(p.key).value = p.value;
        }
    }

    public String getPropertyValue(String key, String defaultValue) {
        for (Property property : properties) {
            if (property.key.equals(key)) {
                return property.value;
            }
        }
        return defaultValue;
    }

    public Property getProperty(String key) {
        for (Property property : properties) {
            if (property.key.equals(key)) {
                return property;
            }
        }
        return null;
    }

    public boolean existProperty(String key) {
        for (Property property : properties) {
            if (property.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static SerializableFactory getSerializableFactory() {
        return new OPSProjectPropertiesFactory();
    }

    static class OPSProjectPropertiesFactory implements SerializableFactory {

        public Serializable create(String type) {
            if (type.equals("ops.netbeansmodules.idlsupport.projectproperties.OPSProjectProperties")) {
                return new OPSProjectProperties();
            }
            if (type.equals("ops.netbeansmodules.idlsupport.projectproperties.JarDependency")) {
                return new JarDependency();
            }
            if (type.equals("ops.netbeansmodules.idlsupport.projectproperties.Property")) {
                return new Property();
            }
            return null;
        }
    }
}
