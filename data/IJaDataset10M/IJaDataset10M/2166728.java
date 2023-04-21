package ro.mosc.reco.algebra;

import java.util.Map;

/**
 * Class encapsulation a mapping (as an isomorphic function) and isomorphism (similarity)
 * degree between two algebras
 */
public class Isomorphism {

    private Algebra sourceAlgebra = null;

    private Algebra destinationAlgebra = null;

    private Map mapping = null;

    private double isomorphismValue = 0;

    public Isomorphism(Algebra sourceAlgebra, Algebra destinationAlgebra, Map mapping, double isomorphismValue) {
        this.sourceAlgebra = sourceAlgebra;
        this.destinationAlgebra = destinationAlgebra;
        this.mapping = mapping;
        this.isomorphismValue = isomorphismValue;
    }

    public Algebra getSourceAlgebra() {
        return sourceAlgebra;
    }

    public Algebra getDestinationAlgebra() {
        return destinationAlgebra;
    }

    public Map getMapping() {
        return mapping;
    }

    public double getIsomorphismValue() {
        return isomorphismValue;
    }

    public String toString() {
        String retValue = "ISOMORPHISM:\n\tvalue:" + isomorphismValue + "\n\tmapping:\n";
        for (Object key : mapping.keySet()) {
            retValue += "-mapping<" + key.getClass() + ">(" + key + ")=" + mapping.get(key) + "\n";
        }
        return retValue;
    }
}
