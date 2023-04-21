package jbomberman.client.game;

import jbomberman.util.StringTokenizer;

/**
 * GameInfo.java
 *
 * This class holds all the information of a running game.
 *
 * @author Wolfgang Schriebl
 * @version 1.0
 */
public class GameInfo {

    /**
   * Holds the name of the game
   */
    private String name_;

    /**
   * Holds weither the game is opened or not
   */
    private boolean is_open_;

    /**
   * Holds the number of players
   */
    private int num_players_;

    /**
   * Holds the level-string
   */
    private String level_;

    /**
   * Holds the ruleset-string
   */
    private String ruleset_;

    /**
   * Creates a new <code>GameInfo</code> and initializes it with null-values.
   */
    public GameInfo() {
        this(null, false, 0, null, null);
    }

    /**
   * Creates a new <code>GameInfo</code> and initializes it with the given parameters
   * @param name Name to initialize
   * @param is_open Is-open-state to initialize
   * @param num_players Number of players to initialize
   * @param level Level-String to initialize
   * @param ruleset Ruleset-String to initialize
   */
    public GameInfo(String name, boolean is_open, int num_players, String level, String ruleset) {
        name_ = name;
        is_open_ = is_open;
        num_players_ = num_players;
        level_ = level;
        ruleset_ = ruleset;
    }

    /**
   * Creates a new <code>GameInfo</code>. The parameters are read from one string.
   * @param info_as_one String which holds the parameters, devided with commas.
   */
    public GameInfo(String info_as_one) {
        String[] infos = new StringTokenizer(info_as_one, ",", false).getStrings();
        if (infos.length < 5) {
            return;
        }
        name_ = infos[0];
        is_open_ = Boolean.getBoolean(infos[1]);
        num_players_ = Integer.parseInt(infos[2]);
        level_ = infos[3];
        ruleset_ = infos[4];
    }

    /**
   * Get the name of the game
   * @return Returns the name
   */
    public String getName() {
        return name_;
    }

    /**
   * Get the is-open-state
   * @return Returns the is-open-state
   */
    public boolean isOpen() {
        return is_open_;
    }

    /**
   * Get the number of players
   * @return Returns the number of players
   */
    public int getNumPlayers() {
        return num_players_;
    }

    /**
   * Get the level
   * @return Returns the level-descriptor
   */
    public String getLevel() {
        return level_;
    }

    /**
   * Get the ruleset
   * @return returns the ruleset-descriptor
   */
    public String getRuleset() {
        return ruleset_;
    }

    /**
   *
   */
    public String toString() {
        return name_;
    }
}
