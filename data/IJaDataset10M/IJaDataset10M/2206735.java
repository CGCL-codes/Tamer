package net.sourceforge.combean.mathprog.lp.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import net.sourceforge.combean.interfaces.mathprog.linalg.VectorOrientation;
import net.sourceforge.combean.interfaces.mathprog.lp.model.LPModelComponent;
import net.sourceforge.combean.interfaces.mathprog.lp.model.LPModelIndex;
import net.sourceforge.combean.interfaces.mathprog.lp.model.LPModelRows;
import net.sourceforge.combean.interfaces.mathprog.lp.model.LPSparseVector;
import net.sourceforge.combean.mathprog.lp.CombinedLPSparseVector;
import org.apache.commons.collections.iterators.IteratorChain;

/**
 * A rowwise LP model component which consists of a set of rowwise
 * LP model components which are joined rowwise.
 * 
 * This means that the first row of the first subcomponent is
 * concatenated with the first row of the second subcomponent etc.
 * to make up the first row of the joint component).
 * 
 * The concatenated components must have distinct row offset ids.
 * These will be retained by the composite object.
 * 
 * The row offset ids are taken from the component
 * with the highest number of rows and the mapping to the model
 * index for a given local id must be indentical for all internal components.
 * 
 * @author schickin
 *
 */
public class ConcatenatedLPModelRows extends AbstractLPModelComponent implements LPModelRows {

    private SortedMap<Integer, LPModelRows> mapColToSubComponent = null;

    private int totalNumColumns = 0;

    private LPModelRows highestComponent = null;

    /**
     * Constructor. The component will be empty after construction.
     */
    public ConcatenatedLPModelRows() {
        super(VectorOrientation.ROWWISE);
        this.mapColToSubComponent = new TreeMap<Integer, LPModelRows>();
    }

    public LPModelIndex getRowModelIndex(int localRow) {
        return this.highestComponent.getRowModelIndex(localRow);
    }

    public Iterator getRowOffsetIds() {
        return this.highestComponent.getRowOffsetIds();
    }

    public Iterator getColumnOffsetIds() {
        IteratorChain result = new IteratorChain();
        Iterator itSubComponents = this.mapColToSubComponent.values().iterator();
        while (itSubComponents.hasNext()) {
            LPModelComponent subComponent = (LPModelComponent) itSubComponents.next();
            result.addIterator(subComponent.getColumnOffsetIds());
        }
        return result;
    }

    public LPSparseVector getRowVector(int localRow) {
        CombinedLPSparseVector result = new CombinedLPSparseVector();
        Collection components = this.mapColToSubComponent.values();
        Iterator itComp = components.iterator();
        while (itComp.hasNext()) {
            LPModelRows currRows = (LPModelRows) itComp.next();
            if (localRow < currRows.getNumRows()) {
                LPSparseVector rowVec = currRows.getRowVector(localRow);
                result.addVector(rowVec);
            }
        }
        return result;
    }

    public int getNumRows() {
        return this.highestComponent.getNumRows();
    }

    public int getNumColumns() {
        return this.totalNumColumns;
    }

    /**
     * Concatenate further rows to the component.
     * 
     * @param newComponent new rowwise component to be concatenated.
     */
    public void concatenateRows(LPModelRows newComponent) {
        int offset = this.totalNumColumns;
        this.totalNumColumns += newComponent.getNumColumns();
        this.mapColToSubComponent.put(new Integer(offset), newComponent);
        if (this.highestComponent == null || this.highestComponent.getNumRows() < newComponent.getNumRows()) {
            this.highestComponent = newComponent;
        }
    }
}
