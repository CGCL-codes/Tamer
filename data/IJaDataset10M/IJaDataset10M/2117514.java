package org.chernovia.net.games.parlour.roshumbo.oldserv;

import java.io.IOException;
import org.chernovia.lib.netgames.JGameBot;
import org.chernovia.lib.netgames.JPlayer;
import org.chernovia.lib.netgames.JServ;
import org.chernovia.lib.netgames.db.JGameDB;
import org.chernovia.lib.netgames.db.JGameDat;

public class RPSBase extends JGameDB {

    public static int MAXHAND = 15, MAXTITLE = 6;

    public static void initBase(JGameBot b) {
        JGameDat.initFields("INT Rating INT Games INT Wins", "1500 0 0");
        init(b, RPSBot.DATAFILE, JServ.newline[JServ.SELFSERV]);
    }

    public static void IOError(IOException e) {
        bot.getServ().tch(bot.getChan(), e.getMessage());
        System.exit(-1);
    }

    public static String statLine(JGameDat D) {
        if (D == null) return "No such player.";
        return D.getHandle() + ": " + D.getField("games") + "/" + D.getField("wins") + ", " + D.getField("rating");
    }

    public static void updateStats(JPlayer winner) {
        updateRatings(winner);
        RPSBoard b = (RPSBoard) winner.getBoard();
        for (int p = 0; p < b.getNumPlayers(); p++) {
            RPSPlayer P = (RPSPlayer) b.getPlayer(p);
            JGameDat D = getStats(P.name, true);
            int games = D.getIntField("games");
            int wins = D.getIntField("wins");
            D.setField("games", games + 1);
            if (P == winner) D.setField("wins", wins + 1);
            editStats(D);
        }
    }
}
