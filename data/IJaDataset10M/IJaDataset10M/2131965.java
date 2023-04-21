package uk.ac.warwick.dcs.cokefolk.server.backend.optimisations;

import uk.ac.warwick.dcs.cokefolk.server.operations.types.TupleSet;
import java.util.Map;

public interface TupleSetOptimizations {

    public TupleSet restrict(BooleanExpression exp);

    public int remove(BooleanExpression exp);

    public int update(Map<String, String> attribAssignments, BooleanExpression where);
}
