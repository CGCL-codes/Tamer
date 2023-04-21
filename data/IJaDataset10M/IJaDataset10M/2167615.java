package org.openscience.cdk.io.formats;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.tools.DataFeatures;

/**
 * See <a href="http://www.mdl.com/downloads/public/ctfile/ctfile.jsp">here</a>.
 * 
 * @cdk.module ioformats
 * @cdk.githash
 * @cdk.set    io-formats
 */
@TestClass("org.openscience.cdk.io.formats.SDFFormatTest")
public class SDFFormat implements IChemFormatMatcher {

    private static IResourceFormat myself = null;

    private SDFFormat() {
    }

    @TestMethod("testResourceFormatSet")
    public static IResourceFormat getInstance() {
        if (myself == null) myself = new SDFFormat();
        return myself;
    }

    @TestMethod("testGetFormatName")
    public String getFormatName() {
        return "MDL Structure-data file";
    }

    @TestMethod("testGetMIMEType")
    public String getMIMEType() {
        return "chemical/x-mdl-sdfile";
    }

    @TestMethod("testGetPreferredNameExtension")
    public String getPreferredNameExtension() {
        return getNameExtensions()[0];
    }

    @TestMethod("testGetNameExtensions")
    public String[] getNameExtensions() {
        return new String[] { "sdf", "sd" };
    }

    @TestMethod("testGetReaderClassName")
    public String getReaderClassName() {
        return "org.openscience.cdk.io.MDLV2000Reader";
    }

    @TestMethod("testGetWriterClassName")
    public String getWriterClassName() {
        return "org.openscience.cdk.io.SDFWriter";
    }

    public boolean matches(int lineNumber, String line) {
        if (line.equals("$$$$")) {
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
