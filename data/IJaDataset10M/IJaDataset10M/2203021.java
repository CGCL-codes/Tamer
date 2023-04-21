package gameserver.configs;

import commons.configuration.Property;

public class PvPConfig {

    /**
	* Difference of levels in pvp
	*/
    @Property(key = "gameserver.dmgreduction.lvldiffpvp", defaultValue = "false")
    public static boolean DMG_REDUCTION_LVL_DIFF_PVP;
}
