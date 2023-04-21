package org.xfeep.asura.bootstrap.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xfeep.asura.core.CoreConsts;
import org.xfeep.asura.core.config.ConfigLoader;
import org.xfeep.asura.core.config.LazyConfigInfo;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import static org.xfeep.asura.bootstrap.config.XmlConfigLoader.*;

/**
 * This class used to load multiple configruations from a single xml file whose format seems like:
 * <pre>
 *  &lt;ConfigSet&gt;
 *    &lt;Config id="first_config"&gt;
 *         .....  one configuration
 *    &lt;/Config&gt;
 *    &lt;Config id="second_config"&gt;
 *       .....  another configuration
 *    &lt;/Config&gt;
 *  &lt;/ConfigSet&gt;
 * </pre>
 * it also support extern configuration xml declared in Config element, e.g.:
 * <pre>
 * 1) declare a simple external xml which contains only one Configuration
 * &lt;ConfigSet&gt;
 *  &lt;Config id="first_config" extern="myExternConfig001.xml"&gt;
 *......
 * 2) declare a complex external xml which contains multiple Configurations
 * &lt;ConfigSet&gt;
   &lt;Config externSet="myExternConfigSet.xml"&gt;
 * </pre>
 * @author zhang yuexiang
 *
 */
public class XmlMultipleConfigLoader implements ConfigLoader {

    public static final String EXTERN_SINGLE_CONFIG = "extern";

    public static final String EXTERN_SET = "externSet";

    protected File file;

    protected List<LazyConfigInfo> list = new ArrayList<LazyConfigInfo>();

    public XmlMultipleConfigLoader() throws ParserConfigurationException, SAXException, IOException {
        this(getDefaultFile());
    }

    public XmlMultipleConfigLoader(String file) throws ParserConfigurationException, SAXException, IOException {
        this(new File(file));
    }

    public XmlMultipleConfigLoader(final File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        docBuilder.setErrorHandler(new DefaultHandler() {

            public void error(SAXParseException e) throws SAXException {
                String systemId = e.getSystemId();
                if (systemId == null) {
                    systemId = "null";
                }
                String message = "Wrong XML inputStream for : file =" + file + " Line=" + e.getLineNumber() + ": " + e.getMessage();
                throw new SAXException(message, e);
            }
        });
        Element e = docBuilder.parse(file).getDocumentElement();
        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getLocalName() == null) {
                continue;
            }
            NamedNodeMap attrsMap = n.getAttributes();
            String configId = getAttrValue(attrsMap, CoreConsts.CONFIG_ID);
            String externSet = getAttrValue(attrsMap, EXTERN_SET);
            if (configId == null && externSet == null) {
                throw new ParserConfigurationException("in file :" + file + "one of attributes: { \"" + CoreConsts.CONFIG_ID + "\", \"" + EXTERN_SET + "\"}" + "is required in node " + n.getLocalName());
            }
            String externSingle = getAttrValue(attrsMap, EXTERN_SINGLE_CONFIG);
            if (externSet != null) {
                XmlMultipleConfigLoader externXmlMultipleConfigLoader = new XmlMultipleConfigLoader(new File(file.getParentFile(), externSet));
                list.addAll(externXmlMultipleConfigLoader.list);
            } else if (externSingle != null) {
                XmlConfigLoader externXmlConfigLoader = new XmlConfigLoader(new File(file.getParentFile(), externSingle));
                list.add(externXmlConfigLoader.lazyConfigInfo);
            } else {
                XmlConfigLoader nodeConfigLoader = new XmlConfigLoader(configId, n);
                list.add(nodeConfigLoader.lazyConfigInfo);
            }
        }
    }

    public Iterator<LazyConfigInfo> iterator() {
        return list.iterator();
    }

    public static String getDefaultFile() {
        String homePath = System.getProperty(XmlDirConfigLoader.ASURA_CONFIG_HOME);
        if (homePath == null) {
            homePath = System.getProperty("user.dir") + "/conf";
        }
        return homePath + "/xfeepAsuraConfigSet.xml";
    }
}
