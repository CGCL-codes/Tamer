package monet.editors.outline;

import monet.editors.xml.XMLElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class EditorContentOutlinePage extends ContentOutlinePage {

    private ITextEditor editor;

    private IEditorInput input;

    private OutlineContentProvider outlineContentProvider;

    private OutlineLabelProvider outlineLabelProvider;

    public EditorContentOutlinePage(ITextEditor editor) {
        super();
        this.editor = editor;
    }

    public void createControl(Composite parent) {
        super.createControl(parent);
        TreeViewer viewer = getTreeViewer();
        outlineContentProvider = new OutlineContentProvider(editor.getDocumentProvider());
        viewer.setContentProvider(outlineContentProvider);
        outlineLabelProvider = new OutlineLabelProvider();
        viewer.setLabelProvider(outlineLabelProvider);
        viewer.addSelectionChangedListener(this);
        if (input != null) viewer.setInput(input);
    }

    /**
	 * Sets the input of the outline page
	 */
    public void setInput(Object input) {
        this.input = (IEditorInput) input;
        update();
    }

    public void selectionChanged(SelectionChangedEvent event) {
        super.selectionChanged(event);
        ISelection selection = event.getSelection();
        if (selection.isEmpty()) editor.resetHighlightRange(); else {
            XMLElement element = (XMLElement) ((IStructuredSelection) selection).getFirstElement();
            int start = element.getPosition().getOffset();
            int length = element.getPosition().getLength();
            try {
                editor.setHighlightRange(start, length, true);
            } catch (IllegalArgumentException x) {
                editor.resetHighlightRange();
            }
        }
    }

    /**
	 * The editor is saved, so we should refresh representation
	 * 
	 * @param tableNamePositions
	 */
    public void update() {
        TreeViewer viewer = getTreeViewer();
        if (viewer != null) {
            Control control = viewer.getControl();
            if (control != null && !control.isDisposed()) {
                control.setRedraw(false);
                viewer.setInput(input);
                viewer.expandAll();
                control.setRedraw(true);
            }
        }
    }
}
