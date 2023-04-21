package visitpc;

import java.io.IOException;
import visitpc.lib.gui.Dialogs;
import visitpc.lib.io.*;

public class ClientConfig extends SimpleConfig {

    public String serverName = "";

    public int serverPort = 19235;

    public String username = "";

    public String password = "";

    /**
	   * Determine if the config is valid
	   * 
	   * If the configuration is not valid an VisitPCException is thrown.
	   */
    public void checkValid() throws VisitPCException {
        if (serverName != null && serverName.length() > 0) {
            if (serverPort > 0 && serverPort < 65536) {
                if (username != null && username.length() > 0) {
                    if (password != null && password.length() > 0) {
                        ClientConfig.CheckPassword(password, SimpleConfigHelper.MIN_PASSWORD_LENGTH, SimpleConfigHelper.MIN_PASSWORD_LENGTH);
                    } else {
                        throw new VisitPCException("No password configured.");
                    }
                } else {
                    throw new VisitPCException("No username configured.");
                }
            } else {
                throw new VisitPCException("Invalid server port configured (1-65535 are valid).");
            }
        } else {
            throw new VisitPCException("No server name configured.");
        }
    }

    public boolean isValid() {
        boolean valid = false;
        try {
            checkValid();
            valid = true;
        } catch (VisitPCException e) {
            Dialogs.showErrorDialog(null, "Warning", e.getLocalizedMessage());
        }
        return valid;
    }

    /**
	 * A Helper method for checking VisitPC user passwords
	 * 
	 * @param password
	 * @param errorMessage
	 * @return
	 * @throws IOException
	 */
    public static String CheckPassword(String password, int minLength, int requiredLength) throws VisitPCException {
        String errorMessage = "The password must be at least " + minLength + " characters long";
        if (password.length() < minLength) {
            throw new VisitPCException(errorMessage + ".");
        }
        int nonLetterCount = 0;
        int letterCount = 0;
        for (int i = 0; i < 8; i++) {
            if (Character.isLetter(password.charAt(i))) {
                letterCount++;
            } else {
                nonLetterCount++;
            }
        }
        if (letterCount == 0 || nonLetterCount == 0) {
        }
        if (requiredLength > 0 && password.length() > requiredLength) {
            password = password.substring(0, requiredLength);
        }
        return password;
    }
}
