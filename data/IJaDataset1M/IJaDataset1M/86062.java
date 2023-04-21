package automenta.netention.client.property;

import automenta.netention.client.value.PropertyValue;
import automenta.netention.client.value.real.RealIs;

public class RealVar extends Property {

    private Unit unit;

    public RealVar() {
        super();
    }

    public RealVar(String id, String name) {
        this(id, name, Unit.Number);
    }

    public RealVar(String id, String name, Unit unit) {
        super(id, name);
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public boolean isInteger() {
        return false;
    }

    @Override
    public PropertyValue newDefaultValue() {
        RealIs r = new RealIs(0.0);
        r.setProperty(getID());
        return r;
    }

    public static Unit getUnit(String s) {
        if (s == null) return null;
        if (s.equalsIgnoreCase("mass")) return Unit.Mass;
        if (s.equalsIgnoreCase("volume")) return Unit.Volume;
        if (s.equalsIgnoreCase("area")) return Unit.Area;
        if (s.equalsIgnoreCase("distance")) return Unit.Distance;
        if (s.equalsIgnoreCase("currency")) return Unit.Currency;
        if (s.equalsIgnoreCase("number")) return Unit.Number;
        if (s.equalsIgnoreCase("speed")) return Unit.Speed;
        if (s.equalsIgnoreCase("timeduration")) return Unit.TimeDuration;
        if (s.equalsIgnoreCase("timepoint")) return Unit.TimePoint;
        return null;
    }
}
