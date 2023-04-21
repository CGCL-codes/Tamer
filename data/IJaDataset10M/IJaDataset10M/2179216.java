package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLStreakSRM4 extends StreakSRMWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = -7630389410195927363L;

    /**
     * 
     */
    public CLStreakSRM4() {
        this.techLevel = TechConstants.T_CLAN_TW;
        this.name = "Streak SRM 4";
        this.setInternalName("CLStreakSRM4");
        this.addLookupName("Clan Streak SRM-4");
        this.addLookupName("Clan Streak SRM 4");
        this.heat = 3;
        this.rackSize = 4;
        this.shortRange = 4;
        this.mediumRange = 8;
        this.longRange = 12;
        this.extremeRange = 16;
        this.tonnage = 2.0f;
        this.criticals = 1;
        this.bv = 79;
        this.cost = 90000;
        this.shortAV = 8;
        this.medAV = 8;
        this.maxRange = RANGE_MED;
    }
}
