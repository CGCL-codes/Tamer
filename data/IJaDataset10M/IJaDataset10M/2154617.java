package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLLRM10OS extends LRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -1260890574819347313L;

    /**
     *
     */
    public CLLRM10OS() {
        super();
        techLevel = TechConstants.T_CLAN_TW;
        name = "LRM 10 (OS)";
        setInternalName("CLLRM10 (OS)");
        addLookupName("Clan OS LRM-10");
        addLookupName("Clan LRM 10 (OS)");
        heat = 4;
        rackSize = 10;
        minimumRange = WEAPON_NA;
        tonnage = 3.0f;
        criticals = 1;
        bv = 22;
        flags = flags.or(F_ONESHOT);
        cost = 50000;
        shortAV = 6;
        medAV = 6;
        longAV = 6;
        maxRange = RANGE_LONG;
    }
}
