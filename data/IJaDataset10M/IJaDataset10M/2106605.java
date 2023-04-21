package com.google.devtools.depan.eclipse.persist;

import com.google.devtools.depan.graph.api.Relation;
import com.google.devtools.depan.graph.basic.BasicEdge;
import com.google.devtools.depan.model.GraphEdge;
import com.google.devtools.depan.model.GraphModel;
import com.google.devtools.depan.model.GraphNode;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * {@code XStream} converter to handle {@code GraphEdge}s.  This converter
 * assumes that the {@code UnmarshallingContext} as an {@code GraphModel} entry
 * that can be retrieved by the {@code GraphModel.class} key.
 * 
 * @author Original lost in the mists of time
 */
public class EdgeConverter implements Converter {

    public static final String EDGE_DEF_TAG = "graph-edge";

    private static final String TAIL_TAG = "tail";

    private static final String HEAD_TAG = "head";

    private static final String RELATION_TAG = "relation";

    private final Mapper mapper;

    public EdgeConverter(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canConvert(Class type) {
        return GraphEdge.class.equals(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        GraphEdge edge = (GraphEdge) source;
        Relation relation = edge.getRelation();
        writer.startNode(RELATION_TAG);
        Class<?> actualType = relation.getClass();
        Class<?> defaultType = mapper.defaultImplementationOf(BasicEdge.class);
        if (!actualType.equals(defaultType)) {
            writer.addAttribute(mapper.aliasForAttribute("class"), mapper.serializedClass(actualType));
        }
        context.convertAnother(relation);
        writer.endNode();
        writer.startNode(HEAD_TAG);
        context.convertAnother(edge.getHead().getId());
        writer.endNode();
        writer.startNode(TAIL_TAG);
        context.convertAnother(edge.getTail().getId());
        writer.endNode();
    }

    /**
   * {@inheritDoc}
   * <p>
   * This implementation assumes that a {@code GraphModel}, used to find nodes,
   * can be retrieved from the {@code UnmarshallingContext} with the key
   * {@code GraphModel.class}.
   */
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        try {
            GraphModel graph = (GraphModel) context.get(GraphModel.class);
            reader.moveDown();
            Relation relation = unmarshallRelation(reader, context);
            reader.moveUp();
            reader.moveDown();
            GraphNode head = unmarshallGraphNode(reader, context, graph);
            reader.moveUp();
            reader.moveDown();
            GraphNode tail = unmarshallGraphNode(reader, context, graph);
            reader.moveUp();
            GraphEdge result = new GraphEdge(head, tail, relation);
            return result;
        } catch (RuntimeException err) {
            err.printStackTrace();
            throw err;
        }
    }

    private Relation unmarshallRelation(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String classAttribute = reader.getAttribute(mapper.aliasForAttribute("class"));
        Class<?> resultClass = mapper.realClass(classAttribute);
        Relation relation = (Relation) context.convertAnother(null, resultClass);
        return relation;
    }

    private GraphNode unmarshallGraphNode(HierarchicalStreamReader reader, UnmarshallingContext context, GraphModel graph) {
        String nodeId = reader.getValue();
        GraphNode result = (GraphNode) graph.findNode(nodeId);
        if (null == result) {
            throw new IllegalStateException("Edge reference to undefined node " + nodeId);
        }
        return result;
    }
}
