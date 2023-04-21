package org.rubypeople.rdt.refactoring.core.inlineclass;

import org.rubypeople.rdt.refactoring.core.RubyRefactoring;
import org.rubypeople.rdt.refactoring.core.TextSelectionProvider;
import org.rubypeople.rdt.refactoring.ui.pages.InlineClassPage;

public class InlineClassRefactoring extends RubyRefactoring {

    public static final String NAME = Messages.InlineClassRefactoring_Name;

    public InlineClassRefactoring(TextSelectionProvider selectionProvider) {
        super(NAME);
        InlineClassConfig config = new InlineClassConfig(getDocumentProvider(), selectionProvider.getCarretPosition());
        InlineClassConditionChecker checker = new InlineClassConditionChecker(config);
        setRefactoringConditionChecker(checker);
        if (checker.shouldPerform()) {
            ClassInliner inliner = new ClassInliner(config);
            setEditProvider(inliner);
            InlineClassPage page = new InlineClassPage(config);
            pages.add(page);
        }
    }
}
