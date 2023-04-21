package net.pmonks.DAL.generator.config;

import java.util.*;
import org.w3c.dom.*;
import org.apache.log4j.*;
import org.apache.log4j.xml.*;

public class GeneratorInfo {

    /**
     * Log4J category used for logging by the class.
     */
    static Category cat = Category.getInstance(GeneratorInfo.class.getName());

    /**
     * Instance constants<p>
     * XML element and attributes names used in the config file.
     */
    protected final String XML_ATTRIBUTE_GENERATOR_CLASS = "class-name";

    protected final String XML_ELEMENT_PARAMETER = "parameter";

    protected final String XML_ATTRIBUTE_PARAMETER_NAME = "name";

    protected final String XML_ATTRIBUTE_PARAMETER_VALUE = "value";

    /**
     * Instance variables<p>
     * Store parsed, validated configuration information.
     */
    protected String generatorClass = null;

    protected Map generatorParameters = null;

    /**
     * Constructor. Instantiates a generator information object from the specified
     * XML DOM object model node.<p>
     */
    public GeneratorInfo(Node node) throws InvalidConfigurationException {
        NamedNodeMap nodeAttributes = node.getAttributes();
        NodeList childNodes = node.getChildNodes();
        int numChildNodes = childNodes.getLength();
        generatorClass = nodeAttributes.getNamedItem(XML_ATTRIBUTE_GENERATOR_CLASS).getNodeValue();
        generatorParameters = new HashMap();
        for (int i = 0; i < numChildNodes; i++) {
            Node currentChildNode = childNodes.item(i);
            String nodeName = currentChildNode.getNodeName();
            if (nodeName.equals(XML_ELEMENT_PARAMETER)) {
                NamedNodeMap parameterAttributes = currentChildNode.getAttributes();
                String key = parameterAttributes.getNamedItem(XML_ATTRIBUTE_PARAMETER_NAME).getNodeValue();
                String value = parameterAttributes.getNamedItem(XML_ATTRIBUTE_PARAMETER_VALUE).getNodeValue();
                generatorParameters.put(key, value);
            } else if (nodeName.charAt(0) == '#') {
            } else {
                String msg = "Invalid element encountered (" + nodeName + ").";
                cat.error(msg);
                throw new InvalidConfigurationException(msg);
            }
        }
    }

    /**
     * Getter method: returns the name of the class to be used to generate the code.<p>
     * <b>Note: This class must extend "net.pmonks.DAL.generator.DALGenerator".</b>
     */
    public String getGeneratorClass() {
        return (generatorClass);
    }

    /**
     * Getter method: returns a map (a set of name / value pairs) representing the
     * parameters to be sent to the generator.<p>
     * <b>Note: May be empty (ie. no parameters were provided in the config file).</b>
     */
    public Map getGeneratorParameters() {
        return (generatorParameters);
    }
}
