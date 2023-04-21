package org.inigma.migrations;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MigrationConfig {

    private static Log logger = LogFactory.getLog(MigrationConfig.class);

    private final File baseDirectory;

    private final ZipFile basePackage;

    private final Map<String, String> properties;

    private File scriptDirectory;

    private boolean ignoreErrorsEnabled;

    private boolean upgradeOnly;

    private boolean autoFailEnabled;

    private Map<String, SchemaInfo> schemaInfos;

    public MigrationConfig(File config) throws MigrationException {
        this.schemaInfos = new LinkedHashMap<String, SchemaInfo>();
        this.properties = new LinkedHashMap<String, String>();
        String extension = config.getName().toLowerCase();
        if (extension.endsWith(".zip") || extension.endsWith(".jar")) {
            this.baseDirectory = null;
            try {
                this.basePackage = new ZipFile(config);
            } catch (IOException e) {
                throw new MigrationException(e);
            }
        } else {
            this.baseDirectory = config.getParentFile();
            this.basePackage = null;
        }
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            if (this.basePackage != null) {
                ZipEntry migrationXml = this.basePackage.getEntry("migration.xml");
                if (migrationXml == null) {
                    migrationXml = this.basePackage.getEntry("migrations.xml");
                    if (migrationXml == null) {
                        throw new MigrationException(config.getName() + " does not contain 'migration.xml' or 'migrations.xml'");
                    }
                }
                processDocument(documentBuilder.parse(this.basePackage.getInputStream(migrationXml)));
            } else {
                processDocument(documentBuilder.parse(config));
            }
        } catch (SAXException e) {
            throw new MigrationException(e);
        } catch (IOException e) {
            throw new MigrationException(e);
        } catch (ParserConfigurationException e) {
            throw new MigrationException(e);
        }
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public SchemaInfo getSchema(String id) {
        return schemaInfos.get(id);
    }

    public Map<String, SchemaInfo> getSchemaInfos() {
        return schemaInfos;
    }

    public File getScriptDirectory() {
        return scriptDirectory;
    }

    public boolean isAutoFailEnabled() {
        return autoFailEnabled;
    }

    public boolean isIgnoreErrorsEnabled() {
        return ignoreErrorsEnabled;
    }

    public boolean isScriptMode() {
        return scriptDirectory != null;
    }

    public boolean isUpgradeOnly() {
        return upgradeOnly;
    }

    public void setAutoFailEnabled(boolean autoFailure) {
        this.autoFailEnabled = autoFailure;
    }

    public void setIgnoreErrorsEnabled(boolean ignoreErrorsEnabled) {
        this.ignoreErrorsEnabled = ignoreErrorsEnabled;
    }

    public void setScriptDirectory(File scriptDirectory) {
        this.scriptDirectory = scriptDirectory;
    }

    public void setUpgradeOnly(boolean upgradeOnly) {
        this.upgradeOnly = upgradeOnly;
    }

    private void processDocument(Document document) throws MigrationException {
        processProperties(document.getElementsByTagName("properties"));
        processSchema(document.getElementsByTagName("schema"));
        processOrder(document.getElementsByTagName("order"));
    }

    private void processOrder(NodeList nodeList) throws MigrationException {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            NamedNodeMap map = item.getAttributes();
            String schemaName = map.getNamedItem("schema").getNodeValue();
            SchemaInfo schemaInfo = schemaInfos.get(schemaName);
            if (schemaInfo == null) {
                throw new MigrationException("Dependency found for " + schemaName + " which is undefined.");
            }
            int version = Integer.parseInt(map.getNamedItem("version").getNodeValue());
            for (int j = 0; j < item.getChildNodes().getLength(); j++) {
                Node depend = item.getChildNodes().item(j);
                if (depend.getNodeType() == Node.ELEMENT_NODE && "depend".equals(depend.getNodeName())) {
                    NamedNodeMap dependMap = depend.getAttributes();
                    String schemaDependName = dependMap.getNamedItem("schema").getNodeValue();
                    SchemaInfo schemaDependInfo = schemaInfos.get(schemaDependName);
                    if (schemaDependInfo == null) {
                        throw new MigrationException("Dependent found for " + schemaName + " but is not defined: " + schemaDependName);
                    }
                    int dependentVersion = Integer.parseInt(dependMap.getNamedItem("version").getNodeValue());
                    Dependency dep = new Dependency();
                    dep.setSchema(schemaDependInfo);
                    dep.setVersion(dependentVersion);
                    dep.setUpgrade(true);
                    schemaInfo.addDependency(version, dep);
                }
            }
        }
    }

    private void processProperties(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                properties.put(item.getNodeName(), item.getTextContent());
            }
        }
    }

    private void processProperties(NodeList nodeList) {
        if (nodeList == null) {
            return;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            processProperties(nodeList.item(i));
        }
    }

    private void processSchema(NodeList schemaNodeList) throws MigrationException {
        if (schemaNodeList == null) {
            throw new MigrationException("No schemas defined. Cannot migrate!");
        }
        for (int i = 0; i < schemaNodeList.getLength(); i++) {
            Node schemaNode = schemaNodeList.item(i);
            NamedNodeMap attributes = schemaNode.getAttributes();
            String schemaId = attributes.getNamedItem("id").getNodeValue();
            SchemaInfo info;
            if (baseDirectory == null) {
                info = new SchemaInfo(basePackage, schemaId);
            } else {
                info = new SchemaInfo(new File(baseDirectory, schemaId));
            }
            info.setDriver(attributes.getNamedItem("driver").getNodeValue());
            if (attributes.getNamedItem("url") != null) {
                info.setUrl(attributes.getNamedItem("url").getNodeValue());
            }
            if (attributes.getNamedItem("username") != null) {
                info.setUsername(attributes.getNamedItem("username").getNodeValue());
            }
            if (attributes.getNamedItem("password") != null) {
                info.setPassword(attributes.getNamedItem("password").getNodeValue());
            }
            schemaInfos.put(info.getId(), info);
            for (String token : info.getTokens()) {
                if (!properties.containsKey(token)) {
                    properties.put(token, null);
                }
            }
            logger.trace("Processed schema " + info.getId());
        }
    }
}
