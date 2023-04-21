package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLSRM2 extends SRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -8216939998088201265L;

    /**
     *
     */
    public CLSRM2() {
        super();
        techLevel = TechConstants.T_CLAN_TW;
        name = "SRM 2";
        setInternalName("CLSRM2");
        addLookupName("Clan SRM-2");
        addLookupName("Clan SRM 2");
        heat = 2;
        rackSize = 2;
        shortRange = 3;
        mediumRange = 6;
        longRange = 9;
        extremeRange = 12;
        tonnage = 0.5f;
        criticals = 1;
        bv = 21;
        flags = flags.or(F_NO_FIRES);
        cost = 10000;
        shortAV = 2;
        maxRange = RANGE_SHORT;
    }
}
