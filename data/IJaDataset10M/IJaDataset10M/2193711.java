package org.andrewwinter.jalker.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.andrewwinter.jalker.OnlinePlayer;
import org.andrewwinter.jalker.Player;
import org.andrewwinter.jalker.Privilege;
import org.andrewwinter.jalker.Util;

public abstract class AbstractMultipleOnlinePlayersCmd extends Cmd {

    private final int minNumOfPlayers;

    private final int maxNumOfPlayers;

    /**
     * @param names
     * @param privileges
     * @param minNumOfPlayers
     * @param maxNumOfPlayers Maximum number of players or
     *   zero for no maximum number.
     */
    public AbstractMultipleOnlinePlayersCmd(String[] names, Privilege[] privileges, int minNumOfPlayers, int maxNumOfPlayers) {
        super(names, privileges);
        if (minNumOfPlayers < 1 || maxNumOfPlayers < 0) {
            throw new IllegalArgumentException("Illegal number of players.");
        }
        if (minNumOfPlayers < maxNumOfPlayers || maxNumOfPlayers == 0) {
            this.minNumOfPlayers = minNumOfPlayers;
            this.maxNumOfPlayers = maxNumOfPlayers;
        } else {
            this.minNumOfPlayers = maxNumOfPlayers;
            this.maxNumOfPlayers = minNumOfPlayers;
        }
    }

    /**
     * @return
     */
    private String wrongNumberOfPlayersError() {
        if (minNumOfPlayers == 1) {
            if (maxNumOfPlayers == 0) {
                return "You need to give the name of at least one online person.";
            } else if (maxNumOfPlayers == 1) {
                return "You need to give the name of an online person.";
            } else {
                return "You need to give the names of between one and " + Util.intInWords(maxNumOfPlayers) + " online people.";
            }
        } else {
            if (maxNumOfPlayers == 0) {
                return "You need to give the names of at least " + Util.intInWords(minNumOfPlayers) + " online people.";
            } else if (maxNumOfPlayers == minNumOfPlayers) {
                return "You need to give the names of " + Util.intInWords(minNumOfPlayers) + " online people.";
            } else {
                return "You need to give the names of between " + Util.intInWords(minNumOfPlayers) + " and " + Util.intInWords(maxNumOfPlayers) + " online people.";
            }
        }
    }

    /**
     * @param csl Comma-separated list of player names.
     * @return
     */
    private static Set<String> getDistinctPlayerNames(final String csl) {
        final Set<String> result = new HashSet<String>();
        final String[] tokens = csl.split(",");
        for (int i = 0; i < tokens.length; ++i) {
            if (tokens[i] != null && tokens[i].length() > 0) {
                result.add(tokens[i].toLowerCase());
            }
        }
        return result;
    }

    /**
     * @param numOfPlayers
     * @return
     */
    private boolean acceptableNumberOfPlayers(final int numOfPlayers) {
        if (numOfPlayers == 0) {
            return false;
        } else if (numOfPlayers < minNumOfPlayers) {
            return false;
        } else if (numOfPlayers > maxNumOfPlayers && maxNumOfPlayers != 0) {
            return false;
        }
        return true;
    }

    @Override
    protected void execute(OnlinePlayer p, String str) {
        final String[] tokens = str.split("\\s", 2);
        final String commaSeparatedNames = tokens[0];
        if (commaSeparatedNames == null || commaSeparatedNames.length() == 0) {
            p.tellShortMsg(wrongNumberOfPlayersError());
        } else {
            final Set<String> playerNames = getDistinctPlayerNames(commaSeparatedNames);
            if (acceptableNumberOfPlayers(playerNames.size())) {
                final List<OnlinePlayer> onlinePlayers = new ArrayList<OnlinePlayer>();
                boolean allOnlineAndNotMe = true;
                for (final String playerName : playerNames) {
                    final Player targetPlayer = Player.find(playerName);
                    if (targetPlayer == null || !targetPlayer.isOnline()) {
                        p.tellShortMsg("No-one connected by the name of '" + playerName + "'.");
                        allOnlineAndNotMe = false;
                        break;
                    } else if (targetPlayer.equals(p.getPlayer())) {
                        p.tellShortMsg("But you ARE " + playerName + "!");
                        allOnlineAndNotMe = false;
                        break;
                    }
                    onlinePlayers.add(targetPlayer.getOnlinePlayer());
                }
                if (allOnlineAndNotMe) {
                    str = "";
                    if (tokens.length > 1) {
                        str = tokens[1].trim();
                    }
                    execute(p, onlinePlayers, str);
                }
            } else {
                p.tellShortMsg(wrongNumberOfPlayersError());
            }
        }
    }

    /**
     * @param p
     * @param target
     * @param str Will always be non-null and pre-trimmed. Implementations should
     * check for this being the empty string, if appropriate.
     */
    protected abstract void execute(OnlinePlayer p, List<OnlinePlayer> targets, String str);
}
