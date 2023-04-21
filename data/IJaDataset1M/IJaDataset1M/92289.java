package plugin.exporttokens;

import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.StringKey;
import pcgen.core.Equipment;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.analysis.OutputNameFormatting;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.WeaponToken;
import pcgen.util.Delta;
import pcgen.util.enumeration.AttackType;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <code>WeaponDroidPCViewerToken</code>.
 * 
 */
public class WeaponDroidPCViewerToken extends WeaponToken {

    /** WeaponDroidPCViewerToken Token. */
    public static final String TOKEN_NAME = "WEAPOND";

    /**
	 * Gets the token name
	 * 
	 * @return The token name.
	 * @see pcgen.io.exporttoken.Token#getTokenName()
	 */
    public String getTokenName() {
        return TOKEN_NAME;
    }

    @Override
    public String getToken(String tokenSource, PlayerCharacter pc, ExportHandler eh) {
        StringTokenizer aTok = new StringTokenizer(tokenSource, ".", false);
        StringTokenizer bTok = new StringTokenizer(tokenSource, ".", false);
        aTok.nextToken();
        bTok.nextToken();
        int merge = Constants.MERGE_ALL;
        int weapon = 0;
        Equipment eq;
        String token = aTok.nextToken();
        bTok.nextToken();
        if (token.equals("MERGENONE")) {
            merge = Constants.MERGE_NONE;
            token = aTok.nextToken();
            bTok.nextToken();
        } else if (token.equals("MERGELOC")) {
            merge = Constants.MERGE_LOCATION;
            token = aTok.nextToken();
            bTok.nextToken();
        } else if (token.equals("MERGEALL")) {
            merge = Constants.MERGE_ALL;
            token = aTok.nextToken();
            bTok.nextToken();
        }
        List<Equipment> weaponList = pc.getExpandedWeapons(merge);
        if (token.equals("ALL")) {
            token = aTok.nextToken();
            bTok.nextToken();
        } else if (token.equals("EQUIPPED")) {
            for (Iterator<Equipment> it = weaponList.iterator(); it.hasNext(); ) {
                if (!it.next().isEquipped()) {
                    it.remove();
                }
            }
            token = aTok.nextToken();
            bTok.nextToken();
        } else if (token.equals("NOT_EQUIPPED")) {
            for (Iterator<Equipment> it = weaponList.iterator(); it.hasNext(); ) {
                if (it.next().isEquipped()) {
                    it.remove();
                }
            }
            token = aTok.nextToken();
            bTok.nextToken();
        } else if (token.equals("CARRIED")) {
            for (Iterator<Equipment> it = weaponList.iterator(); it.hasNext(); ) {
                if (it.next().numberCarried().intValue() == 0) {
                    it.remove();
                }
            }
            token = aTok.nextToken();
            bTok.nextToken();
        } else if (token.equals("NOT_CARRIED")) {
            for (Iterator<Equipment> it = weaponList.iterator(); it.hasNext(); ) {
                if (it.next().numberCarried().intValue() > 0) {
                    it.remove();
                }
            }
            token = aTok.nextToken();
            bTok.nextToken();
        }
        weapon = getIntToken(token, 0);
        if (weapon < weaponList.size()) {
            eq = weaponList.get(weapon);
            if (weapon == weaponList.size() - 1 && eh != null && eh.getExistsOnly()) {
                eh.setNoMoreItems(true);
            }
            String output = getWeaponToken(pc, eq, aTok);
            if (output == "") output = getWeaponTokenEx(pc, eq, bTok);
            return output;
        } else if (eh != null && eh.getExistsOnly()) {
            eh.setNoMoreItems(true);
            if (eh.getCheckBefore()) {
                eh.setCanWrite(false);
            }
        }
        return "";
    }

