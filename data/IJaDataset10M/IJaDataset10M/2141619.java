package net.sf.saxon.xqj;

import net.sf.saxon.javax.xml.xquery.XQMetaData;
import net.sf.saxon.Version;
import net.sf.saxon.Configuration;
import java.util.Set;

/**
 * Saxon implementation of the XQMetaData interface
 */
public class SaxonXQMetaData implements XQMetaData {

    private Configuration config;

    /**
     * Create the metadata for a given Saxon configuration
     * @param config the Saxon configuration
     */
    public SaxonXQMetaData(Configuration config) {
        this.config = config;
    }

    public int getMaxExpressionLength() {
        return Integer.MAX_VALUE;
    }

    public int getMaxUserNameLength() {
        return Integer.MAX_VALUE;
    }

    public int getProductMajorVersion() {
        return Version.getStructuredVersionNumber()[0];
    }

    public int getProductMinorVersion() {
        return Version.getStructuredVersionNumber()[1];
    }

    public String getProductName() {
        return Version.getProductName();
    }

    public String getProductVersion() {
        return Version.getProductVersion();
    }

    public Set getSupportedXQueryEncodings() {
        return java.nio.charset.Charset.availableCharsets().keySet();
    }

    public String getUserName() {
        return null;
    }

    public int getXQJMajorVersion() {
        return 0;
    }

    public int getXQJMinorVersion() {
        return 9;
    }

    public String getXQJVersion() {
        return "0.9";
    }

    public boolean isFullAxisFeatureSupported() {
        return true;
    }

    public boolean isModuleFeatureSupported() {
        return true;
    }

    public boolean isReadOnly() {
        return true;
    }

    public boolean isSchemaImportFeatureSupported() {
        return config.isSchemaAware(Configuration.XQUERY);
    }

    public boolean isSchemaValidationFeatureSupported() {
        return config.isSchemaAware(Configuration.XQUERY);
    }

    public boolean isSerializationFeatureSupported() {
        return true;
    }

    public boolean isStaticTypingExtensionsSupported() {
        return false;
    }

    public boolean isStaticTypingFeatureSupported() {
        return false;
    }

    public boolean isTransactionSupported() {
        return false;
    }

    public boolean isUserDefinedXMLSchemaTypeSupported() {
        return config.isSchemaAware(Configuration.XQUERY);
    }

    public boolean isXQueryEncodingDeclSupported() {
        return true;
    }

    public boolean isXQueryEncodingSupported(String encoding) {
        return getSupportedXQueryEncodings().contains(encoding);
    }

    public boolean isXQueryXSupported() {
        return false;
    }

    public boolean wasCreatedFromJDBCConnection() {
        return false;
    }
}
