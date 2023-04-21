package bgu.reasoning.propogation.graph;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.util.VertexPair;
import org.tzi.use.uml.mm.MClass;
import org.tzi.use.uml.mm.MModel;

public class EmptyGraphFactory implements GraphGenerator {

    protected SimpleGraph<MClass, VertexPair<MClass>> graph;

    @Override
    public SimpleGraph<MClass, VertexPair<MClass>> buildGraph(MModel model) {
        EdgeFactory<MClass, VertexPair<MClass>> ef = new EdgeFactory<MClass, VertexPair<MClass>>() {

            @Override
            public VertexPair<MClass> createEdge(MClass sourceVertex, MClass targetVertex) {
                return new VertexPair<MClass>(sourceVertex, targetVertex);
            }
        };
        this.graph = new SimpleGraph<MClass, VertexPair<MClass>>(ef);
        addVertices(model);
        return this.graph;
    }

    private void addVertices(MModel model) {
        for (Object o : model.classes()) {
            if (o instanceof MClass) {
                MClass cls = (MClass) o;
                this.graph.addVertex(cls);
            }
        }
    }
}
