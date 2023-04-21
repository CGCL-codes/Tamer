package org.openscience.cdk.io.formats;

/**
 * @cdk.module test-ioformats
 */
public class CMLRSSFormatTest extends ChemFormatTest {

    public CMLRSSFormatTest() {
        super.setChemFormat((IChemFormat) CMLRSSFormat.getInstance());
    }
}
