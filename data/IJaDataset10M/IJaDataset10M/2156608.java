package net.sourceforge.refactor4pdt.ui.wizards;

import net.sourceforge.refactor4pdt.core.IRenameInfo;
import net.sourceforge.refactor4pdt.core.PhpRefactoringBase;
import net.sourceforge.refactor4pdt.core.renamefield.RenameFieldInfo;
import net.sourceforge.refactor4pdt.ui.core.PhpRefactoringWizard;
import net.sourceforge.refactor4pdt.ui.wizards.pages.RenameInputPage;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public class RenameFieldWizard extends PhpRefactoringWizard {

    public RenameFieldWizard(PhpRefactoringBase refactoring, RenameFieldInfo info, RefactoringStatus status) {
        super(refactoring, info, status);
    }

    @Override
    protected void addUserInputPages() {
        setDefaultPageTitle(getRefactoring().getName());
        addPage(new RenameInputPage(getRefactoring().getName(), (IRenameInfo) info));
    }
}
