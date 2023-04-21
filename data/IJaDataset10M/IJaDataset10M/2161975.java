package name.angoca.zemucan.core.graph.model;

/**
 * This objects abstracts a set of possible ways in just one. This is useful for
 * factoring several ways in a common point.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>1.0.0 Class creation.</li>
 * <li>1.1.0 final and extract method.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.1.0 2011-11-08
 */
public final class NextNode extends TrajectoryGraphNode {

    /**
     * Constructor of the Next Node.
     *
     * @param name
     *            Name of the node.
     * @param graph
     *            Associated graph.
     */
    public NextNode(final String name, final Graph graph) {
        super(name, graph);
    }
}
