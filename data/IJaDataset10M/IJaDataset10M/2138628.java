package net.sourceforge.queried.gametypes;

import java.util.ArrayList;
import java.util.Collections;
import net.sourceforge.queried.PlayerInfo;
import net.sourceforge.queried.ScoreComparator;
import net.sourceforge.queried.ServerInfo;
import net.sourceforge.queried.Util;

public class BF2142ServerInfo {

    public static ServerInfo getDetails(int localPort, String ipStr, int port, int infoType, int queryType, int gameType) {
        String queryResult = Util.getInfo(localPort, ipStr, port, infoType, queryType, gameType);
        ServerInfo serverInfo = null;
        if (queryResult != null && queryResult.length() > 0) {
            serverInfo = new ServerInfo();
            serverInfo.setGame(Util.getPartGS2(queryResult, "gamename"));
            serverInfo.setGameVersion(Util.getPartGS2(queryResult, "gamever"));
            serverInfo.setIp(ipStr);
            serverInfo.setPort(Util.getPartGS2(queryResult, "hostport"));
            serverInfo.setName(Util.getPartGS2(queryResult, "hostname"));
            serverInfo.setMap(Util.getPartGS2(queryResult, "mapname"));
            serverInfo.setPlayerCount(Util.getPartGS2(queryResult, "numplayers"));
            serverInfo.setMaxPlayers(Util.getPartGS2(queryResult, "maxplayers"));
            serverInfo.setFullResponse(queryResult);
        }
        return serverInfo;
    }

    public static ArrayList getPlayers(int localPort, String ipStr, int port, int infoType, int queryType, int gameType) {
        String recStr = Util.getInfo(localPort, ipStr, port, infoType, queryType, gameType);
        ArrayList playerInfo = new ArrayList();
        String[] players = assembleParts(recStr, "player_");
        if (players == null || players.length == 0) {
            return playerInfo;
        }
        String[] scores = assembleParts(recStr, "score_");
        String[] deaths = assembleParts(recStr, "deaths_");
        String[] skills = assembleParts(recStr, "skill_");
        try {
            for (int i = 0; i < players.length; i++) {
                PlayerInfo player = new PlayerInfo();
                player.setName(players[i]);
                player.setScore(Integer.valueOf(scores[i]).intValue());
                player.setDeaths(Integer.valueOf(deaths[i]).intValue());
                player.setKills(Integer.valueOf(skills[i]).intValue());
                playerInfo.add(player);
            }
        } catch (ArrayIndexOutOfBoundsException aiex) {
        } catch (NumberFormatException nfex) {
        } catch (StringIndexOutOfBoundsException sobex) {
        }
        Collections.sort(playerInfo, new ScoreComparator());
        return playerInfo;
    }

    private static String[] assembleParts(String recStr, String markerString) {
        char chr = 00;
        String marker = markerString + chr;
        boolean search = true;
        int start = 0;
        int end = 0;
        String chunk = "";
        String[] retArray = null;
        while (search) {
            start = recStr.indexOf(marker, start) + marker.length() + 1;
            end = recStr.indexOf(chr + "" + chr, start);
            if (end <= 0) {
                end = recStr.length();
            }
            if (start == marker.length()) {
                search = false;
            }
            if (search) {
                chunk = recStr.substring(start, end);
                if (retArray == null || retArray.length == 0) {
                    retArray = chunk.split(chr + "");
                } else {
                    String[] tmpArray = chunk.split(chr + "");
                    String[] copyArray = (String[]) retArray.clone();
                    retArray = new String[tmpArray.length + copyArray.length - 1];
                    System.arraycopy(copyArray, 0, retArray, 0, copyArray.length - 1);
                    System.arraycopy(tmpArray, 0, retArray, copyArray.length - 1, tmpArray.length);
                }
                start = end;
            }
        }
        if (retArray == null) {
            return null;
        }
        return (String[]) retArray.clone();
    }
}
