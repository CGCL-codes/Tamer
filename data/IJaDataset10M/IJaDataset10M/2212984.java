package org.openscience.cdk.io.formats;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.tools.DataFeatures;

/**
 *  
 * @author Miguel Rojas
 * 
 * @cdk.module ioformats
 * @cdk.githash
 * @cdk.set    io-formats
 */
@TestClass("org.openscience.cdk.io.formats.MacroModelFormatTest")
public class MacroModelFormat implements IChemFormat {

    private static IResourceFormat myself = null;

    private MacroModelFormat() {
    }

    @TestMethod("testResourceFormatSet")
    public static IResourceFormat getInstance() {
        if (myself == null) myself = new MacroModelFormat();
        return myself;
    }

    @TestMethod("testGetFormatName")
    public String getFormatName() {
        return "MacroModel";
    }

    @TestMethod("testGetMIMEType")
    public String getMIMEType() {
        return "chemical/x-macromodel-input";
    }

    @TestMethod("testGetPreferredNameExtension")
    public String getPreferredNameExtension() {
        return getNameExtensions()[0];
    }

    @TestMethod("testGetNameExtensions")
    public String[] getNameExtensions() {
        return new String[] { "mmd", "mmod" };
    }

    @TestMethod("testGetReaderClassName")
    public String getReaderClassName() {
        return null;
    }

    @TestMethod("testGetWriterClassName")
    public String getWriterClassName() {
        return null;
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
