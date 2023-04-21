package com.yennick.fighter.bot;

import java.util.Arrays;
import java.util.List;

public class Constants {

    private static final List<String> availCharacteristics = Arrays.asList("kickPower", "kickReach", "punchPower", "punchReach");

    private static final List<String> availConditions = Arrays.asList("always", "near", "far", "weaker", "much_weaker", "even", "stronger", "much_stronger");

    private static final List<String> availMoveActions = Arrays.asList("stand", "crouch", "jump", "run_towards", "walk_towards", "run_away", "walk_away");

    private static final List<String> availFightActions = Arrays.asList("block_low", "block_high", "kick_low", "kick_high", "punch_low", "punch_high");

    public static List<String> getCharacteristics() {
        return availCharacteristics;
    }

    public static List<String> getConditions() {
        return availConditions;
    }

    public static List<String> getMoveActions() {
        return availMoveActions;
    }

    public static List<String> getFightActions() {
        return availFightActions;
    }
}
