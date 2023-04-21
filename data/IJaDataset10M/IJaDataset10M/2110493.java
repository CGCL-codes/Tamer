package edu.indiana.extreme.xbaya.streaming;

import javax.xml.namespace.QName;
import xsul5.wsdl.WsdlDefinitions;
import edu.indiana.extreme.util.wsdl.WSDLUtil;
import edu.indiana.extreme.xbaya.component.ComponentException;
import edu.indiana.extreme.xbaya.component.ws.WSComponent;
import edu.indiana.extreme.xbaya.graph.Graph;
import edu.indiana.extreme.xbaya.graph.Node;
import edu.indiana.extreme.xbaya.graph.ws.WSNode;

/**
 * @author Chathura Herath
 */
public class StreamReceiveComponent extends WSComponent {

    public static final String NAME = "StreamReceiver";

    public StreamReceiveComponent(WsdlDefinitions wsdl) throws ComponentException {
        this(wsdl, null, null);
    }

    /**
     * Constructs a WSComponent.
     * 
     * @param wsdl
     * @param portTypeQName
     * @param operationName
     * @throws ComponentException 
     * @throws ComponentException
     */
    public StreamReceiveComponent(WsdlDefinitions wsdl, QName portTypeQName, String operationName) throws ComponentException {
        super(wsdl, portTypeQName, operationName);
    }

    public Node createNode(Graph graph) {
        return createNode(graph, new StreamReceiveNode(graph));
    }
}
