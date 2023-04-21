package com.ivis.xprocess.ui.util;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.framework.exceptions.AuthenticationException;
import com.ivis.xprocess.framework.rules.Alert.Severity;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.datawrappers.PersonWrapper;
import com.ivis.xprocess.ui.perspectives.manager.PerspectiveFactory;
import com.ivis.xprocess.ui.properties.ActionMessages;
import com.ivis.xprocess.ui.properties.AlertMessages;
import com.ivis.xprocess.ui.properties.EditorMessages;
import com.ivis.xprocess.ui.properties.VCSMessages;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory;
import com.ivis.xprocess.ui.refresh.ChangeRecord;
import com.ivis.xprocess.ui.refresh.IRefreshListener;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.tables.viewmanagers.AlertTableManager;

public class MainToolbarButtonManager implements IRefreshListener {

    private static MainToolbarButtonManager mainToolbarButtonManager;

    private static final String openAlertActionId = "com.ivis.xprocess.ui.actions.objects.image.OpenAlertsViewIconAction";

    private static final String openPersonalPlannerImageActionId = "com.ivis.xprocess.ui.actions.objects.image.OpenLoggedInPersonPersonalPlannerAction";

    private static final String selectProfileActionId = "com.ivis.xprocess.ui.actions.objects.selectProfileAction";

    private static final String synchronizeActionId = "com.ivis.xprocess.ui.actions.objects.SelectVCSAction";

    private static final String selectHelpActionId = "com.ivis.xprocess.ui.actions.objects.SelectHelpAction";

    private static final String selectGettingStartedActionId = "com.ivis.xprocess.ui.actions.objects.SelectGettingStartedAction";

    private static final String cancelActionId = "com.ivis.xprocess.ui.actions.objects.cancelEditorChangesAction";

    private static Map<String, Image> perspectiveImages = new HashMap<String, Image>();

    private MainToolbarButtonManager() {
        updateGettingStartedButton();
    }

    public static MainToolbarButtonManager getInstance() {
        if (mainToolbarButtonManager == null) {
            mainToolbarButtonManager = new MainToolbarButtonManager();
        }
        return mainToolbarButtonManager;
    }

