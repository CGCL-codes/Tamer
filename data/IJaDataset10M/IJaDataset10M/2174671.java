package org.openscience.cdk.io.formats;

/**
 * @cdk.module test-ioformats
 */
public class MPQCFormatTest extends ChemFormatTest {

    public MPQCFormatTest() {
        super.setChemFormat((IChemFormat) MPQCFormat.getInstance());
    }
}
