package org.o14x.alpha.ui.controller;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.o14x.alpha.domain.Clipboard;
import org.o14x.alpha.ui.coolbar.AddressBarContributionItem;
import org.o14x.alpha.ui.perspectives.MainPerspective;
import org.o14x.alpha.ui.views.folder.columns.ColumnHelperManager;
import org.o14x.alpha.ui.views.foldertree.FolderTreeView;

/**
 * Main access point to UI objects;
 * 
 * @author Olivier DANGREAUX
 */
public class UI {

    public static String CLIPBOARD_CHANGED_KEY = "CLIPBOARD_CHANGED_KEY";

    /**
	 * The main perspective.
	 */
    private MainPerspective mainPerspective;

    /**
	 * The FolderTreeView.
	 */
    private FolderTreeView folderTreeView;

    /**
	 * The AddressBarContributionItem.
	 */
    private AddressBarContributionItem addressBarContributionItem;

    /**
	 * The coolbar.
	 */
    private ICoolBarManager coolBar;

    /**
	 * The tab folder associated with the folder views
	 */
    private CTabFolder folderViewTabFolder;

    /**
	 * The ColumnHelperManager.
	 */
    private ColumnHelperManager columnHelperManager;

    /**
	 * Refresh the state of the clipbord commands.
	 */
    public void refreshClipboardCommands() {
        PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

            public void run() {
                Clipboard clipboard = mainPerspective.getUiController().getDomain().getClipboard();
                Boolean clipboardStatus = clipboard.getStatusIfChanged();
                if (clipboardStatus != null) {
                    ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
                    Map<String, Boolean> filter = new HashMap<String, Boolean>();
                    filter.put(CLIPBOARD_CHANGED_KEY, clipboardStatus);
                    service.refreshElements("org.o14x.alpha.ui.commands.PasteCommand", new HashMap<String, Boolean>());
                }
            }
        });
    }

    /**
	 * Returns the value of coolBar.
	 *
	 * @return The value of coolBar.
	 */
    public ICoolBarManager getCoolBar() {
        return coolBar;
    }

    /**
	 * Sets the value of coolBar.
	 *
	 * @param coolBar The value of coolBar to set.
	 */
    public void setCoolBar(ICoolBarManager coolBar) {
        this.coolBar = coolBar;
    }

    /**
	 * Returns the value of mainPerspective.
	 *
	 * @return The value of mainPerspective.
	 */
    public MainPerspective getMainPerspective() {
        return mainPerspective;
    }

    /**
	 * Sets the value of mainPerspective.
	 *
	 * @param mainPerspective The value of mainPerspective to set.
	 */
    public void setMainPerspective(MainPerspective mainPerspective) {
        this.mainPerspective = mainPerspective;
    }

    /**
	 * Returns the value of folderTreeView.
	 *
	 * @return The value of folderTreeView.
	 */
    public FolderTreeView getFolderTreeView() {
        return folderTreeView;
    }

    /**
	 * Sets the value of folderTreeView.
	 *
	 * @param folderTreeView The value of folderTreeView to set.
	 */
    public void setFolderTreeView(FolderTreeView folderTreeView) {
        this.folderTreeView = folderTreeView;
    }

    /**
	 * Returns the value of addressBarContributionItem.
	 *
	 * @return The value of addressBarContributionItem.
	 */
    public AddressBarContributionItem getAddressBarContributionItem() {
        return addressBarContributionItem;
    }

    /**
	 * Sets the value of addressBarContributionItem.
	 *
	 * @param addressBarContributionItem The value of addressBarContributionItem to set.
	 */
    public void setAddressBarContributionItem(AddressBarContributionItem addressBarContributionItem) {
        this.addressBarContributionItem = addressBarContributionItem;
    }

    /**
	 * Returns the value of folderViewTabFolder.
	 *
	 * @return The value of folderViewTabFolder.
	 */
    public CTabFolder getFolderViewTabFolder() {
        return folderViewTabFolder;
    }

    /**
	 * Sets the value of folderViewTabFolder.
	 *
	 * @param folderViewTabFolder The value of folderViewTabFolder to set.
	 */
    public void setFolderViewTabFolder(CTabFolder folderViewTabFolder) {
        this.folderViewTabFolder = folderViewTabFolder;
    }

    /**
	 * Returns the value of columnHelperManager.
	 *
	 * @return The value of columnHelperManager.
	 */
    public ColumnHelperManager getColumnHelperManager() {
        return columnHelperManager;
    }

    /**
	 * Sets the value of columnHelperManager.
	 *
	 * @param columnHelperManager The value of columnHelperManager to set.
	 */
    public void setColumnHelperManager(ColumnHelperManager columnHelperManager) {
        this.columnHelperManager = columnHelperManager;
    }
}
