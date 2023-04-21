package org.bungeni.editor.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok
 */
public class LocalesReader extends BaseConfigReader {

    private static Logger log = Logger.getLogger(LocalesReader.class.getName());

    public static final String SETTINGS_FOLDER = CONFIGS_FOLDER + File.separator + "locales";

    public static final String LOCALES_FILE = "locales.xml";

    public static final String RELATIVE_PATH_TO_SYSTEM_PARAMETERS_FILE = SETTINGS_FOLDER + File.separator + LOCALES_FILE;

    private static LocalesReader thisInstance = null;

    private Document localesDocument = null;

    private XPath xpathInstance = null;

    private LocalesReader() {
    }

    public static LocalesReader getInstance() {
        if (null == thisInstance) {
            thisInstance = new LocalesReader();
        }
        return thisInstance;
    }

    public List<Element> getLocales() throws JDOMException {
        if (null != getDocument()) {
            List<Element> localeElements = getXPath().selectNodes(getDocument(), "//locale");
            return localeElements;
        } else {
            log.error("Locale code file could not be loaded !");
            return null;
        }
    }

    private XPath getXPath() throws JDOMException {
        if (this.xpathInstance == null) {
            this.xpathInstance = XPath.newInstance("//locale");
        }
        return this.xpathInstance;
    }

    private Document getDocument() {
        if (this.localesDocument == null) {
            try {
                this.localesDocument = CommonXmlUtils.loadFile(RELATIVE_PATH_TO_SYSTEM_PARAMETERS_FILE);
            } catch (FileNotFoundException ex) {
                log.error("file not found", ex);
            } catch (UnsupportedEncodingException ex) {
                log.error("encoding error", ex);
            } catch (JDOMException ex) {
                log.error("dom error", ex);
            } catch (IOException ex) {
                log.error("io error", ex);
            }
        }
        return this.localesDocument;
    }
}
