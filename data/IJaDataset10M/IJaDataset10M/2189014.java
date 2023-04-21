package de.mnit.basis.daten.konstant.position;

/**
 * @author Michael Nitsche
 * 18.04.2009	Erstellt
 * 26.04.2009	Unterschied zum alten System:
 * 				- Alle Konstanten-Objekte in einer Klasse als Enums, nicht in verschiedenen Klassen
 * 				- Rückgabewert in den Interfaces ist die Enum-Klasse, nicht die Schnittstelle
 *
 * Achtung:
 * - Die Interfaces dürfen nicht voneinander erben!!!
 */
public class H_POSITION {

    protected enum MITTE implements POSITION_MITTE, POSITION_H, POSITION_V, POSITION_GERADE, POSITION {

        MITTE
    }

    protected enum H implements POSITION_H, POSITION_GERADE, POSITION {

        LINKS, RECHTS
    }

    protected enum V implements POSITION_V, POSITION_GERADE, POSITION {

        OBEN, UNTEN
    }

    protected enum ECKE implements POSITION {

        LINKS_OBEN, RECHTS_OBEN, LINKS_UNTEN, RECHTS_UNTEN
    }
}
