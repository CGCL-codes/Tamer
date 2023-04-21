package edu.gsbme.msource.CellML.Utility;

import edu.gsbme.MMLParser2.MathML.MEE.Units.units;

/**
 * Baisc variable unit functionality
 * @author David
 *
 */
public class convertUnitStruct {

    String variable_id;

    units old_units;

    units new_units;

    public convertUnitStruct(String variable_id, units old_units, units new_units) {
        this.variable_id = variable_id;
        this.old_units = old_units;
        this.new_units = new_units;
    }

    public String getVariableName() {
        return variable_id;
    }

    public units getOldUnits() {
        return old_units;
    }

    public units getNewUnits() {
        return new_units;
    }

    public boolean check_unit_dim_equivalence() {
        return old_units.dim_equivalence(new_units);
    }

    public double get_new_conversion_ratio() {
        return new_units.getCoefficient() / old_units.getCoefficient();
    }

    public double get_old_conversion_ratio() {
        return old_units.getCoefficient() / new_units.getCoefficient();
    }
}
