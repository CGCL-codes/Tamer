package org.webcockpit.builder.config;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.webcockpit.builder.BuilderDatasource;

/**
 */
public class Datasource extends BuilderDatasource implements Configurable {

    public static final String ELEMENT_DATASOURCE = "datasource";

    public static final String ATTRIBUTE_ID = "id";

    public static final String ATTRIBUTE_DESCRIPTION = "description";

    /**
	 * Initialize the instance from the XML Document. The 
	 * child instances of this instance are initialized 
	 * recursively until the entire instance is initialized.  
	 * 
	 * @param config The document containing all configuration information
	 * @param element The element
	 */
    public void init(Document config, Element element) throws ConfigurationException {
        setId(element.attributeValue(ATTRIBUTE_ID));
        setDescription(element.attributeValue(ATTRIBUTE_DESCRIPTION));
        List pList = element.selectNodes(Parameter.ELEMENT_PARAMETER);
        List params = new ArrayList();
        for (int i = 0; i < pList.size(); i++) {
            Element pElement = (Element) pList.get(i);
            Parameter p = new Parameter();
            p.init(config, pElement);
            params.add(p);
        }
        setParameters(params);
    }

    /**
	 * Save the configurable item to an XML Document. The item
	 * adds elements to the parent element for itself and all
	 * it's children.
	 * 
	 * @param config The document being created
	 * @param element The parent element
	 */
    public void save(Document config, Element element) throws ConfigurationException {
        Element datasource = element.addElement(ELEMENT_DATASOURCE);
        datasource.addAttribute(ATTRIBUTE_ID, getId());
        datasource.addAttribute(ATTRIBUTE_DESCRIPTION, getDescription());
        for (int i = 0; getParameters() != null && i < getParameters().size(); i++) {
            Parameter p = (Parameter) getParameters().get(i);
            p.save(config, datasource);
        }
    }
}
