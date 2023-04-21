package edu.tfh.s2.ehkbot.inventar;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 * Einkaufauftrag speicher die nötigen Informationen für einen Einkaufsaufauftrag.
 * 
 * @author s2zehn
 */
public class Einkaufsauftrag implements Serializable {

    /**
	 * Bildet mit die Statuse ab die ein Auftrag hat.
	 */
    public enum EinkaufsauftragStatus {

        ABGEBROCHEN, FERTIG, NICHTGESTARTET, NICHTMOEGLICH
    }

    private static final long serialVersionUID = 1L;

    ;

    private String auftraggeber;

    private int auftragID;

    private Date ausgangsdatum;

    private Date eingangsdatum;

    public Vector<Einkaufsposten> einkaufsposten;

    private EinkaufsauftragStatus status;

    private Warenbox warenbox;

    /**
	 * Konstruktor für Einkaufsauftrag
	 * 
	 * @param auftraggeber
	 *            Der Auftraggeber.
	 * @param eingangsdatum
	 *            Das Eingangsdatum.
	 */
    public Einkaufsauftrag(String auftraggeber, Date eingangsdatum) {
        this(auftraggeber, eingangsdatum, null);
    }

    /**
	 * Konstruktor für Einkaufsauftrag.
	 * 
	 * @param auftraggeber
	 *            Der Auftraggeber.
	 * @param eingangsdatum
	 *            Das Eingangsdatum.
	 * @param warenbox
	 *            Eine Warenbox.
	 */
    public Einkaufsauftrag(String auftraggeber, Date eingangsdatum, Warenbox warenbox) {
        super();
        this.auftraggeber = auftraggeber;
        this.eingangsdatum = eingangsdatum;
        this.warenbox = warenbox;
        this.auftragID = (int) (Math.random() * 10000);
        this.status = EinkaufsauftragStatus.NICHTGESTARTET;
        this.einkaufsposten = new Vector<Einkaufsposten>();
    }

    /**
	 * Fügt ein Posten zu der Liste der Posten ein.
	 * 
	 * @param posten
	 *            Der Einkaufsposten.
	 */
    public void addEinkausposten(Einkaufsposten posten) {
        einkaufsposten.add(posten);
    }

    /**
	 * Liefert den Auftraggeber.
	 * 
	 * @return Den Auftraggeber.
	 */
    public String getAuftraggeber() {
        return auftraggeber;
    }

    /**
	 * Liefert die AuftragsID.
	 * 
	 * @return Die AuftragsID.
	 */
    public int getAuftragID() {
        return auftragID;
    }

    /**
	 * Liefert das Ausgangsdatum.
	 * 
	 * @return Das Ausgangsdatum.
	 */
    public Date getAusgangsdatum() {
        return ausgangsdatum;
    }

    /**
	 * Liefert das Eingangsdatum.
	 * 
	 * @return Das Eingangsdatum.
	 */
    public Date getEingangsdatum() {
        return eingangsdatum;
    }

    /**
	 * Liefert die Liste der Posten.
	 * 
	 * @return Die Einkaufsposten Liste.
	 */
    public Vector<Einkaufsposten> getEinkaufsposten() {
        return einkaufsposten;
    }

    /**
	 * Den Status des Einkaufsauftrag.
	 * 
	 * @return Den Status.
	 */
    public EinkaufsauftragStatus getStatus() {
        return status;
    }

    /**
	 * Die aktuelle Warenbox.
	 * 
	 * @return Die Warenbox.
	 */
    public Warenbox getWarenbox() {
        return warenbox;
    }

    /**
	 * Setzt die AuftragsID.
	 * 
	 * @param auftragID
	 *            Die zu setzende AuftragsID.
	 */
    public void setAuftragID(int auftragID) {
        this.auftragID = auftragID;
    }

    /**
	 * Setzt das Ausgangsdatum.
	 * 
	 * @param ausgangsdatum
	 *            Das zu setzende Ausgangsdatum.
	 */
    public void setAusgangsdatum(Date ausgangsdatum) {
        this.ausgangsdatum = ausgangsdatum;
    }

    /**
	 * Setzt die Liste der Einkaufsposten.
	 * 
	 * @param einkaufsposten
	 *            Die Liste der Einkaufsposten.
	 */
    public void setEinkaufsposten(Vector<Einkaufsposten> einkaufsposten) {
        this.einkaufsposten = einkaufsposten;
    }

    /**
	 * Setzt den Einkaufsauftrag status.
	 * 
	 * @param status
	 *            Den Einkaufsauftrag status.
	 */
    public void setStatus(EinkaufsauftragStatus status) {
        this.status = status;
    }

    /**
	 * Setzt die Warenbox.
	 * 
	 * @param warenbox
	 *            Die Warenbox.
	 */
    public void setWarenbox(Warenbox warenbox) {
        this.warenbox = warenbox;
    }
}
