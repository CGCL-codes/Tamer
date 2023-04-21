package org.nexopenframework.ide.eclipse.jee.generators;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.corext.refactoring.util.RefactoringASTParser;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.nexopenframework.ide.eclipse.commons.generators.Generator;

/**
 * <p> NexOpen Framework</p>
 * 
 * <p>Abstract class for generate snippets in classes</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public abstract class WizardGenerator extends Generator {

    protected WizardGenerator() {
    }

    /**
	 * 
	 * @see Generator#handleGeneration(org.eclipse.jdt.core.IType, org.eclipse.swt.widgets.Shell)
	 */
    protected void handleGeneration(IType type, Shell shell) throws JavaModelException {
        try {
            asserType(type);
        } catch (IllegalStateException e) {
            MessageDialog.openWarning(shell, "Illegal State", e.getMessage());
            return;
        } catch (JavaModelException e) {
            MessageDialog.openError(shell, "Internal error", e.getMessage());
        }
        Wizard wizard = newWizard(type);
        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.create();
        if (dialog.open() == IDialogConstants.OK_ID) {
            try {
                final ICompilationUnit cu = (ICompilationUnit) type.getAncestor(IJavaElement.COMPILATION_UNIT);
                CompilationUnit cunit = new RefactoringASTParser(AST.JLS3).parse(cu, true);
                Document doc = new Document(cu.getSource());
                cunit.recordModifications();
                handleWizardGeneration(wizard, shell, cu, cunit);
                TextEdit edit = cunit.rewrite(doc, null);
                try {
                    edit.apply(doc);
                    String source = doc.get();
                    cu.getBuffer().setContents(source);
                } catch (MalformedTreeException e) {
                    e.printStackTrace();
                    throw e;
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            } catch (IllegalStateException e) {
                MessageDialog.openWarning(shell, "Illegal State", e.getMessage());
            }
        }
    }

    /**
	 * @param type
	 * @throws JavaModelException
	 * @throws IllegalStateException
	 */
    protected void asserType(IType type) throws JavaModelException, IllegalStateException {
    }

    /**
	 * @param type
	 * @return
	 */
    protected abstract Wizard newWizard(IType type);

    /**
	 * @param wizard
	 * @param shell
	 * @param cu
	 * @param cunit
	 * @throws JavaModelException
	 * @throws IllegalStateException
	 */
    protected abstract void handleWizardGeneration(final Wizard wizard, final Shell shell, final ICompilationUnit cu, final CompilationUnit cunit) throws JavaModelException, IllegalStateException;
}
