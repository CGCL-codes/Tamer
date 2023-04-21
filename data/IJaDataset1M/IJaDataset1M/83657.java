package grenz;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author hbudde
 */
public class SerienformularBean {

    protected int id_Serie;

    public static final String PROP_ID_SERIE = "id_Serie";

    /**
     * Get the value of id_Serie
     *
     * @return the value of id_Serie
     */
    public int getId_Serie() {
        return id_Serie;
    }

    /**
     * Set the value of id_Serie
     *
     * @param id_Serie new value of id_Serie
     */
    public void setId_Serie(int id_Serie) {
        int oldId_Serie = this.id_Serie;
        this.id_Serie = id_Serie;
        propertyChangeSupport.firePropertyChange(PROP_ID_SERIE, oldId_Serie, id_Serie);
    }

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected String serientitel;

    public static final String PROP_SERIENTITEL = "serientitel";

    /**
     * Get the value of serientitel
     *
     * @return the value of serientitel
     */
    public String getSerientitel() {
        return serientitel;
    }

    /**
     * Set the value of serientitel
     *
     * @param serientitel new value of serientitel
     */
    public void setSerientitel(String serientitel) {
        String oldSerientitel = this.serientitel;
        this.serientitel = serientitel;
        propertyChangeSupport.firePropertyChange(PROP_SERIENTITEL, oldSerientitel, serientitel);
    }

    protected int staffeln;

    public static final String PROP_STAFFELN = "staffeln";

    /**
     * Get the value of staffeln
     *
     * @return the value of staffeln
     */
    public int getStaffeln() {
        return staffeln;
    }

    /**
     * Set the value of staffeln
     *
     * @param staffeln new value of staffeln
     */
    public void setStaffeln(int staffeln) {
        int oldStaffeln = this.staffeln;
        this.staffeln = staffeln;
        propertyChangeSupport.firePropertyChange(PROP_STAFFELN, oldStaffeln, staffeln);
    }

    protected int anzahlFolgen;

    public static final String PROP_ANZAHLFOLGEN = "anzahlFolgen";

    /**
     * Get the value of anzahlFolgen
     *
     * @return the value of anzahlFolgen
     */
    public int getAnzahlFolgen() {
        return anzahlFolgen;
    }

    /**
     * Set the value of anzahlFolgen
     *
     * @param anzahlFolgen new value of anzahlFolgen
     */
    public void setAnzahlFolgen(int anzahlFolgen) {
        int oldAnzahlFolgen = this.anzahlFolgen;
        this.anzahlFolgen = anzahlFolgen;
        propertyChangeSupport.firePropertyChange(PROP_ANZAHLFOLGEN, oldAnzahlFolgen, anzahlFolgen);
    }

    protected int bewertung;

    public static final String PROP_BEWERTUNG = "bewertung";

    /**
     * Get the value of bewertung
     *
     * @return the value of bewertung
     */
    public int getBewertung() {
        return bewertung;
    }

    /**
     * Set the value of bewertung
     *
     * @param bewertung new value of bewertung
     */
    public void setBewertung(int bewertung) {
        int oldBewertung = this.bewertung;
        this.bewertung = bewertung;
        propertyChangeSupport.firePropertyChange(PROP_BEWERTUNG, oldBewertung, bewertung);
    }

    public SerienformularBean() {
    }
}
