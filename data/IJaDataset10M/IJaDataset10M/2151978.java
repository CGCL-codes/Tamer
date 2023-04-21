package org.jmlspecs.eclipse.ui;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.jmlspecs.eclipse.jmlast.JmlASTCodeWriter;
import org.jmlspecs.eclipse.jmlchecker.JmlChecker;
import org.jmlspecs.eclipse.jmlchecker.JmlSpecifications;
import org.jmlspecs.eclipse.jmlchecker.ProjectInfo;
import org.jmlspecs.eclipse.jmlchecker.TypeInfo;
import org.jmlspecs.eclipse.jmlchecker.Types;
import org.jmlspecs.eclipse.jmlcl.JmlDriver;
import org.jmlspecs.eclipse.jmldom.JmlMethodSpecification;
import org.jmlspecs.eclipse.jmldom.JmlMethodSpecificationCase;
import org.jmlspecs.eclipse.jmldom.JmlModelImportDeclaration;
import org.jmlspecs.eclipse.jmldom.JmlTypeSpecification;
import org.jmlspecs.eclipse.util.Env;
import org.jmlspecs.eclipse.util.Log;

/**
 * This class holds the implementations of the actions in response to
 * menu items in the menubar and toolbar
 */
public class MenuActions implements IWorkbenchWindowActionDelegate {

    /** Caches the value of the window, when informed of it. */
    protected IWorkbenchWindow window;

    /** Caches the value of the shell in which the window exists. */
    protected Shell shell = null;

    /** The current selection. */
    protected ISelection selection;

    public final void selectionChanged(final IAction action, final ISelection selection) {
        this.selection = selection;
    }

    /**
   * We can use this method to dispose of any system
   * resources we previously allocated.
   * @see IWorkbenchWindowActionDelegate#dispose
   */
    public void dispose() {
    }

    /**
   * We will cache window object in order to
   * be able to provide a parent shell for the message dialog.
   * @param window The parent window
   * @see IWorkbenchWindowActionDelegate#init
   */
    public void init(IWorkbenchWindow window) {
        this.window = window;
        this.shell = window.getShell();
    }

    public void run(final IAction action) {
        Log.errorlog("Should never have reached this program location", new RuntimeException());
        showMessage("JML Plugin", "INTERNAL ERROR: Failed to override menuActions.run -- see error log");
    }

    /**
   * This class implements the action for checking
   * files using JML, using a background job
   * 
   * @author David R. Cok
   */
    public static class CheckJML extends MenuActions {

        public final void run(final IAction action) {
            final Map res = sortByProject(getSelectedElements(selection, window, true));
            if (res.size() == 0) {
                showMessage("JML Check", "Nothing (in Java projects) to check");
                return;
            }
            Job j = new Job("JML Manual Build") {

                public IStatus run(IProgressMonitor monitor) {
                    Iterator i = res.keySet().iterator();
                    while (i.hasNext()) {
                        List<IResource> list = (List<IResource>) res.get(i.next());
                        boolean cancelled = JML3Builder.checkJML(list, monitor);
                        if (cancelled) {
                            showMessageInUI(shell, "JML Check", "Build cancelled");
                            return Status.CANCEL_STATUS;
                        }
                    }
                    return Status.OK_STATUS;
                }
            };
            j.setUser(true);
            j.schedule();
        }
    }

    /**
   * This class implements the action that clears
   * JML markers.
   * 
   * @author David R. Cok
   */
    public static class DeleteMarkers extends MenuActions {

        public final void run(final IAction action) {
            int maxdialogs = 5;
            Iterator i = getSelectedElements(selection, window, true).iterator();
            if (!i.hasNext()) {
                showMessage(shell, "JML Plugin", "Nothing selected");
                return;
            }
            while (i.hasNext()) {
                Object o = i.next();
                IResource r = null;
                try {
                    if (o instanceof IResource) {
                        r = (IResource) o;
                        JML3Builder.deleteMarkers(r, true);
                    } else if (o instanceof IJavaElement) {
                        r = ((IJavaElement) o).getCorrespondingResource();
                        if (r != null) JML3Builder.deleteMarkers(r, true);
                    }
                } catch (Exception e) {
                    Log.errorlog("Exception while deleting markers: " + e, e);
                    if (window != null && (maxdialogs--) > 0) {
                        showMessage("JML Plugin Exception", "Exception while deleting markers " + (r != null ? "on " + r.getName() : "") + e.toString());
                    }
                }
            }
            return;
        }
    }

