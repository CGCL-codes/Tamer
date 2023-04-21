package org.mitre.dm.qud.conditions;

import org.mitre.midiki.agent.*;
import org.mitre.midiki.state.*;
import org.mitre.midiki.logic.*;
import java.util.*;

public class IsNotRelevantCategory extends Condition {

    Object task;

    Object category;

    public IsNotRelevantCategory(Object t, Object a) {
        super();
        task = t;
        category = a;
    }

    public boolean test(ImmutableInfoState infoState, Bindings bindings) {
        LinkedList args = new LinkedList();
        args.add(Unify.getInstance().deref(task, bindings));
        args.add(Unify.getInstance().deref(category, bindings));
        return !infoState.cell("domain").query("relevant_category").query(args, bindings);
    }
}
