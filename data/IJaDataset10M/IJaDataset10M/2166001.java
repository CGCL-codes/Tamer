package org.rubypeople.rdt.refactoring.core.splitlocal;

import org.rubypeople.rdt.refactoring.core.RubyRefactoring;
import org.rubypeople.rdt.refactoring.core.TextSelectionProvider;
import org.rubypeople.rdt.refactoring.ui.pages.SplitLocalPage;

public class SplitTempRefactoring extends RubyRefactoring {

    public static final String NAME = Messages.SplitTempRefactoring_Name;

    public SplitTempRefactoring(TextSelectionProvider selectionProvider) {
        super(NAME);
        SplitLocalConfig config = new SplitLocalConfig(getDocumentProvider(), selectionProvider.getCarretPosition());
        SplitLocalConditionChecker checker = new SplitLocalConditionChecker(config);
        setRefactoringConditionChecker(checker);
        if (checker.shouldPerform()) {
            SplitTempEditProvider splitTempEditProvider = new SplitTempEditProvider(config);
            setEditProvider(splitTempEditProvider);
            pages.add(new SplitLocalPage(splitTempEditProvider.getLocalUsages(), config.getDocumentProvider().getActiveFileContent(), splitTempEditProvider));
        }
    }
}
