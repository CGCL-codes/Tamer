package org.modss.facilitator.util.collection.matrix;

/**
 * Interface representing the source of {@link MatrixEvent}s.  
 *
 * @author mag@netstorm.net.au
 */
public interface MatrixEventSource {

    /**
	 * Adds the specified {@link MatrixListener} to receive 
     * {@link MatrixEvent}s associated with this matrix.
     *
	 * @param l the matrix listener to add.
	 */
    public void addMatrixListener(MatrixListener l);

    /**
	 * Removes the specified {@link MatrixListener} to receive 
     * {@link MatrixEvent}s
	 * associated with this matrix.
     *
	 * @param l the matrix listener to remove.
	 * @param l the matrix listener
	 */
    public void removeMatrixListener(MatrixListener l);
}
