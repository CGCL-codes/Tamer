package org.in4ama.editor.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import org.in4ama.datasourcemanager.cfg.DataSetConfiguration;
import org.in4ama.datasourcemanager.cfg.DataSourceConfiguration;
import org.in4ama.datasourcemanager.cfg.Property;
import org.in4ama.datasourcemanager.util.DocumentHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class to manage the test data in the testdata.xml file.
 * 
 * @author Val Cassidy, Jakub Jonik
 */
public class TestDataManager {

    public static final String CONFIG_FILENAME = "testdata.xml";

    private String filePath;

    private Map<String, Property> parameters = new HashMap<String, Property>();

    public void projectOpened(String path) {
        parameters.clear();
        filePath = path + File.separator + CONFIG_FILENAME;
        readConfigFile();
    }

    /** Creates the Document containing 'design-time' parameters */
    private Document createConfigurationDocument() {
        Document doc = DocumentHelper.createDocument();
        Element testdataElem = doc.createElement("testdata");
        doc.appendChild(testdataElem);
        Element parametersElem = doc.createElement("Parameters");
        testdataElem.appendChild(parametersElem);
        for (Property parameter : parameters.values()) {
            Element parameterElem = doc.createElement("parameter");
            parametersElem.appendChild(parameterElem);
            parameterElem.setAttribute("name", parameter.getName());
            parameterElem.setAttribute("value", parameter.getValue().toString());
            parameterElem.setAttribute("class", parameter.getValueClass().getName());
        }
        Element datasetsElem = doc.createElement("Datasets");
        testdataElem.appendChild(datasetsElem);
        return doc;
    }

    /** Saves the stored 'design-time' parameters to the configuration file */
    public void save() {
        Document doc = createConfigurationDocument();
        DocumentHelper.saveDocument(doc, filePath);
    }

    /** Retrieves the 'design-time' parameters of the current project */
    private void readConfigFile() {
        List<DataSetConfiguration> list = new ArrayList<DataSetConfiguration>();
        File file = new File(filePath);
        if (!file.exists()) return;
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            DocumentBuilder dBuilder = DocumentHelper.getDocumentBuilder(tFactory);
            Document doc = DocumentHelper.getDocument(dBuilder, filePath);
            Node dataSets = doc.getElementsByTagName("testdata").item(0);
            NodeList childNodes = dataSets.getChildNodes();
            int childCount = childNodes.getLength();
            for (int i = 0; i < childCount; i++) {
                Node node = childNodes.item(i);
                if (!(node instanceof Element)) continue;
                Element elem = (Element) node;
                if ("Parameters".equals(elem.getTagName())) parseParameters(elem); else if ("Datasets".equals(elem.getTagName())) parseDataSets(elem);
            }
        } catch (Exception ex) {
            System.err.println("Coulnd't read the test data config file.");
            ex.printStackTrace();
        }
    }

    private void parseParameters(Element elem) {
        NodeList childNodes = elem.getChildNodes();
        int childCount = childNodes.getLength();
        for (int i = 0; i < childCount; i++) {
            Node node = childNodes.item(i);
            if (!(node instanceof Element)) continue;
            Element paramElem = (Element) node;
            Property property = new Property(paramElem.getAttribute("name"), paramElem.getAttribute("class"));
            property.setValue(paramElem.getAttribute("value"));
            parameters.put(property.getName(), property);
        }
    }

    private void parseDataSets(Element elem) {
    }

    /** Indicates whether the specified parameter has been defined */
    public boolean isParamDefined(String paramName) {
        return parameters.containsKey(paramName);
    }

    /** Gets the specified parameter */
    public Property getParameter(String paramName) {
        return parameters.get(paramName);
    }

    /** Adds a defined parameter */
    public void addParameter(Property parameter) {
        parameters.put(parameter.getName(), parameter);
    }

    public Map getParameters() {
        return parameters;
    }
}
