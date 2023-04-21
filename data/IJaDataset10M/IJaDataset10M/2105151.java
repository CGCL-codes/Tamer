package org.netbeans.modules.keyring.win32;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.netbeans.api.keyring.Keyring;
import org.netbeans.modules.keyring.spi.EncryptionProvider;
import org.netbeans.spi.keyring.KeyringProvider;

/**
 * Password provider for windows, using windows user-based encryption.
 * 
 * This makes use of the Windows CryptProtectData and CryptUnprotectData library
 * routines to encrypt a password securely for the logged in user.
 * 
 * In the original NetBeans implementation, those functions were used
 * exclusively, and the resulting encrypted values were stored in the user
 * preferences. This makes me uneasy, because any application running as that
 * user can look in the preferences, grab the binary data, decrypt it, and have
 * a password. (Other operating systems such as Mac OS X will ask the user if
 * keyring access should be granted to an unknown app; but Windows apparently
 * does not.)  If the preference keys use some meaningful value like
 * username@website, this makes it easy to harvest passwords.
 * 
 * So this class adds an extra layer of security. First, the preference keys do
 * not record the name of the protected resource; they only record the MD5 hash
 * of that resource name. Thus, one cannot look at the preference keys and guess
 * what resource is being protected. Next, the name of the protected resource
 * is used to scramble the encrypted data before it is stored in preferences.
 * The result is that an attacker needs to know the textual description of the
 * protected resource before they can successfully decrypt the password.
 * 
 * @author Tuma
 * 
 */
public class Win32Provider implements KeyringProvider {

    private static final Logger LOG = Logger.getLogger(Win32Provider.class.getName());

    private EncryptionProvider encryption = new Win32Protect();

    @Override
    public boolean enabled() {
        return encryption.enabled();
    }

    @Override
    public void delete(String key) {
        Preferences prefs = prefs();
        String prefsKey = getPrefsKey(key);
        prefs.remove(prefsKey);
    }

    @Override
    public char[] read(String key) {
        String prefsKey = getPrefsKey(key);
        byte[] ciphertext = prefs().getByteArray(prefsKey, null);
        if (ciphertext == null) {
            return null;
        }
        try {
            return encryption.decrypt(convolve(ciphertext, key));
        } catch (Exception x) {
            LOG.log(Level.FINE, "failed to decrypt password for " + key, x);
        }
        return null;
    }

    @Override
    public void save(String key, char[] password, String description) {
        _save(key, password, description);
    }

    private boolean _save(String key, char[] password, String description) {
        Preferences prefs = prefs();
        String prefsKey = getPrefsKey(key);
        try {
            byte[] ciphertext = convolve(encryption.encrypt(password), key);
            prefs.putByteArray(prefsKey, ciphertext);
        } catch (Exception x) {
            LOG.log(Level.FINE, "failed to encrypt password for " + key, x);
            return false;
        }
        return true;
    }

    private Preferences prefs() {
        return Preferences.userNodeForPackage(Keyring.class).node(encryption.id());
    }

    private MessageDigest MD5 = null;

    private String getPrefsKey(String key) {
        try {
            if (MD5 == null) MD5 = MessageDigest.getInstance("MD5");
            MD5.reset();
            MD5.update(key.getBytes("UTF-8"));
            byte[] resultBytes = MD5.digest();
            return toHexString(resultBytes);
        } catch (Exception nsae) {
            return key;
        }
    }

    private static String toHexString(byte[] data) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String digits = Integer.toHexString(0xFF & data[i]);
            if (digits.length() < 2) result.append("0");
            result.append(digits);
        }
        return result.toString();
    }

    private static byte[] convolve(byte[] data, String key) {
        try {
            byte[] working = data.clone();
            byte[] xorData = key.getBytes("UTF-8");
            int w = 0;
            int x = xorData.length - 1;
            int iter = Math.max(working.length, xorData.length);
            while (iter-- > 0) {
                working[w] ^= xorData[x];
                w++;
                if (w >= working.length) w = 0;
                x--;
                if (x < 0) x = xorData.length - 1;
            }
            return working;
        } catch (Exception e) {
            return data;
        }
    }
}