    public String getWeaponTokenEx(PlayerCharacter pc, Equipment eq, StringTokenizer aTok) {
        String token = "";
        if (aTok.hasMoreTokens()) token = aTok.nextToken();
        if (token.equals("WIELD")) return String.valueOf(getWieldToken(pc, eq)); else if (token.equals("FINESSEABLE")) return String.valueOf(getFinesseableToken(pc, eq)); else if (token.equals("LIGHT")) return String.valueOf(getWeaponLightToken(pc, eq)); else if (token.equals("ONEHANDED")) return String.valueOf(getWeaponOneHandedToken(pc, eq)); else if (token.equals("TWOHANDED")) return String.valueOf(getWeaponTwoHandedToken(pc, eq)); else if (token.equals("HITBONUS")) return Delta.toString(getTemplateHitToken(pc, eq) + getFeatHitToken(pc, eq) + getMagicHitToken(pc, eq) + getMiscToken(pc, eq)); else if (token.equals("BONUSDAMAGE")) return Delta.toString(getTemplateDamageToken(pc, eq) + getMagicDamageToken(pc, eq) + getFeatDamageToken(pc, eq)); else if (token.equals("ATTACKSEPARATOR")) return String.valueOf(getAttackSeparatorToken(pc, eq)); else if (token.equals("TWOWEAPONHITPRIMARY")) return String.valueOf(pc.getTotalBonusTo("COMBAT", "TOHIT-PRIMARY")); else if (token.equals("TWOWEAPONHITSECONDARY")) return String.valueOf(pc.getTotalBonusTo("COMBAT", "TOHIT-SECONDARY"));
        return "";
    }

    /**
	 * Get the number of attacks token
	 * @param pc
	 * @param eq
	 * @return number of attacks token
	 */
    public static int getAttackSeparatorToken(PlayerCharacter pc, Equipment eq) {
        String melee = pc.getAttackString(AttackType.MELEE, 0, 0);
        String unarmed = pc.getAttackString(AttackType.UNARMED, 0, 0);
        String weaponString = melee;
        if (eq.isRanged()) weaponString = pc.getAttackString(AttackType.RANGED, 0, 0);
        ;
        if (eq.isMonk()) {
            if (unarmed.length() > melee.length()) weaponString = unarmed; else if ((unarmed.length() == melee.length()) && !melee.equals(unarmed)) {
                StringTokenizer mTok = new StringTokenizer(melee, "+/", false);
                StringTokenizer uTok = new StringTokenizer(unarmed, "+/", false);
                String msString = mTok.nextToken();
                String usString = uTok.nextToken();
                if (Integer.parseInt(usString) >= Integer.parseInt(msString)) weaponString = unarmed;
            }
        }
        StringTokenizer bTok = new StringTokenizer(weaponString, "/");
        if (bTok.countTokens() > 1) {
            int temp1 = Delta.parseInt(bTok.nextToken());
            int temp2 = Delta.parseInt(bTok.nextToken());
            return temp1 - temp2;
        }
        return 0;
    }

    /**
	 * Get Finesseable token
	 * @param eq
	 * @return Finesseable token
	 */
    public static int getFinesseableToken(PlayerCharacter pc, Equipment eq) {
        if (eq.isMelee() && eq.isFinessable(pc)) {
            int totalBonus = (int) pc.getTotalBonusTo("COMBAT", "TOHIT.Finesseable");
            return (totalBonus > 0 ? 1 : 0);
        }
        return 0;
    }

    /**
	 * Get weapon light token
	 * @param eq
	 * @return weapon light token
	 */
    public static int getWeaponLightToken(PlayerCharacter pc, Equipment eq) {
        return (eq.isMelee() && eq.isWeaponLightForPC(pc) ? 1 : 0);
    }

    /**
	 * Get weapon one handed token
	 * @param eq
	 * @return weapon one handed token
	 */
    public static boolean getWeaponOneHandedToken(PlayerCharacter pc, Equipment eq) {
        return eq.isMelee() && eq.isWeaponOneHanded(pc);
    }

    /**
	 * Get weapon two handed token
	 * @param eq
	 * @return weapon two handed token
	 */
    public static boolean getWeaponTwoHandedToken(PlayerCharacter pc, Equipment eq) {
        return eq.isMelee() && eq.isWeaponTwoHanded(pc);
    }

    /**
	 * Get wield token
	 * @param eq
	 * @return wield token
	 */
    public static int getWieldToken(PlayerCharacter pc, Equipment eq) {
        if (getWeaponTwoHandedToken(pc, eq)) return 2;
        return 1;
    }
}
