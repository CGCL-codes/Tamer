package com.pawlowsky.zhu.mediafiler.commands;

import com.pawlowsky.zhu.mediafiler.database.Query;
import com.pawlowsky.zhu.mediafiler.database.Database;
import com.pawlowsky.zhu.mediafiler.database.DatabaseFactory;
import com.pawlowsky.zhu.mediafiler.database.DatabaseException;
import com.pawlowsky.zhu.mediafiler.database.TranslationSet;
import com.pawlowsky.zhu.mediafiler.gui.MessageDialog;

/**
 *  Command for setting the new name for a query.
 */
public class SetQueryNameCommand implements Command {

    /**
     * 
     * @param query Query whose name we will be setting.
     * @param name Translations of the names.
     */
    public SetQueryNameCommand(Query query, TranslationSet name) {
        this.query = query;
        this.name = name;
    }

    /**
	 * @see com.pawlowsky.marc.mediafiler.commands.Command#execute()
	 * 
	 * Return value is null.
	 */
    public ExecutionResult execute() throws CommandException {
        Database database = null;
        try {
            database = DatabaseFactory.getDatabase();
        } catch (Exception ex) {
            throw new CommandException(ex);
        }
        try {
            this.query.setName(this.name);
            database.commit();
            return null;
        } catch (DatabaseException databaseException) {
            try {
                database.rollback();
            } catch (Exception ex2) {
                MessageDialog.show(ex2);
            }
            throw new CommandException(databaseException);
        }
    }

    private Query query;

    private TranslationSet name;
}
