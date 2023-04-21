package net.sf.dict4j.command;

import net.sf.dict4j.enumeration.ResponseEnum;
import net.sf.dict4j.exception.Dict4JProtocolException;
import net.sf.dict4j.exception.Dict4JServerException;
import net.sf.dict4j.util.DictPatterns;
import net.sf.dict4j.util.Digest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * 3.11.  The AUTH Command
 * <p/>
 * AUTH username authentication-string
 * The client can authenticate itself to the server using a username and password.
 * The authentication-string will be computed as in the APOP protocol discussed in [RFC1939].
 * Briefly, the authentication-string is the MD5 checksum of the concatenation of the msg-id
 * (obtained from the initial banner) and the "shared secret" that is stored in the server and
 * client configuration files.  Since the user does not have to type this shared secret when
 * accessing the server, the shared secret can be an arbitrarily long passphrase.
 * Because of the computational ease of computing the MD5 checksum, the shared secret should be
 * significantly longer than a usual password.
 */
public class AuthCommand extends AbstractCommand {

    private String username;

    private String authenticationString;

    public AuthCommand(String username, String password, String messageId) {
        validateUsername(username);
        validatePassword(password);
        this.username = username;
        try {
            this.authenticationString = new Digest("MD5", password + messageId).toString();
        } catch (NoSuchAlgorithmException e) {
            throw new Dict4JProtocolException("MD5 digest algorithm is not available", e);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is empty");
        }
    }

    private void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is empty");
        }
        Pattern pattern = Pattern.compile(DictPatterns.WORD);
        if (!pattern.matcher(username).matches()) {
            throw new IllegalArgumentException("Invalid username");
        }
    }

    protected ResponseEnum[] getResponseHeaderCodes() {
        return new ResponseEnum[] { ResponseEnum.AUTHENTICATION_SUCCESSFUL, ResponseEnum.ACCESS_DENIED };
    }

    @Override
    protected void checkResponseCodeForServerException(ResponseEnum responseEnum) throws Dict4JServerException {
        if (!responseEnum.equals(ResponseEnum.ACCESS_DENIED)) {
            super.checkResponseCodeForServerException(responseEnum);
        }
    }

    protected String getString() {
        return String.format("AUTH %s %s", username, authenticationString);
    }

    public boolean isSuccess() {
        return responseCode == ResponseEnum.AUTHENTICATION_SUCCESSFUL.code;
    }
}
