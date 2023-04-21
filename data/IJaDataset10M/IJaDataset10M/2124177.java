package com.jawise.serviceadapter.convert.soap.unused;

import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.codehaus.xfire.soap.SoapVersion;
import org.codehaus.xfire.soap.SoapVersionFactory;
import org.codehaus.xfire.util.jdom.StaxBuilder;
import org.codehaus.xfire.util.stax.FragmentStreamReader;
import org.jdom.Element;
import com.jawise.serviceadapter.convert.MessageContext;
import com.jawise.serviceadapter.convert.soap.binding.SoapException;
import com.jawise.serviceadapter.convert.soap.binding.SoapFault;
import com.jawise.serviceadapter.convert.soap.binding.SoapMessageSerializer;

public class SoapMessageReader {

    public static final String DECLARED_NAMESPACES = "declared.namespaces";

    public SoapMessageReader() {
    }

    /**
	 * reads the soap message until the body part
	 * @param ctx
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public void readToHeader(MessageContext ctx) throws Exception {
        XMLStreamReader reader = (XMLStreamReader) ctx.get("xmlstreamreader");
        Map namespaces = new HashMap();
        ctx.put(DECLARED_NAMESPACES, namespaces);
        boolean end = !reader.hasNext();
        while (!end && reader.hasNext()) {
            int event = reader.next();
            switch(event) {
                case XMLStreamReader.START_DOCUMENT:
                    String encoding = reader.getCharacterEncodingScheme();
                    break;
                case XMLStreamReader.END_DOCUMENT:
                    end = true;
                    return;
                case XMLStreamReader.END_ELEMENT:
                    break;
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals("Header")) {
                        readHeaders(ctx, namespaces);
                    } else if (reader.getLocalName().equals("Body")) {
                        readNamespaces(reader, namespaces);
                        event = reader.nextTag();
                        checkForFault(ctx);
                        return;
                    } else if (reader.getLocalName().equals("Envelope")) {
                        readNamespaces(reader, namespaces);
                        String version = reader.getNamespaceURI();
                        SoapVersion soapVersion = SoapVersionFactory.getInstance().getSoapVersion(version);
                        ctx.put("SoapVersion", soapVersion);
                        if (version == null) {
                            throw new SoapException("Invalid SOAP version: ");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
	 * read name space values and populate
	 * @param reader
	 * @param namespaces
	 */
    private void readNamespaces(XMLStreamReader reader, Map namespaces) {
        for (int i = 0; i < reader.getNamespaceCount(); i++) {
            String prefix = reader.getNamespacePrefix(i);
            if (prefix == null) prefix = "";
            namespaces.put(prefix, reader.getNamespaceURI(i));
        }
    }

    protected void checkForFault(MessageContext ctx) throws Exception {
        XMLStreamReader reader = (XMLStreamReader) ctx.get("xmlstreamreader");
        SoapVersion soapVersion = (SoapVersion) ctx.get("SoapVersion");
        if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
            if (reader.getName().equals(soapVersion.getFault())) {
                SoapMessageSerializer serializer = (SoapMessageSerializer) ctx.get("faultserializer");
                serializer.readMessage(ctx);
                SoapFault fault = (SoapFault) ctx.get("soapboady");
                throw fault;
            }
        }
    }

    /**
	 * Read in the headers as a YOM Element and create a response Header.
	 * 
	 * @param context
	 * @throws XMLStreamException
	 */
    protected void readHeaders(MessageContext ctx, Map namespaces) throws XMLStreamException {
        XMLStreamReader reader = (XMLStreamReader) ctx.get("xmlstreamreader");
        StaxBuilder builder = new StaxBuilder();
        FragmentStreamReader fsr = new FragmentStreamReader(reader);
        fsr.setAdvanceAtEnd(false);
        builder.setAdditionalNamespaces(namespaces);
        Element header = builder.build(fsr).getRootElement();
        ctx.put("header", header);
    }
}
