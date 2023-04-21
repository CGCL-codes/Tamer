package mou;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */
public abstract class Modul implements Shutdownable {

    private Subsystem parent;

    private boolean shutdown = false;

    private Preferences preferences;

    private boolean started = false;

    public Modul(Subsystem parent) {
        setParent(parent);
        preferences = new Preferences(getPreferencesFile());
    }

    public boolean istStarted() {
        return started;
    }

    public Logger getLogger() {
        if (parent != null) {
            Logger ret = parent.getLogger();
            if (ret != null) return ret;
        }
        return Logger.getLogger("");
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void setParent(Subsystem parent) {
        if (parent == null) return;
        parent.registerModul(this);
        this.parent = parent;
    }

    public Subsystem getParent() {
        return parent;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void shutdown() {
        if (isShutdown()) return;
        try {
            if (istStarted()) {
                getLogger().info("Fahre Modul herunter: " + getModulName());
                shutdownIntern();
                preferences.savePreferences();
            }
        } catch (Throwable th) {
            getLogger().log(Level.SEVERE, "Fehler beim Runterfahren des Moduls " + getModulName() + " : " + th.getLocalizedMessage(), th);
        }
        shutdown = true;
        started = false;
    }

    /**
	 * @return Volle Path zum Subsystem mit allen Eltern-Subsystemen z.B "MOU.GUI.Starmap"
	 */
    public String getFullPath() {
        if (parent == null) return getModulName();
        return parent.getFullPath() + "." + getModulName();
    }

    public void logThrowable(String meldung, Throwable th) {
        getLogger().log(Level.SEVERE, meldung, th);
    }

    public void startModul() throws Exception {
        getLogger().info("starte Modul: " + getModulName());
        startModulIntern();
        started = true;
    }

    protected void logException(Throwable th) {
        getLogger().warning(th.getLocalizedMessage());
        getLogger().throwing(getModulName(), "", th);
    }

    /**
	 * @return Names des Moduls z.B "GUI"
	 */
    public abstract String getModulName();

    protected abstract void shutdownIntern();

    protected abstract File getPreferencesFile();

    /**
	 * Startet Modul. Hier sollen alle Initialisierungsarbeiten durchgef�hrt werden. Nur die
	 * unbedingt notwendigen Sache d�rfen im Konstruktor erledigt werden
	 */
    protected abstract void startModulIntern() throws Exception;
}
