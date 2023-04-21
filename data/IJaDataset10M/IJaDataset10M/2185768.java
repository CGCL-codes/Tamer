package net.sf.cdktools.test;

import net.sf.cdktools.util.RepresentationKit;
import org.openscience.cdk.AtomContainer;
import net.sf.cdktools.util.CDKKit;

/**
 * A set of tests for <code>CDKTransformer</code> capabilities.
 * 
 * @author Richard Apodaca
 */
public class CDKTransformerTest extends CDKToolsTestCase {

    /**
   * Default constructor.
   */
    public CDKTransformerTest() {
        super();
    }

    /**
   * Builds benzene and verifies that it is isomorphic with a transformed pure CDK
   * representation and an <code>Adapter</code> representation.
   *
   */
    public void testTransformBenzene() {
        RepresentationKit.buildBenzene(getAtomContainerBuilder());
        AtomContainer fromBuilder = getAtomContainerBuilder().releaseAtomContainer();
        getTransformer().transform(fromBuilder, getAtomContainerBuilder());
        AtomContainer transformedCDK = getAtomContainerBuilder().releaseAtomContainer();
        assertTrue(CDKKit.exactStructureMatch(fromBuilder, transformedCDK));
        getTransformer().transform(fromBuilder, this.getCDKAdapterBuilder());
        AtomContainer transformedAdapter = getCDKAdapterBuilder().releaseAdapter();
        assertTrue(CDKKit.exactStructureMatch(fromBuilder, transformedAdapter));
    }
}
