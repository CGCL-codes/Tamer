package org.mitre.dm.qud.conditions;

import org.mitre.midiki.agent.*;
import org.mitre.midiki.state.*;
import org.mitre.midiki.logic.*;
import java.util.*;

public class ProgramStateIs extends Condition {

    Object st;

    public ProgramStateIs(Object o) {
        super();
        st = o;
    }

    public boolean test(ImmutableInfoState infoState, Bindings bindings) {
        Object state = infoState.cell("is").get("program_state");
        return infoState.getUnifier().matchTerms(state, st, bindings);
    }
}
