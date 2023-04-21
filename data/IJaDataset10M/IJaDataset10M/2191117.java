package org.uddi4j;

import org.uddi4j.response.DispositionReport;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents a UDDI defined error. This exception is thrown in cases
 * where the return value on the UDDI API cannot indicate an error condition.<P>
 *
 * UDDIException usually contains a disposition report that has detailed
 * information about the UDDI error, as defined by the UDDI specification.
 * If the response is a SOAP fault, but does not contain a disposition
 * report, this exception may still be thrown.<P>
 *
 * The DispositionReport class contains definitions for the various error values
 * that can be returned.<P>
 * 
 * @author David Melgar (dmelgar@us.ibm.com)
 */
public class UDDIException extends Exception {

    static final String UDDI_TAG = "Fault";

    String faultCode = null;

    String faultString = null;

    String faultActor = null;

    String detail = null;

    Element detailElement = null;

    DispositionReport dispositionReport = null;

    public UDDIException() {
    }

    /**
    * Constructer that parses the XML dom tree and extracts
    * useful attributes.
    *
    * @param el     Root element of the tree within the SOAP body.
    */
    public UDDIException(Element el, boolean createDispositionReport) {
        if (isValidElement(el)) {
            NodeList nl;
            Element tmp;
            nl = el.getElementsByTagName("faultcode");
            if (nl.getLength() == 0) {
                nl = el.getElementsByTagNameNS(UDDIElement.SOAPNS, "faultcode");
            }
            if (nl != null && nl.getLength() > 0) {
                tmp = (Element) nl.item(0);
                faultCode = getText(tmp);
            }
            nl = el.getElementsByTagName("faultstring");
            if (nl.getLength() == 0) {
                nl = el.getElementsByTagNameNS(UDDIElement.SOAPNS, "faultstring");
            }
            if (nl != null && nl.getLength() > 0) {
                tmp = (Element) nl.item(0);
                faultString = getText(tmp);
            }
            nl = el.getElementsByTagName("faultactor");
            if (nl.getLength() == 0) {
                nl = el.getElementsByTagNameNS(UDDIElement.SOAPNS, "faultactor");
            }
            if (nl != null && nl.getLength() > 0) {
                tmp = (Element) nl.item(0);
                faultActor = getText(tmp);
            }
            nl = el.getElementsByTagName("detail");
            if (nl.getLength() == 0) {
                nl = el.getElementsByTagNameNS(UDDIElement.SOAPNS, "detail");
            }
            if (nl != null && nl.getLength() > 0) {
                tmp = (Element) nl.item(0);
                detailElement = tmp;
                if (createDispositionReport) {
                    try {
                        nl = el.getElementsByTagName(DispositionReport.UDDI_TAG);
                        if (nl != null && nl.getLength() > 0) {
                            tmp = (Element) nl.item(0);
                            dispositionReport = new DispositionReport(tmp);
                        }
                    } catch (UDDIException e) {
                    }
                }
            }
        }
    }

    /**
    * Tests the passed in element to determine if the
    * element is a serialized version of this object.
    *
    * @param el     Root element for this object
    */
    public static boolean isValidElement(Element el) {
        return el.getNamespaceURI().equals(UDDIElement.SOAPNS) && el.getLocalName().equals(UDDI_TAG);
    }

    public String getFaultCode() {
        return faultCode;
    }

    public String getFaultString() {
        return faultString;
    }

    public String getFaultActor() {
        return faultActor;
    }

    public String getDetail() {
        return detail;
    }

    public Element getDetailElement() {
        return detailElement;
    }

    public DispositionReport getDispositionReport() {
        return dispositionReport;
    }

    /**
    * Utility function.
    * Returns text contained in child elements of the
    * passed in element
    *
    * @param el     Element
    * @return java.lang.String
    */
    protected String getText(Node el) {
        NodeList nl = el.getChildNodes();
        String result = "";
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.TEXT_NODE) {
                result += nl.item(i).getNodeValue();
            }
        }
        return result.trim();
    }

    /**
   * Provide simple text exception message
   * For full details, examine the dispositionReport object itself
   * @return java.lang.String
   */
    public String toString() {
        if (dispositionReport != null && dispositionReport.getResultVector() != null && dispositionReport.getResultVector().size() > 0) {
            org.uddi4j.response.Result result = (org.uddi4j.response.Result) dispositionReport.getResultVector().elementAt(0);
            if (result != null) return result.getErrInfo().getText();
        }
        return getFaultString();
    }
}
