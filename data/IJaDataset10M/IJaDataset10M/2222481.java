package ch.isbiel.oois.infospeaker.command;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import ch.oois.common.gui.command.Command;

/**
 * 
 * 
 * @author $Author$
 * @version $Revision$
 */
public class RemoveCommand implements Command {

    private DefaultMutableTreeNode _dmtn;

    private DefaultTreeModel _dtm;

    public RemoveCommand(DefaultMutableTreeNode dmtn, DefaultTreeModel dtm) {
        _dmtn = dmtn;
        _dtm = dtm;
    }

    /**
   * @see ch.isbiel.oois.common.command.Command#execute()
   */
    public void execute() {
        RemoveFromFavoritesCommand rffc = new RemoveFromFavoritesCommand(_dmtn, _dtm);
        rffc.execute();
        _dtm.removeNodeFromParent(_dmtn);
    }
}
