package org.chernovia.net.iccbots.analyzer;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.chernovia.lib.misc.MiscUtil;
import org.chessworks.common.javatools.io.FileHelper;

public class ICCGame implements Comparable<ICCGame> {

    public static int MAX_LIB_LIST = 20;

    public static final int SORT_BY_RATING = 0, SORT_BY_OBS = 1, SORT_BY_KIB = 2, SORT_BY_FLUX = 3, MAXSORT = 4;

    public static int sortcode = SORT_BY_RATING;

    Vector<Ply> movelist;

    Vector<Kibitz> kiblist;

    String wname, bname, result, rescode;

    float flux;

    long id;

    int num, len = 0, wrating = -1, brating = -1, maxobs = -1, time = -1, inc = -1;

    public ICCGame(int n, long gameID, String whiteName, int whiteRating, String blackName, int blackRating, int whiteInitial, int whiteIncrement, int m) {
        num = n;
        id = gameID;
        time = whiteInitial;
        inc = whiteIncrement;
        wname = whiteName;
        wrating = whiteRating;
        bname = blackName;
        brating = blackRating;
        movelist = new Vector<Ply>();
        kiblist = new Vector<Kibitz>();
        maxobs = m;
    }

    public ICCGame(List<String> gamefile, int kibidx) {
        StringTokenizer ST = new StringTokenizer(gamefile.get(kibidx - 1));
        id = MiscUtil.strToLong(ST.nextToken().substring(1));
        wrating = MiscUtil.strToInt(ST.nextToken());
        brating = MiscUtil.strToInt(ST.nextToken());
        flux = MiscUtil.strToFloat(ST.nextToken());
        if (flux < 0) flux = 0;
        maxobs = MiscUtil.strToInt(ST.nextToken());
        wname = ST.nextToken();
        bname = ST.nextToken();
        result = ST.nextToken();
        rescode = ST.nextToken();
        len = MiscUtil.strToInt(ST.nextToken());
        kiblist = new Vector<Kibitz>();
        movelist = null;
        String kibline = gamefile.get(kibidx);
        int knum = MiscUtil.strToInt(kibline);
        for (int k = 1; k <= knum; k++) {
            String S = gamefile.get(kibidx + k);
            StringTokenizer KibToken = new StringTokenizer(S);
            int movenum = MiscUtil.strToInt(KibToken.nextToken());
            String player = KibToken.nextToken();
            String titles = KibToken.nextToken();
            boolean isKib = Boolean.getBoolean(KibToken.nextToken());
            StringBuffer SB = new StringBuffer();
            while (KibToken.hasMoreTokens()) SB.append(KibToken.nextToken() + " ");
            kiblist.add(new Kibitz(movenum, player, titles, isKib, SB.toString()));
        }
    }

    @Override
    public String toString() {
        NumberFormat NF = NumberFormat.getNumberInstance();
        NF.setMinimumFractionDigits(3);
        return "#" + id + " " + wname + "(" + wrating + ") vs (" + brating + ")" + bname + ", " + maxobs + "/" + kiblist.size() + "/" + NF.format(flux);
    }

    public String longSummary() {
        NumberFormat NF = NumberFormat.getNumberInstance();
        NF.setMinimumFractionDigits(3);
        return wname + "(" + wrating + ") vs (" + brating + ")" + bname + ", " + maxobs + "/" + kiblist.size() + "/" + NF.format(flux);
    }

    public String summary() {
        NumberFormat NF = NumberFormat.getNumberInstance();
        NF.setMinimumFractionDigits(3);
        return ((wrating + brating) / 2) + " / " + maxobs + " / " + kiblist.size() + " / " + NF.format(flux);
    }

    public void toFile(String filename) {
        len = movelist.size();
        FileHelper.addToFile("#" + id + " " + wrating + " " + brating + " " + flux + " " + maxobs + " " + wname + " " + bname + " " + result + " " + rescode + " " + len, filename);
        StringBuffer SB = new StringBuffer("" + kiblist.size());
        for (int k = 0; k < kiblist.size(); k++) {
            Kibitz K = kiblist.elementAt(k);
            if (K.titles.equals("")) K.titles = "NT";
            SB.append(LogBot.CR + K.movenum + " " + K.player + " " + K.titles + " " + K.isKib + " " + K.kib);
        }
        FileHelper.addToFile(SB.toString(), filename);
    }

    public int compareTo(ICCGame G) {
        if (!this.getClass().equals(G.getClass())) return -1;
        switch(sortcode) {
            case SORT_BY_RATING:
                float rating1 = (G.wrating + G.brating) / 2;
                float rating2 = (wrating + brating) / 2;
                if (rating1 > rating2) return 1; else if (rating1 < rating2) return -1; else return 0;
            case SORT_BY_OBS:
                if (G.maxobs > maxobs) return 1; else if (G.maxobs < maxobs) return -1; else return 0;
            case SORT_BY_KIB:
                if (G.kiblist.size() > kiblist.size()) return 1; else if (G.kiblist.size() < kiblist.size()) return -1; else return 0;
            case SORT_BY_FLUX:
                if (G.flux > flux) return 1; else if (G.flux < flux) return -1; else return 0;
            default:
                return 0;
        }
    }

    public static ICCGame[] getGames(String gamefile) {
        List<String> GameData = FileHelper.readFile(gamefile);
        Vector<ICCGame> GameList = new Vector<ICCGame>();
        int gameidx = 1;
        while (gameidx < GameData.size()) {
            gameidx++;
            ICCGame G = new ICCGame(GameData, gameidx);
            GameList.add(G);
            int knum = MiscUtil.strToInt(GameData.get(gameidx)) + 1;
            gameidx += knum;
        }
        ICCGame[] games = new ICCGame[GameList.size()];
        GameList.toArray(games);
        return games;
    }

    public static ICCGame[] getLib(String logdate) {
        ICCGame[] games = getGames(logdate + "log.txt");
        int listlen = games.length;
        if (listlen > MAX_LIB_LIST) listlen = MAX_LIB_LIST;
        ICCGame[] libgames = new ICCGame[listlen * ICCGame.MAXSORT];
        for (int s = 0; s < ICCGame.MAXSORT; s++) {
            ICCGame.sortcode = s;
            Arrays.sort(games);
            for (int g = 0; g < listlen; g++) {
                int l = g + (s * listlen);
                libgames[l] = games[g];
            }
        }
        return libgames;
    }
}
