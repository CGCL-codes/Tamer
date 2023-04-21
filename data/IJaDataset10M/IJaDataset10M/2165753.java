package org.systemsEngineering.workbench.core.ui.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.ui.*;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener {

    private static WgaTable wgaTable;

    /**
         * Creates a multi-page editor example.
         */
    public MultiPageEditor() {
        super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }

    /**
         * Creates page 0 of the multi-page editor.
         */
    void createPage0() {
        ScrolledComposite scrolledComposite = new ScrolledComposite(getContainer(), SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        Composite composite = new Composite(scrolledComposite, SWT.NONE);
        scrolledComposite.setContent(composite);
        RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        composite.setLayout(rowLayout);
        wgaTable = new WgaTable(composite);
        wgaTable.addWgaTableMouseListener(SWT.MouseDoubleClick);
        wgaTable.addWgaTableMouseListener(SWT.MouseDown);
        int index = addPage(scrolledComposite);
        setPageText(index, "Wirkungsgradanalyse");
    }

    /**
         * Creates the pages of the multi-page editor.
         */
    protected void createPages() {
        createPage0();
    }

    /**
         * The <code>MultiPageEditorPart</code> implementation of this
         * <code>IWorkbenchPart</code> method disposes all nested editors.
         * Subclasses may extend.
         */
    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }

    /**
         * Saves the multi-page editor's document.
         */
    public void doSave(IProgressMonitor monitor) {
        getEditor(0).doSave(monitor);
    }

    /**
         * Saves the multi-page editor's document as another file. Also updates
         * the text for page 0's tab, and updates this multi-page editor's input
         * to correspond to the nested editor's.
         */
    public void doSaveAs() {
        IEditorPart editor = getEditor(0);
        editor.doSaveAs();
        setPageText(0, editor.getTitle());
        setInput(editor.getEditorInput());
    }

    public void gotoMarker(IMarker marker) {
        setActivePage(0);
        IDE.gotoMarker(getEditor(0), marker);
    }

    /**
         * The <code>MultiPageEditorExample</code> implementation of this
         * method checks that the input is an instance of
         * <code>IFileEditorInput</code>.
         */
    public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
        if (!(editorInput instanceof IFileEditorInput)) throw new PartInitException("Invalid Input: Must be IFileEditorInput");
        super.init(site, editorInput);
    }

    public boolean isSaveAsAllowed() {
        return true;
    }

    /**
         * Closes all project files on project close.
         */
    public void resourceChanged(final IResourceChangeEvent event) {
    }
}
