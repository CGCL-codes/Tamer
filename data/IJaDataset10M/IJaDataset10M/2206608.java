package server.campaign.commands.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;
import server.campaign.CampaignMain;
import server.campaign.commands.Command;
import server.MWChatServer.auth.IAuthenticator;

public class SetOperationCommand implements Command {

    int accessLevel = IAuthenticator.ADMIN;

    public int getExecutionLevel() {
        return accessLevel;
    }

    public void setExecutionLevel(int i) {
        accessLevel = i;
    }

    String syntax = "Op Type[Short/Long/Special]#Op Name";

    public String getSyntax() {
        return syntax;
    }

    public void process(StringTokenizer command, String Username) {
        int userLevel = CampaignMain.cm.getServer().getUserLevel(Username);
        if (userLevel < getExecutionLevel()) {
            CampaignMain.cm.toUser("AM:Insufficient access level for command. Level: " + userLevel + ". Required: " + accessLevel + ".", Username, true);
            return;
        }
        String opType;
        String opName;
        try {
            opType = command.nextToken();
            opName = command.nextToken();
        } catch (Exception ex) {
            CampaignMain.cm.toUser("Syntax setoperation#optype#opname", Username, true);
            return;
        }
        File opFile = new File("./data/operations/" + opType + "/" + opName + ".txt");
        try {
            FileOutputStream fos = new FileOutputStream(opFile);
            PrintStream ps = new PrintStream(fos);
            while (command.hasMoreTokens()) {
                ps.println(command.nextToken().replaceAll("\\(pound\\)", "#"));
            }
            ps.close();
            fos.close();
        } catch (Exception ex) {
            CampaignMain.cm.toUser("Unable to write to " + opFile.getName(), Username, true);
            return;
        }
        CampaignMain.cm.doSendModMail("NOTE", Username + " has updated " + opFile.getName());
    }
}
