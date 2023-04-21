package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Andrew Hunter
 */
public class CLUAC20 extends UACWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 2630276807984380743L;

    /**
     *
     */
    public CLUAC20() {
        super();
        techLevel = TechConstants.T_CLAN_TW;
        name = "Ultra AC/20";
        setInternalName("CLUltraAC20");
        addLookupName("Clan Ultra AC/20");
        heat = 7;
        damage = 20;
        rackSize = 20;
        shortRange = 4;
        mediumRange = 8;
        longRange = 12;
        extremeRange = 16;
        tonnage = 12.0f;
        criticals = 8;
        bv = 335;
        cost = 480000;
        flags = flags.or(F_SPLITABLE);
        shortAV = 30;
        medAV = 30;
        maxRange = RANGE_MED;
        explosionDamage = damage;
        techRating = RATING_F;
    }
}
