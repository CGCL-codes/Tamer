package org.rubypeople.rdt.refactoring.core.overridemethod;

import org.rubypeople.rdt.refactoring.core.RubyRefactoring;
import org.rubypeople.rdt.refactoring.ui.pages.OverrideMethodSelectionPage;

public class OverrideMethodRefactoring extends RubyRefactoring {

    public static final String NAME = Messages.OverrideMethodRefactoring_Name;

    public OverrideMethodRefactoring() {
        super(NAME);
        MethodsOverrider methodsOverrider = new MethodsOverrider(getDocumentProvider());
        setEditProvider(methodsOverrider);
        OverrideMethodSelectionPage page = new OverrideMethodSelectionPage(methodsOverrider);
        pages.add(page);
    }
}
