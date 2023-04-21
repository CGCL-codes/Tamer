package org.cesar.flip.ui.dialogs;

import java.util.List;
import org.cesar.flip.Facade;
import org.cesar.flip.flipg.model.Aspect;
import org.cesar.flip.ui.Messages;
import org.cesar.flip.ui.providers.AspectsLabelProvider;
import org.cesar.flip.ui.wizards.NewAspectCreationWizard;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * The class that represents an aspect selection dialog.
 * 
 * @author Fernando Calheiros (fernando.calheiros@cesar.org.br)
 * 
 */
public class AspectSelectionDialog extends ElementListSelectionDialog {

    /**
	 * The aspect
	 */
    private Aspect aspect;

    public AspectSelectionDialog(Shell parentShell, List<Aspect> aspects) {
        super(parentShell, new AspectsLabelProvider());
        this.setTitle(Messages.DialogNewAspect_DIALOG_TITLE);
        this.setMessage(Messages.DialogNewAspect_DIALOG_MESSAGE);
        this.setElements(aspects.toArray());
    }

    @Override
    public Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        Button buttonNewAspect = new Button(composite, SWT.NONE);
        buttonNewAspect.setText(Messages.DialogNewAspect_NEW_ASPECT_BUTTON);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.END;
        buttonNewAspect.setLayoutData(gridData);
        buttonNewAspect.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {

            @Override
            public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
                NewAspectCreationWizard wizardNewAspect = new NewAspectCreationWizard();
                WizardDialog newAspectDialog = new WizardDialog(AspectSelectionDialog.this.getShell(), wizardNewAspect);
                newAspectDialog.open();
                if (newAspectDialog.getReturnCode() == Window.OK) {
                    IJavaElement newAspect = wizardNewAspect.getCreatedElement();
                    try {
                        AspectSelectionDialog.this.aspect = AspectSelectionDialog.this.createSelectedAspect(newAspect);
                    } catch (CoreException e1) {
                        ErrorDialog.openError(AspectSelectionDialog.this.getShell(), Messages.GENERAL_ERROR_MESSAGE, e1.getMessage(), e1.getStatus());
                    }
                    AspectSelectionDialog.this.okPressed();
                }
            }
        });
        return composite;
    }

    @Override
    public void okPressed() {
        if (this.aspect == null) {
            this.aspect = (Aspect) this.getSelectedElements()[0];
        }
        this.close();
    }

    /**
	 * Returns the selected aspect.
	 * 
	 * @return the selected aspect.
	 */
    public Aspect getSelectedAspect() {
        return this.aspect;
    }

    /**
	 * Creates the selected aspect
	 * 
	 * @param newAspect
	 * @return the aspect created
	 * @throws CoreException
	 */
    public Aspect createSelectedAspect(IJavaElement newAspect) throws CoreException {
        Aspect aspect = null;
        if (newAspect != null) {
            aspect = (Aspect) Facade.getInstance().createComponent(Aspect.TYPE);
            aspect.updatePath(newAspect.getResource().getProjectRelativePath().toPortableString());
        } else {
            Object[] selectedElements = this.getSelectedElements();
            if (selectedElements[0] != null) {
                aspect = ((Aspect) selectedElements[0]);
            }
        }
        return aspect;
    }
}
