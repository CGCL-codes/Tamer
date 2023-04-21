package org.xaware.ide.xadev.gui.actions;

import java.util.Iterator;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.xaware.ide.xadev.wizard.BizDocumentWizard;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class is the ActionDelegate responsible for kicking off the
 * BizDocumentWizard when Generate --> XBD File is selected on an XML Schema
 * file.
 * 
 * @author Tim Uttormark
 */
public class GenerateBizDocumentFromXSDActionHandler extends AbstractHandler implements IHandler {

    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(GenerateBizDocumentFromXSDActionHandler.class.getName());

    /**
	 * Kicks off the BizDocumentWizard when Generate --> XBD File is selected
	 * on an XML Schema file.
	 * 
	 * @param action
	 *            the action that was performed (unused)
	 */
    @SuppressWarnings("unchecked")
    public Object execute(ExecutionEvent event) throws ExecutionException {
        final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final ISelection selection = workbenchWindow.getSelectionService().getSelection();
        if (selection != null) {
            Object selectedObject = null;
            if (selection instanceof IStructuredSelection) {
                final IStructuredSelection ss = (IStructuredSelection) selection;
                final Iterator i = ss.iterator();
                if (i.hasNext()) {
                    selectedObject = i.next();
                }
            }
            if (selectedObject instanceof IFile && selection instanceof IStructuredSelection) {
                final IFile file = (IFile) selectedObject;
                final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                try {
                    BizDocumentWizard.showDialog(workbenchWindow.getShell(), file, structuredSelection);
                } catch (final Exception e) {
                    logger.info("Failed to launch BizDocumentWizard: " + e);
                }
            }
        }
        return selection;
    }

    public void selectionChanged(final IAction action, final ISelection selection) {
    }
}
