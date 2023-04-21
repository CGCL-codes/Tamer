package corina.prefs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import corina.core.AbstractSubsystem;
import corina.core.App;
import corina.gui.Bug;
import corina.logging.CorinaLog;
import corina.ui.I18n;
import corina.util.JDisclosureTriangle;

/**
 * Storage and access of user preferences.
 * 
 * <h2>Left to do</h2>
 * <ul>
 * <li>add getFont(), setColor(), etc. convenience methods
 * <li>get rid of JDisclosureTriangle usage (and then delete it)
 * <li>get rid of "system" preferences (and file Source/prefs.properties) - these should be inline, in each subsystem, with the prefs call
 * <li>extract prefs file location to platform?
 * <li>(goal: 300 lines for everything, maybe 350)
 * </ul>
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at </i> cornell <i style="color: gray">dot </i> edu&gt;
 * @version $Id: Prefs.java,v 1.16 2006/10/03 23:41:53 lucasmo Exp $
 */
public class Prefs extends AbstractSubsystem {

    public static final String EDIT_FOREGROUND = "corina.edit.foreground";

    public static final String EDIT_BACKGROUND = "corina.edit.background";

    public static final String EDIT_FONT = "corina.edit.font";

    public static final String EDIT_GRIDLINES = "corina.edit.gridlines";

    public static final String GRID_HIGHLIGHT = "corina.grid.highlight";

    public static final String GRID_HIGHLIGHTCOLOR = "corina.grid.hightlightcolor";

    private static final CorinaLog log = new CorinaLog("Prefs");

    /**
   * if true, silently ignore if the prefs can't be saved.
   * protected to avoid synthetic accessor
   */
    protected static boolean dontWarn = false;

    private String CORINADIR;

    private String FILENAME;

    private String MACHINEFILENAME;

    /**
   * Our internal Properties object in which to save preferences
   */
    private Properties prefs;

    private Set listeners = new HashSet();

    /**
   * A copy of the default UIDefaults object available at startup. We cache these defaults to allow the user to "reset" any changes they may have made through the Appearance Panel.
   */
    private final Hashtable UIDEFAULTS = new Hashtable();

    public String getName() {
        return "Preferences";
    }

    public String getCorinaDir() {
        return CORINADIR;
    }

    /**
   * Initializes the preferences system. This should be called upon startup.
   */
    public void init() {
        super.init();
        String home = System.getProperty("user.home");
        if (!home.endsWith(File.separator)) home = home + File.separator;
        if (App.platform.isWindows()) {
            String basedir = home;
            File oldprefs = new File(home + "Corina Preferences");
            if (new File(home + "Application Data").isDirectory()) basedir = home + "Application Data" + File.separator;
            basedir += "Corina" + File.separator;
            File corinadir = new File(basedir);
            if (!corinadir.exists()) corinadir.mkdir();
            CORINADIR = basedir;
            FILENAME = basedir + "Corina.pref";
            MACHINEFILENAME = "C:\\Corina System Preferences";
            if (oldprefs.exists()) {
                oldprefs.renameTo(new File(FILENAME));
            }
        } else if (App.platform.isMac()) {
            CORINADIR = home + "Library/Corina/";
            FILENAME = home + "Library/Corina/Preferences";
            MACHINEFILENAME = "/Library/Preferences/Corina System Preferences";
            File basedir = new File(CORINADIR);
            if (!basedir.exists()) basedir.mkdirs();
        } else {
            CORINADIR = home + ".corina";
            FILENAME = home + ".corina/.preferences";
            MACHINEFILENAME = "/etc/corina_system_preferences";
            File basedir = new File(CORINADIR);
            if (!basedir.exists()) basedir.mkdirs();
        }
        initUIDefaults();
        try {
            load();
        } catch (IOException ioe) {
            new Bug(ioe);
        }
        setInitialized(true);
    }

    public void destroy() {
        if (!initialized) log.debug("being destroyed more than once!");
        save();
    }

    public void finalize() throws Throwable {
        save();
        super.finalize();
    }

