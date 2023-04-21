package org.roguelikedevelopment.dweller.gamedata;

/**
 * Constants shared between the generator code and the actual game
 * @author Bj�rn
 *
 */
public class GameDataConstants {

    public static final short FLAG_NONE = 0x000;

    public static final short FLAG_INVISIBLE = 0x001;

    public static final short FLAG_NODROP = 0x002;

    public static final short FLAG_NOPICKUP = 0x004;

    public static final short FLAG_NOREMOVE = 0x008;

    public static final short FLAG_UNLIMITEDAMOUNT = 0x010;

    public static final short FLAG_CANUSEMISSILES = 0x020;

    public static final short FLAG_NOLOOT = 0x040;

    public static final short FLAG_CASTER = 0x080;

    public static final short FLAG_POISONED = 0x100;

    public static final short FLAG_INTELLIGENT = 0x200;

    public static final short FLAG_LIGHTSOURCE = 0x400;

    public static final short FLAG_NOSUFFIX = 0x800;

    public static final short FLAG_TWOHAND = 0x1000;

    public static final short FLAG_FLYING = 0x2000;

    public static final short FLAG_TRADER = 0x4000;

    private static final String[] FLAG_STRINGS = { "NONE", "INVISIBLE", "NODROP", "NOPICKUP", "NOREMOVE", "UNLIMITEDAMOUNT", "CANUSEMISSILES", "NOLOOT", "CASTER", "POISONED", "INTELLIGENT", "LIGHTSOURCE", "NOSUFFIX", "TWOHAND", "FLYING", "TRADER" };

    private static final short[] FLAG_VALUES = { FLAG_NONE, FLAG_INVISIBLE, FLAG_NODROP, FLAG_NOPICKUP, FLAG_NOREMOVE, FLAG_UNLIMITEDAMOUNT, FLAG_CANUSEMISSILES, FLAG_NOLOOT, FLAG_CASTER, FLAG_POISONED, FLAG_INTELLIGENT, FLAG_LIGHTSOURCE, FLAG_NOSUFFIX, FLAG_TWOHAND, FLAG_FLYING, FLAG_TRADER };

    public static int getFlagFromString(String s) {
        s = s.toUpperCase();
        for (int i = 0; i < FLAG_STRINGS.length; i++) {
            if (FLAG_STRINGS[i].equals(s)) {
                return FLAG_VALUES[i];
            }
        }
        return FLAG_NONE;
    }

    public static final byte THEME_NONE = 0x00;

    public static final byte THEME_DUNGEON = 0x01;

    public static final byte THEME_SEWER = 0x02;

    public static final byte THEME_CAVE = 0x04;

    public static final byte THEME_FOREST = 0x08;

    public static final byte THEME_ALL = 0x7F;

    private static final String[] THEME_STRINGS = { "NONE", "DUNGEON", "SEWER", "CAVE", "FOREST", "ALL" };

    private static final byte[] THEME_VALUES = { THEME_NONE, THEME_DUNGEON, THEME_SEWER, THEME_CAVE, THEME_FOREST, THEME_ALL };

    public static int getThemeFromString(String s) {
        s = s.toUpperCase();
        for (int i = 0; i < THEME_STRINGS.length; i++) {
            if (THEME_STRINGS[i].equals(s)) {
                return THEME_VALUES[i];
            }
        }
        return THEME_NONE;
    }

    public static final byte EFFECTID_NONE = 0;

    public static final byte EFFECTID_FIRE = 1;

    public static final byte EFFECTID_SLOW = 2;

    public static final byte EFFECTID_POISON = 3;

    public static final byte EFFECTID_HEAL = 4;

    public static final byte EFFECTID_TELEPORT = 5;

    public static final byte EFFECTID_ENCHANT = 6;

    public static final byte EFFECTID_RECHARGE = 7;

    public static final byte EFFECTID_DRAIN = 8;

    public static final byte EFFECTID_DAMAGE = 9;

    public static final byte EFFECTID_CLONE = 10;

    public static final byte EFFECTID_CHARM = 11;

    public static final byte EFFECTID_MAPPING = 12;

    public static final byte EFFECTID_TRAIN = 13;

    public static final byte EFFECTID_SUMMONEVIL = 14;

    public static final byte EFFECTID_SUMMONGOOD = 15;

    public static final byte EFFECTID_RANDOMOFFENSIVE = 16;

    public static final byte EFFECTID_DISEASE = 17;

    public static final byte EFFECTID_DISARM = 18;

    public static final byte EFFECTID_KNOCKBACK = 19;

    public static final byte EFFECTID_CUREPOISON = 20;

    public static final byte EFFECTID_STUN = 21;

