package org.gjt.universe;

/** Enumerated type for the Relationship class.
	@see Relationship
*/
public final class RelationshipType {

    private String id;

    private RelationshipType(String inID) {
        id = inID;
    }

    public String toString() {
        return id;
    }

    public static final RelationshipType Unexplored = new RelationshipType("Unexplored");

    public static final RelationshipType Explored = new RelationshipType("Explored");

    public static final RelationshipType Owned = new RelationshipType("Owned");

    public static final RelationshipType Enemy = new RelationshipType("Enemy");

    public static final RelationshipType Neutral = new RelationshipType("Neutral");

    public static final RelationshipType Ally = new RelationshipType("Ally");

    public static final RelationshipType UnknownOccupied = new RelationshipType("UnknownOccupied");
}
