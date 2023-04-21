package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Jay Lawson
 */
public class SCL3Weapon extends SubCapitalLaserWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 8756042527483383101L;

    /**
     * 
     */
    public SCL3Weapon() {
        super();
        this.techLevel = TechConstants.T_IS_ADVANCED;
        this.name = "Sub-Capital Laser 3";
        this.setInternalName(this.name);
        this.addLookupName("SCL3");
        this.heat = 32;
        this.damage = 3;
        this.shortRange = 11;
        this.mediumRange = 22;
        this.longRange = 33;
        this.extremeRange = 44;
        this.tonnage = 250.0f;
        this.bv = 531;
        this.cost = 450000;
        this.shortAV = 3;
        this.medAV = 3;
        this.maxRange = RANGE_MED;
    }
}
