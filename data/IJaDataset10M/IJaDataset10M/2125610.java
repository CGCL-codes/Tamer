package com.realtime.crossfire.jxclient.stats;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to parse stat names.
 * @author Andreas Kirschbaum
 */
public class StatsParser {

    /**
     * Maps stat names to stat index values. Only stats useful in skin files are
     * included.
     */
    @NotNull
    private static final Map<String, Integer> STAT_TABLE = new HashMap<String, Integer>();

    static {
        STAT_TABLE.put("AC", Stats.CS_STAT_AC);
        STAT_TABLE.put("ARM", Stats.CS_STAT_ARMOUR);
        STAT_TABLE.put("CHA", Stats.CS_STAT_CHA);
        STAT_TABLE.put("CHA_APPLIED", Stats.CS_STAT_APPLIED_CHA);
        STAT_TABLE.put("CHA_BASE", Stats.CS_STAT_BASE_CHA);
        STAT_TABLE.put("CHA_RACE", Stats.CS_STAT_RACE_CHA);
        STAT_TABLE.put("CON", Stats.CS_STAT_CON);
        STAT_TABLE.put("CON_APPLIED", Stats.CS_STAT_APPLIED_CON);
        STAT_TABLE.put("CON_BASE", Stats.CS_STAT_BASE_CON);
        STAT_TABLE.put("CON_RACE", Stats.CS_STAT_RACE_CON);
        STAT_TABLE.put("DAM", Stats.CS_STAT_DAM);
        STAT_TABLE.put("DEX", Stats.CS_STAT_DEX);
        STAT_TABLE.put("DEX_APPLIED", Stats.CS_STAT_APPLIED_DEX);
        STAT_TABLE.put("DEX_BASE", Stats.CS_STAT_BASE_DEX);
        STAT_TABLE.put("DEX_RACE", Stats.CS_STAT_RACE_DEX);
        STAT_TABLE.put("EXP", Stats.CS_STAT_EXP64);
        STAT_TABLE.put("EXP_NEXT_LEVEL", Stats.C_STAT_EXP_NEXT_LEVEL);
        STAT_TABLE.put("FOOD", Stats.CS_STAT_FOOD);
        STAT_TABLE.put("GOLEM_HP", Stats.CS_STAT_GOLEM_HP);
        STAT_TABLE.put("GRACE", Stats.CS_STAT_GRACE);
        STAT_TABLE.put("HP", Stats.CS_STAT_HP);
        STAT_TABLE.put("INT", Stats.CS_STAT_INT);
        STAT_TABLE.put("INT_APPLIED", Stats.CS_STAT_APPLIED_INT);
        STAT_TABLE.put("INT_BASE", Stats.CS_STAT_BASE_INT);
        STAT_TABLE.put("INT_RACE", Stats.CS_STAT_RACE_INT);
        STAT_TABLE.put("LEVEL", Stats.CS_STAT_LEVEL);
        STAT_TABLE.put("LOWFOOD", Stats.C_STAT_LOWFOOD);
        STAT_TABLE.put("POISONED", Stats.C_STAT_POISONED);
        STAT_TABLE.put("POW", Stats.CS_STAT_POW);
        STAT_TABLE.put("POW_APPLIED", Stats.CS_STAT_APPLIED_POW);
        STAT_TABLE.put("POW_BASE", Stats.CS_STAT_BASE_POW);
        STAT_TABLE.put("POW_RACE", Stats.CS_STAT_RACE_POW);
        STAT_TABLE.put("RANGE", Stats.CS_STAT_RANGE);
        STAT_TABLE.put("RES_ACID", Stats.CS_STAT_RES_ACID);
        STAT_TABLE.put("RES_BLIND", Stats.CS_STAT_RES_BLIND);
        STAT_TABLE.put("RES_COLD", Stats.CS_STAT_RES_COLD);
        STAT_TABLE.put("RES_CONF", Stats.CS_STAT_RES_CONF);
        STAT_TABLE.put("RES_DEATH", Stats.CS_STAT_RES_DEATH);
        STAT_TABLE.put("RES_DEPLETE", Stats.CS_STAT_RES_DEPLETE);
        STAT_TABLE.put("RES_DRAIN", Stats.CS_STAT_RES_DRAIN);
        STAT_TABLE.put("RES_ELEC", Stats.CS_STAT_RES_ELEC);
        STAT_TABLE.put("RES_FEAR", Stats.CS_STAT_RES_FEAR);
        STAT_TABLE.put("RES_FIRE", Stats.CS_STAT_RES_FIRE);
        STAT_TABLE.put("RES_GHOSTHIT", Stats.CS_STAT_RES_GHOSTHIT);
        STAT_TABLE.put("RES_HOLYWORD", Stats.CS_STAT_RES_HOLYWORD);
        STAT_TABLE.put("RES_MAG", Stats.CS_STAT_RES_MAG);
        STAT_TABLE.put("RES_PARA", Stats.CS_STAT_RES_PARA);
        STAT_TABLE.put("RES_PHYS", Stats.CS_STAT_RES_PHYS);
        STAT_TABLE.put("RES_POISON", Stats.CS_STAT_RES_POISON);
        STAT_TABLE.put("RES_SLOW", Stats.CS_STAT_RES_SLOW);
        STAT_TABLE.put("RES_TURN_UNDEAD", Stats.CS_STAT_RES_TURN_UNDEAD);
        STAT_TABLE.put("SP", Stats.CS_STAT_SP);
        STAT_TABLE.put("SPEED", Stats.CS_STAT_SPEED);
        STAT_TABLE.put("STR", Stats.CS_STAT_STR);
        STAT_TABLE.put("STR_APPLIED", Stats.CS_STAT_APPLIED_STR);
        STAT_TABLE.put("STR_BASE", Stats.CS_STAT_BASE_STR);
        STAT_TABLE.put("STR_RACE", Stats.CS_STAT_RACE_STR);
        STAT_TABLE.put("TITLE", Stats.CS_STAT_TITLE);
        STAT_TABLE.put("WC", Stats.CS_STAT_WC);
        STAT_TABLE.put("WEIGHT", Stats.C_STAT_WEIGHT);
        STAT_TABLE.put("WEIGHT_LIMIT", Stats.CS_STAT_WEIGHT_LIM);
        STAT_TABLE.put("WIS", Stats.CS_STAT_WIS);
        STAT_TABLE.put("WIS_APPLIED", Stats.CS_STAT_APPLIED_WIS);
        STAT_TABLE.put("WIS_BASE", Stats.CS_STAT_BASE_WIS);
        STAT_TABLE.put("WIS_RACE", Stats.CS_STAT_RACE_WIS);
        STAT_TABLE.put("WEAPON_SPEED", Stats.CS_STAT_WEAP_SP);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private StatsParser() {
    }

    /**
     * Converts a stat name into a stat index.
     * @param name the stat name
     * @return the stat index
     * @throws IllegalArgumentException if the stat name is undefined
     */
    public static int parseStat(@NotNull final String name) {
        if (!STAT_TABLE.containsKey(name)) {
            throw new IllegalArgumentException();
        }
        return STAT_TABLE.get(name);
    }
}
