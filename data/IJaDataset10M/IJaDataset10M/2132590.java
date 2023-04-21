package com.lolcode.builder.launch.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.DrillDownComposite;

/**
 * This is a GUI widget that allows selecting an LOLCode project.
 * 
 * @author J. Suereth
 *
 */
public class LOLCodeProjectSelectionGroup extends Composite {

    private Listener listener;

    private boolean showClosedProjects = true;

    private IProject selectedProject;

    public static final String DEFAULT_MSG_SELECT_ONLY = "Select an LOLCode Project";

    private static final int SIZING_SELECTION_PANE_WIDTH = 320;

    private static final int SIZING_SELECTION_PANE_HEIGHT = 300;

    /**
	 * Viewer of LOLCode projects
	 */
    TreeViewer treeViewer;

    /**
	 * Creates a new instance of the widget.
	 * 
	 * @param parent
	 *            The parent widget of the group.
	 * @param listener
	 *            A listener to forward events to. Can be null if no listener is
	 *            required.
	 */
    public LOLCodeProjectSelectionGroup(Composite parent, Listener listener) {
        this(parent, listener, null);
    }

    /**
	 * Creates a new instance of the widget.
	 * 
	 * @param parent
	 *            The parent widget of the group.
	 * @param listener
	 *            A listener to forward events to. Can be null if no listener is
	 *            required.
	 * @param message
	 *            The text to present to the user.
	 */
    public LOLCodeProjectSelectionGroup(Composite parent, Listener listener, String message) {
        this(parent, listener, message, true);
    }

    /**
	 * Creates a new instance of the widget.
	 * 
	 * @param parent
	 *            The parent widget of the group.
	 * @param listener
	 *            A listener to forward events to. Can be null if no listener is
	 *            required.
	 * @param message
	 *            The text to present to the user.
	 * @param showClosedProjects
	 *            Whether or not to show closed projects.
	 */
    public LOLCodeProjectSelectionGroup(Composite parent, Listener listener, String message, boolean showClosedProjects) {
        this(parent, listener, message, showClosedProjects, SIZING_SELECTION_PANE_HEIGHT, SIZING_SELECTION_PANE_WIDTH);
    }

    /**
	 * Creates a new instance of the widget.
	 * 
	 * @param parent
	 *            The parent widget of the group.
	 * @param listener
	 *            A listener to forward events to. Can be null if no listener is
	 *            required.
	 * @param message
	 *            The text to present to the user.
	 * @param showClosedProjects
	 *            Whether or not to show closed projects.
	 * @param heightHint
	 *            height hint for the drill down composite
	 * @param widthHint
	 *            width hint for the drill down composite
	 */
    public LOLCodeProjectSelectionGroup(Composite parent, Listener listener, String message, boolean showClosedProjects, int heightHint, int widthHint) {
        super(parent, SWT.NONE);
        this.listener = listener;
        this.showClosedProjects = showClosedProjects;
        if (message != null) {
            createContents(message, heightHint, widthHint);
        } else {
            createContents(DEFAULT_MSG_SELECT_ONLY, heightHint, widthHint);
        }
    }

    /**
	 * Creates the contents of the composite.
	 * 
	 * @param message
	 * @param heightHint
	 * @param widthHint
	 */
    public void createContents(String message, int heightHint, int widthHint) {
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Label label = new Label(this, SWT.WRAP);
        label.setText(message);
        label.setFont(this.getFont());
        new Label(this, SWT.NONE);
        createTreeViewer(heightHint);
        Dialog.applyDialogFont(this);
    }

    /**
	 * Returns a new drill down viewer for this dialog.
	 * 
	 * @param heightHint
	 *            height hint for the drill down composite
	 */
    protected void createTreeViewer(int heightHint) {
        DrillDownComposite drillDown = new DrillDownComposite(this, SWT.BORDER);
        GridData spec = new GridData(SWT.FILL, SWT.FILL, true, true);
        spec.widthHint = SIZING_SELECTION_PANE_WIDTH;
        spec.heightHint = heightHint;
        drillDown.setLayoutData(spec);
        treeViewer = new TreeViewer(drillDown, SWT.NONE);
        drillDown.setChildTree(treeViewer);
        LOLCodeProjectContentProvider cp = new LOLCodeProjectContentProvider();
        cp.showClosedProjects(showClosedProjects);
        treeViewer.setContentProvider(cp);
        treeViewer.setLabelProvider(WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
        treeViewer.setComparator(new ViewerComparator());
        treeViewer.setUseHashlookup(true);
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                projectSelectionChanged((IProject) selection.getFirstElement());
            }
        });
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    Object item = ((IStructuredSelection) selection).getFirstElement();
                    if (item == null) {
                        return;
                    }
                    if (treeViewer.getExpandedState(item)) {
                        treeViewer.collapseToLevel(item, 1);
                    } else {
                        treeViewer.expandToLevel(item, 1);
                    }
                }
            }
        });
        treeViewer.setInput(ResourcesPlugin.getWorkspace());
    }

    /**
	 * The project selection has changed in the tree view. Update the
	 * project name field value and notify all listeners.
	 * 
	 * @param project
	 *            The project that changed
	 */
    public void projectSelectionChanged(IProject project) {
        selectedProject = project;
        if (listener != null) {
            Event changeEvent = new Event();
            changeEvent.type = SWT.Selection;
            changeEvent.widget = this;
            listener.handleEvent(changeEvent);
        }
    }

    /**
	 * Gives focus to one of the widgets in the group, as determined by the
	 * group.
	 */
    public void setInitialFocus() {
        treeViewer.getTree().setFocus();
    }

    /**
	 * Sets the selected existing project.
	 * @param project
	 */
    public void setSelectedProject(IProject project) {
        selectedProject = project;
        List itemsToExpand = new ArrayList();
        IContainer parent = project.getParent();
        while (parent != null) {
            itemsToExpand.add(0, parent);
            parent = parent.getParent();
        }
        treeViewer.setExpandedElements(itemsToExpand.toArray());
        treeViewer.setSelection(new StructuredSelection(project), true);
    }

    /**
	 * 
	 * @return
	 *      The project that's been selected.
	 */
    public IProject getSelectedProject() {
        return selectedProject;
    }
}
