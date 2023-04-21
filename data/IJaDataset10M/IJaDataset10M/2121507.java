package org.xaware.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xaware.server.security.XALoginContext;
import org.xaware.shared.util.XAwareParsingException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This object holds all the parameters needed to provide input for and specify the BizView to be executed.
 * 
 * @author jweaver
 * 
 */
public class BizViewRequestOptions implements IBizViewRequestOptions {

    /** Three mutually exclusive specifications of the BizView to be executed. */
    private String m_bizViewName = null;

    private Document m_bizViewDocument = null;

    private String m_bizViewSerialized = null;

    private Map<String, Object> m_inputParams = null;

    private Map<String, Object> m_sessionVariables = null;

    /** Four mutually exclusive specifications of the Input XML structure to be used. */
    private String m_inputXmlFileName = null;

    private Document m_inputXmlDocument = null;

    private Document m_inputXmlW3CDocument = null;

    private String m_inputXmlSerialized = null;

    /** */
    private String m_resourcePath = null;

    /** Three mutually exclusive specifications of the output stream to be used. */
    private OutputStream m_outputStream = null;

    private String m_outputStreamName = null;

    private Writer m_outputWriter = null;

    /** Three mutually exclusive specifications of the input stream to be used. */
    private InputStream m_inputStream = null;

    private String m_inputStreamName = null;

    private Reader m_inputReader = null;

    private XALoginContext m_loginContext = null;

    private boolean m_isBizDocument = true;

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger(BizViewRequestOptions.class.getName());

    public BizViewRequestOptions() {
        super();
    }

    /**
     * @return Returns the m_bizViewDocument.
     */
    public Document getBizViewDocument() {
        return m_bizViewDocument;
    }

    /**
     * @param viewDocument
     *            The m_bizViewDocument to set.
     */
    public void setBizViewDocument(final Document p_viewDocument) {
        if (p_viewDocument != null) {
            m_bizViewSerialized = null;
        }
        m_bizViewDocument = p_viewDocument;
    }

    /**
     * @return the bizViewSerialized
     */
    public String getBizViewSerialized() {
        return m_bizViewSerialized;
    }

    /**
     * @param p_bizViewSerialized
     *            the bizViewSerialized to set
     */
    public void setBizViewSerialized(final String p_bizViewSerialized) {
        if (p_bizViewSerialized != null) {
            m_bizViewDocument = null;
        }
        m_bizViewSerialized = p_bizViewSerialized;
    }

    /**
     * @return Returns the m_inputParams.
     */
    public Map<String, Object> getInputParams() {
        return m_inputParams;
    }

    /**
     * @param params
     *            The Map of parameters to set. These should be String keys and any type Objects as values
     */
    public void setInputParams(final Map<String, Object> p_params) {
        m_inputParams = p_params;
    }

    /**
     * This method will create a new Map for the parameters even if other params have been added set before. The parsed
     * XML will be turned into key-value String pairs. No conversion to other Object classes is provided.
     * 
     * @param p_serializedParams
     *            Input parameters are passed in as an XML structure. For example, if custName and custAddress are input
     *            parameters for the BizDocument, sInputParam would look like this:
     *            &lt;MYDATA&gt;&lt;custName&gt;Bob&lt;/custName&gt;&lt;custAddress&gt; 123
     *            Main&lt;/custAddress>&lt;/MYDATA&gt;
     * @throws XAwareParsingException
     *             If there is a problem parsing the supplied XML.
     */
    public void setInputParams(final String p_serializedParams) throws XAwareParsingException {
        m_inputParams = new HashMap<String, Object>();
        if (p_serializedParams == null || "".equals(p_serializedParams.trim())) {
            return;
        }
        Element elem = null;
        try {
            if (p_serializedParams != null && p_serializedParams.length() > 0) {
                final StringReader reader = new StringReader(p_serializedParams);
                final Document dataDoc = new SAXBuilder().build(reader);
                elem = dataDoc.getRootElement();
            }
            if (elem != null) {
                final Iterator iter = elem.getChildren().iterator();
                while (iter.hasNext()) {
                    final Element e = (Element) iter.next();
                    m_inputParams.put(e.getName(), e.getText());
                }
            }
        } catch (final JDOMException e) {
            lf.severe("JDOM exception:" + e.getMessage(), "XABizViewThread", "setInputParam");
            throw new XAwareParsingException("JDOM exception:" + e.getMessage());
        } catch (final IOException ioe) {
            lf.severe("IO exception:" + ioe.getMessage(), "XABizViewThread", "setInputParam");
            throw new XAwareParsingException("IO exception:" + ioe.getMessage());
        }
    }

    /**
     * @return Returns the m_inputXmlDocument.
     */
    public Document getInputXmlDocument() {
        return m_inputXmlDocument;
    }

