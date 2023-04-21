package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLSniper extends ArtilleryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -599648142688689572L;

    /**
     *
     */
    public CLSniper() {
        super();
        techLevel = TechConstants.T_CLAN_ADVANCED;
        name = "Sniper";
        setInternalName("CLSniper");
        addLookupName("CLSniperArtillery");
        addLookupName("Clan Sniper");
        flags = flags.or(F_AERO_WEAPON);
        heat = 10;
        rackSize = 20;
        ammoType = AmmoType.T_SNIPER;
        shortRange = 1;
        mediumRange = 2;
        longRange = 18;
        extremeRange = 18;
        tonnage = 20f;
        criticals = 20;
        bv = 85;
        cost = 300000;
    }
}
