package com.tensegrity.palobrowser.editors.subseteditor.flat;

import java.io.UnsupportedEncodingException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import com.tensegrity.palobrowser.editors.subseteditor.SelectionTransfer;
import com.tensegrity.palobrowser.editors.subseteditor.SubsetEditorMessages;

/**
 * <code></code>
 *
 * @author Stepan Rutz
 * @version $ID$
 */
public class SourceListDragListener implements DragSourceListener {

    private final TreeViewer viewer;

    SourceListDragListener(TreeViewer viewer) {
        this.viewer = viewer;
    }

    public void dragSetData(DragSourceEvent event) {
        if (!SelectionTransfer.getInstance().isSupportedType(event.dataType)) return;
        try {
            event.data = getClass().getName().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void dragStart(DragSourceEvent event) {
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        if (selection.isEmpty()) {
            event.doit = false;
            return;
        }
        SelectionTransfer.getInstance().setSelection(selection);
        event.doit = true;
    }

    public void dragFinished(DragSourceEvent event) {
    }
}
