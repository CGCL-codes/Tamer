package mySBML.primitiveTypes;

import java.util.ArrayList;
import java.util.HashMap;
import mySBML.SBase;

public class UnitSId extends SId {

    private static String[] idsArray = null;

    private static ArrayList<String> reservedIds = new ArrayList<String>();

    private static HashMap<String, UnitSId> map = new HashMap<String, UnitSId>();

    public static final UnitSId AMPERE = new UnitSId("ampere", map);

    public static final UnitSId BECQUEREL = new UnitSId("becquerel", map);

    public static final UnitSId CANDELA = new UnitSId("candela", map);

    public static final UnitSId COULOMB = new UnitSId("coulomb", map);

    public static final UnitSId DIMENSIONLESS = new UnitSId("dimensionless", map);

    public static final UnitSId FARAD = new UnitSId("farad", map);

    public static final UnitSId GRAM = new UnitSId("gram", map);

    public static final UnitSId GRAY = new UnitSId("gray", map);

    public static final UnitSId HENRY = new UnitSId("henry", map);

    public static final UnitSId HERTZ = new UnitSId("hertz", map);

    public static final UnitSId ITEM = new UnitSId("item", map);

    public static final UnitSId JOULE = new UnitSId("joule", map);

    public static final UnitSId KATAL = new UnitSId("katal", map);

    public static final UnitSId KELVIN = new UnitSId("kelvin", map);

    public static final UnitSId KILOGRAM = new UnitSId("kilogram", map);

    public static final UnitSId LITRE = new UnitSId("litre", map);

    public static final UnitSId LUMEM = new UnitSId("lumem", map);

    public static final UnitSId LUX = new UnitSId("lux", map);

    public static final UnitSId METRE = new UnitSId("metre", map);

    public static final UnitSId MOLE = new UnitSId("mole", map);

    public static final UnitSId NEWTON = new UnitSId("newton", map);

    public static final UnitSId OHM = new UnitSId("ohm", map);

    public static final UnitSId PASCAL = new UnitSId("pascal", map);

    public static final UnitSId RADIAN = new UnitSId("radian", map);

    public static final UnitSId SECOND = new UnitSId("second", map);

    public static final UnitSId SIEMENS = new UnitSId("siemens", map);

    public static final UnitSId SIEVERT = new UnitSId("sievert", map);

    public static final UnitSId STERADIAN = new UnitSId("steradian", map);

    public static final UnitSId TESLA = new UnitSId("tesla", map);

    public static final UnitSId VOLT = new UnitSId("volt", map);

    public static final UnitSId WATT = new UnitSId("watt", map);

    public static final UnitSId WEBER = new UnitSId("weber", map);

    private UnitSId(String id, HashMap<String, UnitSId> map) {
        super(null, id);
        map.put(id, this);
        reservedIds.add(id);
    }

    public UnitSId(SBase owner) {
        super(owner);
    }

    public UnitSId(SBase owner, String id) {
        super(owner, id);
    }

    public boolean isReserved() {
        return map.get(content) != null;
    }

    public static String[] getAllReservedUnidIds() {
        if (idsArray == null) {
            idsArray = new String[reservedIds.size()];
            for (int i = 0; i < reservedIds.size(); i++) idsArray[i] = reservedIds.get(i);
        }
        return idsArray;
    }
}
