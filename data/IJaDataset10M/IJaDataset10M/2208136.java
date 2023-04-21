package ch.bbv.explorer.components;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerMenuBar.Names;
import ch.bbv.explorer.actions.ExplorerGlobalAction;

/**
 * The explorer window adapter insures that the standard action exit is called
 * if the explorer frame is closed through the window manager of the operating
 * system.
 * @author MarcelBaumann
 * @version $Revision: 1.3 $
 */
public class ExplorerWindowAdapter extends WindowAdapter {

    /**
   * The explorer on which the adapter is applied.
   */
    private Explorer explorer = null;

    /**
   * Constructor of the class.
   * @param explorer explorer on which the adapter is applied
   * @pre explorer != null
   */
    public ExplorerWindowAdapter(Explorer explorer) {
        assert explorer != null;
        this.explorer = explorer;
    }

    /**
   * @inheritDoc
   * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
   */
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        ExplorerGlobalAction action = explorer.getMenuBar().getStandardAction(Names.EXIT);
        if (action != null) {
            if (action.execute()) {
                System.exit(0);
            }
        } else {
            explorer.close();
            System.exit(0);
        }
    }
}
