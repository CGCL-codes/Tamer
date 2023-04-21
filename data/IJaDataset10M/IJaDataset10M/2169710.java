package net.sf.refactorit.ui.module.minaccess;

import net.sf.refactorit.ui.module.AbstractRefactorItModule;
import net.sf.refactorit.ui.module.ModuleManager;
import net.sf.refactorit.ui.module.RefactorItAction;

/**
 * @author vadim
 */
public class MinimizeAccessModule extends AbstractRefactorItModule {

    private static final RefactorItAction[] action = { new MinimizeAccessAction() };

    static {
        ModuleManager.registerModule(new MinimizeAccessModule());
    }

    private MinimizeAccessModule() {
    }

    public RefactorItAction[] getActions() {
        return action;
    }

    public String getName() {
        return "Minimize Access Rights";
    }
}
