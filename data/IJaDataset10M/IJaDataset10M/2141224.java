package org.in4ama.documentengine.project.cfg;

import java.util.HashMap;
import java.util.Map;
import org.in4ama.datasourcemanager.cfg.Property;
import org.in4ama.datasourcemanager.exception.DataSourceException;
import org.in4ama.datasourcemanager.util.DocumentHelper;
import org.in4ama.documentengine.exception.ProjectException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class to manage the test data in the testdata.xml file.
 * 
 * @author Val Cassidy, Jakub Jonik
 */
public class TestDataConfigurationMgr {

    private Map<String, Property> parameters = new HashMap<String, Property>();

    /** Creates the Document containing 'design-time' parameters 
	 * @throws EditorException */
    public Document createParametersConfigDoc() throws ProjectException {
        Document doc = null;
        try {
            doc = DocumentHelper.createDocument();
            Element testdataElem = doc.createElement("testdata");
            doc.appendChild(testdataElem);
            Element parametersElem = doc.createElement("parameters");
            testdataElem.appendChild(parametersElem);
            for (Property parameter : parameters.values()) {
                Element parameterElem = doc.createElement("parameter");
                parametersElem.appendChild(parameterElem);
                parameterElem.setAttribute("name", parameter.getName());
                CDATASection valueElem = doc.createCDATASection(parameter.getValue().toString());
                parameterElem.appendChild(valueElem);
            }
            Element datasetsElem = doc.createElement("datasets");
            testdataElem.appendChild(datasetsElem);
        } catch (Exception ex) {
            String msg = "Unable to save the test data document.";
            throw new ProjectException(msg, ex);
        }
        return doc;
    }

    /** Retrieves the 'design-time' parameters of the current project 
	 * @throws EditorException */
    public void buildParametersConfig(Document doc) throws ProjectException {
        parameters.clear();
        try {
            Node dataSets = doc.getElementsByTagName("testdata").item(0);
            NodeList childNodes = dataSets.getChildNodes();
            int childCount = childNodes.getLength();
            for (int i = 0; i < childCount; i++) {
                Node node = childNodes.item(i);
                if (!(node instanceof Element)) continue;
                Element elem = (Element) node;
                if ("parameters".equals(elem.getTagName())) parseParameters(elem); else if ("datasets".equals(elem.getTagName())) parseDataSets(elem);
            }
        } catch (Exception ex) {
            String msg = "Unable to read the Test Data configuration file.";
            throw new ProjectException(msg, ex);
        }
    }

    /** Builds the list of initialised properties. */
    private void parseParameters(Element elem) throws DataSourceException {
        NodeList childNodes = elem.getChildNodes();
        int childCount = childNodes.getLength();
        for (int i = 0; i < childCount; i++) {
            Node node = childNodes.item(i);
            if (!(node instanceof Element)) continue;
            Element paramElem = (Element) node;
            Property property = new Property(paramElem.getAttribute("name"));
            property.setValue(paramElem.getTextContent());
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

    /** Returns a map containing parameters. */
    public Map<String, Property> getParameters() {
        return parameters;
    }

    /** Removes the specified parameter. */
    public void removeParameter(String name) {
        parameters.remove(name);
    }
}
