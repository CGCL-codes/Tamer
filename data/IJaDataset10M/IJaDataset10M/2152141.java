package models.requests;

import java.util.UUID;
import connection.WhisperTransportConnection;

/**
 * This class is responsible for terminating a friendship.
 * 
 * @author Thomas Pedley
 */
public class RequestFriendshipTermination extends Request {

    /** The UUID of the avatar whose friendship is being terminated. */
    private UUID avatarUUID;

    /**
	 * Constructor.
	 * 
	 * @param connection The connection over which the request will be sent.
	 * @param avatarUUID The UUID of the avatar whose friendship is being terminated.
	 */
    public RequestFriendshipTermination(WhisperTransportConnection connection, UUID avatarUUID) {
        super(connection);
        this.avatarUUID = avatarUUID;
    }

    /**
	 * Execute the request.
	 */
    @Override
    public void execute() {
        addArgument("UUID", avatarUUID.toString());
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
        return "FriendshipTerminate";
    }
}
