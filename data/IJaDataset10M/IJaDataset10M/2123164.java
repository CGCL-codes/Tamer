package org.openscience.cdk.io.formats;

/**
 * @cdk.module test-ioformats
 */
public class PubChemSubstanceXMLFormatTest extends ChemFormatMatcherTest {

    public PubChemSubstanceXMLFormatTest() {
        super.setChemFormatMatcher((IChemFormatMatcher) PubChemSubstanceXMLFormat.getInstance());
    }
}
