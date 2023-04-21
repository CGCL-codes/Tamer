package fate.flow;

import fate.flow.nodes.Node;
import fate.flow.container.ContainerLogEntry;
import fate.flow.container.ContainerStatus;
import fate.flow.container.Container;
import org.apache.log4j.Logger;

/**
 * Handles the flow for a single Container at a time. 
 * 
 * The Container is checked for the presence of the <code>fate.startnode</code>
 * attribute. If this attribute is present and not empty, processing starts
 * at that node. In all other cases the processing will start at the root
 * of the flow.
 * 
 * @author Jeroen van Bergen
 */
public class FlowManager {

    /**
     * The number of FlowManagers that are currently active. 
     */
    private static int numberOfActiveFlowManagers = 0;

    /**
     * The NodeFactory that is used to get instances of the Nodes to call.
     */
    private NodeFactory nodeFactory = NodeFactory.getInstance();

    /**
     * Logs the messages from this class.
     */
    private Logger logger = Logger.getLogger(FlowManager.class);

    private static FlowManagerFactory flowManagerController = FlowManagerFactory.getInstance();

    /**
     * Creates a new FlowManager.
     */
    protected FlowManager() {
        numberOfActiveFlowManagers++;
    }

    /**
     * Gets the number of FlowManagers that are currently active.
     * @return The number of FlowManagers that are currently active.
     */
    public static synchronized int getNumberOfActiveFlowManagers() {
        return numberOfActiveFlowManagers;
    }

    /**
     * Processes the Container, starting the processing in the node indicated by
     * the fate.startnode attribute of the container.
     * 
     * @param container The Container to process.
     * @throws java.lang.Exception
     */
    public void process(Container container) throws Exception {
        logger.debug("FlowManager " + toString() + " is processing container " + container.getId());
        try {
            executeFlow(container, container.getAttribute(AttributeNames.fate_startnode).toString());
        } catch (Exception e) {
            logger.error("Could not process Container " + container.getId(), e);
        }
        try {
            switch(container.getStatus()) {
                case OK:
                    {
                        container.setStatus(ContainerStatus.DONE);
                        break;
                    }
                case WARNING:
                    {
                        container.setStatus(ContainerStatus.DONE_WARNING);
                        break;
                    }
                case DONE:
                    {
                        break;
                    }
                case DONE_WARNING:
                    {
                        break;
                    }
                case DONE_ERROR:
                    {
                        break;
                    }
                case HOLD:
                    {
                        break;
                    }
                default:
                    {
                        container.setStatus(ContainerStatus.DONE);
                    }
            }
        } catch (Exception e) {
            logger.error("Could not set the status of the container after processing has completed");
        } finally {
            flowManagerController.releaseFlowManager(this);
            numberOfActiveFlowManagers--;
        }
    }

    /**
     * Calls the indicated Node to process the Container. This is a recursive method.
     * @param container The container to process.
     * @throws java.lang.Exception
     */
    private void executeFlow(Container container, String nodeName) throws Exception {
        if (container.getStatus().isFinalState()) {
            return;
        }
        Node currentNode = nodeFactory.getNode(nodeName);
        try {
            boolean callChildren = currentNode.process(container);
            if (container.getStatus().isFinalState()) {
                return;
            }
            if (container.getAttribute(AttributeNames.fate_subflow) != null) {
                String subFlowStartNodeName = container.getAttribute(AttributeNames.fate_subflow).toString();
                if (subFlowStartNodeName.equals(nodeName)) {
                    logger.warn("Node " + nodeName + " is calling itself recursively!");
                }
                container.removeAttribute(AttributeNames.fate_subflow);
                executeFlow(container, subFlowStartNodeName);
            }
            if (callChildren) {
                for (String childName : currentNode.getChildrenNames()) {
                    executeFlow(container, childName);
                }
            }
        } catch (Exception e) {
            container.addLogEntry(new ContainerLogEntry("FlowManager", currentNode.getName() + " threw exception " + e.getMessage(), ContainerStatus.DONE_ERROR));
        } finally {
            nodeFactory.returnNode(currentNode);
        }
    }
}
