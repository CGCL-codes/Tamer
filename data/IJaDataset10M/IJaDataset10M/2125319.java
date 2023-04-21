package weka.gui.treevisualizer;

/**
 * Interface implemented by classes that wish to recieve user selection events
 * from a tree displayer.
 *
 * @author Malcolm Ware (mfw4@cs.waikato.ac.nz)
 * @version $Revision: 1.5 $
 */
public interface TreeDisplayListener {

    /**
   * Gets called when the user selects something, in the tree display.
   * @param e Contains what the user selected with what it was selected for.
   */
    void userCommand(TreeDisplayEvent e);
}
