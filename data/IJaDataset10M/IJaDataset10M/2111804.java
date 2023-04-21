package org.ws4d.java.wsdl.soap12;

import java.io.IOException;
import org.ws4d.java.constants.WSDLConstants;
import org.ws4d.java.types.QName;
import org.ws4d.java.types.URI;
import org.ws4d.java.wsdl.WSDLPort;
import org.xmlpull.v1.XmlSerializer;

/**
 * 
 */
public class SOAP12DocumentLiteralHTTPPort extends WSDLPort {

    private URI location;

    /**
	 * 
	 */
    public SOAP12DocumentLiteralHTTPPort() {
        this(null);
    }

    /**
	 * @param name
	 */
    public SOAP12DocumentLiteralHTTPPort(String name) {
        this(name, null);
    }

    /**
	 * @param name
	 * @param bindingName
	 */
    public SOAP12DocumentLiteralHTTPPort(String name, QName bindingName) {
        super(name, bindingName);
    }

    public void serializePortExtension(XmlSerializer serializer) throws IOException {
        serializer.startTag(WSDLConstants.SOAP12_BINDING_NAMESPACE_NAME, WSDLConstants.WSDL_ELEM_ADDRESS);
        serializer.attribute(null, WSDLConstants.WSDL_ATTRIB_LOCATION, location.toString());
        serializer.endTag(WSDLConstants.SOAP12_BINDING_NAMESPACE_NAME, WSDLConstants.WSDL_ELEM_ADDRESS);
    }

    /**
	 * @return the location
	 */
    public URI getLocation() {
        return location;
    }

    /**
	 * @param location the location to set
	 */
    public void setLocation(URI location) {
        this.location = location;
    }
}
