package org.cumt.workbench;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cumt.model.persistence.XmlDocumentBuilderHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class UserContext {

    public static final String PREF_LAST_DIRECTORY = "lastDirectory";

    private static UserContext instance = null;

    private Map<String, String> variables = new HashMap<String, String>();

    private final Log log = LogFactory.getLog(getClass());

    private final File applicationDirectory = new File(System.getProperty("user.home"), ".cumt");

    private final File variableMapFile = new File(applicationDirectory, "variables.xml");

    private final File templatesDirectory = new File(applicationDirectory, "templates");

    private final Preferences userPreferences = Preferences.userNodeForPackage(getClass());

    private UserContext() {
        applicationDirectory.mkdirs();
        templatesDirectory.mkdir();
        try {
            load();
        } catch (IOException e) {
            log.error("Error loading variable map", e);
        }
    }

    public static synchronized UserContext getIt() {
        if (instance == null) {
            instance = new UserContext();
        }
        return instance;
    }

    private void load() throws IOException {
        if (variableMapFile.exists()) {
            importVariables(variableMapFile);
        }
    }

    public void store() {
        try {
            variableMapFile.createNewFile();
            FileWriter writer = new FileWriter(variableMapFile);
            exportVariables(writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            log.error("Error storing variable map", ex);
        }
    }

    public void exportVariables(Writer writer) throws IOException {
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.append("<variableMap>");
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            printWriter.append("<entry><key>").append(entry.getKey()).append("</key><value>").append(entry.getValue()).append("</value></entry>");
        }
        printWriter.append("</variableMap>");
    }

    public void importVariables(File file) throws IOException {
        try {
            Document document = XmlDocumentBuilderHelper.parse(file);
            importVariables(document.getDocumentElement());
        } catch (SAXException e) {
            log.error("Unexpected exception", e);
        }
    }

    public void importVariables(Node node) {
        for (Node entryNode = node.getFirstChild(); entryNode != null; entryNode = entryNode.getNextSibling()) {
            if ("entry".equals(entryNode.getNodeName())) {
                String key = null;
                String value = null;
                for (Node child = entryNode.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if ("key".equals(child.getNodeName())) {
                        key = child.getFirstChild().getNodeValue();
                    } else if ("value".equals(child.getNodeName())) {
                        value = child.getFirstChild() == null ? null : child.getFirstChild().getNodeValue();
                    }
                }
                variables.put(key, value);
            }
        }
    }

    public File getApplicationDirectory() {
        return applicationDirectory;
    }

    public Map<String, String> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = new HashMap<String, String>(variables);
    }

    public void setVariable(String variable, String value) {
        variables.put(variable, value);
    }

    public String getVariable(String variable) {
        return variables.get(variable);
    }

    public Set<String> getVariableNames() {
        return variables.keySet();
    }

    public void deleteVariable(String name) {
        variables.remove(name);
    }

    public File getTemplatesDirectory() {
        return templatesDirectory;
    }

    public Preferences getUserPreferences() {
        return userPreferences;
    }

    public String get(String key, String defaultValue) {
        return userPreferences.get(key, defaultValue);
    }

    public void put(String key, String value) {
        userPreferences.put(key, value);
    }
}
