package net.sourceforge.coffea.editors;

import java.lang.reflect.InvocationTargetException;
import net.sourceforge.coffea.uml2.Resources;
import net.sourceforge.coffea.uml2.model.IModelService;
import net.sourceforge.coffea.uml2.model.creation.ModelServiceBuilder;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Receives reverse engineering actions on  
 * {@link org.eclipse.jdt.core.IJavaElement <em>Java</em> elements}, common 
 * resources for actions and handlers
 * @see org.eclipse.jdt.core.IJavaElement
 */
public class JavaElementsReverseReceiver implements IRunnableWithProgress {

    /** Last source workbench window */
    protected IWorkbenchWindow lastSourceWorkbenchWindow;

    /** Last source view identifier in the last source workbench window */
    protected String lastSourceViewId;

    /** Java element object of the action */
    protected IJavaElement element;

    /** Produced model */
    protected IModelService model;

    /**
	 * Returns Last source workbench window
	 * @return Last source workbench window
	 */
    public IWorkbenchWindow getLastSourceWorkbenchWindow() {
        return lastSourceWorkbenchWindow;
    }

    /**
	 * Returns last source view identifier in the last source workbench window
	 * @return Last source view identifier in the last source workbench window
	 */
    public String getLastSourceViewId() {
        return lastSourceViewId;
    }

