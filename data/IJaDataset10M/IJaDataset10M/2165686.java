package org.mitre.dm.qud.conditions;

import org.mitre.midiki.agent.*;
import org.mitre.midiki.state.*;
import org.mitre.midiki.logic.*;
import java.util.*;

public class AgendaIsEmpty extends Condition {

    public AgendaIsEmpty() {
        super();
    }

    public boolean test(ImmutableInfoState infoState, Bindings bindings) {
        Object agenda = infoState.cell("is").cell("private").get("agenda");
        if (agenda == null) return false;
        if (!(agenda instanceof List)) return false;
        return ((List) agenda).isEmpty();
    }
}
