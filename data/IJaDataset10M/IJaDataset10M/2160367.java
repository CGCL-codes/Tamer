package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLLRM5IOS extends LRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 767564661100835293L;

    /**
     *
     */
    public CLLRM5IOS() {
        super();
        techLevel = TechConstants.T_CLAN_ADVANCED;
        name = "LRM 5 (I-OS)";
        setInternalName("CLLRM5 (IOS)");
        addLookupName("CLLRM5IOS");
        addLookupName("Clan IOS LRM-5");
        addLookupName("Clan LRM 5 (IOS)");
        heat = 2;
        rackSize = 5;
        minimumRange = WEAPON_NA;
        tonnage = 0.5f;
        criticals = 1;
        bv = 11;
        flags = flags.or(F_ONESHOT);
        cost = 24000;
        shortAV = 3;
        medAV = 3;
        longAV = 3;
        maxRange = RANGE_LONG;
    }
}
