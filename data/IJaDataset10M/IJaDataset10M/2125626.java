package de.hattrickorganizer.model;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import plugins.ISpieler;
import de.hattrickorganizer.database.DBZugriff;
import de.hattrickorganizer.gui.HOMainFrame;
import de.hattrickorganizer.gui.RefreshManager;
import de.hattrickorganizer.gui.login.LoginWaitDialog;
import de.hattrickorganizer.gui.model.CBItem;
import de.hattrickorganizer.gui.templates.ImagePanel;
import de.hattrickorganizer.tools.HOLogger;
import de.hattrickorganizer.tools.Helper;

/**
 * DOCUMENT ME!
 *
 * @author tom
 */
public class HOVerwaltung {

    /** singelton */
    protected static HOVerwaltung m_clInstance;

    /** das Model */
    protected HOModel m_clHoModel;

    /** Resource */
    protected Properties m_clResource;

    /** Parameter */
    protected String[] m_sArgs;

    /**
     * Creates a new HOVerwaltung object.
     */
    private HOVerwaltung() {
    }

    /**
     * Set string arguments.
     */
    public void setArgs(String[] args) {
        m_sArgs = args;
    }

    public String[] getArgs() {
        return m_sArgs;
    }

    /**
     * Returns the average TSI
     *
     * @return average TSI
     */
    public float getAvgTSI() {
        int numPlayers = getModel().getAllSpieler().size();
        if (numPlayers <= 1) return 0; else return Helper.round(getSumTSI() / (numPlayers - 1));
    }

    /**
     * Gibt den Durchschnittlichen Mannschaftswert zurück
     * Returns the average estimated market value (EPV)
     *
     * @return average EPV
     */
    public float getAvgEPV() {
        int numPlayers = getModel().getAllSpieler().size();
        if (numPlayers <= 1) return 0; else return Helper.round(getSumEPV() / (numPlayers - 1));
    }

    /**
     * Gibt das Durchschnittsalter zurück
     */
    public float getDurchschnittsAlter() {
        float summe = 0;
        final Vector<ISpieler> vSpieler = getModel().getAllSpieler();
        for (int i = 0; i < vSpieler.size(); i++) {
            if (!(vSpieler.get(i)).isTrainer()) {
                summe += (vSpieler.get(i)).getAlter();
                summe += (vSpieler.get(i)).getAgeDays() / 112.0;
            }
        }
        return Helper.round(summe / (vSpieler.size() - 1));
    }

    /**
     * Gibt das Durchschnittserfahrung zurück
     */
    public float getDurchschnittsErfahrung() {
        float summe = 0;
        final Vector<ISpieler> vSpieler = getModel().getAllSpieler();
        for (int i = 0; i < vSpieler.size(); i++) {
            if (!(vSpieler.get(i)).isTrainer()) {
                summe += (vSpieler.get(i)).getErfahrung();
            }
        }
        return Helper.round(summe / (vSpieler.size() - 1), 3);
    }

    /**
     * Gibt das Durchschnittsform zurück
     */
    public float getDurchschnittsForm() {
        float summe = 0;
        final Vector<ISpieler> vSpieler = getModel().getAllSpieler();
        for (int i = 0; i < vSpieler.size(); i++) {
            if (!(vSpieler.get(i)).isTrainer()) {
                summe += (vSpieler.get(i)).getForm();
            }
        }
        return Helper.round(summe / (vSpieler.size() - 1), 3);
    }

    /**
     * Returns the TSI sum
     */
    public float getSumTSI() {
        float summe = 0;
        final Vector<ISpieler> vSpieler = getModel().getAllSpieler();
        for (int i = 0; i < vSpieler.size(); i++) {
            if (!(vSpieler.get(i)).isTrainer()) {
                summe += (vSpieler.get(i)).getTSI();
            }
        }
        return summe;
    }

    /**
     * Gibt den gesamtmarktwert zurück
     * Returns the sum of all estimated player values (EPV)
     */
    public float getSumEPV() {
        float summe = 0;
        final Vector<ISpieler> vSpieler = getModel().getAllSpieler();
        for (int i = 0; i < vSpieler.size(); i++) {
            if (!(vSpieler.get(i)).isTrainer()) {
                summe += (vSpieler.get(i)).getEPV();
            }
        }
        return summe;
    }

    /**
     * Set the HOModel.
     */
    public void setModel(HOModel model) {
        m_clHoModel = model;
    }

    public HOModel getModel() {
        return m_clHoModel;
    }

    /**
     * Get the HOVerwaltung singleton instance.
     */
    public static HOVerwaltung instance() {
        if (m_clInstance == null) {
            m_clInstance = new HOVerwaltung();
            DBZugriff.instance().getFaktorenFromDB();
        }
        return m_clInstance;
    }

