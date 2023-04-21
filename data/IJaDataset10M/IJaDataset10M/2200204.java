package net.sf.econtycoon.core;

import java.lang.reflect.Array;
import java.io.Serializable;
import net.sf.econtycoon.core.ai.DefaultAI;
import net.sf.econtycoon.core.ai.KonnAI;
import net.sf.econtycoon.core.data.Strings;
import net.sf.econtycoon.core.data.Werte;
import net.sf.econtycoon.gui.swing.Tab_Statistik;

/**
 * Die Spielklasse ist die �bergeordnete Klasse f�r alle Spielrelevanten ereignisse
 * 
 * @author Konstantin
 */
public class Spiel implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Rohstoff rohstoff[];

    private Spieler spieler;

    private int rundenzahl = 0;

    private int spielerzahl = 3;

    private AISpieler konkurrenten[];

    private transient Master master;

    private final Event eventklasse = new Event(this);

    /**
	 * Konstruiert ein neues Spiel
	 *
	 * @param master the master
	 */
    public Spiel(Master master) {
        this.master = master;
        generateRohstoffe();
        generateSpieler();
    }

    /**
	 * Generiert die Rohstoffe
	 */
    public void generateRohstoffe() {
        if (Master.OPTIONEN.isKonsolespiel()) System.out.println("[Spiel]:Starte Rohstoffgenerierung");
        rohstoff = new Rohstoff[Werte.ROHSTOFFZAHL];
        for (int i = 0; i < Werte.ROHSTOFFZAHL; i++) {
            boolean kaufbar;
            if (Werte.ROHSTOFF[i][5] == 1) kaufbar = true; else kaufbar = false;
            boolean verkaufbar;
            if (Werte.ROHSTOFF[i][6] == 1) verkaufbar = true; else verkaufbar = false;
            if (Werte.ROHSTOFF[i][3] == -1) {
                rohstoff[i] = new Rohstoff(i, Strings.getString("Rohstoff." + i), Werte.ROHSTOFF[i][0], Werte.ROHSTOFF[i][1], Werte.ROHSTOFF[i][2], kaufbar, verkaufbar, Master.OPTIONEN);
            }
        }
        for (int i = 0; i < Werte.ROHSTOFFZAHL; i++) {
            boolean kaufbar;
            if (Werte.ROHSTOFF[i][5] == 1) kaufbar = true; else kaufbar = false;
            boolean verkaufbar;
            if (Werte.ROHSTOFF[i][6] == 1) verkaufbar = true; else verkaufbar = false;
            if (Werte.ROHSTOFF[i][3] != -1) {
                rohstoff[i] = new Rohstoff(i, Strings.getString("Rohstoff." + i), getRohstoffint(Werte.ROHSTOFF[i][3]), (Werte.ROHSTOFF[i][4]) / 100.00, Werte.ROHSTOFF[i][2], kaufbar, verkaufbar, Master.OPTIONEN);
            }
            if (Master.OPTIONEN.isKonsolespiel()) System.out.println("[Spiel]:Rohstoff generiert: (ID " + i + ") " + rohstoff[i].getName());
        }
        if (Master.OPTIONEN.isKonsolespiel()) System.out.println("[Spiel]: Rohstoffgenerierung abgeschlossen");
    }

    /**
	 * Generiert die Spieler
	 */
    public void generateSpieler() {
        if (Master.OPTIONEN.isKonsolespiel()) System.out.println("[Spiel]: Starte Spielergenerierung");
        if (spielerzahl >= 2) {
            konkurrenten = new AISpieler[spielerzahl - 1];
            konkurrenten[0] = new DefaultAI(this);
            konkurrenten[0].reset();
            konkurrenten[1] = new KonnAI(this);
            konkurrenten[1].reset();
        }
        spieler = new Spieler(this);
        if (Master.OPTIONEN.isKonsolespiel()) System.out.println("[Spiel]: Spielergenerierung abgeschlossen");
    }

    /**
	 * Wird von Master.Timer aufrufen, steuert alle tick-methoden
	 */
    public void tick() {
        rundenzahl++;
        if (Master.OPTIONEN.isKonsolespiel()) System.out.println("[Spiel]: Tick Runde " + rundenzahl);
        for (int i = 0; i < Array.getLength(rohstoff); i++) {
            rohstoff[i].tick();
        }
        events();
        spieler.tick();
        for (int i = 1; i < spielerzahl; i++) {
            konkurrenten[i - 1].tick();
            konkurrenten[i - 1].aktion();
        }
        master.getGUI().tfUpdate();
    }

    /**
	 * Sagt der Event-Klasse, Events durchzuf�hren und gibt die Nachrichten an den GUI weiter
	 */
    public void events() {
        eventklasse.checkForEvent();
        if (eventklasse.isEvent()) {
            master.getGUI().setNachrichtentext(eventklasse.getEventMessage());
        }
    }

    /**
	 * Rohstoffkauf
	 *
	 * @param rohstoff the rohstoff
	 * @param spieler the spieler
	 * @param menge the menge
	 */
    public void kaufen(Rohstoff rohstoff, Spieler spieler, int menge) {
        for (int i = 0; i < menge; i++) {
            spieler.kaufen(rohstoff);
        }
        master.getGUI().getTabstatistik().statistikUpdate(Tab_Statistik.GEHANDELT, this.spieler);
        master.getGUI().tfUpdate();
    }

    /**
	 * Rohstoffverkauf
	 *
	 * @param rohstoff the rohstoff
	 * @param spieler the spieler
	 * @param menge the menge
	 */
    public void verkaufen(Rohstoff rohstoff, Spieler spieler, int menge) {
        for (int i = 0; i < menge; i++) {
            spieler.verkaufen(rohstoff);
        }
        master.getGUI().getTabstatistik().statistikUpdate(Tab_Statistik.GEHANDELT, this.spieler);
        master.getGUI().tfUpdate();
    }

    /**
	 * Resetet das Spiel
	 */
    public void reset() {
        rundenzahl = 0;
        for (int i = 0; i < rohstoff.length; i++) {
            rohstoff[i] = null;
        }
        spieler = null;
        for (int i = 0; i < konkurrenten.length; i++) {
            konkurrenten[i] = null;
        }
    }

    /**
	 * Gets the rohstoffint.
	 *
	 * @param i ID
	 * @return Rohstoff mit der ID
	 */
    public Rohstoff getRohstoffint(int i) {
        return rohstoff[i];
    }

    /**
	 * Sets the rohstoffint.
	 *
	 * @param i ID
	 * @param r Rohstoff
	 */
    public void setRohstoffint(int i, Rohstoff r) {
        rohstoff[i] = r;
    }

    /**
	 * Gets the rundenzahl.
	 *
	 * @return the rundenzahl
	 */
    public int getRundenzahl() {
        return rundenzahl;
    }

    /**
	 * Sets the rundenzahl.
	 *
	 * @param rundenzahl the new rundenzahl
	 */
    public void setRundenzahl(int rundenzahl) {
        this.rundenzahl = rundenzahl;
    }

    /**
	 * Gets the spieler.
	 *
	 * @return the spieler
	 */
    public Spieler getSpieler() {
        return spieler;
    }

    /**
	 * Sets the spieler.
	 *
	 * @param spieler the new spieler
	 */
    public void setSpieler(Spieler spieler) {
        this.spieler = spieler;
    }

    /**
	 * Gets the konkurrenten.
	 *
	 * @param i Nummer des Konkurrenten (f�ngt bei 0 an)
	 * @return the konkurrenten
	 */
    public AISpieler getKonkurrenten(int i) {
        return konkurrenten[i];
    }

    /**
	 * Sets the konkurrenten.
	 *
	 * @param i Nummer des Konkurrenten (f�ngt bei 0 an)
	 * @param aispieler Konkurrenttyp
	 */
    public void setKonkurrenten(int i, AISpieler aispieler) {
        konkurrenten[i] = aispieler;
    }

    /**
	 * Gets the spielerzahl.
	 *
	 * @return the spielerzahl
	 */
    public int getSpielerzahl() {
        return spielerzahl;
    }

    /**
	 * Sets the spielerzahl.
	 *
	 * @param i the new spielerzahl
	 */
    public void setSpielerzahl(int i) {
        spielerzahl = i;
    }

    /**
	 * Gets the master.
	 *
	 * @return the master
	 */
    public Master getMaster() {
        return master;
    }

    /**
	 * @param master2
	 */
    public void setMaster(Master master) {
        this.master = master;
    }
}
