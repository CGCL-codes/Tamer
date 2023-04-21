package org.mushroomdb.engine.optimizer.impl;

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
public class JoinSwitchingVisitor implements RelationOperationVisitor {

    public void visitProjection(ProjectionOperation projectionOperation) {
    }

    public void visitScan(ScanOperation scanOperation) {
    }

    public void visitSelection(SelectionOperation selectionOperation) {
    }

    public void visitIntersection(IntersectionOperation intersectionOperation) {
    }

    public void visitUnion(UnionOperation unionOperation) {
    }

    public void visitCrossProduct(CrossProductOperation crossProductOperation) {
    }

    public void visitSortMergeJoin(SortMergeJoinOperation sortMergeJoinOperation) {
    }

    /**
	 * 
	 * @param indexSearchOperation
	 */
    public void visitIndexSearch(IndexSearchOperation indexSearchOperation) {
    }

    public void visitIndexNestedLoopJoin(IndexNestedLoopJoinOperation indexNestedLoopJoinOperation) {
    }
}
