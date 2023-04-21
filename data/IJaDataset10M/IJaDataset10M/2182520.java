package org.apache.axis.soap;

import org.apache.axis.Constants;
import javax.xml.namespace.QName;

/**
 * SOAP 1.2 constants
 *
 * @author Glen Daniels (gdaniels@apache.org)
 * @author Andras Avar (andras.avar@nokia.com)
 */
public class SOAP12Constants implements SOAPConstants {

    private static QName headerQName = new QName(Constants.URI_SOAP12_ENV, Constants.ELEM_HEADER);

    private static QName bodyQName = new QName(Constants.URI_SOAP12_ENV, Constants.ELEM_BODY);

    private static QName faultQName = new QName(Constants.URI_SOAP12_ENV, Constants.ELEM_FAULT);

    private static QName roleQName = new QName(Constants.URI_SOAP12_ENV, Constants.ATTR_ROLE);

    /** MessageContext property name for webmethod */
    public static final String PROP_WEBMETHOD = "soap12.webmethod";

    public String getEnvelopeURI() {
        return Constants.URI_SOAP12_ENV;
    }

    public String getEncodingURI() {
        return Constants.URI_SOAP12_ENC;
    }

    public QName getHeaderQName() {
        return headerQName;
    }

    public QName getBodyQName() {
        return bodyQName;
    }

    public QName getFaultQName() {
        return faultQName;
    }

    /**
     * Obtain the QName for the role attribute (actor/role)
     */
    public QName getRoleAttributeQName() {
        return roleQName;
    }

    /**
     * Obtain the MIME content type
     */
    public String getContentType() {
        return "application/soap+xml";
    }

    /**
     * Obtain the "next" role/actor URI
     */
    public String getNextRoleURI() {
        return Constants.URI_SOAP12_NEXT_ROLE;
    }

    /**
     * Obtain the ref attribute name
     */
    public String getAttrHref() {
        return Constants.ATTR_REF;
    }

    /**
     * Obtain the item type name of an array
     */
    public String getAttrItemType() {
        return Constants.ATTR_ITEM_TYPE;
    }

    /**
     * Obtain the Qname of VersionMismatch fault code
     */
    public QName getVerMismatchFaultCodeQName() {
        return Constants.FAULT_SOAP12_VERSIONMISMATCH;
    }

    /**
     * Obtain the Qname of Mustunderstand fault code
     */
    public QName getMustunderstandFaultQName() {
        return Constants.FAULT_SOAP12_MUSTUNDERSTAND;
    }

    /**
     * Obtain the QName of the SOAP array type
     */
    public QName getArrayType() {
        return Constants.SOAP_ARRAY12;
    }
}
