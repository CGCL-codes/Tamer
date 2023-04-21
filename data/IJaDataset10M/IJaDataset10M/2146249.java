package de.juwimm.cms.util;

import java.io.BufferedInputStream;
import java.net.URL;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import de.juwimm.cms.http.HttpClientWrapper;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author <a href="mailto:kulawik@juwimm.com">Sascha Kulawik</a>
 * @version $Id: ConfigReader.java 8 2009-02-15 08:54:54Z skulawik $
 */
public class ConfigReader {

    private static Logger log = Logger.getLogger(ConfigReader.class);

    public static final String CONF_NODE_DCF = "//config/dcf/";

    public static final String CONF_NODE_DEFAULT = "//config/default/";

    public static final String CONF_NODE_SKIN = "//config/skin/";

    private Document confdoc = null;

    private String relativeNodePath = "//config";

    protected ConfigReader() {
    }

    /**
	 * @param confNode
	 */
    public ConfigReader(String confNode) {
        try {
            URL url = ConfigReader.class.getResource("/config.xml");
            StringBuffer buildpage = new StringBuffer();
            int c;
            try {
                BufferedInputStream page = new BufferedInputStream(url.openStream());
                while ((c = page.read()) != -1) {
                    buildpage.append((char) c);
                }
            } catch (Exception exe) {
                log.error("Initialization error", exe);
            }
            String configfile = buildpage.toString();
            this.setConfdoc(XercesHelper.string2Dom(configfile));
            this.setRelativeNodePath(confNode);
        } catch (Exception exe) {
            log.error("Initialization error", exe);
        }
    }

    public ConfigReader(URL urlConfFile, String confNode) {
        try {
            String configfile = HttpClientWrapper.getInstance().getString(urlConfFile);
            this.setConfdoc(XercesHelper.string2Dom(configfile));
        } catch (Exception exe) {
            log.error("Initialization error", exe);
        }
    }

    public ConfigReader(String configfile, String confNode) {
        try {
            this.setConfdoc(XercesHelper.string2Dom(configfile));
            this.setRelativeNodePath(confNode);
        } catch (Exception exe) {
        }
    }

    public NodeList getConfigSubelements(String strNode) {
        NodeList nl = null;
        try {
            nl = XercesHelper.findNode(this.getConfdoc().getFirstChild(), this.getRelativeNodePath() + strNode).getChildNodes();
        } catch (Exception exe) {
        }
        return nl;
    }

    public String getConfigAttribute(String strNode, String strAttrName) {
        String retVal = "";
        try {
            Element elm = (Element) XercesHelper.findNode(this.getConfdoc().getFirstChild(), this.getRelativeNodePath() + strNode);
            retVal = elm.getAttribute(strAttrName);
        } catch (Exception exe) {
        }
        return retVal;
    }

    public String getConfigNodeValue(String strNode) {
        String retVal = "";
        try {
            retVal = XercesHelper.getNodeValue(this.getConfdoc().getFirstChild(), this.getRelativeNodePath() + strNode);
        } catch (Exception exe) {
        }
        return retVal;
    }

    public String getConfigNodeValueWithNull(String strNode) {
        String retVal = null;
        try {
            Element nde = (Element) XercesHelper.findNode(this.getConfdoc().getFirstChild(), this.getRelativeNodePath() + strNode);
            retVal = nde.getFirstChild().getNodeValue();
        } catch (Exception exe) {
        }
        return retVal;
    }

    /**
	 * @param confdoc The confdoc to set.
	 */
    protected void setConfdoc(Document confdoc) {
        this.confdoc = confdoc;
    }

    /**
	 * @return Returns the confdoc.
	 */
    protected Document getConfdoc() {
        return confdoc;
    }

    /**
	 * @param relativeNodePath The relativeNodePath to set.
	 */
    protected void setRelativeNodePath(String relativeNodePath) {
        this.relativeNodePath = relativeNodePath;
    }

    /**
	 * @return Returns the relativeNodePath.
	 */
    protected String getRelativeNodePath() {
        return relativeNodePath;
    }
}
