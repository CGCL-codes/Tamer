package nhb.webflag.web.account;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nhb.webflag.db.DBHandler;
import org.apache.log4j.Logger;

/**
 * authenticates a user
 *
 * @author hendrik
 */
public class Authenticator {

    private static Logger logger = Logger.getLogger(Authenticator.class);

    private static final String SQL = "SELECT id FROM rpaccount WHERE username='[username]' AND password='[password]' AND active='y'";

    /**
	 * authenticates the user
	 *
	 * @param dbhandler DBHandler
	 * @param username username
	 * @param password password
	 * @return account_id or <code>null</code> in case of an error or failed authentication
	 */
    public String authenticate(DBHandler dbhandler, String username, String password) {
        String pass = hashPassword(username, password);
        if (pass == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", pass);
        Set<String> data = dbhandler.getDataAsSet(SQL, params);
        if (data.isEmpty()) {
            return null;
        }
        return data.iterator().next();
    }

    /**
	 * hashes the password
	 *
	 * @param username username
	 * @param password password
	 * @return hashed password, or <code>null</code> in case of an error
	 */
    private String hashPassword(String username, String password) {
        String pass;
        try {
            pass = MD5.md5(password + ":" + username);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e, e);
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error(e, e);
            return null;
        }
        return pass;
    }
}
