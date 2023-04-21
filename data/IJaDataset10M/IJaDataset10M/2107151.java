package net.jonbuck.tassoo.ui.view.dnd;

import net.jonbuck.tassoo.model.Task;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * 
 * 
 * @since 1.0.0
 */
public class TasksViewDragListener implements DragSourceListener {

    private TreeViewer treeViewer;

    /**
	 * 
	 * @param treeViewer
	 */
    public TasksViewDragListener(TreeViewer treeViewer) {
        this.treeViewer = treeViewer;
    }

    /**
	 * 
	 */
    public void dragStart(DragSourceEvent event) {
    }

    /**
	 * 
	 */
    public void dragSetData(DragSourceEvent event) {
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Task task = (Task) selection.getFirstElement();
        event.data = encodeData(task);
    }

    /**
	 * 
	 * @param task
	 * @return
	 */
    private String encodeData(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getContainer().getContainerName());
        sb.append("~");
        sb.append(task.getTaskName());
        return sb.toString();
    }

    /**
	 * 
	 */
    public void dragFinished(DragSourceEvent event) {
    }
}
