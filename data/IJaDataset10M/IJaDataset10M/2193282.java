package org.xaware.server.engine.instruction.bizcomps;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xaware.server.common.XMLNamespaceUtil;
import org.xaware.server.data.soap.MimeMultiPart;
import org.xaware.server.engine.channel.soap.SoapBizDriver;
import org.xaware.server.engine.channel.soap.SoapTemplate;
import org.xaware.server.engine.channel.soap.SoapTemplateFactory;
import org.xaware.server.engine.exceptions.XAwareParsingException;
import org.xaware.server.engine.exceptions.XAwareProcessingException;
import org.xaware.server.engine.instruction.bizcomps.jmsconfig.JmsProducerConfig;
import org.xaware.server.engine.instruction.bizcomps.soap.SoapHttpMessageHelper;
import org.xaware.server.engine.instruction.bizcomps.soap.SoapHttpPostMethod;
import org.xaware.server.engine.instruction.bizcomps.soap.SoapHttpResponseHelper;
import org.xaware.server.engine.instruction.bizcomps.soap.SoapPostMethod;
import org.xaware.server.engine.instruction.bizcomps.soap.SoapRequestConfig;
import org.xaware.shared.util.ExceptionMessageHelper;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.XAwareSubstitutionException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * @author Basil Ibegbu
 * 
 */
public class SoapBizCompInst extends BaseBizComponentInst {

    /** Name of this class used in logging statements */
    private static final String m_className = "SoapBizCompInst";

    /**
     * The template factory - set by the Spring framework
     */
    private SoapTemplateFactory m_soapTemplateFactory = null;

    /**
     * The soap template
     */
    private SoapTemplate m_soapTemplate = null;

    /** reference to the {@link JmsProducerConfig}. */
    private SoapRequestConfig m_requestConfig = null;

    private enum SoapHandlingCodes {

        MAP_CODE, ERROR_CODE, RETURN_FAULT_CODE
    }

    ;

    public enum SoapOutputTypes {

        TEXT, CDATA_SECTION, XML
    }

    public enum SoapStyles {

        DOCUMENT, RPC, ENVELOPE
    }

    public enum SoapUses {

        LITERAL, ENCODED
    }

    SoapHandlingCodes m_faultHandlingCode;

    private int m_execCount = 0;

    /**
     * Default constructor
     */
    public SoapBizCompInst() {
        super(false);
    }

    @Override
    public String getName() {
        return "SoapBizCompInst";
    }

    @Override
    protected Element performOneUnitOfWork() throws XAwareException {
        m_execCount++;
        if (m_execCount > 1) {
            setNothingLeftToDo();
            return null;
        }
        if (m_returnElem != null) {
            m_returnElem.removeContent();
        }
        if (m_requestConfig.isGenerateOnly()) {
            createXmlRepresentationOfRequestMessage();
        } else {
            sendMessage();
        }
        return (Element) m_responseElem.clone();
    }

    @Override
    protected void releaseResources() throws XAwareException {
    }

    /**
     * Gets the instance of the BizDriver and the SoapTemplate.
     * 
     * @throws XAwareProcessingException
     * @throws XAwareSubstitutionException
     * 
     */
    private void setupBizDriver() throws XAwareException, XAwareSubstitutionException {
        final String methodName = "setupBizDriver";
        lf.entering(m_className, methodName);
        final String sBizDriverName = m_bizDriverConfig.getBizDriverName();
        if (sBizDriverName == null || sBizDriverName.length() == 0) {
            final String sErrorMessage = "Null bizdriver name";
            lf.severe(sErrorMessage, m_className, methodName);
            throw new XAwareException(sErrorMessage);
        }
        try {
            m_bizDriver = m_bizDriverFactory.createBizDriverInstance(m_bizDriverConfig, getBizCompContext());
            m_soapTemplate = m_soapTemplateFactory.getTemplate((SoapBizDriver) m_bizDriver, scriptNode);
            if (m_soapTemplate == null) {
                final String sErrorMessage = "SoapTemplateFactory returned null instance: " + m_bizDriverConfig.getBizDriverName();
                lf.severe(sErrorMessage, m_className, methodName);
                throw new XAwareException(sErrorMessage);
            }
            initializeFaultHandlingCode();
        } catch (final XAwareException e) {
            final String msg = ExceptionMessageHelper.getExceptionMessage(e);
            lf.severe(msg, m_className, methodName);
            throw e;
        } catch (final Exception e) {
            final String msg = ExceptionMessageHelper.getExceptionMessage(e);
            lf.severe(msg, m_className, methodName);
            throw new XAwareException("Problem getting the BizDriver", e);
        } finally {
            lf.exiting(m_className, methodName);
        }
    }

