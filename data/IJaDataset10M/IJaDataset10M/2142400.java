package org.ws4d.java.constants;

/**
 * Constants used by WS MetadataExchange.
 */
public class MEXConstants {

    /** The namespace name for WS MetadataExchange. */
    public static final String WSX_NAMESPACE_NAME = "http://schemas.xmlsoap.org/ws/2004/09/mex";

    /** The default prefix for the WSMEX namespace. */
    public static final String WSX_NAMESPACE_PREFIX = "wsx";

    public static final String WSX_ACTION_GETMETADATA_REQUEST = WSX_NAMESPACE_NAME + "/GetMetadata/Request";

    public static final int WSX_ACTION_GETMETADATA_REQUEST_HASH = WSX_ACTION_GETMETADATA_REQUEST.hashCode();

    public static final String WSX_ACTION_GETMETADATA_RESPONSE = WSX_NAMESPACE_NAME + "/GetMetadata/Response";

    public static final int WSX_ACTION_GETMETADATA_RESPONSE_HASH = WSX_ACTION_GETMETADATA_RESPONSE.hashCode();

    public static final String WSX_ELEM_GETMETADATA = "GetMetadata";

    /** "Dialect". */
    public static final String WSX_ELEM_DIALECT = "Dialect";

    /** "Metadata". */
    public static final String WSX_ELEM_METADATA = "Metadata";

    /** "MetadataSection". */
    public static final String WSX_ELEM_METADATASECTION = "MetadataSection";

    /** "Identifier". */
    public static final String WSX_ELEM_IDENTIFIER = "Identifier";

    /** "Location". */
    public static final String WSX_ELEM_LOCATION = "Location";

    public static final String WSX_DIALECT_WSDL = "http://schemas.xmlsoap.org/wsdl";
}
