package com.objectdraw.server.commands;

import com.objectdraw.server.CommunicationServer;
import com.objectdraw.server.ObjectDrawServer;
import com.objectdraw.server.data.RequestData;
import com.objectdraw.server.data.ResponseData;

/**
 * Processes a connect command from the client.
 * 
 * @author jgulik, cfruehan, Harrison
 *
 */
public class ConnectCommand extends Command {

    private static final String myCommand = "Connect";

    /**
	 * Takes a message with a connect command and adds the sender to the list of clients.
	 * Also broadcasts a message of connection to listening clients.
	 */
    @Override
    public void processRequest(RequestData request, ResponseData response) {
        if (myCommand.equalsIgnoreCase(request.getCommand())) {
            System.out.println("Connect Command");
            String username = request.getParams();
            if (username != null) {
                username.trim();
            }
            ObjectDrawServer obj = ObjectDrawServer.getInstance();
            int c = obj.getTotalClientCount();
            System.out.println("Server: " + c + " connected clients");
            if (c < 1) {
                obj.removeAllObjects();
            }
            obj.addClient(request.getClient(), username);
            response.addResponse(username + " Connected");
            System.out.println("Server: " + username + " Connected");
            CommunicationServer.broadcast(response, request.getClient());
        }
        if (this.successor != null) {
            this.successor.processRequest(request, response);
        }
    }
}
