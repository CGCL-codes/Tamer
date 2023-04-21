package org.op.nav;

public interface FolderController {

    /**
	 *  add a folder to the selected node
	 *  - bring up a dialog to request the folder name
	 *  - update the navigation view
	 *  
	 */
    public void addFolder();

    /**
	 *  Rename the selected folder
	 *  - bring up a dialog to request the new name
	 *  - reset all alias paths
	 *  
	 */
    public void renameFolder();

    /**
	 *   Send the selected folder (and all of its contents) to the trash folder
	 */
    public void trashFolder();
}
