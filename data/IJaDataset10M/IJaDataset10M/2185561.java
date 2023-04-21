package org.sdf4j.importer;

import java.util.HashMap;
import org.sdf4j.factories.PSDFVertexFactory;
import org.sdf4j.model.AbstractVertex;
import org.sdf4j.model.parameters.Argument;
import org.sdf4j.model.psdf.PSDFGraph;
import org.sdf4j.model.psdf.PSDFInitVertex;
import org.sdf4j.model.psdf.PSDFSubInitVertex;
import org.sdf4j.model.psdf.parameters.DomainParsingException;
import org.sdf4j.model.psdf.parameters.DynamicParameterDomainFactory;
import org.sdf4j.model.psdf.parameters.PSDFDynamicArgument;
import org.sdf4j.model.psdf.parameters.PSDFDynamicParameter;
import org.sdf4j.model.sdf.SDFAbstractVertex;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GMLPSDFImporter extends GMLSDFImporter {

    PSDFGraph graph;

    public GMLPSDFImporter() {
        super();
    }

    public PSDFGraph parseGraph(Element graphElt) {
        graph = new PSDFGraph();
        NodeList childList = graphElt.getChildNodes();
        parseKeys(graphElt, graph.getPropertyBean(), "graph");
        parseParameters(graph, graphElt);
        parseDynamicParameters(graph, graphElt);
        parseVariables(graph, graphElt);
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).getNodeName().equals("node")) {
                Element vertexElt = (Element) childList.item(i);
                graph.addVertex(parseNode(vertexElt));
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

    public SDFAbstractVertex parseNode(Element vertexElt) {
        SDFAbstractVertex vertex;
        HashMap<String, String> attributes = new HashMap<String, String>();
        for (int i = 0; i < vertexElt.getAttributes().getLength(); i++) {
            attributes.put(vertexElt.getAttributes().item(i).getNodeName(), vertexElt.getAttributes().item(i).getNodeValue());
        }
        vertex = PSDFVertexFactory.createVertex(attributes);
        vertex.setId(vertexElt.getAttribute("id"));
        vertex.setName(vertexElt.getAttribute("id"));
        parseKeys(vertexElt, vertex.getPropertyBean(), "node");
        vertexFromId.put(vertex.getId(), vertex);
        parseArguments(vertex, vertexElt);
        parseGraphDescription(vertex, vertexElt);
        if (vertex instanceof PSDFInitVertex) {
            parseAffectedParameters((PSDFInitVertex) vertex, vertexElt);
        } else if (vertex instanceof PSDFSubInitVertex) {
            parseAffectedParameters((PSDFSubInitVertex) vertex, vertexElt);
        }
        return vertex;
    }

    protected void parseDynamicParameters(PSDFGraph graph, Element parentElt) {
        NodeList childList = parentElt.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).getNodeName().equals("data") && ((Element) childList.item(i)).getAttribute("key").equals("dynamic_parameters")) {
                NodeList argsList = childList.item(i).getChildNodes();
                for (int j = 0; j < argsList.getLength(); j++) {
                    if (argsList.item(j).getNodeName().equals("parameter")) {
                        Element param = (Element) argsList.item(j);
                        PSDFDynamicParameter parameter = new PSDFDynamicParameter(param.getAttribute("name"));
                        graph.addDynamicParameter(parameter);
                    }
                }
            }
        }
    }

    protected void parseAffectedParameters(PSDFInitVertex initVertex, Element parentElt) {
        NodeList childList = parentElt.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).getNodeName().equals("data") && ((Element) childList.item(i)).getAttribute("key").equals("affected_parameters")) {
                NodeList argsList = childList.item(i).getChildNodes();
                for (int j = 0; j < argsList.getLength(); j++) {
                    if (argsList.item(j).getNodeName().equals("parameter")) {
                        Element param = (Element) argsList.item(j);
                        PSDFDynamicParameter p = graph.getDynamicParameter(param.getAttribute("name"));
                        if (p != null) {
                            initVertex.addAffectedParameter(p);
                            try {
                                if (param.getAttribute("value") != null) {
                                    p.setDomain(DynamicParameterDomainFactory.create(param.getAttribute("value")));
                                }
                            } catch (DomainParsingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    protected void parseAffectedParameters(PSDFSubInitVertex initVertex, Element parentElt) {
        NodeList childList = parentElt.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).getNodeName().equals("data") && ((Element) childList.item(i)).getAttribute("key").equals("affected_parameters")) {
                NodeList argsList = childList.item(i).getChildNodes();
                for (int j = 0; j < argsList.getLength(); j++) {
                    if (argsList.item(j).getNodeName().equals("parameter")) {
                        Element param = (Element) argsList.item(j);
                        PSDFDynamicParameter p = graph.getDynamicParameter(param.getAttribute("name"));
                        if (p != null) {
                            initVertex.addAffectedParameter(p);
                            try {
                                if (param.getAttribute("value") != null) {
                                    p.setDomain(DynamicParameterDomainFactory.create(param.getAttribute("value")));
                                }
                            } catch (DomainParsingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    protected void parseArguments(AbstractVertex vertex, Element parentElt) {
        NodeList childList = parentElt.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).getNodeName().equals("data") && ((Element) childList.item(i)).getAttribute("key").equals("arguments")) {
                NodeList argsList = childList.item(i).getChildNodes();
                for (int j = 0; j < argsList.getLength(); j++) {
                    if (argsList.item(j).getNodeName().equals("argument")) {
                        Element arg = (Element) argsList.item(j);
                        if (arg.getAttribute("value").charAt(0) == '$' && graph instanceof PSDFGraph) {
                            PSDFGraph pGraph = ((PSDFGraph) graph);
                            vertex.addArgument(new PSDFDynamicArgument(arg.getAttribute("name"), pGraph.getDynamicParameter(arg.getAttribute("value").substring(1))));
                        } else {
                            vertex.addArgument(new Argument(arg.getAttribute("name"), arg.getAttribute("value")));
                        }
                    }
                }
            }
        }
    }
}
