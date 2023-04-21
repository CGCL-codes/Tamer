package server.campaign.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;
import megamek.common.Entity;
import common.CampaignData;
import server.campaign.CampaignMain;
import server.campaign.SPlayer;
import server.campaign.commands.Command;

public class CodeTestCommand implements Command {

    int accessLevel = 0;

    public int getExecutionLevel() {
        return 200;
    }

    public void setExecutionLevel(int i) {
    }

    String syntax = "";

    public String getSyntax() {
        return syntax;
    }

    public void process(StringTokenizer command, String Username) {
        if (accessLevel != 0) {
            int userLevel = CampaignMain.cm.getServer().getUserLevel(Username);
            if (userLevel < getExecutionLevel()) {
                CampaignMain.cm.toUser("AM:Insufficient access level for command. Level: " + userLevel + ". Required: " + accessLevel + ".", Username, true);
                return;
            }
        }
        int id = Integer.parseInt(command.nextToken());
        String cmd = command.nextToken();
        String file = "./campaign/entites/entity" + id + ".dat";
        SPlayer player = null;
        Entity en = null;
        File dirFile = new File("./campaign/entites");
        if (!dirFile.exists()) dirFile.mkdir();
        if (cmd.equalsIgnoreCase("load")) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                en = (Entity) ois.readObject();
                ois.close();
            } catch (Exception ex) {
                CampaignData.mwlog.errLog(ex);
            }
            CampaignMain.cm.doSendModMail("NOTE", "Entity loaded: " + en.getShortNameRaw());
        } else {
            player = CampaignMain.cm.getPlayer(Username);
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(player.getUnit(id).getEntity());
                oos.close();
            } catch (Exception ex) {
                CampaignData.mwlog.errLog(ex);
            }
        }
    }
}
