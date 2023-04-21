package net.sf.istcontract.wsimport.resources;

import net.sf.istcontract.wsimport.util.localization.Localizable;
import net.sf.istcontract.wsimport.util.localization.LocalizableMessageFactory;
import net.sf.istcontract.wsimport.util.localization.Localizer;

/**
 * Defines string formatting method for each constant in the resource file
 * 
 */
public final class XmlmessageMessages {

    private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.ws.resources.xmlmessage");

    private static final Localizer localizer = new Localizer();

    public static Localizable localizableXML_NULL_HEADERS() {
        return messageFactory.getMessage("xml.null.headers");
    }

    /**
     * Invalid argument. MimeHeaders=null
     * 
     */
    public static String XML_NULL_HEADERS() {
        return localizer.localize(localizableXML_NULL_HEADERS());
    }

    public static Localizable localizableXML_SET_PAYLOAD_ERR() {
        return messageFactory.getMessage("xml.set.payload.err");
    }

    /**
     * Couldn't set Payload in XMLMessage
     * 
     */
    public static String XML_SET_PAYLOAD_ERR() {
        return localizer.localize(localizableXML_SET_PAYLOAD_ERR());
    }

    public static Localizable localizableXML_CONTENT_TYPE_MUSTBE_MULTIPART() {
        return messageFactory.getMessage("xml.content-type.mustbe.multipart");
    }

    /**
     * Content-Type needs to be Multipart/Related and with type=text/xml
     * 
     */
    public static String XML_CONTENT_TYPE_MUSTBE_MULTIPART() {
        return localizer.localize(localizableXML_CONTENT_TYPE_MUSTBE_MULTIPART());
    }

    public static Localizable localizableXML_UNKNOWN_CONTENT_TYPE() {
        return messageFactory.getMessage("xml.unknown.Content-Type");
    }

    /**
     * Unrecognized Content-Type
     * 
     */
    public static String XML_UNKNOWN_CONTENT_TYPE() {
        return localizer.localize(localizableXML_UNKNOWN_CONTENT_TYPE());
    }

    public static Localizable localizableXML_GET_DS_ERR() {
        return messageFactory.getMessage("xml.get.ds.err");
    }

    /**
     * Couldn't get DataSource
     * 
     */
    public static String XML_GET_DS_ERR() {
        return localizer.localize(localizableXML_GET_DS_ERR());
    }

    public static Localizable localizableXML_CONTENT_TYPE_PARSE_ERR() {
        return messageFactory.getMessage("xml.Content-Type.parse.err");
    }

    /**
     * Error while parsing MimeHeaders for Content-Type
     * 
     */
    public static String XML_CONTENT_TYPE_PARSE_ERR() {
        return localizer.localize(localizableXML_CONTENT_TYPE_PARSE_ERR());
    }

    public static Localizable localizableXML_GET_SOURCE_ERR() {
        return messageFactory.getMessage("xml.get.source.err");
    }

    /**
     * Couldn't return Source
     * 
     */
    public static String XML_GET_SOURCE_ERR() {
        return localizer.localize(localizableXML_GET_SOURCE_ERR());
    }

    public static Localizable localizableXML_CANNOT_INTERNALIZE_MESSAGE() {
        return messageFactory.getMessage("xml.cannot.internalize.message");
    }

    /**
     * Cannot create XMLMessage
     * 
     */
    public static String XML_CANNOT_INTERNALIZE_MESSAGE() {
        return localizer.localize(localizableXML_CANNOT_INTERNALIZE_MESSAGE());
    }

    public static Localizable localizableXML_NO_CONTENT_TYPE() {
        return messageFactory.getMessage("xml.no.Content-Type");
    }

    /**
     * MimeHeaders doesn't contain Content-Type header
     * 
     */
    public static String XML_NO_CONTENT_TYPE() {
        return localizer.localize(localizableXML_NO_CONTENT_TYPE());
    }

    public static Localizable localizableXML_ROOT_PART_INVALID_CONTENT_TYPE(Object arg0) {
        return messageFactory.getMessage("xml.root.part.invalid.Content-Type", arg0);
    }

    /**
     * Bad Content-Type for Root Part : {0}
     * 
     */
    public static String XML_ROOT_PART_INVALID_CONTENT_TYPE(Object arg0) {
        return localizer.localize(localizableXML_ROOT_PART_INVALID_CONTENT_TYPE(arg0));
    }

    public static Localizable localizableXML_INVALID_CONTENT_TYPE(Object arg0) {
        return messageFactory.getMessage("xml.invalid.content-type", arg0);
    }

    /**
     * Invalid Content-Type: {0}
     * 
     */
    public static String XML_INVALID_CONTENT_TYPE(Object arg0) {
        return localizer.localize(localizableXML_INVALID_CONTENT_TYPE(arg0));
    }
}
