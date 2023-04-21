package jcontrol.conect.gui.design;

import javax.swing.tree.*;

/**
 * Interface for objects that may be created interactively within a DesignBox.
 *  
 * @version $Revision: 1.1.1.1 $ 
 * @author  Thomas F�rster   
 */
public interface CreatableItem {

    /**
     * Creates the object and inserts it to a specific DesignBox 
     * @param box the DesignBox to add the new object to
     * @param dest the destination-path of the box�s tree
     */
    public void createAndInsert(DesignBox box, TreePath dest);
}
