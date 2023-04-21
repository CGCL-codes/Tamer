package com.ivis.xprocess.ui.views;

import java.util.Iterator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.rules.Alert;
import com.ivis.xprocess.framework.rules.Alert.Severity;
import com.ivis.xprocess.ui.UIConstants;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.datawrappers.DataCacheManager;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.dialogs.selection.AlertResolutionElementSelectionDialog;
import com.ivis.xprocess.ui.draganddrop.ViewerDropTarget;
import com.ivis.xprocess.ui.draganddrop.util.IDropTargetRoot;
import com.ivis.xprocess.ui.editors.util.EditorUtil;
import com.ivis.xprocess.ui.listeners.IListenToDataSourceChange;
import com.ivis.xprocess.ui.properties.DialogMessages;
import com.ivis.xprocess.ui.properties.ViewMessages;
import com.ivis.xprocess.ui.refresh.DeletionManager;
import com.ivis.xprocess.ui.tables.viewmanagers.AlertTableManager;
import com.ivis.xprocess.ui.threadmanagers.BusinessRulesThreadManager;
import com.ivis.xprocess.ui.util.AlertUtil;
import com.ivis.xprocess.ui.util.IAlertsProvider;
import com.ivis.xprocess.ui.util.IconManager;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.ui.util.ViewUtil;
import com.ivis.xprocess.ui.view.providers.AlertTableContentProvider;
import com.ivis.xprocess.ui.view.providers.AlertTableLabelProvider;
import com.ivis.xprocess.ui.views.sorters.AlertsViewSorter;
import com.ivis.xprocess.ui.wizards.FixRuleWizard;

public class AlertsView extends ViewPart implements IAlertsProvider, IListenToDataSourceChange, IDropTargetRoot {

    private static final String ALERT_SORT_BY_COLUMN = "xprocess.ui.alert.sort.column";

    private static final String ALERT_SORT_DIRECTION = "xprocess.ui.alert.sort.direction";

    public static final int UP = 0;

    public static final int DOWN = 1;

    private TableViewer alertTableViewer;

    private String[] columnNames = new String[] { "", ViewMessages.alert_isfixable_column, ViewMessages.alert_description_column, ViewMessages.alert_location_column, ViewMessages.alert_source_of_problem_column, ViewMessages.alert_properties_column, ViewMessages.alert_assignnee_column };

    private int[] columnWidths = new int[] { 28, 80, 300, 100, 200, 200, 120 };

    private TableColumn[] columns = new TableColumn[columnNames.length];

    private MenuManager menuMgr;

    private int currentSortColumn = 1;

    private int currentSortDirection = UP;

    private Image ascendingImage;

    private Image descendingImage;

    private Action refreshAction;

