package org.openscience.cdk.io.formats;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.tools.DataFeatures;

/**
 * See <a href="http://www.q-chem.com/">here</a>.
 * 
 * @cdk.module ioformats
 * @cdk.githash
 * @cdk.set    io-formats
 */
@TestClass("org.openscience.cdk.io.formats.QChemFormatTest")
public class QChemFormat implements IChemFormatMatcher {

    private static IResourceFormat myself = null;

    private QChemFormat() {
    }

    @TestMethod("testResourceFormatSet")
    public static IResourceFormat getInstance() {
        if (myself == null) myself = new QChemFormat();
        return myself;
    }

    @TestMethod("testGetFormatName")
    public String getFormatName() {
        return "Q-Chem";
    }

    @TestMethod("testGetMIMEType")
    public String getMIMEType() {
        return null;
    }

    @TestMethod("testGetPreferredNameExtension")
    public String getPreferredNameExtension() {
        return getNameExtensions()[0];
    }

    @TestMethod("testGetNameExtensions")
    public String[] getNameExtensions() {
        return new String[] { "qc" };
    }

    @TestMethod("testGetReaderClassName")
    public String getReaderClassName() {
        return null;
    }

    @TestMethod("testGetWriterClassName")
    public String getWriterClassName() {
        return null;
    }

    public boolean matches(int lineNumber, String line) {
        if (line.indexOf("Welcome to Q-Chem") != -1) {
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
