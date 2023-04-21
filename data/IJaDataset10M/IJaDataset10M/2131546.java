package ch.sahits.codegen.wizards;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;

/**
 * Delegate factory for the artifact generator
 * @author Andi Hotz
 * @since 1.2.0
 */
public class ArtefacteGeneratorDelegateFactory {

    /**
	 * Factory method for the delegates. Retrieve the appropriate Delegate object based upon the
	 * {@link ECodeGenerationWizard}
	 * @param workbench current workbench
	 * @param shell referenced shell
	 * @param wizard flag indicating which instance of {@link ICodeGeneratorDelegate} to use
	 * @return Instance of {@link ICodeGeneratorDelegate}
	 */
    public ICodeGeneratorDelegate getCodeGeneratorDelegate(IWorkbench workbench, Shell shell, ECodeGenerationWizard wizard) {
        return new MinimalArtefactDelegate(workbench, shell);
    }
}