    /**
   * This action enables the JML nature on the selected projects,
   * so that checking happens as part of compilation.
   * 
   * @author David Cok
   *
   */
    public static class EnableJML extends MenuActions {

        public final void run(final IAction action) {
            Map map = sortByProject(getSelectedElements(selection, window, true));
            Iterator i = map.keySet().iterator();
            if (!i.hasNext()) {
                showMessage(shell, "JML Plugin", "No Java projects selected");
                return;
            }
            int maxdialogs = 5;
            while (i.hasNext()) {
                IProject p = null;
                try {
                    p = ((IJavaProject) i.next()).getProject();
                    JML3Nature.enableJMLNature(p);
                    PlatformUI.getWorkbench().getDecoratorManager().update(PopupActions.JML_DECORATOR_ID);
                } catch (Exception e) {
                    if (window != null && (maxdialogs--) > 0) {
                        showMessage("JML Plugin Exception", "Exception while enabling JML " + (p != null ? "on " + p.getName() : "") + e.toString());
                    }
                    Log.errorlog("Failed to enable JML nature" + (p != null ? " on project " + p.getName() : ""), e);
                }
            }
            Log.log("Completed Enable JML operation ");
        }
    }

    /**
   * This action disables the JML nature on the selected projects.
   * 
   * @author David Cok
   *
   */
    public static class DisableJML extends MenuActions {

        public final void run(final IAction action) {
            Map map = sortByProject(getSelectedElements(selection, window, true));
            Iterator i = map.keySet().iterator();
            if (!i.hasNext()) {
                showMessage(shell, "JML Plugin", "No Java projects selected");
                return;
            }
            int maxdialogs = 5;
            while (i.hasNext()) {
                IProject p = null;
                try {
                    p = ((IJavaProject) i.next()).getProject();
                    JML3Nature.disableJMLNature(p);
                    PlatformUI.getWorkbench().getDecoratorManager().update(PopupActions.JML_DECORATOR_ID);
                } catch (Exception e) {
                    if (window != null && (maxdialogs--) > 0) {
                        showMessage("JML Plugin Exception", "Exception while deleting markers " + (p != null ? "on " + p.getName() : "") + e.toString());
                    }
                    Log.errorlog("Failed to disable JML nature" + (p != null ? " on project " + p.getName() : ""), e);
                }
            }
            Log.log("Completed Disable JML operation ");
        }
    }

    /**
   * This action pops up a dialog showing the specs for the selected
   * Java element.
   * 
   * @author David Cok
   *
   */
    public static class ShowSpecs extends MenuActions {

        public final void run(final IAction action) {
            if (selection instanceof IStructuredSelection) {
                Iterator it = ((IStructuredSelection) selection).iterator();
                if (!it.hasNext()) {
                    showMessage(shell, "JML Plugin", "Nothing selected");
                    return;
                }
                while (it.hasNext()) {
                    IStatus r = showSpecs(shell, it.next());
                    if (r != Status.OK_STATUS) {
                        Log.log("Cancelled Show specs operation ");
                        return;
                    }
                }
                Log.log("Completed Show specs operation ");
            }
        }

