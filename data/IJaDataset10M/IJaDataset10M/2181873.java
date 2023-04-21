package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLLRT10OS extends LRTWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 4402946418858772353L;

    /**
     *
     */
    public CLLRT10OS() {
        super();
        techLevel = TechConstants.T_CLAN_TW;
        name = "LRT 10 (OS)";
        setInternalName("CLLRTorpedo10 (OS)");
        addLookupName("Clan OS LRT-10");
        addLookupName("Clan LRT 10 (OS)");
        addLookupName("CLLRT10OS");
        heat = 4;
        rackSize = 10;
        minimumRange = WEAPON_NA;
        waterShortRange = 7;
        waterMediumRange = 14;
        waterLongRange = 21;
        waterExtremeRange = 28;
        tonnage = 3.0f;
        criticals = 1;
        bv = 22;
        flags = flags.or(F_ONESHOT);
        cost = 50000;
    }
}
