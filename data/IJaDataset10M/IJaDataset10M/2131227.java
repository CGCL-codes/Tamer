package net.sourceforge.squirrel_sql.plugins.refactoring.actions;

import java.awt.event.ActionEvent;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.action.SquirrelAction;
import net.sourceforge.squirrel_sql.client.session.IObjectTreeAPI;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.action.IObjectTreeAction;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.util.ICommand;
import net.sourceforge.squirrel_sql.fw.util.Resources;

public abstract class AbstractRefactoringAction extends SquirrelAction implements IObjectTreeAction {

    /**
	 * Current session.
	 */
    protected ISession _session;

    /**
	 * Current object tree.
	 */
    protected IObjectTreeAPI _tree;

    public AbstractRefactoringAction(IApplication app, Resources rsrc) {
        super(app, rsrc);
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent evt) {
        if (_session != null) {
            IDatabaseObjectInfo[] infos = _tree.getSelectedDatabaseObjects();
            if (infos.length > 1 && !isMultipleObjectAction()) {
                _session.showErrorMessage(getErrorMessage());
            } else {
                try {
                    getCommand(infos).execute();
                } catch (Exception e) {
                    _session.showErrorMessage(e);
                }
            }
        }
    }

    /**
	 * Returns the command to be executed when this action is invoked.
	 * 
	 * @param info
	 *           the selected object(s)
	 * @return the ICommand implementation to execute
	 */
    protected abstract ICommand getCommand(IDatabaseObjectInfo[] info);

    /**
	 * Returns a boolean value indicating whether or not this action can be executed against multiple objects.
	 * 
	 * @return true if the user can execute this action against multiple slected objects; false otherwise.
	 */
    protected abstract boolean isMultipleObjectAction();

    /**
	 * Subclasses must override this to provide a custom error message when the user chooses to execcute an
	 * action on multiple selected objects, when the action can only be executed on a single object.
	 * 
	 * @return the error message to use for single-object actions
	 */
    protected abstract String getErrorMessage();

    /**
	 * @see net.sourceforge.squirrel_sql.client.session.action.IObjectTreeAction#setObjectTree(net.sourceforge.squirrel_sql.client.session.IObjectTreeAPI)
	 */
    public void setObjectTree(IObjectTreeAPI tree) {
        _tree = tree;
        if (null != _tree) _session = _tree.getSession(); else _session = null;
        setEnabled(null != _tree);
    }
}
