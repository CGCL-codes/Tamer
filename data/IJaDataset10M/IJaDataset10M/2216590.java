package org.jboss.jaxr.juddi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.management.MBeanServer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import net.taylor.juddi.RegistryEngineServiceMBean;
import org.apache.juddi.IRegistry;
import org.apache.juddi.datatype.RegistryObject;
import org.apache.juddi.datatype.response.DispositionReport;
import org.apache.juddi.datatype.response.ErrInfo;
import org.apache.juddi.datatype.response.Result;
import org.apache.juddi.error.BusyException;
import org.apache.juddi.error.FatalErrorException;
import org.apache.juddi.error.RegistryException;
import org.apache.juddi.error.UnsupportedException;
import org.apache.juddi.handler.HandlerMaker;
import org.apache.juddi.handler.IHandler;
import org.apache.juddi.registry.RegistryEngine;
import org.apache.juddi.util.Config;
import org.apache.juddi.util.xml.XMLUtils;
import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxy;
import org.jboss.mx.util.MBeanProxyCreationException;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.ws.WSException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Servlet that represents the JUDDI Registry Based on the JUDDI standard
 * servlets
 * 
 * @author <mailto:Anil.Saldhana@jboss.org>Anil Saldhana
 * @since May 18, 2005
 * @version $Revision: 1.1 $
 */
public class JUDDIServlet extends HttpServlet {

    /** The serialVersionUID */
    private static final long serialVersionUID = 8768916717023791095L;

    private static DocumentBuilder docBuilder = null;

    private static HandlerMaker maker = HandlerMaker.getInstance();

    private static Logger log = Logger.getLogger(JUDDIServlet.class);

    /**
	 * 
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("Allow", "POST");
        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "The request " + "method 'GET' is not allowed by UDDI API.");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/xml; charset=utf-8");
        SOAPMessage soapReq = null;
        SOAPMessage soapRes = null;
        try {
            MessageFactory msgFactory = MessageFactory.newInstance();
            soapReq = msgFactory.createMessage(null, req.getInputStream());
            soapRes = msgFactory.createMessage();
            if (log.isTraceEnabled()) {
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                soapReq.writeTo(bs);
                log.trace("Request received::" + bs.toString());
            }
            SOAPBody soapReqBody = soapReq.getSOAPBody();
            Element uddiReq = (Element) soapReqBody.getFirstChild();
            if (uddiReq == null) throw new FatalErrorException("A UDDI request was not " + "found in the SOAP message.");
            String function = uddiReq.getLocalName();
            if ((function == null) || (function.trim().length() == 0)) throw new FatalErrorException("The name of the UDDI request " + "could not be identified.");
            IHandler requestHandler = maker.lookup(function);
            if (requestHandler == null) throw new UnsupportedException("The UDDI request " + "type specified is unknown: " + function);
            String generic = uddiReq.getAttribute("generic");
            if (generic == null) throw new FatalErrorException("A UDDI generic attribute " + "value was not found for UDDI request: " + function + " (The " + "'generic' attribute must be present)"); else if (!generic.equals(IRegistry.UDDI_V2_GENERIC)) throw new UnsupportedException("Currently only UDDI v2 " + "requests are supported. The generic attribute value " + "received was: " + generic);
            RegistryObject uddiReqObj = requestHandler.unmarshal(uddiReq);
            if (uddiReqObj == null) throw new FatalErrorException("Uddi Request is null");
            RegistryObject uddiResObj = null;
            RegistryEngine registry = getJuddiService().getRegistryEngine();
            if ((registry != null) && (registry.isAvailable())) uddiResObj = registry.execute(uddiReqObj); else throw new BusyException("The Registry is currently unavailable.");
            IHandler responseHandler = maker.lookup(uddiResObj.getClass().getName());
            if (responseHandler == null) throw new FatalErrorException("The response object " + "type is unknown: " + uddiResObj.getClass().getName());
            DocumentBuilder docBuilder = getDocumentBuilder();
            Document document = docBuilder.newDocument();
            Element element = document.createElement("temp");
            responseHandler.marshal(uddiResObj, element);
            log.debug("Response that will be sent:");
            log.debug(XMLUtils.toString((Element) element.getFirstChild()));
            Element el = (Element) element.getFirstChild();
            soapRes = this.createSOAPMessage(el);
        } catch (Exception ex) {
            String faultCode = null;
            String faultString = null;
            String faultActor = null;
            String errno = null;
            String errCode = null;
            String errMsg = null;
            if (ex instanceof RegistryException) {
                log.error("RegistryException::", ex);
                RegistryException rex = (RegistryException) ex;
                faultCode = rex.getFaultCode();
                faultString = rex.getFaultString();
                faultActor = rex.getFaultActor();
                DispositionReport dispRpt = rex.getDispositionReport();
                if (dispRpt != null) {
                    Result result = null;
                    ErrInfo errInfo = null;
                    Vector results = dispRpt.getResultVector();
                    if ((results != null) && (!results.isEmpty())) result = (Result) results.elementAt(0);
                    if (result != null) {
                        errno = String.valueOf(result.getErrno());
                        errInfo = result.getErrInfo();
                        if (errInfo != null) {
                            errCode = errInfo.getErrCode();
                            errMsg = errInfo.getErrMsg();
                        }
                    }
                }
            } else {
                log.error(ex.getMessage(), ex);
                faultCode = "Server";
                faultString = ex.getMessage();
                faultActor = null;
                errno = String.valueOf(Result.E_FATAL_ERROR);
                errCode = Result.lookupErrCode(Result.E_FATAL_ERROR);
                errMsg = Result.lookupErrText(Result.E_FATAL_ERROR);
            }
            try {
                SOAPBody soapResBody = soapRes.getSOAPBody();
                SOAPFault soapFault = soapResBody.addFault();
                if (faultCode == null) faultCode = "Unavailable";
                soapFault.setFaultCode(faultCode);
                soapFault.setFaultString(faultString);
                soapFault.setFaultActor(faultActor);
                Detail faultDetail = soapFault.addDetail();
                SOAPElement dispRpt = faultDetail.addChildElement("dispositionReport", "", IRegistry.UDDI_V2_NAMESPACE);
                dispRpt.setAttribute("generic", IRegistry.UDDI_V2_GENERIC);
                dispRpt.setAttribute("operator", Config.getOperator());
                SOAPElement result = dispRpt.addChildElement("result");
                result.setAttribute("errno", errno);
                SOAPElement errInfo = result.addChildElement("errInfo");
                errInfo.setAttribute("errCode", errCode);
                errInfo.setValue(errMsg);
            } catch (Exception e) {
                log.error("A serious error has occured while assembling the SOAP Fault.", e);
            }
        } finally {
            try {
                if (log.isTraceEnabled()) {
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    soapRes.writeTo(bs);
                    log.trace("Response being sent::" + bs.toString());
                }
                soapRes.writeTo(res.getOutputStream());
            } catch (SOAPException sex) {
                log.error("SOAPException::", sex);
            }
        }
    }

    /**
	 * 
	 */
    private DocumentBuilder getDocumentBuilder() {
        if (docBuilder == null) docBuilder = createDocumentBuilder();
        return docBuilder;
    }

