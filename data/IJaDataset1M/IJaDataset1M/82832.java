package org.openscience.cdk.io.formats;

/**
 * @cdk.module test-ioformats
 */
public class SDFFormatTest extends ChemFormatMatcherTest {

    public SDFFormatTest() {
        super.setChemFormatMatcher((IChemFormatMatcher) SDFFormat.getInstance());
    }
}
