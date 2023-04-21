package edu.indiana.extreme.xbaya.graph.dynamic;

import edu.indiana.extreme.xbaya.graph.DataPort;
import edu.indiana.extreme.xbaya.graph.GraphException;

/**
 * @author Chathura Herath
 */
public interface PortAddable {

    public abstract void removeLastDynamicallyAddedInPort() throws GraphException;

    public abstract DataPort getFreeInPort();
}
