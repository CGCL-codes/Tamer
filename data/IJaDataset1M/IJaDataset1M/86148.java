package org.mitre.dm.qud.conditions;

import org.mitre.midiki.agent.*;
import org.mitre.midiki.state.*;
import org.mitre.midiki.logic.*;
import java.util.*;

public class IsRelevantToTasks extends Condition {

    Object tasks;

    Object answer;

    public IsRelevantToTasks(Object a, Object t) {
        super();
        tasks = t;
        answer = a;
    }

    public boolean test(ImmutableInfoState infoState, Bindings bindings) {
        LinkedList args = new LinkedList();
        args.add(answer);
        args.add(tasks);
        return infoState.cell("domain").query("relevant_to_tasks").query(args, bindings);
    }
}
