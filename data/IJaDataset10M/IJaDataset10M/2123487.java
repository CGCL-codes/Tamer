package models.requests;

import java.util.UUID;
import connection.WhisperTransportConnection;

/**
 * This class is responsible for requesting to join a group chat.
 * 
 * @author Thomas Pedley
 */
public class RequestGroupChatJoin extends Request {

    /** The UUID of the group whose chat is being joined. */
    private UUID groupUUID;

    /**
	 * Constructor.
	 * 
	 * @param connection The connection over which the request will be sent.
	 * @param groupUUID The UUID of the group whose chat is being joined.
	 */
    public RequestGroupChatJoin(WhisperTransportConnection connection, UUID groupUUID) {
        super(connection);
        this.groupUUID = groupUUID;
    }

    /**
	 * Execute the request.
	 */
    @Override
    public void execute() {
        addArgument("GroupUUID", groupUUID.toString());
        String request = constructRequest();
        connection.send(request);
    }

    /**
	 * Get the name of the request.
	 * 
	 * @return The name of the request.
	 */
    @Override
    protected String getName() {
        return "GroupChatJoin";
    }
}
