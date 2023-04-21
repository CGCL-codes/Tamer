package pt.utl.ist.lucene.utils.dijkstra;

/**
 *
 * @author Renaud Waldura &lt;renaud+tw@waldura.com&gt;
 * @version $Id: City.java 2367 2007-08-20 21:47:25Z renaud $
 */
public interface DijkstraNode extends Comparable<DijkstraNode> {

    public DijkstraNode getNode(int index);

    int getIndex();
}
