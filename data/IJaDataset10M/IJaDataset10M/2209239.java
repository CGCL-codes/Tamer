package org.xactor.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPMessage;
import org.jboss.logging.Logger;
import org.jboss.remoting.marshal.UnMarshaller;
import org.jboss.remoting.transport.http.HTTPMetadataConstants;

/**
 * A SOAP unmarshaller for WS-Coordination and WS-AtomicTransaction messages.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public class SOAPConnectionStringUnMarshaller implements UnMarshaller {

    private static final long serialVersionUID = -6165521659600623537L;

    /** Logging. */
    private static Logger log = Logger.getLogger(SOAPConnectionStringUnMarshaller.class);

    /** Is trace enabled? */
    private boolean isTraceEnabled = log.isTraceEnabled();

    /** Valid response HTTP codes. */
    private static List<Integer> validResponseCodes = new CopyOnWriteArrayList<Integer>();

    static {
        validResponseCodes.add(new Integer(HttpServletResponse.SC_OK));
        validResponseCodes.add(new Integer(HttpServletResponse.SC_ACCEPTED));
        validResponseCodes.add(new Integer(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }

    /** Our singleton instance. */
    private static SOAPConnectionStringUnMarshaller theInstance = new SOAPConnectionStringUnMarshaller();

    /** Return a reference to the singleton instance. */
    public static SOAPConnectionStringUnMarshaller getInstance() {
        return theInstance;
    }

    /** The charset used into unmarshalled messages. */
    private String charsetEncoding;

    private SOAPConnectionStringUnMarshaller() {
        this.charsetEncoding = System.getProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
    }

    /** @see org.jboss.remoting.marshal.UnMarshaller#read(java.io.InputStream, java.util.Map) */
    public Object read(InputStream inputStream, Map metadata) throws IOException, ClassNotFoundException {
        if (log.isTraceEnabled()) log.trace("Read input stream with metadata=" + metadata);
        Integer resCode = (Integer) metadata.get(HTTPMetadataConstants.RESPONSE_CODE);
        String resMessage = (String) metadata.get(HTTPMetadataConstants.RESPONSE_CODE_MESSAGE);
        if (resCode != null && validResponseCodes.contains(resCode) == false) throw new RuntimeException("Invalid HTTP server response [" + resCode + "] - " + resMessage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        IOUtils.copyStream(baos, inputStream);
        String soapMessage = new String(baos.toByteArray(), charsetEncoding);
        if (isTraceEnabled) {
            String prettySoapMessage = DOMWriter.printNode(DOMUtils.parse(soapMessage), true);
            log.trace("Incoming Response SOAPMessage\n" + prettySoapMessage);
        }
        return soapMessage;
    }

    /** @see org.jboss.remoting.marshal.UnMarshaller#cloneUnMarshaller() */
    public UnMarshaller cloneUnMarshaller() throws CloneNotSupportedException {
        return theInstance;
    }

    /** @see org.jboss.remoting.marshal.UnMarshaller#setClassLoader(java.lang.ClassLoader) */
    public void setClassLoader(ClassLoader cl) {
    }
}
