package org.rubypeople.rdt.refactoring.action;

import org.rubypeople.rdt.refactoring.core.generateconstructor.GenerateConstructorRefactoring;

public class GenerateConstructorAction extends WorkbenchWindowActionDelegate {

    @Override
    public void run() {
        run(GenerateConstructorRefactoring.class, GenerateConstructorRefactoring.NAME);
    }
}
