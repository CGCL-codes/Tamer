package com.usoog.hextd;

import java.awt.Color;
import java.text.DecimalFormat;

/**
 * Constants used by HexTD
 */
public class Constants {

    /**
	 * The maximum amount a rank can change due to one won or lost game.
	 */
    public static final int ELO_MAX_CHANGE = 25;

    /**
	 * Different status types for authentication.
	 */
    public enum AuthStatus {

        unknown, success, failed
    }

    /**
	 * Error types that the server can issue
	 */
    public static enum ErrorType {

        Unknown, AuthFailed, ReplayLoadFailed
    }

    /**
	 * Game settings types.
	 */
    public static enum settingKey {

        ladderGame, openGame, pause, publicGame, ready, mapId, mapMD5, version
    }

    /**
	 * Types of fetch messages
	 */
    public enum FetchType {

        mp, sp, maplist, map
    }

    /**
	 * Game colors of creeps and towers
	 */
    public static enum COLOR {

        RED, GREEN, BLUE, YELLOW, OTHER
    }

    /**
	 * Damage types of the different towers
	 */
    public static enum DamageType {

        normal, speed, power, time
    }

    /**
	 * Directions on the compass
	 */
    public static enum Direction {

        NORTH, EAST, SOUTH, WEST, UP, DOWN, LEFT, RIGHT
    }

    /**
	 * The different ways to grade towers.
	 */
    public static enum TowerUpgradeModes {

        FULL, NORMAL
    }

    /**
	 * Key for the credit resource
	 */
    public static String RESOURCE_CREDIT = "c";

    /**
	 * Key for the bonus-point resource
	 */
    public static String RESOURCE_BONUS = "b";

    /**
	 * Key for the tower resource
	 */
    public static String RESOURCE_TOWER = "t";

    /**
	 * Key for the score "resource". Not really a resource, but the score
	 * rewarded for killing stuff.
	 */
    public static String RESOURCE_SCORE = "s";

    /**
	 * Winning player is for a game that was not checked yet.
	 */
    public static int WINNER_UNKNOWN = -1;

    /**
	 * Winning player is for a game that was lost by the player(s).
	 */
    public static int WINNER_COMPUTER = -2;

    /**
	 * Winning player is for a game that ended in a draw.
	 */
    public static int WINNER_DRAW = -3;

    /**
	 * Formatter used to format most numbers.
	 */
    public static DecimalFormat formatter = new DecimalFormat("#,##0");

    public static final Color colorBaseBackGround = new java.awt.Color(0, 0, 0);

    public static final Color colorOpenBackGround = new java.awt.Color(50, 0, 25);

    public static final Color colorBaseHexBorder = new java.awt.Color(183, 183, 244);

    public static final Color colorBaseHexBackGround = new java.awt.Color(33, 54, 75);

    public static final Color colorBaseEmptyHexBorder = new java.awt.Color(56, 149, 185);

    public static final Color colorHighlightOK = new java.awt.Color(213, 255, 213);

    public static final Color colorHighlightNOK = new java.awt.Color(255, 0, 0);

    public static final Color colorRangeCircle = new java.awt.Color(204, 204, 204);

    public static final Color colorHealthLine = new java.awt.Color(0, 199, 0);

    public static final Color colorForeGround = new java.awt.Color(220, 255, 220);

    public static final Color colorForeGroundPale = new java.awt.Color(184, 207, 229);

    public static final Color colorBackGround = new java.awt.Color(40, 45, 50);

    public static final Color colorBackGroundDark = Color.BLACK;

    public static final Color[] playerColors = { new Color(255, 100, 100), new Color(100, 255, 100), new Color(100, 100, 255), new Color(255, 255, 100), new Color(255, 100, 255), new Color(100, 255, 255), new Color(255, 000, 000), new Color(000, 255, 000), new Color(000, 000, 255), new Color(255, 255, 000), new Color(255, 000, 255), new Color(000, 255, 255) };

    /**
	 * 50% transparent player colors.
	 */
    public static Color[] playerColorsAlpha50;

    static {
        playerColorsAlpha50 = new Color[playerColors.length];
        for (int i = 0; i < playerColors.length; i++) {
            Color c = playerColors[i];
            playerColorsAlpha50[i] = new Color(c.getRed(), c.getGreen(), c.getBlue(), 128);
        }
    }

    public static Color getColorPlayer(int player) {
        if (player >= playerColors.length) {
            player %= playerColors.length;
        }
        return playerColors[player];
    }

    public static Color getColorPlayerAlpha(int player) {
        if (player >= playerColorsAlpha50.length) {
            player %= playerColorsAlpha50.length;
        }
        return playerColorsAlpha50[player];
    }
}
