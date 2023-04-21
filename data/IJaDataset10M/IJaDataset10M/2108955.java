package org.timothyb89.jtelirc.message.type;

import java.util.LinkedList;
import java.util.List;
import org.timothyb89.jtelirc.filter.Filter;
import org.timothyb89.jtelirc.message.Message;
import org.timothyb89.jtelirc.server.Server;

/**
 *
 * @author tim
 */
public class MessageIdentifier {

    /**
	 * The server instance
	 */
    private Server server;

    /**
	 * The list of MessageTypes.
	 */
    private List<MessageType> types;

    /**
	 * Constructs a MessageIdentifier.
	 * @param server The server
	 */
    public MessageIdentifier(Server server) {
        this.server = server;
        initIdentifiers();
    }

    /**
	 * Initializes the list of MessageIdentifiers.
	 * This attempts to read from the TYPES_FILE.
	 */
    public void initIdentifiers() {
        types = new LinkedList<MessageType>();
        types.add(new UserMessageType());
        types.add(new ServerMessageType());
        types.add(new PingMessageType());
    }

    /**
	 * Adds a message type
	 * @param type The MessageType to add
	 */
    public void addType(MessageType type) {
        types.add(type);
    }

    /**
	 * This method determines the message type for the specified raw text, and
	 * returns a message instance.
	 * @param raw the raw text of the message
	 * @return A new Message instance of the determined type, or a Message
	 *		if no specific type can be found.
	 */
    public Message getMessage(String raw) {
        MessageType type = null;
        try {
            for (MessageType t : types) {
                boolean pass = true;
                for (Filter f : t.getFilters()) {
                    if (!f.filter(raw)) {
                        pass = false;
                    }
                }
                if (pass) {
                    type = t;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (type != null) {
            return type.createMessage(server, raw);
        } else {
            System.err.println("Failed to identify message: " + raw);
            return new Message(server, raw);
        }
    }
}
