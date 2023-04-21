package com.soramaki.fna.network.matrix;

import java.util.Map;

/**
 * Matrix where each matrix entry represents a node. 
 * 
 * @author Kimmo Soramaki
 * 
 * @param <Vertex> Node type.
 */
public interface DoubleMatrix<Vertex> {

    /**
	 * Returns the matrix. If there is an edge between two nodes, then there is an
	 * entry in the matrix. Otherwise, it is 0.
	 * 
	 * @return  Matrix representation of the graph
	 */
    double[][] getMatrix();

    /**
	 * Returns the value at the specified row and column.
	 * 
	 * @param row Row index
	 * @param column Column index
	 * @return Matrix value
	 */
    double getValue(int row, int column);

    /**
	 * Returns the node index. If the node is invalid i.e. not in the matrix, null
	 * is returned.
	 * 
	 * @param node Node
	 * @return node Matrix column/row index
	 */
    int getNodeIndex(Vertex v);

    /**
	 * Returns a map of nodes and their matrix column/row indexes
	 *
	 * @return Mapping of node id's to matrix column/row index
	 */
    public Map<Vertex, Integer> getVertexIdMap();
}
