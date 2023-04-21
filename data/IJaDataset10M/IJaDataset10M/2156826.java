package com.pawlowsky.zhu.mediafiler.commands;

import com.pawlowsky.zhu.mediafiler.database.Database;
import com.pawlowsky.zhu.mediafiler.database.DatabaseException;
import com.pawlowsky.zhu.mediafiler.database.DatabaseFactory;
import com.pawlowsky.zhu.mediafiler.database.DistributionList;
import com.pawlowsky.zhu.mediafiler.database.MediaFile;
import com.pawlowsky.zhu.mediafiler.gui.MessageDialog;

/**
 *  Add a Media File to all the categories that make up
 *  a distribution list.
 */
public class AddFileToDistributionListCommand implements Command {

    public AddFileToDistributionListCommand(MediaFile[] file, DistributionList list) {
        this.mediaFile = file;
        this.distributionList = list;
    }

    /**
     * @see com.pawlowsky.zhu.mediafiler.commands.Command#execute()
     * @return null
     */
    public ExecutionResult execute() throws CommandException {
        Database database = null;
        try {
            database = DatabaseFactory.getDatabase();
        } catch (Exception ex) {
            throw new CommandException(ex);
        }
        try {
            for (int i = 0; i < mediaFile.length; ++i) {
                this.distributionList.addMediaFile(this.mediaFile[i]);
            }
            database.commit();
        } catch (DatabaseException ex) {
            try {
                database.rollback();
            } catch (Exception ex2) {
                MessageDialog.show(ex2);
            }
            throw new CommandException(ex);
        }
        return null;
    }

    /** File to add to category. */
    private MediaFile[] mediaFile;

    /** Distribution list to add file to. */
    private DistributionList distributionList;
}
