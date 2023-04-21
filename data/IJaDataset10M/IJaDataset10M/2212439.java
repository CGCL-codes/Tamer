package de.lamasep.map;

import de.lamasep.map.geo.Position;

/**
 * Definiert einen Point of Interest.
 * @see Node
 * @author Johanna Böhm <mail@johanna-boehm.de>
 */
public class PointOfInterest extends Node {

    /**
     * Version der Klasse für Serialisierung.
     * @see java.io.Serializable
     */
    public static final long serialVersionUID = 2L;

    /**
     * Typ des POIs.
     */
    private final PoiType type;

    /**
     * Konstruktor - Initialisierung mit der Bezeichnung, der Position, dem Typ
     * und der ID des POI-Knoten.
     * @param name      Name des POIs
     * @param position  Lage des POIs, <code> position != null </code>
     * @param id        Eindeutige ID
     * @param type      Typ des POI's, <code>type != null</code>
     * @throws IllegalArgumentException falls
     *          <code> position == null || type == null</code>
     */
    public PointOfInterest(String name, Position position, long id, PoiType type) {
        super(position, id);
        if (type == null) {
            throw new IllegalArgumentException("type is null!");
        }
        setName(name);
        this.type = type;
    }

    /**
     * Gibt den Typ des POIs zurück.
     * @return Typ des POIs
     */
    public PoiType getType() {
        return type;
    }
}
