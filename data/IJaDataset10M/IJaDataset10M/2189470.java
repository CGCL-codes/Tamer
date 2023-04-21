package org.openscience.cdk.io.formats;

/**
 * @cdk.module test-ioformats
 */
public class DMol3FormatTest extends ChemFormatTest {

    public DMol3FormatTest() {
        super.setChemFormat((IChemFormat) DMol3Format.getInstance());
    }
}
