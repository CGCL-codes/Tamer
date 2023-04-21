package org.apache.rahas.sandhana.metadata.model;

import javax.xml.namespace.QName;

public class FedTokenBuilderConstants {

    public static final String FED_NS = "http://schemas.xmlsoap.org/ws/2006/12/federation";

    public static final String ADDRESS_NS = "http://www.w3.org/2005/08/addressing";

    public static final String SECURITY_TOKEN_REFERENCE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    public static final String XML_SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";

    public static final String MEX_NS = "http://schemas.xmlsoap.org/ws/2004/09/mex";

    public static final String DS_NS = "http://www.w3.org/2000/09/xmldsig#";

    public static final String FED_PREFIX = "fed";

    public static final String MEX_PREFIX = "mex";

    public static final String ADDRESS_PREFIX = "wsa";

    public static final String DS_PREFIX = "ds";

    public static final String DS_LN = "Signature";

    public static final QName FEDERATION_METADATA = new QName(FedTokenBuilderConstants.FED_NS, FedTokenConstants.FEDERATION_METADATA, FedTokenBuilderConstants.FED_PREFIX);

    public static final QName FEDERATION = new QName(FedTokenBuilderConstants.FED_NS, FedTokenConstants.FEDERATION, FedTokenBuilderConstants.FED_PREFIX);

    public static final QName FEDERATION_ID = new QName(FedTokenBuilderConstants.FED_NS, FedTokenConstants.FEDERATION_ID, FedTokenBuilderConstants.FED_PREFIX);

    public static final QName FEDERATION_INCLUDE = new QName(FedTokenBuilderConstants.FED_NS, FedTokenConstants.FEDERATION_INCLUDE, FedTokenBuilderConstants.FED_PREFIX);

    public static final QName ATTRIBUTE_SERVICE_ENDPOINT = new QName(FedTokenBuilderConstants.FED_NS, FedTokenConstants.ATTRIBUTE_SERVICE_ENDPOINT, FedTokenBuilderConstants.FED_PREFIX);

    public static final QName TOKEN_SIGNING_KEY_INFO = new QName(FED_NS, FedTokenConstants.TOKEN_SIGNING_KEY_INFO, FED_PREFIX);

    public static final QName PSEUDONYM_SERVICE_ENDPOINT = new QName(FED_NS, FedTokenConstants.PSEUDONYM_SERVICE_ENDPOINT, FED_PREFIX);

    public static final QName SINGLE_SIGN_OUT_SUBSCRIPTION_ENDPOINT = new QName(FED_NS, FedTokenConstants.SINGLE_SIGN_OUT_SUBSCRIPTION_ENDPOINT, FED_PREFIX);

    public static final QName AUTOMATIC_PSEUDONYMS = new QName(FED_NS, FedTokenConstants.AUTOMATIC_PSEUDONYMS, FED_PREFIX);

    public static final QName ISSUER_NAME = new QName(FED_NS, FedTokenConstants.ISSUER_NAME, FED_PREFIX);

    public static final QName ISSUER_NAME_URI = new QName(FED_NS, FedTokenConstants.ISSUER_NAME_URI, FED_PREFIX);

    public static final QName ISSUER_NAMES_OFFERED = new QName(FED_NS, FedTokenConstants.ISSUER_NAMES_OFFERED, FED_PREFIX);

    public static final QName METADATA_REFERENCE = new QName(MEX_NS, FedTokenConstants.METADATA_REFERENCE, MEX_PREFIX);

    public static final QName TOKENKEY_TRANSFER_KEY_INFO = new QName(FED_NS, FedTokenConstants.TOKENKEY_TRANSFER_KEY_INFO, FED_PREFIX);

    public static final QName TOKEN_ISSUER_NAME = new QName(FED_NS, FedTokenConstants.TOKEN_ISSUER_NAME, FED_PREFIX);

    public static final QName TOKEN_ISSUER_END_POINT = new QName(FED_NS, FedTokenConstants.TOKEN_ISSUER_END_POINT, FED_PREFIX);

    public static final QName SINGLE_SIGN_OUT_NOTIFICATION_ENDPOINT = new QName(FED_NS, FedTokenConstants.SINGLE_SIGN_OUT_NOTIFICATION_ENDPOINT, FED_PREFIX);

    public static final QName TOKEN_TYPE = new QName(FED_NS, FedTokenConstants.TOKEN_TYPE, FED_PREFIX);

    public static final QName TOKEN_TYPE_URI = new QName(FED_NS, FedTokenConstants.TOKEN_TYPE, FED_PREFIX);

    public static final QName TOKEN_TYPES_OFFERED = new QName(FED_NS, FedTokenConstants.TOKEN_TYPES_OFFERED, FED_PREFIX);

    public static final QName CLAIM_TYPE = new QName(FED_NS, FedTokenConstants.CLAIM_TYPE, FED_PREFIX);

    public static final QName CLAIM_TYPE_URI = new QName(FED_NS, FedTokenConstants.CLAIM_TYPE_URI, FED_PREFIX);

    public static final QName DISPLAY_NAME = new QName(FED_NS, FedTokenConstants.DISPLAY_NAME, FED_PREFIX);

    public static final QName DESCRIPTION = new QName(FED_NS, FedTokenConstants.DESCRIPTION, FED_PREFIX);

    public static final QName URI_NAMED_CLAIM_TYPES_OFFERED = new QName(FED_NS, FedTokenConstants.URI_NAMED_CLAIM_TYPES_OFFERED, FED_PREFIX);

    public static final QName ADDRESSING = new QName(ADDRESS_NS, FedTokenConstants.ADDRESS, ADDRESS_PREFIX);
}
