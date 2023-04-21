package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantrySupportClanSemiPortablePulseLaserWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantrySupportClanSemiPortablePulseLaserWeapon() {
        super();
        techLevel = TechConstants.T_CLAN_TW;
        name = "Infantry Clan Semi-Portable Pulse Laser";
        setInternalName(name);
        addLookupName("InfantryClanSemiPortablePulseLaser");
        addLookupName("InfantryClanMicroPulseLaser");
        ammoType = AmmoType.T_NA;
        cost = 12500;
        bv = 2.3;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_ENERGY).or(F_PULSE).or(F_INF_SUPPORT);
        infantryDamage = 0.55;
        infantryRange = 2;
        crew = 2;
    }
}
