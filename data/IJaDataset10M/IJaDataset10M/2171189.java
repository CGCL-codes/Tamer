package org.mushroomdb.engine.optimizer.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.mushroomdb.catalog.Column;
import org.mushroomdb.engine.operation.impl.CrossProductOperation;
import org.mushroomdb.engine.operation.impl.IntersectionOperation;
import org.mushroomdb.engine.operation.impl.ProjectionOperation;
import org.mushroomdb.engine.operation.impl.ScanOperation;
import org.mushroomdb.engine.operation.impl.SelectionOperation;
import org.mushroomdb.engine.operation.impl.SortMergeJoinOperation;
import org.mushroomdb.engine.operation.impl.UnionOperation;
import org.mushroomdb.engine.optimizer.RelationOperationVisitor;
import org.mushroomdb.index.operation.IndexNestedLoopJoinOperation;
import org.mushroomdb.index.operation.IndexSearchOperation;

/**
 * @author Admin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OutputColumnScanningVisitor implements RelationOperationVisitor {

    private Set outputColumns;

    boolean gotProjected;

    public OutputColumnScanningVisitor() {
        this.outputColumns = new LinkedHashSet();
        this.gotProjected = false;
    }

    public void visitProjection(ProjectionOperation projectionOperation) {
        if (!gotProjected) {
            this.gotProjected = true;
            Iterator it = projectionOperation.getColumns();
            while (it.hasNext()) {
                Column column = (Column) it.next();
                this.outputColumns.add(column);
            }
        }
    }

    public void visitScan(ScanOperation scanOperation) {
        if (!gotProjected) {
            Iterator it = scanOperation.getTable().getColumns();
            while (it.hasNext()) {
                Column column = (Column) it.next();
                this.outputColumns.add(column);
            }
        }
    }

    public void visitSelection(SelectionOperation selectionOperation) {
    }

    public void visitIntersection(IntersectionOperation intersectionOperation) {
    }

    public void visitUnion(UnionOperation unionOperation) {
    }

    public void visitCrossProduct(CrossProductOperation crossProductOperation) {
    }

    public Set getColumns() {
        return this.outputColumns;
    }

    public void visitSortMergeJoin(SortMergeJoinOperation sortMergeJoinOperation) {
    }

    /**
	 * 
	 * @param indexSearchOperation
	 */
    public void visitIndexSearch(IndexSearchOperation indexSearchOperation) {
        if (!gotProjected) {
            Iterator it = indexSearchOperation.getTable().getColumns();
            while (it.hasNext()) {
                Column column = (Column) it.next();
                this.outputColumns.add(column);
            }
        }
    }

    public void visitIndexNestedLoopJoin(IndexNestedLoopJoinOperation indexNestedLoopJoinOperation) {
    }
}
