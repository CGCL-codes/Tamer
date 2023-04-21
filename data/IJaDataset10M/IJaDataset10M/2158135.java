package net.godcode.olivenotes.services.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.tapestry.services.ApplicationStateManager;
import org.slf4j.Logger;
import net.godcode.olivenotes.entities.User;
import net.godcode.olivenotes.entities.UserDAO;

/**
 * UserAuthenticatorImpl is an UserAuthenticator implementation for simple
 * authentication as a Tapestry5 service.
 * 
 * @author Chris Lewis Jan 5, 2008 <chris@thegodcode.net>
 * @version $Id: UserAuthenticatorImpl.java 13 2008-01-07 21:03:06Z burningodzilla $
 */
public class UserAuthenticatorImpl implements UserAuthenticator {

    private UserDAO userDAO;

    private Logger log;

    private ApplicationStateManager asm;

    /**
	 * 
	 * @param userDAO
	 * @param asm
	 * @param log
	 */
    public UserAuthenticatorImpl(UserDAO userDAO, ApplicationStateManager asm, Logger log) {
        this.userDAO = userDAO;
        this.asm = asm;
        this.log = log;
    }

    public User authenticate(String userName, String password) {
        if (userName == null || userName == "") {
            throw new IllegalArgumentException("Argument cannot be null or empty!");
        } else if (userName.length() == 0) {
            throw new IllegalArgumentException("Argument cannot be null or empty!");
        }
        User user = userDAO.findByUserName(userName);
        if (user == null) {
            throw new NoSuchUserException("No user with the username '" + userName + "' exists.");
        }
        String hashedPassword = null;
        try {
            hashedPassword = UserAuthenticatorImpl.hash(password, "MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        if (user.getPassword().equals(hashedPassword)) {
            asm.set(User.class, user);
            return user;
        } else {
            throw new PasswordMismatchException("Incorrect password!");
        }
    }

    public User logout() {
        asm.set(User.class, null);
        return null;
    }

    public boolean isLoggedIn() {
        return asm.exists(User.class);
    }

    /**
	 * TODO This should be in a util class.
	 * @param input
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
    private static String hash(String input, String algorithm) throws NoSuchAlgorithmException {
        String hashedInput = null;
        byte[] bytes = input.getBytes();
        MessageDigest md;
        md = MessageDigest.getInstance(algorithm);
        md.update(bytes);
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(Integer.toHexString(b & 0xff));
        }
        hashedInput = sb.toString();
        sb.setLength(0);
        return hashedInput;
    }
}
