package at.jku.semwiq.mediator.federator.inst;

import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.OntClass;

/**
 * @author dorgon
 *
 * minimal conjuctive type set for a BGP which has triple patterns with equal subject
 * 
 * To match a BGP, the conjunction of all classes in the associated set has to be satisfied.
 */
public class SubjectTypeCache {

    private final Map<Node, Set<OntClass>> cache;

    /**
	 * constructor
	 */
    public SubjectTypeCache() {
        cache = new Hashtable<Node, Set<OntClass>>();
    }

    /**
	 * @param subject
	 * @param type
	 * @return true if the cache didn't already have type for subject
	 */
    public boolean addType(Node subject, OntClass type) {
        Set<OntClass> set = getTypeSet(subject);
        if (set == Collections.EMPTY_SET) {
            set = new HashSet<OntClass>();
            cache.put(subject, set);
        }
        return set.add(type);
    }

    /**
	 * 
	 * @param subject
	 * @param types
	 */
    public void setTypeSet(Node subject, Set<OntClass> types) {
        cache.put(subject, types);
    }

    /**
	 * @param subject
	 * @return
	 */
    public Set<OntClass> getTypeSet(Node subject) {
        Set<OntClass> types = cache.get(subject);
        if (types != null) return types; else return Collections.EMPTY_SET;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node n : cache.keySet()) {
            sb.append(n).append(": ").append(cache.get(n)).append("\n");
        }
        return sb.toString();
    }
}
