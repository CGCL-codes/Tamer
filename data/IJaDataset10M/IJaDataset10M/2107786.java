package filemanager.context.impl;

import filemanager.context.ContextAction;
import filemanager.vfs.ArchiveIfc;
import java.awt.event.ActionEvent;

/**
 *
 * @author sahaqiel
 */
public class DecompressToFolderAction implements ContextAction {

    private ArchiveIfc fileIfc;

    public DecompressToFolderAction(ArchiveIfc fileIfc) {
        this.fileIfc = fileIfc;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
    }

    @Override
    public int getIndex() {
        return 10;
    }

    @Override
    public String getName() {
        return "Decompress to Folder...";
    }

    @Override
    public String getToolTip() {
        return "";
    }
}
