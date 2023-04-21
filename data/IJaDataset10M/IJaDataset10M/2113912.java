package net.sf.colossus.client;

import java.util.ArrayList;
import java.util.List;
import net.sf.colossus.game.BattlePhase;
import net.sf.colossus.game.BattleUnit;
import net.sf.colossus.game.Game;
import net.sf.colossus.game.Legion;
import net.sf.colossus.game.Player;
import net.sf.colossus.util.Split;
import net.sf.colossus.variant.MasterHex;
import net.sf.colossus.variant.Variant;

public class GameClientSide extends Game implements IOracle {

    private Client client;

    /**
     * This is used as a placeholder for activePlayer and battleActivePlayer since they
     * are sometimes accessed when they are not available.
     *
     * TODO this is a hack. Those members should just not be accessed at times where they
     * are not available. It seems to happen during startup (the not yet set case) and in
     * some GUI parts after battles, when battleActivePlayer has been reset already.
     */
    private final PlayerClientSide noone;

    private Player activePlayer;

    public GameClientSide(Variant variant, String[] playerNames) {
        super(variant, playerNames);
        this.noone = new PlayerClientSide(this, "", 0);
        this.activePlayer = noone;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PlayerClientSide initPlayerInfo(List<String> infoStrings, String searchedName) {
        int numPlayers = infoStrings.size();
        PlayerClientSide owningPlayer = null;
        for (int i = 0; i < numPlayers; i++) {
            List<String> data = Split.split(":", infoStrings.get(i));
            String playerName = data.get(1);
            PlayerClientSide player = new PlayerClientSide(this, playerName, i);
            addPlayer(player);
            if (playerName.equals(searchedName)) {
                owningPlayer = player;
            }
        }
        return owningPlayer;
    }

    public Player getNoonePlayer() {
        return noone;
    }

    public void updatePlayerInfo(List<String> infoStrings) {
        for (int i = 0; i < infoStrings.size(); i++) {
            PlayerClientSide player = (PlayerClientSide) players.get(i);
            player.update(infoStrings.get(i));
        }
    }

    /**
     * Resolve playerName into Player object. Name might be null,
     * then returns null.
     * @param playerName
     * @return The player object for given player name, null if name was null
     */
    Player getPlayerByNameIgnoreNull(String playerName) {
        if (playerName == null) {
            return null;
        } else {
            return getPlayerByName(playerName);
        }
    }

    /**
     * Resolve playerName into Player object.
     * Name must not be null. If no player for given name found,
     * it would throw IllegalArgumentException
     * @param playerName
     * @return Player object for given name.
     */
    Player getPlayerByName(String playerName) {
        assert playerName != null : "Name for player to find must not be null!";
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        throw new IllegalArgumentException("No player object found for name '" + playerName + "'");
    }

    public Player getPlayerByTag(int tag) {
        BattleUnit battleUnit = getBattleCS().getBattleUnit(tag);
        assert battleUnit != null : "Illegal value for tag parameter";
        return battleUnit.getLegion().getPlayer();
    }

    private Player getPlayerUsingColor(String shortColor) {
        assert this.players.size() > 0 : "Client side player list not yet initialized";
        assert shortColor != null : "Parameter must not be null";
        for (Player info : players) {
            if (shortColor.equals(info.getShortColor()) && !info.isDead()) {
                return info;
            }
        }
        for (Player info : players) {
            if (info.getPlayersElim().indexOf(shortColor) != -1) {
                if (!info.isDead()) {
                    return info;
                } else {
                    return getPlayerUsingColor(info.getShortColor());
                }
            }
        }
        return null;
    }

    public Player getPlayerByMarkerId(String markerId) {
        assert markerId != null : "Parameter must not be null";
        String shortColor = markerId.substring(0, 2);
        return getPlayerUsingColor(shortColor);
    }

    /** Return the average point value of all legions in the game. */
    public int getAverageLegionPointValue() {
        int totalValue = 0;
        int totalLegions = 0;
        for (Player player : players) {
            totalLegions += player.getLegions().size();
            totalValue += player.getTotalPointValue();
        }
        return (int) (Math.round((double) totalValue / totalLegions));
    }

    @Override
    public Legion getLegionByMarkerId(String markerId) {
        return client.getLegion(markerId);
    }

    public void setActivePlayer(Player player) {
        activePlayer = player;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * ActivePlayer is set first time to something different than "noone"
     * when setupTurnState is called first time.
     * @return true if activePlayer is still "noone".
     */
    public boolean isTurnStateStillUninitialized() {
        return activePlayer.equals(noone);
    }

    public void initBattle(MasterHex hex, int battleTurnNumber, Player battleActivePlayer, BattlePhase battlePhase, Legion attacker, Legion defender) {
        this.battle = new BattleClientSide(this, attacker, defender, hex);
        getBattleCS().init(battleTurnNumber, battleActivePlayer, battlePhase);
    }

    public BattleClientSide getBattleCS() {
        return (BattleClientSide) battle;
    }

    public boolean isBattleOngoing() {
        return battle != null;
    }

    public BattlePhase getBattlePhase() {
        assert battle != null : "No battle phase if there is no battle!";
        return getBattleCS().getBattlePhase();
    }

    public void setBattlePhase(BattlePhase battlePhase) {
        getBattleCS().setBattlePhase(battlePhase);
    }

    public boolean isBattlePhase(BattlePhase phase) {
        return getBattleCS().getBattlePhase() == phase;
    }

    public void setBattleActivePlayer(Player battleActivePlayer) {
        getBattleCS().setBattleActivePlayer(battleActivePlayer);
    }

    public void setBattleTurnNumber(int battleTurnNumber) {
        getBattleCS().setBattleTurnNumber(battleTurnNumber);
    }

    @Override
    public int getBattleTurnNumber() {
        return getBattleCS().getBattleTurnNumber();
    }

    public Player getBattleActivePlayer() {
        if (battle == null) {
            return null;
        }
        return getBattleCS().getBattleActivePlayer();
    }

    public void cleanupBattle() {
        if (battle != null) {
            getBattleCS().cleanupBattle();
            battle = null;
        }
    }

    public List<String> getLegionImageNames(Legion legion) {
        LegionClientSide info = (LegionClientSide) legion;
        if (info != null) {
            return info.getImageNames();
        }
        return new ArrayList<String>();
    }

    public List<Boolean> getLegionCreatureCertainties(Legion legion) {
        LegionClientSide info = (LegionClientSide) legion;
        if (info != null) {
            return info.getCertainties();
        } else {
            List<Boolean> l = new ArrayList<Boolean>(10);
            for (int idx = 0; idx < 10; idx++) {
                l.add(Boolean.valueOf(true));
            }
            return l;
        }
    }
}
