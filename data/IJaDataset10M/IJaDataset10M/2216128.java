package org.sdf4j.demo;

import java.util.Vector;
import org.sdf4j.generator.SDFRandomGraph;
import org.sdf4j.model.parameters.InvalidExpressionException;
import org.sdf4j.model.sdf.SDFAbstractVertex;
import org.sdf4j.model.sdf.SDFGraph;
import org.sdf4j.model.sdf.visitors.TopologyVisitor;
import org.sdf4j.model.visitors.SDF4JException;
import org.sdf4j.optimisations.clustering.internalisation.SDFInternalisation;

/**
 * Test class to test the creation of Random generating graph
 * 
 * @author pthebault
 * 
 */
public class SDFRandomGraphDemo {

    /**
	 * applet as an application.
	 * 
	 * @param args
	 *            ignored.
	 * @throws InvalidExpressionException 
	 */
    public static void main(String[] args) throws InvalidExpressionException {
        int nbVertex = 50, minInDegree = 1, maxInDegree = 2, minOutDegree = 1, maxOutDegree = 2, minrate = 1, maxrate = 2;
        SDFAdapterDemo applet = new SDFAdapterDemo();
        SDFRandomGraph test = new SDFRandomGraph();
        TopologyVisitor topo = new TopologyVisitor();
        SDFGraph demoGraph = test.createRandomGraph(nbVertex, minInDegree, maxInDegree, minOutDegree, maxOutDegree, minrate, maxrate, 15);
        try {
            demoGraph.accept(topo);
        } catch (SDF4JException e) {
            e.printStackTrace();
        }
        applet.init(demoGraph);
        Vector<SDFAbstractVertex> Top = new Vector<SDFAbstractVertex>();
        Vector<SDFAbstractVertex> Bottom = new Vector<SDFAbstractVertex>();
        SDFInternalisation.FindTopBottom(demoGraph, Top, Bottom);
        System.out.println(Top);
    }
}
