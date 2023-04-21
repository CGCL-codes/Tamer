package com.migniot.streamy.application.view;

import java.io.File;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import com.migniot.streamy.core.CorePlugin;
import com.migniot.streamy.core.CorePreferenceInitializer;
import com.migniot.streamy.core.DownloadListener;
import com.migniot.streamy.core.MediaReference;

/**
 * The explorer view.
 */
public class ExplorerView extends ViewPart implements DownloadListener, IDoubleClickListener {

    /**
	 * The view id.
	 */
    public static String VIEW_ID = "com.migniot.streamy.application.ExplorerView";

    /**
	 * The viewer
	 */
    private TreeViewer viewer;

    /**
	 * Constructor.
	 */
    public ExplorerView() {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        viewer.setContentProvider(new ExplorerContentProvider());
        TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.NONE);
        column.setLabelProvider(new NameLabelProvider());
        column.getColumn().setText("Name");
        column.getColumn().setWidth(600);
        column = new TreeViewerColumn(viewer, SWT.NONE);
        column.setLabelProvider(new AuthorLabelProvider());
        column.getColumn().setText("Author");
        column.getColumn().setWidth(150);
        column = new TreeViewerColumn(viewer, SWT.NONE);
        column.setLabelProvider(new AlbumLabelProvider());
        column.getColumn().setText("Album");
        column.getColumn().setWidth(150);
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);
        viewer.setInput(getDefaultRoot());
        viewer.addDragSupport(DND.DROP_COPY, new Transfer[] { FileTransfer.getInstance() }, new ExplorerDragSourceListener(viewer));
        viewer.addDoubleClickListener(this);
    }

    /**
	 * Return the default root.
	 *
	 * @return The default root.
	 */
    private File getDefaultRoot() {
        return new File(CorePlugin.getDefault().getPluginPreferences().getString(CorePreferenceInitializer.MEDIA_STORAGE_LIBRARY_FOLDER));
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setFocus() {
        viewer.getTree().setFocus();
    }

    /**
	 * {@inheritDoc}
	 */
    public void notifyEnd(MediaReference reference) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                viewer.refresh();
            }
        });
    }

    /**
	 * {@inheritDoc}
	 */
    public void notifyConversion(MediaReference reference) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void notifyStart(MediaReference reference) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void doubleClick(DoubleClickEvent event) {
        ISelection selection = event.getSelection();
        if (selection instanceof IStructuredSelection) {
            for (Object object : ((IStructuredSelection) selection).toArray()) {
                viewer.expandToLevel(object, AbstractTreeViewer.ALL_LEVELS);
            }
        }
    }
}
