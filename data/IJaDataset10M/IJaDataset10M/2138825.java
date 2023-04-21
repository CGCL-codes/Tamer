package gate.creole.measurements;

import java.util.List;

/**
 * A unit.
 */
class Unit extends Factor {

    /**
   * Construct object for unit 'nam' defined at 'loc'. The unit is defined by
   * string 'df'.
   */
    Unit(final String nam, final String df, MeasurementsParser gnuUnits) {
        super(nam, df, gnuUnits);
    }

    /**
   * Given is a line number 'lin' from units.dat file, parsed into name 'nam'
   * and definition 'df'. It should be a unit definition. Construct a Unit
   * object defined by the line, enter it into Units table, and return true.
   */
    static boolean accept(final String nam, final String df, Location loc, MeasurementsParser gnuUnits) {
        if ("23456789".indexOf(nam.charAt(nam.length() - 1)) >= 0) {
            System.err.println("Unit '" + nam + "' on line " + loc.lineNum + " ignored. It ends with a digit 2-9.");
            return true;
        }
        if ("0123456789".indexOf(nam.charAt(0)) >= 0) {
            System.err.println("Unit '" + nam + "' on line " + loc.lineNum + " ignored. It starts with a digit.");
            return true;
        }
        if (gnuUnits.units.containsKey(nam)) {
            System.err.println("Redefinition of unit '" + nam + "' on line " + loc.lineNum + " is ignored.");
            return true;
        }
        gnuUnits.units.put(nam, new Unit(nam, df, gnuUnits));
        return true;
    }

    @Override
    void check() {
        Measurement v = Measurement.fromString(name, gnuUnits);
        if (v == null || !v.isCompatibleWith(new Measurement(gnuUnits), Ignore.PRIMITIVE)) System.err.println("'" + name + "' defined as '" + def + "' is irreducible");
        if (gnuUnits.functions.containsKey(name)) System.err.println("unit '" + name + "' is hidden by function '" + name + "'");
    }

    /**
   * If unit defined by this object is compatible with Value 'v', add its
   * description to 'list'. Used in 'tryallunits'.
   */
    @Override
    void addtolist(final Measurement v, List<Entity> list) {
        Measurement thisvalue = Measurement.fromString(name, gnuUnits);
        if (thisvalue == null) return;
        if (thisvalue.isCompatibleWith(v, Ignore.DIMLESS)) insertAlph(list);
    }

    /**
   * Return short description of this object to be shown by 'tryallunits'.
   */
    @Override
    String desc() {
        return (isPrimitive ? "<primitive unit>" : "= " + def);
    }

    /**
   * Find out if 'name' is the name of a known unit, possibly in plural. Return
   * the Unit object if so, or null otherwise. (Originally part of
   * 'lookupunit'.)
   */
    static Unit find(final String name, MeasurementsParser gnuUnits) {
        if (gnuUnits.units.containsKey(name)) return gnuUnits.units.get(name);
        int ulg = name.length();
        if (ulg > 3 && name.charAt(ulg - 1) == 's') {
            String temp = name.substring(0, ulg - 1);
            if (gnuUnits.units.containsKey(temp)) return gnuUnits.units.get(temp);
            if (ulg > 4 && name.charAt(ulg - 2) == 'e') {
                temp = name.substring(0, ulg - 2);
                if (gnuUnits.units.containsKey(temp)) return gnuUnits.units.get(temp);
                if (ulg > 5 && name.charAt(ulg - 3) == 'i') {
                    temp = name.substring(0, ulg - 3) + "y";
                    if (gnuUnits.units.containsKey(temp)) return gnuUnits.units.get(temp);
                }
            }
        }
        return null;
    }
}