    public static void startUp() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                updateSynchronizeButton();
                updateGettingStartedButton();
                updateHelpButton();
                updateSelectProfileButton();
                updateCancel();
                updateAlertsButton();
                updateToolBar();
                ChangeEventFactory.getInstance().addRefreshListener(getInstance());
            }
        });
    }

    /**
     * Must be called after you make a change to your toolbar button, to
     * propogate your change to the toolbar itself.
     */
    private static void updateToolBar() {
        if (UIPlugin.isXprocessClosing()) {
            return;
        }
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window != null) {
            CoolBarManager coolBarManager = ((ApplicationWindow) window).getCoolBarManager();
            coolBarManager.update(true);
        }
    }

    public static void updateAlertsButtonAsync() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                updateAlertsButton();
                updateToolBar();
            }
        });
    }

    private static void updateAlertsButton() {
        ToolItem toolItem = getTooltemForAction(openAlertActionId);
        if ((toolItem != null) && !toolItem.isDisposed()) {
            toolItem.setText(ActionMessages.open_alerts_view);
            Severity currentSeverity = AlertTableManager.getInstance().getHighestSeverityAmongCurrentAlerts();
            if (currentSeverity != null) {
                UIType currentSeverityType = IconManager.getInstance().getSeverityType(currentSeverity);
                toolItem.setImage(currentSeverityType.image);
                toolItem.setToolTipText(AlertMessages.alert_tooltip_with_space + currentSeverityType.label);
            } else {
                toolItem.setImage(UIType.severity_none.image);
                toolItem.setText(AlertMessages.no_alerts_tooltip);
            }
        }
    }

    public static void updateHelpButtonAsync() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                updateHelpButton();
                updateToolBar();
            }
        });
    }

    public static void updateHelpButton() {
        ToolItem toolItem = getTooltemForAction(selectHelpActionId);
        if ((toolItem != null) && !toolItem.isDisposed()) {
            toolItem.setText(ActionMessages.help);
        }
    }

    private static void updateGettingStartedButton() {
        ToolItem toolItem = getTooltemForAction(selectGettingStartedActionId);
        if ((toolItem != null) && !toolItem.isDisposed()) {
            toolItem.setImage(UIType.getting_started.image);
        }
    }

    public static void updatePersonalPlannerButtonAsynch() {
        if (UIPlugin.getDataSource() == null) {
            return;
        }
        try {
            UIPlugin.getDataSource().getAuthenticatedPerson();
        } catch (AuthenticationException e1) {
            return;
        }
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                try {
                    ToolItem toolItem = getTooltemForAction(openPersonalPlannerImageActionId);
                    if ((toolItem != null) && !toolItem.isDisposed()) {
                        Person person = UIPlugin.getDataSource().getAuthenticatedPerson();
                        toolItem.setText(person.getLabel());
                        updateToolBar();
                    }
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateSelectProfileButtonAsync() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                updateSelectProfileButton();
                updateToolBar();
            }
        });
    }

    private static void updateSelectProfileButton() {
        ToolItem toolItem = getTooltemForAction(selectProfileActionId);
        if ((toolItem != null) && !toolItem.isDisposed()) {
            if (PerspectiveFactory.getInstance().getPerspectiveDescriptor() != null) {
                toolItem.setText(PerspectiveFactory.getInstance().getPerspectiveDescriptor().getLabel());
                toolItem.setImage(getImage(PerspectiveFactory.getInstance().getPerspectiveDescriptor()));
                toolItem.setToolTipText(ActionMessages.switch_perspective_tooltip);
            }
        }
    }

    public static Image getImage(IPerspectiveDescriptor perspectiveDescriptor) {
        if (perspectiveImages.containsKey(perspectiveDescriptor.getId())) {
            return perspectiveImages.get(perspectiveDescriptor.getId());
        }
        Image image = perspectiveDescriptor.getImageDescriptor().createImage();
        perspectiveImages.put(perspectiveDescriptor.getId(), image);
        return image;
    }

    public static void updateSynchronizeButtonAsync() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                updateSynchronizeButton();
                updateToolBar();
            }
        });
    }

    private static void updateSynchronizeButton() {
        ToolItem toolItem = getTooltemForAction(synchronizeActionId);
        if ((toolItem != null) && !toolItem.isDisposed()) {
            if (UIPlugin.hasVCS()) {
                if (UIPlugin.isConnectedToVCS()) {
                    toolItem.setText(ActionMessages.synchronize);
                    toolItem.setImage(UIType.datasource_withvcs_connected.image);
                    toolItem.setToolTipText(VCSMessages.connected_mode);
                } else {
                    toolItem.setText(ActionMessages.connect);
                    toolItem.setImage(UIType.datasource_withvcs_disconnected.image);
                    toolItem.setToolTipText(VCSMessages.working_disconnected_tooltip);
                }
            } else {
                toolItem.setImage(UIType.datasource_share.image);
                toolItem.setText(ActionMessages.share_data_source);
                toolItem.setToolTipText(VCSMessages.click_to_share);
            }
            toolItem.getParent().update();
        }
    }

    public static void updateCancelAsync() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                updateCancel();
                updateToolBar();
            }
        });
    }

    private static void updateCancel() {
        ToolItem toolItem = getTooltemForAction(cancelActionId);
        if ((toolItem != null) && !toolItem.isDisposed()) {
            toolItem.setEnabled(false);
            IWorkbenchPage activePage = WorkbenchUtil.getActivePage();
            if (activePage != null) {
                if ((activePage != null) && (activePage.getActiveEditor() != null)) {
                    toolItem.setEnabled(activePage.getActiveEditor().isDirty());
                    toolItem.setToolTipText(EditorMessages.cancel_changes_tooltip);
                }
            }
        }
    }

    /**
     * Gets you toolbar button for the specified action.
     *
     */
    public static ToolItem getTooltemForAction(String actionId) {
        if (Display.getCurrent().isDisposed()) {
            return null;
        }
        if (UIPlugin.isXprocessClosing()) {
            return null;
        }
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window == null) {
            return null;
        }
        CoolBarManager coolBarManager = ((ApplicationWindow) window).getCoolBarManager();
        if (coolBarManager != null) {
            if (coolBarManager.getControl() != null) {
                for (Control control : coolBarManager.getControl().getChildren()) {
                    if (control instanceof ToolBar) {
                        ToolBar toolBar = (ToolBar) control;
                        for (ToolItem toolItem : toolBar.getItems()) {
                            if (toolItem.getData() instanceof ActionContributionItem) {
                                ActionContributionItem actionContributionItem = (ActionContributionItem) toolItem.getData();
                                if (actionContributionItem.getId().equals(actionId)) {
                                    return toolItem;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void refreshEvent(ChangeRecord changeRecord) {
        if (changeRecord.getElementWrapper() instanceof PersonWrapper) {
            if (changeRecord.hasChange(ChangeEvent.FIELDS_CHANGED) && changeRecord.hasProperty("NAME")) {
                try {
                    Person person = (Person) changeRecord.getElement();
                    Person autenticatedPerson = UIPlugin.getDataSource().getAuthenticatedPerson();
                    if (person.getUuid().equals(autenticatedPerson.getUuid())) {
                        updatePersonalPlannerButtonAsynch();
                    }
                } catch (AuthenticationException authenticationException) {
                }
            }
        }
    }
}
