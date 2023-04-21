package org.openscience.cdk.smsd.algorithm.rgraph;

import org.openscience.cdk.smsd.algorithm.rgraph.CDKRGraph;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.CDKTestCase;

/**
 * @cdk.module test-smsd
 * @author     Syed Asad Rahman
 * @cdk.require java1.5+
 */
public class CDKRGraphTest extends CDKTestCase {

    @Test
    public void testRGraph() {
        CDKRGraph graph = new CDKRGraph();
        Assert.assertNotNull(graph);
    }
}
