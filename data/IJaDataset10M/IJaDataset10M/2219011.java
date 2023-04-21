package com.google.inject.tools.ideplugin.eclipse;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import com.google.inject.tools.ideplugin.GuicePlugin;

/**
 * The action to take when the user selects an object in the tree view (Outline)
 * and selects "Find Bindings" from the right click menu.
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class BindingsObjectAction implements IObjectActionDelegate {

    private IWorkbenchPart part;

    private GuicePlugin guicePlugin;

    public BindingsObjectAction() {
        super();
        guicePlugin = Activator.getDefault().getGuicePlugin();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.part = targetPart;
    }

    /**
   * Eclipse callback to have us run the bindings engine.
   */
    public void run(IAction action) {
        IStructuredSelection selection = (IStructuredSelection) part.getSite().getSelectionProvider().getSelection();
        IJavaElement element = (IJavaElement) selection.getFirstElement();
        EclipseJavaElement javaElement = element == null ? null : new EclipseJavaElement(element);
        if (javaElement != null) {
            guicePlugin.getBindingsEngine(javaElement, new EclipseJavaProject(element.getJavaProject()));
        } else {
            guicePlugin.getMessenger().display(PluginTextValues.SELECTION_NO_BINDINGS);
        }
    }

    /**
   * Eclipse callback that the selected text changed.
   */
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
