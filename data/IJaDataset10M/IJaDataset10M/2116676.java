package uk.ac.shef.wit.aleph.algorithm.graph.io;

import no.uib.cipr.matrix.Matrix;
import uk.ac.shef.wit.aleph.AlephException;

/**
 * Minimal interface that classes that load a graph into memory must subscribe to. 
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public interface GraphReader {

    /**
    * Load the graph into memory.
    * @return a {@link Matrix} representing the graph.
    * @throws AlephException
    */
    Matrix read() throws AlephException;

    /**
    * Load the graph into memory. If the number of nodes is known in advance, use this method instead.
    * @param numNodes the number of nodes in the graph.
    * @return a {@link Matrix} representing the graph.
    * @throws AlephException
    */
    Matrix read(int numNodes) throws AlephException;
}
