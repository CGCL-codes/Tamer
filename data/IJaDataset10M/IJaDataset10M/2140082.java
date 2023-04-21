package samples.reduction.client;

import samples.reduction.common.*;
import samples.reduction.supplier.*;

public class ReductionClientFieldFailure {

    public static void doSomething(SupplierField sf) {
        sf.jas = new Object();
    }
}
