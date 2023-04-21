package gamelogic.units;

import java.util.Vector;

/**
 * @author kbok
 * Provides a way of identify the type of each unit and to get graphics and sound resource from it.
 */
public class UnitIdentifier {

    private static Vector<UnitIdentifier> list;

    private static String[] names = { "appros", "assault", "constructor", "fuel", "genius", "minelayer", "missile", "mobdca", "repair", "rocket", "scanner", "scout", "surveyor", "tank", "airtransport" };

    private static String[] files = { "SPLYTRCK", "ARTILLRY", "CONSTRCT", "FUELTRCK", "ENGINEER", "MINELAYR", "MISSLLCH", "ANTIAIR", "REPAIR", "ROCKTLCH", "SCANNER", "SCOUT", "SURVEYOR", "TANK", "AIRTRANS" };

    private String name;

    private String file;

    private int uid;

    public static final int UID_APPROS = 0;

    public static final int UID_ASSAULT = 1;

    public static final int UID_CONSTRUCTOR = 2;

    public static final int UID_FUEL = 3;

    public static final int UID_GENIUS = 4;

    public static final int UID_MINELAYER = 5;

    public static final int UID_MISSILE = 6;

    public static final int UID_MOBDCA = 7;

    public static final int UID_REPAIR = 8;

    public static final int UID_ROCKET = 9;

    public static final int UID_SCANNER = 10;

    public static final int UID_SCOUT = 11;

    public static final int UID_SURVEYOR = 12;

    public static final int UID_TANK = 13;

    public static final int UID_AIRTRANSPORT = 14;

    public static final int UID_MAX = 15;

    private UnitIdentifier() {
    }

    /**
	 * Creates an identifier for the type of unit given in argument.
	 * @param uid The type of unit the identifier belongs to.
	 */
    public UnitIdentifier(int uid) {
        if (list == null) initList();
        this.name = list.get(uid).name;
        this.file = list.get(uid).file;
        this.uid = uid;
    }

    private static void initList() {
        list = new Vector<UnitIdentifier>();
        list.setSize(UID_MAX);
        for (int i = 0; i < UID_MAX; i++) {
            UnitIdentifier u = new UnitIdentifier();
            u.name = names[i];
            u.file = files[i];
            u.uid = i;
            list.set(i, u);
        }
    }

    /**
	 * Returns the list of identifiers available.
	 * @return The list of identifiers.
	 */
    public static Vector<UnitIdentifier> getList() {
        if (list == null) initList();
        return list;
    }

    public String toString() {
        return new String(name).concat("(").concat(String.valueOf(uid)).concat(")");
    }

    /**
	 * @return The name of the unit.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return The MAX.RES identifier of the unit.
	 */
    public String getFile() {
        return file;
    }

    /**
	 * @return The integer representation of the indentifier.
	 */
    public int getUid() {
        return uid;
    }
}
