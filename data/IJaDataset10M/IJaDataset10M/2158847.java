package com.google.gwt.eclipse.oophm.views.hierarchical;

import com.google.gwt.eclipse.oophm.breadcrumbs.BreadcrumbViewer;
import com.google.gwt.eclipse.oophm.model.BreadcrumbContentProvider;
import com.google.gwt.eclipse.oophm.model.BrowserTab;
import com.google.gwt.eclipse.oophm.model.IModelNode;
import com.google.gwt.eclipse.oophm.model.IWebAppDebugModelListener;
import com.google.gwt.eclipse.oophm.model.LaunchConfiguration;
import com.google.gwt.eclipse.oophm.model.ModelLabelProvider;
import com.google.gwt.eclipse.oophm.model.Server;
import com.google.gwt.eclipse.oophm.model.WebAppDebugModel;
import com.google.gwt.eclipse.oophm.model.WebAppDebugModelEvent;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * The panel for the navigation setup using the breadcrumb. Note that this class
 * registers a listener (itself) for model changes, as opposed to the
 * BreadcrumbViewer's content provider registering a listener. This is due to
 * the unconventional way in which the viewer's setInput method is used.
 */
public class BreadcrumbNavigationView extends SelectionProvidingComposite implements IWebAppDebugModelListener {

    private final BreadcrumbViewer breadcrumbViewer;

    private final ContentPanel contentPanel;

    private WebAppDebugModel model;