    @Override
    public void createPartControl(Composite parent) {
        UIPlugin.getDefault().addDataSourceListener(this);
        ascendingImage = UIType.sort_ascending.image;
        descendingImage = UIType.sort_descending.image;
        currentSortColumn = UIPlugin.getDefault().getPreferenceStore().getInt(ALERT_SORT_BY_COLUMN);
        if (currentSortColumn == 0) {
            currentSortColumn = 1;
        }
        currentSortDirection = UIPlugin.getDefault().getPreferenceStore().getInt(ALERT_SORT_DIRECTION);
        alertTableViewer = new TableViewer(parent, SWT.V_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        alertTableViewer.getTable().setLinesVisible(true);
        alertTableViewer.getTable().setHeaderVisible(true);
        createColumns();
        alertTableViewer.getTable().layout();
        alertTableViewer.setContentProvider(new AlertTableContentProvider());
        alertTableViewer.setLabelProvider(new AlertTableLabelProvider());
        alertTableViewer.setInput(AlertsView.this);
        alertTableViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                StructuredSelection structuredSelection = (StructuredSelection) alertTableViewer.getSelection();
                Iterator<?> selectionIterator = structuredSelection.iterator();
                while (selectionIterator.hasNext()) {
                    Alert alert = (Alert) selectionIterator.next();
                    if (alert.getOffendingElementUUIDs().length == 1) {
                        Xelement offendingElement = UIPlugin.getPersistenceHelper().getElement(alert.getOffendingElementUUIDs()[0]);
                        if (offendingElement == null) {
                            UIPlugin.log("Alert created for element that persistence helper can't resolve: " + alert.getOffendingElementUUIDs()[0], IStatus.WARNING, null);
                            return;
                        }
                        IElementWrapper elementWrapper = DataCacheManager.getWrapperByElement(offendingElement);
                        if (elementWrapper == null) {
                            UIPlugin.log("Alert created for element that DataCacheManager can't get wrapper for: " + offendingElement.getUuid(), IStatus.WARNING, null);
                            return;
                        }
                        EditorUtil.getInstance().openEditor(elementWrapper);
                    } else {
                        if (EditorUtil.canOpenEditor(alert)) {
                            Shell currentShell = UIPlugin.getDefault().getWorkbench().getDisplay().getActiveShell();
                            AlertResolutionElementSelectionDialog dialog = new AlertResolutionElementSelectionDialog(currentShell, alert, DialogMessages.alert_element_dialog_title);
                            dialog.open();
                        }
                    }
                }
            }
        });
        alertTableViewer.setSorter(new AlertsViewSorter(this));
        updateContentDescription();
        makeActions();
        contributeToActionBars();
        hookContextMenu();
        new ViewerDropTarget(alertTableViewer.getControl(), this);
        AlertTableManager.getInstance().setAlertsView(this);
        updateTitleImage();
        setupTestHarness();
    }

    private void createColumns() {
        for (int i = 0; i < columnNames.length; i++) {
            columns[i] = new TableColumn(alertTableViewer.getTable(), 0);
            columns[i].setText(columnNames[i]);
            columns[i].setWidth(columnWidths[i]);
        }
        for (int i = 0; i < columns.length; i++) {
            final int index = i;
            TableColumn column = alertTableViewer.getTable().getColumn(i);
            column.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateSortingDirection(index + 1);
                }
            });
        }
        updateSortIcon(currentSortColumn);
    }

    private void updateSortingDirection(int index) {
        if (currentSortDirection == 0) {
            currentSortDirection = 1;
        } else {
            currentSortDirection = 0;
        }
        updateSortIcon(index);
    }

    private void updateSortIcon(int newSortColumnId) {
        if (newSortColumnId != 0) {
            if (currentSortColumn != 0) {
                int theOldSortIndex = currentSortColumn - 1;
                columns[theOldSortIndex].setImage(null);
            }
            int theRealIndex = newSortColumnId - 1;
            currentSortColumn = newSortColumnId;
            if (currentSortDirection == UP) {
                columns[theRealIndex].setImage(ascendingImage);
            } else {
                columns[theRealIndex].setImage(descendingImage);
            }
            alertTableViewer.refresh();
        }
    }

    public int getSortDirection() {
        return currentSortDirection;
    }

    public int getSortColumn() {
        return currentSortColumn;
    }

    @Override
    public void setFocus() {
    }

    private void refresh() {
        if (!alertTableViewer.getControl().isDisposed()) {
            IWorkbenchSiteProgressService service = (IWorkbenchSiteProgressService) AlertsView.this.getSite().getAdapter(IWorkbenchSiteProgressService.class);
            service.warnOfContentChange();
            if (alertTableViewer.getInput() == null) {
                alertTableViewer.setInput(AlertsView.this);
            } else {
                alertTableViewer.refresh();
            }
            updateContentDescription();
            updateTitleImage();
        }
    }

    public void refreshAsync() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                if (DeletionManager.isDeleteRunning()) {
                    return;
                }
                refresh();
            }
        });
    }

    @Override
    public void dispose() {
        if (UIPlugin.getDefault() != null) {
            UIPlugin.getDefault().getPreferenceStore().setValue(ALERT_SORT_BY_COLUMN, currentSortColumn);
            UIPlugin.getDefault().getPreferenceStore().setValue(ALERT_SORT_DIRECTION, currentSortDirection);
        }
        AlertTableManager.getInstance().removeAlertsView();
        UIPlugin.getDefault().removeDataSourceListener(this);
        super.dispose();
    }

    public Object[] getAlerts() {
        return AlertTableManager.getInstance().getAlerts();
    }

    private void hookContextMenu() {
        menuMgr = new MenuManager("#PopupMenu", UIConstants.vcs_view_id);
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(alertTableViewer.getTable());
        alertTableViewer.getTable().setMenu(menu);
    }

    protected void fillContextMenu(IMenuManager manager) {
        final StructuredSelection structuredSelection = (StructuredSelection) alertTableViewer.getSelection();
        final Alert[] alerts = AlertUtil.convertToAlerts(structuredSelection);
        if ((alerts.length > 0) && AlertUtil.allTheSameAlerts(alerts) && AlertUtil.isSelectionFixable(structuredSelection)) {
            Action fixAction = new Action(ViewMessages.alert_fix) {

                @Override
                public void run() {
                    FixRuleWizard fixRuleWizard = new FixRuleWizard(alerts);
                    WizardDialog wizardDialog = new WizardDialog(ViewUtil.getCurrentShell(), fixRuleWizard);
                    int button = wizardDialog.open();
                    if (button == IDialogConstants.CANCEL_ID) {
                        return;
                    }
                }
            };
            manager.add(fixAction);
            manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        }
    }

    private void updateContentDescription() {
        int errorCount = 0;
        int highCount = 0;
        int mediumCount = 0;
        int lowCount = 0;
        for (TableItem tableItem : alertTableViewer.getTable().getItems()) {
            if (tableItem.getData() instanceof Alert) {
                Alert alert = (Alert) tableItem.getData();
                if (alert.getSeverity() == Severity.STOP) {
                    errorCount++;
                }
                if (alert.getSeverity() == Severity.HIGH) {
                    highCount++;
                }
                if (alert.getSeverity() == Severity.MEDIUM) {
                    mediumCount++;
                }
                if (alert.getSeverity() == Severity.LOW) {
                    lowCount++;
                }
            }
        }
        String contentDescription = errorCount + " " + ViewMessages.alert_stop + ", " + highCount + " " + ViewMessages.alert_high + ", " + mediumCount + " " + ViewMessages.alert_medium + ", " + lowCount + " " + ViewMessages.alert_low + " (" + alertTableViewer.getTable().getItems().length + " " + ViewMessages.alert_postfix + ")";
        setContentDescription(contentDescription);
    }

    private void updateTitleImage() {
        Severity currentSeverity = AlertTableManager.getInstance().getHighestSeverityAmongCurrentAlerts();
        if (currentSeverity != null) {
            UIType currentSeverityType = IconManager.getInstance().getSeverityType(currentSeverity);
            this.setTitleImage(currentSeverityType.image);
        } else {
            this.setTitleImage(UIType.severity_none.image);
        }
    }

    protected void setupTestHarness() {
        TestHarness.name(alertTableViewer.getTable(), TestHarness.ALERTSVIEW_TABLE);
    }

    public void newDatasourceEvent() {
        refreshAsync();
    }

    public void profileChangeEvent() {
        refreshAsync();
    }

    public void inputHasChanged(IElementWrapper newBaseElementWrapper) {
        refreshAsync();
    }

    public Object getRootObject() {
        return null;
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(refreshAction);
    }

    private void makeActions() {
        refreshAction = new Action() {

            public void run() {
                BusinessRulesThreadManager.setHasRunOnAllElements(false);
                BusinessRulesThreadManager.getInstance().executeRulesOnAllKnownElements();
            }
        };
        refreshAction.setToolTipText("Refresh");
        refreshAction.setImageDescriptor(UIType.refresh.getImageDescriptor());
    }
}
