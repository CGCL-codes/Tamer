package org.rubypeople.rdt.refactoring.action;

import org.rubypeople.rdt.refactoring.core.splitlocal.SplitTempRefactoring;

public class SplitTempAction extends WorkbenchWindowActionDelegate {

    @Override
    public void run() {
        run(SplitTempRefactoring.class, SplitTempRefactoring.NAME);
    }
}
