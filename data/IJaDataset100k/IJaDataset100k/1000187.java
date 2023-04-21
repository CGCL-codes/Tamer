package SASLib.Util;

import java.util.Collection;

/**
 * This is a interface for Graphs of Data
 * @author Wil Cecil
 */
public interface DataGraph extends Graph {

    /**
     * This will add a collection of data to the graph
     * @param c
     */
    public void addData(Collection c);

    /**
     * This will remove a collection of data to the graph, if data is found.
     * @param c
     */
    public void removeData(Collection c);
}
