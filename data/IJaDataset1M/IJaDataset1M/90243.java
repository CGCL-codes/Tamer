package picasatagstopictures.download;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 *
 * @author tom
 */
public class SimpleAuthenticator extends Authenticator {

    private String username;

    private String password;

    public SimpleAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
    }
}