        /** A static helper method that can be called for PopupActions
     * as well - it puts up an informational dialog with specification
     * information about the object o.  This may spawn a computational
     * task.
     * @param shell the shell responsible for the dialog window
     * @param o the object whose specs are to be shown
     * @return a Status value indicating whether a cancel occurred
     */
        public static IStatus showSpecs(Shell shell, Object o) {
            try {
                ProjectInfo pi = ProjectInfo.getProjectInfo(((IJavaElement) o).getJavaProject());
                if (pi == null) {
                    pi = new ProjectInfo(Activator.options, JML3Builder.preq);
                    pi.setJavaProject(((IJavaElement) o).getJavaProject());
                }
                final ProjectInfo ppi = pi;
                final Shell sh = shell;
                String title, content;
                if (o instanceof IType) {
                    final IType tt = (IType) o;
                    IType t = tt;
                    title = "Specifications of type " + t.getFullyQualifiedName();
                    JmlSpecifications.TypeDeclSpecs s = JmlSpecifications.findTypeSpecs(t);
                    StringBuilder ss = new StringBuilder();
                    if (s == null) {
                        Job j = new Job("JML - getting type specs") {

                            public IStatus run(IProgressMonitor monitor) {
                                try {
                                    (new JmlChecker(ppi)).getSpecs(tt, TypeInfo.State.JML_SIGNATURE_ONLY, monitor);
                                } catch (Exception e) {
                                    String msg = "An exception occurred while computing the specs for type " + tt.getFullyQualifiedName() + ": " + e;
                                    showMessageInUI(sh, "JML Plugin Exception", msg);
                                    Log.errorlog(msg, e);
                                }
                                if (monitor.isCanceled()) return Status.CANCEL_STATUS;
                                return Status.OK_STATUS;
                            }
                        };
                        j.setUser(true);
                        j.schedule();
                        j.join();
                        IStatus result = j.getResult();
                        if (result != Status.OK_STATUS) return result;
                        s = JmlSpecifications.findTypeSpecs(t);
                    }
                    if (s == null) {
                        ss.append("No specs cached or generated");
                    } else {
                        for (JmlTypeSpecification j : s.typeSpecs) {
                            ss.append(JmlASTCodeWriter.generateSnippets(j));
                        }
                        while (true) {
                            t = Types.getSuperClass(t, pi);
                            if (t == null) break;
                            s = JmlSpecifications.findTypeSpecs(t);
                            ss.append("\nSpecifications of super type " + t.getFullyQualifiedName() + "\n");
                            for (JmlTypeSpecification j : s.typeSpecs) {
                                ss.append(JmlASTCodeWriter.generateSnippets(j));
                            }
                        }
                    }
                    content = ss.toString();
                } else if (o instanceof ICompilationUnit) {
                    final ICompilationUnit t = (ICompilationUnit) o;
                    JmlSpecifications.CompUnitSpecs s = JmlSpecifications.findCUSpecs(t);
                    StringBuilder ss = new StringBuilder();
                    if (s == null) {
                        Job j = new Job("JML - getting compilation unit specs") {

                            public IStatus run(IProgressMonitor monitor) {
                                try {
                                    JmlChecker jmlc = new JmlChecker(ppi);
                                    jmlc.getSpecs(t, TypeInfo.State.JML_SIGNATURE_ONLY, monitor);
                                } catch (Exception e) {
                                    String msg = "An exception occurred while computing the specs for compilation unit " + t.getElementName() + ": " + e;
                                    showMessageInUI(sh, "JML Plugin Exception", msg);
                                    Log.errorlog(msg, e);
                                }
                                if (monitor.isCanceled()) return Status.CANCEL_STATUS;
                                return Status.OK_STATUS;
                            }
                        };
                        j.setUser(true);
                        j.schedule();
                        j.join();
                        IStatus res = j.getResult();
                        if (res == Status.CANCEL_STATUS) return res;
                        s = JmlSpecifications.findCUSpecs(t);
                    }
                    if (s == null) {
                        ss.append("No specs cached or generated");
                    } else {
                        for (JmlModelImportDeclaration j : s.modelImports) {
                            ss.append(JmlASTCodeWriter.generateSnippets(j));
                        }
                        ss.append(s.modelTypes.size() + " model types\n");
                        if (s.specssequence == null || s.specssequence.size() == 0) {
                            ss.append("No specification refinement sequence found\n" + "THIS IS A PROBLEM - check that your specs path is correct.\n" + "You may need to include your .java files on your specs path");
                        } else {
                            ss.append("The specification refinement sequence:\n");
                            for (IFile f : s.specssequence) {
                                ss.append(f.getLocation().toOSString() + "\n");
                            }
                        }
                    }
                    content = ss.toString();
                    title = "Specifications of compilation unit " + t.getResource().getLocation().toOSString();
                } else if (o instanceof IMethod) {
                    final IMethod m = (IMethod) o;
                    title = "Specifications of method " + m.getElementName();
                    Log.log("Showing " + title);
                    JmlSpecifications.MethodDeclSpecs s = JmlSpecifications.findMethodSpecs(m);
                    StringBuilder ss = new StringBuilder();
                    if (s == null) {
                        Job j = new Job("JML - getting method specs") {

                            public IStatus run(IProgressMonitor monitor) {
                                try {
                                    (new JmlChecker(ppi)).getSpecs(m.getDeclaringType(), TypeInfo.State.JML_SIGNATURE_ONLY, monitor);
                                } catch (Exception e) {
                                    String msg = "An exception occurred while computing the specs for method " + m.getElementName() + ": " + e;
                                    showMessageInUI(sh, "JML Plugin Exception", msg);
                                    Log.errorlog(msg, e);
                                }
                                if (monitor.isCanceled()) return Status.CANCEL_STATUS;
                                return Status.OK_STATUS;
                            }
                        };
                        j.setUser(true);
                        j.schedule();
                        j.join();
                        IStatus res = j.getResult();
                        if (res == Status.CANCEL_STATUS) return res;
                        s = JmlSpecifications.findMethodSpecs(m);
                    }
                    boolean showParsed = true;
                    if (s == null) {
                        ss.append("No specs cached");
                    } else {
                        if (!showParsed) {
                            for (JmlMethodSpecification ms : s.raw) {
                                ss.append(JmlASTCodeWriter.generateSnippets(ms));
                            }
                        }
                        if (showParsed) {
                            if (s.parsed != null) {
                                for (JmlMethodSpecificationCase ms : s.parsed) {
                                    ss.append(JmlASTCodeWriter.generateSnippets(ms));
                                }
                            }
                        }
                        IMethod mfirst = m;
                        IType st = m.getDeclaringType();
                        while (true) {
                            st = Types.getSuperClass(st, pi);
                            if (st == null) break;
                            IMethod[] meths = st.findMethods(mfirst);
                            if (meths == null || meths.length == 0) continue;
                            if (meths.length > 1) {
                                Log.log("Ambiguous method " + mfirst + " in super type " + st.getElementName());
                                break;
                            }
                            s = JmlSpecifications.findMethodSpecs(meths[0]);
                            ss.append("\nSpecifications from super type " + st.getFullyQualifiedName() + "\n");
                            if (!showParsed) {
                                for (JmlMethodSpecification ms : s.raw) {
                                    ss.append(JmlASTCodeWriter.generateSnippets(ms));
                                }
                            }
                            if (showParsed && s.parsed != null) {
                                for (JmlMethodSpecificationCase ms : s.parsed) {
                                    ss.append(JmlASTCodeWriter.generateSnippets(ms));
                                }
                            }
                        }
                    }
                    content = ss.toString();
                } else if (o instanceof IField) {
                    IField t = (IField) o;
                    title = "Specifications of field " + t.getElementName();
                    content = "              ????\n  ?????";
                } else if (o instanceof IPackageFragment) {
                    String packagename = ((IPackageFragment) o).getElementName();
                    List<IFolder> locations = pi.getLocations(packagename);
                    title = "Locations for package " + packagename;
                    content = "Files for package " + packagename + " are located at\n";
                    for (IFolder f : locations) {
                        content += "    " + f.getLocation().toOSString() + "\n";
                    }
                } else if (o instanceof IJavaElement) {
                    IJavaElement t = (IJavaElement) o;
                    title = "Specifications of Java element " + t.getElementName();
                    content = "Sorry, presentation of the specfications of a " + t.getClass() + " is not implemented";
                } else if (o instanceof IResource) {
                    IMethod t = (IMethod) o;
                    title = "Specifications of method " + t.getElementName();
                    content = "              ????\n  ?????";
                } else {
                    title = "JML Specifications";
                    content = "I did not expect to be called with an object of type " + o.getClass();
                }
                showSpecsDialog(shell, title, content);
            } catch (Exception e) {
                String msg = "Exception while showing specs " + (o != null ? "for a " + o.getClass() : "") + e;
                Log.errorlog(msg, e);
                if (shell != null) {
                    showMessage(shell, "Show Specs exception ", e.toString());
                }
            }
            return Status.OK_STATUS;
        }

