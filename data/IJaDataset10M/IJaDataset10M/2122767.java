package cz.cvut.fel.mvod.global;

import cz.cvut.fel.mvod.common.ObjectReadWriter;
import cz.cvut.fel.mvod.common.networkAddressRange;
import cz.cvut.fel.mvod.prologueServer.PrologueServer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A global settings repository. All relevant settings that affect the applcation are stored here.
 * Whenever the settings are altered the listeners of this class are notified.
 * @author Radovan Murin
 */
public final class GlobalSettingsAndNotifier implements Serializable {

    public static GlobalSettingsAndNotifier singleton = new GlobalSettingsAndNotifier();

    Locale locale;

    public ResourceBundle messages;

    private ArrayList<Notifiable> listeners;

    public HashMap<String, String> settings;

    public ArrayList<networkAddressRange> permited;

    /**
     * Constructs the singleton and Inserts the default settings
     */
    private GlobalSettingsAndNotifier() {
        listeners = new ArrayList<Notifiable>();
        try {
            tryLoad();
            modifySettings("prologueState", PrologueServer.STATE_INACTIVE + "");
        } catch (Exception ex) {
            ex.printStackTrace();
            locale = new Locale("cs", "CZ");
            settings = new HashMap<String, String>();
            modifySettings("PROLOGUE_PORT", "10443");
            modifySettings("allowBeacon", "true");
            modifySettings("HTTP_PORT", "10666");
            modifySettings("SSL_PORT", "11109");
            modifySettings("IMPLICIT_ALLOW", "false");
            modifySettings("SERVER_NAME", "Default Value");
            modifySettings("prologueState", PrologueServer.STATE_INACTIVE + "");
            modifySettings("Prologue_certpath", "null");
            modifySettings("Prologue_USEDEFAULTCERT", "true");
            modifySettings("NET_ORIGIN", "NO_RESTRICTIONS");
            modifySettings("Voting_useEmbedded", "true");
            permited = new ArrayList<networkAddressRange>();
            int[] add = new int[] { 0, 0, 0, 0 };
            int[] mask = new int[] { 0, 0, 0, 0 };
            try {
                permited.add(new networkAddressRange(add, mask, networkAddressRange.ALLOW_ANY));
            } catch (Exception e) {
            }
        }
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
    }

    /**
 * Changes the locale to the one specified
 * @param loc the locale the system should change to, if the locale is not supported the default is used.(English)
 */
    public void ChangeLocale(Locale loc) {
        try {
            System.out.println("Changing locale to " + loc.getLanguage());
            messages = ResourceBundle.getBundle("MessagesBundle", loc);
            locale = loc;
            ObjectReadWriter.saveSettings();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
 * Returns the locale at the present time
 * @return the locale
 */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Adds a settings change listener that will be alerted everytime a change in settings has occured
     * @param n an Notifiable instance
     */
    public void addListener(Notifiable n) {
        listeners.add(n);
    }

    /**
     * Modifies settings
     * @param name the setting name, see documentation for possible names
     * @param value the setting value
     */
    public void modifySettings(String name, String value) {
        settings.put(name.toUpperCase(), value);
        notifyListeners();
    }

    /**
     * Notifies the listeners of change
     */
    public void notifyListeners() {
        Iterator<Notifiable> iN = listeners.iterator();
        while (iN.hasNext()) {
            iN.next().notifyOfChange();
        }
    }

    /**
     * Returns the setting
     * @param name the setting nam, see documentation for possible names
     * @return the setting value
     */
    public String getSetting(String name) {
        String n = name.toUpperCase();
        String out = null;
        out = settings.get(n);
        if (out == null) {
            out = " Null ";
        }
        return out;
    }

    /**
     * Modifies settings - this one can prevent looping
     * @param name the setting name, see documentation for possible names
     * @param value the setting value
     * @param flagNotify if true the change will provoke a notification to all listeners
     */
    public void modifySettings(String name, String value, boolean flagNotify) {
        System.out.println("SETTING " + name + " modified to " + value);
        if (!flagNotify) {
            settings.put(name.toUpperCase(), value);
        } else {
            modifySettings(name.toUpperCase(), value);
        }
    }

    /**
 * Attempts to load the settings.conf file that contain the settings storedfrom a previous session
 * @throws IOException if the file is corrupted
 * @throws FileNotFoundException if the file is not found
 * @throws ClassNotFoundException if the file has bad data
 */
    public void tryLoad() throws IOException, FileNotFoundException, ClassNotFoundException {
        savedSettings s = ObjectReadWriter.loadSettings();
        locale = s.getLocale();
        System.out.println(s.getLocale().getLanguage());
        settings = s.getSettings();
        permited = s.getPermited();
    }

    /**
 * Returns a serialisable representation of the settings
 * @return the instance that can be saved to a file
 */
    public savedSettings getSavable() {
        return new savedSettings(locale, settings, permited);
    }
}
