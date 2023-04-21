package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLLRT15IOS extends LRTWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 2935323332234777496L;

    /**
     *
     */
    public CLLRT15IOS() {
        super();
        techLevel = TechConstants.T_CLAN_ADVANCED;
        name = "LRT 15 (I-OS)";
        setInternalName("CLLRTorpedo15 (IOS)");
        addLookupName("Clan IOS LRT-15");
        addLookupName("Clan LRT 15 (IOS)");
        addLookupName("CLLRT15IOS");
        heat = 5;
        rackSize = 15;
        minimumRange = WEAPON_NA;
        waterShortRange = 7;
        waterMediumRange = 14;
        waterLongRange = 21;
        waterExtremeRange = 28;
        tonnage = 3.0f;
        criticals = 2;
        bv = 33;
        flags = flags.or(F_ONESHOT);
        cost = 140000;
    }
}