        /** A String array used to define the buttons in a show specs dialog */
        private static final String[] okbutton = { "OK" };

        public static void showSpecsDialog(Shell shell, String title, String content) {
            MessageDialog m = new MessageDialog(shell, title, null, content, MessageDialog.NONE, okbutton, 0);
            m.open();
        }
    }

    /**
   * This action edits the specspath and sets up the appropriate
   * structure for the new specs path.  No selection is needed or
   * paid attention to (for now - until the specspath is made 
   * dependent on the project).
   * 
   * @author David Cok
   *
   */
    public static class EditSpecsPath extends MenuActions {

        public final void run(final IAction action) {
            IStatus result = editSpecsPath(shell);
            Log.log((result == Status.OK_STATUS ? "Completed" : "Cancelled") + " Edit specs path operation ");
        }

        /** Internal helper routine to do the work of editing the specs path
     * @param shell the shell to own the windows
     * @return a Status value, e.g. OK_STATUS or CANCEL_STATUS
     */
        private IStatus editSpecsPath(Shell shell) {
            final ProjectInfo pi = new ProjectInfo(Activator.options, JML3Builder.preq);
            specsProjectText = pi.options.specsProjectName;
            EditPath d = new EditPath(shell);
            boolean ok = d.open() == Window.OK;
            if (!ok) return Status.CANCEL_STATUS;
            pi.options.specsProjectName = specsProjectText;
            final Shell sh = shell;
            Job j = new Job("Creating specs project") {

                public IStatus run(IProgressMonitor monitor) {
                    pi.specsproject = pi.createEmptyJavaProject(specsProjectText, true, true);
                    String errors = pi.createSpecspathFolders(listItems);
                    if (errors != null) showMessageInUI(sh, "JML Plugin", errors);
                    return Status.OK_STATUS;
                }
            };
            j.setUser(true);
            j.schedule();
            IStatus res = j.getResult();
            if (res == Status.CANCEL_STATUS) return res;
            return Status.OK_STATUS;
        }

