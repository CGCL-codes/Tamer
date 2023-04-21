package jung.ext.predicates;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import edu.uci.ics.jung.graph.Element;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.utils.UserData;
import org.apache.commons.collections.Predicate;

public class PredicateUtils {

    protected static final Object defaultDatum = Boolean.TRUE;

    /**
   * The method <code>addKeyFrom</code> uses the key provided by a specific
   * <code>KeyDecorator</code>. 
   * <p>A predicate that allows only elements with a certain key can be such
   * a key decorator. Calling this method leads to passing that predicate 
   * (after updating it if needed/preconfigured).
   * @param keyDecorator source for the specific key
   * @param e the element that will be decorated with the userdatum key 
   */
    public static void addKeyFrom(KeyDecorator keyDecorator, Element e) {
        e.addUserDatum(keyDecorator.getKey(), defaultDatum, UserData.REMOVE);
    }

    public static Set filterVertices(Predicate predicate, Set vertices) {
        Set result = new HashSet();
        if (!vertices.isEmpty()) {
            for (Iterator i = vertices.iterator(); i.hasNext(); ) {
                Vertex v = (Vertex) i.next();
                if (predicate.evaluate(v)) {
                    result.add(v);
                }
            }
        }
        return result;
    }

    public static Set filterEdges(Predicate predicate, Set edges) {
        Set result = new HashSet();
        if (!edges.isEmpty()) {
            for (Iterator i = edges.iterator(); i.hasNext(); ) {
                Edge e = (Edge) i.next();
                if (predicate.evaluate(e)) result.add(e);
            }
        }
        return result;
    }
}