    public Hashtable getUIDefaults() {
        return UIDEFAULTS;
    }

    private void initUIDefaults() {
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration e = defaults.keys();
        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            UIDEFAULTS.put(key, defaults.get(key));
        }
    }

    /**
   * Load system and user properties (preferences). This runs 3 steps:
   * <ol>
   * <li>Load system properties
   * <li>Load default properties from class loader resource prefs.properties
   * <li>Override with user's properties file (differs by OS; FIXME?)
   * </ol>
   * 
   * TODO: System properties should really override the user properties (as they are more "immediate"), but we'd have to make sure to only save the corina properties back out. I /think/ all corina
   * properties start with 'corina' but I'm not sure yet.
   */
    private synchronized void load() throws IOException {
        Properties systemprops = System.getProperties();
        log.debug("Loading preferences");
        StringBuffer errors = new StringBuffer();
        Properties defaults = new Properties(systemprops);
        ClassLoader cl = Prefs.class.getClassLoader();
        try {
            java.io.InputStream is = cl.getResourceAsStream("prefs.properties");
            if (is != null) {
                try {
                    defaults.load(is);
                } finally {
                    is.close();
                }
            }
        } catch (IOException ioe) {
            errors.append("Error loading Corina's default preferences (bug!).\n");
        }
        try {
            defaults.load(new FileInputStream(MACHINEFILENAME));
        } catch (IOException ioe) {
        }
        prefs = new Properties(defaults);
        try {
            prefs.load(new FileInputStream(FILENAME));
        } catch (FileNotFoundException fnfe) {
            try {
                prefs.store(new FileOutputStream(FILENAME), "Corina user preferences");
            } catch (IOException ioe) {
                errors.append("Error copying preferences file to your home directory: " + ioe.getMessage() + "\n");
            }
        } catch (IOException ioe) {
            errors.append("Error loading user preferences file: " + ioe.getMessage() + "\n");
        }
        installUIDefaultsPrefs();
        if (errors.length() != 0) throw new IOException(errors.toString());
    }

    /**
   * Loads any saved uidefaults preferences and installs them
   */
    private synchronized void installUIDefaultsPrefs() {
        Iterator it = prefs.entrySet().iterator();
        UIDefaults uidefaults = UIManager.getDefaults();
        log.debug("iterating prefs");
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String prefskey = entry.getKey().toString();
            if (!prefskey.startsWith("uidefaults.") || prefskey.length() <= "uidefaults.".length()) continue;
            String uikey = prefskey.substring("uidefaults.".length());
            Object object = uidefaults.get(uikey);
            log.debug("prefs property " + uikey + " " + object);
            installUIDefault(object.getClass(), prefskey, uikey);
        }
    }

    private void installUIDefault(Class type, String prefskey, String uikey) {
        Object decoded = null;
        String pref = prefs.getProperty(prefskey);
        if (pref == null) {
            log.warn("Preference '" + prefskey + "' held null value.");
            return;
        }
        if (Color.class.isAssignableFrom(type)) {
            decoded = Color.decode(pref);
        } else if (Font.class.isAssignableFrom(type)) {
            decoded = Font.decode(pref);
        } else {
            log.warn("Unsupported UIDefault preference type: " + type);
            return;
        }
        if (decoded == null) {
            log.warn("UIDefaults color preference '" + prefskey + "' was not decodable.");
            return;
        }
        UIDefaults uidefaults = UIManager.getDefaults();
        log.debug("Removing UIDefaults key before overwriting: " + uikey);
        uidefaults.remove(uikey);
        if (Color.class.isAssignableFrom(type)) {
            uidefaults.put(uikey, new ColorUIResource((Color) decoded));
        } else {
            uidefaults.put(uikey, new FontUIResource((Font) decoded));
        }
    }

    /**
   * Save current properties to the user's system-writable properties (preferences) file, <code>FILENAME</code>.
   */
    public synchronized void save() {
        log.debug("Saving preferences...");
        while (true) {
            try {
                prefs.store(new FileOutputStream(FILENAME), "Corina user preferences");
                return;
            } catch (IOException ioe) {
                if (dontWarn) return;
                boolean tryAgain = cantSave(ioe);
                if (!tryAgain) return;
            }
        }
    }

    private static boolean cantSave(Exception e) {
        JPanel message = new JPanel(new BorderLayout(0, 8));
        message.add(new JLabel(I18n.getText("prefs_cant_save")), BorderLayout.NORTH);
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog(null, I18n.getText("prefs_cant_save_title"));
        optionPane.setOptions(new String[] { I18n.getText("try_again"), I18n.getText("cancel") });
        JComponent stackTrace = new JScrollPane(new JTextArea(Bug.getStackTrace(e), 10, 60));
        JDisclosureTriangle v = new JDisclosureTriangle(I18n.getText("click_for_details"), stackTrace, false);
        message.add(v, BorderLayout.CENTER);
        JCheckBox dontWarnCheckbox = new JCheckBox(I18n.getText("dont_warn_again"), false);
        dontWarnCheckbox.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dontWarn = !dontWarn;
            }
        });
        message.add(dontWarnCheckbox, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setResizable(false);
        dialog.show();
        return optionPane.getValue().equals(I18n.getText("try_again"));
    }

    public Properties getPrefs() {
        return prefs;
    }

    public void setPref(String pref, String value) {
        prefs.setProperty(pref, value);
        save();
        firePrefChanged(pref);
    }

    public String getPref(String pref) {
        return prefs.getProperty(pref);
    }

    public String getPref(String pref, String deflt) {
        String value = prefs.getProperty(pref);
        if (value == null) value = deflt;
        return value;
    }

    public Dimension getDimensionPref(String pref, Dimension deflt) {
        String value = prefs.getProperty(pref);
        if (value == null) return deflt;
        StringTokenizer st = new StringTokenizer(value, ",");
        Dimension d = new Dimension(deflt);
        if (st.hasMoreTokens()) {
            String s = st.nextToken();
            try {
                int i = Integer.parseInt(s);
                if (i > 0) d.width = i;
            } catch (NumberFormatException nfe) {
                log.warn("Invalid dimension width: " + s);
            }
        }
        if (st.hasMoreTokens()) {
            String s = st.nextToken();
            try {
                int i = Integer.parseInt(s);
                if (i > 0) d.height = i;
            } catch (NumberFormatException nfe) {
                log.warn("Invalid dimension height: " + s);
            }
        }
        return d;
    }

    public int getIntPref(String pref, int deflt) {
        String value = prefs.getProperty(pref);
        if (value == null) return deflt;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            log.warn("Invalid integer for preference '" + pref + "': " + value);
            return deflt;
        }
    }

    public Color getColorPref(String pref, Color deflt) {
        String value = prefs.getProperty(pref);
        if (value == null) return deflt;
        try {
            return Color.decode(value);
        } catch (NumberFormatException nfe) {
            log.warn("Invalid color for preference '" + pref + "': " + value);
            return deflt;
        }
    }

    public Font getFontPref(String pref, Font deflt) {
        String value = prefs.getProperty(pref);
        if (value == null) return deflt;
        return Font.decode(value);
    }

    public void removePref(String pref) {
        prefs.remove(pref);
        save();
        firePrefChanged(pref);
    }

    public synchronized void addPrefsListener(PrefsListener l) {
        if (!listeners.contains(l)) listeners.add(l);
    }

    public synchronized void removePrefsListener(PrefsListener l) {
        listeners.remove(l);
    }

    public void firePrefChanged(String pref) {
        PrefsListener[] l;
        synchronized (Prefs.class) {
            l = (PrefsListener[]) listeners.toArray(new PrefsListener[listeners.size()]);
        }
        int size = l.length;
        if (size == 0) return;
        PrefsEvent e = new PrefsEvent(Prefs.class, pref);
        for (int i = 0; i < size; i++) {
            l[i].prefChanged(e);
        }
    }
}
