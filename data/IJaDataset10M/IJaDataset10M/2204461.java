package org.openscience.cdk.io.formats;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.tools.DataFeatures;

/**
 * @cdk.module ioformats
 * @cdk.githash
 * @cdk.set    io-formats
 */
@TestClass("org.openscience.cdk.io.formats.MDLV2000FormatTest")
public class MDLV2000Format implements IChemFormatMatcher {

    private static IResourceFormat myself = null;

    private MDLV2000Format() {
    }

    @TestMethod("testResourceFormatSet")
    public static IResourceFormat getInstance() {
        if (myself == null) myself = new MDLV2000Format();
        return myself;
    }

    @TestMethod("testGetFormatName")
    public String getFormatName() {
        return "MDL Molfile V2000";
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
        return new String[] { "mol" };
    }

    @TestMethod("testGetReaderClassName")
    public String getReaderClassName() {
        return "org.openscience.cdk.io.MDLV2000Reader";
    }

    @TestMethod("testGetWriterClassName")
    public String getWriterClassName() {
        return "org.openscience.cdk.io.MDLV2000Writer";
    }

    public boolean matches(int lineNumber, String line) {
        if (lineNumber == 4 && (line.indexOf("v2000") >= 0 || line.indexOf("V2000") >= 0)) {
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
        return getRequiredDataFeatures() | DataFeatures.HAS_2D_COORDINATES | DataFeatures.HAS_3D_COORDINATES | DataFeatures.HAS_GRAPH_REPRESENTATION;
    }

    @TestMethod("testGetRequiredDataFeatures")
    public int getRequiredDataFeatures() {
        return DataFeatures.HAS_ATOM_ELEMENT_SYMBOL;
    }
}
