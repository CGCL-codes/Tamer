package net.rsapollo.skill.base;

import net.rsapollo.models.*;

public class Combat {

    public Combat(PlayerModel c) {
        this.c = c;
    }

    private PlayerModel c;

    public int[] dagger = { 1205, 1203, 1207, 1209, 1211, 1213 };

    public int[] scimitar = { 1321, 1323, 1325, 1329, 1331, 1333 };

    public int[] twoHand = { 1307, 1309, 1311, 1315, 1317, 1319 };

    public int[] longSword = { 1291, 1293, 1295, 1299, 1301, 1303 };

    public int[] pickAxe = { 1265, 1267, 1269, 1273, 1271, 1275 };

    public int[] warHammer = { 1337, 1335, 1339, 1343, 1345, 1347 };

    public int[] battleAxe = { 1375, 1363, 1365, 1369, 1371, 1373 };

    public int[] wcAxe = { 1351, 1349, 1353, 1355, 1357, 1359 };

    public int[] shortSword = { 1277, 1279, 1281, 1285, 1287, 1289 };

    public int[] shortBow = { 841, 843, 849, 853, 857, 861 };

    public int[] longBow = { 839, 845, 847, 851, 855, 859 };

    public int[] arrows = { 882, 883, 884, 885, 886, 887, 888, 889, 890, 891, 892, 893 };

    public int mageSpell = -1;

    public int[] staff = { 1381, 1383, 1385, 1387 };

    public int[] runesForStaff = { 556, 555, 557, 554 };

    public int[] poisonWeapons = { 883, 885, 887, 889, 891, 893, 1221, 1219, 1223, 1225, 1227, 1229 };

    public int weaponDelay = 0;

    public int attackingOn = -1;

    public boolean attackingPlayer = false;

    public int killedBy = -1;

    public int poisonDelay = -1;

    public int[] specialAttackWeps = { -1 };

    public boolean usingSpecAttack = false;

    public void applyDead() {
        c.sendMessage("Dead");
        c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
    }

    public String weaponType() {
        ItemModel wep = c.playerEquipment[c.playerWeapon];
        ItemModel arrow = c.playerEquipment[c.playerArrows];
        if (wep.id == -1) {
            return "melee";
        }
        for (int i = 0; i < 6; i++) {
            if ((wep.id == staff[i] && mageSpell == -1) || wep.id == dagger[i] || wep.id == scimitar[i] || wep.id == twoHand[i] || wep.id == longSword[i] || wep.id == shortSword[i] || wep.id == pickAxe[i] || wep.id == warHammer[i] || wep.id == battleAxe[i] || wep.id == wcAxe[i]) {
                return "melee";
            }
        }
        if (mageSpell != -1) {
            return "mage";
        }
        for (int i = 0; i < 4; i++) {
            if (wep.id == shortBow[i] || wep.id == longBow[i]) {
                if (arrow.id == -1) {
                    c.sendMessage("You need arrows!");
                    return "none";
                }
                for (int a = 0; a < 4; a++) {
                    if (arrow.id == arrows[a]) {
                        if (i >= a || a <= 2) {
                            return "range";
                        } else {
                            c.sendMessage("You need a higher bow to use this arrows!");
                            return "none";
                        }
                    }
                }
            }
        }
        return "none";
    }

    public int getWepAnim() {
        String wepType = getWepType();
        if (wepType.equalsIgnoreCase("staff")) {
            return 408;
        }
        if (wepType.equalsIgnoreCase("fist")) {
            return 422;
        }
        if (wepType.equalsIgnoreCase("shortsword")) {
            return 451;
        }
        if (wepType.equalsIgnoreCase("longsword")) {
            return 451;
        }
        if (wepType.equalsIgnoreCase("wcaxe")) {
            return 875;
        }
        if (wepType.equalsIgnoreCase("battleaxe")) {
            return 1833;
        }
        if (wepType.equalsIgnoreCase("warhammer")) {
            return 451;
        }
        if (wepType.equalsIgnoreCase("twoHand")) {
            return 407;
        }
        if (wepType.equalsIgnoreCase("pickaxe")) {
            return 451;
        }
        if (wepType.equalsIgnoreCase("dagger")) {
            return 402;
        }
        if (wepType.equalsIgnoreCase("scimitar")) {
            return 451;
        }
        return 451;
    }

