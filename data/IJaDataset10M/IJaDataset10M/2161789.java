package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.BombType;
import megamek.common.TechConstants;

/**
 * @author Jay Lawson
 */
public class BombArrowIV extends AmmoWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = -1321502140176775035L;

    public BombArrowIV() {
        super();
        this.techLevel = TechConstants.T_IS_ADVANCED;
        this.name = "Arrow IV (Bomb)";
        this.setInternalName(BombType.getBombWeaponName(BombType.B_ARROW));
        this.heat = 0;
        this.rackSize = 20;
        this.ammoType = AmmoType.T_ARROW_IV_BOMB;
        this.shortRange = 1;
        this.mediumRange = 2;
        this.longRange = 9;
        this.extremeRange = 9;
        this.tonnage = 0;
        this.criticals = 0;
        this.hittable = false;
        this.bv = 0;
        this.cost = 0;
    }
}