    @Override
    protected void setupResources() throws XAwareException {
        final String methodName = "setupResources";
        lf.entering(m_className, methodName);
        setupBizDriver();
        lf.exiting(m_className, methodName);
    }

    /**
     * Reads in the request configuration information.
     * 
     * @throws XAwareException
     *             if this required configuration information is missing.
     */
    private void setupRequestConfigInfo() throws XAwareException {
        final String methodName = "setupRequestConfigInfo";
        lf.entering(m_className, methodName);
        lf.finest("creating a new SoapRequestConfig", m_className, "setupRequestConfigInfo");
        m_requestConfig = new SoapRequestConfig(getBizCompContext(), lf, m_requestElem, m_returnElem);
        final Element soapEnvelope = m_requestConfig.getSoapEnvelope();
        if (soapEnvelope != null) {
            addElementToPreprocess(soapEnvelope);
        }
        if (m_returnElem == null) {
            m_returnElem = m_requestConfig.getReturnElement();
        }
        lf.exiting(m_className, methodName);
    }

    @Override
    protected void transferConfigInfo() throws XAwareException {
        setupDriverConfigInfo();
        setupRequestConfigInfo();
    }

    /**
     * Provided for Spring to inject the {@link SoapTemplateFactory} instance.
     * 
     * @param soapTemplateFactory
     *            the SoapTemplateFactory to set
     */
    public final void setSoapTemplateFactory(final SoapTemplateFactory soapTemplateFactory) {
        m_soapTemplateFactory = soapTemplateFactory;
    }

    /**
     * This method creates an XML structure to represent the expected parts of the message that can be sent later be
     * sent by some protocol other than HTTP. That structure is placed as contents of the output element.
     * 
     * @param p_output
     * @param p_inputDataElement
     * @return output element has the xml structure of the outbound message.
     * @throws XAwareSubstitutionException
     *             If there is a problem substituting values into the soap envelope template.
     * 
     */
    private void createXmlRepresentationOfRequestMessage() throws XAwareSubstitutionException {
        Element elemToReturn;
        final MimeMultiPart reqMultipartConfig = m_requestConfig.getRequestMultipartRelatedConfig();
        if (reqMultipartConfig != null) {
            elemToReturn = reqMultipartConfig.getConfigElement();
        } else {
            elemToReturn = m_requestConfig.getRequestSoapEnvelopeElement();
        }
        if (elemToReturn != null) {
            m_returnElem.addContent(elemToReturn.detach());
        }
    }

