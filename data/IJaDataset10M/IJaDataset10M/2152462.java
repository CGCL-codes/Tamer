package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLSRM2OS extends SRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 3798165725512473586L;

    /**
     *
     */
    public CLSRM2OS() {
        super();
        techLevel = TechConstants.T_CLAN_TW;
        name = "SRM 2 (OS)";
        setInternalName("CLSRM2 (OS)");
        addLookupName("CLSRM2OS");
        addLookupName("Clan OS SRM-2");
        addLookupName("Clan SRM 2 (OS)");
        heat = 2;
        rackSize = 2;
        shortRange = 3;
        mediumRange = 6;
        longRange = 9;
        extremeRange = 12;
        tonnage = 1;
        criticals = 1;
        bv = 4;
        flags = flags.or(F_NO_FIRES).or(F_ONESHOT);
        cost = 5000;
        shortAV = 2;
        maxRange = RANGE_SHORT;
    }
}
