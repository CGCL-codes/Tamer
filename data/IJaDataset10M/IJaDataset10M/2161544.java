package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Jay Lawson
 */
public class MRMBayWeapon extends AmmoBayWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 8756042527483383101L;

    /**
     * 
     */
    public MRMBayWeapon() {
        super();
        this.techLevel = TechConstants.T_ALL;
        this.name = "MRM Bay";
        this.setInternalName(this.name);
        this.heat = 0;
        this.damage = DAMAGE_VARIABLE;
        this.shortRange = 6;
        this.mediumRange = 12;
        this.longRange = 20;
        this.extremeRange = 25;
        this.tonnage = 0.0f;
        this.bv = 0;
        this.cost = 0;
        this.atClass = CLASS_MRM;
    }
}
