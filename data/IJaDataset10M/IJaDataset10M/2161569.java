package monitor.entities;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

/**
 * 
 * Ez az objektum reprezentalja a multicast virtualis cimet
 * 
 * @author Vegh Adam Zoltan 
 *
 */
public class MCastVirtual extends ManagedNode {

    /**
	 * gui specifikus tulajdonsagok beallitasa (? nincs doksi)
	 */
    @Override
    public void setDataToHashMap() {
    }

    @Override
    public Document getProperties() {
        try {
            DocumentBuilderFactory aDBFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = aDBFactory.newDocumentBuilder();
            Document aDocument = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "if you can read this, you are my slave";
    }
}
