package com.neurogrid.simulation.root;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author <a href="mailto:sam@neurogrid.com">Sam Joseph</a>
 *
 * an implementation of the Action interface that allows us to 
 * take a message and have it handled by the node it has arrived
 * at
 */
public class InjectMessageAction implements Action {

    private static Category o_cat = Category.getInstance(HandleMessageAction.class.getName());

    /**
	 * initialize the logging system
	 *
	 * @param p_conf      configuration filename
	 */
    public static void init(String p_conf) {
        BasicConfigurator.configure();
        PropertyConfigurator.configure(p_conf);
        o_cat.info("HandleMessagesAction logging Initialized");
    }

    private Message o_message = null;

    /**
	 * @param p_message  - the message that we are going to act upon 
	 */
    public InjectMessageAction(Message p_message) {
        o_message = p_message;
    }

    /**
	 * @see com.neurogrid.simulation.root.Action#execute()
	 */
    public boolean execute() throws Exception {
        Node x_node = o_message.getLocation();
        if (o_cat.isDebugEnabled()) {
            o_cat.debug("Going to handle message at: " + x_node.getNodeID());
        }
        x_node.injectMessage(o_message);
        return false;
    }
}