    public void setResource(String pfad, ClassLoader loader) {
        m_clResource = new java.util.Properties();
        try {
            m_clResource.load(loader.getResourceAsStream("sprache/" + pfad + ".properties"));
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
    }

    public Properties getResource() {
        return m_clResource;
    }

    /**
     * ersetzt das aktuelle model durch das aus der DB mit der angegebenen ID
     */
    public void loadHoModel(int id) {
        m_clHoModel = loadModel(id);
    }

    /**
     * läadt das zuletzt importtiert model ein
     */
    public void loadLatestHoModel() {
        int id = DBZugriff.instance().getLatestHrfId();
        m_clHoModel = loadModel(id);
    }

    /**
     * Recalculate subskills since a certain HRF date.
     * If the HRF date is null, the whole training history is recalculated.
     */
    public void recalcSubskills(boolean showWait, Timestamp hrfDate) {
        HOLogger.instance().log(getClass(), "Start full subskill calculation. " + new Date());
        long start = System.currentTimeMillis();
        if (hrfDate == null) {
            hrfDate = new Timestamp(0);
        }
        LoginWaitDialog waitDialog = null;
        if (showWait) {
            waitDialog = new LoginWaitDialog(HOMainFrame.instance(), false);
            waitDialog.setVisible(true);
        }
        final Vector<CBItem> hrfListe = new Vector<CBItem>();
        hrfListe.addAll(DBZugriff.instance().getCBItemHRFListe(hrfDate));
        Collections.reverse(hrfListe);
        long s1, s2, lSum = 0, mSum = 0;
        HOLogger.instance().log(getClass(), "Subskill calculation prepared. " + new Date());
        for (int i = 0; i < hrfListe.size(); i++) {
            try {
                if (showWait && waitDialog != null) {
                    waitDialog.setValue((int) ((i * 100d) / hrfListe.size()));
                }
                s1 = System.currentTimeMillis();
                final HOModel model = this.loadModel((hrfListe.get(i)).getId());
                lSum += (System.currentTimeMillis() - s1);
                s2 = System.currentTimeMillis();
                model.calcSubskills();
                mSum += (System.currentTimeMillis() - s2);
            } catch (Exception e) {
                HOLogger.instance().log(getClass(), "recalcSubskills : ");
                HOLogger.instance().log(getClass(), e);
            }
        }
        if (showWait && waitDialog != null) {
            waitDialog.setVisible(false);
        }
        loadLatestHoModel();
        RefreshManager.instance().doReInit();
        HOLogger.instance().log(getClass(), "Subskill calculation done. " + new Date() + " - took " + (System.currentTimeMillis() - start) + "ms (" + (System.currentTimeMillis() - start) / 1000L + " sec), lSum=" + lSum + ", mSum=" + mSum);
    }

    /**
     * interne Func die ein Model aus der DB lädt
     */
    protected HOModel loadModel(int id) {
        final HOModel model = new HOModel();
        model.setSpieler(DBZugriff.instance().getSpieler(id));
        model.setAllOldSpieler(DBZugriff.instance().getAllSpieler());
        model.setAufstellung(DBZugriff.instance().getAufstellung(id, Lineup.DEFAULT_NAME));
        model.setLastAufstellung(DBZugriff.instance().getAufstellung(id, Lineup.DEFAULT_NAMELAST));
        model.setBasics(DBZugriff.instance().getBasics(id));
        model.setFinanzen(DBZugriff.instance().getFinanzen(id));
        model.setLiga(DBZugriff.instance().getLiga(id));
        model.setStadium(DBZugriff.instance().getStadion(id));
        model.setTeam(DBZugriff.instance().getTeam(id));
        model.setVerein(DBZugriff.instance().getVerein(id));
        model.setID(id);
        model.setSpielplan(DBZugriff.instance().getSpielplan(-1, -1));
        model.setXtraDaten(DBZugriff.instance().getXtraDaten(id));
        return model;
    }

    /**
     * Returns the String connected to the active language file or connected
     * to the english language file. Returns !key! if the key can not be found.
     *
     * @param key Key to be searched in language files
     *
     * @return String connected to the key or !key! if nothing can be found in language files
     */
    public String getLanguageString(String key) {
        String temp = getResource().getProperty(key);
        if (temp != null) return temp;
        if (!gui.UserParameter.instance().sprachDatei.equalsIgnoreCase("english")) {
            Properties tempResource = new Properties();
            final ClassLoader loader = new ImagePanel().getClass().getClassLoader();
            try {
                tempResource.load(loader.getResourceAsStream("sprache/English.properties"));
            } catch (Exception e) {
                HOLogger.instance().log(getClass(), e);
            }
            temp = tempResource.getProperty(key);
            if (temp != null) return temp;
        }
        HOLogger.instance().warning(getClass(), "HOVerwaltung.getLanguageString: Key: " + key + " not found!");
        return "!" + key + "!";
    }
}
