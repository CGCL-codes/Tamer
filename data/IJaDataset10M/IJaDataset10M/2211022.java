package net.sourceforge.squirrel_sql.plugins.refactoring.actions;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.util.ICommand;
import net.sourceforge.squirrel_sql.fw.util.Resources;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.plugins.refactoring.commands.DropUniqueConstraintCommand;

public class DropUniqueConstraintAction extends AbstractRefactoringAction {

    private static final long serialVersionUID = 2716140995904907381L;

    /**
	 * Internationalized strings for this class.
	 */
    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(DropUniqueConstraintAction.class);

    private static interface i18n {

        String ACTION_PART = s_stringMgr.getString("DropUniqueConstraintAction.actionPart");

        String OBJECT_PART = s_stringMgr.getString("Shared.tableObject");

        String SINGLE_OBJECT_MESSAGE = s_stringMgr.getString("Shared.singleObjectMessage", OBJECT_PART, ACTION_PART);
    }

    public DropUniqueConstraintAction(IApplication app, Resources rsrc) {
        super(app, rsrc);
    }

    /**
	 * @see net.sourceforge.squirrel_sql.plugins.refactoring.actions.AbstractRefactoringAction#getCommand(net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo[])
	 */
    @Override
    protected ICommand getCommand(IDatabaseObjectInfo[] info) {
        return new DropUniqueConstraintCommand(_session, info);
    }

    /**
	 * @see net.sourceforge.squirrel_sql.plugins.refactoring.actions.AbstractRefactoringAction#getErrorMessage()
	 */
    @Override
    protected String getErrorMessage() {
        return i18n.SINGLE_OBJECT_MESSAGE;
    }

    /**
	 * @see net.sourceforge.squirrel_sql.plugins.refactoring.actions.AbstractRefactoringAction#isMultipleObjectAction()
	 */
    @Override
    protected boolean isMultipleObjectAction() {
        return false;
    }
}
