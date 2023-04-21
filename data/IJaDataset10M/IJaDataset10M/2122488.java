package de.bea.util;

/**
 * Implementierende Klassen m�ssen eindeutige sog. Nametags f�r
 * alle ihre Instanzen liefern.
 * Diese Nametags k�nnen von anderen Komponenten beliebiger Schichten verwendet werden.
 * Hauptintention f�r die Einf�hrung: die GUI Schicht benutzt den Nametag von
 * Objekten, um �bersetzungen bzw. Anzeigetexte zu definieren.
 */
public interface Nameable {

    public String getNameTag();

    public String getShortTag();
}
