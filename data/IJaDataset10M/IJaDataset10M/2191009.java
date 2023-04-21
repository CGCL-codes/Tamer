package org.xulbooster.eclipse.xb.ui.snippets.utils;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "xul". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */
public class LuxItemWizard extends Wizard implements INewWizard {

    protected ISelection selection;

    private static String res;

    /**
	 * Constructor for XULNewWizard.
	 */
    public LuxItemWizard() {
        super();
        res = "";
    }

    public static String getResult() {
        return res;
    }

    /**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

    protected void addOpenTag(String tagname) {
        res = res + "<" + tagname;
    }

    protected void addAttribute(String name, String value) {
        if (value.length() > 0) res = res + " " + name + "=\"" + value + "\"";
    }

    protected void addAttribute(String name, int value) {
        if (value > 0) res = res + " " + name + "=\"" + value + "\"";
    }

    protected void endTag() {
        res = res + ">";
    }

    protected void closeTag() {
        res = res + "/>";
    }

    protected void addCloseTag(String tagname) {
        res = res + "</" + tagname + ">";
    }

    public boolean performFinish() {
        return false;
    }
}