    /**
     * @param xmlDocument
     *            The m_inputXmlDocument to set.
     */
    public void setInputXmlDocument(final Document p_xmlDocument) {
        m_inputXmlDocument = p_xmlDocument;
    }

    /**
     * @return Returns the m_inputXmlW3CDocument.
     */
    public Document getInputXmlW3CDocument() {
        return m_inputXmlW3CDocument;
    }

    /**
     * @param xmlW3CDocument
     *            The m_inputXmlW3CDocument to set.
     */
    public void setInputXmlW3CDocument(final Document p_xmlW3CDocument) {
        m_inputXmlDocument = p_xmlW3CDocument;
    }

    /**
     * @return Returns the m_inputXmlFileName.
     */
    public String getInputXmlResourceName() {
        return m_inputXmlFileName;
    }

    /**
     * @param xmlFileName
     *            The m_inputXmlFileName to set.
     */
    public void setInputXmlResourceName(final String p_xmlFileName) {
        m_inputXmlFileName = p_xmlFileName;
    }

    /**
     * @return Returns the m_is.
     */
    public InputStream getInputXmlStream() {
        return m_inputStream;
    }

    /**
     * @param p_inputStream
     *            The input stream to set.
     */
    public void setInputXmlStream(final InputStream p_inputStream) {
        m_inputStream = p_inputStream;
    }

    public Reader getInputXmlReader() {
        if (m_inputReader == null && m_inputStream != null) {
            m_inputReader = new InputStreamReader(m_inputStream);
        }
        return m_inputReader;
    }

    public void setInputXmlReader(final Reader p_reader) {
        m_inputReader = p_reader;
    }

    /**
     * @return the inputXmlSerialized
     */
    public String getInputXmlSerialized() {
        return m_inputXmlSerialized;
    }

    /**
     * @param p_inputXmlSerialized
     *            the Serialized input Xml structure to set
     */
    public void setInputXmlSerialized(final String p_inputXmlSerialized) {
        m_inputXmlSerialized = p_inputXmlSerialized;
    }

    /**
     * @return Returns the m_inputStreamName.
     */
    public String getInputStreamName() {
        return m_inputStreamName;
    }

    /**
     * @param p_inputStreamName
     *            The m_inputStreamName to set.
     */
    public void setInputStreamName(final String p_inputStreamName) {
        m_inputStreamName = p_inputStreamName;
    }

    /**
     * @return Returns the XALoginContext.
     */
    public XALoginContext getLoginContext() {
        return m_loginContext;
    }

    /**
     * @param p_loginContext
     *            The XALoginContext to set.
     */
    public void setLoginContext(final XALoginContext p_loginContext) {
        m_loginContext = p_loginContext;
    }

    /**
     * @return Returns the m_os.
     */
    public OutputStream getOutputStream() {
        return m_outputStream;
    }

    /**
     * @param m_os
     *            The m_os to set.
     */
    public void setOutputStream(final OutputStream p_outputStream) {
        m_outputStream = p_outputStream;
    }

    /**
     * @return Returns the m_outputStreamName.
     */
    public String getOutputStreamName() {
        return m_outputStreamName;
    }

    /**
     * @param streamName
     *            The m_outputStreamName to set.
     */
    public void setOutputStreamName(final String p_outputStreamName) {
        m_outputStreamName = p_outputStreamName;
    }

    /**
     * @return Returns the m_resourcePath.
     */
    public String getResourcePath() {
        return m_resourcePath;
    }

    /**
     * @param path
     *            The m_resourcePath to set.
     */
    public void setResourcePath(final String p_path) {
        m_resourcePath = p_path;
    }

    public Writer getOutputWriter() {
        return m_outputWriter;
    }

    public void setOutputWriter(final Writer p_outputWriter) {
        m_outputWriter = p_outputWriter;
    }

    public boolean isBizDocument() {
        return m_isBizDocument;
    }

    public void setIsBizDocument(final boolean p_isBizDocument) {
        m_isBizDocument = p_isBizDocument;
    }

    /**
     * Name used for reporting/logging purposes.
     * 
     * @see org.xaware.api.IBizViewRequestOptions#getBizViewName()
     */
    public String getBizViewName() {
        return m_bizViewName;
    }

    /**
     * Sets the Name used for reporting/logging purposes.
     * 
     * @param p_bizViewName
     */
    public void setBizViewName(final String p_bizViewName) {
        m_bizViewName = p_bizViewName;
    }

    /**
     * @return Session variable map
     */
    public Map<String, Object> getSessionVariables() {
        return m_sessionVariables;
    }

    /**
     * 
     * @param params
     *            The Map of session variables to set. These should be String keys and any type 
     *            Objects as values.
     */
    public void setSessionVariables(Map<String, Object> p_sessionVariables) {
        m_sessionVariables = p_sessionVariables;
    }
}
