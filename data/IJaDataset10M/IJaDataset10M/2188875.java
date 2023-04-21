package org.chernovia.net.games.board.stratego.server;

import java.net.UnknownHostException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.chernovia.lib.misc.MiscUtil;
import org.chernovia.lib.netgames.db.GameBase;
import org.chernovia.lib.netgames.db.GameData;
import org.chernovia.lib.netgames.old.GameBot;
import org.chessworks.common.javatools.io.FileHelper;

public class StratBot extends GameBot<Stratego> {

    public static final String BOT_NAME = "StrategoBot";

    public static final String BOT_VERSION = "Stratego Version 1.1";

    public static final String STRATDIR = "res/strat/";

    public static final String HELPFILE = STRATDIR + "strathlp.txt";

    public String ICCHost = "chessclub.com";

    public String ICCUser = "StrategoBot";

    public String ICCPass = "****";

    public int ICCChan = 128, ICCPort = 5000, LocPort = 0;

    public static final String INIBOARD = "+ ", ENDBOARD = "~ ", NEWBOARD = "! ", DMPBOARD = "@ ", LSTBOARD = "? ";

    public Vector<String> GUI;

    public static void main(String[] a) throws UnknownHostException {
        GameBase.DATAFILE = STRATDIR + "stratdat.txt";
        StratBot S = new StratBot(a);
        System.out.println(S.log.toString());
        S.log.println(VERSION);
    }

    public StratBot(String[] a) throws UnknownHostException {
        super(BOT_NAME, "DuckStorm");
        parseArgs(a);
        if (LocPort > 0) Serv = new DualServ(LocPort); else {
            final boolean isTD = false;
            Serv = new DualServ(ICCChan, ICCHost, ICCPort, ICCUser, ICCPass, isTD);
            Serv.start();
            Serv.send("-ch *");
            advertiser.start();
        }
        GUI = new Vector<String>();
        GameData.initData(StroBase.initFields());
        StroBase.Bot = this;
        GameBase.CR = Serv.CR;
        GameBase.editStats(new GameData(StroBase.newPlayer("Zippy")));
    }

    @Override
    public void advertise() {
        Serv.shout("Play Stratego on ICC! 'finger StrategoBot' " + "or 'tell StrategoBot help' for details.");
    }

    public void parseArgs(String[] a) {
        if (a.length < 4) return;
        ICCHost = a[0];
        ICCPort = MiscUtil.strToInt(a[1]);
        ICCUser = a[2];
        ICCPass = a[3];
        LocPort = 0;
    }

    @Override
    public void croak(String msg) {
        log.close();
        Serv.tch(msg);
        System.exit(-1);
    }

    @Override
    public void addBoard(String creator) {
        boards.addElement(new Stratego(creator, this));
    }

    @Override
    public void gameTell(String handle, String msg) {
        if (!msg.equals("")) log.println(handle + ": " + msg);
        if (cmdTell(handle, msg)) return;
        StringTokenizer ST = new StringTokenizer(msg);
        int t = ST.countTokens();
        String m = null;
        if (t > 0) m = ST.nextToken(); else m = "";
        Stratego s = locate(handle);
        if (s == null) s = getObs(handle);
        SPlayer p = null;
        if (s != null) p = s.locate(handle);
        if (msg.equalsIgnoreCase("HELP")) {
            List<String> helpvec = FileHelper.readFile(HELPFILE);
            for (int v = 0; v < helpvec.size(); v++) Serv.say(handle, helpvec.get(v));
        } else if (msg.equalsIgnoreCase("VER")) {
            Serv.say(handle, VERSION);
        } else if (msg.equalsIgnoreCase("GUI")) {
            if (!GUI.contains(handle)) GUI.addElement(handle);
            Serv.say(handle, "GUI set.");
        } else if (msg.equalsIgnoreCase("!GUI")) {
            GUI.removeElement(handle);
            Serv.say(handle, "GUI unset.");
        } else if (s != null && msg.equalsIgnoreCase(LSTBOARD)) {
            s.showHist(handle);
        } else if (msg.equalsIgnoreCase("TOPTEN")) {
            Serv.qSay(handle, GameBase.topTen("wins"));
        } else if (t > 1 && m.equalsIgnoreCase("TOPTEN")) {
            Serv.qSay(handle, GameBase.topTen(ST.nextToken()));
        } else if (msg.equalsIgnoreCase("FINGER")) {
            Serv.qSay(handle, StroBase.statLine(GameBase.getStats(handle, null)));
        } else if (t > 1 && m.equalsIgnoreCase("FINGER")) {
            Serv.qSay(handle, StroBase.statLine(GameBase.getStats(ST.nextToken(), null)));
        } else if (p != null && p.isWaiting()) {
            p.RESPONSE = msg;
            p.wakeup();
        } else Serv.say(handle, "Excuse me?");
    }

    public Stratego locate(String p) {
        return getPlayer(p);
    }
}
