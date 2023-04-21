package ch.bbv.explorer.actions;

import javax.swing.Action;
import ch.bbv.explorer.Explorer;

/**
 * The class defines a global action executed on the model displayed in the
 * explorer owning the action. Global actions are normally menu items in the
 * menu bar of the explorer.
 * @author MarcelBaumann
 * @version $Revision: 1.6 $
 */
public interface ExplorerGlobalAction extends Action {

    /**
   * Returns the name of the action as displayed on the user interface.
   * @return name of the action
   */
    String getName();

    /**
   * Returns the explorer owning the global action.
   * @return the owning explorer
   * @see #setExplorer(Explorer)
   */
    Explorer getExplorer();

    /**
   * Sets the explorer owning the global action.
   * @param explorer the owning explorer
   * @see #getExplorer()
   */
    void setExplorer(Explorer explorer);

    /**
   * Executes the action.
   * @return true if sucessful otherwise false
   */
    boolean execute();

    /**
   * Returns the result of the last action executed. The result before the first
   * action executed will be false.
   * @return result of last action executed
   */
    boolean getLastResult();
}
