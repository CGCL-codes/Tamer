package net.sf.j2s.ui.property;

import java.io.File;
import java.util.ArrayList;
import net.sf.j2s.ui.classpath.IRuntimeClasspathEntry;
import net.sf.j2s.ui.launching.JavaRuntime;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.debug.core.JDIDebugPlugin;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceSorter;

/**
 * @author zhou renjian
 *
 * 2006-2-1
 */
public class J2SAddJarAction implements SelectionListener {

    J2SConfigPage page;

    private ISelectionStatusValidator validator = new ISelectionStatusValidator() {

        public IStatus validate(Object[] selection) {
            if (selection.length == 0) {
                return new Status(IStatus.ERROR, JDIDebugPlugin.getUniqueIdentifier(), 0, "", null);
            }
            for (int i = 0; i < selection.length; i++) {
                if (!(selection[i] instanceof IFile)) {
                    return new Status(IStatus.ERROR, JDIDebugPlugin.getUniqueIdentifier(), 0, "", null);
                }
            }
            return new Status(IStatus.OK, JDIDebugPlugin.getUniqueIdentifier(), 0, "", null);
        }
    };

    public J2SAddJarAction(J2SConfigPage page) {
        super();
        this.page = page;
    }

    public void widgetSelected(SelectionEvent e) {
        ViewerFilter filter = new ArchiveFilter(new ArrayList());
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        String rootPath = root.getLocation().toOSString();
        String rootTruePath = new File(rootPath).getAbsolutePath();
        String j2sPath = page.j2sFile.getAbsolutePath();
        String prjName = null;
        if (j2sPath.startsWith(rootTruePath)) {
            j2sPath = j2sPath.substring(rootTruePath.length());
            if (j2sPath.startsWith(File.separator)) {
                j2sPath = j2sPath.substring(1);
            }
            prjName = j2sPath.substring(0, j2sPath.indexOf(File.separatorChar));
        }
        final IProject project = (prjName != null) ? root.getProject(prjName) : null;
        ILabelProvider lp = new WorkbenchLabelProvider();
        ITreeContentProvider cp = new WorkbenchContentProvider();
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(e.display.getActiveShell(), lp, cp) {

            protected TreeViewer createTreeViewer(Composite parent) {
                TreeViewer treeViewer = super.createTreeViewer(parent);
                if (project != null) {
                    treeViewer.setSelection(new StructuredSelection(project));
                    treeViewer.expandToLevel(project, 2);
                }
                return treeViewer;
            }
        };
        dialog.setValidator(validator);
        dialog.setTitle("Java2Script Resources Selection");
        dialog.setMessage("Choose Java2Scrip resources (*.js and *.css)");
        dialog.addFilter(filter);
        dialog.setInput(root);
        dialog.setSorter(new ResourceSorter(ResourceSorter.NAME));
        if (dialog.open() == Window.OK) {
            Object[] expandedElements = page.viewer.getExpandedElements();
            Object[] elements = dialog.getResult();
            boolean added = false;
            for (int i = 0; i < elements.length; i++) {
                IResource elem = (IResource) elements[i];
                IRuntimeClasspathEntry entry = JavaRuntime.newArchiveRuntimeClasspathEntry(elem);
                if (entry != null) {
                    page.classpathModel.resources.add(entry);
                    added = true;
                }
            }
            if (added) {
                ScrollBar bar = page.viewer.getTree().getVerticalBar();
                double selection = 0;
                if (bar != null) {
                    selection = (0.0 + bar.getSelection()) / bar.getMaximum();
                }
                page.viewer.refresh();
                page.viewer.setExpandedElements(expandedElements);
                if (bar != null) {
                    bar.setSelection((int) Math.round(selection * bar.getMaximum()));
                }
                page.updateButtonGroup();
                page.fireConfigModified();
            }
        }
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }
}
