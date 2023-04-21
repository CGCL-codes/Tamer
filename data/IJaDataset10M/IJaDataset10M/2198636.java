package ca.qc.adinfo.rouge.social.command;

import ca.qc.adinfo.rouge.RougeServer;
import ca.qc.adinfo.rouge.command.RougeCommand;
import ca.qc.adinfo.rouge.data.RougeObject;
import ca.qc.adinfo.rouge.server.DBManager;
import ca.qc.adinfo.rouge.server.core.SessionContext;
import ca.qc.adinfo.rouge.social.db.SocialDb;
import ca.qc.adinfo.rouge.user.User;

public class RemoveBuddy extends RougeCommand {

    @Override
    public void execute(RougeObject data, SessionContext session, User user) {
        DBManager dbManager = RougeServer.getInstance().getDbManager();
        long friendId = data.getLong("friend");
        boolean ret = SocialDb.deleteFriend(dbManager, user.getId(), friendId);
        if (ret) {
            sendSuccess(session);
        } else {
            sendFailure(session);
        }
    }
}
