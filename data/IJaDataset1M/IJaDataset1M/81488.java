package de.ifgi.simcat2.reasoner.rendering;

import java.util.HashMap;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * A class for registering and requesting unknown OWLObjects.
 * 
 * @author Christoph
 *
 */
public class UnknownAxioms {

    private static HashMap<Integer, OWLObject> map = new HashMap<Integer, OWLObject>();

    public static OWLObject get(int hashcode) {
        return map.get(hashcode);
    }

    public static void add(int hashcode, OWLObject o_ohoh) {
        map.put(hashcode, o_ohoh);
    }
}
