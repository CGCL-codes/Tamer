package alice.cartago.util;

import alice.cartago.*;
import alice.cartago.util.Tuple;
import alice.cartago.util.TupleTemplate;

@ARTIFACT_INFO(manual_file = "alice/cartago/util/SimpleTupleSpace-manual.txt")
public class SimpleTupleSpace extends Artifact {

    TupleSet tset;

    @OPERATION
    void init() {
        tset = new TupleSet();
    }

    @OPERATION
    void out(Tuple t) {
        tset.add(t);
    }

    @OPERATION
    void in(TupleTemplate tt) {
        Tuple t = tset.removeMatching(tt);
        if (t != null) {
            signal("tuple", t);
        } else {
            nextStep("completeIN", tt);
        }
    }

    @OPSTEP(guard = "foundMatch")
    void completeIN(TupleTemplate tt) {
        Tuple t = tset.removeMatching(tt);
        signal("tuple", t);
    }

    @OPERATION
    void rd(TupleTemplate tt) {
        Tuple t = tset.readMatching(tt);
        if (t != null) {
            signal("tuple", t);
        } else {
            nextStep("completeRD", tt);
        }
    }

    @OPSTEP(guard = "foundMatch")
    void completeRD(TupleTemplate tt) {
        Tuple t = tset.readMatching(tt);
        signal("tuple", t);
    }

    @GUARD
    boolean foundMatch(TupleTemplate tt) {
        return tset.hasTupleMatching(tt);
    }
}
