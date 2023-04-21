package org.xaware.ide.xadev.datamodel.soap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.http.HTTPOperation;
import javax.wsdl.extensions.soap.SOAPAddress;
import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xaware.ide.shared.UserPrefs;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.common.GlobalConstants;
import org.xaware.ide.xadev.common.ResourceUtils;
import org.xaware.ide.xadev.conversion.WSDLSOAPHelper;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Data Model for SOAP BizDriver Screen for SOAP BizComponent wizard.
 * 
 * @author Vasu Thadaka
 */
public class SOAPBizDriverInfo {

    /** Instance of XAwareLogger. */
    public static final XAwareLogger logger = XAwareLogger.getXAwareLogger(SOAPBizDriverInfo.class.getName());

    /** namespace */
    public static final Namespace ns = XAwareConstants.xaNamespace;

    /** Translator instance to get names from message bundle */
    public static final Translator translator = XA_Designer_Plugin.getTranslator();

    /**String constant to hold the 'name' attribute value for the wizard step*/
    public static final String SOAP_CONNECTION_INFO_WIZARD_STEP = "SOAP Connection Info";

    /** Instance of designer Element. */
    private Element designerElement;

    /** Selected WSDL file name. */
    private String wsdlFileName;

    /** Instance of Service. */
    private Service service;

    /** Instance of Port. */
    private Port port;

    /** String value representing service url. */
    private String url;

    /** WSDLSOAP Helper instance */
    private WSDLSOAPHelper wsdlSOAPHelper;

    /** string containing file name */
    protected String bizDriverFileName;

    /** URL representation of biz driver file name */
    private String urlFileName;

    /** useExisting */
    protected boolean useExisting;

    /** BizDriver document */
    protected Document myDoc;

    /** Description of bizdriver */
    private String description;

    /** Default constructor for SOAPBizDriverInfo */
    public SOAPBizDriverInfo() {
    }

    /**
	 * Sets the value of URL.
	 * 
	 * @param in
	 *            String value representing url.
	 */
    public void setURL(final String in) {
        url = in;
    }

    /**
	 * Returns value of URL member variable.
	 * 
	 * @return String value representing URL.
	 */
    public String getURL() {
        return url;
    }

    /**
	 * Sets the value of Designer Element variable.
	 * 
	 * @param designerElement
	 *            Element representing Designer Element.
	 */
    public void setDesignerElement(final Element designerElement) {
        this.designerElement = designerElement;
    }

    /**
	 * Returns the designer Element
	 * 
	 * @return Instance of Element.
	 */
    public Element getDesignerElement() {
        return designerElement;
    }

    /**
	 * @return the wsdlFileName
	 */
    public String getWsdlFileName() {
        return wsdlFileName;
    }

    /**
	 * @param wsdlFileName
	 *            the wsdlFileName to set
	 */
    public void setWsdlFileName(String wsdlFileName) {
        this.wsdlFileName = wsdlFileName;
    }

    /**
	 * Sets the value of Service member variable.
	 * 
	 * @param inService
	 *            Instance of Service.
	 */
    public void setService(final Service inService) {
        service = inService;
    }

    /**
	 * Sets the value of Port member variable.
	 * 
	 * @param inPort
	 *            Instance of Port.
	 */
    public void setPort(final Port inPort) {
        port = inPort;
    }

    /**
	 * Returns the value of Service member variable.
	 * 
	 * @return Instance of Service.
	 */
    public Service getService() {
        return service;
    }

    /**
	 * Returns the value of Port member variable.
	 * 
	 * @return Instance of Port.
	 */
    public Port getPort() {
        return port;
    }

    /**
	 * Returns the URI of SoapAddress or HTTPAddress or HTTPOperation within
	 * port member variable.
	 * 
	 * @return String value representing URI of service.
	 */
    public static String getServiceAddress(final Port port) {
        String retVal = "";
        try {
            final Iterator itr = port.getExtensibilityElements().iterator();
            if (itr.hasNext()) {
                final ExtensibilityElement tmp = (ExtensibilityElement) itr.next();
                final String displayStr = tmp.toString();
                if (tmp instanceof SOAPAddress) {
                    retVal = ((SOAPAddress) tmp).getLocationURI();
                } else if (tmp instanceof HTTPAddress) {
                    retVal = ((HTTPAddress) tmp).getLocationURI();
                } else if (tmp instanceof HTTPOperation) {
                    retVal = ((HTTPOperation) tmp).getLocationURI();
                } else {
                    retVal = displayStr;
                }
            }
        } catch (final Exception e) {
            logger.finest("Exception : " + e);
        }
        return retVal;
    }

    /**
	 * @return the wsdlSOAPHelper
	 */
    public WSDLSOAPHelper getWsdlSOAPHelper() {
        return wsdlSOAPHelper;
    }

