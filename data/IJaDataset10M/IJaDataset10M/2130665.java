package de.altitude.cimuelle.utils;

import de.altitude.cimuelle.Config;
import de.altitude.cimuelle.MessageBundle;
import de.altitude.cimuelle.coop.CoopGame;
import de.altitudecustomcommands.Player;
import de.altitudecustomcommands.exceptions.ServerNotFoundException;
import de.altitudecustomcommands.playerHandler;
import de.altitudecustomcommands.serverInformations;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.*;

/**
 * Class description:
 * <p/>
 * User: cybernaut
 * Date: 07.10.11
 * Time: 22:47
 */
public class ServerUtils {

    private static final Logger LOGGER = Logger.getLogger("ServerUtils");

    public static boolean checkCountingRequirements(String port, playerHandler playerList) {
        return playerList.getPlayersByPort(port).length > Integer.parseInt(Config.getInstance().getConfigByPort(port, "minimumPlayers", "4"));
    }

    /**
     * @param port       String
     * @param server     server
     * @param playerList playerHandler
     * @param coopGame   CoopGame
     * @return boolean
     */
    public static boolean checkCountingRequirementsForCoop(String port, serverInformations server, playerHandler playerList, CoopGame coopGame) {
        try {
            boolean configActive = Config.getInstance().isCoopRankingActiveByPort(port);
            boolean leftSiteHasHumanPlayers = false;
            int leftTeam = server.getServerByPort(port).getLeftTeam();
            for (Player p : playerList.getPlayersByPort(port)) {
                if (!p.playerIsBot() && p.getTeam() == leftTeam) {
                    leftSiteHasHumanPlayers = true;
                    break;
                }
            }
            if (configActive && leftSiteHasHumanPlayers) {
                LOGGER.info(port + ":Requirements for coop counting met." + ((coopGame != null) ? "Still checking coop requirements..." : ""));
                return coopGame == null || checkAdditionalCoopConfig(port, coopGame);
            }
            LOGGER.debug(port + ":leftSiteHasHumanPlayers: " + leftSiteHasHumanPlayers);
            LOGGER.debug(port + ":configActive: " + configActive);
        } catch (ServerNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkAdditionalCoopConfig(final String port, final CoopGame coopGame) {
        ServerOperations.sendServerMessage(port, MessageBundle.getString("scoopRequirementCheck1"));
        final String mapName = coopGame.getMap().getMap();
        try {
            final Properties coopProperty;
            coopProperty = Config.getInstance().getCoopConfig(port, mapName);
            if (coopProperty != null) {
                new Thread() {

                    @Override
                    public void run() {
                        boolean check = true;
                        try {
                            sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int minimumPlayers = Integer.parseInt(coopProperty.getProperty("minimumPlayer", "0"));
                        int players = coopGame.getLeftTeamPlayersAsList().size();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("MINIMUMPLAYER", String.valueOf(minimumPlayers));
                        map.put("CURRENTPLAYERS", String.valueOf(coopGame.getLeftTeamPlayersAsList().size()));
                        if (players < minimumPlayers) {
                            String message = MessageBundle.getString("scoopRequirementCheck2", map);
                            ServerOperations.sendServerMessage(port, message);
                            LOGGER.info(message);
                            check = false;
                        }
                        check = checkMinCoopProperty(port, coopProperty, "Loopy", coopGame.getLoopyCount(), check);
                        check = checkMinCoopProperty(port, coopProperty, "Explodet", coopGame.getExplodetCount(), check);
                        check = checkMinCoopProperty(port, coopProperty, "Bomber", coopGame.getBomberCount(), check);
                        check = checkMinCoopProperty(port, coopProperty, "Biplane", coopGame.getBiplaneCount(), check);
                        check = checkMinCoopProperty(port, coopProperty, "Miranda", coopGame.getMirandaCount(), check);
                        if (check) {
                            coopGame.saveDatabaseInit();
                            map.put("ID", String.valueOf(coopGame.getDatabaseId()));
                            ServerOperations.sendServerMessage(port, MessageBundle.getString("scoopRequirementCheck3", map));
                        } else {
                            ServerOperations.sendServerMessage(port, MessageBundle.getString("scoopRequirementCheck4", map));
                            coopGame.delete();
                        }
                    }
                }.start();
                return true;
            } else {
                coopGame.saveDatabaseInit();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("CURRENTPLAYERS", String.valueOf(coopGame.getLeftTeamPlayersAsList().size()));
                map.put("ID", String.valueOf(coopGame.getDatabaseId()));
                LOGGER.info(port + ":Additional config not found. Overriding and starting game...");
                ServerOperations.sendServerMessage(port, MessageBundle.getString("scoopRequirementCheck5", map));
                LOGGER.info(port + ":CoopGame started with id " + coopGame.getDatabaseId());
            }
        } catch (IOException e) {
            LOGGER.error(port + ":" + e, e);
        }
        return false;
    }

    private static boolean checkMinCoopProperty(String port, Properties coopProperty, String plane, int count, boolean check) {
        int expected = Integer.parseInt(coopProperty.getProperty("min" + plane, "0"));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("AMOUNTEXPECTED", String.valueOf(expected));
        map.put("AMOUNTFOUND", String.valueOf(count));
        map.put("PLANE", plane);
        if (expected > count) {
            LOGGER.info(port + " min" + plane + " requirement not met, expected " + expected + " but found only " + count + ".");
            ServerOperations.sendServerMessage(port, MessageBundle.getString("scoopMinimumRequirementCheck1", map));
            return false;
        }
        LOGGER.info(port + " min" + plane + " requirement met, expected " + expected + " and found " + count + ".");
        return check;
    }

    @SuppressWarnings({ "unchecked" })
    public static void typeLogServerStatus(Map json, String commandPort, playerHandler playerList) {
        List<String> vapors = (LinkedList<String>) json.get("vaporIds");
        List<String> nicknames = (LinkedList<String>) json.get("nicknames");
        List<String> ips = (LinkedList<String>) json.get("ips");
        List<Number> playerIds = (LinkedList<Number>) json.get("playerIds");
        if (vapors.size() > 0) {
            for (int i = 0; i < vapors.size(); i++) {
                playerList.addPlayer(new Player(nicknames.get(i), vapors.get(i), ips.get(i), commandPort, playerIds.get(i).intValue()));
            }
        }
    }
}
