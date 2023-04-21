package jbomberman.client.game;

import jbomberman.client.config.ConfigKeys;
import jbomberman.client.config.Configuration;
import jbomberman.game.Command;
import jbomberman.game.RuleSet;
import jbomberman.game.Score;
import jbomberman.util.Log;
import jbomberman.util.Observable;
import java.util.Vector;

/**
 * GameLogic.java
 *
 *
 *
 * @author Wolfgang Schriebl
 * @version 1.0
 */
public class GameLogic extends Observable {

    /**
   *
   */
    public static final byte UPDATE_GAME_LIST = 10;

    public static final byte UPDATE_LEVEL_LIST = 11;

    public static final byte UPDATE_BOT_LIST = 12;

    public static final byte UPDATE_RULESET_LIST = 13;

    public static final byte UPDATE_PLAYER_STATE = 14;

    public static final byte UPDATE_PLAYER_LIST = 15;

    public static final byte UPDATE_MESSAGE = 16;

    /**
   *
   */
    private Game game_;

    /**
   *
   */
    private Vector players_;

    /**
   *
   */
    private Vector messages_;

    /**
   *
   */
    private ClientNetworkAdapter network_;

    /**
  *
  */
    private Protocol protocol_;

    /**
   *
   */
    private Configuration config_;

    /**
   *
   */
    private boolean is_connected_;

    /**
   *
   */
    private boolean is_joined_;

    /**
   *
   */
    private boolean is_running_;

    /**
   *
   */
    private String[] levels_;

    /**
   *
   */
    private String[] rule_sets_;

    /**
   *
   */
    private String[] bots_;

    /**
   *
   */
    private int max_players_;

    /**
   *
   */
    private GameInfo[] game_infos_;

    /**
   *
   */
    private String game_name_;

    /**
   *
   */
    private String player_name_;

    /**
   *
   */
    private byte player_id_;

    /**
   *
   */
    private RuleSet rule_set_;

    /**
   *
   */
    private Score[] score;

    /**
   *
   */
    public GameLogic(Configuration config) {
        config_ = config;
        game_ = null;
        players_ = new Vector(0);
        messages_ = new Vector(0);
        network_ = null;
        protocol_ = null;
        is_connected_ = false;
        is_joined_ = false;
    }

    /**
   *
   */
    public void setNetwork(ClientNetworkAdapter network) {
        network_ = network;
        if (network_ != null) {
            protocol_ = network_.getProtocol();
        }
    }

    /**
   *
   */
    public boolean connect(String hostname) {
        if (network_ == null) {
            return false;
        }
        if (network_.getHost() != null) {
            return false;
        }
        is_connected_ = false;
        return network_.connect(hostname);
    }

    /**
   *
   */
    public boolean isConnected() {
        return is_connected_;
    }

    /**
   *
   */
    public boolean isJoined() {
        return is_joined_;
    }

    /**
   *
   */
    public boolean isRunning() {
        return is_running_;
    }

    /**
   *
   */
    public String[] getBots() {
        return bots_;
    }

    /**
   *
   */
    public String[] getLevels() {
        return levels_;
    }

    /**
   *
   */
    public String[] getRuleSets() {
        return rule_sets_;
    }

    /**
   *
   */
    public GameInfo[] getGameInfos() {
        return game_infos_;
    }

    /**
   *
   */
    public Vector getMessages() {
        return messages_;
    }

    /**
   *
   */
    public byte getLocalPlayerID() {
        return player_id_;
    }

    /**
   *
   */
    public Vector getPlayers() {
        return players_;
    }

    /**
   *
   */
    public void requestLevels() {
        protocol_.getLevelList();
    }

    /**
   *
   */
    public void requestRuleSets() {
        protocol_.getRuleSetList();
    }

    /**
   *
   */
    public void requestGameInfos() {
        protocol_.getGameList();
    }

    /**
   *
   */
    public void requestBots() {
        protocol_.getBotList();
    }

    /**
   *
   */
    public void createGame(String player_name, String game_name, String level_name, String rule_set, int max_players) {
        player_name_ = player_name;
        if (max_players <= 0) {
            max_players = max_players_;
        }
        protocol_.createGame(game_name, level_name, rule_set, max_players);
    }

    /**
   *
   */
    public void joinGame(String game_name, String player_name) {
        player_name_ = player_name;
        game_name_ = game_name;
        player_id_ = -1;
        protocol_.joinGame(game_name, player_name_, jbomberman.server.JBHumanPlayerInfo.TYPE);
    }

    /**
   *
   */
    public void joinBot(String type) {
        protocol_.joinGame(game_name_, type, jbomberman.server.JBBotPlayerInfo.TYPE);
    }

    /**
   *
   */
    public void deleteBot(byte id) {
        protocol_.playerQuit(game_name_, id);
    }

    /**
   *
   */
    public void say(String message) {
        protocol_.sendMessage(game_name_, message, player_id_);
    }

    /**
   *
   */
    public void setReady(boolean ready) {
        protocol_.userReady(game_name_, ready, player_id_);
    }

    /**
   *
   */
    public void setTeam(byte id, byte team) {
        protocol_.setTeam(game_name_, team, id);
    }

