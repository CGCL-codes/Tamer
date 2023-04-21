package fr.fg.client.data;

import com.google.gwt.core.client.JavaScriptObject;

public class FleetData extends JavaScriptObject {

    public static final String FIELD_ID = "a", FIELD_X = "b", FIELD_Y = "c", FIELD_NAME = "d", FIELD_MOVEMENT = "e", FIELD_MOVEMENT_RELOAD = "f", FIELD_MOVEMENT_MAX = "g", FIELD_LAST_MOVE = "i", FIELD_AI = "j", FIELD_LEVEL = "k", FIELD_OWNER = "l", FIELD_ALLY = "m", FIELD_TREATY = "n", FIELD_START_JUMP = "o", FIELD_END_JUMP = "p", FIELD_SKIN = "q", FIELD_PIRATE = "s", FIELD_COLONIZING = "t", FIELD_CAN_JUMP = "u", FIELD_SLOTS = "v", FIELD_SHIPS = "w", FIELD_CLASSES = "x", FIELD_OFFENSIVE_LINK = "y", FIELD_DEFENSIVE_LINK = "z", FIELD_SKILLS = "A", FIELD_XP = "B", FIELD_CAPTURE = "C", FIELD_STEALTH = "D", FIELD_RESERVED = "E", FIELD_NPC_ACTION = "F", FIELD_POWER_LEVEL = "G", FIELD_SCHEDULED_MOVE = "H", FIELD_ALLY_TAG = "I", FIELD_OFFENSIVE_LINKED_COUNT = "J", FIELD_DEFENSIVE_LINKED_COUNT = "K", FIELD_LINE_OF_SIGHT = "L", FIELD_DELUDE = "M", FIELD_SKIRMISH_ABILITIES = "N", FIELD_BATTLE_ABILITIES = "O", FIELD_IMMOBILIZED = "P", FIELD_JUMP_TARGET = "Q", FIELD_VERSION = "R";

    public static final String CLASS_NAME = "FleetData";

    public static final int[] TRAINING_COST = { 0, 4000, 16000, 65000, 250000, 1000000, 3000000, 7500000, 1500000, 22000000, 30000000, 39000000, 46000000, 50000000, 52000000 };

    private static final int[] XP_LEVELS = { 0, 30, 64, 100, 150, 200, 260, 330, 410, 500, 605, 730, 870, 1030, 1210 };

    protected FleetData() {
    }

    public final native int getId();

    public final native int getX();

    public final native int getY();

    public final native String getName();

    public final native int getMovement();

    public final native int getMovementReloadRemainingTime();

    public final native int getMovementMax();

    public final native int getLastMove();

    public final native boolean isAi();

    public final native boolean isImmobilized();

    public final native int getLevel();

    public final native String getOwner();

    public final native String getAlly();

    public final boolean hasAlly() {
        return getAlly().length() > 0;
    }

    public final native String getAllyTag();

    public final boolean hasAllyTag() {
        return getAllyTag().length() > 0;
    }

    public final native String getTreaty();

    public final native int getStartJumpRemainingTime();

    public final native int getEndJumpRemainingTime();

    public final native int getSkin();

    public final native boolean isPirate();

    public final native boolean isCapturing();

    public final native int getColonizationRemainingTime();

    public final native int getJumpReloadRemainingTime();

    public final native boolean hasSlots();

    public final native int getSlotsCount();

    public final native SlotInfoData getSlotAt(int index);

    public final native boolean hasShips();

    public final native int getShipsCount();

    public final native ShipInfoData getShipAt(int index);

    public final native boolean hasClasses();

    public final native int getClassesCount();

    public final native int getClassAt(int index);

    public final native int getOffensiveLinkedFleetId();

    public final native int getDefensiveLinkedFleetId();

    public final native double getOffensiveLinkedCount(int index);

    public final native double getDefensiveLinkedCount(int index);

    public final native int getSkillsCount();

    public final native SkillData getSkillAt(int index);

    public final native int getXp();

    public final native boolean isReserved();

    public final native String getNpcAction();

    public final native int getPowerLevel();

    public final int getFleetLevel() {
        return getFleetLevel(getXp());
    }

    public final native boolean isScheduledMove();

    public final native boolean isStealth();

    public final native boolean isDelude();

    public final native int getLineOfSight();

    public final native int getSkirmishAbilitiesCount();

    public final native int getSkirmishAbilityAt(int index);

    public final native int getBattleAbilitiesCount();

    public final native int getBattleAbilityAt(int index);

    public final native String getJumpTarget();

    public final native double getVersion();

    public static final double getXpFactor(int attackerPower, int defenderPower) {
        if (attackerPower == defenderPower) {
            return 1;
        } else if (attackerPower < defenderPower) {
            double coef = Math.round(100 * ((getPowerLevel(defenderPower + 1) - 1) / (double) (getPowerLevel(attackerPower + 1) - 1))) / 100.;
            coef -= 1;
            coef /= 2;
            coef += 1;
            return Math.min(1.5, coef);
        } else {
            return Math.max(0, Math.round(100 * ((getPowerLevel(defenderPower + 1) - 1) / (double) (getPowerLevel(attackerPower + 1) - 1))) / 100. - 0.1 * (attackerPower - defenderPower));
        }
    }

    public static final int getFleetLevelXp(int level) {
        return XP_LEVELS[level - 1];
    }

    public static int getPowerLevel(int level) {
        if (level <= 1) {
            return 0;
        } else {
            double value = 500;
            double prod = 25;
            for (int i = 0; i < level - 2; i++) {
                double prodBefore = prod;
                prod = prod * (1.02 + .53 * Math.pow(.95, i + 1));
                double coef1 = prod / prodBefore;
                double coef2 = (3 + 2 * (i + 2)) / (double) (3 + 2 * (i + 1));
                value = value * coef1 / coef2;
            }
            return (int) Math.floor(value / 10) * 10 + 1;
        }
    }

    public static final int getFleetLevel(double xp) {
        int i;
        for (i = 0; i < XP_LEVELS.length; i++) if (xp < XP_LEVELS[i]) return i;
        return 15;
    }
}
