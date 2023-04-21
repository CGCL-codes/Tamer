package au.gov.naa.digipres.xena.kernel.metadatawrapper;

public class TagNames {

    public static final String DEFAULT_CHECKSUM_ALGORITHM = "SHA-512";

    public static final String NAA_PACKAGE = "NAA Package";

    public static final String XENA_URI = "http://preservation.naa.gov.au/xena/1.0";

    public static final String PACKAGE_URI = "http://preservation.naa.gov.au/package/1.0";

    public static final String WRAPPER_URI = "http://preservation.naa.gov.au/wrapper/1.0";

    public static final String METADATA_URI = "http://preservation.naa.gov.au/metadata/1.0";

    public static final String METADATA_PREFIX = "metadata";

    public static final String PACKAGE_PREFIX = "package";

    public static final String WRAPPER_PREFIX = "wrapper";

    public static final String XENA = "xena";

    public static final String PACKAGE = "package";

    public static final String SIGNED_AIP = "signed-aip";

    public static final String PACKAGE_PACKAGE = PACKAGE_PREFIX + ":" + PACKAGE;

    public static final String WRAPPER_SIGNED_AIP = WRAPPER_PREFIX + ":" + SIGNED_AIP;

    public static final String META = "meta";

    public static final String PACKAGE_META = PACKAGE_PREFIX + ":" + META;

    public static final String WRAPPER_META = WRAPPER_PREFIX + ":" + META;

    public static final String XENA_META = XENA + ":" + META;

    public static final String AIP = "aip";

    public static final String WRAPPER_AIP = WRAPPER_PREFIX + ":" + AIP;

    public static final String SIGNATURE = "signature";

    public static final String WRAPPER_SIGNATURE = WRAPPER_PREFIX + ":" + SIGNATURE;

    public static final String CONTENT = "content";

    public static final String PACKAGE_CONTENT = PACKAGE_PREFIX + ":" + CONTENT;

    public static final String IDENTIFIER = "identifier";

    public static final String SOURCE = "source";

    public static final String IDENTIFIER_URI = "http://preservation.naa.gov.au/identifier/1.0";

    public static final String DC_URI = "http://purl.org/dc/elements/1.1/";

    public static final String DC_PREFIX = "dc";

    public static final String DCIDENTIFIER = DC_PREFIX + ":" + IDENTIFIER;

    public static final String DCSOURCE = DC_PREFIX + ":" + SOURCE;

    public static final String DCTERMS_URI = "http://purl.org/dc/terms/";

    public static final String DCTERMS_PREFIX = "dcterms";

    public static final String CREATED = "created";

    public static final String DCCREATED = DCTERMS_PREFIX + ":" + CREATED;

    public static final String NAA_URI = "http://preservation.naa.gov.au/naa/1.0";

    public static final String NAA_PREFIX = "naa";

    public static final String DATASOURCE = "datasource";

    public static final String NAA_DATASOURCE = NAA_PREFIX + ":" + DATASOURCE;

    public static final String DATASOURCES = "datasources";

    public static final String NAA_DATASOURCES = NAA_PREFIX + ":" + DATASOURCES;

    public static final String LASTMODIFIED = "last-modified";

    public static final String NAA_LASTMODIFIED = NAA_PREFIX + ":" + LASTMODIFIED;

    public static final String SOURCEID = "source-id";

    public static final String NAA_SOURCEID = NAA_PREFIX + ":" + SOURCEID;

    public static final String TYPE = "type";

    public static final String NAA_TYPE = NAA_PREFIX + ":" + TYPE;

    public static final String WRAPPER = "wrapper";

    public static final String NAA_WRAPPER = NAA_PREFIX + ":" + WRAPPER;
}
