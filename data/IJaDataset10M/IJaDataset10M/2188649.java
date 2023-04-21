package name.levering.ryan.sparql.logic.debug;

import java.util.Collection;
import name.levering.ryan.sparql.common.RdfBindingSet;
import name.levering.ryan.sparql.common.RdfSource;
import name.levering.ryan.sparql.model.data.TripleConstraintData;
import name.levering.ryan.sparql.model.logic.ConstraintLogic;

public class TripleConstraintDebug implements ConstraintLogic {

    private final ConstraintLogic logic;

    private final TripleConstraintData data;

    private final DebugListener out;

    public TripleConstraintDebug(TripleConstraintData data, ConstraintLogic logic, DebugListener listener) {
        this.data = data;
        this.logic = logic;
        this.out = listener;
    }

    public RdfBindingSet constrain(RdfBindingSet bindings, RdfSource source, Collection defaultDatasets, Collection namedDatasets) {
        this.out.tripleFetchPreExecute(this.data);
        long start = System.currentTimeMillis();
        RdfBindingSet returnSet = this.logic.constrain(bindings, source, defaultDatasets, namedDatasets);
        long end = System.currentTimeMillis();
        this.out.tripleFetchPostExecute(end - start, returnSet);
        return returnSet;
    }
}
