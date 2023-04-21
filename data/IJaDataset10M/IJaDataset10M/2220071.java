package org.apache.ws.security;

import javax.xml.namespace.QName;

/**
 * SOAP 1.2 constants
 *
 * @author Glen Daniels (gdaniels@apache.org)
 * @author Andras Avar (andras.avar@nokia.com)
 */
public class SOAP12Constants implements SOAPConstants {

    private static QName headerQName = new QName(WSConstants.URI_SOAP12_ENV, WSConstants.ELEM_HEADER);

    private static QName bodyQName = new QName(WSConstants.URI_SOAP12_ENV, WSConstants.ELEM_BODY);

    private static QName roleQName = new QName(WSConstants.URI_SOAP12_ENV, WSConstants.ATTR_ROLE);

    /**
     * MessageContext property name for webmethod
     */
    public static final String PROP_WEBMETHOD = "soap12.webmethod";

    public String getEnvelopeURI() {
        return WSConstants.URI_SOAP12_ENV;
    }

    public QName getHeaderQName() {
        return headerQName;
    }

    public QName getBodyQName() {
        return bodyQName;
    }

    /**
     * Obtain the QName for the role attribute (actor/role)
     */
    public QName getRoleAttributeQName() {
        return roleQName;
    }

    /**
     * Obtain the "next" role/actor URI
     */
    public String getNextRoleURI() {
        return WSConstants.URI_SOAP12_NEXT_ROLE;
    }

    /**
     * Obtain the Mustunderstand string
     */
    public String getMustunderstand() {
        return "true";
    }
}
