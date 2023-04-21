package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Jay Lawson
 */
public class SCL1Weapon extends SubCapitalLaserWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 8756042527483383101L;

    /**
     * 
     */
    public SCL1Weapon() {
        super();
        this.techLevel = TechConstants.T_IS_ADVANCED;
        this.name = "Sub-Capital Laser 1";
        this.setInternalName(this.name);
        this.addLookupName("SCL1");
        this.heat = 24;
        this.damage = 1;
        this.shortRange = 11;
        this.mediumRange = 22;
        this.longRange = 33;
        this.extremeRange = 44;
        this.tonnage = 150.0f;
        this.bv = 237;
        this.cost = 220000;
        this.shortAV = 1;
        this.medAV = 1;
        this.longAV = 1;
        this.maxRange = RANGE_LONG;
    }
}