    /**
	 * @param wsdlSOAPHelper
	 *            the wsdlSOAPHelper to set
	 */
    public void setWsdlSOAPHelper(WSDLSOAPHelper wsdlSOAPHelper) {
        this.wsdlSOAPHelper = wsdlSOAPHelper;
    }

    /**
     * Updates the document with current values of bizdriver screen.
     * 
     * @return Document object.
     */
    public Element constructBizDriverRootElement() {
        Element root = new Element(XAwareConstants.ELEMENT_BIZDRIVER, ns);
        String bizDriverTypeAttribute = XAwareConstants.BIZDRIVER_SOAP_HTTP_BIZDRIVER_TYPE_ATTR;
        root.setAttribute(new Attribute(GlobalConstants.ATTRIBUTE_BIZDRIVER_TYPE, bizDriverTypeAttribute, ns));
        root.setAttribute(new Attribute(XAwareConstants.XAWARE_ATTR_VERSION, "5.0", ns));
        final Element descriptionElement = new Element(XAwareConstants.XAWARE_ELEMENT_DESCRIPTION, ns);
        descriptionElement.setText(description);
        root.addContent(descriptionElement);
        final Element httpElement = new Element("http", ns);
        final Element urlElement = new Element("url", ns);
        urlElement.setText(url);
        httpElement.addContent(urlElement);
        root.addContent(httpElement);
        final Element designerElement = buildDesignerElement();
        final Comment comment = new Comment(Translator.getInstance().getString("Do not modify the following XA-Designer element as it is used by XA-Designer wizard."));
        root.addContent(comment);
        root.addContent(designerElement);
        return root;
    }

    private Element buildDesignerElement() {
        final Element designerElem = new Element("designer", ns);
        final Element wizardStepElement = new Element("wizard_step", ns);
        wizardStepElement.setAttribute("name", SOAP_CONNECTION_INFO_WIZARD_STEP, ns);
        final Element connectionElement = new Element(XAwareConstants.BIZDRIVER_CONNECTION, ns);
        connectionElement.setAttribute(XAwareConstants.BIZDRIVER_WSDL_URL, wsdlFileName, ns);
        connectionElement.setAttribute(XAwareConstants.BIZDRIVER_ATTR_SERVICE, service.getQName().getLocalPart(), ns);
        connectionElement.setAttribute(XAwareConstants.BIZDRIVER_ATTR_PORT, port.getName(), ns);
        wizardStepElement.addContent(connectionElement);
        designerElem.addContent(wizardStepElement);
        return designerElem;
    }

    /**
	 * Saves BizDriver file.
	 * 
	 * @param showSuccessfullMessage
	 *            true if to show successfull message.
	 * @throws IOException
	 *             if file not found or error saving file.
	 */
    public void saveBizDriverFile(boolean showSuccessfullMessage) throws IOException {
        final String bizDriverFileName = getBizDriverFileName();
        urlFileName = null;
        try {
            urlFileName = ResourceUtils.convertFilenameToURL(bizDriverFileName);
        } catch (final Exception e) {
            logger.fine("Invalid URL encountered while trying to save Biz Driver." + " Using file path instead");
            urlFileName = bizDriverFileName;
        }
        if (!isUseExisting()) {
            final FileOutputStream fileOut = new FileOutputStream(ResourceUtils.getFile(bizDriverFileName));
            UserPrefs.getFileOutputter().output(getDocument(), fileOut);
            fileOut.close();
            if (showSuccessfullMessage) ControlFactory.showInformationDialog(translator.getString("Successfully saved BizDriver file."));
        }
    }

    /**
	 * @return the bizDriverFileName
	 */
    public String getBizDriverFileName() {
        return bizDriverFileName;
    }

    /**
	 * @param bizDriverFileName
	 *            the bizDriverFileName to set
	 */
    public void setBizDriverFileName(String bizDriverFileName) {
        this.bizDriverFileName = bizDriverFileName;
    }

    /**
	 * @return the useExisting
	 */
    public boolean isUseExisting() {
        return useExisting;
    }

    /**
	 * @param useExisting
	 *            the useExisting to set
	 */
    public void setUseExisting(boolean useExisting) {
        this.useExisting = useExisting;
    }

    /**
	 * getter for Document
	 * 
	 * @return Document
	 */
    public Document getDocument() {
        if (useExisting) {
            try {
                final SAXBuilder sb = new SAXBuilder();
                myDoc = sb.build(ResourceUtils.getFile(bizDriverFileName));
            } catch (final Exception e) {
                logger.info("Exception parsing bizdriver file : " + e);
                logger.printStackTrace(e);
                final Element ele = new Element("BizDriverFileError");
                myDoc = new Document(ele);
            }
        }
        return myDoc;
    }

    /**
	 * @return the urlFileName
	 */
    public String getUrlFileName() {
        return urlFileName;
    }

    /**
	 * @param myDoc the myDoc to set
	 */
    public void setMyDoc(Document myDoc) {
        this.myDoc = myDoc;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }
}
