package org.neodatis.odb.core.server.message;

import org.neodatis.odb.core.server.layers.layer3.engine.Command;
import org.neodatis.odb.core.server.layers.layer3.engine.Message;

/**
 * A StoreMessageResponse is used by the Client/Server mode to answer a StoreMessage
 * 
 * @author olivier s
 * 
 */
public class DeleteBaseMessageResponse extends Message {

    public DeleteBaseMessageResponse(String baseId, String error) {
        super(Command.DELETE_BASE, baseId, null);
        setError(error);
    }

    public DeleteBaseMessageResponse(String baseId) {
        super(Command.DELETE_BASE, baseId, null);
    }
}
