package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 *
 */
public class CLPROSRM1 extends SRMWeapon {

    /**
     * 
     */
    public CLPROSRM1() {
        super();
        this.techLevel = TechConstants.T_CLAN_LEVEL_2;
        this.name = "SRM 1";
        this.setInternalName("CLPROSRM1");
        this.heat = 0;
        this.rackSize = 1;
        this.shortRange = 3;
        this.mediumRange = 6;
        this.longRange = 9;
        this.extremeRange = 12;
        this.tonnage = 0.25f;
        this.criticals = 0;
        this.bv = 15;
        this.flags |= F_PROTOMECH;
    }
}
