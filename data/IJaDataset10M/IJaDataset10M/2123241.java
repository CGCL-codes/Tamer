package com.gestioni.adoc.aps.system.services.protocollo.config;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

public class SignatureConfigDOM {

    /**
	 * 
	 * @param xml
	 * @return
	 * @throws ApsSystemException
	 */
    public Signature extractConfig(String xml) throws ApsSystemException {
        Signature signature = new Signature();
        Element root = this.getRootElement(xml);
        signature.setLabel(root.getAttribute("label").getValue());
        List<Element> params = root.getChildren("param");
        Iterator<Element> paramsIt = params.iterator();
        while (paramsIt.hasNext()) {
            Element param = paramsIt.next();
            String posValue = param.getAttributeValue("pos");
            String propertyName = param.getValue();
            signature.getProperties().put(new Integer(posValue), propertyName);
        }
        return signature;
    }

    /**
	 * 
	 * @param config
	 * @return xml
	 */
    public String createConfigXml(Signature config) {
        Element root = new Element("signature");
        root.setAttribute("label", config.getLabel());
        Iterator it = config.getProperties().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Integer pos = (Integer) pairs.getKey();
            String value = (String) pairs.getValue();
            Element param = new Element("param");
            param.setAttribute("pos", pos.toString());
            param.setText(value);
            root.addContent(param);
        }
        Document doc = new Document(root);
        String xml = new XMLOutputter().outputString(doc);
        return xml;
    }

    /**
	 * Returns the Xml element from a given text.
	 * @param xmlText The text containing an Xml.
	 * @return The Xml element from a given text.
	 * @throws ApsSystemException In case of parsing exceptions.
	 */
    private Element getRootElement(String xmlText) throws ApsSystemException {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        StringReader reader = new StringReader(xmlText);
        Element root = null;
        try {
            Document doc = builder.build(reader);
            root = doc.getRootElement();
        } catch (Throwable t) {
            ApsSystemUtils.getLogger().severe("Error parsing xml: " + t.getMessage());
            throw new ApsSystemException("Error parsing xml", t);
        }
        return root;
    }
}
