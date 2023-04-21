package org.apache.axis2.transport.http;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.httpclient.methods.RequestEntity;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RESTRequestEntity implements RequestEntity {

    private byte[] bytes;

    private String charSetEnc;

    private boolean chunked;

    private OMElement element;

    private MessageContext msgCtxt;

    private String soapActionString;

    private OMOutputFormat format;

    public RESTRequestEntity(OMElement element, boolean chunked, MessageContext msgCtxt, String charSetEncoding, String soapActionString, OMOutputFormat format) {
        this.element = element;
        this.chunked = chunked;
        this.msgCtxt = msgCtxt;
        this.charSetEnc = charSetEncoding;
        this.soapActionString = soapActionString;
        this.format = format;
    }

    private void handleOMOutput(OutputStream out, boolean doingMTOM) throws XMLStreamException {
        format.setDoOptimize(doingMTOM);
        element.serializeAndConsume(out, format);
    }

    public byte[] writeBytes() throws AxisFault {
        try {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            if (!format.isOptimized()) {
                OMOutputFormat format2 = new OMOutputFormat();
                format2.setCharSetEncoding(charSetEnc);
                element.serializeAndConsume(bytesOut, format2);
                return bytesOut.toByteArray();
            } else {
                format.setCharSetEncoding(charSetEnc);
                format.setDoOptimize(true);
                element.serializeAndConsume(bytesOut, format);
                return bytesOut.toByteArray();
            }
        } catch (XMLStreamException e) {
            throw AxisFault.makeFault(e);
        } catch (FactoryConfigurationError e) {
            throw AxisFault.makeFault(e);
        }
    }

    public void writeRequest(OutputStream out) throws IOException {
        try {
            if (chunked) {
                this.handleOMOutput(out, format.isDoingSWA());
            } else {
                if (bytes == null) {
                    bytes = writeBytes();
                }
                out.write(bytes);
            }
            out.flush();
        } catch (XMLStreamException e) {
            throw AxisFault.makeFault(e);
        } catch (FactoryConfigurationError e) {
            throw AxisFault.makeFault(e);
        } catch (IOException e) {
            throw AxisFault.makeFault(e);
        }
    }

    public long getContentLength() {
        try {
            if (chunked) {
                return -1;
            } else {
                if (bytes == null) {
                    bytes = writeBytes();
                }
                return bytes.length;
            }
        } catch (AxisFault e) {
            return -1;
        }
    }

    public String getContentType() {
        String encoding = format.getCharSetEncoding();
        String contentType = format.getContentType();
        if (encoding != null) {
            contentType += "; charset=" + encoding;
        }
        if (!msgCtxt.isSOAP11() && (soapActionString != null) && !"".equals(soapActionString.trim()) && !"\"\"".equals(soapActionString.trim())) {
            contentType = contentType + ";action=\"" + soapActionString + "\";";
        }
        return contentType;
    }

    public boolean isRepeatable() {
        return true;
    }
}
