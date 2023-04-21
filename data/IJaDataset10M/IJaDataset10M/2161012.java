package de.guhsoft.jinto.core.search.reference;

import java.util.HashMap;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import de.guhsoft.jinto.core.JIntoCorePlugin;
import de.guhsoft.jinto.core.search.SearchUtil;

/**
 * @author mseele
 * 
 * toDo To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReferenceSearchResultPage extends AbstractTextSearchViewPage {

    public ReferenceSearchResultPage() {
        super(FLAG_LAYOUT_TREE);
    }

    /**
	 * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#elementsChanged(java.lang.Object[])
	 */
    protected void elementsChanged(Object[] objects) {
        if (objects.length > 0) {
            getViewer().refresh();
        }
    }

    /**
	 * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#clear()
	 */
    protected void clear() {
        getViewer().refresh();
    }

    /**
	 * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#configureTreeViewer(org.eclipse.jface.viewers.TreeViewer)
	 */
    protected void configureTreeViewer(final TreeViewer viewer) {
        viewer.setContentProvider(new ITreeContentProvider() {

            public Object[] getChildren(Object parentElement) {
                Object[] elements = ((AbstractTextSearchResult) viewer.getInput()).getElements();
                IJavaElement[] javaElements = new IJavaElement[elements.length];
                System.arraycopy(elements, 0, javaElements, 0, elements.length);
                return SearchUtil.getChildrenWithSkippingCompilationUnitsAndNotNeededElements((IJavaElement) parentElement, javaElements);
            }

            public Object getParent(Object element) {
                return null;
            }

            public boolean hasChildren(Object element) {
                return (getChildren(element).length > 0);
            }

            public Object[] getElements(Object inputElement) {
                AbstractTextSearchResult searchResult = (AbstractTextSearchResult) inputElement;
                Object[] elements = searchResult.getElements();
                if (elements.length > 0) {
                    return getChildren(((IJavaElement) elements[0]).getJavaModel());
                }
                return new Object[0];
            }

            public void dispose() {
            }

            public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            }
        });
        viewer.setLabelProvider(new JavaElementLabelProvider());
    }

    /**
	 * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#configureTableViewer(org.eclipse.jface.viewers.TableViewer)
	 */
    protected void configureTableViewer(TableViewer viewer) {
    }

    /**
	 * @see org.eclipse.search.ui.text.AbstractTextSearchViewPage#showMatch(org.eclipse.search.ui.text.Match,
	 *      int, int, boolean)
	 */
    protected void showMatch(Match match, int currentOffset, int currentLength, boolean activate) {
        int offset = match.getOffset();
        int length = match.getLength();
        try {
            Object element = match.getElement();
            IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            IResource resource = ((IJavaElement) element).getResource();
            IFile file = null;
            if (resource.getType() == IResource.FILE) {
                file = (IFile) resource;
            }
            if (file != null) {
                IEditorPart editor = IDE.openEditor(workbenchPage, file, activate);
                if (editor instanceof ITextEditor) {
                    ITextEditor textEditor = (ITextEditor) editor;
                    textEditor.selectAndReveal(offset, length);
                } else if (editor != null) {
                    if (element instanceof IFile) {
                        showWithMarker(editor, file, offset, length);
                    }
                }
            } else {
                String pluginID = JIntoCorePlugin.getID();
                String message = "Resource " + resource + " is not a type of IFile.";
                IStatus status = new Status(IStatus.ERROR, pluginID, IStatus.ERROR, message, new Throwable());
                JIntoCorePlugin.getDefault().getLog().log(status);
            }
        } catch (PartInitException e) {
            String pluginID = JIntoCorePlugin.getID();
            IStatus status = new Status(IStatus.ERROR, pluginID, IStatus.ERROR, e.getMessage(), e);
            JIntoCorePlugin.getDefault().getLog().log(status);
        }
    }

    private void showWithMarker(IEditorPart editor, IFile file, int offset, int length) throws PartInitException {
        try {
            IMarker marker = file.createMarker(NewSearchUI.SEARCH_MARKER);
            HashMap<String, Integer> attributes = new HashMap<String, Integer>(4);
            attributes.put(IMarker.CHAR_START, new Integer(offset));
            attributes.put(IMarker.CHAR_END, new Integer(offset + length));
            marker.setAttributes(attributes);
            IDE.gotoMarker(editor, marker);
            marker.delete();
        } catch (CoreException e) {
            throw new PartInitException("Could not create marker", e);
        }
    }
}
