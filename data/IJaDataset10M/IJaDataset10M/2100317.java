package modules;

import control.*;

/**
 * Die Klasse erweitert "HS485_Modul" um die spezifischen Eigenschaften
 * und Methoden der Aktoren.
 */
public class HS485_Aktor extends modules.HS485_Modul {

    public HS485_Aktor(Bus hausbus) {
        super(hausbus);
    }

    /**
 * An den Hausbus erfolgt die Aufforderung den Aktorzustand mit der
 * angegebenen Aktornummer zu ermitteln.
 * 
 * @param aktornummer 
 * @return Zustand des Aktors als character
 */
    public byte getAktorZustand(char aktornummer) {
        return hausbus.getAktorZustand(address, aktornummer);
    }
}