    /**
   *
   */
    public void quit() {
        if (is_connected_ && (game_name_ != null)) {
            protocol_.playerQuit(game_name_, player_id_);
        }
        if (network_ != null) {
            network_.disconnect();
        }
        is_connected_ = false;
        is_running_ = false;
        is_joined_ = false;
        update();
    }

    /**
   *
   */
    public void frameUpdate(byte[] frame_data, byte frame_type) {
        if (game_ == null) {
            initGame(frame_data, frame_type);
        } else {
            game_.frameUpdate(frame_data, frame_type, rule_set_);
        }
    }

    /**
   *
   */
    public void gameCreated(String game_name) {
        joinGame(game_name, player_name_);
    }

    /**
   *
   */
    public void gameFinished(Score[] scores) {
        if (game_ != null) {
            game_.quitGame();
            game_ = null;
        }
        if (network_ != null) {
            network_.disconnect();
        }
        is_connected_ = false;
        is_joined_ = false;
        is_running_ = false;
        update();
    }

    /**
   *
   */
    public void gameStarted() {
        game_ = null;
        is_running_ = true;
        update();
    }

    /**
   *
   */
    public void sendMessage(String name, String message) {
        messages_.add(new Message(name, message));
        update(UPDATE_MESSAGE);
        if (game_ != null) {
            game_.messageUpdate();
        }
    }

    /**
   *
   */
    public void setBots(String[] bots) {
        bots_ = bots;
        update(UPDATE_BOT_LIST);
    }

    /**
   *
   */
    public void setLevels(String[] levs) {
        levels_ = levs;
        update(UPDATE_LEVEL_LIST);
    }

    /**
   *
   */
    public void setRuleSets(String[] rule_sets) {
        rule_sets_ = rule_sets;
        update(UPDATE_RULESET_LIST);
    }

    /**
   *
   */
    public void setGames(GameInfo[] game_infos) {
        game_infos_ = game_infos;
        update(UPDATE_GAME_LIST);
    }

    /**
   *
   *
   */
    public void addPlayer(String name, byte id, byte type) {
        if ((player_name_.compareTo(name) == 0) && (player_id_ == -1)) {
            player_id_ = id;
            is_joined_ = true;
            update();
        }
        if (players_.size() <= id) {
            players_.setSize(id + 1);
        }
        players_.setElementAt(new PlayerInfo(id, name, type), id);
        update(UPDATE_PLAYER_LIST);
        if (game_ != null) {
            game_.playerUpdate();
        }
    }

    /**
   *
   *
   */
    public void removePlayer(int id) {
        if (players_.size() > id) {
            players_.set(id, null);
        }
        update(UPDATE_PLAYER_LIST);
        if (game_ != null) {
            game_.playerUpdate();
        }
    }

    /**
   *
   */
    public void setPlayerReady(int id, boolean ready) {
        if (players_.size() > id) {
            ((PlayerInfo) players_.get(id)).setReady(ready);
        }
        update(UPDATE_PLAYER_STATE);
    }

    /**
   *
   *
   */
    public void setPlayerTeam(int player_id, int team_id) {
        if (players_.size() > player_id) {
            ((PlayerInfo) players_.get(player_id)).setTeamID(team_id);
        }
        update(UPDATE_PLAYER_STATE);
    }

    /**
   *
   *
   */
    public void setMaxPlayerNumber(int max_players) {
        max_players_ = max_players;
    }

    /**
   *
   */
    public void setRuleSet(String rule_set_name) {
        rule_set_name = "jbomberman.game.ruleset." + rule_set_name + "RuleSet";
        try {
            rule_set_ = (RuleSet) Class.forName(rule_set_name).newInstance();
        } catch (Exception e) {
            Log.error("GameLogic: RuleSet " + rule_set_name + " not found");
        }
    }

    /**
   *
   */
    public void connectionLost() {
        is_connected_ = false;
        is_joined_ = false;
        is_running_ = false;
        if (game_ != null) {
            game_.quitGame();
            game_ = null;
        }
        update();
    }

    /**
   *
   */
    public void connectingTimedOut() {
        connectionLost();
    }

    /**
   *
   */
    public void connectionEstablished() {
        is_connected_ = true;
        update();
        protocol_.getGameList();
        protocol_.getLevelList();
        protocol_.getMaxPlayerNumber();
        protocol_.getRuleSetList();
        protocol_.getBotList();
    }

    /**
   *
   */
    public void sendCommand(Command command) {
        if (protocol_ != null) {
            protocol_.gameKeyPressed(game_name_, command.getDirection(), command.getButtonStates(), player_id_);
        }
    }

    /**
   *
   */
    public void setTeam(String game_name, byte team_nr, byte player_id) {
        if (protocol_ != null) {
            protocol_.setTeam(game_name, team_nr, player_id);
        }
    }

    /**
   *
   */
    public void update() {
        notifyObservers(null);
    }

    /**
   *
   */
    public void update(byte section) {
        notifyObservers(new Byte(section));
    }

    /**
   *
   */
    private void initGame(byte[] frame_data, byte frame_type) {
        messages_ = new Vector();
        game_ = new Game(config_, null, this);
        game_.loadSkin(config_.getAsString(ConfigKeys.SKIN_CLASS, ""), config_.getAsString(ConfigKeys.RENDERER_PLUGIN, null));
        game_.frameUpdate(frame_data, frame_type, rule_set_);
        protocol_.systemReady(game_name_, player_id_);
        game_.show();
    }
}
