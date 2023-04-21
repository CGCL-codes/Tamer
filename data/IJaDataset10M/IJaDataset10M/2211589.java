package net.confex.action;

import net.confex.tree.FolderNode;
import net.confex.views.FolderView;

public class AddNewFolderAction extends TranslatableAction {

    private FolderView folderView;

    protected String getID() {
        return "net.confex.tree.AddNewFolderAction";
    }

    protected String getTextKey() {
        return "ACTION_ADD_NEW_FOLDER";
    }

    protected String getIconFileName() {
        return FolderNode.getDefaultImageName();
    }

    public AddNewFolderAction(FolderView folderView) {
        super();
        this.folderView = folderView;
    }

    public void run() {
        folderView.runNewFolder();
    }
}