    /**
     * This method utilizes the HttpMethod created for the type of outbound message specified in the .xbc file. The
     * SOAPAction header is added to the HTTP message as specified in the SOAP 1.1 spec.
     * 
     * @param inputDataElement
     * @param output
     * @return
     * @throws XAwareException
     *             passed on from addFaultToOutput
     */
    private void sendMessage() throws XAwareException {
        SoapHttpPostMethod post = null;
        String errMsg;
        try {
            post = getHttpMethod();
            post.setDoAuthentication(true);
            addSoapActionHeader(post);
            post.setRequestHeader("Connection", "close");
            dumpPostHeaders(post.getRequestHeaders());
            final int status = m_soapTemplate.executeMethod(post);
            lf.fine("Post method return code: " + status, m_className, "sendMessage");
            if (status == HttpStatus.SC_OK) {
                createOutput(post, m_requestConfig.getOutputType());
            } else {
                if (!canGetSoapFault(post)) {
                    errMsg = "HTTP Post method error: " + status + " " + post.getStatusText();
                    lf.severe(errMsg, m_className, "sendMessage");
                    lf.severe("Post method return value:" + post.getResponseBodyAsString(), m_className, "sendMessage");
                    throw new XAwareProcessingException(errMsg);
                }
            }
        } catch (final IOException ex) {
            errMsg = "IOException: " + ex;
            lf.severe(errMsg, m_className, "sendMessage");
            throw new XAwareProcessingException(ex);
        } catch (final JDOMException caughtEx) {
            errMsg = "JDOMException: " + caughtEx.getMessage();
            lf.severe(errMsg, m_className, "sendMessage");
            throw new XAwareProcessingException(caughtEx);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
    }

    /**
     * Logs the given headers a the finest level.
     * 
     * @param requestHeaders
     */
    private void dumpPostHeaders(final Header[] requestHeaders) {
        for (int i = 0; i < requestHeaders.length; i++) {
            final Header aHeader = requestHeaders[i];
            lf.finest("Header name: " + aHeader.getName() + " value: " + aHeader.getValue());
        }
    }

    /**
     * @param p_inputDataElement
     * @return
     * @throws XAwareSubstitutionException
     *             If there is a substitution error in creating the data to send.
     * @throws JDOMException
     * @throws IOException
     *             If there is a problem reading the file.
     */
    private SoapHttpPostMethod getHttpMethod() throws XAwareException, JDOMException, IOException {
        if (m_requestConfig.isMultipartRequestMessage()) {
            return SoapHttpMessageHelper.createMultipartMessage(m_soapTemplate.getServiceAddress(), m_requestConfig.getRequestMultipartRelatedConfig(), getSoapDataToSend(), getBizCompContext(), m_requestConfig.getRequestCharSet());
        } else {
            return createSinglePartMessage();
        }
    }

    /**
     * Creates a PostMethod to send the message to and receive the response from the service.
     * 
     * @return
     * @throws XAwareException
     *             If the supplied charset is not valid.
     */
    private SoapHttpPostMethod createSinglePartMessage() throws XAwareException {
        final String serviceAddress = m_soapTemplate.getServiceAddress();
        final SoapPostMethod post = new SoapPostMethod(serviceAddress);
        final String charset = m_requestConfig.getRequestCharSet();
        final String contentType = "text/xml";
        final String sSoapData = getSoapDataToSend();
        StringRequestEntity requestEntity;
        try {
            requestEntity = new StringRequestEntity(sSoapData, contentType, charset);
        } catch (final UnsupportedEncodingException e) {
            throw new XAwareException("Invalid charset value: " + charset == null ? contentType : charset, e);
        }
        ((PostMethod) post).setRequestEntity(requestEntity);
        return post;
    }

    /**
     * 
     * @param inputDataElement
     * @return if no soap envelope is found an empty string is returned otherwise a string representing the processed
     *         (for substitutions) soap envelope and all sub elements.
     *         <p>
     *         Note that if needed, xa:script commands should be added to the xa:preprocess element and the results
     *         mapped to the envelope (using xa:data_path. Those results will then be substituted during bizComponent
     *         initilaization.
     * @throws XAwareSubstitutionException
     */
    private String getSoapDataToSend() throws XAwareSubstitutionException {
        lf.entering(m_className, "getSoapDataToSend");
        final Element soapEnvelope = m_requestConfig.getSoapEnvelope();
        final String body = elemToString(soapEnvelope);
        lf.finest("Outbound SOAP Envelope: \n" + body, m_className, "getSoapDataToSend()");
        return body;
    }

    /**
     * Gets the String equivalent of the given Element
     */
    public final String elemToString(final Element elem) {
        if (elem == null) {
            return "";
        }
        final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        return outputter.outputString(elem);
    }

    /**
     * Adds the SOAPAction header to the request message that is either backward compatible for the rpc/encoded or
     * compliant with WS-I Basic Profile 1.0 for quoting the value of the header.
     * 
     * @param post
     */
    private void addSoapActionHeader(final HttpMethod post) {
        String soapAction = m_requestConfig.getSoapAction();
        if (m_requestConfig.isRpc() && m_requestConfig.isEncoded()) {
            if (soapAction != null) {
                post.setRequestHeader(XAwareConstants.HTTP_MSG_HEADER_NAME_SOAPACTION, soapAction);
            }
        } else {
            if (soapAction == null || "".equals(soapAction)) {
                soapAction = "\"\"";
            }
            if (!soapAction.startsWith("\"")) {
                soapAction = "\"" + soapAction;
            }
            if (!soapAction.endsWith("\"")) {
                soapAction = soapAction + "\"";
            }
            post.setRequestHeader(XAwareConstants.HTTP_MSG_HEADER_NAME_SOAPACTION, soapAction);
        }
    }

    /**
     * This is the method that takes the response from the HttpMethod and turns it into an xml structure and places that
     * structure into the return element. The return element's first element child is then handed in as the relative
     * element to produce the response element structure. That structure is then added as the contents of the output
     * element.
     * 
     * @param sOut
     * @param p_outputElem
     * @param p_outputType
     * @param inputDataElement
     * @return
     * @throws IOException
     *             If there is a problem reading the response stream.
     * @throws XAwareException
     * @throws NoSuchElementException
     */
    @SuppressWarnings("unchecked")
    private void createOutput(final SoapHttpPostMethod post, final SoapOutputTypes p_outputType) throws IOException, XAwareException {
        List childElems = m_returnElem.getContent();
        childElems = new ArrayList<Element>(childElems);
        final List results = new ArrayList(1);
        try {
            final Element result = SoapHttpResponseHelper.getContentFromResponseMessage(post, childElems, m_requestConfig.getReturnSoapEnvelopeElement(), m_requestConfig.getSoapStyle().toString(), m_requestConfig.getReturnMultipartRelatedConfig(), m_requestConfig.ignoresResponseNamespaces());
            m_requestConfig.applyNSMappingRules(result);
            if (!addFaultToOutput(result)) {
                result.detach();
                XMLNamespaceUtil.removeAddlNamespaces(result);
                results.add(result);
                if (p_outputType == SoapOutputTypes.TEXT || p_outputType == SoapOutputTypes.CDATA_SECTION) {
                    lf.finer("non-xml output type", m_className, "createOutput");
                    outputTextOrCdata(results);
                } else if (p_outputType == SoapOutputTypes.XML) {
                    lf.finer("xml output type", m_className, "createOutput");
                    m_returnElem.addContent(results);
                }
            }
        } catch (final JDOMException e) {
            final String errMsg = "JDOM Exception: " + e.getMessage();
            lf.severe(errMsg, m_className, "createOutput");
            throw new XAwareParsingException(e);
        }
    }

    private void outputTextOrCdata(final List results) throws XAwareException {
        final Element textElementHolder = new Element("Response", XAwareConstants.xaNamespace);
        final Element result = new Element(m_requestConfig.getResultName());
        final StringBuffer data = new StringBuffer();
        if (results != null) {
            final Iterator itr = results.iterator();
            while (itr.hasNext()) {
                final Object obj = itr.next();
                if (obj instanceof Element) {
                    final Element elem = (Element) obj;
                    data.append(elemToString(elem));
                } else {
                    data.append(obj);
                }
            }
        }
        result.setText(data.toString());
        textElementHolder.addContent(result);
        m_returnElem.addContent(textElementHolder);
    }

    /**
     * @param p_result
     * @return
     * @throws XAwareException
     *             If error_on_fault is set to yes or is null or empty and a SOAP Fault was returned by the SOAP
     *             Service.
     */
    @SuppressWarnings("unchecked")
    private boolean addFaultToOutput(final Element p_result) throws XAwareException {
        final Element envelope = m_requestConfig.getSoapEnvelope(p_result);
        if (envelope != null) {
            final Element body = envelope.getChild("Body", envelope.getNamespace());
            if (body != null) {
                final Element fault = body.getChild("Fault", envelope.getNamespace());
                if (fault != null) {
                    final String faultMsg = elemToString(fault);
                    switch(m_faultHandlingCode) {
                        case ERROR_CODE:
                            final String sErrorMessage = faultMsg;
                            lf.severe(sErrorMessage, m_className, "addFaultToOutput");
                            throw new XAwareException(sErrorMessage);
                        case RETURN_FAULT_CODE:
                            final List content = new ArrayList(1);
                            content.add(fault);
                            fault.detach();
                            m_returnElem.setContent(content);
                            return true;
                        default:
                        case MAP_CODE:
                            final List content2 = new ArrayList(1);
                            envelope.detach();
                            content2.add(envelope);
                            m_returnElem.setContent(content2);
                            return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * An error has occurred on the web service provider's side. Lets try to pull a soap fault out (or any other xml) of
     * the post method and stick it under the output.
     * 
     * @param p_post
     * @return
     * @throws XAwareException
     *             Passed on from #addFaultToOutput
     */
    private boolean canGetSoapFault(final HttpMethod p_post) throws XAwareException {
        try {
            final InputStream data = p_post.getResponseBodyAsStream();
            if (data == null) {
                return false;
            }
            final Reader reader = new InputStreamReader(data);
            final SAXBuilder builder = new SAXBuilder();
            final Document dataDoc = builder.build(reader);
            final Element dataDocElement = dataDoc.getRootElement();
            dataDocElement.detach();
            final ArrayList<Element> list = new ArrayList<Element>(1);
            list.add(dataDocElement);
            SoapHttpResponseHelper.setNamespaceOnSoapElements(list, new HashMap<String, Namespace>(0), m_requestConfig.ignoresResponseNamespaces());
            return addFaultToOutput(dataDocElement);
        } catch (final JDOMException caughtEx) {
            String msg = caughtEx.getMessage();
            if (msg == null || "".equals(msg)) {
                msg = caughtEx.getClass().getName();
            }
            lf.severe("Caught Exception: " + msg, m_className, "canGetSoapFault");
        } catch (final IOException ioe) {
            String msg = ioe.getMessage();
            if (msg == null || "".equals(msg)) {
                msg = ioe.getClass().getName();
            }
            lf.severe("Caught Exception: " + msg, m_className, "canGetSoapFault");
        }
        return false;
    }

    /**
     * Gets the value of the XAwareConstants.XAWARE_ON_SOAP_FAULT attribute and runs substitute on it.
     * 
     * @throws XAwareSubstitutionException
     * @return
     */
    private String getSubstitutedOnSoapFault() throws XAwareSubstitutionException, XAwareException {
        final Element scriptRoot = scriptNode.getContext().getScriptRoot();
        String onSoapFault = scriptRoot.getAttributeValue(XAwareConstants.XAWARE_ON_SOAP_FAULT, XAwareConstants.xaNamespace);
        if (onSoapFault != null) {
            onSoapFault = substitute(onSoapFault).toUpperCase() + "_CODE";
            lf.fine(XAwareConstants.XAWARE_ON_SOAP_FAULT + "=" + onSoapFault, m_className, "processRequestType");
        } else {
            onSoapFault = "MAP_CODE";
        }
        return onSoapFault;
    }

    /**
     * @param inputDataElement
     * @return
     * @throws XAwareSubstitutionException
     */
    private boolean initializeFaultHandlingCode() throws XAwareSubstitutionException, XAwareException {
        final String handlingCode = getSubstitutedOnSoapFault();
        try {
            m_faultHandlingCode = SoapHandlingCodes.valueOf(handlingCode);
        } catch (final Exception ex) {
            final String sErrorMessage = "xa:" + XAwareConstants.XAWARE_ON_SOAP_FAULT + " value is invalid. Case sensitive value must be " + XAwareConstants.XAWARE_ON_SOAP_FAULT_ERROR + " or " + XAwareConstants.XAWARE_ON_SOAP_FAULT_MAP + " or " + XAwareConstants.XAWARE_ON_SOAP_FAULT_RETURN_FAULT + " . Value is: " + handlingCode;
            lf.severe(sErrorMessage, m_className, "parseRequestElement");
            return false;
        }
        return true;
    }

    public final String getRequestCharSet() {
        return m_requestConfig.getRequestCharSet();
    }

    @Override
    protected XAwareLogger getLogger() {
        return lf;
    }
}
