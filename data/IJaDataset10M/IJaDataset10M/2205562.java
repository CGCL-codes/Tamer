package com.kokesoft.easywebdav.actions;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.eclipse.ui.views.navigator.IResourceNavigator;
import org.eclipse.ui.views.navigator.OpenActionGroup;
import com.kokesoft.easywebdav.Activator;

public class ActionsProvider extends CommonActionProvider {

    private static Logger logger = Logger.getLogger(Activator.PLUGIN_ID);

    private NewRepositoryAction newRepositoryAction;

    private DeleteRepositoryAction deleteRepositoryAction;

    private OpenRepositoryAction openRepositoryAction;

    private CloseRepositoryAction closeRepositoryAction;

    private OpenResourceAction openAction;

    private NewCollectionAction newCollectionAction;

    private NewFileAction newFileAction;

    private DeleteResourceAction deleteResourceAction;

    private PasteResourceAction pasteResourceAction;

    private CopyResourceAction copyResourceAction;

    private RenameResourceAction renameResourceAction;

    private ExportResourceAction exportResourceAction;

    private ImportResourceAction importResourceAction;

    private GetUriAction getUriAction;

    private RefreshAction refreshAction;

    /**
	 * Construct Property Action provider.
	 */
    public ActionsProvider() {
    }

    public void init(ICommonActionExtensionSite aSite) {
        logger.log(Level.FINEST, aSite.getClass().getName());
        ICommonViewerSite viewSite = aSite.getViewSite();
        if (viewSite instanceof ICommonViewerWorkbenchSite) {
            ICommonViewerWorkbenchSite workbenchSite = (ICommonViewerWorkbenchSite) viewSite;
            openAction = new OpenResourceAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            newRepositoryAction = new NewRepositoryAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            deleteRepositoryAction = new DeleteRepositoryAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            openRepositoryAction = new OpenRepositoryAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            closeRepositoryAction = new CloseRepositoryAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            newCollectionAction = new NewCollectionAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            pasteResourceAction = new PasteResourceAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            copyResourceAction = new CopyResourceAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            deleteResourceAction = new DeleteResourceAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            refreshAction = new RefreshAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            renameResourceAction = new RenameResourceAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            newFileAction = new NewFileAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            exportResourceAction = new ExportResourceAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            importResourceAction = new ImportResourceAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
            getUriAction = new GetUriAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
        }
    }

    public void fillActionBars(IActionBars actionBars) {
        if (openAction.isEnabled()) actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, openAction);
    }

    public void fillContextMenu(IMenuManager menu) {
        logger.log(Level.FINEST, "" + menu.getClass().getName());
        if (newRepositoryAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, newRepositoryAction);
        if (openAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, openAction);
        if (deleteRepositoryAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, deleteRepositoryAction);
        if (newCollectionAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, newCollectionAction);
        if (newFileAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, newFileAction);
        if (pasteResourceAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, pasteResourceAction);
        if (copyResourceAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, copyResourceAction);
        if (deleteResourceAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, deleteResourceAction);
        if (renameResourceAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, renameResourceAction);
        menu.appendToGroup(ICommonMenuConstants.GROUP_GENERATE, refreshAction);
        if (openRepositoryAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_GENERATE, openRepositoryAction);
        if (closeRepositoryAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_GENERATE, closeRepositoryAction);
        if (exportResourceAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_GENERATE, exportResourceAction);
        if (importResourceAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_GENERATE, importResourceAction);
        if (getUriAction.isEnabled()) menu.appendToGroup(ICommonMenuConstants.GROUP_SOURCE, getUriAction);
    }
}
