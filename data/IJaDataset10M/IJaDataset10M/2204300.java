package ow.routing;

import java.io.IOException;
import ow.id.ID;
import ow.id.IDAddressPair;
import ow.messaging.Message;
import ow.messaging.MessageHandler;
import ow.messaging.MessageSender;

/**
 * An interface through which a routing algorithm (like Kademlia) works on a routing runtime,
 * which provides facilities like communication.
 */
public interface RoutingRuntime {

    MessageSender getMessageSender();

    boolean ping(MessageSender sender, IDAddressPair target) throws IOException;

    void addMessageHandler(Class<? extends Message> messageClass, MessageHandler handler);

    RoutingResult route(ID target, int numNeighbors) throws RoutingException;

    RoutingResult route(ID target, RoutingContext initialRoutingContext, int numNeighbors) throws RoutingException;

    RoutingResult[] route(ID[] targets, int numResponsibleNodeCandidates);

    RoutingResult[] route(ID[] targets, RoutingContext[] initialRoutingContexts, int numResponsibleNodeCandidates);

    /**
	 * Return the ID and address pair of this node.
	 */
    IDAddressPair getSelfIDAddressPair();

    /**
	 * Returns the configuration on which this routing service is based.
	 */
    RoutingServiceConfiguration getConfiguration();
}
