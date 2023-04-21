package uk.ac.roslin.ensembl.model.relationship;

import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.HashMap;
import uk.ac.roslin.ensembl.config.EnsemblType;

public class RelationshipType extends EnsemblType {

    private static HashMap<String, RelationshipType> typeHash;

    public static final RelationshipType UNKNOWN = RelationshipType.makeRelationshipType("unknown");

    private static RelationshipType makeRelationshipType(String typeName) {
        if (RelationshipType.typeHash == null) {
            RelationshipType.typeHash = new HashMap<String, RelationshipType>();
        }
        RelationshipType rt = getRelationshipType(typeName);
        if (rt == null) {
            rt = new RelationshipType(typeName);
            RelationshipType.typeHash.put(typeName, rt);
        }
        return rt;
    }

    protected RelationshipType(String value) {
        this.label = value;
    }

    public static Collection<? extends RelationshipType> getAllTypes() {
        return typeHash.values();
    }

    public static RelationshipType getRelationshipType(String value) {
        return typeHash.get(value);
    }

    /**
     * Ensure Singleton class
     */
    private Object readResolve() throws ObjectStreamException {
        return this;
    }

    /**
     * Overrides the Object method to test for equality
     * @param   obj   the reference object with which to compare.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        String otherString = obj.toString();
        return this.toString().equals(otherString);
    }

    /**
     * hashCode() is overriden because we have overridden equals.
     * @return int the hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.label != null ? this.label.hashCode() : 0);
        return hash;
    }
}
