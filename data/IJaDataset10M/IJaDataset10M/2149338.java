package org.easyform.xml;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.easyform.ConfigException;
import org.easyform.Configuration;
import org.easyform.core.Context;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author ZhuYanYu
 * @version $Revision: 1.1
 * @since 2009-7-23
 */
public class XMLConfiguration implements Configuration {

    public void config(InputStream input, Context context) throws ConfigException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            Element root = doc.getDocumentElement();
            NodeList listChild = root.getChildNodes();
            if (!root.getNodeName().equals("easyForm")) {
                throw new ConfigException("XML root element must be easyForm.");
            }
            new EasyQueryParser(context).parseNodes(listChild);
        } catch (Exception e) {
            throw new ConfigException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                throw new ConfigException(e);
            }
        }
    }
}
