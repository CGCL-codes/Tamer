package allensoft.javacvs.client.event;

import allensoft.javacvs.client.*;

public class CVSClientAdapter implements CVSClientListener {

    /** Fired when some text is sent to the server. */
    public void sentText(CVSClientTextEvent event) {
    }

    /** Fired when some text is received from the server. */
    public void receivedText(CVSClientTextEvent event) {
    }

    /** Fired when the client's status changes. The event's text is the status
	    of the client. For example, "Connection to .....", "Updating ...." etc. */
    public void statusUpdate(CVSClientTextEvent event) {
    }

    public void receivedResponse(CVSClientResponseEvent event) {
    }

    /** Fired when the client is about to start performing some requests or just a single request. */
    public void startingRequests(CVSClientEvent event) {
    }

    /** Fired when the client has actually opened a connection and is about to perform requests.
	 When this is fired the client has successfully logged in. */
    public void openedConnection(CVSClientEvent event) {
    }

    /** Fired when the client has finished performing some requests or just a single request. */
    public void finishedRequests(CVSClientEvent event) {
    }

    /** Fired when the client starts batch mode processing. */
    public void enteredBatchMode(CVSClientEvent event) {
    }

    /** Fired when the client exits batch mode processing. */
    public void exitedBatchMode(CVSClientEvent event) {
    }
}