    public int getWepDelay() {
        if (mageSpell == -1) {
            String wepType = getWepType();
            if (wepType.equalsIgnoreCase("fist")) {
                return 5;
            }
            if (wepType.equalsIgnoreCase("staff")) {
                return 5;
            }
            if (wepType.equalsIgnoreCase("shortsword")) {
                return 4;
            }
            if (wepType.equalsIgnoreCase("longsword")) {
                return 5;
            }
            if (wepType.equalsIgnoreCase("wcaxe")) {
                return 5;
            }
            if (wepType.equalsIgnoreCase("battleaxe")) {
                return 6;
            }
            if (wepType.equalsIgnoreCase("warhammer")) {
                return 6;
            }
            if (wepType.equalsIgnoreCase("twoHand")) {
                return 7;
            }
            if (wepType.equalsIgnoreCase("pickaxe")) {
                return 5;
            }
            if (wepType.equalsIgnoreCase("dagger")) {
                return 4;
            }
            if (wepType.equalsIgnoreCase("scimitar")) {
                return 4;
            }
        }
        if (mageSpell != -1) {
            return getSpellDelay();
        }
        return 6;
    }

    public int getSpellDelay() {
        return 4;
    }

    public int maxHitMelee() {
        return 8;
    }

    public String getWepType() {
        ItemModel ID = c.playerEquipment[c.playerWeapon];
        if (ID.id == -1) {
            return "fist";
        }
        for (int i = 0; i < staff.length; i++) {
            if (staff[i] == ID.id) {
                return "staff";
            }
        }
        for (int i = 0; i < dagger.length; i++) {
            if (dagger[i] == ID.id) {
                return "dagger";
            }
        }
        for (int i = 0; i < scimitar.length; i++) {
            if (scimitar[i] == ID.id) {
                return "scimitar";
            }
        }
        for (int i = 0; i < twoHand.length; i++) {
            if (twoHand[i] == ID.id) {
                return "twohand";
            }
        }
        for (int i = 0; i < longSword.length; i++) {
            if (longSword[i] == ID.id) {
                return "longsword";
            }
        }
        for (int i = 0; i < pickAxe.length; i++) {
            if (pickAxe[i] == ID.id) {
                return "pickaxe";
            }
        }
        for (int i = 0; i < warHammer.length; i++) {
            if (warHammer[i] == ID.id) {
                return "warhammer";
            }
        }
        for (int i = 0; i < battleAxe.length; i++) {
            if (battleAxe[i] == ID.id) {
                return "battleaxe";
            }
        }
        for (int i = 0; i < wcAxe.length; i++) {
            if (wcAxe[i] == ID.id) {
                return "wcaxe";
            }
        }
        for (int i = 0; i < shortSword.length; i++) {
            if (shortSword[i] == ID.id) {
                return "shortsword";
            }
        }
        return "none";
    }

    public boolean canAttack() {
        if (weaponDelay == 0) {
            return true;
        }
        return false;
    }

    public Melee Melee = new Melee(this, c);

    public Mage Mage = new Mage(this, c);

    public Range Range = new Range(this, c);

    public void process() {
        if (weaponDelay > 0) {
            weaponDelay -= .5;
        }
        if (attackingOn != -1) {
            System.out.println("Attacking Someone");
            if (canAttack()) {
                System.out.println("Can Attack~~~~~~~~~~~~~~~");
                if (weaponType().equalsIgnoreCase("melee")) {
                    System.out.println("Melee!!!!!!!!!!!!!!!!!!!!!!");
                    Melee.melee();
                }
                if (weaponType().equalsIgnoreCase("mage")) {
                }
                if (weaponType().equalsIgnoreCase("range")) {
                }
                if (weaponType().equalsIgnoreCase("none")) {
                    attackingOn = -1;
                }
            }
        }
    }
}