    private static final String[] EFFECTID_STRINGS = { "NONE", "FIRE", "SLOW", "POISON", "HEAL", "TELEPORT", "ENCHANT", "RECHARGE", "DRAIN", "DAMAGE", "CLONE", "CHARM", "MAPPING", "TRAIN", "SUMMONEVIL", "SUMMONGOOD", "RANDOMOFFENSIVE", "DISEASE", "DISARM", "KNOCKBACK", "CUREPOISON", "STUN" };

    /**
	 * Gets an effect id from a human readable string
	 * @param s
	 * @return
	 */
    public static int getEffectIdFromString(String s) {
        s = s.toUpperCase();
        for (int i = 0; i < EFFECTID_STRINGS.length; i++) {
            if (EFFECTID_STRINGS[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public static final byte ENTITYTYPE_NPC = 0;

    public static final byte ENTITYTYPE_UNIQUE = 1;

    public static final byte ENTITYTYPE_SPINELESS = 2;

    public static final byte ENTITYTYPE_ANIMAL = 3;

    public static final byte ENTITYTYPE_INSECT = 4;

    public static final byte ENTITYTYPE_FANTASY = 5;

    public static final byte ENTITYTYPE_HUMANOID = 6;

    public static final byte ENTITYTYPE_UNDEAD = 7;

    public static final byte ENTITYTYPE_AMMO = 8;

    public static final byte ENTITYTYPE_MELEE = 9;

    public static final byte ENTITYTYPE_MISSILE = 10;

    public static final byte ENTITYTYPE_SHIELD = 11;

    public static final byte ENTITYTYPE_BOOTS = 12;

    public static final byte ENTITYTYPE_CLOAK = 13;

    public static final byte ENTITYTYPE_HELMET = 14;

    public static final byte ENTITYTYPE_ARMOUR = 15;

    public static final byte ENTITYTYPE_RING = 16;

    public static final byte ENTITYTYPE_MONEY = 17;

    public static final byte ENTITYTYPE_SINGLEUSE = 18;

    public static final byte ENTITYTYPE_BOOK = 19;

    public static final byte ENTITYTYPE_TRINKET = 20;

    public static final byte ENTITYTYPE_DEATHMOVEABILITY = 21;

    public static final byte ENTITYTYPE_MELEEABILITY = 22;

    public static final byte ENTITYTYPE_ARMOURABILITY = 23;

    public static final byte ENTITYTYPE_COMESTIBLE = 24;

    public static final byte ENTITYTYPE_AMULET = 25;

    public static final byte ENTITYTYPE_PLAYERTYPE = 26;

    public static final byte ENTITYTYPE_PASSIVEABILITY = 27;

    private static final String[] ENTITYTYPE_STRINGS = { "NPC", "UNIQUE", "SPINELESS", "ANIMAL", "INSECT", "FANTASY", "HUMANOID", "UNDEAD", "AMMO", "MELEE", "MISSILE", "SHIELD", "BOOTS", "CLOAK", "HELMET", "ARMOUR", "RING", "MONEY", "SINGLEUSE", "BOOK", "TRINKET", "DEATHMOVEABILITY", "MELEEABILITY", "ARMOURABILITY", "COMESTIBLE", "AMULET", "PLAYERTYPE", "PASSIVEABILITY" };

    /**
	 * Gets an entity type id from a human readable string
	 * @param s
	 * @return
	 */
    public static int getEntityTypeFromString(String s) {
        s = s.toUpperCase();
        for (int i = 0; i < ENTITYTYPE_STRINGS.length; i++) {
            if (ENTITYTYPE_STRINGS[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public static String getEntityTypeAsString(int entityType) {
        return ENTITYTYPE_STRINGS[entityType];
    }

    public static final byte ALIGNMENT_FRIENDLY = 0x01;

    public static final byte ALIGNMENT_NEUTRAL = 0x02;

    public static final byte ALIGNMENT_HOSTILE = 0x04;

    public static final byte ALIGNMENT_CHARMED = 0x08;

    private static final String[] ALIGNMENT_STRINGS = { "FRIENDLY", "NEUTRAL", "HOSTILE", "CHARMED" };

    private static final byte[] ALIGNMENT_VALUES = { ALIGNMENT_FRIENDLY, ALIGNMENT_NEUTRAL, ALIGNMENT_HOSTILE, ALIGNMENT_CHARMED };

    public static byte getAlignmentFromString(String s) {
        s = s.toUpperCase();
        for (int i = 0; i < ALIGNMENT_STRINGS.length; i++) {
            if (ALIGNMENT_STRINGS[i].equals(s)) {
                return ALIGNMENT_VALUES[i];
            }
        }
        return ALIGNMENT_NEUTRAL;
    }

    public static final byte ENTITYCLASS_CREATURE = 0;

    public static final byte ENTITYCLASS_ITEM = 1;

    public static final byte ENTITYCLASS_PLAYER = 2;
}
