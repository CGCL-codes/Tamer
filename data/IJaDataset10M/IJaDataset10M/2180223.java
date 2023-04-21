package freets.gui.design;

/**
 * Interface for objects that may be created interactively within a DesignBox.
 *  
 * @author  Thomas F�rster   
 * @version $Revision: 1.1.1.1 $
 */
public interface ModifyDialogLauncher {

    /**
     * Notifies the ModifyDialogLauncher that an item has been modified.
     */
    public void itemModified(DragDropItem item);
}
