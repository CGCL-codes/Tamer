package org.openaion.gameserver.configs.main;

import org.openaion.commons.configuration.Property;

public class EnchantsConfig {

    /**
	 * ManaStone Rates
	 */
    @Property(key = "gameserver.manastone.percent.slot1", defaultValue = "98")
    public static int MS_SLOT1;

    @Property(key = "gameserver.manastone.percent.slot2", defaultValue = "85")
    public static int MS_SLOT2;

    @Property(key = "gameserver.manastone.percent.slot3", defaultValue = "75")
    public static int MS_SLOT3;

    @Property(key = "gameserver.manastone.percent.slot4", defaultValue = "65")
    public static int MS_SLOT4;

    @Property(key = "gameserver.manastone.percent.slot5", defaultValue = "55")
    public static int MS_SLOT5;

    @Property(key = "gameserver.manastone.percent.slot6", defaultValue = "45")
    public static int MS_SLOT6;

    @Property(key = "gameserver.manastone.percent.slot7", defaultValue = "35")
    public static int MS_SLOT7;

    /**
	 * Supplement Additional Success Rates
	 */
    @Property(key = "gameserver.supplement.lesser", defaultValue = "10")
    public static int LSSUP;

    @Property(key = "gameserver.supplement.regular", defaultValue = "15")
    public static int RGSUP;

    @Property(key = "gameserver.supplement.greater", defaultValue = "20")
    public static int GRSUP;
}
