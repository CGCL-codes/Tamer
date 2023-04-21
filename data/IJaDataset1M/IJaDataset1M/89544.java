package org.rubypeople.rdt.refactoring.action;

import org.rubypeople.rdt.refactoring.core.inlinelocal.InlineLocalRefactoring;

public class InlineTempAction extends WorkbenchWindowActionDelegate {

    @Override
    public void run() {
        run(InlineLocalRefactoring.class, InlineLocalRefactoring.NAME);
    }
}
