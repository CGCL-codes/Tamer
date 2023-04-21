package org.openscience.cdk.io.formats;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.tools.DataFeatures;

/**
 * @cdk.module ioformats
 * @cdk.githash
 * @cdk.set    io-formats
 */
@TestClass("org.openscience.cdk.io.formats.INChIPlainTextFormatTest")
public class INChIPlainTextFormat implements IChemFormatMatcher {

    private static IResourceFormat myself = null;

    private INChIPlainTextFormat() {
    }

    @TestMethod("testResourceFormatSet")
    public static IResourceFormat getInstance() {
        if (myself == null) myself = new INChIPlainTextFormat();
        return myself;
    }

    @TestMethod("testGetFormatName")
    public String getFormatName() {
        return "IUPAC-NIST Chemical Identifier (Plain Text)";
    }

    @TestMethod("testGetMIMEType")
    public String getMIMEType() {
        return null;
    }

    @TestMethod("testGetPreferredNameExtension")
    public String getPreferredNameExtension() {
        return null;
    }

    @TestMethod("testGetNameExtensions")
    public String[] getNameExtensions() {
        return new String[0];
    }

    @TestMethod("testGetReaderClassName")
    public String getReaderClassName() {
        return "org.openscience.cdk.io.INChIPlainTextReader";
    }

    @TestMethod("testGetWriterClassName")
    public String getWriterClassName() {
        return null;
    }

    public boolean matches(int lineNumber, String line) {
        if (line.startsWith("INChI=")) {
            return true;
        }
        return false;
    }

    @TestMethod("testIsXMLBased")
    public boolean isXMLBased() {
        return false;
    }

    @TestMethod("testGetSupportedDataFeatures")
    public int getSupportedDataFeatures() {
        return DataFeatures.NONE;
    }

    @TestMethod("testGetRequiredDataFeatures")
    public int getRequiredDataFeatures() {
        return DataFeatures.NONE;
    }
}
