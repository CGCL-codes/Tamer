package org.netbeans.modules.keyring.mac;

import com.sun.jna.Pointer;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.spi.keyring.KeyringProvider;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = KeyringProvider.class, position = 200)
public class MacProvider implements KeyringProvider {

    private static final Logger LOG = Logger.getLogger(MacProvider.class.getName());

    public boolean enabled() {
        if (Boolean.getBoolean("netbeans.keyring.no.native")) {
            LOG.fine("native keyring integration disabled");
            return false;
        }
        return Utilities.isMac();
    }

    public char[] read(String key) {
        try {
            byte[] serviceName = key.getBytes("UTF-8");
            byte[] accountName = "NetBeans".getBytes("UTF-8");
            int[] dataLength = new int[1];
            Pointer[] data = new Pointer[1];
            error("find", SecurityLibrary.LIBRARY.SecKeychainFindGenericPassword(null, serviceName.length, serviceName, accountName.length, accountName, dataLength, data, null));
            if (data[0] == null) {
                return null;
            }
            byte[] value = data[0].getByteArray(0, dataLength[0]);
            return new String(value, "UTF-8").toCharArray();
        } catch (UnsupportedEncodingException x) {
            LOG.log(Level.WARNING, null, x);
            return null;
        }
    }

    public void save(String key, char[] password, String description) {
        delete(key);
        try {
            byte[] serviceName = key.getBytes("UTF-8");
            byte[] accountName = "NetBeans".getBytes("UTF-8");
            byte[] data = new String(password).getBytes("UTF-8");
            error("save", SecurityLibrary.LIBRARY.SecKeychainAddGenericPassword(null, serviceName.length, serviceName, accountName.length, accountName, data.length, data, null));
        } catch (UnsupportedEncodingException x) {
            LOG.log(Level.WARNING, null, x);
        }
    }

    public void delete(String key) {
        try {
            byte[] serviceName = key.getBytes("UTF-8");
            byte[] accountName = "NetBeans".getBytes("UTF-8");
            Pointer[] itemRef = new Pointer[1];
            error("find (for delete)", SecurityLibrary.LIBRARY.SecKeychainFindGenericPassword(null, serviceName.length, serviceName, accountName.length, accountName, null, null, itemRef));
            if (itemRef[0] != null) {
                error("delete", SecurityLibrary.LIBRARY.SecKeychainItemDelete(itemRef[0]));
            }
        } catch (UnsupportedEncodingException x) {
            LOG.log(Level.WARNING, null, x);
        }
    }

    private static void error(String msg, int code) {
        if (code != 0 && code != -25300) {
            LOG.warning(msg + ": " + code);
        }
    }
}
