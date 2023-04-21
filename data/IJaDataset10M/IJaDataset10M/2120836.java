package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLLRT5 extends LRTWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 5982164560053938134L;

    /**
     * 
     */
    public CLLRT5() {
        super();
        this.techLevel = TechConstants.T_CLAN_TW;
        this.name = "LRT 5";
        this.setInternalName("CLLRTorpedo5");
        this.addLookupName("Clan LRT-5");
        this.addLookupName("Clan LRT 5");
        this.setInternalName("CLLRT5");
        this.heat = 2;
        this.rackSize = 5;
        this.minimumRange = WEAPON_NA;
        this.waterShortRange = 7;
        this.waterMediumRange = 14;
        this.waterLongRange = 21;
        this.waterExtremeRange = 28;
        this.tonnage = 1.0f;
        this.criticals = 1;
        this.bv = 55;
        this.cost = 30000;
    }
}
