package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLBAMG extends BAMGWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = -5021714235121936669L;

    /**
     * 
     */
    public CLBAMG() {
        super();
        this.techLevel = TechConstants.T_CLAN_TW;
        this.name = "Machine Gun";
        this.setInternalName("CLBAMG");
        this.addLookupName("Clan BA Machine Gun");
        this.heat = 0;
        this.damage = 2;
        this.rackSize = 2;
        this.shortRange = 1;
        this.mediumRange = 2;
        this.longRange = 3;
        this.extremeRange = 4;
        this.tonnage = 0.25f;
        this.criticals = 1;
        this.bv = 5;
        this.cost = 5000;
    }
}
