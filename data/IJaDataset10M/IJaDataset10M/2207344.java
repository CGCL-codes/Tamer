package org.wsmostudio.discovery.ui.view;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.wsmo.service.WebService;
import org.wsmostudio.runtime.*;
import org.wsmostudio.ui.GUIHelper;
import org.wsmostudio.ui.WsmoUIPlugin;
import org.wsmostudio.ui.editors.WSMOEditorInput;
import org.wsmostudio.ui.editors.model.ObservableModel;

public class DiscoveryResultsView extends ViewPart {

    public static final String VIEW_ID = "org.wsmostudio.discovery.ui.view.resultsView";

    private Text repositoryField, queryField;

    private Table resultsTable;

    private Group resultsPanel;

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        Composite infoPanel = new Composite(parent, SWT.BORDER);
        infoPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        infoPanel.setLayout(new GridLayout(4, false));
        new Label(infoPanel, SWT.NONE).setText("Repository : ");
        repositoryField = new Text(infoPanel, SWT.SINGLE | SWT.READ_ONLY);
        repositoryField.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
        repositoryField.setLayoutData(new GridData(200, SWT.DEFAULT));
        new Label(infoPanel, SWT.NONE).setText("  Query : ");
        queryField = new Text(infoPanel, SWT.SINGLE | SWT.READ_ONLY);
        queryField.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
        queryField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        resultsPanel = new Group(parent, SWT.NONE);
        resultsPanel.setText("Result Services");
        resultsPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
        resultsPanel.setLayout(new GridLayout(1, false));
        resultsTable = new Table(resultsPanel, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        resultsTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        resultsTable.setLinesVisible(false);
        resultsTable.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                doOpenService((WebService) resultsTable.getSelection()[0].getData());
            }
        });
        createContextMenu();
    }

    public void setData(String repositoryName, String goalIRI, java.util.List<WebService> results) {
        repositoryField.setText(repositoryName);
        queryField.setText(goalIRI);
        resultsTable.removeAll();
        if (results != null) {
            for (WebService service : results) {
                TableItem item = new TableItem(resultsTable, SWT.NONE);
                item.setText(service.getIdentifier().toString());
                item.setImage(JFaceResources.getImage(WsmoImageRegistry.WEBSERVICE_ICON));
                item.setData(service);
            }
        }
        resultsPanel.setText("Result Services - " + ((results == null || results.size() == 0) ? "no matches" : results.size() + " matches") + " found");
    }

    @Override
    public void setFocus() {
    }

    private void doOpenService(WebService service) {
        try {
            String targetEditorID = WsmoUIPlugin.getDefault().getExtensionManager().locateEditorForEntity(service);
            if (targetEditorID == null) {
                return;
            }
            ObservableModel model = GUIHelper.createEditorModel(service, targetEditorID);
            WSMOEditorInput input = new WSMOEditorInput(service, model);
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, targetEditorID);
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.wsmostudio.ui.views.WSMOView");
        } catch (PartInitException e) {
            LogManager.logError(e);
        }
    }

    private void doSaveService(WebService service) {
        SaveResourceWizard wizard = new SaveResourceWizard(service);
        wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        dialog.open();
    }

    private void createContextMenu() {
        final MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(menuMgr);
            }
        });
        resultsTable.setMenu(menuMgr.createContextMenu(resultsTable));
    }

    private void fillContextMenu(MenuManager menuMgr) {
        if (false == GUIHelper.containsCursor(resultsTable)) {
            return;
        }
        menuMgr.add(new Action("Open WebService") {

            public void run() {
                doOpenService((WebService) resultsTable.getSelection()[0].getData());
            }
        });
        menuMgr.add(new Action("Save in Workspace") {

            public void run() {
                doSaveService((WebService) resultsTable.getSelection()[0].getData());
            }
        });
        menuMgr.add(new Separator());
        menuMgr.add(new Action("Remove from List") {

            public void run() {
                resultsTable.getSelection()[0].dispose();
            }
        });
    }
}
