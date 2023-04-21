package risk.game;

import risk.player.*;
import java.util.*;

/**
 * Die Klasse Continent repraesentiert ein Kontinent innerhalb der Spielkarte.
 * 
 */
public class Continent {

    /**
     * Name des Kontinents.
     */
    private String name;

    /**
     * Bonuspunkte fuer den Spieler, der Besetzer aller Territorien 
     * dieses Kontinents ist.
     */
    private int bonus;

    /**
     * Wenn ein Spieler den gesamten Kontinent besitzt, wird dieser als Besitzer
     * eingetragen.
     */
    private PlayerModel owner;

    /**
     * Liste der Laender dieses Kontinents.
     */
    private ArrayList territories;

    /**
     * Erstellt eine neue Instanz von Continent.
     *
     * @param name         Name des Kontinents.
     * @param bonus        Bonuspunkte dieses Kontinents fuer alleinigen 
     *                     Besitzer.
     * @param territories  Liste mit den Laendern des Kontinents.   
     */
    public Continent(String name, int bonus, ArrayList territories) {
        this.name = name;
        this.bonus = bonus;
        this.territories = territories;
    }

    /**
     * Ueberprueft ob ein Spieler eine komplette Runde der Besitzer eines 
     * gesamten Kontinents war und gibt im Erfolgsfall den Kontinentbonus 
     * zurueck. Ansonsten wird 0 zurueckgegeben.
     * Diese Ueberpruefung findet jedesmal am Anfang eines Spielzuges vor der 
     * Herausgabe neuer Armeen statt.
     *
     * @param   player Der Spieler, fuer den die Pruefung auf Kontinentbonus
     *                 vorgenommen werden soll
     * @return  Der Kontinentbonus oder 0.
     */
    public int getPlayerBonus(PlayerModel player) {
        if (player.equals(this.owner)) {
            return this.bonus;
        }
        return 0;
    }

    /**
     * Setzt moeglicherweise einen Spieler als alleinigen Besitzer des 
     * Kontinents.
     */
    public void setPossibleOwner() {
        boolean noOwner = false;
        PlayerModel firstPlayer = ((Territory) this.territories.get(0)).getOwner();
        for (int i = 1; i < territories.size(); i++) {
            PlayerModel anotherPlayer = ((Territory) this.territories.get(i)).getOwner();
            if (!firstPlayer.equals(anotherPlayer)) {
                this.owner = null;
                noOwner = true;
            }
        }
        if (!noOwner) {
            this.owner = firstPlayer;
        }
    }

    /**
     * Gibt, sofern vorhanden, den Besitzer des gesamten Kontinents zurueck.
     *
     * @return  Der Spieler, der den Kontinent besitzt oder <code>null</code>.
     */
    public PlayerModel getOwner() {
        return this.owner;
    }

    /**
     * Gibt den Namen des Kontinents zurueck.
     *
     * @return  Name des Kontinents.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Rueckgabe der Liste der Laender, die zu diesem Continent gehoeren.
     *
     * @return  Die Liste der Laender, die dem Kontinent zugeordnet sind.
     */
    public ArrayList getTerretorienList() {
        return this.territories;
    }
}
