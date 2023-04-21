package org.rubypeople.rdt.refactoring.action;

import org.rubypeople.rdt.refactoring.core.pullup.PullUpRefactoring;

public class PullUpAction extends WorkbenchWindowActionDelegate {

    public void run() {
        run(PullUpRefactoring.class, PullUpRefactoring.NAME);
    }
}