        /** Just used to communicate between editSpecspath() and EditPath.
     * Note that the EditPath dialog gets disposed and not completely
     * usable after the open() call returns.
     */
        String[] listItems = null;

        /** Keeps the previous list of items, so that the user can revert if desired."
     */
        String[] previousListItems = null;

        /** Just used to communicate between editSpecspath() and EditPath.
     * Note that the EditPath dialog gets disposed and not completely
     * usable after the open() call returns.
     */
        private String specsProjectText = null;

        /** This class holds functionality to allow editing the specs path in the UI */
        public class EditPath extends Dialog {

            /** The wdiget that hold sthe list of path items */
            org.eclipse.swt.widgets.List listcontrol;

            /** The widget that holds the name of the specs project. */
            Text specsProjectField;

            /** The text widget for the directory browser */
            Widgets.DirTextField dirTextField;

            /** The constructor, obviously. 
       * @param shell the parent shell used for new windows
       */
            public EditPath(Shell shell) {
                super(shell);
            }

            protected Control createDialogArea(Composite parent) {
                Log.log("Creating dialog area");
                Composite composite = (Composite) super.createDialogArea(parent);
                Composite vv = new Widgets.VComposite(composite);
                Composite hh = new Widgets.HComposite(vv, 2);
                new Label(hh, SWT.CENTER).setText("Specifications project name");
                specsProjectField = new Text(hh, SWT.SINGLE);
                specsProjectField.setText(specsProjectText + "             ");
                specsProjectField.setSize(specsProjectField.getSize().x * 5, specsProjectField.getLineHeight());
                dirTextField = new Widgets.DirTextField(vv, "Directory or jar file to add", "", "A widget that browses for directories to be added to the specs path", 50);
                final Widgets.DirTextField f = dirTextField;
                Composite w = new Widgets.HComposite(vv, 2);
                listcontrol = new org.eclipse.swt.widgets.List(w, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
                final org.eclipse.swt.widgets.List list = listcontrol;
                listcontrol.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(SelectionEvent e) {
                        int i = list.getSelectionIndex();
                        Log.log("Statemask " + e.stateMask + " " + i + " " + list.getItem(i));
                        if (i < 0) f.setText(list.getItem(i));
                        list.select(i);
                        list.setFocus();
                    }

                    ;

                    public void widgetDefaultSelected(SelectionEvent e) {
                        Log.log("Default selected " + e.stateMask);
                    }
                });
                Composite v = new Widgets.VComposite(w);
                Button b = new Button(v, SWT.PUSH | SWT.CENTER);
                b.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(SelectionEvent e) {
                        int i = list.getSelectionIndex();
                        if (i == -1) i = list.getItemCount() - 1;
                        list.add(f.value(), i);
                        list.select(i);
                        list.setFocus();
                    }

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });
                b.setText("Add");
                b = new Button(v, SWT.PUSH | SWT.CENTER);
                b.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(SelectionEvent e) {
                        int i = list.getSelectionIndex();
                        if (i == -1) i = list.getItemCount() - 1;
                        try {
                            String s = JmlDriver.internalspecs();
                            list.add(s, i);
                            list.select(i);
                            list.setFocus();
                        } catch (IOException ee) {
                            showMessage(shell, "JML UI Plugin Exception", "Failed to find the internal specs library - see the error log");
                            Log.errorlog("Failed to find the internal specs library", ee);
                        }
                    }

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });
                b.setText("Add Internal Specs");
                b = new Button(v, SWT.PUSH | SWT.CENTER);
                b.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(SelectionEvent e) {
                        int i = list.getSelectionIndex();
                        if (i >= 0 && i < list.getItemCount() - 1) {
                            list.remove(i);
                            list.add(f.value(), i);
                            list.select(i);
                            list.setFocus();
                        }
                    }

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });
                b.setText("Replace");
                b = new Button(v, SWT.PUSH | SWT.CENTER);
                b.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(SelectionEvent e) {
                        int i = list.getSelectionIndex();
                        if (i >= 0 && i < list.getItemCount() - 1) {
                            list.remove(i);
                            list.setFocus();
                        }
                    }

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });
                b.setText("Remove");
                b = new Button(v, SWT.PUSH | SWT.CENTER);
                b.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(SelectionEvent e) {
                        int i = list.getSelectionIndex();
                        if (i > 0 && i < list.getItemCount() - 1) {
                            String s = list.getItem(i);
                            list.remove(i);
                            list.add(s, i - 1);
                            list.select(i - 1);
                            list.setFocus();
                        }
                    }

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });
                b.setText("Up");
                b = new Button(v, SWT.PUSH | SWT.CENTER);
                b.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(SelectionEvent e) {
                        int i = list.getSelectionIndex();
                        if (i < list.getItemCount() - 2) {
                            String s = list.getItem(i);
                            list.remove(i);
                            list.add(s, i + 1);
                            list.select(i + 1);
                            list.setFocus();
                        }
                    }

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });
                b.setText("Down");
                b = new Button(v, SWT.PUSH | SWT.CENTER);
                b.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(SelectionEvent e) {
                        if (previousListItems != null) {
                            list.setItems(previousListItems);
                        }
                        list.setFocus();
                    }

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });
                b.setText("Revert");
                IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(specsProjectText);
                List<String> listloc = null;
                if (p != null && p.exists()) {
                    IJavaProject jp = JavaCore.create(p);
                    if (jp != null && jp.exists()) {
                        IFolder ff = p.getFolder(Env.specsContainerName);
                        int i = 0;
                        listloc = new LinkedList<String>();
                        if (ff != null && ff.exists()) {
                            while (true) {
                                String s = Env.specsFolderRoot + (++i);
                                IFolder fs = ff.getFolder(s);
                                if (fs == null || !fs.exists()) break;
                                String loc = fs.getRawLocation().toOSString();
                                listloc.add(loc);
                            }
                            listloc.add("");
                        }
                    }
                }
                if (listloc == null) {
                    try {
                        String specs = JmlDriver.internalspecs();
                        listItems = new String[] { specs, "" };
                    } catch (IOException e) {
                        Log.errorlog("Failed to find the internal specs library", e);
                        showMessage(shell, "JML Plugin Exception", "Failed to find the internal specs library (see error log): " + e);
                        return composite;
                    }
                } else {
                    listItems = listloc.toArray(new String[listloc.size()]);
                }
                list.setItems(listItems);
                list.setSize(list.getSize().y, list.getItemHeight() * 10);
                Log.log((listloc.size() - 1) + " specs path items detected in the existing specs project named " + specsProjectText);
                return composite;
            }

            protected void configureShell(Shell newShell) {
                super.configureShell(newShell);
                newShell.setText("Specs path editor");
            }

            protected void okPressed() {
                previousListItems = listItems;
                listItems = listcontrol.getItems();
                specsProjectText = specsProjectField.getText().trim();
                super.okPressed();
            }
        }
    }

    public static void showMessageInUI(Shell sh, final String title, final String msg) {
        final Shell shell = sh;
        Display d = shell == null ? Display.getDefault() : shell.getDisplay();
        d.asyncExec(new Runnable() {

            public void run() {
                MessageDialog.openInformation(shell, title, msg);
            }
        });
    }

    public static void showMessage(Shell shell, String title, String msg) {
        MessageDialog.openInformation(shell, title, msg);
    }

    /**
   * Shows a message to the user; call this only for situations 
   * in which we are already running in the UI thread.
   * @param title the title for the dialog box
   * @param msg  The message to show
   */
    public void showMessage(String title, String msg) {
        showMessage(window.getShell(), title, msg);
    }

    /**
   * This method interprets the selection returning a List
   * of objects that the plugin
   * knows how to handle.
   * 
   * @param selection  The selection to inspect
   * @param window  The window in which a selected editor exists
   * @param requestResource if true, then only IResource objects will be added to the output list
   * @return A List of IResource or IJavaElement
   */
    public static List<Object> getSelectedElements(ISelection selection, IWorkbenchWindow window, boolean requestResource) {
        List<Object> list = new LinkedList<Object>();
        if (!selection.isEmpty()) {
            if (selection instanceof IStructuredSelection) {
                IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                for (Iterator iter = structuredSelection.iterator(); iter.hasNext(); ) {
                    Object element = iter.next();
                    if (!requestResource && element instanceof IJavaElement) {
                        list.add(element);
                    } else if (element instanceof IResource) {
                        list.add(element);
                    } else if (element instanceof IWorkingSet) {
                        for (IAdaptable a : ((IWorkingSet) element).getElements()) {
                            IResource r = (IResource) a.getAdapter(IResource.class);
                            if (r != null) list.add(r);
                        }
                        continue;
                    } else if (element instanceof IJavaElement) {
                        if (!requestResource) {
                            list.add(element);
                            Log.log("Selected Java Element " + ((IJavaElement) element).getElementName());
                        } else {
                            try {
                                list.add(((IJavaElement) element).getCorrespondingResource());
                            } catch (JavaModelException e) {
                                Log.errorlog("Exception when finding selected targets: " + e, e);
                                showMessage(window.getShell(), "getCorrespondingResource", "Exception occurred when finding selected targets: " + e);
                            }
                        }
                    }
                }
            }
        }
        if (list.size() == 0) {
            try {
                IEditorPart p = window.getActivePage().getActiveEditor();
                IEditorInput e = p == null ? null : p.getEditorInput();
                Object o = e == null ? null : e.getAdapter(IFile.class);
                if (o != null) {
                    list.add(o);
                }
            } catch (Exception ee) {
                Log.errorlog("Exception when finding selected targets: " + ee, ee);
                showMessage(window.getShell(), "JML Plugin Exception", "Exception occurred when finding selected targets: " + ee);
            }
        }
        return list;
    }

    public static Map<IJavaProject, List> sortByProject(Collection elements) {
        Map<IJavaProject, List> map = new HashMap<IJavaProject, List>();
        Iterator i = elements.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            IJavaProject jp;
            if (o instanceof IResource) {
                jp = JavaCore.create(((IResource) o).getProject());
            } else if (o instanceof IJavaElement) {
                jp = ((IJavaElement) o).getJavaProject();
            } else {
                Log.errorlog("INTERNAL ERROR: Unexpected content for a selection List - " + o.getClass(), null);
                continue;
            }
            if (jp != null && jp.exists()) addToMap(map, jp, o);
        }
        return map;
    }

    /**
   * If key is not a key in the map, it is added, with an empty
   * Collection for its value; then the given object is added
   * to the Collection for that key.
   * @param map A map of key values to Collections
   * @param key A key value to add to the map, if it is not
   *      already present
   * @param object An item to add to the Collection for the given key
   */
    private static void addToMap(Map<IJavaProject, List> map, IJavaProject key, Object object) {
        List list = map.get(key);
        if (list == null) map.put(key, list = new LinkedList());
        list.add(object);
    }
}