    /**
	 * 
	 */
    private synchronized DocumentBuilder createDocumentBuilder() {
        if (docBuilder != null) return docBuilder;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException pcex) {
            log.error("ParserConfigurationException::", pcex);
        }
        return docBuilder;
    }

    private SOAPMessage createSOAPMessage(Element elem) throws Exception {
        String prefix = "uddi";
        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPMessage message = msgFactory.createMessage();
        message.getSOAPHeader().detachNode();
        SOAPPart soapPart = message.getSOAPPart();
        SOAPBody soapBody = soapPart.getEnvelope().getBody();
        String uddins = IRegistry.UDDI_V2_NAMESPACE;
        Name bodyName = factory.createName(elem.getNodeName(), prefix, uddins);
        SOAPBodyElement bodyElement = soapBody.addBodyElement(bodyName);
        bodyElement.addNamespaceDeclaration(prefix, uddins);
        appendAttributes(bodyElement, elem.getAttributes(), factory);
        appendElements(bodyElement, elem.getChildNodes(), factory);
        return message;
    }

    private void appendAttributes(SOAPElement bodyElement, NamedNodeMap nnm, SOAPFactory factory) throws SOAPException {
        int len = nnm != null ? nnm.getLength() : 0;
        for (int i = 0; i < len; i++) {
            Node n = nnm.item(i);
            String nodename = n.getNodeName();
            String nodevalue = n.getNodeValue();
            if ("xmlns".equals(nodename)) continue;
            if ("xml:lang".equals(nodename)) {
                Name xmlLang = factory.createName("lang", "xml", "http://www.w3.org/TR/REC-xml/");
                bodyElement.addAttribute(xmlLang, nodevalue);
            } else bodyElement.addAttribute(factory.createName(nodename), nodevalue);
        }
    }

    private void appendElements(SOAPElement bodyElement, NodeList nlist, SOAPFactory factory) throws SOAPException {
        String prefix = "uddi";
        String uddins = IRegistry.UDDI_V2_NAMESPACE;
        int len = nlist != null ? nlist.getLength() : 0;
        for (int i = 0; i < len; i++) {
            Node node = nlist.item(i);
            short nodeType = node != null ? node.getNodeType() : -100;
            if (Node.ELEMENT_NODE == nodeType) {
                Element el = (Element) node;
                Name name = factory.createName(el.getNodeName(), prefix, uddins);
                SOAPElement attachedEl = bodyElement.addChildElement(name);
                appendAttributes(attachedEl, el.getAttributes(), factory);
                appendElements(attachedEl, el.getChildNodes(), factory);
            } else if (nodeType == Node.TEXT_NODE) {
                bodyElement.addTextNode(node.getNodeValue());
            }
        }
    }

    private RegistryEngineServiceMBean getJuddiService() {
        RegistryEngineServiceMBean ds;
        try {
            MBeanServer server = MBeanServerLocator.locateJBoss();
            ds = (RegistryEngineServiceMBean) MBeanProxy.get(RegistryEngineServiceMBean.class, RegistryEngineServiceMBean.OBJECT_NAME, server);
            if (ds == null) throw new WSException("Cannot obtain JuddiServiceMBean: " + RegistryEngineServiceMBean.OBJECT_NAME);
            return ds;
        } catch (MBeanProxyCreationException ex) {
            throw new WSException("Cannot obtain proxy to JuddiServiceMBean");
        }
    }
}
