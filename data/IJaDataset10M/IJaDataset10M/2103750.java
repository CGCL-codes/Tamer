package edu.uci.ics.jung.io.graphml.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.*;
import edu.uci.ics.jung.io.graphml.GraphMetadata.EdgeDefault;

/**
 * Parses graph elements.
 *
 * @author Nathan Mittler - nathan.mittler@gmail.com
 */
public class GraphElementParser<G extends Hypergraph<V, E>, V, E> extends AbstractElementParser<G, V, E> {

    public GraphElementParser(ParserContext<G, V, E> parserContext) {
        super(parserContext);
    }

    @SuppressWarnings("unchecked")
    public GraphMetadata parse(XMLEventReader xmlEventReader, StartElement start) throws GraphIOException {
        try {
            GraphMetadata graphMetadata = new GraphMetadata();
            Iterator iterator = start.getAttributes();
            while (iterator.hasNext()) {
                Attribute attribute = (Attribute) iterator.next();
                String name = attribute.getName().getLocalPart();
                String value = attribute.getValue();
                if (graphMetadata.getId() == null && GraphMLConstants.ID_NAME.equals(name)) {
                    graphMetadata.setId(value);
                } else if (graphMetadata.getEdgeDefault() == null && GraphMLConstants.EDGEDEFAULT_NAME.equals(name)) {
                    graphMetadata.setEdgeDefault(GraphMLConstants.DIRECTED_NAME.equals(value) ? EdgeDefault.DIRECTED : EdgeDefault.UNDIRECTED);
                } else {
                    graphMetadata.setProperty(name, value);
                }
            }
            if (graphMetadata.getEdgeDefault() == null) {
                throw new GraphIOException("Element 'graph' is missing attribute 'edgedefault'");
            }
            Map<String, V> idToVertexMap = new HashMap<String, V>();
            Collection<EdgeMetadata> edgeMetadata = new LinkedList<EdgeMetadata>();
            Collection<HyperEdgeMetadata> hyperEdgeMetadata = new LinkedList<HyperEdgeMetadata>();
            while (xmlEventReader.hasNext()) {
                XMLEvent event = xmlEventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement element = (StartElement) event;
                    String name = element.getName().getLocalPart();
                    if (GraphMLConstants.DESC_NAME.equals(name)) {
                        String desc = (String) getParser(name).parse(xmlEventReader, element);
                        graphMetadata.setDescription(desc);
                    } else if (GraphMLConstants.DATA_NAME.equals(name)) {
                        DataMetadata data = (DataMetadata) getParser(name).parse(xmlEventReader, element);
                        graphMetadata.addData(data);
                    } else if (GraphMLConstants.NODE_NAME.equals(name)) {
                        NodeMetadata metadata = (NodeMetadata) getParser(name).parse(xmlEventReader, element);
                        V vertex = getParserContext().createVertex(metadata);
                        metadata.setVertex(vertex);
                        idToVertexMap.put(metadata.getId(), vertex);
                        graphMetadata.addNodeMetadata(vertex, metadata);
                    } else if (GraphMLConstants.EDGE_NAME.equals(name)) {
                        EdgeMetadata metadata = (EdgeMetadata) getParser(name).parse(xmlEventReader, element);
                        if (metadata.isDirected() == null) {
                            metadata.setDirected(graphMetadata.getEdgeDefault() == EdgeDefault.DIRECTED);
                        }
                        E edge = getParserContext().createEdge(metadata);
                        edgeMetadata.add(metadata);
                        metadata.setEdge(edge);
                        graphMetadata.addEdgeMetadata(edge, metadata);
                    } else if (GraphMLConstants.HYPEREDGE_NAME.equals(name)) {
                        HyperEdgeMetadata metadata = (HyperEdgeMetadata) getParser(name).parse(xmlEventReader, element);
                        E edge = getParserContext().createHyperEdge(metadata);
                        hyperEdgeMetadata.add(metadata);
                        metadata.setEdge(edge);
                        graphMetadata.addHyperEdgeMetadata(edge, metadata);
                    } else {
                        getUnknownParser().parse(xmlEventReader, element);
                    }
                }
                if (event.isEndElement()) {
                    EndElement end = (EndElement) event;
                    verifyMatch(start, end);
                    break;
                }
            }
            applyKeys(graphMetadata);
            G graph = getParserContext().createGraph(graphMetadata);
            graphMetadata.setGraph(graph);
            addVerticesToGraph(graph, idToVertexMap.values());
            addEdgesToGraph(graph, edgeMetadata, idToVertexMap);
            addHyperEdgesToGraph(graph, hyperEdgeMetadata, idToVertexMap);
            return graphMetadata;
        } catch (Exception e) {
            ExceptionConverter.convert(e);
        }
        return null;
    }

    private void addVerticesToGraph(G graph, Collection<V> vertices) {
        for (V vertex : vertices) {
            graph.addVertex(vertex);
        }
    }

    @SuppressWarnings("unchecked")
    private void addEdgesToGraph(G graph, Collection<EdgeMetadata> metadata, Map<String, V> idToVertexMap) throws GraphIOException {
        for (EdgeMetadata emd : metadata) {
            E edge = (E) emd.getEdge();
            V source = idToVertexMap.get(emd.getSource());
            V target = idToVertexMap.get(emd.getTarget());
            if (source == null || target == null) {
                throw new GraphIOException("edge references undefined source or target vertex. " + "Source: " + emd.getSource() + ", Target: " + emd.getTarget());
            }
            if (graph instanceof Graph) {
                ((Graph<V, E>) graph).addEdge(edge, source, target, emd.isDirected() ? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
            } else {
                graph.addEdge(edge, new Pair<V>(source, target));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addHyperEdgesToGraph(G graph, Collection<HyperEdgeMetadata> metadata, Map<String, V> idToVertexMap) throws GraphIOException {
        for (HyperEdgeMetadata emd : metadata) {
            E edge = (E) emd.getEdge();
            List<V> verticies = new ArrayList<V>();
            List<EndpointMetadata> endpoints = emd.getEndpoints();
            for (EndpointMetadata ep : endpoints) {
                V v = idToVertexMap.get(ep.getNode());
                if (v == null) {
                    throw new GraphIOException("hyperedge references undefined vertex: " + ep.getNode());
                }
                verticies.add(v);
            }
            graph.addEdge(edge, verticies);
        }
    }
}
