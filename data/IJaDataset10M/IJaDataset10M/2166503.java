package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISStreakSRM6 extends StreakSRMWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 3341440732332387700L;

    /**
     * 
     */
    public ISStreakSRM6() {
        super();
        this.techLevel = TechConstants.T_IS_TW_NON_BOX;
        this.name = "Streak SRM 6";
        this.setInternalName("ISStreakSRM6");
        this.addLookupName("IS Streak SRM-6");
        this.addLookupName("IS Streak SRM 6");
        this.heat = 4;
        this.rackSize = 6;
        this.shortRange = 3;
        this.mediumRange = 6;
        this.longRange = 9;
        this.extremeRange = 12;
        this.tonnage = 4.5f;
        this.criticals = 2;
        this.bv = 89;
        this.cost = 120000;
        this.shortAV = 12;
        this.maxRange = RANGE_SHORT;
    }
}
