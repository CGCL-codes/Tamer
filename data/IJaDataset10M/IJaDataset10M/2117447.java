package conga.io;

import conga.param.tree.Tree;

/** @author Justin Caballero */
public interface Sink {

    /**
     * Sends params from <code>tree</code> to this Sink.  Most sinks will
     * flatten the hierarchal structure of the tree in the process.
     *
     * @throws conga.CongaRuntimeException if I/O or other errors occur
     */
    void sink(Tree tree);
}
