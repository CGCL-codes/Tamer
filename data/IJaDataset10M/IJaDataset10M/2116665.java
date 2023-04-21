package org.openscience.cdk.isomorphism;

import org.junit.Before;
import org.junit.Test;

/**
 *  Concrete test class for Ullman subgraph isomorphism
 *  @author Mark Rijnbeek
 *  @cdk.module test-standard
 */
public class UllmanSubgraphIsomorphismTest extends SubgraphIsomorphismTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    SubgraphIsomorphism.Algorithm getAlgorithm() {
        return SubgraphIsomorphism.Algorithm.ULLMAN;
    }

    @Test
    public void placeholder() {
    }
}
