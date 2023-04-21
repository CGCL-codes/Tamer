package scamsoft.squadleader.rules.persistence;

import scamsoft.squadleader.rules.Transport;
import scamsoft.squadleader.rules.Unit;
import scamsoft.squadleader.rules.UnitGroup;

/**
 * User: Andreas Mross
 * Date: 06-Jul-2007
 * Time: 08:35:27
 */
public class CounterGroupUnitSource extends UnitSource {

    UnitGroup units;

    public CounterGroupUnitSource(UnitGroup units) {
        if (units == null) throw new NullPointerException();
        this.units = units;
    }

    public Unit getUnit(int id) {
        return units.getByID(id);
    }

    public Transport getTransport(int id) {
        return (Transport) units.getByID(id);
    }
}