    public BreadcrumbNavigationView(Composite parent, int style) {
        super(parent, style);
        addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                setInput(null);
            }
        });
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        breadcrumbViewer = new BreadcrumbViewer(this, SWT.HORIZONTAL) {

            @Override
            protected void configureDropDownViewer(TreeViewer viewer, Object input) {
                viewer.setContentProvider(new BreadcrumbContentProvider());
                viewer.setLabelProvider(new ModelLabelProvider());
                viewer.setComparator(new ModelNodeViewerComparator());
            }
        };
        breadcrumbViewer.addOpenListener(new IOpenListener() {

            public void open(OpenEvent event) {
                setSelection(event.getSelection());
            }
        });
        breadcrumbViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                fireSelectionChangedEvent(event);
            }
        });
        breadcrumbViewer.setLabelProvider(new ModelLabelProvider());
        breadcrumbViewer.setContentProvider(new BreadcrumbContentProvider());
        contentPanel = new ContentPanel(this, SWT.NONE);
    }

    public void browserTabCreated(final WebAppDebugModelEvent<BrowserTab> e) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                setSelection(new StructuredSelection(e.getElement()));
                breadcrumbViewer.refresh();
            }
        });
    }

    public void browserTabNeedsAttention(final WebAppDebugModelEvent<BrowserTab> e) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                if (!resetSelectionIfModelNodeIsEqualToCurrentSelection(e.getElement())) {
                    resetSelectionIfModelNodeIsChildOfCurrentSelection(e.getElement());
                }
                breadcrumbViewer.refresh();
            }
        });
    }

    public void browserTabRemoved(final WebAppDebugModelEvent<BrowserTab> e) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                maybeClearSelectionAndSelectNextAvailable(e.getElement());
                breadcrumbViewer.refresh();
            }
        });
    }

    public void browserTabTerminated(final WebAppDebugModelEvent<BrowserTab> e) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                resetSelectionIfModelNodeIsEqualToCurrentSelection(e.getElement());
                breadcrumbViewer.refresh();
            }
        });
    }

    @Override
    public ISelection getSelection() {
        return breadcrumbViewer.getSelection();
    }

    public void launchConfigurationLaunched(final WebAppDebugModelEvent<LaunchConfiguration> e) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                setSelection(new StructuredSelection(e.getElement()));
                breadcrumbViewer.refresh();
            }
        });
    }

    public void launchConfigurationLaunchUrlsChanged(WebAppDebugModelEvent<LaunchConfiguration> e) {
    }

    public void launchConfigurationRemoved(final WebAppDebugModelEvent<LaunchConfiguration> e) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                maybeClearSelectionAndSelectNextAvailable(e.getElement());
                breadcrumbViewer.refresh();
            }
        });
    }

    public void launchConfigurationRestartWebServerStatusChanged(WebAppDebugModelEvent<LaunchConfiguration> e) {
    }

    public void launchConfigurationTerminated(final WebAppDebugModelEvent<LaunchConfiguration> e) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                resetSelectionIfModelNodeIsEqualToCurrentSelection(e.getElement());
                breadcrumbViewer.refresh();
            }
        });
    }

    public void serverCreated(final WebAppDebugModelEvent<Server> e) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                resetSelectionIfModelNodeIsChildOfCurrentSelection(e.getElement());
                breadcrumbViewer.refresh();
            }
        });
    }

    public void serverNeedsAttention(final WebAppDebugModelEvent<Server> e) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                if (!resetSelectionIfModelNodeIsEqualToCurrentSelection(e.getElement())) {
                    resetSelectionIfModelNodeIsChildOfCurrentSelection(e.getElement());
                }
                breadcrumbViewer.refresh();
            }
        });
    }

    public void serverTerminated(final WebAppDebugModelEvent<Server> e) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                resetSelectionIfModelNodeIsEqualToCurrentSelection(e.getElement());
                breadcrumbViewer.refresh();
            }
        });
    }

    public void setInput(WebAppDebugModel newModel) {
        if (model != null) {
            model.removeWebAppDebugModelListener(this);
        }
        this.model = newModel;
        setSelection(new StructuredSelection(WebAppDebugModel.getInstance()));
        if (model != null) {
            model.addWebAppDebugModelListener(this);
        }
    }

    @Override
    public void setSelection(final ISelection selection) {
        assert (selection != null && !selection.isEmpty());
        final Object firstElement = ((IStructuredSelection) selection).getFirstElement();
        breadcrumbViewer.setInput(firstElement);
        breadcrumbViewer.setSelection(selection, true);
        contentPanel.setSelection(firstElement);
        contentPanel.setFocus();
    }

    private IModelNode getFirstElementInSelection() {
        StructuredSelection currentSelection = (StructuredSelection) getSelection();
        if (currentSelection == null || currentSelection.isEmpty()) {
            return null;
        }
        return (IModelNode) currentSelection.getFirstElement();
    }

    /**
   * Even though we don't have a lock on it, querying the model at this point is
   * actually safe. We're looking for parent relationships, and parent
   * relationships are set at construction time for each model object. So, it is
   * not possible for a parent-child relationship to become invalid.
   * 
   * The only possible issue is that a launch configuration could become
   * disconnected from the model while this code is running. That in itself is
   * not dangerous, because the possibleParent is a launch configuration.
   */
    private boolean isParentOf(IModelNode node, IModelNode possibleParent) {
        assert (!(possibleParent instanceof WebAppDebugModel));
        if (possibleParent == null) {
            return false;
        }
        while (node != null) {
            if (node.getParent() == possibleParent) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }

    private void maybeClearSelectionAndSelectNextAvailable(BrowserTab removedBrowserTab) {
        IModelNode currentSelection = getFirstElementInSelection();
        if (currentSelection == null) {
            return;
        }
        if (currentSelection == removedBrowserTab) {
            BrowserTab latestActiveBrowserTab = removedBrowserTab.getLaunchConfiguration().getLatestActiveBrowserTab();
            if (latestActiveBrowserTab != null) {
                setSelection(new StructuredSelection(latestActiveBrowserTab));
            } else {
                setSelection(new StructuredSelection(removedBrowserTab.getLaunchConfiguration()));
            }
        }
    }

    private void maybeClearSelectionAndSelectNextAvailable(LaunchConfiguration removedLaunchConfiguration) {
        IModelNode currentSelection = getFirstElementInSelection();
        if (currentSelection == null) {
            return;
        }
        if (currentSelection == removedLaunchConfiguration || isParentOf(currentSelection, removedLaunchConfiguration)) {
            LaunchConfiguration latestActiveLaunchConfiguration = WebAppDebugModel.getInstance().getLatestActiveLaunchConfiguration();
            if (latestActiveLaunchConfiguration != null) {
                setSelection(new StructuredSelection(latestActiveLaunchConfiguration));
            } else {
                setSelection(new StructuredSelection(WebAppDebugModel.getInstance()));
            }
        }
    }

    private boolean resetSelectionIfModelNodeIsChildOfCurrentSelection(IModelNode modelNode) {
        assert (modelNode instanceof BrowserTab || modelNode instanceof Server);
        IModelNode selectionModelNode = getFirstElementInSelection();
        if (isParentOf(modelNode, selectionModelNode)) {
            setSelection(getSelection());
            return true;
        }
        return false;
    }

    private boolean resetSelectionIfModelNodeIsEqualToCurrentSelection(final IModelNode modelNode) {
        if (getFirstElementInSelection() == modelNode) {
            setSelection(getSelection());
            return true;
        }
        return false;
    }
}
