package org.mushroomdb.engine.optimizer.impl;

import org.mushroomdb.engine.operation.RelationOperation;
import org.mushroomdb.engine.optimizer.Optimizer;

/**
 * @author Admin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AccessMethodDefiningOptimizer implements Optimizer {

    public RelationOperation optimize(RelationOperation relationOperation) {
        AccessMethodDefiningVisitor visitor = new AccessMethodDefiningVisitor(relationOperation);
        relationOperation.accept(visitor);
        return visitor.getInitialRelationOperation();
    }
}
