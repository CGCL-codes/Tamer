package jtalk.controller;

import jtalk.model.Message;

public class DefaultController extends AbstractController {

    /**
	 * Change the server in the model.
	 * The model will try to connect to the new server
	 * automatically.
	 * 
	 * @param newServer
	 */
    public void changeServer(String newServer) {
        setModelProperty(Properties.SERVER.value(), newServer);
    }

    /**
     * 
     * Call the login method in the model
     * whitch try authenticate to the server
     * with the given login and password.
     * 
     * @param login
     * @param passwd
     */
    public void login(String login, String passwd) {
        callModelMethod(Methods.LOGIN.value(), new Object[] { login, passwd });
    }

    /**
     * Disconnect the current connection
     *
     */
    public void disconnect() {
        callModelMethod(Methods.DISCONNECT.value(), new Object[] {});
    }

    /**
     * Load the contact list sending evt properties
     *
     */
    public void loadContactList() {
        callModelMethod(Methods.LOAD_CONTACT_LIST.value(), new Object[] {});
    }

    /**
     * Send a message
     *
     */
    public void sendMessage(Message message) {
        callModelMethod(Methods.MESSAGE_OUT.value(), new Object[] { message });
    }
}
