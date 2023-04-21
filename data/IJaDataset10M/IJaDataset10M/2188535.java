package ray.mgocc.net;

import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GameAccount {

    private static final Log log = LogFactory.getLog(GameAccount.class);

    private final Preferences pref = Preferences.userNodeForPackage(this.getClass());

    private static final String ID = "id";

    private static final String PASSWORD = "password";

    public GameAccount(String id, String password) {
        setId(id);
        setPassword(password);
    }

    public GameAccount() {
        load();
    }

    public String getId() {
        return getValue(ID);
    }

    public String getPassword() {
        return getValue(PASSWORD);
    }

    public void setId(String id) {
        setValue(ID, id);
    }

    public void setPassword(String password) {
        setValue(PASSWORD, password);
    }

    private String getValue(String key) {
        load();
        String value = null;
        value = pref.get(key, value);
        String system = System.getProperty(getClass().getName() + "." + key);
        if (null != system && !system.equals(value)) {
            value = system;
            setValue(key, value);
        }
        if (null == value) {
            throw new RuntimeException(key + " is empty.");
        }
        return value;
    }

    private void setValue(String id, String value) {
        pref.put(id, value);
        save();
    }

    public void save() {
        try {
            pref.sync();
        } catch (BackingStoreException e) {
            log.warn("game account can not save.", e);
        }
    }

    private void load() {
        try {
            pref.sync();
        } catch (BackingStoreException e) {
            log.warn("game account can not load.", e);
        }
    }
}
