package com.ivis.xprocess.ui.views.burndown;

import java.text.MessageFormat;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.diagram.AntialiasedScalableRootEditPart;
import com.ivis.xprocess.ui.diagram.DiagramVisualizer;
import com.ivis.xprocess.ui.diagram.DiagramVisualizerSettings;
import com.ivis.xprocess.ui.diagram.PointsBasedConnectionRouter;
import com.ivis.xprocess.ui.diagram.actions.PrintAction;
import com.ivis.xprocess.ui.diagram.model.ModelProvider;
import com.ivis.xprocess.ui.diagram.util.Util;
import com.ivis.xprocess.ui.listeners.IListenToDataSourceChange;
import com.ivis.xprocess.ui.properties.BurndownMessages;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory;
import com.ivis.xprocess.ui.refresh.ChangeRecord;
import com.ivis.xprocess.ui.refresh.IRefreshListener;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.util.SelectionChangeManager;
import com.ivis.xprocess.ui.util.ViewUtil;

public class BurndownView extends ViewPart implements IRefreshListener, IListenToDataSourceChange {

    private ISelectionProvider mySelectionProvider = new ISelectionProvider() {

        public void addSelectionChangedListener(ISelectionChangedListener listener) {
        }

        public ISelection getSelection() {
            return (myModelProvider.getInput() != null) ? new StructuredSelection(myModelProvider.getInput()) : StructuredSelection.EMPTY;
        }

        public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        }

        public void setSelection(ISelection selection) {
        }
    };

    private ISelectionChangedListener mySelectionListener = new ISelectionChangedListener() {

        public void selectionChanged(SelectionChangedEvent event) {
            BurndownView.this.selectionChanged(event.getSelection());
        }
    };

    private IPartListener myPartListener = new IPartListener() {

        public void partActivated(IWorkbenchPart part) {
            if (BurndownView.this == part) {
                refresh();
            }
        }

        public void partBroughtToTop(IWorkbenchPart part) {
        }

        public void partClosed(IWorkbenchPart part) {
        }

        public void partDeactivated(IWorkbenchPart part) {
        }

        public void partOpened(IWorkbenchPart part) {
        }
    };

    private ControlListener myResizeListener = new ControlAdapter() {

        @Override
        public void controlResized(ControlEvent e) {
            myVisualizer.refresh(false);
        }
    };

    private DiagramVisualizer myVisualizer;

    private ModelProvider myModelProvider = new BurndownModelProvider();

    private BurndownLockAction myLockAction;

    private PrintAction myPrintAction;

    public BurndownView() {
    }

    @Override
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        myLockAction = new BurndownLockAction(this);
        IActionBars actionBars = site.getActionBars();
        IToolBarManager toolbarManager = actionBars.getToolBarManager();
        toolbarManager.add(myLockAction);
        myPrintAction = new PrintAction(this);
        myPrintAction.setLazyEnablementCalculation(false);
        myPrintAction.setEnabled(false);
        toolbarManager.add(myPrintAction);
        toolbarManager.update(true);
        getSite().setSelectionProvider(mySelectionProvider);
        SelectionChangeManager.addSelectionListener(mySelectionListener);
        ChangeEventFactory.getInstance().addRefreshListener(this);
        UIPlugin.getDefault().addDataSourceListener(this);
        getSite().getPage().addPartListener(myPartListener);
    }

    @Override
    public void createPartControl(Composite parent) {
        DiagramVisualizerSettings settings = new DiagramVisualizerSettings();
        settings.setEditPartFactory(new BurndownEditPartFactory());
        settings.setModelProvider(myModelProvider);
        settings.setLayoutPerformer(new BurndownLayoutPerformer());
        ScalableRootEditPart rootEditPart = new AntialiasedScalableRootEditPart();
        ConnectionLayer connectionLayer = (ConnectionLayer) rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
        connectionLayer.setConnectionRouter(new PointsBasedConnectionRouter());
        settings.setRootEditPart(rootEditPart);
        myVisualizer = new DiagramVisualizer(settings, parent);
        myPrintAction.setGraphicalViewer(myVisualizer.getViewer());
        myVisualizer.getControl().addControlListener(myResizeListener);
        refresh();
    }

    @Override
    public void setFocus() {
        myVisualizer.getControl().setFocus();
    }

    @Override
    public void dispose() {
        UIPlugin.getDefault().removeDataSourceListener(this);
        ChangeEventFactory.getInstance().removeRefreshListener(this);
        SelectionChangeManager.removeSelectionListener(mySelectionListener);
        getSite().getPage().removePartListener(myPartListener);
        super.dispose();
    }

    public void refreshEvent(final ChangeRecord changeRecord) {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                IElementWrapper input = (IElementWrapper) myModelProvider.getInput();
                if (input != null) {
                    if (input.isDeleted() || input.isGhost()) {
                        refresh();
                    } else if (changeRecord.hasChange(ChangeEvent.RESCHEDULED)) {
                        IElementWrapper changed = changeRecord.getChangedObject();
                        if (Util.isPredecessorOrEqual(changed, input) || Util.isPredecessorOrEqual(input, changed)) {
                            refresh();
                        }
                    }
                }
            }
        });
    }

    public void inputHasChanged(IElementWrapper newBaseElementWrapper) {
    }

    public void newDatasourceEvent() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                myModelProvider.setInput(null, false);
                refresh();
            }
        });
    }

    public void profileChangeEvent() {
        newDatasourceEvent();
    }

    private void refresh() {
        IElementWrapper input = (IElementWrapper) myModelProvider.getInput();
        if ((input != null) && (input.isDeleted() || input.isGhost())) {
            input = null;
        }
        if (input == null) {
            setPartName(BurndownMessages.burndown_chart_title);
            myPrintAction.setEnabled(false);
        } else {
            setPartName(MessageFormat.format("Burndown Chart - {0}", input.getLabel()));
        }
        myLockAction.setEnabled(true);
        if (myVisualizer != null) {
            if (input == null) {
                myVisualizer.showMessage("Select \"Show in | Burndown Chart\" from menu of Parent task, Project or Folder to display Burndown chart");
            } else {
                myPrintAction.setEnabled(true);
                myModelProvider.rebuildModel();
                myVisualizer.refresh();
                myVisualizer.showDiagram();
            }
        }
    }

    private void selectionChanged(ISelection selection) {
        if (myLockAction.isLocked()) {
            return;
        }
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            if (structuredSelection.size() == 1) {
                Object object = structuredSelection.getFirstElement();
                if (object instanceof IElementWrapper) {
                    IElementWrapper elementWrapper = (IElementWrapper) object;
                    Xelement inputElement = BurndownHelper.getInputElement(elementWrapper);
                    if (inputElement != null) {
                        myModelProvider.setInput(elementWrapper, false);
                        refresh();
                    }
                }
            }
        }
    }

    public void setInitialElementWrapper(final IElementWrapper elementWrapper) {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                myModelProvider.setInput(elementWrapper, false);
                refresh();
            }
        });
    }

    public boolean isLocked() {
        return myLockAction.isLocked();
    }

    public IElementWrapper getViewInput() {
        return (IElementWrapper) myModelProvider.getInput();
    }

    public void updateLockImage(boolean locked) {
        if (locked) {
            this.setTitleImage(UIType.burndown_chart_locked.image);
        } else {
            this.setTitleImage(UIType.burndown_chart.image);
        }
    }
}
