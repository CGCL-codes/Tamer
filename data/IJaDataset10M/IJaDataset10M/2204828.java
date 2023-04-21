package org.openscience.cdk.isomorphism;

import org.junit.Before;
import org.junit.Test;

/**
 * Concrete test class for VF2 subgraph isomorphism
 * @author Mark Rijnbeek, EBI
 * @cdk.module test-standard
 */
public class VF2SubgraphIsomorphismTest extends SubgraphIsomorphismTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    SubgraphIsomorphism.Algorithm getAlgorithm() {
        return SubgraphIsomorphism.Algorithm.VF2;
    }

    @Test
    public void placeHolderTest() {
    }
}
