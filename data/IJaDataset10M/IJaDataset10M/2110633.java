package org.ietr.preesm.mapper.graphtransfo;

import net.sf.dftools.algorithm.importer.GMLImporter;
import net.sf.dftools.algorithm.model.dag.DAGEdge;
import net.sf.dftools.algorithm.model.dag.DAGVertex;
import net.sf.dftools.algorithm.model.sdf.SDFGraph;
import org.ietr.preesm.core.types.ImplementationPropertyNames;
import org.ietr.preesm.mapper.model.MapperDAG;
import org.ietr.preesm.mapper.model.MapperDAGEdge;
import org.ietr.preesm.mapper.model.MapperDAGVertex;
import org.ietr.preesm.mapper.model.MapperEdgeFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Importer for mapper DAG graphs
 * 
 * @author mpelcat
 * 
 */
public class GMLMapperDAGImporter extends GMLImporter<MapperDAG, MapperDAGVertex, MapperDAGEdge> {

    MapperEdgeFactory localFactory = null;

    /**
	 * Constructs a new DAG importer with the specified factories
	 */
    public GMLMapperDAGImporter() {
        super(null);
        localFactory = new MapperEdgeFactory();
    }

    @Override
    public void parseEdge(Element edgeElt, MapperDAG parentGraph) {
        DAGVertex vertexSource = vertexFromId.get(edgeElt.getAttribute("source"));
        DAGVertex vertexTarget = vertexFromId.get(edgeElt.getAttribute("target"));
        DAGEdge edge = parentGraph.addEdge(vertexSource, vertexTarget);
        parseKeys(edgeElt, edge);
    }

    @Override
    public MapperDAG parseGraph(Element graphElt) {
        MapperDAG graph = new MapperDAG(localFactory, null);
        parseKeys(graphElt, graph);
        graph.setReferenceSdfGraph((SDFGraph) graph.getPropertyBean().getValue(ImplementationPropertyNames.Graph_SdfReferenceGraph));
        NodeList childList = graphElt.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).getNodeName().equals("node")) {
                Element vertexElt = (Element) childList.item(i);
                parseNode(vertexElt, graph);
            }
        }
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).getNodeName().equals("edge")) {
                Element edgeElt = (Element) childList.item(i);
                parseEdge(edgeElt, graph);
            }
        }
        return graph;
    }

    @Override
    public MapperDAGVertex parsePort(Element portElt, MapperDAG parentGraph) {
        return null;
    }

    @Override
    public MapperDAGVertex parseNode(Element vertexElt, MapperDAG parentGraph) {
        MapperDAGVertex vertex = new MapperDAGVertex();
        parentGraph.addVertex(vertex);
        vertex.setId(vertexElt.getAttribute("id"));
        vertexFromId.put(vertex.getId(), vertex);
        parseKeys(vertexElt, vertex);
        return vertex;
    }
}
