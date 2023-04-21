package gossipServices.gossipMessages;

import java.util.Queue;
import gossipServices.basic.nodeDescriptors.NodeDescriptor;
import messages.Message;

/**
 * Description: 
 *
 */
public interface BootstrapingMessage extends Message {

    Queue<NodeDescriptor> getCarryingNodeDescriptor();
}
