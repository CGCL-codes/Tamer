package com.google.gwt.eclipse.core.refactoring;

import org.eclipse.core.resources.IFile;
import org.eclipse.ltk.core.refactoring.TextFileChange;

/**
 * Creates type reference changes for compilation units containing refs in JSNI.
 */
class DefaultChangeFactory implements IRefactoringChangeFactory {

    public TextFileChange createChange(IFile file) {
        return new TextFileChange(file.getName(), file);
    }
}