    /** 
	 * Simple reverse of a <em>Java</em> element to an UML model
	 * @param edition
	 * Boolean value indicating if the source should be edited through the 
	 * UML model editor
	 * @param el
	 * <em>Java</em> element to reverse
	 * @param sourceWorkbenchWindow
	 * Source workbench window from which the operation is triggered
	 * @param sourceViewId
	 * Source view identifier in the source workbench window
	 * @return Produced UML model
	 */
    public IModelService reverse(IJavaElement el, IWorkbenchWindow sourceWorkbenchWindow, String sourceViewId) {
        lastSourceWorkbenchWindow = sourceWorkbenchWindow;
        lastSourceViewId = sourceViewId;
        element = el;
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        try {
            dialog.run(false, true, this);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return model;
    }

    protected IJavaElement getSelectedJavaElement(IWorkbenchWindow workbenchWindow) throws ExecutionException {
        String sourceViewId = fetchSourceViewId(workbenchWindow);
        ITreeSelection treeSel = fetchTreeSelection(workbenchWindow, sourceViewId);
        if (treeSel != null) {
            IJavaElement el = selectedJavaElement(treeSel);
            return el;
        }
        return null;
    }

    /** 
	 * Simple reverse of the current selection to an UML model
	 * @param edition
	 * Boolean value indicating if the source should be edited through the 
	 * UML model editor
	 * @param workbenchWindow
	 * Workbench window in which the current selection must be reversed
	 * @param sourceViewId
	 * Source view identifier in the source workbench window
	 * @return Operation result
	 * @throws ExecutionException
	 */
    public Object reverseFromJavaElements(IWorkbenchWindow workbenchWindow) throws ExecutionException {
        String sourceViewId = fetchSourceViewId(workbenchWindow);
        IJavaElement el = getSelectedJavaElement(workbenchWindow);
        if (el != null) {
            return reverse(el, workbenchWindow, sourceViewId);
        }
        workbenchWindow = null;
        return null;
    }

    /** 
	 * Simple reverse of the current selection to an UML model
	 * @param edition
	 * Boolean value indicating if the source should be edited through the 
	 * UML model editor
	 * @param workbenchWindow
	 * Workbench window in which the current selection must be reversed
	 * @param sourceViewId
	 * Source view identifier in the source workbench window
	 * @return Operation result
	 * @throws ExecutionException
	 */
    public Object reverseFromSelectedJavaElement(IWorkbenchWindow workbenchWindow, String sourceViewId) throws ExecutionException {
        ITreeSelection treeSel = fetchTreeSelection(workbenchWindow, sourceViewId);
        if (treeSel != null) {
            IJavaElement el = selectedJavaElement(treeSel);
            return reverse(el, workbenchWindow, sourceViewId);
        }
        workbenchWindow = null;
        return null;
    }

    protected String fetchSourceViewId(IWorkbenchWindow workbenchWindow) {
        String sourceViewId = null;
        if (workbenchWindow != null) {
            IWorkbenchPage[] pages = workbenchWindow.getPages();
            if (pages != null) {
                IWorkbenchPage page = null;
                for (int i = 0; i < pages.length; i++) {
                    page = pages[i];
                    IWorkbenchPartReference activePart = page.getActivePartReference();
                    if (activePart instanceof IViewReference) {
                        IViewReference viewPart = (IViewReference) activePart;
                        sourceViewId = viewPart.getId();
                    }
                }
            }
        }
        return sourceViewId;
    }

    /**
	 * Fetches the current tree selection the source workbench window
	 * @param workbenchWindow
	 * Source workbench window
	 * @param sourceViewId
	 * Source view identifier in the source workbench window
	 * @return Current tree selection
	 * 
	 * @throws ExecutionException
	 */
    protected ITreeSelection fetchTreeSelection(IWorkbenchWindow workbenchWindow, String sourceViewId) throws ExecutionException {
        ISelection selection = null;
        ITreeSelection treeSel = null;
        if ((workbenchWindow != null) && (sourceViewId != null)) {
            IWorkbenchPage[] pages = workbenchWindow.getPages();
            if (pages != null) {
                IWorkbenchPage page = null;
                for (int i = 0; i < pages.length; i++) {
                    page = pages[i];
                    if (page != null) {
                        selection = page.getSelection(sourceViewId);
                    }
                }
                if (selection != null) {
                    if (selection instanceof ITreeSelection) {
                        treeSel = (ITreeSelection) selection;
                    }
                }
            }
        }
        return treeSel;
    }

    /**
	 * Returns the selected <em>Java</em> element (only if it is in first 
	 * position in the selection)
	 * @param treeSel
	 * Selection
	 * @return Selected <em>Java</em> element
	 */
    protected IJavaElement selectedJavaElement(ITreeSelection treeSel) {
        IJavaElement el = null;
        if (treeSel != null) {
            Object first = treeSel.getFirstElement();
            if (first instanceof IJavaElement) {
                el = (IJavaElement) first;
            }
        }
        return el;
    }

    /** Simple reverse action construction */
    public JavaElementsReverseReceiver() {
    }

    /** 
	 * Simple reverse of the current selection to an UML model
	 * @param edition
	 * Boolean value indicating if the source should be edited through the 
	 * UML model editor
	 * @param w
	 * Workbench window in which the reverse is done
	 * @return Result of the operation
	 * @throws ExecutionException
	 */
    protected Object reverse(IWorkbenchWindow w) throws ExecutionException {
        lastSourceWorkbenchWindow = w;
        String sourceViewId = fetchSourceViewId(w);
        lastSourceViewId = sourceViewId;
        ITreeSelection treeSel = fetchTreeSelection(w, sourceViewId);
        if (treeSel != null) {
            IJavaElement el = selectedJavaElement(treeSel);
            return reverse(el, w, sourceViewId);
        }
        w = null;
        return null;
    }

    protected IProject selectedProject(ITreeSelection treeSel) {
        Object first = treeSel.getFirstElement();
        IProject proj = null;
        if (first instanceof IProject) {
            proj = (IProject) first;
        } else if (first instanceof IProjectNature) {
            proj = ((IProjectNature) first).getProject();
        }
        return proj;
    }

    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        monitor.beginTask(Resources.getMessage("labels.processingSelection"), 10);
        if (element instanceof IPackageFragmentRoot) {
            IPackageFragmentRoot r = (IPackageFragmentRoot) element;
            try {
                if (r.getKind() == IPackageFragmentRoot.K_SOURCE) {
                    element = r.getPackageFragment(new String());
                }
            } catch (JavaModelException e) {
                e.printStackTrace();
            }
        }
        while ((element != null) && (!(element instanceof IPackageFragment))) {
            element = element.getParent();
        }
        if (element != null) {
            ModelServiceBuilder builder = new ModelServiceBuilder(lastSourceViewId);
            model = builder.buildModelService(element, new SubProgressMonitor(monitor, 7));
            IPath res = element.getResource().getLocation();
            String path = res.toOSString();
            builder.save(path, builder.getCoffeeName(), new SubProgressMonitor(monitor, 3));
            lastSourceWorkbenchWindow = null;
        }
    }
}
