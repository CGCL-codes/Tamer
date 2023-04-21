package org.ws4d.java.constants;

/**
 * Constants of WS MetadataExchange.
 */
public class WXFConstants {

    /** The namespace name for WS MetadataExchange. */
    public static final String WXF_NAMESPACE_NAME = "http://schemas.xmlsoap.org/ws/2004/09/transfer";

    /** The default prefix for the WSMEX namespace. */
    public static final String WXF_NAMESPACE_PREFIX = "wxf";

    public static final String WXF_ACTION_GET = WXF_NAMESPACE_NAME + "/Get";

    public static final int WXF_ACTION_GET_HASH = WXF_ACTION_GET.hashCode();

    public static final String WXF_ACTION_GETRESPONSE = WXF_NAMESPACE_NAME + "/GetResponse";

    public static final int WXF_ACTION_GETRESPONSE_HASH = WXF_ACTION_GETRESPONSE.hashCode();

    public static final String WXF_ACTION_GET_REQUEST = WXF_NAMESPACE_NAME + "/Get/Request";

    public static final int WXF_ACTION_GET_REQUEST_HASH = WXF_ACTION_GET_REQUEST.hashCode();

    public static final String WXF_ACTION_GET_RESPONSE = WXF_NAMESPACE_NAME + "/Get/Response";

    public static final int WXF_ACTION_GET_RESPONSE_HASH = WXF_ACTION_GET_RESPONSE.hashCode();

    public static final String WXF_ELEM_GETMETADATA = "GetMetadata";

    /** "Dialect". */
    public static final String WXF_ELEM_DIALECT = "Dialect";

    /** "Metadata". */
    public static final String WXF_ELEM_METADATA = "Metadata";

    /** "MetadataSection". */
    public static final String WXF_ELEM_METADATASECTION = "MetadataSection";

    /** "Identifier". */
    public static final String WXF_ELEM_IDENTIFIER = "Identifier";

    public static final String WXF_DIALECT_WSDL = "http://schemas.xmlsoap.org/wsdl";
}
