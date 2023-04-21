package com.kbframework.workflow.operaction;

import com.kbframework.workflow.Action;
import com.kbframework.workflow.ProcessInvokerContext;

public class TestAction {

    public static final Action testAction = new Action("testAction.execute");

    public void execute(ProcessInvokerContext context) {
        Tester.counts2.put(context.getCurrTransit().getTransitName(), 1);
    }
}
