package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISStreakSRM4OS extends StreakSRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -8651111887714823028L;

    /**
     *
     */
    public ISStreakSRM4OS() {
        super();
        techLevel = TechConstants.T_IS_TW_NON_BOX;
        name = "Streak SRM 4 (OS)";
        setInternalName("ISStreakSRM4OS");
        addLookupName("ISStreakSRM4 (OS)");
        addLookupName("IS Streak SRM 4 (OS)");
        addLookupName("OS Streak SRM-4");
        heat = 3;
        rackSize = 4;
        shortRange = 3;
        mediumRange = 6;
        longRange = 9;
        extremeRange = 12;
        tonnage = 3.5f;
        criticals = 1;
        flags = flags.or(F_NO_FIRES).or(F_ONESHOT);
        bv = 12;
        cost = 45000;
        shortAV = 8;
        maxRange = RANGE_SHORT;
    }
}
