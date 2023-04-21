package org.activision.util;

import org.activision.model.Entity;
import org.activision.model.Item;
import org.activision.model.player.Equipment;
import org.activision.model.player.Player;

public class CombatManager {

    public static byte getSpeedForWeapon(int weaponId) {
        if (weaponId == -1) return 3;
        if (new Item(weaponId).getDefinition().name.contains("axe")) return 5;
        if (new Item(weaponId).getDefinition().name.contains("spear")) return 5;
        if (new Item(weaponId).getDefinition().name.contains("Torag")) return 5;
        if (new Item(weaponId).getDefinition().name.contains("flail")) return 5;
        if (new Item(weaponId).getDefinition().name.contains("Orge")) return 8;
        if (new Item(weaponId).getDefinition().name.contains("battleaxe")) return 6;
        if (new Item(weaponId).getDefinition().name.contains("pickaxe")) return 5;
        if (new Item(weaponId).getDefinition().name.contains("thrownaxe")) return 5;
        if (new Item(weaponId).getDefinition().name.contains("warhammer")) return 6;
        if (new Item(weaponId).getDefinition().name.contains("halberd")) return 7;
        if (new Item(weaponId).getDefinition().name.contains("mace")) return 5;
        if (new Item(weaponId).getDefinition().name.contains("sword")) return 4;
        if (new Item(weaponId).getDefinition().name.contains("claws")) return 4;
        if (new Item(weaponId).getDefinition().name.contains("whip")) return 4;
        if (new Item(weaponId).getDefinition().name.contains("scimitar")) return 4;
        if (new Item(weaponId).getDefinition().name.contains("godsword")) return 6;
        if (new Item(weaponId).getDefinition().name.contains("Dharok")) return 6;
        if (new Item(weaponId).getDefinition().name.contains("Granite maul")) return 7;
        if (new Item(weaponId).getDefinition().name.contains("Toktz-xil-ak")) return 4;
        if (new Item(weaponId).getDefinition().name.contains("Tzhaar-ket-em")) return 5;
        if (new Item(weaponId).getDefinition().name.contains("Tzhaar-ket-om")) return 7;
        if (new Item(weaponId).getDefinition().name.contains("Toktz-xil-ek")) return 4;
        if (new Item(weaponId).getDefinition().name.contains("Toktz-xil-ul")) return 4;
        if (new Item(weaponId).getDefinition().name.contains("Toktz-mej-tal")) return 6;
        if (new Item(weaponId).getDefinition().name.contains("staff")) return 4;
        if (new Item(weaponId).getDefinition().name.contains("short")) return 4;
        if (new Item(weaponId).getDefinition().name.contains("long")) return 6;
        if (new Item(weaponId).getDefinition().name.contains("Crystal")) return 6;
        return 3;
    }

    public static boolean wearingDharok(Player p) {
        if (p.getEquipment().get(Equipment.SLOT_WEAPON) == null || !p.getEquipment().get(Equipment.SLOT_WEAPON).getDefinition().name.contains("Dharok")) return false;
        if (p.getEquipment().get(Equipment.SLOT_HAT) == null || !p.getEquipment().get(Equipment.SLOT_HAT).getDefinition().name.contains("Dharok")) return false;
        if (p.getEquipment().get(Equipment.SLOT_CHEST) == null || !p.getEquipment().get(Equipment.SLOT_CHEST).getDefinition().name.contains("Dharok")) return false;
        if (p.getEquipment().get(Equipment.SLOT_LEGS) == null || !p.getEquipment().get(Equipment.SLOT_LEGS).getDefinition().name.contains("Dharok")) return false;
        return true;
    }

    public static boolean wearingVerac(Player p) {
        if (p.getEquipment().get(Equipment.SLOT_WEAPON) == null || !p.getEquipment().get(Equipment.SLOT_WEAPON).getDefinition().name.contains("Verac")) return false;
        if (p.getEquipment().get(Equipment.SLOT_HAT) == null || !p.getEquipment().get(Equipment.SLOT_HAT).getDefinition().name.contains("Verac")) return false;
        if (p.getEquipment().get(Equipment.SLOT_CHEST) == null || !p.getEquipment().get(Equipment.SLOT_CHEST).getDefinition().name.contains("Verac")) return false;
        if (p.getEquipment().get(Equipment.SLOT_LEGS) == null || !p.getEquipment().get(Equipment.SLOT_LEGS).getDefinition().name.contains("Verac")) return false;
        return true;
    }

    public static byte distForWeap(int weaponId) {
        switch(weaponId) {
            default:
                return 0;
        }
    }

    public static double getSpecDamageDoublePercentage(int weaponId) {
        switch(weaponId) {
            case 11694:
                return 1.34375;
            case 11696:
                return 1.1825;
            case 3204:
            case 3101:
                return 1.1;
            case 3105:
                return 1.15;
            case 1434:
                return 1.45;
            case 11698:
            case 11700:
                return 1.075;
            case 13902:
            case 13904:
                return 1.25;
            case 13899:
            case 13901:
                return 1.20;
        }
        String weaponName = weaponId == -1 ? "" : new Item(weaponId).getDefinition().name;
        if (weaponName.contains("Dragon dagger")) return 1.1;
        return 1;
    }

    public static short getSpecAmt(int weaponId) {
        switch(weaponId) {
            case 4151:
            case 4153:
            case 10887:
            case 11694:
            case 14484:
            case 11698:
                return 50;
            case 11696:
            case 11730:
            case 11700:
                return 100;
            case 1305:
            case 5698:
                return 25;
            case 4587:
                return 0;
            default:
                return -1;
        }
    }

    public static short getDefenceEmote(Entity entity) {
        if (entity instanceof Player) {
            Player target = (Player) entity;
            final short weaponId = (short) (target.getEquipment().get(3) == null ? -1 : target.getEquipment().get(3).getId());
            final short shieldId = (short) (target.getEquipment().get(5) == null ? -1 : target.getEquipment().get(5).getId());
            if (shieldId == -1 && weaponId == -1) return 424;
            String weaponName = weaponId == -1 ? "" : new Item(weaponId).getDefinition().name;
            String shieldName = shieldId == -1 ? "" : new Item(shieldId).getDefinition().name;
            if (shieldId != -1 && shieldName.contains("defender")) return 4177;
            if (shieldId != -1 && shieldName.contains("shield")) return 1156;
            if (weaponId != -1 && (weaponName.contains("godsword") || weaponName.contains("2h sword"))) return 7050;
            if (weaponId != -1 && (weaponName.contains("Keris") || weaponName.contains("dagger"))) return 403;
        }
        return 424;
    }

    public static boolean isRangingWeapon(int weaponId) {
        if (weaponId == -1) return false;
        String weaponName = new Item(weaponId).getDefinition().name;
        if (weaponName.contains("bow") || weaponName.contains("dart") || weaponName.contains("knife")) return true;
        return false;
    }
}
