package com.objectdraw.server.commands;

import com.objectdraw.server.CommunicationServer;
import com.objectdraw.server.ObjectDrawServer;
import com.objectdraw.server.data.RequestData;
import com.objectdraw.server.data.ResponseData;

/**
 * Processes a delete command from the client.
 * 
 * @author jgulik, cfruehan, Harrison
 *
 */
public class DeleteCommand extends Command {

    private static String myCommand = "delete";

    /**
	 * Takes a message with a delete command and calls removeObject() on the objectDrawServer.
	 * Also, broadcasts the message to all listening clients.
	 */
    @Override
    public void processRequest(RequestData request, ResponseData response) {
        if (myCommand.equalsIgnoreCase(request.getCommand())) {
            System.out.println(myCommand + " Command");
            ObjectDrawServer.getInstance().removeObject();
            response.addResponse("delete ");
            CommunicationServer.broadcast(response, ObjectDrawServer.getInstance().getViewingClientSockets());
        }
        if (this.successor != null) {
            this.successor.processRequest(request, response);
        }
    }
}
