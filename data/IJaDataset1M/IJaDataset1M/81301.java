package com.jjcp;

import java.io.IOException;
import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.QuickAuthenticator;

/**
 *
 * @author irimi
 */
public class DICOMAuthenticator extends QuickAuthenticator {

    @Override
    public boolean askAuthorisation(ClientHandler clientHandler) throws IOException {
        String username = askStringInput(clientHandler, java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("USER_NAME_:"));
        String password = askStringInput(clientHandler, java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("PASSWORD_:"));
        if (username == null || password == null) return false;
        if (username.equals(java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("IRIMI")) && password.equals("")) {
            sendString(clientHandler, java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("AUTH_OK"));
            return true;
        } else {
            sendString(clientHandler, java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("AUTH_FAILED"));
            return false;
        }
    }
}
