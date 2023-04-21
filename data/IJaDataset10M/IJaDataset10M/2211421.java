package ca.whu.taxman.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author Peter Wu <peterwu@hotmail.com>
 */
@Stateless
public class PasswordProtectionBean implements PasswordProtectionLocal {

    public String computeHash(final String plainTextPassword, final byte[] salt) {
        String hashedPassword = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(salt);
            hashedPassword = new String(digest.digest(plainTextPassword.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordProtectionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PasswordProtectionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashedPassword;
    }

    public boolean verifyHash(final String plainTextPassword, byte[] salt, final String hashedPassword) {
        return hashedPassword.equals(computeHash(plainTextPassword, salt));
    }
}
