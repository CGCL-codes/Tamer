package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISCruiseMissile70 extends ArtilleryWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 5323886711682442495L;

    /**
     * 
     */
    public ISCruiseMissile70() {
        super();
        this.techLevel = TechConstants.T_IS_EXPERIMENTAL;
        this.name = "Cruise Missile/70";
        this.setInternalName("ISCruiseMissile70");
        this.heat = 70;
        this.rackSize = 70;
        this.ammoType = AmmoType.T_CRUISE_MISSILE;
        this.shortRange = 1;
        this.mediumRange = 2;
        this.longRange = 90;
        this.extremeRange = 90;
        this.tonnage = 80f;
        this.criticals = 80;
        this.bv = 1031;
        this.cost = 1250000;
    }
}
