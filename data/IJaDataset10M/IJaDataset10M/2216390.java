package net.sf.refactorit.transformations;

import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.source.edit.EditorManager;

/**
 * Public interface for Transformations. Transformations work with
 * ClassModel and delegate source editing further, to the editors.
 *
 * @author Jevgeni Holodkov
 * @author Anton Safonov
 */
public interface Transformation {

    public RefactoringStatus apply(EditorManager e);
}
