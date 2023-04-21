package jdbframework.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author korotindv
 */
public class LanguageManagement {

    private static HashMap langList = null;

    private static String languageClass = null;

    private static String languageMethod = null;

    private static final String JDBF_CONFIG_FILE = "jdbf-config.xml";

    private static void createLangList(HttpServletRequest request) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = (Document) db.parse("file:///" + request.getSession().getServletContext().getRealPath("/WEB-INF/" + JDBF_CONFIG_FILE));
            Element rootElement = dom.getDocumentElement();
            NodeList langNode = rootElement.getElementsByTagName("language");
            if (langNode != null && langNode.getLength() > 0) {
                Properties properties[];
                properties = new Properties[langNode.getLength()];
                FileInputStream file = null;
                langList = new HashMap();
                for (int i = 0; i < langNode.getLength(); i++) {
                    Element langElement = (Element) langNode.item(i);
                    try {
                        file = new FileInputStream(request.getSession().getServletContext().getRealPath("/WEB-INF/" + langElement.getAttribute("source")));
                    } catch (FileNotFoundException fnfe) {
                        throw new RuntimeException(fnfe.getMessage());
                    }
                    properties[i] = new Properties();
                    properties[i].load(file);
                    langList.put(langElement.getAttribute("name"), properties[i]);
                }
            }
            NodeList langClassNode = rootElement.getElementsByTagName("languageClass");
            if (langClassNode != null && langClassNode.getLength() > 0) {
                Element langClassElement = (Element) langClassNode.item(0);
                LanguageManagement.languageClass = langClassElement.getAttribute("name");
                NodeList langMethodNode = langClassElement.getElementsByTagName("languageMethod");
                if (langMethodNode != null && langMethodNode.getLength() > 0) {
                    Element langMethodElement = (Element) langMethodNode.item(0);
                    LanguageManagement.languageMethod = langMethodElement.getAttribute("name");
                }
            }
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException("Config file \"" + request.getSession().getServletContext().getRealPath("/WEB-INF/" + JDBF_CONFIG_FILE) + "\" not found!");
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException(pce.getMessage());
        } catch (SAXException se) {
            throw new RuntimeException(se.getMessage());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
    }

    public static String getMessage(String messageName, HttpServletRequest request) {
        if (langList == null || langList.size() == 0) createLangList(request);
        Object obj = RunMethod.run(LanguageManagement.languageClass, LanguageManagement.languageMethod, new Class[] { HttpServletRequest.class }, new Object[] { request });
        if (obj instanceof String) {
            Properties properties = (Properties) langList.get(obj);
            if (properties == null) throw new RuntimeException("Bad language index:\"" + obj + "\""); else return properties.getProperty(GeneralSettings.replace(messageName, "?", ""), messageName);
        } else throw new RuntimeException(LanguageManagement.languageClass + "." + LanguageManagement.languageMethod + "() must return String.");
    }
}
