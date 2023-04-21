package net.sourceforge.combean.interfaces.mathprog.lp.model;

/**
 * An LP component which can be accessed column-wise.
 * 
 * @author schickin
 *
 */
public interface LPModelColumns extends LPModelComponent {

    /**
     * Convert a local index into a model index.
     * 
     * @param localColumn the local column.
     * @return the correponding model index.
     */
    public LPModelIndex getColumnModelIndex(int localColumn);

    public LPSparseVector getColumnVector(int localColumn);
}
