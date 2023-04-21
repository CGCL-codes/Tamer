package uk.org.biotext.graphspider.tools;

import java.util.Collection;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * The Class GraphPrettifier.
 */
public class GraphPrettifier {

    /**
     * To string.
     * 
     * @param graph
     *            the graph
     * 
     * @return the string
     */
    public static String toString(Collection<TypedDependency> graph) {
        StringBuilder sb = new StringBuilder(graph.size() * 20);
        for (TypedDependency arc : graph) {
            sb.append(arc.gov().value());
            sb.append(" -> ");
            sb.append(arc.reln().toString());
            sb.append(" -> ");
            sb.append(arc.dep().value());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * To detailed string.
     * 
     * @param graph
     *            the graph
     * 
     * @return the string
     */
    public static String toDetailedString(Collection<TypedDependency> graph) {
        StringBuilder sb = new StringBuilder(graph.size() * 40);
        for (TypedDependency arc : graph) {
            sb.append(arc.gov().parent().value());
            sb.append("~~");
            sb.append(arc.gov());
            sb.append(" -> ");
            sb.append(arc.reln().toString());
            sb.append(" -> ");
            sb.append(arc.dep().parent().value());
            sb.append("~~");
            sb.append(arc.dep());
            sb.append("\n");
        }
        return sb.toString();
    }
}
